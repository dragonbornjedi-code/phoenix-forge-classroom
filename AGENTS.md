# AI Agents — Phoenix Forge Classroom

All autonomous coding agents must use the same rules.

## Canonical source

**[docs/AI_AGENT_UNIVERSAL_RULES.md](docs/AI_AGENT_UNIVERSAL_RULES.md)**

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

## Master todo (repo-wide)

- **[docs/roadmaps/00_MASTER_ROADMAP.md](docs/roadmaps/00_MASTER_ROADMAP.md)** — only decimal schedule (`0.01` … `5.00`)
- Sub-roadmaps `01`–`04` — indexes only
- **Current step:** read master header + `registry/phoenix-forge-classroom.yaml` `current_step`

## Quick commands

```bash
./scripts/cline-essence-drift-check.sh
./gradlew -p phoenix-forge-classroom-forge-profile :forge-profile-app:assembleDebug
```

Human dev rules: [docs/DEVELOPMENT_RULES.md](docs/DEVELOPMENT_RULES.md)
