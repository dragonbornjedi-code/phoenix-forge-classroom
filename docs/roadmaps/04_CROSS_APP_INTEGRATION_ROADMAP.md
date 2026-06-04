# Cross-App Integration — Index (master steps only)

**Schedule:** [00_MASTER_ROADMAP.md](00_MASTER_ROADMAP.md) — spine steps only here.

**Flow authority:** [CURRICULUM_RUNTIME_FLOW.md](../contracts/CURRICULUM_RUNTIME_FLOW.md)

---

## Step map

| Master steps | Work |
|--------------|------|
| 0.17–0.18 | reference-tile YAML + sync-contract present |
| 0.38–0.40 | Provider smoke + deployment matrix |
| 0.76–0.85 | Shared Kotlin types + YAML load + transport doc |
| 1.01–1.06 | Integration scaffold |
| 1.51–1.55 | Loop 50% |
| 2.01–2.07 | **Loop proven** (irreversible) |
| 4.01–4.06 | CMOS bridge |
| 4.53 | sync-contract implementation |
| 4.55 | PCAS DB decision |

---

## Loop spine (master steps 2.01–2.05)

```text
2.01 Teacher tile → handoff
2.02 Student quest + complete → MemoryEvent shape
2.03 Profile timeline
2.04 Teacher compass signal
2.05 reference-tiles 10-step checklist
```

---

## Contracts by step

| Step | Contract / artifact |
|------|---------------------|
| 0.76–0.78 | INTENT_TILE, MEMORY_EVENT, QuestPayload |
| 0.79 | [secret-label-decoder.yaml](../contracts/reference-tiles/secret-label-decoder.yaml) |
| 0.81 | [sync-contract.md](../../shared/sync-contract.md), OFFLINE_DATA |
| 2.05 | [reference-tiles/README.md](../contracts/reference-tiles/README.md) |
| 4.02 | [TILE_EVENT_MAPPING.md](../contracts/TILE_EVENT_MAPPING.md) |

---

## Connection matrix (target at 5.00)

| Link | Master steps when done |
|------|------------------------|
| Teacher → Student | 1.02, 2.01 |
| Student → Profile | 0.37, 2.02–2.03 |
| Profile → Teacher | 0.38, 2.04, 2.55 |
| Student → Teacher | 2.04 |

---

## Out of repo (hooks only at 5.00)

| Item | Notes |
|------|-------|
| Sovereign Deck | Optional narrative |
| Godot shell | Import contracts at 2.51 |

---

## Legacy path names

| Old label | Master steps |
|-----------|--------------|
| P0 bridge | 0.38–0.40 |
| P1 contracts | 0.76–0.85 |
| P2 one loop | 2.01–2.07 |
| P3 platform | 4.01–4.06, 4.53 |
