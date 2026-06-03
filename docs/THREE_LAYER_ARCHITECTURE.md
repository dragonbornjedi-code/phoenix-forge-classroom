# Phoenix Forge Architecture: The Three Layers

Phoenix Forge is structured as a three-layer stack to ensure that a child's life history remains a sovereign, durable, and accessible asset for 20+ years, independent of the technology used to view it.

## Layer 1: Childhood Memory OS (CMOS)
**The Core Asset.** CMOS is the "Childhood Database." It is the only layer that must survive until age 25. It is designed for absolute durability and platform independence.
- **Ownership:** ForgeProfile, Chronicle, Family Mythology, Artifacts, Relationship Graph, Identity Threads.
- **Form:** Open-standard JSON, ZIP bundles (.pfc), and SQLite.
- **Principle:** If all software dies, the data remains human-readable and accessible.

## Layer 2: Persistent Childhood Adventure System (PCAS)
**The Runtime Interpreter.** PCAS is the logic engine that consumes CMOS data to generate dynamic experiences. It is modular and replaceable.
- **Responsibility:** Quest generation, World state logic, Spark's maturation rules, Museum layout logic, Semantic theme discovery.
- **Principle:** PCAS translates the "Static Archive" (CMOS) into a "Living World."

## Layer 3: Experience Shells
**The Frontends.** Experience shells are the windows through which the child interacts with their world. They are completely interchangeable.
- **Examples:**
  - **Android APK:** The entry-point for early childhood.
  - **Godot RPG:** The 3D world for middle childhood.
  - **XR Museum:** An immersive gallery for adolescence.
  - **Age 25 Browser:** A read-only legacy viewer for the adult.
- **Principle:** The experience changes; the childhood remains the same.

---

# Phoenix Forge Principle #1: The 2046 Test

> **"A childhood memory must remain accessible without Phoenix Forge existing."**

This is the foundational constraint of the entire architecture. 
1. **No Proprietary Lock-in:** No vendor-specific databases or encrypted formats that require a central server to unlock.
2. **No Cloud Dependency:** 100% offline-first. The archive is stored locally and owned by the family.
3. **No AI Dependency for Storage:** While AI may be used to *enrich* or *query* memories (PCAS), the raw memory (CMOS) must be stored in a flat, human-readable format.
4. **No DRM:** The child's history is not a product; it is a right.
