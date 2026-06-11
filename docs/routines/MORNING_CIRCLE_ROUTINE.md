# Morning circle — routine spec (not a daily quest)

**Status:** Teacher field guide pre-filled · Student popup flow **not built yet**  
**Operator:** Josh · **Child uid:** drives sync paths when on tablet

---

## What it is

A **morning routine** — separate from the three interchangeable **daily quest** slots.

| List type | Purpose | Example trigger |
|-----------|---------|-------------------|
| Morning routine | Calm connection + stretch | 10:00 AM popup on child tablet |
| Night routine | Wind-down | Evening (TBD) |
| Daily quests | Expedition board stack | Teacher push after approval |

Morning circle must **not** compete with daily quest tiles on the expedition board as a swap-in quest.

---

## Flow (target UX)

1. **10:00 AM** — Student Edition gentle popup on child tablet (future alarm permission).
2. **Companion check:** "Is someone with you?" — options adapt to house:
   - Find Me (Josh's house)
   - Mom (other house)
   - Not yet → snooze routine
3. **Vibe picker** (rotation, open-source / licensed-safe):
   - Healing frequencies (very low volume — felt more than heard)
   - Soft kid songs
   - Quiet instrumental stretch bed
4. **Vocal instructor** (TTS or pre-recorded clips):
   - Explains next stretch/activity
   - **Start button** appears — one person taps when everyone is in position (no fixed dead-air timers)
   - Between holds: encouragement mix (breathing cues, playful praise)
5. **Questions corner** (end):
   - One wonder
   - One grateful thing
   - One plan for today
6. **Events** (when built): `ROUTINE_STARTED`, `ROUTINE_COMPLETED` under `{studentUid}/events/` scope PUBLIC.

---

## Audio constraints

- Prefer CC0 / open-license vibrational and healing-tone assets (inventory before ship).
- Healing track: default volume cap ~15% — "heal without being heard."
- Kid songs: normal kid volume.

---

## Teacher Edition today

- Tile **Morning circle** has full field guide (materials, coaching, examples, supports, recovery).
- `routineKind = morning_routine` — labeled in tile detail.
- **Not** auto-pushed as daily quest until Josh approves routine engine.

---

## Build order

1. ✅ Pre-filled field guide in Teacher Edition
2. Student routine scheduler + companion gate UI
3. Audio rotation + Start-per-pose stretch player
4. Questions corner screen
5. EventWriter hooks + Forge Profile timeline ingest
