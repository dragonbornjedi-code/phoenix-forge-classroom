# Chariot car companion — shared contracts

**Product lane:** detachable branch per `SUITE_MAP.md`. Offline SD-card / Kia Soul head unit. Not Tier A Classroom runtime dependency.

## Handoff flow

```text
Teacher Expedition Board
  → ChariotExport (expedition_tiles | sage_session)
  → quest-stack.json
  → Student ChariotDeckScreen / adb push-stack
  → same quest_voice_manifest clips as Forge World
```

## Authority

| Artifact | Path |
|----------|------|
| Weather sessions (8) | `sessions/sage-sessions-salvage.json` |
| Audio path map | `audio_path_map.json` |
| Live mp3 (60) | `phoenix-forge-world/assets/audio/quests/` |
| Manifest categories | `phoenix-forge-world/data/audio/quest_voice_manifest.json` |
| Stack path on car | `/sdcard/PhoenixForge/Chariot/quest-stack.json` |

## Voice IDs (no franchise NPCs)

| Legacy name | voice_id |
|-------------|----------|
| Brave Bear | `steward_physical` |
| Owl Sage | `steward_cognitive` |
| Spark Fox | `steward_creative` |
| Gentle Giant / Calm Chameleon | `steward_emotional` |
| Wise Raccoon | `steward_practical` |
| Narrator | `ignavarr` |

## Tools

```bash
python3 scripts/port-sage-sessions.py          # re-port from ~/Downloads if needed
python3 scripts/validate-chariot-sessions.py   # schema + audio on disk
./scripts/chariot-adb-helper.sh push-stack FILE.json
```

## Pi golden image (not in git)

Cold storage: `~/Downloads/chariot-golden-master-2026-02-19.img.gz` on Crucible.

## Godot alignment

Chariot sessions and Forge World quests share `quest_voice_manifest.json` categories. Weather clips (`sunny.mp3`, `cloudy.mp3`, …) can map to `DailyFlagEngine` day_status for realm mood — optional stub, not wired in Godot HTML.
