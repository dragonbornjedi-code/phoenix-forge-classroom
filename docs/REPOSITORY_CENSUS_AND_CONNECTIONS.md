# Repository Census & Connections

**Generated:** 2026-06-04 · **Last audit:** 2026-06-04  
**Purpose:** Authoritative **per-file register** for all **76** `.md` / `.yaml` artifacts plus Kotlin modules (104 `.kt` files).  
**Alignment policy:** [DOCUMENTATION_ALIGNMENT_REPORT.md](DOCUMENTATION_ALIGNMENT_REPORT.md) — distinguishes **contradiction** (fix doc) vs **code-lag** (fix code, doc stays).  
**Proof of runtime:** [PHOENIX_FORGE_SYSTEM_ATLAS.md](PHOENIX_FORGE_SYSTEM_ATLAS.md) · [AUTHORITY_AND_REALITY_MAPPING.md](AUTHORITY_AND_REALITY_MAPPING.md).

**Canonical product names (do not rename):**

1. **Forge Profile** — Ezra’s lifelong identity record (CMOS direction)  
2. **Phoenix Forge Classroom Student Edition** — child experience shell (`:student-app`)  
3. **Phoenix Forge Classroom Teacher Edition** — parent expedition / curriculum command surface (`:teacher-app`)

**External (other repos):** Sovereign Deck, Godot reflection world, Phoenix automation.

---

## How to read completion

| Label | Meaning |
|-------|---------|
| **Doc %** | How mature the *document* is as a spec (not code) |
| **Code %** | How much is *implemented* in Kotlin for that concern |
| **Tag** | `CURRENT` · `STALE` · `ORPHAN` · `PLAN-OUTDATED` · `IMPLEMENTED` · `PARTIAL` |

---

## Evolution timeline (repo history)

| When | What changed |
|------|----------------|
| Init | Workspace split: teacher-edition / student-edition folders + `docs/` constitution |
| Curriculum wave | `curriculum-of-life`, taxonomy, starter lessons, Teacher planning docs |
| CMOS/PCAS pivot | Three-layer architecture, Memory OS, ForgeProfile spec, contracts |
| Implementation | `phoenix-forge-classroom-forge-profile` Gradle monorepo: core + profile app + student-app |
| Audit | `PHOENIX_FORGE_SYSTEM_ATLAS`, `AUTHORITY_AND_REALITY_MAPPING` (proof-based) |
| Deploy path | `DEPLOYMENT_REALITY`, `install-phone-apks.sh`, Forge Profile navigation fix |
| Teacher APK | `:teacher-app` MVP shell; naming restored to Phoenix Forge Classroom * |

**Drift to watch:** Empty `teacher-edition/android/` and `student-edition/android/` are placeholders only. Runnable APKs: `phoenix-forge-classroom-forge-profile/{forge-profile-app,student-app,teacher-app}`.

---

## System connection map (why things exist)

```text
                    ┌─────────────────────────────────────┐
                    │  Ezra (human) — center of design     │
                    └──────────────────┬──────────────────┘
                                       │
                    ┌──────────────────▼──────────────────┐
                    │  Forge Profile (CMOS / life record)    │
                    │  forge-profile-core + forge-profile-app│
                    └──────────┬───────────────┬────────────┘
                               │               │
              ContentProvider  │               │  future MemoryEvent write
              (read today)     │               │
         ┌─────────────────────▼───┐   ┌───────▼────────────────────────┐
         │ Student Edition          │   │ Teacher Edition                 │
         │ :student-app             │   │ :teacher-app                    │
         │ simulation + quests      │   │ intent tiles + expedition board │
         └────────────┬─────────────┘   └───────────────┬────────────────┘
                      │                                 │
                      └──────────┬──────────────────────┘
                                 │  TARGET (not built):
                                 │  IntentTile → Quest → MemoryEvent → Compass
                                 ▼
                    docs/contracts/* + reference-tiles/*.yaml
                    shared/sync-contract.md (spec only)
```

---

## Layer 0 — Repo root & infrastructure

| File | Purpose | Doc % | Code % | Tag | Connects to | Roadmap | Needs |
|------|---------|-------|--------|-----|-------------|---------|-------|
| `README.md` | Entry point | 90 | — | **CURRENT** | Three APK paths, roadmaps | Master P0 | Field-test matrix in DEPLOYMENT_REALITY |
| `gradlew` (root) | Wrapper duplicate | — | — | **ORPHAN** | forge-profile gradlew | Master | Delete or delegate doc |
| `scripts/install-phone-apks.sh` | Build+install 3 APKs | 90 | — | **CURRENT** | DEPLOYMENT_REALITY | Master 0.25 | Field-test matrix |
| `scripts/cline-essence-drift-check.sh` | Drift guard | 95 | — | **CURRENT** | registry, MASTER_PROTOCOL | Master 0.00 | Run in CI optional |
| `scripts/systemd/*` | Daily drift timer | 90 | — | **CURRENT** | drift-check.sh | Master 0.00 | User install |
| `scripts/README.md` | Script index | 90 | — | **CURRENT** | install + drift | Master | — |
| `registry/README.md` | Tier 0 registry index | 95 | 0 | **CURRENT** | phoenix-forge-classroom.yaml | Master 0.00 | — |
| `registry/phoenix-forge-classroom.yaml` | Tier 0 manifest | 95 | 0 | **CURRENT** | CONSTITUTION, drift script | Master 0.00 | Bump `current_step` |
| `docs/CONSTITUTION.md` | Tier 0 pointer | 95 | — | **CURRENT** | REPOSITORY_CONSTITUTION | Master | — |
| `docs/cline_essence/MASTER_PROTOCOL.md` | Agent execution protocol | 95 | — | **CURRENT** | 00_MASTER step gate | Master 0.00 | — |
| `docs/cline_essence/AGENT_EXTENSIONS.md` | Per-role extensions | 90 | — | **CURRENT** | MASTER_PROTOCOL | Master | — |
| `shared/README.md` | Cross-app + Forge Profile export index | 95 | 0 | **CURRENT** | sync-contract, PCAS SQL, FORGEPROFILE | Cross-app P1 | Kotlin `shared` module when contracts stabilize |
| `shared/sync-contract.md` | Cross-app sync spec | 70 | 0 | **DOC-COMPLETE** | Teacher, Student, Profile | Cross-app P1 | **Implement** |
| `shared/schemas/PCAS_DB_SCHEMA.sql` | PCAS tables | 60 | 0 | **PARTIAL** | PCAS_ARCHITECTURE | Student P2 | **Wire** or trim |

---

## Layer 1 — Constitution & vision (`docs/`)

| File | Purpose | Doc % | Code % | Tag | Connects to | Roadmap | Needs |
|------|---------|-------|--------|-----|-------------|---------|-------|
| `REPOSITORY_CONSTITUTION.md` | Tier-0 rules, 2046 test | 95 | 0 | **DOC-COMPLETE** | All apps | Master | CI enforcement (optional) |
| `THREE_LAYER_ARCHITECTURE.md` | CMOS / PCAS / Shells | 95 | 15 | **CURRENT** | Profile, contracts | Master | **Evolve** as code catches up |
| `UNIFIED_VISION.md` | Product philosophy | 90 | — | **CURRENT** | All | Master | Minor alignment pass |
| `HUMAN_MEMORY_PRESERVATION.md` | Human voice > AI | 85 | 5 | **CURRENT** | Forge Profile, capture | Profile P2 | **Implement** capture |
| `AGE_25_BACKCAST_DESIGN.md` | Adult legacy viewer | 80 | 0 | **DOC-COMPLETE** | CMOS export | Profile P4 | **Plan** |
| `CHILDHOOD_TIME_CAPSULE_SPEC.md` | Handover bundle | 75 | 10 | **PARTIAL** | .pfc export | Profile P3 | **Implement** export |
| `TEN_YEAR_EVOLUTION_STRATEGY.md` | Long horizon | 70 | — | **CURRENT** | Godot, shells | Master P4 | **Evolve** yearly |
| `DOCUMENTATION_ALIGNMENT_REPORT.md` | Alignment policy + tiers | 95 | — | **CURRENT** | This census | Master | Update when phases complete |
| `DEVELOPMENT_RULES.md` | Agent/human build rules | 95 | — | **CURRENT** | roadmaps/ | Master | — |
| `COMMIT_PREVIEW.md` | Commit notes | 50 | — | **ORPHAN** | — | — | Archive or delete |
| `MAGIC_LAYER.md` | Immersion / delight | 60 | 0 | **ORPHAN** | Not on spine | Master P4 | **Relink** or defer |
| `SHELL_OVERVIEW.md` | Shell lifecycle | 85 | 20 | **CURRENT** | Student, Godot | Student P3 | **Update** Student naming |
| `STUDENT_TEACHER_BOUNDARY.md` | Adult vs child surfaces | 90 | 40 | **CURRENT** | All three apps | Cross-app | **Update** teacher-app path |
| `DEPLOYMENT_REALITY.md` | Install truth | 95 | 80 | **CURRENT** | 3 APKs | All P0 | Field matrix fill-in |
| `PHOENIX_FORGE_SYSTEM_ATLAS.md` | Proof-based audit | 90 | — | **CURRENT** | Authority map | Master | **Update** Teacher APK + naming |
| `AUTHORITY_AND_REALITY_MAPPING.md` | Row-level status | 90 | — | **CURRENT** | Atlas | Master | Same refresh |
| `SYSTEM_ATLAS_SOURCE_INDEX.md` | Atlas input file list | 90 | — | **CURRENT** | teacher-app, roadmaps | Master | Keep in sync with census |
| `README.md` (docs index) | Doc map | 90 | — | **CURRENT** | All docs, shared/ | Master | — |

---

## Layer 2 — CMOS / Forge Profile (`docs/` + `forge-profile-*`)

| File | Purpose | Doc % | Code % | Tag | Connects to | Roadmap | Needs |
|------|---------|-------|--------|-----|-------------|---------|-------|
| `FORGEPROFILE_SPEC.md` | Identity schema target | 85 | 45 | **PARTIAL** | core models, provider | Profile P1–P3 | **Evolve** bonds/threads |
| `MEMORY_ENGINE_ARCHITECTURE.md` | Chronicle + artifacts | 80 | 25 | **PARTIAL** | Timeline, vault | Profile P2 | **Implement** registry |
| `MEMORY_PERSISTENCE_STRATEGY.md` | Durability / .pfc | 75 | 15 | **PARTIAL** | Export DTOs | Profile P3 | **Implement** export UX |
| `IDENTITY_FORMATION_ENGINE.md` | Identity threads | 70 | 0 | **DOC-COMPLETE** | Teacher confirm | Profile P4 | **Wire** to Student |
| `FAMILY_LEGACY_SYSTEM.md` | Family lore | 65 | 0 | **DOC-COMPLETE** | Chronicle | Profile P4 | **Plan** |
| `LEGACY_OBJECT_SYSTEM.md` | Heirloom objects | 65 | 0 | **DOC-COMPLETE** | Artifacts | Profile P4 | **Plan** |
| `EVENT_CAPTURE_PROTOCOL.md` | Life → archive | 70 | 10 | **PARTIAL** | MemoryEvent | Cross-app P2 | **Implement** |
| `forge-profile-core` (Kotlin) | Room, provider, models | — | 55 | **IMPLEMENTED** | student import | Profile P0–P1 | CMOS fields |
| `forge-profile-app` (Kotlin) | Steward UI | — | 50 | **PARTIAL** | Navigation fixed 06-04 | Profile P0 | Artifact capture |
| `ProfileContentProvider` | Cross-app read API | — | 70 | **IMPLEMENTED** | Student importer | Cross-app P0 | Write paths |

**Internal code note:** `DigitalHouse` / `DigitalHouseState` in Student Edition are **legacy internal class names** (house simulation state), not the product name. User-facing label is Phoenix Forge Classroom Student Edition.

---

## Layer 3 — PCAS / interpretation (`docs/`)

| File | Purpose | Doc % | Code % | Tag | Connects to | Roadmap | Needs |
|------|---------|-------|--------|-----|-------------|---------|-------|
| `PCAS_ARCHITECTURE.md` | Interpreter layer | 75 | 5 | **PARTIAL** | student simulation | Student P2 | **Implement** |
| `PCAS_IMPLEMENTATION_ROADMAP.md` | PCAS build order | 70 | — | **PARTIAL** | Atlas Part 10 | Master | **Merge** into roadmaps |
| `QUEST_ENGINE_DESIGN.md` | Narrative wrapper | 75 | 15 | **PARTIAL** | Teacher accept, Student quests | Teacher P2 | **Implement** |
| `SIGNIFICANCE_ENGINE.md` | Memory ranking | 65 | 0 | **DOC-COMPLETE** | Chronicle query | Profile P4 | **Plan** |
| `CHRONICLE_QUERY_ENGINE.md` | Semantic retrieval | 65 | 0 | **DOC-COMPLETE** | CMOS | Profile P4 | **Plan** |
| `CHILDHOOD_COMPASS.md` | Compass philosophy | 80 | 0 | **DOC-COMPLETE** | Teacher compass | Teacher P2 | **Implement** snapshot |
| `student-app` simulation stack | WorldOrchestrator spine | — | 65 | **IMPLEMENTED** | Local Room only | Student P0–P1 | CMOS bridge |

---

## Layer 4 — Contracts (`docs/contracts/`)

| File | Purpose | Doc % | Code % | Tag | Connects to | Roadmap | Needs |
|------|---------|-------|--------|-----|-------------|---------|-------|
| `CURRICULUM_OS_SCHEMA.md` | Atoms, capabilities, signals | 85 | 0 | **DOC-COMPLETE** | Teacher, Student signals | Cross-app P1 | **curriculum-core** module |
| `CURRICULUM_RUNTIME_FLOW.md` | Operational spine | 90 | 25 | **PARTIAL** | Atlas Part 7 | Cross-app P1 | **Implement** missing stages |
| `INTENT_TILE_CONTRACT.md` | Tile schema | 80 | 5 | **PARTIAL** | Teacher, YAML tile | Teacher P1 | **Kotlin types** |
| `MEMORY_EVENT_CONTRACT.md` | Unified ingestion | 80 | 10 | **PARTIAL** | Profile, Student LifeEvent | Cross-app P2 | **Bridge** |
| `TILE_EVENT_MAPPING.md` | Tile → event map | 75 | 0 | **DOC-COMPLETE** | Memory pipeline | Cross-app P2 | **Code** |
| `reference-tiles/secret-label-decoder.yaml` | Integration proof tile | 90 | 0 | **PARTIAL** | 10-step checklist | Cross-app P1 | **Loader** |
| `reference-tiles/README.md` | Proof checklist | 85 | 0 | **DOC-COMPLETE** | Teacher MVP | Cross-app P1 | Automate |

---

## Layer 5 — Teacher Edition docs (`phoenix-forge-classroom-teacher-edition/docs/`)

| File | Purpose | Doc % | Code % | Tag | Connects to | Roadmap | Needs |
|------|---------|-------|--------|-----|-------------|---------|-------|
| `curriculum-of-life.md` | Human canonical curriculum | 95 | 0 | **DOC-COMPLETE** | OS schema | Teacher P2 | Stay prose authority |
| `CURRICULUM_ATOMIZATION_GUIDE.md` | Pattern → atoms | 85 | 0 | **DOC-COMPLETE** | CURRICULUM_OS | Teacher P2 | Tooling |
| `curriculum-taxonomy.md` | Categories | 90 | 0 | **DOC-COMPLETE** | Starter pack | Teacher P2 | — |
| `CHILDHOOD_COMPASS_ENGINE.md` | CDNS engine | 80 | 0 | **DOC-COMPLETE** | Compass doc | Teacher P2 | **Compute** |
| `EXPEDITION_BOARD_UX.md` | Daily board UX | 90 | 45 | **PARTIAL** | teacher-app P1a–P1b | Teacher P1c | Drag reorder; 7-day view |
| `PLAN_GENERATION_RULES.md` | Morning stack generation | 85 | 0 | **DOC-COMPLETE** | Tiles | Teacher P2 | **Engine** |
| `teacher-edition-product-spec.md` | Tabs, sync, views | 85 | 10 | **PARTIAL** | teacher-app | Teacher P1 | 7-day/month views |
| `teacher-edition-feature-backlog.md` | Feature list | 80 | — | **CURRENT** | Roadmaps | Teacher P1–P4 | Prioritize |
| `starter-lessons-pack-01.md` | Content pack | 90 | 0 | **DOC-COMPLETE** | Tiles | Teacher P2 | Import to DB |
| `system-initialization.md` | Daily launch routine | 85 | 0 | **DOC-COMPLETE** | Board | Teacher P1 | In-app ritual |
| `lesson-activity-development-pipeline.md` | Content pipeline | 80 | 0 | **DOC-COMPLETE** | Registry | Teacher P3 | — |
| `teaching-methods-research.md` | Methods library | 85 | 0 | **DOC-COMPLETE** | Method map | Teacher P3 | — |
| `subcategory-method-map.md` | Methods per category | 85 | 0 | **DOC-COMPLETE** | Taxonomy | Teacher P3 | — |
| `inclusive-learning-supports.md` | Supports | 85 | 0 | **DOC-COMPLETE** | Tile detail | Teacher P1 | In tile popup |
| `thematic-playthroughs.md` | Multi-day arcs | 80 | 0 | **DOC-COMPLETE** | Narrative | Teacher P3 | — |
| `education-research-map.md` | Research index | 75 | 0 | **DOC-COMPLETE** | — | Teacher P4 | — |
| `teacher-app` (Kotlin, 14 files) | Teacher APK | — | 40 | **PARTIAL** | Board + tiles + detail | Teacher P1c–P2 | Drag; send to Student |

---

## Layer 6 — Student Edition docs (`phoenix-forge-classroom-student-edition/docs/`)

| File | Purpose | Doc % | Code % | Tag | Connects to | Roadmap | Needs |
|------|---------|-------|--------|-----|-------------|---------|-------|
| `EXPERIENCE_SHELL_SPEC.md` | Shell mandates | 85 | 30 | **PARTIAL** | student-app | Student P1 | Align UI to Hearthhome |
| `HEARTHHOME_MUSEUM_UX.md` | World-as-menu vision | 80 | 10 | **PARTIAL** | Home screen | Student P2 | **Implement** hub |
| `SPARK_COMPANION_UX.md` | Spark companion | 75 | 20 | **PARTIAL** | NPCEngine | Student P2 | Stateful companion |
| `IDENTITY_LENSES_UX.md` | Wisps / lenses | 70 | 0 | **DOC-COMPLETE** | IFE | Student P3 | **UI** |
| `student-app` (Kotlin) | Runnable Student Edition | — | 60 | **IMPLEMENTED** | Profile import | Student P0 | Teacher feed |

---

## Layer 7 — Plans & stale artifacts

| File | Purpose | Doc % | Code % | Tag | Connects to | Roadmap | Needs |
|------|---------|-------|--------|-----|-------------|---------|-------|
| `superpowers/plans/2026-06-02-forge-profile-app.md` | Historical build plan | 50 | 60 | **SUPERSEDED** | Use roadmaps/01 + census | Profile | Do not execute paths verbatim |
| `superpowers/plans/2026-06-02-student-edition-mvp.md` | Historical UME plan | 40 | 30 | **SUPERSEDED** | Wrong `android/` path | Student | Use `:student-app`; UME deferred |
| `teacher-edition/android/` | Placeholder | — | 0 | **ORPHAN** | teacher-app module | Teacher | README only |
| `student-edition/android/` | Placeholder | — | 0 | **ORPHAN** | student-app | Student | README only |
| `GODOT_MIGRATION_STRATEGY.md` | Future shell | 70 | 0 | **DOC-COMPLETE** | External repo | Master P4 | — |
| `OFFLINE_DATA_AND_SYNC_DESIGN.md` | Sync design | 75 | 10 | **PARTIAL** | shared contract | Cross-app P1 | **Implement** |

---

## Kotlin modules (code census summary)

| Module | Files ~ | Role | Code % ready | Roadmap owner |
|--------|---------|------|--------------|---------------|
| `forge-profile-core` | 21 | CMOS persistence, provider | 55% | Forge Profile |
| `forge-profile-app` | 17 | Steward UI | 50% | Forge Profile |
| `student-app` | 48 | Student Edition runtime | 60% | Student Edition |
| `teacher-app` | 14 | Teacher APK (Expedition Board MVP) | 40% | Teacher Edition |

---

## Layer 1b — Execution roadmaps (`docs/roadmaps/`)

| File | Purpose | Tag | North-star aligned? |
|------|---------|-----|---------------------|
| `00_MASTER_ROADMAP.md` | Sequencing, four tracks | **CURRENT** | Yes |
| `01_FORGE_PROFILE_ROADMAP.md` | Profile + Avatar P1-A | **CURRENT** | Yes |
| `02_STUDENT_EDITION_ROADMAP.md` | student-app paths | **CURRENT** | Yes |
| `03_TEACHER_EDITION_ROADMAP.md` | teacher-app P1a–P1d | **CURRENT** | Yes |
| `04_CROSS_APP_INTEGRATION_ROADMAP.md` | Tile→Memory spine | **CURRENT** | Yes |

---

## Document → roadmap index (quick lookup)

| Roadmap | Owns |
|---------|------|
| [00_MASTER_ROADMAP.md](roadmaps/00_MASTER_ROADMAP.md) | Sequencing, dependencies, four tracks overview |
| [01_FORGE_PROFILE_ROADMAP.md](roadmaps/01_FORGE_PROFILE_ROADMAP.md) | All Profile docs + core + app |
| [02_STUDENT_EDITION_ROADMAP.md](roadmaps/02_STUDENT_EDITION_ROADMAP.md) | student-edition/docs + student-app |
| [03_TEACHER_EDITION_ROADMAP.md](roadmaps/03_TEACHER_EDITION_ROADMAP.md) | teacher-edition/docs + teacher-app |
| [04_CROSS_APP_INTEGRATION_ROADMAP.md](roadmaps/04_CROSS_APP_INTEGRATION_ROADMAP.md) | contracts/, shared/, spine |

---

## Absolute connection table (what hooks to what)

| From | To | How | Status |
|------|-----|-----|--------|
| Teacher intent tile | Student quest | Export / sync file or provider | **Not built** |
| Student life event | Forge MemoryEvent | CMOS contract | **Not built** |
| Student completion | Teacher Compass | Aggregate signals | **Not built** |
| Forge Profile | Student | ContentProvider read | **Built** (import) |
| Forge Profile | Teacher | `ForgeProfileViewerScreen` stub | **PARTIAL** |
| reference-tile YAML | Teacher loader | Parse → Room | **Not built** |
| curriculum-of-life | Curriculum OS | Human → atoms | **Doc only** |
| WorldOrchestrator | CURRICULUM_RUNTIME_FLOW | De facto student spine | **Partial** |
| Sovereign Deck | Profile provider | External repo | **Out of scope here** |
| Godot world | Profile / PCAS | Future contract | **Planned** |

---

*This census should be updated when a roadmap phase completes or a doc is superseded.*
