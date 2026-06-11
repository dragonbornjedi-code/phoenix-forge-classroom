# AI Agents — Phoenix Forge Classroom

## Phoenix Forge Agency (read first)
**Contract:** `phoenix-forge-home/registry/phoenix_forge_agency.yaml`
**Soul:** `phoenix-forge-home/registry/phoenix_forge_agency_soul.yaml`
**CLI:** `phoenix-forge-home/scripts/phoenix-forge-agency verify-all`
**Audit:** `phoenix-forge-home/scripts/phoenix-forge-agency audit-drift`
**Parity:** `phoenix-forge-home/scripts/phoenix-forge-agency parity-check`

---

All autonomous coding agents must use the same rules.

## Canonical source

**[docs/AI_AGENT_UNIVERSAL_RULES.md](docs/AI_AGENT_UNIVERSAL_RULES.md)** — short rules for tool entry points.

**New session:** paste **[docs/UNIVERSAL_AI_INSTRUCTIONS.md](docs/UNIVERSAL_AI_INSTRUCTIONS.md)** at the top of the chat (cascade matrix, session bridge, forbidden actions).

## Tool-specific entry points

| Tool | File |
|------|------|
| Cursor | `.cursor/rules/phoenix-forge-universal.mdc` |
| Windsurf | `.windsurfrules` |
| Gemini | `GEMINI.md` |
| GitHub Copilot | `.github/copilot-instructions.md` |
| Cline | `.clinerules` |

## Cline Essence (project skill)

- [docs/cline_essence/SKILL.md](docs/cline_essence/SKILL.md) — v1.1.0
- [docs/cline_essence/MASTER_PROTOCOL.md](docs/cline_essence/MASTER_PROTOCOL.md)

## Session handoff (live)

- **[docs/SESSION_HANDOFF_20260609.md](docs/SESSION_HANDOFF_20260609.md)** — what's done, next slice, phone setup
- **[docs/PHOENIX_FORGE_MASTER_SPEC.md](docs/PHOENIX_FORGE_MASTER_SPEC.md)** — read before sync/event/profile work
- **[docs/HANDOFF_1_02.md](docs/HANDOFF_1_02.md)** — manifest push paths (1.01–1.03)

## Master todo (repo-wide)

- **[docs/roadmaps/00_MASTER_ROADMAP.md](docs/roadmaps/00_MASTER_ROADMAP.md)** — only decimal schedule (`0.01` … `5.00`)
- Sub-roadmaps `01`–`04` — indexes only
- **Current step:** read master header + `registry/phoenix-forge-classroom.yaml` `current_step`

## Lane entry (mandatory)

| Slash | Mode |
|-------|------|
| `/phoenix-forge-classroom` | **RECAP** — step, handoff, verify → wait for goal |
| `/phoenix-forge-classroom <goal>` | **AUTONOMOUS** — route leaf → develop → verify-all PASS → deliver |

| Asset | Path |
|-------|------|
| Skill | `.cursor/skills/phoenix-forge-classroom/SKILL.md` |
| Agency layers | `.cursor/skills/phoenix-forge-classroom/AGENCY_LAYERS.md` |
| Context budget | `.cursor/skills/phoenix-forge-classroom/CONTEXT_BUDGET.md` |
| Delegation | `.cursor/skills/phoenix-forge-classroom/DELEGATION.md` |
| Architecture | `docs/CLASSROOM_AGENCY_ARCHITECTURE.md` |
| Active manifest | `registry/classroom_agent_manifest.yaml` |
| Context pack | `scripts/classroom-context-pack.sh` |
| Delegation verify | `scripts/classroom-delegation-verify.py` |
| Verify all | `scripts/phoenix-forge-classroom-verify-all.sh` |

Symlink: `~/.phoenix-forge-agent-skills/phoenix-forge-classroom`

## Quick commands

```bash
./scripts/classroom-context-pack.sh "your goal"
./scripts/classroom-delegation-verify.py
./scripts/cline-essence-drift-check.sh
./gradlew -p phoenix-forge-classroom-forge-profile :forge-profile-app:assembleDebug
```

Human dev rules: [docs/DEVELOPMENT_RULES.md](docs/DEVELOPMENT_RULES.md)
