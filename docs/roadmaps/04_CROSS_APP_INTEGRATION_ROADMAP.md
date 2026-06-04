# Cross-App Integration — Roadmap (four paths)

**This is the spine.** Three apps are surfaces; integration is the product.

**Census:** [REPOSITORY_CENSUS_AND_CONNECTIONS.md](../REPOSITORY_CENSUS_AND_CONNECTIONS.md) — contracts + shared  
**Flow authority:** [CURRICULUM_RUNTIME_FLOW.md](../contracts/CURRICULUM_RUNTIME_FLOW.md)

---

## Four paths

| Path | Goal | Exit criteria |
|------|------|---------------|
| **P0 Stabilize** | Prove existing bridge | Profile provider + Student import on one device |
| **P1 Contracts** | Shared Kotlin truth types | IntentTile, MemoryEvent, QuestPayload compile in monorepo |
| **P2 One loop** | Curriculum Execution Spine live | Secret Label Decoder (or simpler tile) end-to-end |
| **P3 Platform** | Sync, compass recompute, automation | sync-contract implemented; optional Sovereign |

---

## P0 Stabilize (existing wire)

| Link | How | Doc | Status |
|------|-----|-----|--------|
| Profile → Student read | ContentProvider | FORGEPROFILE_SPEC | **Built** |
| Student import UI | `ForgeProfileImporter.kt` | DEPLOYMENT_REALITY | **Test on phone** |

**Not built:** Teacher ↔ anyone.

---

## P1 Contracts

| Deliverable | Attached doc | Owner modules |
|-------------|--------------|---------------|
| `IntentTile` Kotlin ↔ YAML | INTENT_TILE_CONTRACT, reference-tiles | teacher-app + shared |
| `MemoryEvent` Kotlin | MEMORY_EVENT_CONTRACT | forge-profile-core |
| `QuestPayload` / mapping | TILE_EVENT_MAPPING, CURRICULUM_RUNTIME_FLOW | student-app |
| YAML loader for reference tile | secret-label-decoder.yaml | teacher-app |
| Document transport choice | OFFLINE_DATA_AND_SYNC_DESIGN, sync-contract | **Decision:** file export vs provider vs both |

**Needs update**

- [shared/README.md](../../shared/README.md) — point to real module path  
- Root [README.md](../../README.md) — remove false AIDL claims until implemented

**Needs evolve**

- [CURRICULUM_OS_SCHEMA.md](../contracts/CURRICULUM_OS_SCHEMA.md) — trim to what P2 loop uses (YAGNI)

---

## P2 One loop (Curriculum Execution Spine)

Minimum loop (master MVP):

```text
Teacher: IntentTile "Observation walk"
    → export / handoff
Student: Quest "Find 3 patterns"
    → complete
Forge Profile: MemoryEvent recorded
    → timeline visible
Teacher: Compass signal "Nature +1" (minimal UI)
```

| Step | Attached doc | Implementation target |
|------|--------------|----------------------|
| 1 | INTENT_TILE_CONTRACT | teacher-app Room |
| 2 | sync-contract § handoff | file or provider |
| 3 | CURRICULUM_RUNTIME_FLOW § Student | QuestEngine ingest |
| 4 | MEMORY_EVENT_CONTRACT | ProfileRepository |
| 5 | CHILDHOOD_COMPASS_ENGINE | Teacher compass stub |

**Proof checklist:** [reference-tiles/README.md](../contracts/reference-tiles/README.md)

---

## P3 Platform

| Feature | Attached doc |
|---------|--------------|
| Full sync-contract | shared/sync-contract.md |
| PCAS runtime DB | PCAS_DB_SCHEMA.sql |
| TILE → event automation | TILE_EVENT_MAPPING.md |
| Narrative wrapper generation | QUEST_ENGINE_DESIGN.md |
| Event capture from real world | EVENT_CAPTURE_PROTOCOL.md |

---

## Connection matrix (target state)

| From → To | P0 | P1 | P2 | P3 |
|-----------|----|----|----|-----|
| Teacher → Student | — | types | send tile | auto sync |
| Student → Profile | import | types | MemoryEvent | promote chronicle |
| Profile → Teacher | — | read API | compass input | full viewer |
| Student → Teacher | — | — | completion signal | compass recompute |

---

## What NOT to build in this repo

| Item | Where |
|------|-------|
| Sovereign Deck mesh | Other repo |
| Godot shell | Other repo |
| Phoenix lesson generator | Other repo / tool |

Hooks only: narrative AI, optional profile read.

---

## Parallel work rules

1. **P0** on all three apps can run same day (install + smoke test).  
2. **P1 contracts** before Teacher “send to Student” button does anything real.  
3. **P2 loop** before Hearthhome visual overhaul.  
4. Update [AUTHORITY_AND_REALITY_MAPPING.md](../AUTHORITY_AND_REALITY_MAPPING.md) when a link moves from “Not built” to “Built”.

---

## Document attachment list (integration-owned)

| File | P1 | P2 | P3 |
|------|----|----|-----|
| CURRICULUM_OS_SCHEMA.md | read | partial impl | full |
| CURRICULUM_RUNTIME_FLOW.md | read | **authority** | maintain |
| INTENT_TILE_CONTRACT.md | **implement** | use | extend |
| MEMORY_EVENT_CONTRACT.md | **implement** | use | extend |
| TILE_EVENT_MAPPING.md | read | **implement** | automate |
| sync-contract.md | **decide** | implement | harden |
| secret-label-decoder.yaml | load | **prove** | more tiles |
| OFFLINE_DATA_AND_SYNC_DESIGN.md | align | implement | — |
| STUDENT_TEACHER_BOUNDARY.md | enforce | enforce | audit |
