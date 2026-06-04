# GitHub Copilot — Phoenix Forge Classroom

Canonical rules: **`docs/AI_AGENT_UNIVERSAL_RULES.md`**

## Governance

- **Tier 0:** `docs/CONSTITUTION.md`, `registry/phoenix-forge-classroom.yaml`
- **Protocol:** `docs/cline_essence/SKILL.md` (v1.1.0) + `docs/cline_essence/MASTER_PROTOCOL.md`

## Scheduling (strict)

All work is tracked only in **`docs/roadmaps/00_MASTER_ROADMAP.md`**:

| Rule | Requirement |
|------|-------------|
| Step format | `X.XX` from `0.01` to `5.00` — no letter suffixes |
| Insertion | Forward-only; new steps before `5.00`; ≥+0.01 from highest step |
| Sub-roadmaps | `01`–`04` are indexes — do not use P0/P1/P1a as task IDs |
| New features | Add master row first, then code |

Read **Current step** in the master file and `current_step` in registry before implementing.

## Implementation

- **Modules:** `phoenix-forge-classroom-forge-profile/forge-profile-app`, `student-app`, `teacher-app`
- **Avoid:** `phoenix-forge-classroom-teacher-edition/android/`, `phoenix-forge-classroom-student-edition/android/` builds
- **Verify:** gradle / device proof; mark master rows `[VERIFIED]` or `[TESTED]` only with evidence
- **Drift:** run `./scripts/cline-essence-drift-check.sh` after doc or path edits

## Cline Essence

Use resilience-first workflow from SKILL.md: risks before code, guardrails, verifiable changes, forensic summary citing the master step.

**Forbidden:** alternate global timelines, phantom documentation files, renaming the three products.
