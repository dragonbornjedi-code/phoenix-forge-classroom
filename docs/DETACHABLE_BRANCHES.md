# Detachable branches — Phoenix Forge childhood OS

**Source pattern:** `EZRA_QUEST_ARCHITECTURE.md` § "EVERY BRANCH IS DETACHABLE"  
**Rule:** Each optional capability registers in a manifest. Boot probes availability; missing branch = graceful degrade, never crash.

Aligned by contract, never dependent at runtime.

---

## Philosophy (legacy → new)

| Legacy (Ezra Quest) | Phoenix Forge | Detached fallback |
|---------------------|---------------|-------------------|
| Google Sheets (34 tabs) | **Local Childhood Ledger** (Room + NDJSON + CSV export) | Parent marks tile complete manually |
| Apps Script triggers | File-drop + in-app events (`forge_profile_push.json`, `emotion_checkins.ndjson`) | No auto morning summary |
| Home Assistant LED/TTS | `real_world_gadgets.json` optional branch | In-app celebration only |
| Piper TTS / Dad recordings | **Voice branch** (`shared/voice/`) | Text subtitle only |
| Faster Whisper STT | **Listen branch** (sidecar, Phase 4) | Tap-to-respond buttons |
| Wii / PS5 | **Input branch** (`ps5_input.gd`, future Wii) | Touch controls |
| n8n webhooks | Ops mesh Tier C only — **never P0** | Manual export |
| Godot 3D world | Forge World | Student 2D shell |
| React dashboard | Teacher Edition expedition board | — |

---

## Branch registry locations

| Branch type | Manifest | Runtime probe |
|-------------|----------|---------------|
| Voice | `shared/voice/voice_actor_manifest.json` | Clip exists → play; else Piper stub → text |
| Input | `phoenix-forge-world/data/branches/input_branches.json` | `ForgeBridge.input_branch_available(id)` |
| Hardware | `phoenix-forge-world/data/integrations/real_world_gadgets.json` | Append `hardware_stub` NDJSON only |
| Ledger | `shared/ledger/manifests/ledger_tables.yaml` | Room / file always local |
| Quest ledger | `scripts/quest-ledger-tool.py` + `quest_log.ndjson` | Parent marks complete manually |
| Quest pipeline | `shared/quests/` schemas + story templates | Gold-standard JSON handoff only |
| Emotion flag | `shared/schemas/daily_emotional_flag.schema.json` | `DailyFlagEngine` computes offline |

---

## Voice playback fallback chain

Per actor (`playback_policy.yaml`):

1. `human_recording` — Dad/operator captures (`dad_joshua`, highest priority for routines)
2. `prerecorded_npc` — bundled m4a/mp3 (`ignavarr` live today)
3. `piper_tts` — offline synthesis from dialogue text (stub until Piper wired)
4. `text_only` — subtitle on screen; game continues

**Human voice > AI** for memory evidence. Piper never overrides an existing human clip for the same `line_id`.

---

## Listen branch (optional, Phase 4)

Faster Whisper sidecar (Pi/laptop) writes `voice_transcript.ndjson`. Student/Forge World import file — maps to emotion check-in or quest response. **Not required** for Phases 0–3.

---

## Voice sidecar branch (optional, Tier C)

Port pattern from `Downloads/voice_controller.py` — **not** a runtime dependency.

| Capability | Location | Fallback |
|------------|----------|----------|
| Piper WAV cache `(voice_id, text_hash)` | `scripts/voice-sidecar/` (ops/classroom) | Prerecorded clips in `assets/audio/` |
| NPC profile table | merge into `playback_policy.yaml` `piper_profiles` | `voice_actor_manifest.json` stewards |
| CLI synthesize | `voice-sidecar synthesize --voice ignavarr --text "..."` | Text subtitle via `VoicePlayer` |

Forge World `VoicePlayer` checks human/prerecorded first; sidecar cache path is probed before Piper stub. No Flask HA broadcast. No Crucible as Tier A.

---

## Quest ledger branch (local, Tier A file contract)

| Capability | Location | Fallback |
|------------|----------|----------|
| Gold-standard import + validate | `scripts/quest-ledger-tool.py` | Manual JSON edit |
| Completion NDJSON | `quest_log.ndjson` (`quest_log_event.schema.json`) | Parent manual log |
| Teacher expedition export | `export-teacher-pack --realm` | Read gold-standard JSON directly |
| SQLite streaks | `data/quest_ledger.db` | Skip streak bonus |

Godot `QuestLogExporter` appends `started` / `step_advanced` / `completed` during play. Student Edition Room table is Phase 2.

---

## Local Childhood Ledger

Replaces Google Sheets without cloud runtime. See `shared/ledger/README.md`.

- **Append-only** NDJSON logs (quest, XP, gold, emotion)
- **Room tables** on Android (Student/Teacher)
- **CSV export** for parent backup (Sheets-down equivalent)
- **Computed views** (Daily Flags) — never authoritative source

---

## Curriculum mapping

Legacy 20 Ezra categories map to **7 Curriculum Of Life domains** (`COGNITIVE_ACADEMIC` … `ETHICS_CIVIC`). Do not revive 20-category codes in new JSON.

---

## Salvage posture from MASTER_EZRA_ECOSYSTEM.gs

| Legacy | Port as | Skip |
|--------|---------|------|
| `logQuestCompletion()` | `quest_log_event.schema.json` | `SpreadsheetApp` |
| `dailyAutoUpdate()` | `daily_brief.schema.json` | Cron triggers |
| `calculateEmotionState()` 4D | `emotion_vector.schema.json` (Phase 2) | HA webhooks |
| `sendToHomeAssistant()` | `ha_event.ndjson` stub pattern | Runtime HA |
| Dad voice priority | `playback_policy` order | Franchise Easter eggs |

**Already ported:** DailyFlagEngine → `daily_flag_engine.gd` + emotion check-in schemas.

---

## Verification

```bash
# Godot
godot --headless -s res://scripts/verify_voice_system.gd
godot --headless -s res://scripts/verify_daily_flag_engine.gd
godot --headless -s res://scripts/verify_p3_environment.gd

# Classroom drift
./scripts/cline-essence-drift-check.sh
```
