# Student Edition — Index (master steps only)

**Schedule:** [00_MASTER_ROADMAP.md](00_MASTER_ROADMAP.md) — do not use P0/P1 labels for work tracking.

**Code:** `:student-app` · `com.phoenixforge.student`  
**UX docs:** `phoenix-forge-classroom-student-edition/docs/` (not `student-edition/android/`)

---

## Step map

| Master steps | Work |
|--------------|------|
| 0.13–0.14 | Launcher name, no “Digital House” UI |
| 0.30–0.32 | Cold start, bootstrap, home |
| 0.37 | Import Forge Profile |
| 0.42–0.48 | Quests, gallery, vault, settings, story, NPC |
| 0.68–0.73 | P1 usability + orchestrator + polish |
| 1.03 | Receive quest payload |
| 1.52–1.53 | Loop 50% quest flow |
| 2.01–2.02 | Full loop participant |
| 2.54 | Full avatar import |
| 4.63 | Companions hub (Companion / Whisps / Pet Space) |
| 3.01–3.06 | Hearthhome, Spark, lenses, cleanup |
| 4.02 | TILE_EVENT_MAPPING emitter |

---

## Files by step

| Step | Primary files |
|------|----------------|
| 0.31 | `StudentWorldBootstrap.kt` |
| 0.37 | `ForgeProfileImporter.kt`, `ImportForgeProfileScreen.kt` |
| 0.42 | `QuestEngine.kt`, `QuestsScreen.kt` |
| 0.70 | `WorldOrchestrator.kt` (protect) |
| 0.71 | `StudentGallery.kt`, `MemoryVault.kt` |
| 1.03 | quest ingest route + VM |
| 1.52–1.53 | `LifeEventCollector.kt`, completion path |
| 4.63 | `CompanionsHubScreen.kt`, `NpcRoomViewModel.kt` |
| 3.01 | `HomeScreen.kt` → Hearthhome hub |

---

## Simulation spine (protect from step 0.70)

```text
LifeEventCollector → WorldOrchestrator → StoryEngine / NPCEngine / Drift / BehaviorSignalTracker → Room
```

---

## Docs

| Doc | Steps |
|-----|-------|
| [EXPERIENCE_SHELL_SPEC.md](../../phoenix-forge-classroom-student-edition/docs/EXPERIENCE_SHELL_SPEC.md) | 0.68, 3.01–3.03 |
| [HEARTHHOME_MUSEUM_UX.md](../../phoenix-forge-classroom-student-edition/docs/HEARTHHOME_MUSEUM_UX.md) | 3.01 |
| [SPARK_COMPANION_UX.md](../../phoenix-forge-classroom-student-edition/docs/SPARK_COMPANION_UX.md) | 3.02 |
| [CURRICULUM_RUNTIME_FLOW.md](../contracts/CURRICULUM_RUNTIME_FLOW.md) | 1.52+, 2.01+ |

---

## Legacy path names

| Legacy phase (historical) | Master steps |
|---------------------------|--------------|
| Device stabilization | 0.30–0.37, 0.42–0.48 |
| Standalone polish | 0.68–0.73 |
| Loop participant | 1.03, 1.52–1.53, 2.01–2.02 |
| Student depth | 3.01–3.06 |
