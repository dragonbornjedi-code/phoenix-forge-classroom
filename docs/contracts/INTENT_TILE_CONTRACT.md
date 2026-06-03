# Intent Tile Contract

The **Intent Tile** is the atomic unit of the Phoenix Forge Teacher Edition. It represents a discrete developmental "Intent" that can be realized through various real-world activities. It elevates the traditional "Lesson Plan" into a metadata-rich, flexible object.

## 1. Data Structure

```yaml
IntentTile:
  id: "uuid-v4"
  title: "A concise, child-facing or mentor-facing title"
  description: "Short description of the activity"

  # Taxonomy Alignment
  category: "Primary category (e.g., Cognitive Foundations)"
  subcategory: "Specific subcategory (e.g., Numeracy)"
  domain: "Wisp Domain (MOSS | EMBER | RIPPLE | VOLT)"
  
  # Planning Metadata
  anchor_tags: ["Reading", "Math", "Outdoors", "Movement"] # For "Minimum Win" tracking
  energy_level: "LOW | MEDIUM | HIGH"
  environment_tags: ["Indoor", "Outdoor", "Car-Friendly", "Quiet", "Messy"]
  estimated_duration: "5-15m"

  # The Living Lesson Plan (Knowledge Record)
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

  # PCAS Integration
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

  # Continuity
  linked_identity_threads: ["thread_builder", "thread_pattern_finder"]
  linked_themes:
    month: "Exploration"
    week: "Signals"
```

## 2. States
- **DRAFT:** Being edited or generated.
- **ACTIVE:** On the Daily Expedition Board (Draggable).
- **COMPLETED:** Locked in historical order (Immutable).
- **DEFERRED:** Pushed to a future stack.

## 3. The Contract Lifecycle
1. **Definition:** Tiles are defined in the **Curriculum Library** (built from the Taxonomy).
2. **Generation:** The **Lesson Generation Rules** pick tiles based on themes and the **Childhood Compass**.
3. **Execution:** The parent interacts with the tile on the **Daily Expedition Board**.
4. **Emission:** Completion triggers a `MemoryEvent` mapped via the `memory_event_template`.
