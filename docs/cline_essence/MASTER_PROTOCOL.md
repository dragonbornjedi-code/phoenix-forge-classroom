# Cline Essence — Phoenix Forge Master Protocol

| Field | Value |
|-------|--------|
| Path | `docs/cline_essence/MASTER_PROTOCOL.md` |
| Version | 1.0.0 |
| Source skill | cline-essence v1.1.0 |
| **Authority** | **ADVISORY** — Tier 0 = `registry/*.yaml` + [docs/CONSTITUTION.md](../CONSTITUTION.md) |

---

## What this is

Every Phoenix Forge agent and subagent operates under **Cline Essence**: resilience-first execution discipline. This file is the **repo-wide contract**. Per-role extensions: [AGENT_EXTENSIONS.md](AGENT_EXTENSIONS.md).

**No agent may claim task completion without satisfying the Verification Contract below.**

**No implementation may skip ahead of the master timeline step** in [roadmaps/00_MASTER_ROADMAP.md](../roadmaps/00_MASTER_ROADMAP.md). Sub-roadmaps (`01`–`04`) are detail only; **step `0.00`–`5.00` lives in the master roadmap alone.**

---

## Phoenix Forge drift prevention (repo-wide)

Before any write, agents MUST:

1. Read [registry/phoenix-forge-classroom.yaml](../../registry/phoenix-forge-classroom.yaml) — product names, module paths, `forbidden_build_paths`.
2. Read [REPOSITORY_CENSUS_AND_CONNECTIONS.md](../REPOSITORY_CENSUS_AND_CONNECTIONS.md) — all **76** docs; do not invent parallel audits.
3. Read **current master step** from `00_MASTER_ROADMAP.md` header (`Current step: X.XX`).
4. Run `./scripts/cline-essence-drift-check.sh` after doc/path changes (or rely on systemd timer on operator machine).

| Drift type | Guardrail |
|------------|-----------|
| Wrong APK path (`*/android/` as build root) | Registry `forbidden_build_paths` + drift script **FAIL** |
| Phantom doc filenames | Registry `phantom_files` + drift script **FAIL** |
| Doc says IMPLEMENTED without code | [PHOENIX_FORGE_SYSTEM_ATLAS.md](../PHOENIX_FORGE_SYSTEM_ATLAS.md) proof row required |
| New top-level architecture doc | [DEVELOPMENT_RULES.md](../DEVELOPMENT_RULES.md) — forbidden unless user asks |
| Work past master step | Master roadmap gate — mark [PARTIAL] or stop |
| Three apps out of sync on P0 | Do not start Profile P3 while Teacher P0 fails |

**Three products (never rename):** Forge Profile · Phoenix Forge Classroom Student Edition · Phoenix Forge Classroom Teacher Edition.

**Center:** Ezra’s Forge Profile record — Avatar Studio, chronicle, export to Student + Godot.

---

## Universal execution workflow

Every agent, every task, every time:

```text
1. READ FIRST        — Registry, CONSTITUTION, census, current master step, Atlas for claims
2. MAP RISKS         — ≥3 systemic risks before coding or doc edits
3. DESIGN GUARDRAILS — Timeouts, validation, offline-first, rollback, verification commands
4. IMPLEMENT+VERIFY  — Logging/assertions that PROVE guardrails; run drift-check + gradle
5. FORENSIC SUMMARY  — What changed | Risk mitigated | Residual | Master step impact
```

---

## Output contract (substantial tasks)

### Mission

One-sentence objective tied to **master step** (e.g. “Advance 0.25: Teacher cold-start verified on device”).

### Threat model (top risks)

1. **[CRITICAL]** … → Impact → Guardrail  
2. **[HIGH]** …  
3. **[MEDIUM]** …

### Plan

1.0 Foundation — Prerequisites, read registry, baseline commands  
2.0 Safety/Integrity — Data integrity, steward/child boundary, no CMOS delete from child shell  
3.0 Reliability — Offline-first, Room migrations, provider permissions, retry on import  
4.0 Ops/Observability — `adb`/gradle proof, DEPLOYMENT_REALITY matrix, Atlas row update  
5.0 Final hardening — Edge cases, rollback (`git revert`), residual risks  

### Tradeoff scorecard

| Option | Speed | Safety | Complexity | Chosen? |
|--------|-------|--------|------------|---------|
| A | 0–5 | 0–5 | 0–5 | |
| B | 0–5 | 0–5 | 0–5 | → reason |

### Residual risks

- Not solved  
- Operator action required  
- Graceful degrade vs fail-hard  

---

## Verification contract

Never mark **DONE** without:

| Tag | Meaning |
|-----|---------|
| `[VERIFIED]` | Runtime proof — test, `adb`, screenshot, log snippet |
| `[TESTED]` | `./gradlew assembleDebug` or unit test pass |
| `[PARTIAL]` | Some deliverables done; master step not advanced |
| `[UNTESTED]` | Code/docs written; no runtime proof — **cannot advance step** |

For each material change provide:

- **What changed** (files)  
- **How verified** (exact command output or log line)  
- **Rollback** (`git revert <sha>` or doc restore path)  
- **Master step** — stays same, or bump only with `[VERIFIED]` criteria met  

---

## Master timeline `0.00`–`5.00` (repo-wide)

**Single source:** [roadmaps/00_MASTER_ROADMAP.md](../roadmaps/00_MASTER_ROADMAP.md)

| Step | Name | All three apps |
|------|------|----------------|
| **0.00** | Genesis | Protocol + registry + builds green |
| **0.25** | Device truth | 3 APKs on phone, P0 stable |
| **0.50** | Standalone P1 | Each app useful alone |
| **0.75** | Contracts | Shared Kotlin + YAML loader |
| **1.00** | Integration scaffold | Handoff plumbing |
| **1.50** | Loop half | Tile→Quest→Memory partial |
| **2.00** | **Loop proven** | End-to-end [VERIFIED] |
| **2.50** | Profile depth | Avatar P1-A + artifacts + export |
| **3.00** | Student depth | Hearthhome + Spark |
| **3.50** | Teacher depth | Board complete + compass seed |
| **4.00** | CMOS bridge | Unified MemoryEvent |
| **4.50** | Chronicle tier | Promotion + significance |
| **5.00** | **Repo complete** | In-repo north star 100% |

**Current step (update when verified):** `0.00` — see master roadmap header.

Sub-roadmaps **must not** define a competing global step number.

---

## Order of operations (three apps)

```text
0.00–0.25   ALL apps: build + install + P0 (parallel allowed)
0.50        ALL apps: P1 standalone (Profile Avatar is Ezra priority within 0.50)
0.75        Contracts BEFORE more UI polish on any app
2.00        Cross-app loop BEFORE P3 vision depth on any app
5.00        External Godot/Sovereign = out of repo; in-repo scope must be [VERIFIED]
```

---

## Systemd drift guard (operator machine)

Optional but recommended on the dev workstation:

- `scripts/systemd/phoenix-forge-drift-check.service`  
- `scripts/systemd/phoenix-forge-drift-check.timer` — daily  

Install:

```bash
cp scripts/systemd/phoenix-forge-drift-check.* ~/.config/systemd/user/
systemctl --user daemon-reload
systemctl --user enable --now phoenix-forge-drift-check.timer
```

Logs: `journalctl --user -u phoenix-forge-drift-check.service`

---

## Quick links

- [AGENT_EXTENSIONS.md](AGENT_EXTENSIONS.md) — implementer / reviewer / doc auditor  
- [DEVELOPMENT_RULES.md](../DEVELOPMENT_RULES.md)  
- [DOCUMENTATION_ALIGNMENT_REPORT.md](../DOCUMENTATION_ALIGNMENT_REPORT.md)  
