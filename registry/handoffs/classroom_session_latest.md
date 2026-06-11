# Classroom session — 2026-06-10T23:01:00-07:00

**Lane:** phoenix-forge-classroom · **Step:** 0.68 · **Agency:** Phoenix Forge Agency

## Route bootstrap
```bash
./scripts/classroom-context-pack.sh "<goal>"
./scripts/classroom-delegation-verify.py
```

## Verify cache
FAIL shell-verify 2026-06-10T20:21:49-07:00 — export JAVA_HOME=/home/joshuar/.local/jdk; export PATH="$JAVA_HOME/bin:$PATH"; cd /var/lib/phoenix-ai/workspace/phoenix-forge-classroom/phoenix-forge-classroom-forge-profile && ./gradlew :forge-profile-app:assembleDebug :student-app:assembleDebug --no-daemon --continue 2>&1 | tee /tmp/build-$(date +%H%M).log

## adb
GN434J024265051W	device

## Git
```
branch: main
head: 805fbfe
 M AGENTS.md
 M docs/content/quests/README.md
 M docs/roadmaps/00_MASTER_ROADMAP.md
 M phoenix-forge-classroom-forge-profile/forge-profile-app/build.gradle.kts
 M phoenix-forge-classroom-forge-profile/forge-profile-app/src/main/AndroidManifest.xml
 M phoenix-forge-classroom-forge-profile/forge-profile-app/src/main/java/com/phoenixforge/profile/ForgeProfileApp.kt
 M phoenix-forge-classroom-forge-profile/forge-profile-app/src/main/java/com/phoenixforge/profile/data/export/ProfileManualPushExporter.kt
 M phoenix-forge-classroom-forge-profile/forge-profile-app/src/main/java/com/phoenixforge/profile/ui/dashboard/DashboardScreen.kt
 M phoenix-forge-classroom-forge-profile/forge-profile-app/src/main/java/com/phoenixforge/profile/ui/gate/ProfileAppShell.kt
 M phoenix-forge-classroom-forge-profile/forge-profile-app/src/main/java/com/phoenixforge/profile/ui/gate/ProfileGateViewModel.kt
 M phoenix-forge-classroom-forge-profile/forge-profile-app/src/main/java/com/phoenixforge/profile/ui/gate/ProfileSignInScreen.kt
 M phoenix-forge-classroom-forge-profile/forge-profile-app/src/main/java/com/phoenixforge/profile/ui/interop/ExternalApps.kt
 M phoenix-forge-classroom-forge-profile/forge-profile-app/src/main/java/com/phoenixforge/profile/ui/navigation/ProfileNavigation.kt
 M phoenix-forge-classroom-forge-profile/forge-profile-app/src/main/java/com/phoenixforge/profile/ui/students/StudentsScreen.kt
 D phoenix-forge-classroom-forge-profile/forge-profile-app/src/main/java/com/phoenixforge/profile/ui/teacher/TeacherGate.kt
 D phoenix-forge-classroom-forge-profile/forge-profile-app/src/main/java/com/phoenixforge/profile/ui/teacher/TeacherViewModel.kt
 M phoenix-forge-classroom-forge-profile/forge-profile-app/src/main/java/com/phoenixforge/profile/ui/theme/Theme.kt
 M phoenix-forge-classroom-forge-profile/forge-profile-app/src/main/java/com/phoenixforge/profile/ui/timeline/ChildhoodTimelineScreen.kt
 M phoenix-forge-classroom-forge-profile/forge-profile-core/build.gradle.kts
 M phoenix-forge-classroom-forge-profile/forge-profile-core/src/main/java/com/phoenixforge/profile/data/local/ProfileDatabase.kt
 M phoenix-forge-classroom-forge-profile/forge-profile-core/src/main/java/com/phoenixforge/profile/data/local/dao/ProfileDao.kt
 M phoenix-forge-classroom-forge-profile/forge-profile-core/src/main/java/com/phoenixforge/profile/data/local/entity/ProfileEntity.kt
 M phoenix-forge-classroom-forge-profile/forge-profile-core/src/main/java/com/phoenixforge/profile/data/provider/ProfileContentProvider.kt
 M phoenix-forge-classroom-forge-profile/forge-profile-core/src/main/java/com/phoenixforge/profile/data/provider/ProfileContract.kt
 M phoenix-forge-classroom-forge-profile/forge-profile-core/src/main/java/com/phoenixforge/profile/data/provider/ProfileExportDto.kt
 M phoenix-forge-classroom-forge-profile/forge-profile-core/src/main/java/com/phoenixforge/profile/data/provider/ProfileExportReader.kt
 M phoenix-forge-classroom-forge-profile/forge-profile-core/src/main/java/com/phoenixforge/profile/data/repository/ProfileRepositoryImpl.kt
 M phoenix-forge-classroom-forge-profile/forge-profile-core/src/main/java/com/phoenixforge/profile/di/ProfileModule.kt
 M phoenix-forge-classroom-forge-profile/forge-profile-core/src/main/java/com/phoenixforge/profile/domain/bootstrap/ProfileBootstrap.kt
 M phoenix-forge-classroom-forge-profile/forge-profile-core/src/main/java/com/phoenixforge/profile/domain/copy/AppBoundaryCopy.kt
```

## Handoff excerpt
# Session handoff — Classroom lane (2026-06-09, refreshed)

**For:** Cursor window on `phoenix-forge-classroom`  
**Operator:** Josh · wireless adb connected on Galaxy  
**Policy:** No commits unless Josh asks. No Ezra reveal until **"Ezra reveal"**.

---

## Read first (every session)

1. `docs/PHOENIX_FORGE_MASTER_SPEC.md`
2. `docs/HANDOFF_1_02.md`
3. This file
4. Cross-lane: `../phoenix-forge-world/docs/SESSION_HANDOFF_20260609.md`

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

### Student EventWriter (P2) ✅ on disk

| File | Role |
|------|------|
| `EventWriter.kt`, `ForgeEventModels.kt`, `LogicalClockStore.kt` | Append `EVT_*.json` |
| `EventWriterLogicTest.kt` | 5 tests pass |
| Today's Expedition UI | Start mission / Mark complete |

Events: `QUEST_STARTED`, `QUEST_COMPLETED` · scope PUBLIC · path spec-aligned.

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

## Forbidden
- teacher-edition/android · student-edition/android build trees
- Adult profile → Student import
- PROTECTED writers on Student
- Godot edits (World lane) unless bridged
