# Phoenix Forge: Unified Vision

**Status:** North star for all roadmaps and implementation. If another doc contradicts this file, update that doc — not this one.

## Who this is for

**Ezra** is the center — not “the ecosystem,” not “the suite.” Every product exists to preserve and enrich one child’s real life from age 5 through 25.

## Three products (canonical names — do not rename)

| Product | Role |
|---------|------|
| **Forge Profile** | Lifelong identity record (CMOS direction) |
| **Phoenix Forge Classroom Student Edition** | Child experience shell (`:student-app`) |
| **Phoenix Forge Classroom Teacher Edition** | Parent expedition / curriculum command surface (`:teacher-app`) |

**Execution order:** [roadmaps/00_MASTER_ROADMAP.md](roadmaps/00_MASTER_ROADMAP.md) · **Dev rules:** [DEVELOPMENT_RULES.md](DEVELOPMENT_RULES.md)

## Near-term product goal (one loop)

```text
Teacher creates IntentTile
  → Student receives Quest
  → Student completes Quest
  → Forge Profile records MemoryEvent
  → Teacher sees resulting signal
```

Until that loop runs on device, treat CMOS-complete vision docs as **direction**, not **done**.

**Avatar:** Ezra’s look is owned in Forge Profile **Avatar Studio** and exported to Student (2D) and Godot (3D) — not duplicated per app. Spec: [FORGEPROFILE_SPEC.md](FORGEPROFILE_SPEC.md) · Godot: [GODOT_MIGRATION_STRATEGY.md](GODOT_MIGRATION_STRATEGY.md).

---

## The Prime Mandate: Childhood as the Product
Phoenix Forge is not educational software. It is a **Sovereign Childhood Archive** that preserves, enriches, and celebrates a child's life from age 5 through age 25. The most valuable asset in the entire ecosystem is the **Chronicle**—the living record of who the child is becoming.

## The Three-Layer Sovereign Architecture

### 1. The Database (CMOS)
The **Childhood Memory Operating System** is the permanent DNA of the childhood. It is built for 20-year durability, platform independence, and total data sovereignty. It stores the truth: voice notes, photos, family mythology, and identity threads.

### 2. The Interpreter (PCAS)
The **Persistent Childhood Adventure System** is the magic layer. it consumes CMOS data to generate stories, quests, and world states. It translates raw memories into a meaningful narrative world where the child can see their life reflected.

### 3. The Frontends (Experience Shells)
The replaceable windows into the world. Whether it is the current Android APK or a future Godot RPG, the shells are temporary. They emit **MemoryEvents** to CMOS and display the resulting world from PCAS.

## Core Philosophy: Human Primacy
At age 25, the sound of a parent's voice describing a 5-year-old discovery is more valuable than any AI-generated summary. We prioritize human memories (audio, photos, projects) over digital rewards. Spark and the Wisps exist as **Witnesses** and **Historians**, never as the primary source of truth.

## The Journey: Discovery to Legacy
1. **Discovery:** The child lives their life.
2. **Capture:** Moments enter the system via the **Discovery Journal** (Milestone 0).
3. **Preservation:** Events are promoted to the **Sovereign Archive** (CMOS).
4. **Reflection:** Spark and the World help the child reflect on their growth.
5. **Legacy:** At age 18, the child receives their **Time Capsule**—a complete, readable history of their childhood.
