# Local Childhood Ledger

Offline replacement for Ezra Quest Google Sheets (34 tabs). **No Google APIs at runtime.**

## Principles

1. **Append-only event logs** as NDJSON (same pattern as `emotion_checkins.ndjson`)
2. **Structured tables** in Room (Android Student/Teacher)
3. **CSV export** for parent backup when cloud/Sheets unavailable
4. **Manual file handoff** between surfaces until loop spine 2.01–2.07 proven
5. **Computed views** (Daily Flags, compass sunlight) derived from events — not authoritative

## Critical sheet mapping

| Legacy sheet | NDJSON / table | Writer | Readers |
|--------------|----------------|--------|---------|
| Quest Log | `quest_log.ndjson` | Student, Teacher | Profile timeline, Teacher compass |
| XP Ledger | `xp_ledger.ndjson` | Student | Teacher snapshot (read-only) |
| Gold Tracker | `gold_ledger.ndjson` | Student | Teacher steward view |
| Emotional Log | `emotion_checkins.ndjson` | Student | Teacher, Godot DailyFlagEngine |
| Daily Flags | *(computed)* | DailyFlagEngine | Teacher, Godot HUD |
| Quest Library | Teacher curriculum JSON | Teacher | Student quests |
| Weekly Schedule | Teacher expedition board (Room) | Teacher | — |

## File drop paths (Forge World / manual sync)

| File | Android path | Godot path |
|------|--------------|------------|
| `emotion_checkins.ndjson` | `PhoenixForge/import/` | `user://import/` |
| `quest_log.ndjson` | `PhoenixForge/import/` | read-only fixture |
| `forge_profile_push.json` | share / export | `user://import/` |

## Schemas

- `schemas/quest_log_event.schema.json`
- `schemas/xp_ledger_event.schema.json`
- `schemas/gold_ledger_event.schema.json`
- `schemas/daily_brief.schema.json`

## Manifest

`manifests/ledger_tables.yaml` — old sheet name → schema + storage target.

## Kotlin / GDScript helpers (phased)

| Phase | Surface | Module |
|-------|---------|--------|
| 3 | Student | `QuestLogWriter.kt` (append) |
| 3 | Teacher | CSV export + snapshot read |
| 1+ | Godot | read-mostly fixtures + future import |
