# Documentation Alignment Report

This report audits the existing documentation against the **Phoenix Forge Repository Constitution**. It identifies documents that require rewriting, renaming, merging, or deprecation to eliminate architectural drift.

## Status Summary

| File | Status | Action Required | Constitution Compliance |
| :--- | :--- | :--- | :--- |
| `docs/REPOSITORY_CONSTITUTION.md` | VALID | None | 100% |
| `docs/THREE_LAYER_ARCHITECTURE.md` | VALID | None | 100% |
| `docs/UNIFIED_VISION.md` | OUTDATED | Rewrite to align with CMOS/PCAS split. | 60% |
| `docs/ARCHITECTURAL_BLUEPRINT.md` | REDUNDANT | Rename to `docs/SHELL_OVERVIEW.md` and refactor for Layer 3. | 50% |
| `docs/MEMORY_ENGINE_ARCHITECTURE.md` | VALID | None | 100% |
| `docs/IDENTITY_FORMATION_ENGINE.md` | VALID | None | 100% |
| `docs/HUMAN_MEMORY_PRESERVATION.md` | VALID | None | 100% |
| `docs/FAMILY_LEGACY_SYSTEM.md` | VALID | None | 100% |
| `docs/MEMORY_PERSISTENCE_STRATEGY.md` | VALID | None | 100% |
| `docs/CHRONICLE_QUERY_ENGINE.md` | VALID | None | 100% |
| `docs/SPARK_MEMORY_KEEPER_SPEC.md` | VALID | Merge with `SPARK_LIFECYCLE_DESIGN.md`. | 90% |
| `docs/SPARK_LIFECYCLE_DESIGN.md` | VALID | Merge with `SPARK_MEMORY_KEEPER_SPEC.md`. | 100% |
| `docs/COMPANION_SPARK_DESIGN.md` | OUTDATED | Deprecate in favor of `SPARK_LIFECYCLE_DESIGN.md`. | 40% |
| `docs/WISP_SYSTEM_DESIGN.md` | VALID | Update terminology to "Identity Lenses." | 90% |
| `docs/QUEST_ENGINE_DESIGN.md` | VALID | None | 100% |
| `docs/MEMORY_MUSEUM_DESIGN.md` | VALID | None | 100% |
| `docs/HOUSE_AND_MUSEUM_DESIGN.md` | REDUNDANT | Merge into `MEMORY_MUSEUM_DESIGN.md`. | 70% |
| `docs/EVENT_CAPTURE_PROTOCOL.md` | VALID | None | 100% |
| `docs/SIGNIFICANCE_ENGINE.md` | VALID | None | 100% |
| `docs/LEGACY_OBJECT_SYSTEM.md` | VALID | None | 100% |
| `docs/PCAS_IMPLEMENTATION_ROADMAP.md` | VALID | None | 100% |
| `docs/MVP_AND_ROADMAP.md` | DEPRECATED | Delete or Archive. | 20% |
| `docs/FORGEPROFILE_SPEC.md` | VALID | Ensure inclusion of Identity Threads. | 90% |
| `docs/FORGEPROFILE_EXTENSIONS.md` | VALID | Merge into `FORGEPROFILE_SPEC.md`. | 100% |
| `docs/GODOT_MIGRATION_STRATEGY.md` | VALID | None | 100% |
| `docs/STUDENT_TEACHER_BOUNDARY.md` | VALID | None | 100% |
| `docs/contracts/MEMORY_EVENT_CONTRACT.md` | VALID | None | 100% |
| `teacher-edition/docs/product-spec.md` | OUTDATED | Rewrite as "Archive Steward Interface." | 30% |
| `student-edition/docs/experience-boundary.md` | OUTDATED | Rewrite as "Experience Shell Spec." | 40% |

## Priority Rewrites
1. **UNIFIED_VISION.md:** Must focus on "Childhood as the Product."
2. **Teacher Edition Spec:** Reposition parent as "Steward/Historian."
3. **Student Edition Spec:** Reposition app as "Experience Shell."
4. **Wisp System:** Shift from "Teachers" to "Identity Lenses."
