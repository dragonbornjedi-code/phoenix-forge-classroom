# Phoenix Forge Repository Constitution

## Vision
Phoenix Forge exists to preserve, enrich, and celebrate a child's real life. It is not educational software; it is a **Persistent Childhood Adventure System (PCAS)** built upon a **Childhood Memory Operating System (CMOS)**.

**North star (product + loop):** [UNIFIED_VISION.md](UNIFIED_VISION.md) · **Execution:** [roadmaps/00_MASTER_ROADMAP.md](roadmaps/00_MASTER_ROADMAP.md) · **Dev rules:** [DEVELOPMENT_RULES.md](DEVELOPMENT_RULES.md)

## The Core Mandates

### Article I: Sovereign Memory
A childhood memory must remain accessible without Phoenix Forge existing. All core archives (CMOS) must be stored in open, human-readable formats (JSON, ZIP, SQLite).

### Article II: Human Primacy
Human-created memories (audio, photos, parent notes) outrank AI-created memories (summaries, quest text). Spark and the Wisps are readers and narrators of the childhood story, never its primary source.

### Article III: Childhood Ownership
The child ultimately owns the Chronicle. The Parent/Mentor acts as the Steward, Curator, and Guardian until the final Handover Protocol is executed.

### Article IV: Privacy by Design
The system is 100% offline-first. No cloud dependency, no vendor lock-in, and no third-party data tracking. Sovereignty requires local data integrity.

---

## Architectural Hierarchy

### Layer 1: CMOS (The Database)
The "Childhood DNA." This is the only layer that must survive until Age 25.
- **Components:** ForgeProfile, Chronicle, Artifact Registry, Identity Threads, Family Mythology, Human Voice Archive.
- **Constraint:** Immutable history. No shell may directly delete a validated CMOS record.

### Layer 2: PCAS (The Interpreter)
The runtime interpretation layer. Consumes CMOS to generate world state.
- **Components:** Spark, Wisp System, Quest Engine, Theme Discovery, Significance Engine, Museum Logic.
- **Responsibility:** Translating "Static Data" into "Meaningful Adventure."

### Layer 3: Experience Shells (The Frontends)
Replaceable windows into the world.
- **Student Edition:** Daily childhood interaction. Emits MemoryEvents.
- **Teacher Edition:** Stewardship, curation, and approval. Annotates CMOS records.
- **Godot World:** High-fidelity 3D interpretation of the archive.
- **Legacy Viewer:** Read-only adult access to the PFC bundle.

---

## Technical Standards
1. **Memory First:** Every feature must contribute to or reflect the Chronicle.
2. **2046 Test:** If you can't read it with a basic text editor and ZIP utility in 20 years, don't build it that way.
3. **Event-Driven:** Ingestion into CMOS happens exclusively via the **Memory Event Contract**.
