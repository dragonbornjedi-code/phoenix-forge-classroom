# Tile -> MemoryEvent Mapping

This document defines the formal mapping between the completion of an **Intent Tile** on the Teacher Edition's Expedition Board and the resulting **MemoryEvent** ingested into CMOS.

## 1. Mapping Logic

### Trigger: Level 1 (Complete Only)
- **MemoryEvent Type:** `MILESTONE`
- **Narrative Title:** Defaults to `IntentTile.title`.
- **Narrative Summary:** Defaults to `IntentTile.description`.
- **Significance:** `memory_event_template.default_significance`.
- **Wisp Affinities:** `IntentTile.domain` @ 1.0 intensity.

### Trigger: Level 2 (Quick Note)
- **MemoryEvent Type:** `REFLECTION`
- **Narrative Title:** `IntentTile.title`.
- **Narrative Summary:** The content of the Parent's note.
- **Significance:** `default_significance + 0.1` (Boosted for human context).
- **Wisp Affinities:** `IntentTile.domain`.

### Trigger: Level 3 (Deep Reflection)
- **MemoryEvent Type:** `REFLECTION`
- **Narrative Title:** `IntentTile.title`.
- **Narrative Summary:** Synthesized reflection from the five axes (Mental, Emotional, Physical, Educational, Behavioral).
- **Significance:** `default_significance + 0.3` (Highly significant human context).
- **Identity Threads:** System checks if `linked_identity_threads` should be reinforced based on the axes.

## 2. Field Mapping Registry

| Tile Field | MemoryEvent Field | Transformation |
| :--- | :--- | :--- |
| `id` | `sourceTileId` | Direct map. |
| `title` | `narrative.title` | Direct map. |
| `domain` | `impact.wispAffinities` | Map domain to 100% affinity. |
| `linked_identity_threads` | `impact.threadRef` | Reference first matching thread. |
| `story_hooks` | `narrative.context` | Injected as keywords for Spark's memory. |

## 3. Atomic Unit Preservation
When a tile is promoted to a MemoryEvent:
1. The **Full Intent Tile Object** is bundled as a hidden metadata artifact within the event.
2. This ensures that in 2046, the adult can see exactly *why* that activity was chosen (the Objective and "Why It Matters" fields).
