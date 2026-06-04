# Phoenix Forge Classroom Development Rules

Before making any changes, read [AI_AGENT_UNIVERSAL_RULES.md](AI_AGENT_UNIVERSAL_RULES.md) (all AI tools), [cline_essence/SKILL.md](cline_essence/SKILL.md) + [cline_essence/MASTER_PROTOCOL.md](cline_essence/MASTER_PROTOCOL.md); use [cline_essence/SKILL_STACK.md](cline_essence/SKILL_STACK.md) for domain skills. Check **Current step** in [roadmaps/00_MASTER_ROADMAP.md](roadmaps/00_MASTER_ROADMAP.md) (`0.01`, `0.02`, … — never `0.00-A` style). Run `./scripts/cline-essence-drift-check.sh` after path/doc edits.

**AI entry points:** `AGENTS.md`, `.cursor/rules/`, `.windsurfrules`, `GEMINI.md`, `.github/copilot-instructions.md`, `.clinerules`.

## Authoritative planning

0. [CONSTITUTION.md](CONSTITUTION.md) · [registry/phoenix-forge-classroom.yaml](../registry/phoenix-forge-classroom.yaml) (Tier 0)
1. [roadmaps/00_MASTER_ROADMAP.md](roadmaps/00_MASTER_ROADMAP.md) — **only** repo-wide `0.00`–`5.00` timeline
2. [roadmaps/01_FORGE_PROFILE_ROADMAP.md](roadmaps/01_FORGE_PROFILE_ROADMAP.md)
3. [roadmaps/02_STUDENT_EDITION_ROADMAP.md](roadmaps/02_STUDENT_EDITION_ROADMAP.md)
4. [roadmaps/03_TEACHER_EDITION_ROADMAP.md](roadmaps/03_TEACHER_EDITION_ROADMAP.md)
5. [roadmaps/04_CROSS_APP_INTEGRATION_ROADMAP.md](roadmaps/04_CROSS_APP_INTEGRATION_ROADMAP.md)

**Do not** replace these, create competing architectures, invent new products, or rename products.

## Three products

- **Forge Profile**
- **Phoenix Forge Classroom Student Edition**
- **Phoenix Forge Classroom Teacher Edition**

**Cross-App Integration** is the spine connecting them.

Develop the existing system; do not redesign it.

## Workflow

1. Read the relevant roadmap (including **Build decomposition** sections).
2. Read linked specification documents.
3. Extend existing phases, tasks, contracts, and implementations.
4. Prefer implementation over documentation.
5. Prefer modifying existing files over creating new files.
6. Only create new documents when explicitly requested or required for implementation.

## Roadmap discipline

**Schedule only by master decimals** in [00_MASTER_ROADMAP.md](roadmaps/00_MASTER_ROADMAP.md). New work = new `X.XX` row (forward-only, before `5.00`).

| Legacy label | Meaning (map to master steps, do not use as task IDs) |
|--------------|--------------------------------------------------------|
| P0 | Stabilize → e.g. `0.26`–`0.49` device band |
| P1 | Standalone usefulness → e.g. `0.51`–`0.74` |
| P2 | Integration → e.g. `1.00`–`2.07` |
| P3 | Vision → e.g. `2.50`+ depth bands |

Never work on high milestone depth if the relevant lower master steps for that app are still failing.

## Implementation priority

1. Working APKs on device
2. Teacher Expedition Board
3. Shared contracts
4. First Tile → Quest → MemoryEvent loop
5. Deeper profile systems
6. Hearthhome, Compass, Narrative, Threads, Godot (other repos)

## Change proposals

Do not provide replacement architectures. Provide:

**Feature → Build steps → Files to modify → Exit criteria**

Assume existing documents are intentional unless directly contradicted by [REPOSITORY_CENSUS_AND_CONNECTIONS.md](REPOSITORY_CENSUS_AND_CONNECTIONS.md) (all **76** docs) or code.

**Alignment:** [DOCUMENTATION_ALIGNMENT_REPORT.md](DOCUMENTATION_ALIGNMENT_REPORT.md) — code-lag ≠ contradiction. Do not create parallel audit files.

## Current product goal

```text
Teacher creates IntentTile
  → Student receives Quest
  → Student completes Quest
  → Forge Profile records MemoryEvent
  → Teacher sees resulting signal
```

Favor implementation progress over architectural discussion.
