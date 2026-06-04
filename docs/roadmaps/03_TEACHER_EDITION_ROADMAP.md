# Teacher Edition — Index (master steps only)

**Schedule:** [00_MASTER_ROADMAP.md](00_MASTER_ROADMAP.md) — do not use P0/P1/P1a–P1f labels for work tracking.

**Code:** `:teacher-app` · `com.phoenixforge.classroom.teacher`  
**UX docs:** `phoenix-forge-classroom-teacher-edition/docs/`

---

## Step map

| Master steps | Work |
|--------------|------|
| 0.15 | Launcher label |
| 0.33–0.36 | Board, create tile, field guide, complete persist (**done on device — verify at 0.36**) |
| 0.41 | ForgeProfileViewerScreen |
| 0.51–0.55 | Board polish, drag reorder, steward L3, start-day export |
| 0.79 | YAML reference tile load |
| 1.02 | Send tile stub |
| 1.51 | Send Observation tile |
| 2.04 | Compass signal stub |
| 2.55 | Profile viewer complete |
| 3.51–3.58 | 7-day, month, search, narrative, compass, curriculum tags |
| 4.05 | Chronicle approval UI |

---

## Files by step

| Step | Primary files |
|------|----------------|
| 0.33–0.34 | `ExpeditionBoardScreen.kt`, `TileRepository.kt` |
| 0.35–0.36 | `TileDetailScreen.kt`, `TileDetailViewModel.kt` |
| 0.52–0.53 | `ExpeditionBoardScreen.kt` drag + `sortOrder` |
| 0.54 | `TileDetailScreen.kt` L3 fields |
| 0.41, 2.55 | `ForgeProfileViewerScreen.kt` |
| 0.79 | YAML loader (new) |
| 1.02 | handoff from `TileRepository` |

---

## Docs

| Doc | Steps |
|-----|-------|
| [EXPEDITION_BOARD_UX.md](../../phoenix-forge-classroom-teacher-edition/docs/EXPEDITION_BOARD_UX.md) | 0.33–0.36, 0.51–0.55 |
| [teacher-edition-product-spec.md](../../phoenix-forge-classroom-teacher-edition/docs/teacher-edition-product-spec.md) | 3.51–3.53 |
| [CHILDHOOD_COMPASS_ENGINE.md](../../phoenix-forge-classroom-teacher-edition/docs/CHILDHOOD_COMPASS_ENGINE.md) | 2.04, 3.55 |
| [curriculum-of-life.md](../../phoenix-forge-classroom-teacher-edition/docs/curriculum-of-life.md) | 3.56 (human canon) |

---

## Legacy path names

| Old label | Master steps |
|-----------|--------------|
| P0 | 0.33–0.36 |
| P1a board | 0.33–0.34 (done) |
| P1b field guide | 0.35–0.36 (done) |
| P1c drag | 0.52–0.53 |
| P1d steward | 0.54 |
| P2 | 0.79, 1.02, 1.51, 2.01+ |
| P3 | 3.51–3.58 |

**Build:** `phoenix-forge-classroom-forge-profile/teacher-app` only — not `teacher-edition/android/`.
