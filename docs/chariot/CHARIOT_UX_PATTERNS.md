# Chariot UX patterns (salvaged from chariot-loj.html)

Pattern reference only — **not** merged into Classroom or Godot runtime. Life of Josh branding stays out of childhood OS.

## Boot screen

- Full-screen overlay on load (~2s), cyan scanline header, progress bar fill animation
- Fades out before main UI — maps to `forge_world_boot.py` / future Student Chariot boot

## Body Weather tab

- Weather-matched quest sessions (`body_weather_flags` in `shared/chariot/sessions/`)
- Aligns with DailyFlagEngine day_status + `emotion_palette.yaml`
- Sunny = challenge; cloudy = gentle activation; rainy/stormy/frozen = co-regulation

## Bottom navigation

| Tab | Car-safe role |
|-----|----------------|
| Telemetry | OBD stub — Pi product only, detachable |
| Quests | SAGE session missions — **Classroom export target** |
| Music | SD-card playlists — not Tier A Classroom |
| Parent | Progress snapshot — Teacher reads `quest_log.ndjson` |

## Car-seat constraints

- No standing, no large motor movements — isometric squeeze, wiggle, marching in seat
- Parent co-pilot required for spelling, emotion, creative narration
- Weather check steps have zero XP — regulation checkpoint, not punishment

## Classroom handoff

Teacher Expedition Board → `ChariotExport` (`sage_session` mode) → `quest-stack.json` → `chariot-adb-helper.sh push-stack`
