# Deployment Reality (Field Test)

**Last verified:** 2026-06-04  
**Build host:** `/var/lib/phoenix-ai/workspace/phoenix-forge-classroom`  
**Naming (canonical):** **Forge Profile** · **Phoenix Forge Classroom Student Edition** · **Phoenix Forge Classroom Teacher Edition**

Architecture docs do not install apps. This document does.

---

## Canonical app names vs Gradle modules

| Canonical name | Gradle module | Folder docs | Package ID |
|----------------|---------------|-------------|------------|
| **Forge Profile** | `:forge-profile-app` + `:forge-profile-core` | `phoenix-forge-classroom-forge-profile/` | `com.phoenixforge.profile` |
| **Phoenix Forge Classroom Student Edition** | `:student-app` | `phoenix-forge-classroom-student-edition/docs/` (UX specs) | `com.phoenixforge.student` |
| **Phoenix Forge Classroom Teacher Edition** | `:teacher-app` | `phoenix-forge-classroom-teacher-edition/docs/` (pedagogy) | `com.phoenixforge.classroom.teacher` |

**Do not use** “Digital House” as a product name — that was an internal codename; launcher label is **Phoenix Forge Classroom Student Edition**.

---

## What actually builds today

| App | Module | APK path | Status |
|-----|--------|----------|--------|
| **Forge Profile** | `:forge-profile-app` | `phoenix-forge-classroom-forge-profile/forge-profile-app/build/outputs/apk/debug/forge-profile-app-debug.apk` | **BUILD OK** |
| **Phoenix Forge Classroom Student Edition** | `:student-app` | `phoenix-forge-classroom-forge-profile/student-app/build/outputs/apk/debug/student-app-debug.apk` | **BUILD OK** |
| **Phoenix Forge Classroom Teacher Edition** | `:teacher-app` | `phoenix-forge-classroom-forge-profile/teacher-app/build/outputs/apk/debug/teacher-app-debug.apk` | **BUILD OK** (MVP shell) |

```bash
export JAVA_HOME=/home/joshuar/.local/jdk
cd phoenix-forge-classroom-forge-profile
./gradlew :forge-profile-app:assembleDebug :student-app:assembleDebug :teacher-app:assembleDebug
```

Or: `./scripts/install-phone-apks.sh [adb-serial]` (build + install).

**Tests:** No automated test suite in repo. Verification = build + manual device checklist below.

---

## Target device layout

| Device | Who | Apps | Purpose |
|--------|-----|------|---------|
| **Parent phone** | You | Forge Profile + Teacher Edition | Steward identity + daily expedition board |
| **Ezra tablet** | Student | Forge Profile + Student Edition | Optional identity import + child experience |

**Mini PC** = build machine only.

---

## Install order (same device)

1. **Forge Profile** first (ContentProvider for Student import).
2. **Student Edition** or **Teacher Edition** (any order after profile).

```bash
SERIAL=YOUR_ADB_SERIAL   # adb devices
BASE=phoenix-forge-classroom-forge-profile
adb -s $SERIAL install -r $BASE/forge-profile-app/build/outputs/apk/debug/forge-profile-app-debug.apk
adb -s $SERIAL install -r $BASE/student-app/build/outputs/apk/debug/student-app-debug.apk
adb -s $SERIAL install -r $BASE/teacher-app/build/outputs/apk/debug/teacher-app-debug.apk
```

---

## Field test checklist (parent phone first)

| Check | Pass? | Notes |
|-------|-------|-------|
| `adb devices` shows phone | | |
| Forge Profile installs | | |
| Forge Profile launches | | Bottom nav: Home, Studio, Timeline, Memories, Steward |
| Taps navigate (not static dashboard) | | Reinstall after 2026-06-04 nav fix |
| Student Edition installs | | |
| Student Edition launches | | Home shows **Phoenix Forge Classroom Student Edition** |
| Teacher Edition installs | | |
| Teacher Edition launches | | Sample expedition tiles (MVP shell) |
| Import Forge Profile (Student, optional) | | Settings → Import |

Failures: `adb logcat -s AndroidRuntime:E`

---

## Deployment matrix (update by hand)

| App | Builds? | On parent phone? | On Ezra tablet? | Cross-app wired? |
|-----|---------|------------------|-----------------|------------------|
| Forge Profile | Yes | ? | ? | Provider read by Student (import only) |
| Student Edition | Yes | ? (debug) | ? | No live Teacher feed yet |
| Teacher Edition | Yes (shell) | ? | N/A | Not wired to Student/Profile yet |

**Current bottleneck:** stabilize three APKs on parent phone → prove one Intent Tile loop → then Ezra tablet.

---

## Related

- [roadmaps/00_MASTER_ROADMAP.md](roadmaps/00_MASTER_ROADMAP.md)
- [REPOSITORY_CENSUS_AND_CONNECTIONS.md](REPOSITORY_CENSUS_AND_CONNECTIONS.md)
- [PHOENIX_FORGE_SYSTEM_ATLAS.md](PHOENIX_FORGE_SYSTEM_ATLAS.md)
