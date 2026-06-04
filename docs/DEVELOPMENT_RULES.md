# Phoenix Forge Classroom Development Rules

Before making any changes, read [cline_essence/MASTER_PROTOCOL.md](cline_essence/MASTER_PROTOCOL.md) and check **Current step** in [roadmaps/00_MASTER_ROADMAP.md](roadmaps/00_MASTER_ROADMAP.md). Run `./scripts/cline-essence-drift-check.sh` after path/doc edits.

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

| Path | Meaning |
|------|---------|
| P0 | Stabilize |
| P1 | Standalone usefulness |
| P2 | Integration |
| P3 | Vision |

Never work on P3 if the relevant P0/P1 requirements are still failing.

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
