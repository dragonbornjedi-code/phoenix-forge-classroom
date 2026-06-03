# Memory Museum Design: Hearthhome

The Hearthhome House is a **Living Museum of Childhood**. It is the physical and digital representation of the **Sovereign Childhood Archive**.

## 1. Design Philosophy
The museum is not a reward room. It is a space where the child's life history physically manifests. Every artifact displayed is a link to a real-world memory.

## 2. Core Modules

### The Memory Shelf (Artifact Display)
- **Manifestation:** Displays **Legacy Objects** (e.g., The Sky Feather).
- **Interaction:** Tapping plays back the original human audio and displays the photo.
- **Scaling:** The shelf expands into new rooms as the collection grows.

### Memory Books (Chronicle Narratives)
- **Manifestation:** Physical-style storybooks on the House bookshelf.
- **Content:** Procedurally generated from **Chronicle Chapters**.
- **Voice:** Spark reads these books, combining his narration with the child's original voice notes.

### The Memory Globe (Spatial Hub)
- **Manifestation:** A magical floating globe in the Wisp Nook.
- **Content:** Visualizes **Sacred Landmarks** from the **Family Mythology Engine**.
- **Expansion:** New landmasses appear when the family visits a new real-world location.

### The Hall of Companions (Relationship Mirror)
- **Manifestation:** Evolving portraits and spirit-statues.
- **Detail:** Tracks the history of bonds with Mom, Dad, Pets, and Wisps.

### The Project Table (Creative Heart)
- **Manifestation:** A rotating 2D/3D diorama of current real-world creations (LEGO, robots, art).
- **Update Logic:** Emits a `MemoryEvent` when a project is completed.

### The Time Mirror (Age Snapshot)
- **Function:** Allows viewing the world as it was at Age 5, 8, or 12.
- **Visuals:** Renders earlier experience shells (e.g., 2D APK style) within the current frontend.

## 3. World Temporal Sync
The Museum's lighting, weather, and seasonal decorations are driven by the **PCAS Environmental Engine**, reflecting the child's actual real-world context.
