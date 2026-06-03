# Chronicle Query Engine: Retrieving the Past

The Chronicle Query Engine allows Spark, the world, and future interfaces to semantically search and retrieve memories. It is designed to run locally on the device (Android/PC).

## 1. Local Semantic Search
Instead of simple keyword matching, the engine uses **Semantic Vectors** (local embeddings) to find related memories.
- **Query:** "Show me building projects."
- **Results:** LEGO bridge, Cardboard castle, Mud wall.
- **Technique:** Use a lightweight, local embedding model (e.g., MediaPipe or a custom dictionary-based approach for very young profiles) to index memory summaries.

## 2. The Contextual Filter
The engine provides a "Contextual Retrieval API" for Spark.
- **Spark Asks:** `queryChronicle(mood="SAD", involvedEntities=["MOSS"])`
- **Result:** Returns a memory of a time Moss Wisp helped the child when they were sad.
- **Usage:** Spark uses this to offer emotional support or encouragement.

## 3. Thematic Aggregation (The Storyteller)
Automatically groups episodes into chapters based on frequency and time.
- **Logic:** If 5 memories occur within a 2-week span with the tag "CONSTRUCTION," create a candidate chapter: "The Fort-Building Weeks."
- **Parent Approval:** Parents can rename and finalize these suggested chapters.

## 4. Multi-Modal Retrieval
- **Visual Search:** Retrieve artifacts by "Visual Category" (e.g., "All photos of bugs").
- **Chronological Search:** Retrieve the "Age 5 Highlights."
- **Entity Search:** "Everything we did with Grandpa."

## 5. Security & Privacy
- **Local Only:** All indexing and retrieval logic stays on the device.
- **Encrypted Index:** The memory index is encrypted with the ForgeProfile's primary key.
- **Mentor Gate:** Certain "Sensitive" memories (tagged by the parent) are hidden from Spark's general dialogue but accessible in the "Parent Review" view.

## 6. Future-Proofing (SQL to Vector)
- **Today:** Structured SQL queries with simple tag-based weighting.
- **Tomorrow:** Full vector database (local) for complex semantic relationships as the ForgeProfile grows and hardware becomes more capable.
