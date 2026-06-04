# Documentation Alignment Report

**North star:** [UNIFIED_VISION.md](UNIFIED_VISION.md) · [REPOSITORY_CONSTITUTION.md](REPOSITORY_CONSTITUTION.md)  
**Proof of implementation:** [PHOENIX_FORGE_SYSTEM_ATLAS.md](PHOENIX_FORGE_SYSTEM_ATLAS.md) · [AUTHORITY_AND_REALITY_MAPPING.md](AUTHORITY_AND_REALITY_MAPPING.md)  
**Last updated:** 2026-06-04

This report tracks **living docs only** — files that exist on disk today. No separate audit file; update this report when code or docs move.

---

## Summary

| Status | Count | Meaning |
|--------|-------|---------|
| ALIGNED | 45 | Matches constitution + north star; keep |
| ALIGNED-CODE-LAG | 22 | Doc good; implementation behind — roadmaps own work |
| STALE | 0 | After 2026-06-04 atlas/authority fix |
| PHANTOM-REF | 0 | Removed from this report |
| ARCHIVE | 3 | `COMMIT_PREVIEW`, `MAGIC_LAYER` (optional), superpowers plans |

---

## Tier 0 — Must stay valid

| File | Status | Notes |
|------|--------|-------|
| `REPOSITORY_CONSTITUTION.md` | ALIGNED | Constitutional mandates |
| `THREE_LAYER_ARCHITECTURE.md` | ALIGNED | CMOS / PCAS / shells |
| `UNIFIED_VISION.md` | ALIGNED | **Canonical** end state + three products + one loop |
| `DEVELOPMENT_RULES.md` | ALIGNED | Extend roadmaps; no redesign |

---

## Tier 1 — Product & deploy truth

| `shared/README.md` | ALIGNED | Rewritten 2026-06-04 — Forge Profile center, Godot path; UME/USE deferred |
| `registry/README.md` | ALIGNED | Content packs only; not identity |

| File | Status | Action |
|------|--------|--------|
| `DEPLOYMENT_REALITY.md` | ALIGNED | Update matrix when field test done |
| `PHOENIX_FORGE_SYSTEM_ATLAS.md` | ALIGNED | Updated 2026-06-04 for `teacher-app` |
| `AUTHORITY_AND_REALITY_MAPPING.md` | ALIGNED | Updated 2026-06-04 |
| `roadmaps/*.md` | ALIGNED | Decompose under P0–P3 |

---

## Tier 2 — Contracts (DOCUMENTED, not IMPLEMENTED)

| File | Status | Code |
|------|--------|------|
| `contracts/INTENT_TILE_CONTRACT.md` | ALIGNED-CODE-LAG | Slim `IntentTile` in teacher-app |
| `contracts/MEMORY_EVENT_CONTRACT.md` | ALIGNED-CODE-LAG | Student `LifeEvent` local |
| `contracts/CURRICULUM_OS_SCHEMA.md` | ALIGNED-CODE-LAG | No curriculum-core |
| `contracts/CURRICULUM_RUNTIME_FLOW.md` | ALIGNED-CODE-LAG | Student orchestrator partial |
| `reference-tiles/secret-label-decoder.yaml` | ALIGNED-CODE-LAG | Not loaded |

**Do not** mark Milestone -1 [√] in `PCAS_IMPLEMENTATION_ROADMAP.md` as implemented until YAML loads in Teacher.

---

## Tier 3 — Teacher pedagogy (doc-complete)

| File | Status |
|------|--------|
| `teacher-edition/docs/curriculum-of-life.md` | ALIGNED — human canon |
| `teacher-edition/docs/EXPEDITION_BOARD_UX.md` | ALIGNED-CODE-LAG — board MVP exists |
| `teacher-edition/docs/teacher-edition-product-spec.md` | ALIGNED-CODE-LAG |
| All other `teacher-edition/docs/*.md` | ALIGNED — content authority |

**Runnable code:** `phoenix-forge-classroom-forge-profile/teacher-app` (not `teacher-edition/android/`).

---

## Tier 4 — Student UX (doc-complete)

| File | Status |
|------|--------|
| `student-edition/docs/EXPERIENCE_SHELL_SPEC.md` | ALIGNED-CODE-LAG |
| `student-edition/docs/HEARTHHOME_*`, `SPARK_*`, `IDENTITY_*` | ALIGNED-CODE-LAG |

**Runnable code:** `student-app` module.

**Boundary doc:** [STUDENT_TEACHER_BOUNDARY.md](STUDENT_TEACHER_BOUNDARY.md) (not `student-experience-boundary.md`).

---

## Removed from old report (never existed here)

Do not search for: `ARCHITECTURAL_BLUEPRINT.md`, `SPARK_MEMORY_KEEPER_SPEC.md`, `SPARK_LIFECYCLE_DESIGN.md`, `COMPANION_SPARK_DESIGN.md`, `WISP_SYSTEM_DESIGN.md`, `MEMORY_MUSEUM_DESIGN.md`, `HOUSE_AND_MUSEUM_DESIGN.md`, `MVP_AND_ROADMAP.md`, `FORGEPROFILE_EXTENSIONS.md`, `teacher-edition/docs/product-spec.md`, `student-edition/docs/experience-boundary.md`.

---

## Priority updates (documentation only when code moves)

1. When loop stage passes → update Atlas + Authority + `04_CROSS_APP` checklist  
2. When IntentTile model matches contract → update `INTENT_TILE_CONTRACT` appendix with Kotlin mapping  
3. When Hearthhome ships → update `EXPERIENCE_SHELL_SPEC` with screenshots/paths  

Do **not** rewrite constitution or unified vision unless Ezra-centered premise changes.
