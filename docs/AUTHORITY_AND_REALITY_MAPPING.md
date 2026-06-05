# Authority and Reality Mapping

Maps each major subsystem to **authority** (where the design lives), **implementation** (where code or data exists), **integration** (whether it is wired into the operational spine), and a **single** status.

**Status values (exactly one per row):** `IMPLEMENTED` | `PARTIAL` | `DOCUMENTED` | `DEPRECATED` | `UNKNOWN` | `ORPHANED`

**Atlas rules:**
1. One status per subsystem — details go in notes, not the status cell.
2. **Exists ≠ integrated** — file present but unwired → `PARTIAL`, not `IMPLEMENTED`.
3. Every row must be re-verifiable via **Verification Method**.

**Last verified:** 2026-06-04 (source inspection; `teacher-app` Expedition Board P1a–P1b; `./gradlew :student-app:assembleDebug` prior session)

---

## Operational spine (stage-level)

| Stage | Authority Source | Implementation Source | Integrated? | State | Verification Method |
|-------|------------------|----------------------|-------------|-------|---------------------|
| Curriculum Of Life | `phoenix-forge-classroom-teacher-edition/docs/curriculum-of-life.md` | `teacher-app` `domain/curriculum/*`, `ui/curriculum/*` | PARTIAL | 7 domains + Pack 01 + weekly audit in app; human docs remain canonical |
| Curriculum OS | `docs/contracts/CURRICULUM_OS_SCHEMA.md`, `CURRICULUM_RUNTIME_FLOW.md` | Contract docs + `reference-tiles/secret-label-decoder.yaml` | No Kotlin module; not loaded by any app | PARTIAL | Repo census; no `curriculum-core` code |
| Compass | `phoenix-forge-classroom-teacher-edition/docs/CHILDHOOD_COMPASS_ENGINE.md`, `docs/CHILDHOOD_COMPASS.md` | Not found in Kotlin | No | DOCUMENTED | No CompassSnapshot runtime |
| Intent Tiles | `docs/contracts/INTENT_TILE_CONTRACT.md` | `teacher-app/.../IntentTile.kt`, Room DAO; reference YAML | teacher-app local tiles only | PARTIAL | App persists tiles; YAML loader absent |
| Expedition Board | `teacher-edition/docs/EXPEDITION_BOARD_UX.md` | `teacher-app/ui/expedition/`, `TileRepository` | Yes — board + detail + complete | PARTIAL | Drag reorder (P1c) not built |
| Lesson Evidence | `reference-tiles/secret-label-decoder.yaml` `evidence_template` | Schema in YAML only | No Teacher UI | PARTIAL | Contract review |
| MemoryEvent | `docs/contracts/MEMORY_EVENT_CONTRACT.md` | Spec only; Student uses local `LifeEvent` / `StoryFragment` Room | Partial bridge via life events, not CMOS | PARTIAL | Code vs contract diff |
| Student Simulation | `student-app/domain/world/WorldOrchestrator.kt` | `phoenix-forge-classroom-forge-profile/student-app/` | Yes — closed loop in student-app | IMPLEMENTED | Source + build |
| Chronicle Artifact | `docs/MEMORY_ENGINE_ARCHITECTURE.md`, `FORGEPROFILE_SPEC.md` | Forge Profile Room timeline; `shared/schemas/PCAS_DB_SCHEMA.sql` | Forge Profile partial; CMOS not runtime | PARTIAL | Schema vs app code |

---

## Forge Profile (`phoenix-forge-classroom-forge-profile`)

| Subsystem | Authority Source | Implementation Source | State | Verification Method |
|-----------|------------------|----------------------|-------|---------------------|
| Identity / ForgeProfile | `docs/FORGEPROFILE_SPEC.md` | `forge-profile-core/.../ForgeProfile.kt`, Room `ProfileDatabase` | IMPLEMENTED | Source inspection |
| Local auth (steward) | Profile app docs / hardening | `LocalSecureAuthProvider.kt` | IMPLEMENTED | Source inspection |
| Timeline / snapshot chronicle | `FORGEPROFILE_SPEC.md` | `ProfileRepositoryImpl` timeline encode/decode | PARTIAL | Not full CMOS chronicle |
| Artifact Registry | `MEMORY_ENGINE_ARCHITECTURE.md` | Not in forge-profile-core | DOCUMENTED | Grep — no registry impl |
| ContentProvider read | Profile contract | `ProfileContentProvider.kt`, `ProfileContract.kt` | IMPLEMENTED | Source + manifest |
| Import / export | Provider + export DTOs | `ProfileExportReader.kt`, `ProfileExportDto.kt` | PARTIAL | Export path exists; PFC bundle TBD |
| Forge Profile App UI | `forge-profile-app` | Compose screens, sign-in gate, `DreamBoardViewModel` | PARTIAL | Sign-in + empty dream board; avatar/identity depth pending |

---

## Student Edition (runnable: `student-app` — standalone APK)

| Subsystem | Authority Source | Implementation Source | Exists | Integrated | State | Verification Method |
|-----------|------------------|----------------------|--------|------------|-------|---------------------|
| World Orchestrator | `CURRICULUM_RUNTIME_FLOW.md` | `domain/world/WorldOrchestrator.kt` | Yes | Yes — dispatch spine | IMPLEMENTED | Source + grep callers |
| Story / StoryGraph | Story graph docs | `StoryEngine.kt` → `StoryGraphEngine.kt` via orchestrator | Yes | Yes | IMPLEMENTED | Wiring trace |
| NPC + memory compression | Phase 2 seal docs | `NPCEngine.kt`, `NpcMemoryCompressor.kt` | Yes | Yes | IMPLEMENTED | Orchestrator calls |
| Emotional impact spine | `SimulationModels.kt` | `EmotionalImpactSpine.kt` | Yes | Yes | IMPLEMENTED | Orchestrator calls |
| World drift | Atlas / drift panel | `WorldDriftEngine.kt`, `WorldDriftPanel.kt` | Yes | Yes | IMPLEMENTED | Persisted in world meta |
| Behavior signals | Curriculum OS schema | `BehaviorSignalTracker.kt`, Room v3 | Yes | Yes | IMPLEMENTED | Repository + orchestrator |
| Quests | Student models | `QuestEngine.kt` | Yes | Yes | IMPLEMENTED | LifeEventCollector |
| Memory vault / gallery | Student domain | `MemoryVault.kt`, `StudentGallery.kt` | Yes | Yes (local) | IMPLEMENTED | UI + domain |
| Forge Profile import | `ForgeProfileImporter.kt` | ContentProvider client in student-app | Yes | Yes (optional) | PARTIAL | No live sync; copy-on-import |
| UI surfaces | Navigation | Home, Gallery, Vault, Quests, NPC, Import, Settings, Story Archive | Yes | Yes | IMPLEMENTED | `StudentNavHost.kt` |
| CMOS MemoryEvent pipeline | `MEMORY_EVENT_CONTRACT.md` | Local `LifeEvent` + `StoryFragment` only | Partial | No unified CMOS | PARTIAL | Contract vs code |

**Note:** `phoenix-forge-classroom-student-edition/` (Hearthhome / Spark UX specs) is **not** the running app. Runnable code lives under `phoenix-forge-classroom-forge-profile/student-app/`.

---

## Teacher Edition (runnable: `teacher-app` — docs in `phoenix-forge-classroom-teacher-edition`)

| Subsystem | Authority Source | Implementation Source | State | Verification Method |
|-----------|------------------|----------------------|-------|---------------------|
| Teacher APK | `teacher-edition-product-spec.md` | `phoenix-forge-classroom-forge-profile/teacher-app/` | IMPLEMENTED | Module build + install |
| Expedition Board | `docs/EXPEDITION_BOARD_UX.md` | `ExpeditionBoardScreen.kt`, `TileRepository` | PARTIAL | P1a–P1b done; P1c drag pending |
| Intent tiles (local) | `INTENT_TILE_CONTRACT.md` | `IntentTile.kt`, `IntentTileDao.kt`, seed data | PARTIAL | Slim model; no YAML loader |
| Tile field guide | `EXPEDITION_BOARD_UX.md` | `TileDetailScreen.kt`, `TileDetailViewModel.kt` | IMPLEMENTED | Save + mark complete persist |
| Curriculum Of Life | `phoenix-forge-classroom-teacher-edition/docs/curriculum-of-life.md` | `teacher-app` `domain/curriculum/*`, `ui/curriculum/*` | PARTIAL | 7 domains + Pack 01 + weekly audit in app; human docs remain canonical |
| Curriculum (human) | `docs/curriculum-of-life.md` | Bundled in Teacher app catalog | PARTIAL | App mirrors prose; markdown stays source of truth |
| Curriculum taxonomy | `docs/curriculum-taxonomy.md` | Markdown only | DOCUMENTED | File read |
| Atomization guide | `docs/CURRICULUM_ATOMIZATION_GUIDE.md` | Markdown only | DOCUMENTED | File read |
| Starter lessons Pack 01 | `docs/starter-lessons-pack-01.md` | `StarterLessonsPack01.kt` | IMPLEMENTED | 7 lessons + expedition import |
| Compass engine | `docs/CHILDHOOD_COMPASS_ENGINE.md` | No Kotlin aggregates | DOCUMENTED | Grep |
| Plan generation rules | `docs/PLAN_GENERATION_RULES.md` | No generator code | DOCUMENTED | Grep |
| Reference tile YAML | `reference-tiles/secret-label-decoder.yaml` | Not loaded by app | PARTIAL | Contract review |
| Forge Profile viewer | `teacher-edition-product-spec.md` | `ForgeProfileViewerScreen.kt` (stub) | PARTIAL | UI exists; full provider read TBD |
| Send to Student | `04_CROSS_APP_INTEGRATION_ROADMAP.md` | Not implemented | DOCUMENTED | No handoff code |
| Weekly audit | `curriculum-of-life.md` § Weekly Teacher Audit | `WeeklyAuditScreen.kt` + prefs | IMPLEMENTED | Draft save on device |

**Note:** `phoenix-forge-classroom-teacher-edition/android/` is empty. Runnable code is `:teacher-app` in the forge-profile monorepo.

---

## Cross-cutting / shared

| Subsystem | Authority Source | Implementation Source | State | Verification Method |
|-----------|------------------|----------------------|-------|---------------------|
| PCAS DB schema | `docs/PCAS_ARCHITECTURE.md` | `shared/schemas/PCAS_DB_SCHEMA.sql` | PARTIAL | SQL only, no runtime DB |
| Sync contract | `shared/sync-contract.md` | Markdown | DOCUMENTED | No sync impl in apps |
| Identity Formation Engine | `docs/IDENTITY_FORMATION_ENGINE.md` | Not wired to student-app threads | DOCUMENTED | Vision doc |
| Narrative Wrapper | `docs/QUEST_ENGINE_DESIGN.md` | Not implemented (Teacher accept flow absent) | DOCUMENTED | No engine code |
| TILE → MemoryEvent map | `docs/contracts/TILE_EVENT_MAPPING.md` | Spec only | DOCUMENTED | No emission code |
| Reference tile proof | `reference-tiles/README.md` | `secret-label-decoder.yaml` | PARTIAL | 10-step checklist not automated |

---

## Orphaned / deprecated candidates

| Item | Location | State | Notes |
|------|----------|-------|-------|
| `phoenix-forge-classroom-student-edition/` UX shell specs | docs only | ORPHANED | UX authority; runnable code is `student-app` (Phoenix Forge Classroom Student Edition) |
| `docs/MAGIC_LAYER.md` | docs | UNKNOWN | Not referenced by current spine |
| `docs/GODOT_MIGRATION_STRATEGY.md` | docs | DOCUMENTED | Future; no Godot project in repo |
| `registry/`, `scripts/` | README stubs | ORPHANED | Not connected to three-app pipeline |
| Root `gradlew` | repo root | ORPHANED | Delegates to forge-profile; duplicate of module gradlew |

---

## Related documents

- [PHOENIX_FORGE_SYSTEM_ATLAS.md](PHOENIX_FORGE_SYSTEM_ATLAS.md) — consolidated atlas
- [SYSTEM_ATLAS_SOURCE_INDEX.md](SYSTEM_ATLAS_SOURCE_INDEX.md) — file census
- [contracts/CURRICULUM_RUNTIME_FLOW.md](contracts/CURRICULUM_RUNTIME_FLOW.md) — target spine
