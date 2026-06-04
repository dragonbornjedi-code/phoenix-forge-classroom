# Master Roadmap — Phoenix Forge Classroom

**Date:** 2026-06-04  
**Center:** Ezra’s Forge Profile (one child’s life record)  
**Three products in this repo:** Forge Profile · Phoenix Forge Classroom Student Edition · Phoenix Forge Classroom Teacher Edition

**Census (every file, connections):** [REPOSITORY_CENSUS_AND_CONNECTIONS.md](../REPOSITORY_CENSUS_AND_CONNECTIONS.md)  
**Deploy truth:** [DEPLOYMENT_REALITY.md](../DEPLOYMENT_REALITY.md)  
**Dev rules:** [DEVELOPMENT_RULES.md](../DEVELOPMENT_RULES.md) — extend roadmaps; do not redesign.

---

## Four tracks (one per app + integration)

| Track | Document | Primary outcome |
|-------|----------|-----------------|
| **A** | [01_FORGE_PROFILE_ROADMAP.md](01_FORGE_PROFILE_ROADMAP.md) | Durable identity + chronicle on device |
| **B** | [02_STUDENT_EDITION_ROADMAP.md](02_STUDENT_EDITION_ROADMAP.md) | Child experience shell stable + magical |
| **C** | [03_TEACHER_EDITION_ROADMAP.md](03_TEACHER_EDITION_ROADMAP.md) | Daily expedition command board real |
| **D** | [04_CROSS_APP_INTEGRATION_ROADMAP.md](04_CROSS_APP_INTEGRATION_ROADMAP.md) | One loop: Tile → Quest → Memory → Compass |

Tracks run **in parallel** where possible; **D** depends on A/B/C reaching P0 stability.

---

## Master phases (order of operations)

### Phase 0 — Truth on devices (NOW)

**All tracks P0**

- [ ] Rebuild & reinstall 3 APKs on **parent phone** ([DEPLOYMENT_REALITY.md](../DEPLOYMENT_REALITY.md))
- [ ] Forge Profile: bottom nav works (not static)
- [ ] Student Edition: correct launcher name
- [ ] Teacher Edition: Expedition Board + tile persist verified on phone
- [ ] Fill deployment matrix (phone yes/no, crashes)

**Docs:** Update [PHOENIX_FORGE_SYSTEM_ATLAS.md](../PHOENIX_FORGE_SYSTEM_ATLAS.md) Teacher row + Student naming  
**Docs:** Fix root [README.md](../../README.md) android paths

---

### Phase 1 — Stabilize each app alone

| Track | Deliverable |
|-------|-------------|
| A | Profile: **Avatar Studio P1-A** (layered preview, AvatarConfig export) + artifact capture |
| B | Student: no crash paths; import verified; home/quests usable |
| C | Teacher: P1c drag reorder; then P2 send tile → Student |

**No cross-app requirement yet** except Profile provider read.

---

### Phase 2 — Contract layer (shared truth)

**Track D P1** — before more UI polish

- [ ] Kotlin `IntentTile`, `MemoryEvent`, `QuestPayload` aligned to contracts
- [ ] `shared/` module or `forge-profile-core` export package
- [ ] reference-tile YAML loads in Teacher

**Docs owner:** [INTENT_TILE_CONTRACT.md](../contracts/INTENT_TILE_CONTRACT.md), [MEMORY_EVENT_CONTRACT.md](../contracts/MEMORY_EVENT_CONTRACT.md)

---

### Phase 3 — One complete loop (magic starts)

**Track D P2** — the irreversible milestone

1. Teacher: create/send tile (“Observation walk”)
2. Student: quest appears from tile
3. Student: complete → MemoryEvent
4. Forge Profile: timeline entry
5. Teacher: compass signal updates (minimal)

**Docs proof:** [reference-tiles/README.md](../contracts/reference-tiles/README.md) 10-step checklist

---

### Phase 4 — Depth per app

| Track | Focus |
|-------|--------|
| A | bonds, threads, .pfc export, chronicle promotion |
| B | Hearthhome UX, Spark companion, identity lenses |
| C | drag/reorder, tile detail popup, 7-day view, search, narrative wrapper |
| D | sync-contract implementation, tile→event automation |

---

### Phase 5 — External shells (other repos)

- Sovereign Deck (optional AI narrative) — **not in this repo**
- Godot reflection world — [GODOT_MIGRATION_STRATEGY.md](../GODOT_MIGRATION_STRATEGY.md)

---

## Four paths per track (strategy options)

Each app roadmap defines **four paths** (stabilize / feature / integration / vision). At master level:

| Path | Meaning | When to use |
|------|---------|-------------|
| **P0 Stabilize** | Build, install, crash-free, naming correct | Always first |
| **P1 Core** | App valuable standalone without full spine | Parent phone daily use |
| **P2 Integrate** | Participates in Tile→Memory loop | After contract layer |
| **P3 Vision** | CMOS-complete, Compass, Hearthhome, Godot | After loop proven |

**Rule:** Do not start P3 Vision on one app while P0 fails on another — you’ll rebuild docs, not childhood.

---

## Dependency graph

```text
P0 (all apps on phone)
  → P1 (each app standalone useful)
    → P2 contracts (shared/)
      → P3 one loop (D)
        → P4 depth (parallel)
          → P5 external repos
```

---

## What “MVP done” means (master definition)

**Not** three polished apps. **Yes:**

1. Three APKs on parent phone, stable enough to trust  
2. One full loop documented with screenshots or notes  
3. [DEPLOYMENT_REALITY.md](../DEPLOYMENT_REALITY.md) matrix filled  
4. No doc claims “IMPLEMENTED” without proof row in [AUTHORITY_AND_REALITY_MAPPING.md](../AUTHORITY_AND_REALITY_MAPPING.md)

---

## Attached documents by phase

| Phase | Must-read docs |
|-------|----------------|
| P0 | DEPLOYMENT_REALITY, SYSTEM_ATLAS, REPOSITORY_CENSUS |
| P1-A | FORGEPROFILE_SPEC, forge-profile README |
| P1-B | EXPERIENCE_SHELL_SPEC, student-edition README |
| P1-C | EXPEDITION_BOARD_UX, teacher-edition-product-spec |
| P2 | CURRICULUM_OS_SCHEMA, CURRICULUM_RUNTIME_FLOW, INTENT_TILE, MEMORY_EVENT, sync-contract |
| P3 | reference-tiles/secret-label-decoder.yaml, TILE_EVENT_MAPPING |
| P4 | IDENTITY_FORMATION_ENGINE, HEARTHHOME, SPARK_COMPANION, PLAN_GENERATION_RULES |

---

## Immediate next actions (this week)

1. Run `./scripts/install-phone-apks.sh` on parent phone  
2. Field-test Teacher P1a–P1b on phone (board, create, detail, complete, persist)  
3. Teacher P1c drag reorder, then Cross-app P1 shared IntentTile Kotlin  
4. Update root README + Atlas naming  
5. Field-test checklist in DEPLOYMENT_REALITY — mark Pass/Fail

---

*Sub-roadmaps contain file-level checklists and four paths in detail.*
