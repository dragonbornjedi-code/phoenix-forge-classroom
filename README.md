# Phoenix Forge Classroom

Offline-first homeschool system for Galaxy S24 Ultra — centered on **Ezra’s Forge Profile** (lifelong identity record).

## Three products (canonical names)

| Product | Docs folder | Runnable code |
|---------|-------------|---------------|
| **Forge Profile** | `docs/FORGEPROFILE_SPEC.md` | `phoenix-forge-classroom-forge-profile/forge-profile-app` + `forge-profile-core` |
| **Phoenix Forge Classroom Student Edition** | `phoenix-forge-classroom-student-edition/docs/` | `phoenix-forge-classroom-forge-profile/student-app` |
| **Phoenix Forge Classroom Teacher Edition** | `phoenix-forge-classroom-teacher-edition/docs/` | `phoenix-forge-classroom-forge-profile/teacher-app` |

**Build all APKs:** see [docs/DEPLOYMENT_REALITY.md](docs/DEPLOYMENT_REALITY.md) or `./scripts/install-phone-apks.sh`.

**Roadmaps:** [docs/roadmaps/00_MASTER_ROADMAP.md](docs/roadmaps/00_MASTER_ROADMAP.md)  
**Full file census:** [docs/REPOSITORY_CENSUS_AND_CONNECTIONS.md](docs/REPOSITORY_CENSUS_AND_CONNECTIONS.md)

## Repo layout

- `phoenix-forge-classroom-forge-profile/` — Gradle monorepo (all three APK modules)
- `phoenix-forge-classroom-teacher-edition/` — Teacher pedagogy & UX specs
- `phoenix-forge-classroom-student-edition/` — Student UX specs (runtime is `student-app`)
- `docs/` — constitution, contracts, atlas, roadmaps
- `shared/` — sync contract + PCAS SQL (spec)
- `scripts/` — install automation

## External (other repos)

- **Sovereign Deck** — personal AI / mesh (optional narrative layer)
- **Godot reflection world** — future experience shell

Private: https://github.com/dragonbornjedi-code/phoenix-forge-classroom
