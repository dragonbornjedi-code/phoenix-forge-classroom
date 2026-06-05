# Deployment Reality (Field Test)

**Last verified:** 2026-06-05 (build + install; phone UI re-check after device restart)  
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
| `adb devices` shows phone | Pass | Wireless adb `adb-R5CXB09D32N-expfjf._adb-tls-connect._tcp` (Samsung SM_S928U); unset stale `ANDROID_SERIAL` |
| Forge Profile installs | Pass | `./scripts/install-phone-apks.sh` 2026-06-05 |
| Forge Profile launches | Pass | Bottom nav: Home, Studio, Timeline, Memories; Identity card via stat grid |
| Taps navigate (not static dashboard) | Pass | All bottom-nav tabs + dashboard stat cards (step 0.27–0.29) |
| Student Edition installs | Pass | Same install script |
| Student Edition launches | Pass | Home shows **Phoenix Forge Classroom Student Edition** |
| Teacher Edition installs | Pass | Same install script |
| Teacher Edition launches | Pass | Curriculum landing → Expedition Board; seed + created tiles |
| Import Forge Profile (Student, optional) | Pass | More → Import Forge Profile (Optional) reads provider |
| Student Companions hub (4.63) | Install OK | More → **Companions** — Spark / Whisps / Pet Space zones; re-verify UI after reboot |
| Teacher Lesson Planner (4.64–4.65) | Build OK | Curriculum → **Lesson Planner** → subdomain → Add to Expedition Board |
| Teacher Sage Advisor (4.66–4.68) | Build OK | Curriculum → **Sage Advisor** → encrypted API key → online chat w/ curriculum context |

Failures: `adb logcat -s AndroidRuntime:E` · Wireless adb: `unset ANDROID_SERIAL` · If `offline`: `adb kill-server && adb start-server`

**After device restart:** `adb devices` → `./scripts/install-phone-apks.sh` → spot-check Companions + Lesson Planner + Sage settings.

---

## Deployment matrix (update by hand)

| App | Builds? | On parent phone? | On Ezra tablet? | Cross-app wired? |
|-----|---------|------------------|-----------------|------------------|
| Forge Profile | Yes | **Pass** (debug, Joshua rowland teacher profile) | Not tested | Provider `/profile` + `/avatar` registered (shell Permission Denial = OK) |
| Student Edition | Yes | **Pass** (debug) | Not tested | Import-only provider read; no live Teacher feed |
| Teacher Edition | Yes | **Pass** (debug) | N/A | Curriculum + Lesson Planner + Sage (online); expedition board; not wired to Student quests |

**Current bottleneck:** after reboot — re-verify Companions hub (4.63) on phone → prove one Intent Tile loop → Ezra tablet.

---

## Related

- [roadmaps/00_MASTER_ROADMAP.md](roadmaps/00_MASTER_ROADMAP.md)
- [REPOSITORY_CENSUS_AND_CONNECTIONS.md](REPOSITORY_CENSUS_AND_CONNECTIONS.md)
- [PHOENIX_FORGE_SYSTEM_ATLAS.md](PHOENIX_FORGE_SYSTEM_ATLAS.md)
