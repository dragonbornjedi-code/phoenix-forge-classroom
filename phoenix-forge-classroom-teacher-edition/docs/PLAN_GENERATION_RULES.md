# Lesson Plan Generation Rules

This document defines the rules and heuristics for generating the hierarchical lesson structure in the Teacher Edition. The goal is to provide consistent, balanced, and theme-appropriate guidance.

## 1. The Hierarchy
- **Month Theme:** High-level narrative and focus area (e.g., "Exploration & Discovery").
- **Week Theme:** Specific sub-focus (e.g., "Patterns in Nature").
- **Day Plan:** A collection of **Intent Tiles** selected for a specific day of the week.

## 2. Selection Heuristics

### Rule I: The Theme Anchor
The system selects at least two **Intent Tiles** per day that explicitly match the `Week Theme`.
- *Example:* If the theme is "Patterns," tiles like "Leaf Printing" (Art/Nature) and "Bead Sequencing" (Math) are prioritized.

### Rule II: The Childhood Compass Bias
The system scans the **Childhood Compass** for areas that "Need More Sunlight."
- **Logic:** If "Music" has been quiet for 14 days, the system MUST include one Music-related tile in the next available day plan.

### Rule III: Energy and Environment Balance
Each Day Plan should contain a mix of energy levels and environments to ensure mobile/travel resilience.
- **Minimum 1 Car-Friendly Tile:** For travel windows.
- **Minimum 1 High-Energy Tile:** For outdoor/physical mastery.
- **Minimum 1 Quiet Tile:** For transitions or rest periods.

## 3. The Generation Loop

### Step 1: Context Intake
The system reads the current **ForgeProfile Identity Threads** and **Living Chronicle** history.

### Step 2: Goal Synthesis
The system defines the "Next Best Wins" based on the **Childhood Compass** gaps.

### Step 3: Tile Drafting
The system pulls **Intent Tiles** from the **Curriculum Library** that satisfy:
- The current Theme.
- The Compass requirements.
- The Energy/Environment mix.

### Step 4: Parent Review (The "Steward Gate")
The parent reorders, swaps, or edits tiles on the **Daily Expedition Board**. The parent has final authority over the "Intent of the Day."

### Step 5: Narrative Wrapping
Once the tiles are "Accepted," the **Narrative Wrapper Engine** generates the day's story based on the finalized tile list.

## 4. Resilience Rules
- **No Penalty for Skips:** If a tile is skipped or deferred, it is simply returned to the library. The system recalibrates the following day's suggestions.
- **Manual Overrides:** The parent can manually add a "Wildcard" tile at any time, which the **Childhood Compass** then incorporates into its future analysis.
