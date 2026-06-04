# Phoenix Forge Classroom Student Edition — Roadmap (four paths)

**Product name:** Phoenix Forge Classroom Student Edition  
**Not:** Digital House (deprecated label; internal `DigitalHouse*` classes may remain until refactor)  
**Code module:** `:student-app` (Gradle)  
**Docs folder:** `phoenix-forge-classroom-student-edition/docs/`  
**Package:** `com.phoenixforge.student`

**Census:** [REPOSITORY_CENSUS_AND_CONNECTIONS.md](../REPOSITORY_CENSUS_AND_CONNECTIONS.md) — Layer 6

---

## Four paths

| Path | Goal | Exit criteria |
|------|------|---------------|
| **P0 Stabilize** | Runnable child app on parent phone for QA | Launches, bottom nav, quests run, Room persists |
| **P1 Core** | Ezra could use for 15 min without parent | Home, quests, gallery/vault, Spark NPC stable |
| **P2 Integrate** | Teacher tiles become quests; events hit Profile | One quest from external tile |
| **P3 Vision** | Hearthhome + lenses + companion | Match HEARTHHOME / SPARK / IDENTITY_LENSES UX |

---

## P0 Stabilize

| Task | Code | Doc |
|------|------|-----|
| Launcher name correct | `student-app/.../strings.xml` | Done 2026-06-04 |
| Install on phone | DEPLOYMENT_REALITY | User verify |
| Cold start + bootstrap | `StudentWorldBootstrap.kt` | Built |
| Import Forge Profile | `ForgeProfileImporter.kt` | Test on device |
| No Digital House in UI strings | Home, Settings, Quests | Done 2026-06-04 |

**Attached docs (read, don’t block P0)**

| Document | Doc % | Code % | Action |
|----------|-------|--------|--------|
| [EXPERIENCE_SHELL_SPEC.md](../../phoenix-forge-classroom-student-edition/docs/EXPERIENCE_SHELL_SPEC.md) | 85% | 30% | **Evolve** after P1 |
| [superpowers/plans/2026-06-02-student-edition-mvp.md](../superpowers/plans/2026-06-02-student-edition-mvp.md) | 40% | — | **STALE** — wrong android path |

---

## P1 Core

| Feature | Code | Attached doc |
|---------|------|--------------|
| WorldOrchestrator loop | `WorldOrchestrator.kt` | CURRICULUM_RUNTIME_FLOW |
| Quests UI | `QuestEngine.kt`, `QuestsScreen.kt` | QUEST_ENGINE_DESIGN |
| Story / drift / NPC | simulation package | PCAS_ARCHITECTURE |
| Gallery + vault | `StudentGallery`, `MemoryVault` | EXPERIENCE_SHELL_SPEC |
| Settings + offline copy | `SettingsScreen.kt` | OFFLINE_DATA_AND_SYNC_DESIGN |

**Needs dev**

- Quest copy and balance (not curriculum-driven yet)  
- Crash fixes from phone QA  

**Needs plan**

- Rename internal `DigitalHouse` → `StudentHome` (optional cleanup, P1 tail)

---

## P2 Integrate

| Task | Attached doc |
|------|--------------|
| Receive IntentTile / quest payload | INTENT_TILE_CONTRACT, sync-contract |
| Emit MemoryEvent on complete | MEMORY_EVENT_CONTRACT, TILE_EVENT_MAPPING |
| Stop using only local `LifeEvent` | CURRICULUM_RUNTIME_FLOW |

**Cross-track:** [04_CROSS_APP_INTEGRATION_ROADMAP.md](04_CROSS_APP_INTEGRATION_ROADMAP.md)

---

## P3 Vision

| Feature | Attached doc | Code today |
|---------|--------------|------------|
| Hearthhome hub (world as menu) | HEARTHHOME_MUSEUM_UX | Home screen only |
| Spark companion loop | SPARK_COMPANION_UX | NPC lines only |
| Identity lenses / Wisps | IDENTITY_LENSES_UX | None |
| Godot shell handoff | GODOT_MIGRATION_STRATEGY | External repo |

---

## Simulation spine (already built — protect in P0)

```text
LifeEventCollector → WorldOrchestrator → StoryEngine / NPCEngine / Drift / BehaviorSignalTracker → Room
```

**Do not rip this out** for UX rework; wrap it with Hearthhome in P3.

---

## Orphan folder warning

`phoenix-forge-classroom-student-edition/android/` is **empty**. All Kotlin is in `phoenix-forge-classroom-forge-profile/student-app/`. Docs in student-edition folder remain **authority for UX** until merged into implementation.

---

## Dependencies

- **Uses:** Forge Profile ContentProvider (P0 import)  
- **Blocked by Teacher P2:** for real missions  
- **Feeds:** Profile MemoryEvents (Cross-app P2)
