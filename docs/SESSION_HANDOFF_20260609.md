# Session handoff — Classroom lane (2026-06-09, refreshed)

**For:** Cursor window on `phoenix-forge-classroom`  
**Operator:** Josh · wireless adb connected on Galaxy  
**Policy:** No Ezra reveal until **"Ezra reveal"**.

---

## Read first (every session)

1. **`docs/STASH_AND_SESSION_HANDOFF_20260611.md`** — stash restore, no hardcoded UUIDs
2. `docs/PRIMARY_DEVICE_AUTHORITY.md` · `docs/INBOX_ARCHITECTURE.md` · `docs/IDENTITY_CONTINUITY_AUDIT.md`
3. `docs/PHOENIX_FORGE_MASTER_SPEC.md`
4. `docs/HANDOFF_1_02.md`
5. Cross-lane World stash: `../phoenix-forge-world` → `git stash list` → `world-polish-ezra-session-2026-06-10`

---

## Done on disk (latest APK install includes most of this — confirm on phone)

### Path B — Lesson manifest (1.01–1.03) ✅

Teacher push + Student Today's Expedition. Path:  
`/sdcard/PhoenixForge/sync/profiles/{studentUid}/manifests/lesson_manifest_{yyyy-MM-dd}.json`

### Forge Profile UX ✅ (2026-06-09)

| Change | Notes |
|--------|-------|
| **My adult profile** vs **Child / student profile** | Parallel profiles, not "create account for student" |
| Adult dashboard | **Open Teacher Edition** only — no avatar push on adult |
| Child dashboard | Push snapshot + Open Student Edition + Profile UUID |
| Parent gate removed | No toggle, no TeacherGate screen |
| Teacher launch fix | `<queries>` + explicit MainActivity intent (no Play Store chooser) |
| `DeviceProfilePolicy` | Adult phone (Teacher installed): create adult + child; child tablet: child only |
| Student import | Child profile only — adult blocked |

### Cross-app sync spine (`:forge-sync-client`) ✅ on disk

| Module / file | Role |
|---------------|------|
| `forge-sync-client/` | **Send-end plugin** — paths, `PublicSyncWriter`, `ForgeSyncClient`, messages |
| `docs/PLUGIN_CONTRACT.md` | Single contract for all Forge Profile expansion apps |
| Student `EventWriter.kt` | Events via `ForgeSyncClient.writeEvent` |
| Student `MessageWriter.kt` | Steward replies via `ForgeSyncClient.writeMessage` |
| Teacher `ManifestWriter.kt` + `MessageRelayCoordinator.kt` | Manifests + steward→student messages |
| `ForgeSyncModule.kt` (student + teacher) | Hilt wiring per APK |

**Deprecated removed from app modules:** duplicate `PublicSyncWriter`, `ManifestSyncPaths`, `LogicalClockStore`, `ForgeEventModels`.

Events: `QUEST_STARTED`, `QUEST_COMPLETED` · scope PUBLIC · path spec-aligned.  
Messages: optional `TO_STUDENT` / `TO_STEWARD` under `{uid}/messages/MSG_*.json`.

---

## Not done (spec order)

| Priority | Task | Ref |
|----------|------|-----|
| **1.04** | `EventFileWatcher` + ingest → `forge_events` + timeline (`HANDOFF_1_04.md`) | ✅ on disk |
| **Next** | `StateProjector` + NanoHTTPD `:7433` | P1 |
| Then | Forge Profile P1 — `StateProjector`, NanoHTTPD `:7433`, mDNS | P1 |
| Then | Export/backup profiles (survive reinstall) | P1 |
| Deferred | PROTECTED writers, full Teacher roster timeline | P4 |

---

## Phone setup (recreate if data cleared)

1. **Josh** → My adult profile → Open Teacher Edition → Expedition Board → Push today's stack  
2. **Test child** → Child profile → note UUID → Push snapshot (child only)  
3. **Student Edition** → Import child profile only  
4. **Today's Expedition** → Start mission → events under `.../events/`

```bash
STUDENT_UID="<child-uuid>"
adb shell ls "/sdcard/PhoenixForge/sync/profiles/${STUDENT_UID}/events/"
```

**Data survival:** use `adb install -r`. Do not uninstall first.

---

## Build + install (Crucible — agent runs this when adb is up)

```bash
export JAVA_HOME="$HOME/.local/jdk"
export PATH="$JAVA_HOME/bin:$PATH"
cd /var/lib/phoenix-ai/workspace/phoenix-forge-classroom/phoenix-forge-classroom-forge-profile

./gradlew :forge-profile-app:assembleDebug :teacher-app:assembleDebug :student-app:assembleDebug

ADB=$(adb devices | awk '/device$/{print $1; exit}')
adb -s "$ADB" install -r forge-profile-app/build/outputs/apk/debug/forge-profile-app-debug.apk
adb -s "$ADB" install -r teacher-app/build/outputs/apk/debug/teacher-app-debug.apk
adb -s "$ADB" install -r student-app/build/outputs/apk/debug/student-app-debug.apk
```

Unit tests:
```bash
./gradlew :forge-profile-core:testDebugUnitTest :student-app:testDebugUnitTest
```

---

## Laws

- No Gradle deps between APKs — JSON files only  
- PROTECTED never on Student  
- `{studentUid}` = child UUID for all sync paths  
- Teacher pushes daily stack; child pushes avatar snapshot  

---

## Parallel lane

**World** owns Godot only. When EventWriter schema changes, notify World for parity.

---
## Autonomous run 2026-06-10T15:03:46-07:00
- **Goal:** Plan TeacherNavGraph navigation for new expedition flow
- **Verify:** FAIL
- Navigation plan for expedition sub-graph, modal vs route split, Sage→Board handoff, and 3.51+ routes

---
## Autonomous run 2026-06-10T15:12:28-07:00
- **Goal:** Design compose screen plan for student quest hub
- **Verify:** FAIL
- Compose architecture for Quest Hub: hub cards, mission detail sheet, QuestEngine merge, Today dedup

---
## Autonomous run 2026-06-10T15:14:45-07:00
- **Goal:** Architect navigation graph for digital home tabs
- **Verify:** FAIL
- Nested digital_home graph, inner tab bar, room routes, bottom bar consolidation plan

---
## Autonomous run 2026-06-10T15:21:10-07:00
- **Goal:** Plan TeacherNavGraph navigation for new expedition flow
- **Verify:** FAIL
- Expedition-centric nav plan: nested hub, Sage/planner handoffs, modal vs route split

---
## Autonomous run 2026-06-10T15:26:17-07:00
- **Goal:** Expedition nav handoffs P0
- **Verify:** FAIL
- Sage and Lesson Planner navigate to Expedition Board and tile detail

---
## Autonomous run 2026-06-10T15:47:27-07:00
- **Goal:** Implement QuestHubScaffold and QuestMissionSheet in QuestsScreen
- **Verify:** FAIL
- Split quest hub into scaffold cards + mission bottom sheet

---
## Autonomous run 2026-06-10T15:53:02-07:00
- **Goal:** Wire SideQuestStrip QuestEngine into quest hub 0.69
- **Verify:** FAIL
- QuestEngine active quests on hub strip with side mission sheet

---
## Autonomous run 2026-06-10T15:59:19-07:00
- **Goal:** Quest status pills and Today tab consolidation
- **Verify:** FAIL
- Mission status chips on cards; Today route aliases to Quests daily category

---
## Autonomous run 2026-06-11T02:48:56-07:00
- **Goal:** stabilize QuestEngine 0.69
- **Verify:** PASS
- QuestEngine start/complete lifecycle, SIDE-only strip filter, refresh without full-screen flash, QuestEngineLogicTest, ChariotExport asset load fix

---
## Autonomous run 2026-06-11T07:28:13-07:00
- **Goal:** forge profile syncthing plugins account connection ezra
- **Verify:** PASS
- Fixed student compile; child dashboard Profile ID copy; forge-sync-client plugin wired; APKs on Ezra tablet

---
## Autonomous run 2026-06-11T11:12:49-07:00
- **Goal:** unify cross-app forge-sync spine
- **Verify:** PASS
- Consolidated send-end sync into :forge-sync-client; Teacher+Student messaging; removed duplicate app-module writers; PLUGIN_CONTRACT + handoff updated; verify-all PASS
