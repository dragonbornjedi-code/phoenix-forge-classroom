# Universal AI Agent Rules — Phoenix Forge Classroom

**Every coding agent** (Cursor, Windsurf, Gemini, Copilot, Cline, Codex, etc.) must follow this file plus the linked Tier 0 and Cline Essence sources.

---

## Tier 0 (wins all conflicts)

| File | Role |
|------|------|
| [CONSTITUTION.md](CONSTITUTION.md) | Governance |
| [registry/phoenix-forge-classroom.yaml](../registry/phoenix-forge-classroom.yaml) | Products, paths, `current_step`, forbidden builds |
| [REPOSITORY_CONSTITUTION.md](REPOSITORY_CONSTITUTION.md) | Repo law |

---

## Cline Essence (required skill)

| File | Role |
|------|------|
| [cline_essence/SKILL.md](cline_essence/SKILL.md) | v1.1.0 — workflow, verification contract, tradeoffs |
| [cline_essence/MASTER_PROTOCOL.md](cline_essence/MASTER_PROTOCOL.md) | Phoenix bindings — three APKs, master timeline |
| [cline_essence/SKILL_STACK.md](cline_essence/SKILL_STACK.md) | Domain skills by step range |

**Before any write:** read SKILL + MASTER_PROTOCOL, check **Current step**, map ≥3 risks, implement with proof, run drift check after doc/path edits.

---

## Single repo-wide todo (mandatory)

| Rule | Detail |
|------|--------|
| **Only schedule** | [roadmaps/00_MASTER_ROADMAP.md](roadmaps/00_MASTER_ROADMAP.md) |
| **Step format** | `X.XX` only (`0.01` … `5.00`) — **never** letter suffixes (`0.00-A`) |
| **Insertion** | New work = next free decimal **before** `5.00`; at `3.24` use `3.25+` — **never** renumber backward |
| **Increment** | Each new step ≥ **+0.01** above highest existing step in master file |
| **Sub-roadmaps** | `01`–`04` = **indexes only** (file paths, doc links) — **not** parallel P0/P1/P1a task IDs |
| **Legacy P0/P1** | Historical labels only; map to master decimals, do not schedule by P-label |
| **New features** | Add master row **first**, then code — no side builds without a step number |
| **Done** | Row `[VERIFIED]` or `[TESTED]`; bump `Current step:` in master + `registry` `current_step` |
| **Finish line** | `5.00` = repo complete per master “At 5.00” section |

---

## Products & build paths

**Names (never rename):** Forge Profile · Phoenix Forge Classroom Student Edition · Phoenix Forge Classroom Teacher Edition.

**Runnable APKs only:**

```text
phoenix-forge-classroom-forge-profile/forge-profile-app
phoenix-forge-classroom-forge-profile/student-app
phoenix-forge-classroom-forge-profile/teacher-app
```

**Forbidden:** `phoenix-forge-classroom-teacher-edition/android/`, `phoenix-forge-classroom-student-edition/android/` as build targets.

**Center:** Ezra — Forge Profile, Avatar Studio, chronicle, export to Student + Godot contracts.

---

## Order of operations

```text
0.00–0.25   All three APKs: build, install, device P0
0.50–0.74   Standalone P1 per app (Profile Avatar priority)
0.75–0.85   Contracts before deep UI polish
2.00        Cross-app loop proven before P3 vision depth
5.00        Out-of-repo binaries optional; in-repo contracts [VERIFIED]
```

---

## Verification & drift

```bash
./scripts/cline-essence-drift-check.sh
```

- Do not claim **IMPLEMENTED** / task complete without device, gradle, or test proof.
- Do not create competing timelines or parallel audit files. **Phantom files** (never existed — do not reference as live): `END_GOAL_AND_NORTH_STAR.md`, `ALIGNMENT_AUDIT_2026-06-04.md` — see registry `phantom_files`.
- Prefer editing existing docs; alignment: [DOCUMENTATION_ALIGNMENT_REPORT.md](DOCUMENTATION_ALIGNMENT_REPORT.md).

---

## Read-first checklist

1. `docs/cline_essence/SKILL.md` + `MASTER_PROTOCOL.md`
2. `registry/phoenix-forge-classroom.yaml`
3. `docs/roadmaps/00_MASTER_ROADMAP.md` → **Current step**
4. Matching sub-roadmap index (`01`–`04`) for file hints
5. [DEPLOYMENT_REALITY.md](DEPLOYMENT_REALITY.md) for install/verify

---

*Canonical copy for all tool-specific rule files. Tool entry points: `.cursor/rules/`, `.windsurfrules`, `GEMINI.md`, `.github/copilot-instructions.md`, `.clinerules`, `AGENTS.md`.*
