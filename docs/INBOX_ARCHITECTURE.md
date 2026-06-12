# Inbox Architecture

**Status:** Architecture (June 2026)  
**Authority:** `docs/PHOENIX_FORGE_MASTER_SPEC.md` (inbox addendum) + `docs/PLUGIN_CONTRACT.md`  
**Transport:** Syncthing (offline-first) · Forge Profile ingest (primary) · ContentProvider (same device)

---

## Purpose

Let a parent send a short note that appears on the child's device as a warm, kid-safe card — **without** Student Edition talking to Teacher Edition directly.

```
Josh (Teacher Edition, phone)  →  inbox file  →  Forge Profile (ingest)  →  Student Edition card
```

Example UX copy: **"New from Dad ✨"** on the Student home / inbox surface.

---

## Path law (canonical)

```
/PhoenixForge/sync/profiles/{studentUid}/inbox/MSG_{fromDevice}_{epochMs}.json
```

| Segment | Rule |
|---------|------|
| `{studentUid}` | Canonical child UID from tablet Forge Profile — **never** a phone-local debug UID |
| `inbox/` | Append-only message folder (profile-scoped) |
| `MSG_{fromDevice}_{epochMs}.json` | Globally unique filename; `fromDevice` = writer's stable device id |

**Note:** Current implementation uses `messages/` instead of `inbox/` (`ManifestSyncPaths.messagesRelativePath`). Migration is a rename + ingester path update — see `IDENTITY_CONTINUITY_AUDIT.md` gap I-07.

Optional sequence suffix (`_0001`) is permitted for burst writes on the same millisecond; filenames must remain immutable after write.

---

## Message schema

```json
{
  "schemaVersion": 1,
  "messageId": "MSG_josh-phone_1749470400000",
  "threadId": "steward-ezra",
  "direction": "TO_STUDENT",
  "fromDeviceId": "josh-phone",
  "fromDisplayName": "Dad",
  "toStudentUid": "{studentUid}",
  "targetApp": "student_edition",
  "epochMs": 1749470400000,
  "logicalClock": 88,
  "subject": "Great job yesterday",
  "bodyMarkdown": "Finish literacy before Minecraft.",
  "readEpochMs": null,
  "replyToMessageId": null
}
```

### Field rules

| Field | Writer | Reader |
|-------|--------|--------|
| `direction` | `TO_STUDENT` (parent→child) or `TO_STEWARD` (child reply) | Filter by direction per app |
| `fromDeviceId` | Must match writer's `TeacherDeviceIdStore` / `StudentDeviceIdStore` | Display + dedup |
| `toStudentUid` | Must match linked tablet UID | Forge Profile rejects mismatch |
| `targetApp` | `student_edition` for parent notes; `forge_profile` or `teacher_edition` for replies | Student ignores non-student targets |
| `bodyMarkdown` | Plain markdown subset; no PROTECTED assessment content | Rendered in inbox card + detail |
| `readEpochMs` | Set by Student on open (writes `MESSAGE_READ` event) | Hide "new" badge |

**Scope:** Inbox content is **not** PROTECTED. Behavioral notes stay in Teacher PROTECTED events. Inbox is child-visible by design.

---

## Writers and readers

| Role | Writer? | Reader? | Path |
|------|---------|---------|------|
| **Teacher Edition** (Josh phone) | **Yes** — primary author of `TO_STUDENT` | `TO_STEWARD` replies | Append to `profiles/{studentUid}/inbox/` |
| **Student Edition** (Ezra tablet) | `TO_STEWARD` replies only | **Yes** — inbox UI + home card | Via Forge Profile ContentProvider + local ingest |
| **Forge Profile** (Ezra tablet) | No (ingest authority) | **Yes** — indexes all messages for UID | `MessageIngester` → Room |
| **Forge World** (Godot) | No | Optional read for "Today" brief modal | `public_state` / brief compile only |
| **Teacher Edition** | Reads replies after ingest | Pull from provider or sync folder |

### Write flow (parent → child)

```
1. Teacher Edition: MessageRelayCoordinator.sendToStudent()
2. Resolve toStudentUid via ManifestPushTargetResolver → linked tablet UID
3. Append MSG_*.json under profiles/{uid}/inbox/
4. Optional LAN POST → Forge Profile :7433/api/v1/messages/{uid}
5. Syncthing replicates inbox/ to tablet
6. Forge Profile MessageIngester watches folder → Room
7. Student Edition ProfileMessageReader → InboxViewModel → "New from Dad ✨"
```

### Read flow (child sees card)

```
1. Student app launch or pull-to-refresh
2. Query content://com.phoenixforge.profile.provider/.../messages?direction=TO_STUDENT
3. Fallback: read profiles/{uid}/inbox/*.json from sync root
4. Surface newest unread (readEpochMs == null) on home + Inbox tab
5. On open: emit MESSAGE_READ PUBLIC event; set readEpochMs in derived state
```

---

## Offline-first guarantees

| Property | Guarantee |
|----------|-----------|
| **Offline write** | Teacher can write MSG file on phone with no LAN; Syncthing delivers later |
| **Offline read** | Student reads last ingested messages from Forge Profile Room without network |
| **Append-only** | Never edit or delete MSG files; compaction archives to snapshot if needed |
| **Profile-scoped** | Messages live under `{studentUid}` — no global inbox |
| **No cross-app RPC** | Teacher does not call Student APK; only files + provider on same device |

---

## Visibility and parenting rules

| Content type | Child can see? | Parent can see? |
|--------------|----------------|-----------------|
| `TO_STUDENT` inbox message | **Yes** | Yes (sent copy + thread) |
| `TO_STEWARD` reply | No (parent views in Teacher) | **Yes** |
| PROTECTED behavioral note | **Never** | Yes (Teacher only) |
| LESSON manifest narrative | Yes (assigned days) | Yes |

Parent messages must not embed PROTECTED diagnostics. Teacher UI should offer templates ("Proud of you", "Try the dragon quest") separate from assessment notes.

---

## Cross-house replication (Syncthing)

```
Phone (steward)                           Tablet (child primary)
profiles/{studentUid}/inbox/MSG_*.json ──► profiles/{studentUid}/inbox/MSG_*.json
        │                                          │
        └──────────── Syncthing ───────────────────┘
```

Requirements:

1. Both devices share the `phoenix-forge-sync` folder.
2. **Same `{studentUid}`** on both sides — established via Forge Profile → Students → link tablet Profile ID (runtime, not hardcoded).
3. Ignore rules must **not** exclude `inbox/` for the child's UID.
4. Conflict-free: unique `MSG_{fromDevice}_{epochMs}` filenames.

LAN shortcut: if tablet Forge Profile is on Wi‑Fi, `ForgeProfileLanMessagesClient` POSTs the same JSON for faster ingest — Syncthing remains source of truth.

### Can Forge Profile own the sync folder? (vs external Syncthing-Fork)

**Yes — architecturally, three tiers (pick per device):**

| Tier | Model | Pros | Cons |
|------|--------|------|------|
| **A — App-owned spine** | Forge Profile uses app-specific storage or SAF-picked folder; writes `profiles/{uid}/` directly; optional embedded sync library later | One APK, child can't break path; inbox/events always under authority | Full Syncthing in APK is heavy; v1 uses companion app |
| **B — Companion Syncthing-Fork** (current) | Shared `/sdcard/PhoenixForge/sync/`; Forge Profile watches via `EventFileWatcher` | Battle-tested, no GPL embed in Classroom APK | Two apps to install; user must wire folder once |
| **C — Hybrid** | Forge Profile declares canonical `syncRoot` in `.device_registry`; Syncthing-Fork syncs **that** path only | Single config pointer; Profile remains authority | Still two APKs until embedded sync ships |

**Target:** Tier C now → Tier A when `forge-profile-core` ships an optional `SyncRootManager` (SAF URI persisted in Room, exposed to `:forge-sync-client`). Teacher/Student/Godot read `syncRoot` from ContentProvider — **never** hardcode `/sdcard/PhoenixForge/sync/`.

Until embedded sync exists: install Syncthing-Fork once, point at the folder Forge Profile already writes to. Inbox files are just JSON under `profiles/{studentUid}/inbox/` — no separate message database required.

---

## Home card UX contract

| State | Student UI |
|-------|--------------|
| New `TO_STUDENT`, `readEpochMs == null` | Home banner: **"New from {fromDisplayName} ✨"** → tap opens inbox detail |
| No unread | No banner; Inbox tab still lists history |
| First launch after 06:00 | May combine with `daily_brief` items (`kind: MESSAGE`) — see master spec daily brief schema |

---

## Event sidecar (optional but recommended)

| eventType | scope | Writer | When |
|-----------|-------|--------|------|
| `MESSAGE_SENT` | LESSON | Teacher / Student | After MSG file written |
| `MESSAGE_READ` | PUBLIC | Student | Child opens message |
| `DAILY_BRIEF_PUBLISHED` | LESSON | Teacher | When brief compiled from manifest + pinned messages |

Events go to `profiles/{uid}/events/` — separate from inbox files. Forge Profile projector may denormalize unread counts into `public_state.json`.

---

## Implementation map (current code)

| Component | Path | Role |
|-----------|------|------|
| `MessageRelayCoordinator` | `teacher-app/.../MessageRelayCoordinator.kt` | Teacher write orchestration |
| `MessageWriter` | `student-app/.../MessageWriter.kt` | Student reply |
| `DefaultForgeSyncClient.writeMessage` | `forge-sync-client/...` | Append to sync folder |
| `MessageIngester` | `forge-profile-core/.../MessageIngester.kt` | Authority ingest |
| `InboxViewModel` | `student-app/.../InboxViewModel.kt` | Student UI |
| `ForgeProfileSyncServer` | `forge-profile-core/.../ForgeProfileSyncServer.kt` | LAN `/api/v1/messages/{uid}` |

**Gap:** folder name `messages/` vs canonical `inbox/` — track in identity audit.

---

## Related documents

- `docs/PRIMARY_DEVICE_AUTHORITY.md` — who may write to which UID
- `docs/IDENTITY_CONTINUITY_AUDIT.md` — linked UID vs phone-local child
- `docs/PHOENIX_FORGE_MASTER_SPEC.md` — sync tree, scope law, daily brief
