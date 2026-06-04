# Phoenix Forge Classroom Student Edition

Student APK for lessons, learning games, assignments, and offline study.

**Runnable code:** `../phoenix-forge-classroom-forge-profile/student-app` (Gradle module `:student-app`).  
**UX specs:** `docs/` in this folder.  
**Roadmap:** [docs/roadmaps/02_STUDENT_EDITION_ROADMAP.md](../docs/roadmaps/02_STUDENT_EDITION_ROADMAP.md)

## Scope
- Complete assigned lessons and activities.
- Play learning games tied to subjects and skill goals.
- Track progress locally for teacher review.
- Work offline on the Galaxy S24 Ultra.
- Sync with Teacher Edition through shared local contracts when available.
- Show child-facing missions, games, rewards, and simple progress only.

## Product Boundary
- [docs/STUDENT_TEACHER_BOUNDARY.md](../docs/STUDENT_TEACHER_BOUNDARY.md): what belongs in Student Edition and what stays adult-only.

## Android (runnable)

All Kotlin lives in the shared monorepo — not the empty `android/` folder here:

```bash
export JAVA_HOME=/home/joshuar/.local/jdk
cd ../phoenix-forge-classroom-forge-profile
./gradlew :student-app:assembleDebug
```

APK: `phoenix-forge-classroom-forge-profile/student-app/build/outputs/apk/debug/student-app-debug.apk`  
Package: `com.phoenixforge.student`
