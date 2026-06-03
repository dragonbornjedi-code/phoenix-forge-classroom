# PCAS Implementation Roadmap: The Sovereign Archive

The roadmap is ordered to ensure structural integrity and 20-year durability before building runtime UI.

## Milestone -1: The Intent & Compass Contracts (Current Focus)
**Goal:** Define the atomic units and navigation heuristics.
- [√] **Intent Tile Contract:** YAML/JSON specification for metadata-rich tiles.
- [√] **Childhood Compass Spec:** Navigation signals and "Sunlight" heuristics.
- [√] **Lesson Generation Rules:** Logic for Month -> Week -> Day synthesis.
- [√] **MemoryEvent Mapping:** Formal mapping of Tile Completion -> `MemoryEvent`.

## Milestone 0: The Capture Foundation
**Goal:** Ensure the child and parent can record reality today.
- [ ] **Discovery Journal Tool:** Photo and High-Quality Audio capture interface in Student Edition.
- [ ] **Human Memory Preservation:** Audio recording loops for "Grandparent Stories" and "Parental Echoes."
- [ ] **Blob Store Scaffolding:** Local Android media storage structure (`/media/artifacts/`).
- [ ] **MemoryEvent Ingestion:** Implementation of the **Memory Event Contract** buffer.

## Milestone 1: The Sovereign Database (CMOS Core)
**Goal:** Establish the persistent data structures that will survive 20+ years.
- [ ] **ForgeProfile JSON Core:** Identity, Bonds, and Thread placeholders.
- [ ] **Living Chronicle SQLite Schema:** Episodes, Chapters, and HumanVoices tables.
- [ ] **Family Mythology Registry:** Sacred Landmarks and Traditions data model.
- [ ] **The 2046 Manifest:** Implementation of checksums and format-agnostic export logic (.pfc).

## Milestone 2: The Semantic Mirror (PCAS Layer)
**Goal:** Translate the data into patterns and world state.
- [ ] **Chronicle Query Engine:** Local semantic search and thematic grouping.
- [ ] **Identity Formation Engine (IFE):** Discovery of "Threads" (Builder, Explorer) from Chronicle evidence.
- [ ] **Significance Engine:** Ranking memories for Museum and Spark navigation.

## Milestone 3: The Memory Keeper (Spark)
**Goal:** Give the companion a voice based on the archive.
- [ ] **Spark Maturation Logic:** Vocabulary and logic shifts based on Profile Age.
- [ ] **Dialogue Template Engine:** Spark referencing specific Episodes and Artifacts.
- [ ] **Historical Recall:** Spark's ability to narrate past Chapters.

## Milestone 4: The Dynamic World (Hearthhome)
**Goal:** Build the runtime Experience Shells.
- [ ] **Android Hub (2D):** The Cottage, Garden, and Clock Tower windows.
- [ ] **Museum of Childhood:** Dynamic rendering of the Memory Shelf and Project Table.
- [ ] **Legacy Object Manifestation:** Mapping CMOS IDs to shell-specific 2D/3D assets.

## Milestone 5: The Legacy Bridge
**Goal:** High-fidelity transition and handover.
- [ ] **Godot Bridge Library:** Reading the CMOS SQLite/JSON data in Godot.
- [ ] **Time Capsule Generator:** The self-contained handover bundle.
- [ ] **HTML/JS Legacy Viewer:** Zero-dependency browser-based Chronicle browser.
