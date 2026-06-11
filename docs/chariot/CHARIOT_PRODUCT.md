# Chariot — Smart Car Companion (product lane)

**Detachable branch** per `phoenix-forge-world/docs/SUITE_MAP.md`. Parallel to Sovereign car path; childhood OS handoff via file only.

## What Chariot is

Offline car companion: weather-matched SAGE sessions, human voice clips, car-seat-safe activities. Pi Zero / Kia Soul head unit targets.

## What Classroom owns

| Piece | Location |
|-------|----------|
| Session authority | `shared/chariot/sessions/sage-sessions-salvage.json` |
| Export builder | `ChariotExport.kt` (`expedition_tiles` \| `sage_session`) |
| Stack path | `/sdcard/PhoenixForge/Chariot/quest-stack.json` |
| ADB push | `scripts/chariot-adb-helper.sh push-stack` |

## Audio

60 quest mp3 in `phoenix-forge-world/assets/audio/quests/` — **same clips** Forge World uses via `quest_voice_manifest.json`. Ezra hears consistent voice in car and at home.

## Pi golden image (not in git)

Master SD image (cold storage):

```text
~/Downloads/chariot-golden-master-2026-02-19.img.gz
```

Crucible holds production copies. Never commit `.img.gz` to git.

## Ops

```bash
./scripts/chariot-cleanup.sh          # disk audit on Pi/Termux
./scripts/chariot-cleanup.sh --clean  # safe cache clear
./scripts/validate-chariot-sessions.py
```

## Sellable product scope (trimmed from 2026-02-18 plan)

- Offline-first SD card kit — Pi product lane, not Classroom Tier A
- Body Weather session matching — **ported** to `shared/chariot/`
- Parent progress — via `quest_log.ndjson` + Teacher expedition board (not HA)
- OBD telemetry tabs — Pi dashboard only; optional detachable branch

Full product enhancement notes: archived from `~/Downloads/2026-02-18-chariot-product.md` (2026-06 port).
