# Phoenix Forge Classroom
Offline-first homeschool OS for Galaxy S24 Ultra.
Standalone apps enhanced by Sovereign Deck / Phoenix when running.

This repo is split into two APK targets:

- `phoenix-forge-classroom-teacher-edition`: teacher/admin app for planning, assigning, reviewing, and managing homeschool records.
- `phoenix-forge-classroom-student-edition`: student app for lessons, games, assignments, progress, and offline learning.

## Repo
Private: https://github.com/dragonbornjedi-code/phoenix-forge-classroom

## Workspace
/var/lib/phoenix-ai/workspace/phoenix-forge-classroom

## Layout
- `phoenix-forge-classroom-teacher-edition/`: teacher-facing Android app.
- `phoenix-forge-classroom-student-edition/`: student-facing Android app.
- `shared/`: shared contracts, AIDL, schemas, and common learning data models.
- `registry/`: content, lesson, game, and capability registry data.
- `scripts/`: workspace automation.
- `docs/`: repo-wide design notes.

## Integration
Sovereign Deck reads data via ContentProvider.
Phoenix creates lesson plans via AIDL tool call.
