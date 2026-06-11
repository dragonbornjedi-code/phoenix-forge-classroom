---
name: phoenix-forge-classroom
description: >-
  Phoenix Forge Classroom FULL AUTONOMOUS LANE. /phoenix-forge-classroom alone = recap.
  /phoenix-forge-classroom <goal> = bootstrap, route leaf agent, develop Kotlin/Compose,
  verify until PASS, update handoff, score SRS/FPS/PIS — ONE turn. Student, Teacher,
  Forge Profile, Sage, sync/events — Classroom scope only.
disable-model-invocation: true
---

# Phoenix Forge Classroom — full autonomous lane

**Slash:** `/phoenix-forge-classroom` or `/phoenix-forge-classroom <goal>`

Read **`AUTONOMOUS_LOOP.md`** first — executable contract.

## Mode switch

```
/phoenix-forge-classroom                    → RECAP (wait for goal)
/phoenix-forge-classroom stabilize QuestEngine 0.69  → AUTONOMOUS (full loop)
```

## AUTONOMOUS mode — complete in one session

1. `./scripts/phoenix-forge-classroom-loop.sh "<goal>"`
2. `./scripts/classroom-context-pack.sh "<goal>"` — activate **leaf**, not `ao_classroom_bridge` alone
3. Load max 2 skills from route (cline-essence + frontend-design or android-kotlin-*)
4. Implement in `phoenix-forge-classroom-forge-profile/` only
5. **Loop:** `./scripts/phoenix-forge-classroom-verify-all.sh` → fix → PASS (max 8)
6. `./scripts/classroom-delegation-verify.py` if routing changed
7. `./scripts/phoenix-forge-classroom-finish.sh "<goal>" "<summary>" "<why>"`
8. `./scripts/record-classroom-turn-score.sh` with leaf_agent_id
9. Deliver A6 report with `classroom_turn_score` YAML

## RECAP mode

```bash
./scripts/phoenix-forge-classroom-session.sh
phoenix-forge-home/scripts/agency-preflight.sh --lane classroom --skip-jarvis-scan
```

Recap step 0.68 status, verify, planned 0.69. **Stop. Wait for goal.**

## Constitutional (always)

- Gradle root: `phoenix-forge-classroom-forge-profile/` only
- **Forbidden:** `teacher-edition/android/`, `student-edition/android/`
- File contracts to World — no Gradle coupling
- Student writes PUBLIC events only; Teacher writes manifests + PROTECTED
- Adult profile never imports to Student
- No Godot edits unless Josh bridges lanes
- No commits unless goal says commit

## References

| File | Purpose |
|------|---------|
| `AUTONOMOUS_LOOP.md` | A0–A6 loop |
| `AGENCY_LAYERS.md` | Tier stack + 12 leaves |
| `CONTEXT_BUDGET.md` | Per-turn limits |
| `DELEGATION.md` | Parent→Leaf proof |
| `SKILL_ROUTING.md` | Work type matrix |
| `CLASSROOM_SCORING.md` | SRS / FPS / PIS |
| `docs/CLASSROOM_AGENCY_ARCHITECTURE.md` | Master architecture |
| `registry/classroom_agent_manifest.yaml` | Active leaves |

## Scripts

| Script | When |
|--------|------|
| `classroom-context-pack.sh` | Every turn |
| `classroom-route-classify.py` | Goal → leaf |
| `classroom-delegation-verify.py` | Corpus ≥92% |
| `phoenix-forge-classroom-loop.sh` | Start AUTONOMOUS |
| `phoenix-forge-classroom-verify-all.sh` | After each fix |
| `phoenix-forge-classroom-finish.sh` | After PASS |
