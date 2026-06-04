# Intent Tile Contract

The **Intent Tile** is the atomic unit of the Phoenix Forge Teacher Edition. It represents a discrete developmental "Intent" that can be realized through various real-world activities. It elevates the traditional "Lesson Plan" into a metadata-rich, flexible object.

Operational types are defined in [CURRICULUM_OS_SCHEMA.md](CURRICULUM_OS_SCHEMA.md). Atomization procedure: [CURRICULUM_ATOMIZATION_GUIDE.md](../../phoenix-forge-classroom-teacher-edition/docs/CURRICULUM_ATOMIZATION_GUIDE.md).

## 1. Data Structure

```yaml
IntentTile:
  id: "uuid-v4"
  schema_version: "1.0"
  title: "A concise, child-facing or mentor-facing title"
  description: "Short description of the activity"

  # Taxonomy Alignment (human curriculum)
  category: "Primary category (e.g., Cognitive Foundations)"
  subcategory: "Specific subcategory (e.g., Numeracy)"
  domain: "Wisp Domain (MOSS | EMBER | RIPPLE | VOLT)"
  lesson_pattern_id: "label_hunt"              # key in curriculum-of-life Lesson patterns

  # Curriculum OS (operational — Compass + simulation)
  skill_atom_ids:
    - "label_hunt.match_with_model"
    - "label_hunt.explain_job"
  capability_ids:                              # denormalized for Compass queries
    - "SYMBOL_TO_OBJECT_MAPPING"
    - "OBJECT_FUNCTION_MAPPING"
  default_difficulty: "L1_GUIDED_IMITATION"    # L1 | L2 | L3 | L4

  failure_signatures:                          # subset for this tile
    - type: "CONFUSION"
      observables: ["guesses without looking"]
      reset_intervention: "What do your eyes notice?"
    - type: "SKILL_GAP"
      observables: ["cannot transfer to new object"]
      reset_intervention: "Same pattern, new object"

  lesson_signals:                              # template: what to log after session
    engagement: "Stayed with matching through 3 labels"
    frustration: "Refusal, throwing materials"
    curiosity: "Unprompted job explanation"
    independence: "Prompt level reached independent"
    recovery: "Returned after Recovery Hub"

  memory_hooks:
    emotional_anchor: "First independent label match"
    narrative_seed_phrase: "The house learned new words on the walls"
    npc_reaction_trigger: "Spark emphasizes secret codes on objects"
    world_drift_bias: "GALLERY_VITALITY_UP"    # optional
    repeat_exposure_suggestion: "3 labels, 3 minutes next low-energy window"

  # Planning Metadata
  anchor_tags: ["Reading", "Math", "Outdoors", "Movement"]
  energy_level: "LOW | MEDIUM | HIGH"
  environment_tags: ["Indoor", "Outdoor", "Car-Friendly", "Quiet", "Messy"]
  estimated_duration: "5-15m"

  # The Living Lesson Plan (Knowledge Record — prose from curriculum/starter lessons)
  field_guide:
    objective: "The 'Why'—long-term developmental purpose."
    why_it_matters: "Parent-focused rationale."
    procedures:
      - step: "Step 1 description"
      - step: "Step 2 description"
    alternative_paths:
      - "Indoors version: ..."
      - "Low-energy version: ..."
    sensory_accommodations:
      - "Tactile: ..."
      - "Audio: ..."
    materials:
      - "Item 1"
      - "Item 2"
    nutrition_links: ["Protein-boost suggested before this", "Hydration focus"]

  # PCAS Integration (Expedition narrative — day-of story)
  story_hooks:
    nouns: ["Signal Tower", "Lost Message"]
    verbs: ["Recover", "Decode"]
    spark_guidance: "Spark should emphasize 'Finding the Hidden Patterns'."

  # CMOS Ingestion
  completion_options:
    - "COMPLETE_ONLY"
    - "QUICK_NOTE"
    - "DEEP_REFLECTION"
  memory_event_template:
    eventType: "BUILD | DISCOVERY | CONVERSATION"
    default_significance: 0.5

  # Session evidence (post-completion, links to OS schema)
  evidence_template:
    evidence_types: ["OBSERVATION", "PHOTO", "PROMPT_LEVEL_LOG"]
    atom_outcome_slots: ["label_hunt.match_with_model", "label_hunt.explain_job"]

  # Continuity
  linked_identity_threads: ["thread_builder", "thread_pattern_finder"]
  linked_themes:
    month: "Exploration"
    week: "Signals"
```

### Field semantics

| Field | Purpose |
|-------|---------|
| `lesson_pattern_id` | Stable link to [curriculum-of-life.md](../../phoenix-forge-classroom-teacher-edition/docs/curriculum-of-life.md) pattern name |
| `skill_atom_ids` | Smallest measurable units for this tile instance |
| `capability_ids` | Longitudinal Compass axis (derived from atoms, stored for query speed) |
| `lesson_signals` | Behavioral signal layer → Childhood Compass + Student spine |
| `memory_hooks` | Persistent narrative structure → Narrative Wrapper + `StoryFragment` continuity |
| `failure_signatures` | Structured failure → regulation-first reset (not punishment) |
| `story_hooks` | Same-day Expedition narrative (intent celebration) |
| `evidence_template` | What to capture when tile completes |

## 2. States
- **DRAFT:** Being edited or generated.
- **ACTIVE:** On the Daily Expedition Board (Draggable).
- **COMPLETED:** Locked in historical order (Immutable).
- **DEFERRED:** Pushed to a future stack.

## 3. The Contract Lifecycle
1. **Definition:** Tiles are defined in the **Curriculum Library** (built from the Taxonomy + atomization guide).
2. **Generation:** The **Lesson Generation Rules** pick tiles based on themes, **Childhood Compass** capability gaps, and domain coupling rules.
3. **Execution:** The parent interacts with the tile on the **Daily Expedition Board**.
4. **Observation:** Parent logs `lesson_signals` and `evidence_template` outcomes → `LessonSessionEvidence`.
5. **Emission:** Completion triggers a `MemoryEvent` mapped via `memory_event_template` and [TILE_EVENT_MAPPING.md](TILE_EVENT_MAPPING.md).
6. **Compass update:** Capabilities and `CompassSnapshot` aggregates refresh from session evidence (not lesson checklist).

## 4. Mapping to MemoryEvent (summary)

| Tile field | MemoryEvent / downstream |
|------------|--------------------------|
| `memory_hooks.narrative_seed_phrase` | Chronicle keywords; Student `continuityThread` |
| `memory_hooks.emotional_anchor` | Significance boost; `emotionalImpact` |
| `lesson_signals` (session instance) | Compass trends; optional Student `BehaviorSignals` |
| `capability_ids` | Identity thread reinforcement candidates |
| `skill_atom_ids` + `evidence_template` | Bundled metadata artifact (2046 replay) |
| `story_hooks` | Narrative Wrapper day story |
| Full tile object | Hidden metadata on MemoryEvent per TILE_EVENT_MAPPING |

## 5. Reference implementation

- [reference-tiles/secret-label-decoder.yaml](reference-tiles/secret-label-decoder.yaml) — full tile + atoms + mandatory evidence + proof examples
- [reference-tiles/README.md](reference-tiles/README.md) — 10-step integration checklist

## 6. Related contracts

- [CURRICULUM_OS_SCHEMA.md](CURRICULUM_OS_SCHEMA.md)
- [MEMORY_EVENT_CONTRACT.md](MEMORY_EVENT_CONTRACT.md)
- [TILE_EVENT_MAPPING.md](TILE_EVENT_MAPPING.md)
