# Teacher Edition: Daily Expedition Board UX

**Implementation (2026-06-04):** `phoenix-forge-classroom-forge-profile/teacher-app` — board, create tile, field guide, save, mark complete, Room persist. **Not yet:** drag reorder (P1c), 7-day/month views. **Build:** `:teacher-app:assembleDebug` — not `teacher-edition/android/`.

The **Daily Expedition Board** is the primary interface for the Teacher Edition. It is designed as a living, breathing field guide for parents navigating a mobile or unpredictable childhood.

## 1. Core Visual Layout

### The Header: Today's Anchors
The "Minimum Wins" for the day. These are non-negotiable anchors that provide structure without a schedule.
- **Example:** `[✓] Reading` | `[ ] Math` | `[ ] Outdoors` | `[ ] Emotion Check`
- **Logic:** Toggling an anchor doesn't necessarily mark an activity complete; it marks a *purpose* fulfilled.

### The Stack: The Priority Stack
A vertical or grid-based collection of **Expedition Cards**.
- **State:** Unfinished cards are draggable.
- **Behavior:** The parent reorders them based on the current energy, weather, or location.

---

## 2. The Intent Tile (Expedition Card)

### Face View (Minimized)
- **Visual Identity:** Color-coded by Wisp domain (Green/Nature, Red/Creation, Blue/Connection, Yellow/Understanding).
- **Core Intent:** A clear, concise title (e.g., "Observation Walk" or "Number Forge").
- **Possibility Tags:** Mobile-specific tags like "Car-Friendly," "Indoors," "High Energy," "Quiet."

### The Living Lesson Plan (Expanded Popup)
Tapping a tile expands it into a comprehensive "Field Guide." This is where the lesson plan is elevated into a rich navigation tool.
- **The "Why":** The developmental objective and long-term rationale.
- **Procedures:** 3-5 simple, actionable steps for the mentor.
- **Alternative Paths:** "If we're stuck in traffic, do this instead..."
- **Sensory Guidance:** Lighting, noise, and tactile accommodations.
- **Linked Insights:** "Last time we did this, Ezra enjoyed using the magnifying glass."
- **Story Integration:** The core narrative hook for the **Narrative Wrapper Engine**.

---

## 3. The Completion Loop (Double Tap)

Double-tapping a card triggers the **Mentorship Selection**.

### Level 1: Complete Only
- **Speed:** < 1 second.
- **Result:** Card locks, timestamps, and moves to the "Completed" section.

### Level 2: Quick Note
- **Action:** Opens a single text field.
- **Usage:** "Ezra found this really easy" or "Interrupted by rain."
- **Result:** Note is attached to the `MemoryEvent`.

### Level 3: Deep Reflection
A multi-dimensional check-in across five axes:
1. **Mental:** Focused vs. Distracted.
2. **Emotional:** Happy vs. Frustrated.
3. **Physical:** Energetic vs. Tired.
4. **Educational:** Easy vs. Challenging.
5. **Behavioral:** Independent vs. Supported.

---

## 4. The Historical Lock

As cards are completed, they form the **Chronicle of the Day**.
- **Locked Order:** Once a card is marked complete, its position in the list is locked relative to other completed items.
- **Actual Record:** If the parent did "Nature Walk" then "Snack" then "Reading," that becomes the permanent record of the day, regardless of the morning's plan.
- **PCAS Integration:** The locked list is used by the **Living Chronicle** to generate the "Episode" narrative.

---

## 5. Mobile/Rugged Optimization
- **Offline First:** All knowledge records and card data are local.
- **Environment Aware:** Weather and time of day are prominently displayed and used to suggest reordering (e.g., "It's raining; maybe move 'Nature Walk' to later?").
- **One-Handed Operation:** Large tap targets and swipe gestures for use while on a trail or holding a child.
