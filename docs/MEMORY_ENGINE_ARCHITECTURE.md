# Memory Engine Architecture

The Memory Engine is the heart of the Phoenix Forge Childhood Memory OS. It is a multi-layered system designed to capture the "Episodic," "Semantic," and "Relational" data of a child's life.

## 1. Episodic Memory (The Narrative Pulse)
Episodic memory captures specific, time-bound events. Unlike a log, it stores **Context and Emotion**.

### Memory Object Schema
- **EventID:** UUID.
- **NarrativeTitle:** A child-friendly title (e.g., "The Day of the Giant Snowman").
- **Timestamp:** ISO 8601.
- **ChildAge:** In months/years at time of event.
- **Season:** Spring/Summer/Autumn/Winter (contextual trigger).
- **Location:** LandmarkID (Family Mythology) or GPS coordinate.
- **EmotionalTags:** A map of feelings during the event (e.g., `{"JOY": 0.9, "WONDER": 0.8}`).
- **Companions:** List of EntityIDs (Spark, Wisps, Family Members).
- **ArtifactRefs:** List of IDs for photos, audio, or built objects.
- **NarrativeArc:** A short summary of "What happened."

## 2. Semantic Memory (The Pattern Finder)
Semantic memory discovers the "Theme" of a childhood. It uses the collection of Episodic memories to identify recurring interests and traits.

### Theme Discovery Engine
- **Interest Graphing:** Identifies clusters of activities (e.g., building cardboard boxes + LEGO + making mud pies -> Theme: "The Builder").
- **Vocabulary Growth:** Tracks the evolution of language used in Victory Journals and voice notes.
- **World Impact:** Adjusts the visual vibrancy of specific "Realms" based on these themes (e.g., the Inventor Lab grows more complex for "The Builder").

## 3. Relational Memory (The Bond Ledger)
Tracks the status and history of relationships with every entity, real or digital.

### Entity Memory Graph
- **People:** "Mom," "Dad," "Grandpa." Stores recurring interactions and roles.
- **Animals:** "Rex" (Family Dog). Stores memories of play and care.
- **Digital Spirits:** Spark and the Wisps. Stores the evolution of their shared history.
- **Sacred Places:** "The Old Oak," "The Creek." Tracks visits and discoveries at specific locations.

## 4. Continuity Pipeline
The engine ensures that data captured in the Stage 1 Android APK is structured such that it can be "Replayed" or "Narrated" by Spark in the Stage 3 Godot RPG or a future VR/XR experience at age 18.
- **Universal Event Dispatcher:** Every "Mission" or "Nature Discovery" emits a standard Memory Event.
- **Semantic Tagging:** Automatic tagging based on curriculum domain (Moss/Ember/Ripple/Volt) and parental approval notes.
