# Documentation Alignment Report

**North star:** [UNIFIED_VISION.md](UNIFIED_VISION.md) · [REPOSITORY_CONSTITUTION.md](REPOSITORY_CONSTITUTION.md)  
**Every file register (76 docs):** [REPOSITORY_CENSUS_AND_CONNECTIONS.md](REPOSITORY_CENSUS_AND_CONNECTIONS.md)  
**Runtime proof:** [PHOENIX_FORGE_SYSTEM_ATLAS.md](PHOENIX_FORGE_SYSTEM_ATLAS.md) · [AUTHORITY_AND_REALITY_MAPPING.md](AUTHORITY_AND_REALITY_MAPPING.md)  
**Last updated:** 2026-06-04

---

## Can we guarantee zero contradictions repo-wide?

**No.** Nobody can honestly promise that for a living repo with 76 spec documents, 104 Kotlin files, and intentional **vision ahead of code**.

What we **do** guarantee:

| Guarantee | Meaning |
|-----------|---------|
| **Single north star** | If docs disagree on *purpose*, [UNIFIED_VISION.md](UNIFIED_VISION.md) wins; other docs get updated. |
| **Single path truth** | Runnable APKs are only under `phoenix-forge-classroom-forge-profile/{forge-profile-app,student-app,teacher-app}` — never `*/android/` placeholders. |
| **Single product names** | Forge Profile · Phoenix Forge Classroom Student Edition · Phoenix Forge Classroom Teacher Edition ([DEVELOPMENT_RULES.md](DEVELOPMENT_RULES.md)). |
| **Per-file register** | Every `.md`/`.yaml` is listed in [REPOSITORY_CENSUS_AND_CONNECTIONS.md](REPOSITORY_CENSUS_AND_CONNECTIONS.md) with tag + roadmap owner. |
| **Proof-based status** | Atlas/Authority rows cite code paths or say **Not found** — not wishful “IMPLEMENTED”. |

---

## Two kinds of “misalignment” (do not confuse them)

| Type | Definition | Example | Fix |
|------|------------|---------|-----|
| **CONTRADICTION** | Doc A and Doc B disagree on facts or paths | Old `shared/README` UME vs CMOS vision | **Update doc** immediately |
| **CODE-LAG** | Doc describes target; Kotlin not there yet | Avatar `AvatarConfig` in spec vs 5-field `Avatar` | **Roadmap owns code**; doc stays |

Code-lag is **not** a documentation bug. It is the planned gap between P3 vision and P1 implementation.

---

## Repository inventory (why “23 files” on GitHub ≠ full audit)

Recent commits touched **~14–23 paths per commit**. The **full** register is **76** markdown/yaml files + **104** Kotlin sources — see census layers 0–7.

**Do not use GitHub’s “files changed” count as alignment coverage.**

---

## Alignment status (summary)

| Tag | Count (approx.) | Meaning |
|-----|-----------------|--------|
| **CURRENT** / **ALIGNED** | 50+ | Same end goal; paths and names correct |
| **PARTIAL** / **ALIGNED-CODE-LAG** | 25+ | Doc correct; implementation incomplete |
| **SUPERSEDED** | 2 | `docs/superpowers/plans/*` — historical only |
| **ORPHAN** | 4 | Empty `android/`, root `gradlew`, `COMMIT_PREVIEW`, `MAGIC_LAYER` off-spine |
| **CONTRADICTION** | **0 known** | After 2026-06-04 shared/README + Atlas Teacher rows |

---

## Tier 0 — Constitutional (must not contradict)

| File | Status |
|------|--------|
| `REPOSITORY_CONSTITUTION.md` | ALIGNED |
| `THREE_LAYER_ARCHITECTURE.md` | ALIGNED |
| `UNIFIED_VISION.md` | ALIGNED — Ezra, three products, one loop, Avatar/Godot pointers |
| `DEVELOPMENT_RULES.md` | ALIGNED |

---

## Tier 1 — Product entry points

| File | Status |
|------|--------|
| `/README.md` | ALIGNED — Forge Profile first |
| `shared/README.md` | ALIGNED — CMOS center; UME/USE deferred |
| `registry/README.md` | ALIGNED — content packs only |
| `DEPLOYMENT_REALITY.md` | ALIGNED — update matrix after field test |
| `PHOENIX_FORGE_SYSTEM_ATLAS.md` | ALIGNED — teacher-app P1a–P1b |
| `AUTHORITY_AND_REALITY_MAPPING.md` | ALIGNED |
| `REPOSITORY_CENSUS_AND_CONNECTIONS.md` | ALIGNED — **master file list** |
| `roadmaps/00`–`04` | ALIGNED |
| `FORGEPROFILE_SPEC.md` | ALIGNED — Avatar Studio target schema |
| `GODOT_MIGRATION_STRATEGY.md` | ALIGNED — import bundle + avatar 3D |

**Runnable code:** `phoenix-forge-classroom-forge-profile/teacher-app` (not `teacher-edition/android/`).

---

## Tier 2 — Contracts (doc-complete, code-lag expected)

| File | Status |
|------|--------|
| `contracts/INTENT_TILE_CONTRACT.md` | CODE-LAG — slim Room `IntentTile` in teacher-app |
| `contracts/MEMORY_EVENT_CONTRACT.md` | CODE-LAG — Student `LifeEvent` local |
| `contracts/CURRICULUM_OS_SCHEMA.md` | CODE-LAG |
| `contracts/CURRICULUM_RUNTIME_FLOW.md` | CODE-LAG — Student orchestrator partial |
| `reference-tiles/secret-label-decoder.yaml` | CODE-LAG — not loaded |
| `shared/sync-contract.md` | CODE-LAG — not implemented |
| `PCAS_IMPLEMENTATION_ROADMAP.md` | ALIGNED — Milestone -1 = **spec done**, runtime open |

---

## Tier 3 — Teacher pedagogy (16 files in `teacher-edition/docs/`)

All **ALIGNED** with north star. Expedition Board UX is **CODE-LAG** for drag/7-day only — P1a–P1b **implemented** in `teacher-app`.

---

## Tier 4 — Student UX (4 files in `student-edition/docs/`)

All **ALIGNED-CODE-LAG** — Hearthhome/Spark vision vs `student-app` Home/NPC UI.

**Runnable code:** `:student-app` only.

**Boundary:** [STUDENT_TEACHER_BOUNDARY.md](STUDENT_TEACHER_BOUNDARY.md) — not `student-experience-boundary.md`.

---

## Tier 5 — Historical plans (do not execute)

| File | Status |
|------|--------|
| `superpowers/plans/2026-06-02-forge-profile-app.md` | **SUPERSEDED** → `roadmaps/01`, `forge-profile-app` module |
| `superpowers/plans/2026-06-02-student-edition-mvp.md` | **SUPERSEDED** → `:student-app`; UME path wrong |

---

## Phantom files (never existed — do not cite)

`ARCHITECTURAL_BLUEPRINT.md`, `SPARK_MEMORY_KEEPER_SPEC.md`, `END_GOAL_AND_NORTH_STAR.md`, `ALIGNMENT_AUDIT_2026-06-04.md`, `student-edition/docs/experience-boundary.md`, `teacher-edition/docs/product-spec.md`.

---

## Known intentional gaps (not contradictions)

| Gap | Owner |
|-----|--------|
| Cross-app Tile → Quest → MemoryEvent loop | `04_CROSS_APP_INTEGRATION_ROADMAP.md` |
| Avatar Studio P1-A (full `AvatarConfig`) | `01_FORGE_PROFILE_ROADMAP.md` |
| Teacher P1c drag reorder | `03_TEACHER_EDITION_ROADMAP.md` |
| CMOS / unified MemoryEvent runtime | Profile + Cross-app P2 |
| Godot project | External repo; import spec in `GODOT_MIGRATION_STRATEGY.md` |
| UME/USE minigames | Deferred → `registry/` |

---

## When to update what

1. **Phase completes** → roadmap checkbox + Atlas + Authority + census row  
2. **New Kotlin module** → census Layer 8 + forge-profile README  
3. **Doc path wrong** → fix immediately (contradiction)  
4. **Vision expands** → UNIFIED_VISION first, then FORGEPROFILE_SPEC, then roadmaps  

Do **not** add new top-level audit markdown files — update **this report** and **REPOSITORY_CENSUS**.
