# Handoff 1.04 — Event ingest loop (Forge Profile)

**Master ref:** P1 steps 2–3 · `PHOENIX_FORGE_MASTER_SPEC.md`

## Architecture (important)

| App | Role |
|-----|------|
| **Student Edition** | **Writes** `EVT_*.json` to sync folder (quests, future routines) |
| **Forge World** | **Writes** `EVT_*.json` (level up, companion, lesson) |
| **Forge Profile** | **Watches + ingests** into Room (`forge_events` + childhood timeline) |

Student Edition does **not** duplicate ingest — Forge Profile is the childhood OS aggregator on the steward phone/tablet.

## Sync path

```
/sdcard/PhoenixForge/sync/profiles/{studentUid}/events/EVT_*.json
```

`{studentUid}` = child profile UUID (e.g. Ezra's `f4b1376b-…`).

## Components (forge-profile-core)

| Class | Role |
|-------|------|
| `EventFileWatcher` | FileObserver on events dir |
| `EventIngester` | Parse JSON → `forge_events` table |
| `EventTimelineProjector` | Mirror ingested events → `timeline_events` |
| `EventIngestCoordinator` | Start watchers per child profile; rescan on app foreground |

Boot: `ForgeProfileApp.onCreate()` → `eventIngestCoordinator.start()`

## Scopes ingested

- `PUBLIC` ✅ (quests, level up, companion, …)
- `LESSON` ✅ (e.g. LESSON_STARTED from Forge World)
- `PROTECTED` ❌ skipped (Teacher / adult only)

## Verify on phone

1. Ezra child profile active in Forge Profile
2. Student Edition → Today's Expedition → **Start mission**
3. Forge Profile → **Timeline** — should show "Started quest · …"
4. Or adb:

```bash
STUDENT_UID="f4b1376b-9afc-459d-b84d-8b69116597ed"
adb shell ls "/sdcard/PhoenixForge/sync/profiles/${STUDENT_UID}/events/"
```

5. Open Forge Profile (foreground) — triggers `rescanAll()` if watcher missed a file

## Tests

```bash
./gradlew :forge-profile-core:testDebugUnitTest --tests "*.ForgeEventParserTest" --tests "*.EventIngesterTest"
```

## Next (P1 remainder)

- `StateProjector` full public state replay (not just timeline labels)
- `ForgeProfileSyncServer` NanoHTTPD `:7433`
- Profile export/backup so UUID survives reinstall
