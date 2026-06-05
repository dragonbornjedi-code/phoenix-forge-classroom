# Forge Profile — Index (master steps only)

**Schedule:** [00_MASTER_ROADMAP.md](00_MASTER_ROADMAP.md) — do not use P0/P1/P1-A labels for work tracking.

**Code:** `forge-profile-core` · `forge-profile-app` · `com.phoenixforge.profile`

---

## Step map

| Master steps | Work |
|--------------|------|
| 0.11 | Bootstrap Ezra (`ProfileBootstrap.kt`) |
| 0.12 | Bottom nav + dashboard tabs |
| 0.27–0.29 | Device P0: nav, cold start, identity card |
| 0.38–0.39 | Provider smoke `/profile`, `/avatar` |
| 0.44–0.45 | Timeline + memory capsule screens |
| 0.56–0.62 | Avatar Studio expansion |
| 0.63–0.66 | Artifact capture + timeline + steward copy |
| 1.04 | MemoryEvent write API |
| 2.03 | MemoryEvent on timeline |
| 2.51–2.57 | Avatar export, Godot hints, depth |
| 4.03 | Chronicle promotion |
| 4.51 | Artifact registry |

---

## Files by step (implementation hints)

| Step | Primary files |
|------|----------------|
| 0.56 | `ui/studio/AvatarStudioScreen.kt`, new `AvatarPreview.kt` |
| 0.57 | `AvatarViewModel.kt`, `AvatarStudioScreen.kt` |
| 0.58 | `ProfileRepositoryImpl.kt` |
| 0.59 | `domain/model/`, `AvatarEntity`, `ProfileExportDto.kt` |
| 0.60–0.61 | `MemoryCapsuleScreen.kt`, capture intents |
| 0.63–0.64 | camera/mic integration |
| 0.66 | `ProfileRepositoryImpl` timeline |
| 1.04 | `ProfileContentProvider.kt`, ingest API |

---

## Docs (read when implementing step)

| Doc | Steps |
|-----|-------|
| [FORGEPROFILE_SPEC.md](../FORGEPROFILE_SPEC.md) | 0.56–0.62, 2.51–2.56 |
| [GODOT_MIGRATION_STRATEGY.md](../GODOT_MIGRATION_STRATEGY.md) | 2.51 |
| [MEMORY_ENGINE_ARCHITECTURE.md](../MEMORY_ENGINE_ARCHITECTURE.md) | 0.66, 4.51 |
| [DEPLOYMENT_REALITY.md](../DEPLOYMENT_REALITY.md) | 0.26–0.40 |

---

## Legacy path names (do not schedule)

| Old label | Master steps |
|-----------|--------------|
| Device + provider smoke | 0.27–0.29, 0.38–0.45 |
| P1-A Avatar | 0.56–0.62 |
| P1-B Capture | 0.63–0.67 |
| P2 Integrate | 1.04, 2.03, 4.03 |
| P3 Vision | 2.52, 2.56, 4.51–4.54 |
