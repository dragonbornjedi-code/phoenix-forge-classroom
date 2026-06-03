# Narrative Wrapper & Quest Engine

The Narrative Wrapper Engine is the "Storyteller" of the PCAS layer. It ensures that the parent's accepted plan is translated into a cohesive childhood adventure.

## 1. The Wrapper Workflow: Plan to Story
The story should never dictate the developmental plan; it should celebrate the intent.

1. **Plan Acceptance:** The parent finalizes and accepts the **Daily Expedition Board** tiles in the Teacher Edition.
2. **Intent Scanning:** The engine scans the `Story Integration` hooks from each tile (e.g., [Reading: forgotten message] + [Walk: search location] + [Math: restoring power]).
3. **Synthesis:** The AI (local or teacher-assisted) synthesizes a unifying narrative arc.
   - *Example Theme:* "The Lost Signal Towers."
4. **Injection:** The story is injected into **Spark's** dialogue and the **Student Shell** as the day's "Mission."

## 2. Environmental Injection (Runtime)
The engine reads local sensor data (weather, time, location) to skin the world and adjust the wrapper.
- **Scenario:** It is raining in the real world.
- **Quest Hook:** Ripple Wisp says: "I hear a strange song on the cottage roof! Can you find where the water drops go?"

## 3. Artifact-Based Missions
New quests are generated when a child adds an item to their **Living Chronicle**.
- **Scenario:** Child photos a "Red Leaf."
- **Engine Logic:** Create `Episode: The Day of the Crimson Leaf`.
- **Quest Hook:** Moss Wisp wants to find "his other red friends" (Color matching engine).

## 4. Developmental Scaffolding (Hidden)
The quest complexity is automatically tuned by the `mastery` metrics in the **ForgeProfile**.
- If `mastery.literacy.interest` is high, the quest instructions become more narrative.
- If `mastery.fineMotor.persistence` is low, missions are kept shorter with more frequent "Spark Support" interruptions.
