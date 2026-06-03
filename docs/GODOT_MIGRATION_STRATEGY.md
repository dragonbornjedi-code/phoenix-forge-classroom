# Godot Migration Strategy: The Future Bridge

Phoenix Forge Classroom is designed with a **10-Year Trajectory**. The current Android APK is the "First Chapter." The future Godot RPG is the "Full World."

## 1. Shared Data Schema (The Universal Language)
To ensure the Godot RPG can reconstruct the child's world, we use a shared JSON schema for the **ForgeProfile**.
- **Godot Resource Loading:** Godot can natively parse JSON into `Resource` objects.
- **Entity ID Mapping:** 
  - `ObjectID: "blue_feather_001"` maps to `res://assets/decorations/blue_feather.png` in the APK.
  - In Godot, the same ID maps to `res://scenes/objects/blue_feather.tscn` (a 3D model with physics).

## 2. World Reconstitution
When a ForgeProfile is imported into the Godot RPG:
- **The Hub:** The 2D coordinates of the APK Hub are translated into 3D world coordinates.
- **The House:** The Godot engine reads the `houseLayout` and spawns the 3D versions of the earned furniture and trophies.
- **The Wisps:** The vibrancy and closeness levels from the APK are used to initialize the Wisp's AI behavior trees and visual effects.

## 3. Narrative Continuity
- **Dialog System:** Use a shared narrative engine (e.g., a custom JSON-based branching story format) that both Android (Kotlin) and Godot (GDScript/C#) can interpret.
- **Spark's Memory:** The `legacy` logs (voice notes, photos) are accessible in the Godot world via a "Memory Mirror" object, allowing Spark to reference the child's real-life history from years ago.

## 4. Engineering for Parity
- **AIDL to IPC:** While the APK uses AIDL for local sync, the Godot version will use a shared "Forge Bridge" library (likely C# or a C++ GDExtension) to read the same SQLite databases used by the Teacher Edition.
- **Asset Pipelines:** Source assets (textures, sounds) should be stored in high-resolution master files and exported to the appropriate format for each platform (WebP for APK, VRAM-compressed for Godot).
