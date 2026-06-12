# Primary Device Authority

**Status:** Architecture (June 2026)  
**Authority chain:** `phoenix-forge-home/registry/phoenix_forge_agency.yaml` → `docs/PHOENIX_FORGE_MASTER_SPEC.md` → this document  
**Scope:** How one human maps to one canonical Forge Profile, one primary device, and many read-only shells.

---

## Core law

```
One person  →  one canonical Forge Profile (UID)  →  one primary device  →  many shells
```

| Layer | Meaning | Mutates profile spine? |
|-------|---------|------------------------|
| **Person** | Josh or Ezra (human identity) | — |
| **Forge Profile** | Canonical `uid` in Room + `profiles/{uid}/` on disk | **Yes** (authority only) |
| **Primary device** | The one install that owns ingest, projection, and conflict resolution for that UID | **Yes** |
| **Shell** | Student Edition, Teacher Edition, Forge World, future apps on non-primary hardware | **No** (events + inbox append only) |

A shell never becomes a second source of truth for the same person. If two devices both believe they are primary, the suite is misconfigured.

---

## Operator device map (Josh household)

| Person | Role | Primary device | Primary APK | Shells on other devices |
|--------|------|----------------|-------------|-------------------------|
| **Josh** | Steward / parent | **Phone** (`josh-phone`) | Forge Profile (adult) + Teacher Edition | Laptop Godot (read-only), Crucible dev builds |
| **Ezra** | Student | **Tablet** (`ezra-tablet`) | Forge Profile (child) + Student Edition | — (World on laptop reads Ezra's PUBLIC state only) |

Rules:

1. **Ezra's canonical UID** is created once on his tablet Forge Profile (`UUID` via `ProfileBootstrap.createBlankProfile`). That UID is the only spine key under `profiles/{uid}/`.
2. **Josh's phone** holds an adult Forge Profile plus a **linked student row** pointing at Ezra's tablet UID — not a second Ezra profile on the phone unless explicitly marked as a debug sandbox (see `IDENTITY_CONTINUITY_AUDIT.md`).
3. **Teacher Edition on Josh's phone** is a shell: it writes LESSON manifests, PROTECTED notes, and inbox messages **to Ezra's UID folder**, never to a phone-local child UID.
4. **Forge World on laptop** is a shell: reads PUBLIC state and manifests; writes PUBLIC events with `actorApp: forge_world` and `actorDeviceId: godot-{install}`.

---

## Shell declaration (`authorityDeviceId`)

Every non-primary app **must declare** which device owns the profile it is serving at cold start.

### Required startup handshake

**No UUIDs or child names are hardcoded in apps.** Every field is resolved at runtime from Room, ContentProvider, or the sync spine. Docs use `{placeholders}` only.

```json
{
  "schemaVersion": 1,
  "shellAppId": "student_edition",
  "shellDeviceId": "{thisInstallDeviceId}",
  "authorityDeviceId": "{primaryDeviceIdForActiveUid}",
  "activeStudentUid": "{uuidFromForgeProfileRoomOrProvider}",
  "declaredAtEpochMs": 1749470400000
}
```

Example shape (illustrative — values change every install):

| Field | Example value | Never compile-time constant |
|-------|---------------|---------------------------|
| `shellDeviceId` | `tablet-a1b2c3` | Generated once per install |
| `authorityDeviceId` | same as shell on primary; tablet id when phone is shell | Read from linked-student row or device registry |
| `activeStudentUid` | `f4b1376b-9afc-459d-b84d-8b69116597ed` | `ProfileBootstrap` UUID at profile creation |

| Field | Rule |
|-------|------|
| `shellDeviceId` | This install's stable device id (`StudentDeviceIdStore`, `TeacherDeviceIdStore`, `user://device_id.txt` in Godot) |
| `authorityDeviceId` | Primary device id for the **active** `activeStudentUid` — from `.device_registry/` or linked-student metadata |
| `activeStudentUid` | **Runtime only:** active Forge Profile row `uid`, ContentProvider, or `ForgeSyncClient.activeStudentUid()` |
| `shellAppId` | `forge_profile` \| `student_edition` \| `teacher_edition` \| `forge_world` |

### Dynamic resolution (fresh install / profile deleted)

| Situation | Behavior |
|-----------|----------|
| **Fresh tablet install** | Forge Profile creates new `UUID` → becomes `activeStudentUid`; declaration written on first boot |
| **Profile deleted / reset** | New UUID; old sync folder is orphaned until steward re-links; apps must not reuse cached UID from prefs |
| **Steward re-links child** | Teacher reads linked `profileUid` from Forge Profile Students table — not from docs or env vars |
| **No active profile** | Shell declares `activeStudentUid: null`; read-only or gate UI — **no writes** to `profiles/` |
| **Godot / laptop** | Read `profile.uid` from imported `forge_profile_push.json` or LAN `GET /api/v1/ping`; never mint `local-{name}` UIDs |

Nothing in the suite should import Josh or Ezra's UUID from markdown. Handoff docs reference **procedures**, not identity constants.

### Enforcement

| Check | Pass | Fail (circuit breaker) |
|-------|------|------------------------|
| Shell on primary device | `shellDeviceId == authorityDeviceId` | Allow full read/write via ContentProvider + sync |
| Shell on secondary device | `shellDeviceId != authorityDeviceId` | **Read** PUBLIC + LESSON via sync/LAN; **write** append-only events/inbox to `profiles/{uid}/`; **never** open Room authority or import a competing profile row |
| Teacher on Josh's phone | `authorityDeviceId` must equal Ezra's tablet id from linked student row | Block manifest/inbox write if resolved UID is `FORGE_PROFILE_CHILD` on phone |
| Missing declaration | — | Treat as **non-trustworthy shell**: read-only PUBLIC, no writes |

Shell declaration is written to:

```
/PhoenixForge/sync/.device_registry/{shellDeviceId}.json
```

and mirrored in local app prefs for offline boot.

---

## Circuit breaker (apps never talk to each other)

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│  Teacher    │     │  Student    │     │ Forge World │
│  Edition    │     │  Edition    │     │   (Godot)   │
└──────┬──────┘     └──────┬──────┘     └──────┬──────┘
       │                   │                   │
       │    ╳ direct APK-to-APK intents ╳     │
       │    ╳ shared DataStore / Room   ╳     │
       │                   │                   │
       └───────────────────┼───────────────────┘
                           ▼
              ┌────────────────────────┐
              │   Forge Profile APK    │  ← only authority on primary device
              │   (Room + projection)  │
              └───────────┬────────────┘
                          │
                          ▼
              ┌────────────────────────┐
              │  /PhoenixForge/sync/     │  ← Syncthing transport
              │  profiles/{uid}/         │
              └────────────────────────┘
```

**Allowed edges**

| From | To | Mechanism |
|------|-----|-----------|
| Shell | Forge Profile (same device) | `content://com.phoenixforge.profile.provider/*` |
| Shell | Sync spine | Append `EVT_*.json`, `inbox/MSG_*.json`, read `public_state.json` |
| Shell | Forge Profile (LAN) | `GET /api/v1/profile/public/{uid}`, `POST /api/v1/event` — enhancement only |
| Teacher | Ezra's UID folder | Write manifests + inbox files **scoped to linked UID** |

**Forbidden edges**

| Pattern | Why |
|---------|-----|
| Student Edition ↔ Teacher Edition intents | Bypasses scope guards and creates dual writers |
| Godot → Teacher Edition | No Gradle bridge; file contract only |
| Shell opens another shell's Room DB | Split brain |
| Shell creates a new `ForgeProfile` UUID for an already-linked person | UID duplication (see audit doc) |

When the circuit trips (e.g. Teacher resolves `FORGE_PROFILE_CHILD` instead of `FORGE_PROFILE_LINKED`), UI must show **non-trustworthy** status — already partially implemented in `StudentConnectionStatus.kt`.

---

## `DeviceProfilePolicy` — current and target rules

**Today** (`forge-profile-core/.../DeviceProfilePolicy.kt`): governs which **profile roles** may be created on a device (child-only tablet vs adult-capable phone with Teacher Edition installed).

**Target extension** (architecture; not all enforced in code yet):

| Rule ID | Condition | Policy |
|---------|-----------|--------|
| DPP-01 | `teacherEditionInstalled == true` | Device is **adult-capable**; may create `STEWARD_FOR_STUDENT` + link external child UIDs |
| DPP-02 | No Teacher + no adult profile | **Child-only device**; may create `STUDENT_SELF` only |
| DPP-03 | `STUDENT_SELF` active on phone that also has Teacher | Show warning: "This is a local test profile, not Ezra's tablet" |
| DPP-04 | `ProfileImportPolicy` | Student Edition may import **only** `STUDENT_SELF` rows — never steward/teacher rows |
| DPP-05 | Primary declaration | Forge Profile on tablet sets `isPrimaryForUid: true` in device registry for Ezra's UID |
| DPP-06 | Secondary declaration | Josh phone registry sets `isPrimaryForUid: false`, `linkedStudentUids: [ezraUid]` |
| DPP-07 | Debug sandbox | Profile with `profileTag: debug` cannot link to production roster or share sync folder with production UID |

### Creatable roles (unchanged logic)

```kotlin
// Child tablet, no Teacher app
creatableProfileRoles → [STUDENT_SELF]

// Josh phone with Teacher Edition
creatableProfileRoles → [STEWARD_FOR_STUDENT, STUDENT_SELF]
```

`STUDENT_SELF` on Josh's phone is permitted for **debug/playtest** but must never become the UID that Teacher Edition pushes manifests to unless `ManifestPushTargetResolver` explicitly selects a linked tablet UID.

---

## Primary vs secondary write matrix

| Data | Primary (Ezra tablet) | Secondary (Josh phone) | Shell (Godot laptop) |
|------|----------------------|------------------------|----------------------|
| Room profile row | Read/write | Adult row only; child via link | None |
| `events/EVT_*.json` | Ingest + append | Append (scoped) | Append PUBLIC only |
| `inbox/MSG_*.json` | Ingest + read | Append TO_STUDENT | None |
| `manifests/` | Read | Write LESSON | Read |
| `public_state.json` | Project + write | Read | Read |
| PROTECTED notes | Read (authority) | Write via Teacher | **Never** |

---

## Boot order (canonical)

1. Read `.device_registry/{deviceId}.json` — confirm primary vs shell.
2. Load active Forge Profile UID from Room (primary) or ContentProvider / linked row (secondary).
3. Emit shell declaration if non-primary.
4. Open sync spine at `profiles/{uid}/`.
5. Start LAN server **only** on primary Forge Profile service.
6. Shell apps: subscribe to `public_state.json` + inbox ingests via Forge Profile provider.

---

## Related documents

- `docs/PHOENIX_FORGE_MASTER_SPEC.md` — event schema, sync tree, scope law
- `docs/INBOX_ARCHITECTURE.md` — parent → child message path
- `docs/IDENTITY_CONTINUITY_AUDIT.md` — UID duplication gaps and remediation
- `docs/PLUGIN_CONTRACT.md` — `ForgeSyncClient` send-end plugin
