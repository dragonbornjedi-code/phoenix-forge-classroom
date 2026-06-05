# Phoenix Forge System Atlas

**As-implemented audit** of the Phoenix Forge Classroom monorepo.  
**Not** a vision document. Every claim cites proof or states **Not found**.

| Field | Value |
|-------|--------|
| Atlas version | 1.2 (teacher-app P1a–P1b) |
| Last verified | 2026-06-04 |
| North star | [UNIFIED_VISION.md](UNIFIED_VISION.md) · [REPOSITORY_CONSTITUTION.md](REPOSITORY_CONSTITUTION.md) |
| Alignment report | [DOCUMENTATION_ALIGNMENT_REPORT.md](DOCUMENTATION_ALIGNMENT_REPORT.md) |
| Census | [REPOSITORY_CENSUS_AND_CONNECTIONS.md](REPOSITORY_CENSUS_AND_CONNECTIONS.md) |
| Authority map | [AUTHORITY_AND_REALITY_MAPPING.md](AUTHORITY_AND_REALITY_MAPPING.md) |

### Atlas rules

1. **One status per subsystem** — `IMPLEMENTED` | `PARTIAL` | `DOCUMENTED` | `DEPRECATED` | `UNKNOWN` | `ORPHANED`
2. **Exists ≠ integrated** — unwired code is `PARTIAL`
3. **Where is the proof?** — every claim lists doc path, code path, or "Not found"

### Runnable modules (proof)

| Module | Path | Build verified |
|--------|------|----------------|
| Forge Profile core | `phoenix-forge-classroom-forge-profile/forge-profile-core` | Yes (prior `assembleDebug`) |
| Forge Profile app | `phoenix-forge-classroom-forge-profile/forge-profile-app` | Yes |
| Phoenix Forge Classroom Student Edition | `phoenix-forge-classroom-forge-profile/student-app` | Yes (`:student-app:assembleDebug`) |
| Phoenix Forge Classroom Teacher Edition | `phoenix-forge-classroom-forge-profile/teacher-app` | Yes (`:teacher-app:assembleDebug` MVP shell) |
| teacher-edition/android/ placeholder | empty | **Not used** — docs only |
| Legacy student-edition APK | `phoenix-forge-classroom-student-edition/android/` | **Not found** (empty directory) |

---

## Part 1: Constitution Summary

**Claim:** Tier-0 Git truth, CMOS vs PCAS vs Experience Shells, 2046 test govern the repo.

| Proof type | Location |
|------------|----------|
| Authority | [REPOSITORY_CONSTITUTION.md](REPOSITORY_CONSTITUTION.md), [THREE_LAYER_ARCHITECTURE.md](THREE_LAYER_ARCHITECTURE.md), [UNIFIED_VISION.md](UNIFIED_VISION.md) |
| Implementation | Enforced by documentation and review practice — **no automated constitution linter** |
| State | **DOCUMENTED** |

**Details:** Architecture mandates are real and extensive. Runtime enforcement is human/process, not CI gates.

---

## Part 2: State of Each App (by subsystem)

### Forge Profile

| Subsystem | State | Proof (implementation) | Gaps |
|-----------|-------|------------------------|------|
| Identity | **IMPLEMENTED** | `forge-profile-core/.../ForgeProfile.kt`, Room | — |
| Chronicle (timeline) | **PARTIAL** | Timeline events in Profile DB | Not full CMOS Living Chronicle |
| Artifact Registry | **DOCUMENTED** | [MEMORY_ENGINE_ARCHITECTURE.md](MEMORY_ENGINE_ARCHITECTURE.md) | No registry code in forge-profile |
| ContentProvider | **IMPLEMENTED** | `ProfileContentProvider.kt`, permission enforced | — |
| Import / Export | **PARTIAL** | `ProfileExportReader.kt`, provider contract | PFC handover bundle not end-to-end |

### Student Edition (`student-app` — actual runnable shell)

| Subsystem | State | Proof (implementation) | Integrated? |
|-----------|-------|------------------------|-------------|
| World Simulation | **IMPLEMENTED** | `WorldOrchestrator.kt`, `WorldEngine.kt` | Yes |
| Story System | **IMPLEMENTED** | `StoryEngine` → `StoryGraphEngine` via orchestrator | Yes |
| NPC System | **IMPLEMENTED** | `NPCEngine.kt`, memory compression | Yes |
| Drift System | **IMPLEMENTED** | `WorldDriftEngine.kt`, UI panel, Room drift JSON | Yes |
| Memory System | **PARTIAL** | `MemoryVault`, `StudentGallery`, local Room | Not CMOS |
| Quest System | **IMPLEMENTED** | `QuestEngine.kt`, Quests UI | Yes |
| UI Surfaces | **IMPLEMENTED** | Home, Gallery, Vault, Quests, NPC, Import, Settings, Story Archive | Yes |
| Forge import bridge | **PARTIAL** | `ForgeProfileImporter.kt` (ContentProvider client) | Copy-on-import only |

**Claim correction:** Student simulation is **not** "partial missing persistence" globally — it has Room persistence (`student_house.db` v3). It is **partial** relative to CMOS/chronicle integration.

**Orphan note:** [phoenix-forge-classroom-student-edition/](../phoenix-forge-classroom-student-edition/) (Hearthhome, Spark UX) = **ORPHANED** vision docs; not the running APK.

### Teacher Edition (`teacher-app`)

| Subsystem | State | Proof | Gaps |
|-----------|-------|-------|------|
| Teacher APK | **IMPLEMENTED** | `teacher-app/` builds; Room `TeacherDatabase` | YAML reference tile not loaded |
| Expedition Board | **IMPLEMENTED** | `ExpeditionBoardScreen.kt`, `ExpeditionBoardViewModel.kt` | Drag reorder (P1c) not built |
| Intent Tiles (local) | **PARTIAL** | `IntentTile.kt`, `IntentTileDao.kt`, `TileRepository.kt` | Slim model vs full contract; no YAML loader |
| Tile detail / field guide | **IMPLEMENTED** | `TileDetailScreen.kt`, `TileDetailViewModel.kt` | Steward L3 dimensions planned |
| Curriculum | **DOCUMENTED** | [curriculum-of-life.md](../phoenix-forge-classroom-teacher-edition/docs/curriculum-of-life.md) | Not loaded at runtime |
| Compass | **DOCUMENTED** | [CHILDHOOD_COMPASS_ENGINE.md](../phoenix-forge-classroom-teacher-edition/docs/CHILDHOOD_COMPASS_ENGINE.md) | No Kotlin aggregates |
| Send to Student | **DOCUMENTED** | Cross-app roadmap | No quest handoff yet |
| Audit Layer | **DOCUMENTED** | Weekly audit in curriculum-of-life | Manual prose only |

**Runnable module:** `phoenix-forge-classroom-forge-profile/teacher-app` — not `teacher-edition/android/` (empty placeholder).

---

## Part 3: Cross-App Contracts

| Contract | Authority | Consumed by code? | State |
|----------|-----------|-------------------|-------|
| [CURRICULUM_OS_SCHEMA.md](contracts/CURRICULUM_OS_SCHEMA.md) | docs/contracts | No — docs + reference YAML | **PARTIAL** |
| [CURRICULUM_RUNTIME_FLOW.md](contracts/CURRICULUM_RUNTIME_FLOW.md) | docs/contracts | Aligns with student orchestrator; Teacher stages absent | **PARTIAL** |
| [INTENT_TILE_CONTRACT.md](contracts/INTENT_TILE_CONTRACT.md) | docs/contracts | `teacher-app` Room `IntentTile`; reference YAML not loaded | **PARTIAL** |
| [MEMORY_EVENT_CONTRACT.md](contracts/MEMORY_EVENT_CONTRACT.md) | docs/contracts | Student uses different local events | **PARTIAL** |
| [TILE_EVENT_MAPPING.md](contracts/TILE_EVENT_MAPPING.md) | docs/contracts | Not implemented | **DOCUMENTED** |
| [shared/sync-contract.md](../shared/sync-contract.md) | shared/ | Not implemented | **DOCUMENTED** |
| Forge Profile ContentProvider | `ProfileContract.kt` | Student `ForgeProfileImporter` | **IMPLEMENTED** (read path) |

**Working tree note:** Curriculum OS contracts and reference tile exist locally; may not yet be on `origin/main` (check git).

---

## Part 4: Curriculum OS

**Claim:** Operational model (capabilities, atoms, signals) bridges human curriculum to Compass and simulation.

| Layer | Authority | Implementation | State |
|-------|-----------|----------------|-------|
| Human curriculum | curriculum-of-life.md | Markdown | **DOCUMENTED** |
| OS schema | CURRICULUM_OS_SCHEMA.md | Markdown | **DOCUMENTED** |
| Runtime flow | CURRICULUM_RUNTIME_FLOW.md | Markdown | **DOCUMENTED** |
| Atomization | CURRICULUM_ATOMIZATION_GUIDE.md | Markdown | **DOCUMENTED** |
| Reference tile | secret-label-decoder.yaml | YAML data | **PARTIAL** |
| Kotlin/Room curriculum-core | CURRICULUM_OS_SCHEMA §3 | **Not found** | **DOCUMENTED** (spec only) |
| Compass aggregates | CompassSnapshot in schema | **Not found** in code | **DOCUMENTED** |

**Longitudinal unit:** Capabilities (not lesson completion) — encoded in schema; **not yet computed at runtime**.

**Proof path for architecture:** [reference-tiles/README.md](contracts/reference-tiles/README.md) 10-step checklist — **not automated**.

---

## Part 5: Student Simulation

**Claim:** Event-driven closed loop with memory, story graph, drift, and persistence.

**Proof (wired path):**

```text
LifeEventCollector
  → WorldOrchestrator.dispatch()
      → BehaviorSignalTracker
      → EmotionalImpactSpine
      → NPCEngine + NpcMemoryCompressor
      → StoryEngine → StoryGraphEngine
      → WorldDriftEngine
      → WorldEngine (Room: progress, house, NPCs, story_fragments, world_meta, behavior_signals)
```

| Component | File | State |
|-----------|------|-------|
| Orchestrator | `domain/world/WorldOrchestrator.kt` | **IMPLEMENTED** |
| Story graph | `domain/simulation/StoryGraphEngine.kt` | **IMPLEMENTED** (via StoryEngine) |
| Drift | `domain/simulation/WorldDriftEngine.kt` | **IMPLEMENTED** |
| Absence narrative | `WorldEventType.ABSENCE_RETURNED` in orchestrator | **IMPLEMENTED** |
| CMOS bridge | — | **Not found** |

**Build proof:** `./gradlew :student-app:assembleDebug` — SUCCESS (2026-06-03 session).

---

## Part 6: Chronicle Architecture

**Claim:** Long-term childhood archive (CMOS + ForgeProfile + PCAS).

| Layer | Authority | Implementation | State |
|-------|-----------|----------------|-------|
| CMOS / Memory OS | MEMORY_ENGINE_ARCHITECTURE.md | Spec | **DOCUMENTED** |
| PCAS schema | PCAS_DB_SCHEMA.sql | SQL file only | **PARTIAL** |
| Chronicle query | CHRONICLE_QUERY_ENGINE.md | No engine code | **DOCUMENTED** |
| Forge Profile timeline | FORGEPROFILE_SPEC.md | Room in forge-profile-core | **PARTIAL** |
| Student story ledger | — | `story_fragments` Room table | **PARTIAL** (local, not CMOS) |
| Identity threads (IFE) | IDENTITY_FORMATION_ENGINE.md | Not in student-app | **DOCUMENTED** |

---

## Part 7: Authority Model & Operational Spine

**Parent is final authority** ([CURRICULUM_RUNTIME_FLOW.md](contracts/CURRICULUM_RUNTIME_FLOW.md) §1). Compass recommends; does not prescribe.

### Operational spine (each stage: authority | implementation | state)

```text
Curriculum Of Life
  authority: teacher-edition/docs/curriculum-of-life.md
  implementation: same (markdown)
  state: DOCUMENTED
        ↓
Curriculum OS (capabilities, atoms, signals)
  authority: docs/contracts/CURRICULUM_OS_SCHEMA.md
  implementation: contracts + reference-tiles/secret-label-decoder.yaml
  state: PARTIAL
        ↓
Compass (capability sunlight)
  authority: CHILDHOOD_COMPASS_ENGINE.md
  implementation: Not found
  state: DOCUMENTED
        ↓
Plan Generation
  authority: PLAN_GENERATION_RULES.md
  implementation: Not found
  state: DOCUMENTED
        ↓
Intent Tiles
  authority: INTENT_TILE_CONTRACT.md
  implementation: teacher-app Room IntentTile + seed tiles
  state: PARTIAL (contract + YAML loader pending)
        ↓
Expedition Board (parent reorder + accept)
  authority: EXPEDITION_BOARD_UX.md
  implementation: ExpeditionBoardScreen.kt, TileRepository
  state: PARTIAL (create/detail/complete done; drag reorder P1c)
        ↓
Narrative Wrapper (decorates accepted plan)
  authority: QUEST_ENGINE_DESIGN.md
  implementation: Not found
  state: DOCUMENTED
        ↓
Lesson Session + Evidence + Lesson Signals
  authority: reference tile evidence_template
  implementation: Not found (Teacher UI)
  state: DOCUMENTED
        ↓
MemoryEvent → CMOS buffer
  authority: MEMORY_EVENT_CONTRACT.md
  implementation: Not found (unified pipeline)
  state: DOCUMENTED
        ↓
Student Simulation
  authority: WorldOrchestrator (de facto)
  implementation: student-app domain layer
  state: IMPLEMENTED
        ↓
Chronicle Artifact (approved)
  authority: MEMORY_ENGINE_ARCHITECTURE.md
  implementation: Forge Profile timeline PARTIAL; CMOS not runtime
  state: PARTIAL
        ↓
Compass Recalibration
  authority: CURRICULUM_OS_SCHEMA CompassSnapshot
  implementation: Not found
  state: DOCUMENTED
```

### Decision vs decoration vs reaction

```text
                    ┌─────────────────┐
                    │  PARENT/STEWARD │  ← decides + approves chronicle
                    └────────┬────────┘
                             │
         ┌───────────────────┼───────────────────┐
         ▼                   ▼                   ▼
   Expedition (planned)   Evidence (future)   Chronicle approval
         │
         ▼
   Narrative Wrapper ──▶ decorates (DOCUMENTED — not built)
         │
         ▼
   Lesson Session (real world)
         │
         ├──────────────────▶ MemoryEvent (DOCUMENTED — not unified)
         │
         └──────────────────▶ WorldOrchestrator (IMPLEMENTED)
                                    ├─ EmotionalImpactSpine
                                    ├─ StoryGraphEngine
                                    └─ WorldDriftEngine

   Compass ◀── DOCUMENTED (reads sessions — not built)
         └──▶ Plan Generator (DOCUMENTED)
```

---

## Part 8: Repository Audit (machine-auditable)

| Subsystem | Owner Doc | Implementation Location | State | Last Verified | Verification Method | Known Gaps |
|-----------|-----------|-------------------------|-------|---------------|---------------------|------------|
| Repository Constitution | REPOSITORY_CONSTITUTION.md | docs only | DOCUMENTED | 2026-06-03 | File read | No CI enforcement |
| Forge Profile identity | FORGEPROFILE_SPEC.md | forge-profile-core | IMPLEMENTED | 2026-06-03 | Source inspection | — |
| Forge ContentProvider | ProfileContract.kt | ProfileContentProvider.kt | IMPLEMENTED | 2026-06-03 | Source inspection | — |
| Curriculum Of Life | curriculum-of-life.md | teacher-edition/docs | DOCUMENTED | 2026-06-03 | File read | — |
| Curriculum OS schema | CURRICULUM_OS_SCHEMA.md | docs/contracts | PARTIAL | 2026-06-03 | Contract review | No Kotlin module |
| Reference Intent Tile | secret-label-decoder.yaml | docs/contracts/reference-tiles | PARTIAL | 2026-06-03 | YAML review | No loader; checklist open |
| Childhood Compass | CHILDHOOD_COMPASS_ENGINE.md | Not found | DOCUMENTED | 2026-06-03 | Repo census | No runtime |
| Expedition Board | EXPEDITION_BOARD_UX.md | teacher-app/ui/expedition/ | PARTIAL | 2026-06-04 | Source inspection | Drag reorder; send to Student |
| WorldOrchestrator | CURRICULUM_RUNTIME_FLOW.md | student-app/.../WorldOrchestrator.kt | IMPLEMENTED | 2026-06-03 | Wiring trace | — |
| StoryGraphEngine | CURRICULUM_OS_SCHEMA.md | student-app/.../StoryGraphEngine.kt | IMPLEMENTED | 2026-06-03 | StoryEngine → orchestrator | — |
| WorldDriftEngine | same | student-app/.../WorldDriftEngine.kt | IMPLEMENTED | 2026-06-03 | Orchestrator + Room | — |
| CMOS MemoryEvent | MEMORY_EVENT_CONTRACT.md | Not found unified | DOCUMENTED | 2026-06-03 | Contract vs code | Local LifeEvent only |
| PCAS SQL schema | PCAS_ARCHITECTURE.md | shared/schemas/PCAS_DB_SCHEMA.sql | PARTIAL | 2026-06-03 | File exists | No runtime DB |
| Teacher Edition APK | teacher-edition-product-spec.md | teacher-app/ | IMPLEMENTED | 2026-06-05 | assembleDebug + install-phone-apks | Drag reorder (0.52–0.53); cross-app send |
| Student-edition shell docs | EXPERIENCE_SHELL_SPEC.md | student-edition/docs only | ORPHANED | 2026-06-03 | vs student-app | Superseded in practice |
| ForgeProfileImporter | STUDENT_TEACHER_BOUNDARY.md | student-app forgeimport | PARTIAL | 2026-06-03 | Source | No live sync |

---

## Part 9: Technical Debt Register

Explicit gaps (not roadmap promises):

| ID | Debt | State today | Impact |
|----|------|-------------|--------|
| TD-01 | Teacher drag reorder (0.52–0.53) + cross-app send (1.02+) not built | PARTIAL | Loop blocked at handoff |
| TD-02 | Curriculum OS **not loaded from code** — YAML/contracts only | PARTIAL | Reference tile proof blocked at step 1 |
| TD-03 | **No unified MemoryEvent** ingestion — student local events ≠ CMOS contract | PARTIAL | Spine break at MemoryEvent stage |
| TD-04 | **CompassSnapshot** not computed | DOCUMENTED | Capability-longitudinal analytics absent |
| TD-05 | **CMOS / PCAS** schema without runtime | PARTIAL | Chronicle architecture on paper |
| TD-06 | **student-edition/** UX docs disconnected from **student-app** implementation | ORPHANED | Confusing navigation for contributors |
| TD-07 | **Identity threads (IFE)** not wired to student simulation | DOCUMENTED | Thread reinforcement manual |
| TD-08 | Reference tile **10-step checklist** not automated | PARTIAL | Integration proof manual |
| TD-09 | Curriculum OS + reference tile **may be uncommitted** on main | UNKNOWN | Atlas drift vs origin |

---

## Part 10: Roadmap (derived from docs — not commitments)

Ordered by dependency on operational spine ([CURRICULUM_RUNTIME_FLOW.md](contracts/CURRICULUM_RUNTIME_FLOW.md) §7):

1. **Validate reference tile** — run 10-step proof on Secret Label Decoder (manual or harness).
2. **curriculum-core** — Kotlin types + YAML loader for Intent Tiles (enables step 1 automation).
3. **Teacher Edition 0.52–0.53 + 1.02** — drag reorder; send tile → Student quest (Expedition Board 0.33–0.36 **done**).
4. **CompassSnapshot** — compute capability trends from session evidence (observation language only).
5. **MemoryEvent bridge** — map tile completion + student WorldEvent → CMOS buffer contract.
6. **Forge Profile chronicle** — align timeline with promoted MemoryEvents.
7. **Consolidate student shell docs** — mark student-edition UX as future shell or merge naming.

**Explicitly deferred:** Bulk lesson library, Godot migration, full PCAS query engine until spine proof passes.

---

## Appendix: Atlas corrections

| Date | Correction |
|------|------------|
| 2026-06-03 | Do not cite empty `teacher-edition/android/` as implementation proof. |
| 2026-06-04 | `teacher-app` ships Expedition Board + Room tiles + field guide (P1a–P1b). Compass, YAML loader, and cross-app loop remain **PARTIAL** / **DOCUMENTED**. |

---

## Quick links

- [docs/README.md](README.md) — doc index
- [contracts/reference-tiles/secret-label-decoder.yaml](contracts/reference-tiles/secret-label-decoder.yaml) — integration test tile
- [phoenix-forge-classroom-forge-profile/README.md](../phoenix-forge-classroom-forge-profile/README.md) — Gradle modules
