# Phoenix Forge Classroom

Offline-first childhood OS for **Ezra** — centered on **Forge Profile** (lifelong identity, avatar, chronicle), with Teacher and Student editions as role-based shells on the same record.

Private: https://github.com/dragonbornjedi-code/phoenix-forge-classroom

---

## Forge Profile (start here)

**Forge Profile** is not “one of three apps.” It is the **CMOS direction** — the encrypted local store for who Ezra is becoming: identity, **Avatar Studio**, timeline, artifacts, and future chronicle promotion.

| Concern | Doc | Code |
|---------|-----|------|
| Identity & 2046 durability | [docs/FORGEPROFILE_SPEC.md](docs/FORGEPROFILE_SPEC.md) | `forge-profile-core` |
| Avatar Studio (expand next) | FORGEPROFILE_SPEC § Avatar | `forge-profile-app/ui/studio/` |
| Steward UI APK | [docs/DEPLOYMENT_REALITY.md](docs/DEPLOYMENT_REALITY.md) | `forge-profile-app` |
| Cross-app read API | Profile ContentProvider | `/profile`, `/avatar`, `/timeline` |
| Godot 3D import path | [docs/GODOT_MIGRATION_STRATEGY.md](docs/GODOT_MIGRATION_STRATEGY.md) | Future repo |

**Build Profile APK:** `./scripts/install-phone-apks.sh` or see DEPLOYMENT_REALITY.

---

## Three experience shells (canonical names)

| Product | Specs | Runnable module |
|---------|-------|-----------------|
| **Forge Profile** | `docs/FORGEPROFILE_SPEC.md` | `:forge-profile-app` + `:forge-profile-core` |
| **Phoenix Forge Classroom Student Edition** | `phoenix-forge-classroom-student-edition/docs/` | `:student-app` |
| **Phoenix Forge Classroom Teacher Edition** | `phoenix-forge-classroom-teacher-edition/docs/` | `:teacher-app` |

Gradle monorepo: `phoenix-forge-classroom-forge-profile/`

---

## Architecture (one picture)

```text
                    Ezra (human)
                         │
              ┌──────────▼──────────┐
              │   Forge Profile      │  ◄── Avatar Studio, timeline, CMOS
              │   (single record)    │
              └──────────┬──────────┘
         ┌────────────────┼────────────────┐
         ▼                ▼                ▼
   Student Edition   Teacher Edition    Godot world
   (child shell)     (steward shell)    (future 3D shell)
         │                │                │
         └────────────────┴────────────────┘
                    shared contracts
              docs/contracts/ + shared/
```

**North star:** [docs/UNIFIED_VISION.md](docs/UNIFIED_VISION.md) · **Execution:** [docs/roadmaps/00_MASTER_ROADMAP.md](docs/roadmaps/00_MASTER_ROADMAP.md) (**Current step: 0.04** — decimal ledger `0.01`→`5.00`)  
**Agent protocol:** [docs/cline_essence/MASTER_PROTOCOL.md](docs/cline_essence/MASTER_PROTOCOL.md) · **Drift check:** `./scripts/cline-essence-drift-check.sh`

**Near-term loop:** Teacher IntentTile → Student Quest → MemoryEvent → Profile timeline → Teacher signal ([docs/DEVELOPMENT_RULES.md](docs/DEVELOPMENT_RULES.md)).

---

## Documentation index

| Area | Entry |
|------|--------|
| Constitution & vision | [docs/README.md](docs/README.md) |
| Full file census | [docs/REPOSITORY_CENSUS_AND_CONNECTIONS.md](docs/REPOSITORY_CENSUS_AND_CONNECTIONS.md) |
| Implementation truth | [docs/PHOENIX_FORGE_SYSTEM_ATLAS.md](docs/PHOENIX_FORGE_SYSTEM_ATLAS.md) |
| Cross-app contracts | [shared/README.md](shared/README.md) |

---

## Repo layout

- `phoenix-forge-classroom-forge-profile/` — all three APK modules + core library
- `phoenix-forge-classroom-teacher-edition/` — pedagogy & Expedition Board UX specs
- `phoenix-forge-classroom-student-edition/` — Hearthhome / Spark UX specs (runtime = `student-app`)
- `docs/` — constitution, contracts, atlas, roadmaps
- `shared/` — sync contract, PCAS SQL spec, export contracts (see shared README)
- `registry/` — future structured content packs (lessons, games)
- `scripts/` — `install-phone-apks.sh`

---

## External (other repos)

- **Sovereign Deck** — optional AI narrative layer (not required for offline childhood record)
- **Godot reflection world** — 3D experience shell; imports Forge Profile + AvatarConfig JSON
