# Phoenix Forge Classroom Teacher Edition — Roadmap (four paths)

**Product name:** Phoenix Forge Classroom Teacher Edition  
**Code module:** `:teacher-app`  
**Docs folder:** `phoenix-forge-classroom-teacher-edition/docs/`  
**Package:** `com.phoenixforge.classroom.teacher`

**Your landing page vision:** Daily **Expedition Board** (magnetic/chalkboard tiles), drag-reorder, double-tap full detail, plus 7-day overview, monthly theme, search — fed by curriculum + Student/Profile signals.

**Census:** [REPOSITORY_CENSUS_AND_CONNECTIONS.md](../REPOSITORY_CENSUS_AND_CONNECTIONS.md) — Layer 5

---

## Four paths

| Path | Goal | Exit criteria |
|------|------|---------------|
| **P0 Stabilize** | Real APK on parent phone | Launches, sample board, no crash |
| **P1 Core** | **Today’s expedition board** usable | Create tiles, reorder, open detail sheet, persist locally |
| **P2 Integrate** | Send tile → Student quest; see completion | One tile round-trip |
| **P3 Vision** | Morning generator, 7-day/month, search, narrative wrapper, Sovereign AI hook | Per product-spec + PLAN_GENERATION_RULES |

---

## P0 Stabilize

| Task | Status |
|------|--------|
| `teacher-app` module in monorepo | Done |
| Launcher label Phoenix Forge Classroom Teacher Edition | Done 2026-06-04 |
| Install on parent phone | User verify |
| Expedition Board + Room + tile detail | **Done** 2026-06-04 |
| Drag reorder (P1c) | **Next** |

**Attached docs**

| Document | Doc % | Action |
|----------|-------|--------|
| [EXPEDITION_BOARD_UX.md](../../phoenix-forge-classroom-teacher-edition/docs/EXPEDITION_BOARD_UX.md) | 90% | **Implement** P1 |
| [teacher-edition-product-spec.md](../../phoenix-forge-classroom-teacher-edition/docs/teacher-edition-product-spec.md) | 85% | **Implement** tabs P3 |
| [teacher-edition-feature-backlog.md](../../phoenix-forge-classroom-teacher-edition/docs/teacher-edition-feature-backlog.md) | 80% | **Prioritize** from P1 |

**Do not use yet:** empty `teacher-edition/android/` — build `:teacher-app` only.

---

## P1 Core — build decomposition

### P1a Expedition Board surface

| Step | Files | Status |
|------|-------|--------|
| Board screen + grid | `ui/expedition/ExpeditionBoardScreen.kt` | **Done** |
| Tile component + FAB | same | **Done** |
| Room + seed tiles | `TileRepository.ensureSeedData()` | **Done** |
| Scroll + empty state | same | **Done** |

**Exit:** Board opens, tiles visible, no crash on cold start.

---

### P1b Tile interaction (field guide)

| Step | Files | Status |
|------|-------|--------|
| Tap tile → detail route | `TeacherNavGraph.kt` | **Done** |
| Materials / coaching / evidence fields | `TileDetailScreen.kt` | **Done** |
| Save details | `TileDetailViewModel.kt` | **Done** |
| Mark completed | `TileRepository.markComplete` | **Done** |

**Exit:** Every tile opens a field guide; complete + reopen app → status persists.

**Doc:** [EXPEDITION_BOARD_UX.md](../../phoenix-forge-classroom-teacher-edition/docs/EXPEDITION_BOARD_UX.md)

---

### P1c Tile reordering

| Step | Status |
|------|--------|
| `sortOrder` on `IntentTile` | **Done** (schema) |
| Drag / drop UI | **Not started** |
| Persist order on drop | **Not started** |

**Exit:** Close app → reopen → tile order preserved.

---

### P1d Steward completion (levels)

| Level | Status |
|-------|--------|
| L1 Complete + timestamp + lock | **Partial** (`markComplete`) |
| L2 Quick note | **Done** (`evidenceNotes`) |
| L3 Mental/emotional/physical/educational/behavioral | **Planned** (P1d-next) |

**Exit:** Completed tiles queryable; chronicle promotion = Cross-app P2.

---

### P1e–P1f (remaining P1)

| Feature | Attached doc | Priority |
|---------|--------------|----------|
| Create tile sheet | EXPEDITION_BOARD_UX | **Done** |
| Tile states enum | INTENT_TILE_CONTRACT | **Done** |
| “Start day” export list | system-initialization | P1f |
| Send to Student Edition | 04_CROSS_APP | P2 |

**Content sources (doc-only today → import later)**

| Document | Doc % | Use |
|----------|-------|-----|
| [curriculum-of-life.md](../../phoenix-forge-classroom-teacher-edition/docs/curriculum-of-life.md) | 95% | Human authority |
| [starter-lessons-pack-01.md](../../phoenix-forge-classroom-teacher-edition/docs/starter-lessons-pack-01.md) | 90% | Seed tiles |
| [curriculum-taxonomy.md](../../phoenix-forge-classroom-teacher-edition/docs/curriculum-taxonomy.md) | 90% | Tags/domains |

**100% planned, 0% UI**

| Document | When |
|----------|------|
| [PLAN_GENERATION_RULES.md](../../phoenix-forge-classroom-teacher-edition/docs/PLAN_GENERATION_RULES.md) | P3 — morning stack |
| [CHILDHOOD_COMPASS_ENGINE.md](../../phoenix-forge-classroom-teacher-edition/docs/CHILDHOOD_COMPASS_ENGINE.md) | P2–P3 — sunlight |
| [thematic-playthroughs.md](../../phoenix-forge-classroom-teacher-edition/docs/thematic-playthroughs.md) | P3 |

---

## P2 Integrate

| Task | Attached doc |
|------|--------------|
| Load reference tile YAML | secret-label-decoder.yaml |
| Send tile to Student | sync-contract, INTENT_TILE |
| Read Profile / Student signals for compass bias | CHILDHOOD_COMPASS, CURRICULUM_OS_SCHEMA |
| Evidence notes on complete | TILE_EVENT_MAPPING |

---

## P3 Vision

| Feature | Attached doc |
|---------|--------------|
| 7-day overview | teacher-edition-product-spec |
| Monthly thematic overview | thematic-playthroughs |
| Search / tags | curriculum-taxonomy |
| Narrative wrapper (day story) | QUEST_ENGINE_DESIGN — **core UX, not optional** |
| Sovereign Deck AI story generation | External repo hook |
| Forge Profile viewer tab | product-spec |

---

## Claude Downloads merge plan (helpful parts only)

| Keep | Skip |
|------|------|
| IntentTile.kt, ExpeditionBoard*, Room, Repository | Separate Gradle project |
| Hilt setup | Package `com.phoenixforge.teacher` — use `classroom.teacher` |
| | Newer AGP 8.7 until monorepo upgrades |

---

## Dependencies

- **Needs:** Shared IntentTile types (Cross-app P1) before send-to-student  
- **Feeds:** Student quests, Profile memory, Compass  
- **Does not replace:** curriculum-of-life prose — app is a **view** over that truth
