# Gemini — Phoenix Forge Classroom

You must follow **`docs/AI_AGENT_UNIVERSAL_RULES.md`** and the sources it references.

## Required reading (before changes)

| Priority | Path |
|----------|------|
| Tier 0 | `docs/CONSTITUTION.md`, `registry/phoenix-forge-classroom.yaml` |
| Skill | `docs/cline_essence/SKILL.md`, `docs/cline_essence/MASTER_PROTOCOL.md` |
| Schedule | `docs/roadmaps/00_MASTER_ROADMAP.md` (**Current step**) |
| Index | Matching `docs/roadmaps/01`–`04` for file hints |

## Master decimal ledger (mandatory)

- **One todo list:** `00_MASTER_ROADMAP.md` only — not P0/P1 labels in sub-roadmaps
- **IDs:** `0.01` … `5.00` — never `0.00-A`; minimum +0.01 between new steps
- **Insert forward:** at `3.24`, next new item is `3.25+`; use gaps (`4.91`) before finish — never renumber down
- **New scope:** new row in master first, then implement
- **Done:** `[VERIFIED]` / `[TESTED]` on row + update `Current step:` and registry

## Products & APKs

Forge Profile · Student Edition · Teacher Edition (names fixed).

Build: `phoenix-forge-classroom-forge-profile/{forge-profile-app,student-app,teacher-app}`.

## Cline Essence workflow

1. State objective tied to master step  
2. Map ≥3 risks  
3. Guardrails (offline-first, drift check, no forbidden paths)  
4. Implement with proof  
5. Forensic summary  

Run: `./scripts/cline-essence-drift-check.sh` after documentation or path changes.

**Do not:** competing roadmaps, phantom docs, claim complete without verification.
