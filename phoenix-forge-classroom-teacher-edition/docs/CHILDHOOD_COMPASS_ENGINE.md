# Childhood Development Navigation System

The Childhood Development Navigation System (CDNS) is the "Inner Engine" of the Teacher Edition. Its mission is to ensure a balanced, rich childhood by illuminating developmental paths and providing a multi-dimensional map for the mentor.

## 1. The Childhood Compass
The system tracks engagement across seven core domains (defined in the Curriculum Taxonomy) over months and years.

### Navigation Signals
- **Function:** Analyzes the **Living Chronicle** and **Accepted Plans** to identify areas that "Need More Sunlight."
- **Feedback Loop:** 
  - `Language Development: covered frequently`
  - `Fine Motor Skills: • needs more sunlight`
  - `Music: • quiet this month`
- **Purpose:** Not to create pressure, but to provide "Awareness at a Glance" for the parent during the planning phase.

## 2. The Living Lesson Plan (Intent Tiles)
Instead of static text, activities are metadata-rich **Intent Tiles** (see **[INTENT_TILE_CONTRACT.md](contracts/INTENT_TILE_CONTRACT.md)**).

### Tile Metadata Schema
- **The "Why":** Deep developmental rationale (linked to Wisp domains).
- **Primary Approach:** The default "Ghibli-style" delivery.
- **Alternative Approaches:** "If Ezra is tired, try this..." or "If indoors, try this..."
- **Accommodations:** Sensory supports, physical modifications, and attention anchors.
- **Linked Observations:** Historical data on what worked last time this specific intent was pursued.
- **Story Integration Hooks:** Key nouns and verbs for the **Narrative Wrapper Engine**.

## 3. Narrative Wrapper Engine (Post-Plan Generation)
The story should never dictate the plan; it should celebrate the intent.

### Workflow:
1. **Selection:** Parent picks intent tiles (Reading, Math, Outdoors).
2. **Reorder:** Parent arranges intent (Intent > Time).
3. **Acceptance:** Parent clicks "Accept Expedition."
4. **Generation:** The engine scans the tiles and generates a cohesive **Narrative Wrapper**.
   - *Example:* "Today the Rangers must find the lost signal towers. Reading reveals the message; the Walk uncovers the location."

## 4. Multi-Year Navigation
The CDNS uses the **ForgeProfile Identity Threads** to adjust navigation suggestions.
- If the "Builder" thread is high confidence, the system suggests adding an "Engineering Challenge" to the next Nature Walk.
- If a thread is "Quiet" (e.g., Artist), the system gently surfaces "Creative Expression" tiles to ensure exposure.

## 5. Continuity & Resilience
The system is designed to handle life's chaos. If a week is "Lost" to travel or illness, the CDNS doesn't reset. It simply observes the gap and recalibrates the "Next Best Wins" for the following week.
