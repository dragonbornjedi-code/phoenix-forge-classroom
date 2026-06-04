# Reference Intent Tiles

Canonical integration-test tiles for the Curriculum OS. Each tile proves the full runtime in [CURRICULUM_RUNTIME_FLOW.md](../CURRICULUM_RUNTIME_FLOW.md).

**Do not bulk-author lessons until the reference tile passes all proof checks.**

## Tiles

| Tile | File | Lesson pattern | Status |
|------|------|----------------|--------|
| Secret Label Decoder | [secret-label-decoder.yaml](secret-label-decoder.yaml) | Label Hunt | Reference implementation |

## Proof checklist (Secret Label Decoder)

Run in order. Fix architecture before adding more tiles if any step fails.

| # | Check | Pass criteria |
|---|--------|----------------|
| 1 | Tile loads from contract | Parser/validator accepts YAML; all required Intent Tile + atom fields present |
| 2 | Teacher can execute lesson | `field_guide` steps runnable from starter-lesson parity |
| 3 | Evidence can be captured | All **mandatory** `evidence_template.required_fields` populated (not `completed: true` alone) |
| 4 | Capability growth updates | At least one `AtomOutcome` advances difficulty or prompt level for a `capability_id` |
| 5 | Compass reflects capability evidence | `CompassSnapshot.capabilityGrowth` changes for touched capabilities |
| 6 | MemoryEvent can be generated | [TILE_EVENT_MAPPING.md](../TILE_EVENT_MAPPING.md) produces valid draft from tile + session |
| 7 | StoryGraph accepts memory hook | `narrative_seed_phrase` → `StoryFragment.continuityThread`; anchor → emotional weight |
| 8 | Student simulation reacts | `WorldOrchestrator` dispatch with mapped `WorldEventType` + drift bias |
| 9 | Chronicle entry can be produced | Steward promotion bundles full tile + `LessonSessionEvidence` |
| 10 | Weekly audit can reference evidence | Audit fields (win, friction, metric) mappable from session record |

Record pass/fail in your validation harness or a manual run log under `reference-tiles/validation-log.md` when executed.

## Capability ID aliases

Human shorthand → schema `CapabilityId`:

| Shorthand | Schema ID |
|-----------|-----------|
| `SYMBOL_OBJECT_MAPPING` | `SYMBOL_TO_OBJECT_MAPPING` |
| `OBJECT_FUNCTION_REASONING` | `OBJECT_FUNCTION_MAPPING` |
| `CATEGORY_CLASSIFICATION` | `CATEGORY_GROUPING` |
| `RETRIEVAL_RECALL` | `VERBAL_RETRIEVAL` |

Reference tiles use **schema IDs only**.
