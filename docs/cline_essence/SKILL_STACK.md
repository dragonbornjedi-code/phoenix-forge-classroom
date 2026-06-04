# Phoenix Forge — Skill stack routing

When working in **this repo**, load **cline-essence** first ([SKILL.md](SKILL.md) + [MASTER_PROTOCOL.md](MASTER_PROTOCOL.md)), then add domain skills by **master step** and task type.

Personal/global skills you listed are **optional accelerators** — they do not override Tier 0 ([CONSTITUTION.md](../CONSTITUTION.md)) or the `0.00`–`5.00` timeline.

---

## Always-on (every task)

| Skill | Use for |
|-------|---------|
| **cline-essence** | Workflow, verification tags, threat model, tradeoff scoring |
| **phoenix-ecosystem-guide** (if available) | Mesh/registry context — defer to in-repo CONSTITUTION when they conflict |

---

## By master step ([00_MASTER_ROADMAP.md](../roadmaps/00_MASTER_ROADMAP.md))

| Step | Suggested skills |
|------|------------------|
| **0.00–0.25** | `android-gradle-build-logic`, `android-kotlin-core`, `mobile-offline-support` |
| **0.50** Profile | `android-kotlin-development`, `mobile-offline-support` |
| **0.50** Student/Teacher | `android-kotlin-development`, `godot-best-practices` (docs only until Godot repo) |
| **0.75–2.00** | `typescript-advanced-types` (contracts), `mcp-integration` / `nodejs-backend-patterns` (if sync service), `agentic-eval` (loop proof) |
| **2.50** Avatar / UI | `android-kotlin-development`, `frontend-design` (Compose polish) |
| **3.00** Student UX | `godot-gdscript-patterns` (future shell), `playwright-cli` / `webapp-testing` (if web harness) |
| **4.00** CMOS | `sql-optimization`, `mongodb` (if Atlas later), `langchain-rag` (Chronicle query — future) |
| **4.50+** Ops | `devops-engineer`, `observability-stack-setup`, `systemd-services` |

---

## By work type

| Task | Skills |
|------|--------|
| Gradle / modules / APK | `android-gradle-build-logic` |
| Kotlin UI / Room / Compose | `android-kotlin-development`, `android-kotlin-core` |
| Offline sync / conflict | `mobile-offline-support` |
| Local LLM / edge (future) | `llama-cpp`, `gguf-quantization`, `huggingface-local-models` |
| Agent hooks / Cursor plugin | `hook-development`, `plugin-structure`, `skill-development` |
| PR / CI / deploy | `devops-automator`, `deployment-engineer`, `fix-ci` |
| Multi-agent fan-out | `dispatching-parallel-agents`, `subagent-driven-development` |
| Doc alignment only | cline-essence only — **no** new architecture skills |
| Threat / governance | `agent-governance`, `governed-execution` |

---

## Explicitly deferred in this repo (do not apply as spine)

| Skill | Why |
|-------|-----|
| UME/playground mission engines | Pre-pivot; see [shared/README.md](../../shared/README.md) |
| `jarvis-roadmap` / `prd-generator` | Use `00_MASTER_ROADMAP` only |
| `n8n-mcp-tools-expert` | Not in offline childhood spine |

---

## Subagent dispatch

Parent passes: `master_step`, `skills_recommended[]` from this table, `verification_commands_run[]`.  
Subagent must return Cline Essence sections + verification tags.
