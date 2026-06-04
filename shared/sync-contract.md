# Cross-App Sync Contract

**Forge Profile** remains the identity and avatar owner. This document covers **Teacher ↔ Student** pedagogical sync only. Profile/avatar handoff uses ContentProvider + JSON export ([FORGEPROFILE_SPEC.md](../docs/FORGEPROFILE_SPEC.md), [shared/README.md](README.md)).

Teacher Edition and Student Edition are separate APKs with controlled local sync.

## Direction Names

Use these names in the app:

- Import from Student: Teacher Edition receives Student Edition data.
- Export to Student: Teacher Edition sends assignments, schedule blocks, missions, and settings to Student Edition.

## Link Model

Teacher Edition should create or approve a student link before exchanging data.

Link fields:
- Student ID.
- Teacher app install ID.
- Student app install ID.
- Device label.
- Link created time.
- Last seen time.
- Allowed data scopes.
- Shared protocol version.
- Revoked flag.

## Export To Student

Teacher Edition sends:
- Student profile basics needed for child-facing personalization.
- Assigned missions.
- Lessons converted to short child-facing tasks.
- Game/activity queue.
- Simple day schedule.
- Reward goals.
- Visual prompts.
- Approved content IDs.
- App limits/settings.

Teacher Edition must not export:
- Private teacher notes.
- Behavioral interpretation.
- Adult planning rationale.
- Safety/medical notes unless explicitly needed for Student Edition UX.

## Import From Student

Student Edition sends:
- Mission completion events.
- Scores.
- Attempts.
- Time spent.
- Hint usage.
- Retry count.
- Emotional check-ins.
- Energy check-ins.
- Break/help requests.
- Student reflections.
- Badge progress.
- Offline event logs.
- Blocked task reports.

## Conflict Rules

- Teacher Edition is authoritative for assignments, schedules, lesson plans, and student profile settings.
- Student Edition is authoritative for raw completion, score, and check-in events it generated.
- Imported Student Edition data should append as events instead of overwriting teacher notes.
- Duplicate events should be ignored by stable event ID.
- Manual conflict review should be available when two teacher devices later exist.

## Transport Options

Preferred local-first options:
- ContentProvider for structured local reads.
- AIDL service for explicit app-to-app calls.
- File export/import for backup or manual transfer.
- QR pairing or local code for link setup.

Future optional options:
- Nearby/local network sync.
- Encrypted cloud backup.

## Privacy Defaults

- Local-first.
- Adult-only data stays in Teacher Edition.
- Child-facing data stays simple in Student Edition.
- Exports are explicit.
- Backups are encrypted before leaving the device.
