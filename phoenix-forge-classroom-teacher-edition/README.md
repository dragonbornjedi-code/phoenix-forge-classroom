# Phoenix Forge Classroom Teacher Edition

Teacher/admin APK for homeschool planning and oversight.

**Runnable code:** `../phoenix-forge-classroom-forge-profile/teacher-app` (Gradle module `:teacher-app`).  
**Roadmap:** [docs/roadmaps/03_TEACHER_EDITION_ROADMAP.md](../docs/roadmaps/03_TEACHER_EDITION_ROADMAP.md)

## Scope
- Create and manage lesson plans.
- Assign lessons, games, quizzes, and projects.
- Review student progress and completion history.
- Manage homeschool records, schedules, and offline sync.
- Use Phoenix/Sovereign Deck integration for lesson generation and automation.
- Keep adult-only behavioral insight, curriculum planning, and adaptation notes.

## Planning Docs
- `docs/curriculum-of-life.md`: seven-section homeschool framework (canonical pedagogy).
- `docs/CURRICULUM_ATOMIZATION_GUIDE.md`: how to decompose patterns into skill atoms without editing canonical curriculum.
- `docs/curriculum-taxonomy.md`: expanded top-tier categories and subcategories.
- `../docs/contracts/CURRICULUM_OS_SCHEMA.md`: operational data model (capabilities, atoms, signals, Compass).
- `../docs/contracts/CURRICULUM_RUNTIME_FLOW.md`: runtime pipeline and authority boundaries (curriculum → chronicle loop).
- `docs/teaching-methods-research.md`: evidence-informed teaching method library.
- `docs/subcategory-method-map.md`: teaching methods and starter lesson directions for every subcategory.
- `docs/inclusive-learning-supports.md`: flexible supports for attention, sensory, transition, regulation, and communication needs.
- `docs/lesson-activity-development-pipeline.md`: template and process for building lessons/activities for every subcategory.
- `docs/thematic-playthroughs.md`: story-driven lesson groupings for engaging multi-day arcs.
- `docs/starter-lessons-pack-01.md`: first detailed lesson pack with Student Edition mission translations.
- `docs/education-research-map.md`: framework and resource map for evidence-informed curriculum development.
- `docs/system-initialization.md`: five-minute daily launch routine.
- `docs/teacher-edition-product-spec.md`: tabs, student profiles, cross-app sync, daily itinerary, and lesson-plan views.
- `docs/teacher-edition-feature-backlog.md`: adult-only app feature backlog.

## Android (runnable MVP)

The installable APK is built from the shared Gradle monorepo (separate repo folder for docs, same build tree as Forge Profile):

```bash
export JAVA_HOME=/home/joshuar/.local/jdk
cd ../phoenix-forge-classroom-forge-profile
./gradlew :teacher-app:assembleDebug
```

APK: `phoenix-forge-classroom-forge-profile/teacher-app/build/outputs/apk/debug/teacher-app-debug.apk`  
Package: `com.phoenixforge.classroom.teacher`

The empty `android/` folder here is reserved for a future standalone module; use `teacher-app` only.

**Implemented in `teacher-app` (2026-06-04):** Expedition Board, create tile, field guide (materials/coaching/evidence), save details, mark completed, Room persistence. **Next:** P1c drag reorder · P2 send tile to Student Edition.
