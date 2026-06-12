# Identity Continuity Audit

**Status:** Architecture audit (June 2026)  
**Goal:** Enumerate every place UID duplication or profile conflation can occur, and prescribe remediation.  
**Household rule:** Phone `ezra-debug` (or any steward-side test profile) must **never** share a UID or sync spine with production Ezra on tablet.

---

## Canonical identity spine

```
One Ezra  →  one UUID  →  profiles/{uuid}/  →  all apps reference same studentUid
```

| Identity | Canonical source | Example |
|----------|------------------|---------|
| Child UID | `ProfileBootstrap.createBlankProfile` on **primary** Forge Profile | New `UUID` per profile — **never** a constant in source code |
| Josh steward UID | `ProfileBootstrap` on **phone** Forge Profile (adult role) | separate UUID |
| Device ids | Per-install stable strings | `ezra-tablet`, `josh-phone`, `godot-182736` |
| Debug Ezra | Explicit `profileTag: debug` + **new UUID** on phone only | `ezra-debug` ≠ production UID |

---

## Gap register

### I-01 — Godot avatar creator mints `local-{forgeName}` UIDs

| | |
|--|--|
| **Location** | `phoenix-forge-world/scripts/autoload/forge_import.gd` → `build_push_bundle()` |
| **Behavior** | `uid := "local-%s" % child_forge_name.to_lower()` for both `profile.uid` and `avatar.childId` |
| **Risk** | Godot-created identity diverges from tablet UUID; events and `character_config.json` land under `profiles/local-ezra/` instead of `profiles/{realUid}/` |
| **Remediation** | Avatar creator must **never** mint UIDs. On confirm: read active UID from imported `forge_profile_push.json` or require steward push first. If no UID, block save with "Create profile on tablet first." Remove `local-*` generation from production paths; keep only in headless fixtures with `fixture-ezra` label. |

---

### I-02 — Fixture UID `fixture-ezra` bleeds into dev workflows

| | |
|--|--|
| **Locations** | `import/fixtures/sample_forge_profile_push.json`, `sample_lesson_manifest.json`, `sample_character_config.json`; `scripts/sync/public_state_reader.gd` hardcoded fallback |
| **Behavior** | When no live profile loaded, World reads/writes `fixture-ezra` paths |
| **Risk** | Agents and playtests report PASS while bound to fixture spine, not Ezra's real UID; conflates test data with production on Syncthing if copied to sync folder |
| **Remediation** | Fixtures OK for CI only when `PHOENIX_FORGE_NO_FIXTURE=1` on steward devices. Remove hardcoded `fixture-ezra` fallback in `public_state_reader.gd` — fail closed. Document fixture UID in verify scripts only. |

---

### I-03 — `childId` vs `profile.uid` dual keys in push bundle

| | |
|--|--|
| **Locations** | `forge_profile_push.json` schema: `profile.uid` and `avatar.childId`; World `ForgeImport.student_uid()` reads `profile.uid` only |
| **Behavior** | Steward push can carry both fields; drift if one is updated without the other |
| **Risk** | Student Edition or chronicle readers use `childId` while events use `studentUid` from profile — split spine |
| **Remediation** | Single canonical field: `profile.uid`. Treat `avatar.childId` as deprecated alias; validate equality on ingest in Forge Profile. Add schema test in `forge-profile-core`. |

---

### I-04 — Phone-local child profile vs linked tablet UID (Teacher push target)

| | |
|--|--|
| **Locations** | `ChildStudentUidResolver.resolve()` priority order; `ManifestPushTargetResolver`; `StudentConnectionStatus.kt` |
| **Behavior** | Resolver prefers **linked** students, then **local child on phone**, then Student Edition import |
| **Risk** | Josh creates/debugs child "Ezra" on phone → Teacher pushes manifests/messages to phone UID → tablet never receives them |
| **Remediation** | **Enforce PRIMARY_DEVICE_AUTHORITY:** Teacher write paths require `ChildUidSource.FORGE_PROFILE_LINKED` with `isTrustworthy == true`. Block manifest/inbox write when source is `FORGE_PROFILE_CHILD` or `STUDENT_EDITION`. UI already warns — add hard gate in `ManifestPushCoordinator` + `MessageRelayCoordinator`. |

---

### I-05 — `rememberStudentUid` SharedPreferences cache

| | |
|--|--|
| **Locations** | `ChildStudentUidResolver.rememberStudentUid()` / `peekRememberedStudentUid()`; prefs key `manifest_push_student_uid` |
| **Behavior** | Caches last pushed UID for diagnostics |
| **Risk** | Stale UID misleads operator; was historically used as implicit target |
| **Remediation** | Keep cache **diagnostic only** (current `peekRemembered` + `REMEMBERED_PUSH` source). Never use remembered UID in `resolve()`. Add "Clear stale cache" to Teacher settings (partially documented in `StudentConnectionStatus`). |

---

### I-06 — `ProfileBootstrap` UUID per create on any device

| | |
|--|--|
| **Locations** | `forge-profile-core/.../ProfileBootstrap.createBlankProfile()` |
| **Behavior** | Every "Create profile" tap mints `UUID.randomUUID()` |
| **Risk** | Multiple "Ezra" profiles across devices with different UUIDs — legitimate for debug, fatal if mistaken for same person |
| **Remediation** | UI: require `forgeName` + role + **profileTag** (`production` \| `debug`). Debug profiles: orange badge, excluded from roster link export. Production child: show "Copy Profile ID" prominently once. `DeviceProfilePolicy` DPP-07 (see PRIMARY_DEVICE_AUTHORITY.md). |

---

### I-07 — Inbox folder name drift (`messages/` vs `inbox/`)

| | |
|--|--|
| **Locations** | `ManifestSyncPaths.messagesRelativePath` → `{uid}/messages`; architecture target → `{uid}/inbox/` |
| **Behavior** | Working code writes to `messages/` |
| **Risk** | Docs, Syncthing ignore rules, and cross-repo agents disagree on path; inbox ingest misses files |
| **Remediation** | Rename to `inbox/` in `ManifestSyncPaths`, `MessageDiskWriter`, `MessageIngester`, Forge Profile watcher. One-time migration: copy `messages/*.json` → `inbox/` on sync spine. Update `PLUGIN_CONTRACT.md` and master spec consistently. |

---

### I-08 — Godot `device_id` independent of Android registry

| | |
|--|--|
| **Locations** | `forge_import.gd` → `user://device_id.txt` (`godot-{randi}`) |
| **Behavior** | Godot mints its own actor device id for events |
| **Risk** | Not tied to `.device_registry/` or `authorityDeviceId` declaration; hard to correlate with tablet events in timeline |
| **Remediation** | On laptop: write `shell_declaration.json` beside sync root with `authorityDeviceId: ezra-tablet`. Godot reads declaration and sets `actorDeviceId` to shell id while tagging events with `authorityDeviceId` field (schema v2). Register `godot-{id}` in `.device_registry/`. |

---

### I-09 — `character_config.json` mirror uses bundle UID at save time

| | |
|--|--|
| **Locations** | `character_config.gd` → `_mirror_to_sync()` uses `studentUid` from saved config |
| **Behavior** | Godot avatar creator saves config with UID from `build_push_bundle` or active bundle |
| **Risk** | If bundle has `local-ezra`, mirror writes to wrong folder; tablet Student never sees config |
| **Remediation** | Save config only when `profile.uid` matches linked production UID (read from imported push). Reject `local-*` prefixes in `CharacterConfig.save_config()`. |

---

### I-10 — Student Edition import without role guard at file layer

| | |
|--|--|
| **Locations** | `ProfileImportPolicy` (Kotlin); Student pull from ContentProvider |
| **Behavior** | Policy rejects non-`STUDENT_SELF` roles in app logic |
| **Risk** | Future code path bypasses policy if raw JSON import added |
| **Remediation** | Centralize import through `ProfileImportPolicy.selectImportableProfile()` only. Add instrumented test: steward profile pull → rejected. |

---

### I-11 — Auto-link child to adult on same device

| | |
|--|--|
| **Locations** | `ProfileBootstrap.autoLinkChildToAdultOnDevice()` |
| **Behavior** | Creating child on phone auto-links to adult profile on phone |
| **Risk** | Reinforces phone-local Ezra as "linked" student — Teacher may think connection is trustworthy |
| **Remediation** | Auto-link only when `profileTag == debug` OR explicit "This is a test profile on this device" confirm. Production link must be **manual paste of tablet Profile ID** (cross-device). |

---

### I-12 — Event `studentUid` mismatch not enforced on all writers

| | |
|--|--|
| **Locations** | `EventIngester` (enforces); Godot `write_event()` (uses bundle uid); Teacher `ForgeSyncClient` |
| **Behavior** | Ingester quarantines mismatch; writers may still emit wrong UID to disk |
| **Risk** | Orphan event files under wrong folder pollute Syncthing |
| **Remediation** | All writers validate UID against active profile before append. Godot: refuse `write_event` if `student_uid()` empty or starts with `local-`. |

---

### I-13 — Roster `roster.json` vs linked students in Room

| | |
|--|--|
| **Locations** | Master spec `roster/roster.json`; Forge Profile linked students table |
| **Behavior** | Two roster representations possible |
| **Risk** | Teacher shows roster entry UID ≠ linked UID used for push |
| **Remediation** | Single source: Room linked students on phone; `roster.json` is export snapshot written by Forge Profile, not hand-edited. |

---

## Debug profile isolation matrix

| Profile | Device | UID | Sync folder | May link to Teacher push? |
|---------|--------|-----|-------------|---------------------------|
| Production Ezra | Tablet | UUID-1 | `profiles/UUID-1/` | **Yes** |
| Debug Ezra | Josh phone | UUID-2 (different) | `profiles/UUID-2/` or isolated `.stignore` | **No** — local play only |
| `fixture-ezra` | CI / Godot headless | `fixture-ezra` | Not on production Syncthing | **No** |
| `local-ezra` (Godot minted) | Laptop | `local-ezra` | **Bug** — must not ship | **No** — remove generator |

**Enforcement checklist for `ezra-debug`:**

1. `profileTag: debug` in Room row on phone.
2. Distinct `forgeName` suffix optional ("Ezra (debug)").
3. Syncthing: exclude `profiles/{debugUid}/` from household share OR use separate debug sync folder.
4. Teacher Edition: `isTrustworthy == false` for any non-`FORGE_PROFILE_LINKED` source.
5. Never paste debug Profile ID into Students link field.

---

## Remediation priority

| Priority | Gap | Effort | Owner lane |
|----------|-----|--------|------------|
| **P0** | I-01 Godot `local-*` UID minting | Small | World |
| **P0** | I-04 Teacher write gate on linked UID only | Small | Classroom |
| **P0** | I-09 character_config mirror guard | Small | World |
| **P1** | I-07 inbox → `inbox/` rename | Medium | Classroom |
| **P1** | I-06 profileTag debug vs production | Medium | Forge Profile |
| **P1** | I-11 auto-link guard | Small | Forge Profile |
| **P2** | I-08 Godot authorityDeviceId declaration | Medium | World + ops |
| **P2** | I-02 fixture fallback removal on devices | Small | World |
| **P2** | I-03 childId deprecation | Small | Classroom + World |
| **P3** | I-13 roster single source | Medium | Forge Profile |

---

## Verification signals (no false PASS)

| Check | Pass means |
|-------|------------|
| Teacher Student tab | `FORGE_PROFILE_LINKED` + trustworthy |
| Tablet `profiles/{uid}/events/` | Events carry same `studentUid` as Forge Profile Room |
| Phone push test | New manifest appears under **tablet UID** folder after Syncthing |
| Godot playtest | No `local-*` UID in `write_event` or `character_config.json` |
| Debug phone profile | Different UUID; Teacher cannot push manifests |

---

## Related documents

- `docs/PRIMARY_DEVICE_AUTHORITY.md`
- `docs/INBOX_ARCHITECTURE.md`
- `docs/PLUGIN_CONTRACT.md`
- `docs/PHOENIX_FORGE_MASTER_SPEC.md`
