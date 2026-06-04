# Cline Essence — Phoenix Forge Master Protocol

| Field | Value |
|-------|--------|
| Path | `docs/cline_essence/MASTER_PROTOCOL.md` |
| Version | **1.1.0** (aligned with skill) |
| Canonical skill | [SKILL.md](SKILL.md) (`cline-essence` v1.1.0) |
| Skill routing | [SKILL_STACK.md](SKILL_STACK.md) |
| **Authority** | **ADVISORY** — Tier 0 = `registry/*.yaml` + [docs/CONSTITUTION.md](../CONSTITUTION.md) |

---

## What this is

**Universal rules:** [SKILL.md](SKILL.md) — resilience-first workflow, output contract, verification tags, tradeoff scoring, source-of-truth hierarchy.

**Phoenix Forge bindings (this file):** three APKs, Ezra-centered Forge Profile, master step `0.00`–`5.00`, drift script, registry paths.

Every agent and subagent loads **SKILL.md + this file**. Per-role detail: [AGENT_EXTENSIONS.md](AGENT_EXTENSIONS.md).

**No agent may claim task completion without the Verification Contract in SKILL.md.**

**No work may skip the master timeline step** in [roadmaps/00_MASTER_ROADMAP.md](../roadmaps/00_MASTER_ROADMAP.md).

---

## Universal execution workflow (repo-bound)

Maps to SKILL.md required workflow + Phoenix read-first:

```text
1. READ FIRST        — SKILL.md, registry YAML, CONSTITUTION, census, Current step: X.XX, Atlas
2. MAP RISKS         — ≥3 systemic risks (Black Swan + product-specific)
3. DESIGN GUARDRAILS — Offline-first, Room, provider ACL, drift-check, rollback
4. IMPLEMENT+VERIFY  — Prove guardrails; gradle + drift-check + device when applicable
5. FORENSIC SUMMARY  — Cline Essence sections + master step impact
```

---

## Phoenix Forge drift prevention

| # | Before any write |
|---|------------------|
| 1 | [registry/phoenix-forge-classroom.yaml](../../registry/phoenix-forge-classroom.yaml) |
| 2 | [REPOSITORY_CENSUS_AND_CONNECTIONS.md](../REPOSITORY_CENSUS_AND_CONNECTIONS.md) (76 docs) |
| 3 | `Current step: X.XX` in [00_MASTER_ROADMAP.md](../roadmaps/00_MASTER_ROADMAP.md) |
| 4 | `./scripts/cline-essence-drift-check.sh` after doc/path edits |

| Drift type | Guardrail |
|------------|-----------|
| Wrong APK path (`*/android/` build) | Registry `forbidden_build_paths` → script **FAIL** |
| Phantom doc filenames | Registry `phantom_files` → **FAIL** |
| IMPLEMENTED without proof | Atlas + Authority row |
| Competing global timeline | Only `00_MASTER` has `0.00`–`5.00` |
| P3 on one app while P0 fails another | Master order-of-operations |

**Products (never rename):** Forge Profile · Phoenix Forge Classroom Student Edition · Phoenix Forge Classroom Teacher Edition.

**Center:** Ezra — Forge Profile, **Avatar Studio**, chronicle, export to Student + Godot.

---

## Output contract (Phoenix additions)

Use full contract from [SKILL.md](SKILL.md). Additionally:

- **Mission** must cite master step (e.g. “Advance **0.25**: all three APKs P0 on device”).
- **Plan 1.0–5.0** in SKILL maps to engineering phases; **master step `0.00`–`5.00`** maps to product release (see below) — do not confuse the two scales.
- **Tradeoff scorecard** use Codex weights from SKILL when safety-critical (child data, CMOS).

---

## Master timeline `0.00`–`5.00` (product — repo-wide only)

**Single source:** [roadmaps/00_MASTER_ROADMAP.md](../roadmaps/00_MASTER_ROADMAP.md)

| Step | Name |
|------|------|
| 0.00 | Genesis |
| 0.25 | Device P0 |
| 0.50 | Standalone P1 |
| 0.75 | Contracts |
| 1.00 | Integration scaffold |
| 1.50 | Loop 50% |
| **2.00** | **Loop proven** |
| 2.50 | Profile depth |
| 3.00 | Student depth |
| 3.50 | Teacher depth |
| 4.00 | CMOS bridge |
| 4.50 | Chronicle |
| **5.00** | **Repo 100%** |

**Current step:** `0.00` (sync with registry `current_step` when advancing).

Sub-roadmaps `01`–`04` = file-level detail for active step only.

---

## Order of operations (three apps)

```text
0.00–0.25   ALL apps: build + install + P0
0.50        ALL apps: P1 (Profile Avatar P1-A priority)
0.75        Contracts before more UI polish
2.00        Cross-app loop before P3 vision depth
5.00        Godot/Sovereign out of repo; import contracts [VERIFIED]
```

---

## Systemd drift guard

```bash
cp scripts/systemd/phoenix-forge-drift-check.* ~/.config/systemd/user/
# Edit WorkingDirectory= if repo path differs
systemctl --user daemon-reload
systemctl --user enable --now phoenix-forge-drift-check.timer
```

---

## Quick links

- [SKILL.md](SKILL.md) — portable cline-essence v1.1.0  
- [SKILL_STACK.md](SKILL_STACK.md) — android / offline / agent skills by step  
- [AGENT_EXTENSIONS.md](AGENT_EXTENSIONS.md)  
- [DEVELOPMENT_RULES.md](../DEVELOPMENT_RULES.md)  
