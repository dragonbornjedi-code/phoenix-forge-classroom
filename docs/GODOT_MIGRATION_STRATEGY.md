# Godot Migration Strategy: The Future Bridge

Phoenix Forge Classroom is designed with a **10-Year Trajectory**. The current Android APKs are the **First Chapter.** The future Godot RPG is the **Full World** — a Layer 3 experience shell that **imports** Ezra’s Forge Profile; it does not replace CMOS.

**Data owner:** Forge Profile on device → export bundle → Godot import. See [FORGEPROFILE_SPEC.md](FORGEPROFILE_SPEC.md) § Avatar Studio and [shared/README.md](../shared/README.md).

## 0. Import bundle (what Godot loads)

| File | Contents |
|------|----------|
| `forge_profile.json` | uid, forgeName, stage, threads, bonds (as implemented) |
| `avatar_config.json` | Full `AvatarConfig` + `godotMeshHints` for rig mapping |
| `chronicle_index.json` | Episode IDs, artifact refs, human voice refs |
| `universal_id_map.json` | ObjectID → `res://scenes/...` paths |

Import is **offline**: USB, Tailscale file share, or steward export from Forge Profile app — no cloud requirement.

## 1. Avatar → 3D (first-class)

Godot reads `avatar_config.json`:

- **Proportions:** `ageAppearance`, `bodyType` → bone scale multipliers on child rig.
- **Materials:** `skinColor`, hair/eye hex → shader parameters.
- **Attachments:** `accessories[]` + `godotMeshHints.attachmentSlots` → instanced scenes (hat, cape, wand).
- **Shards:** `shardLevel` → unlock tables for wacky mesh variants in-world.

Android Avatar Studio must emit stable enum IDs (not display strings) so Godot importers do not break on copy edits.

## 2. Shared Data Schema (The Universal Language)
To ensure the Godot RPG can reconstruct the child's world, we use a shared JSON schema for the **ForgeProfile**.
- **Godot Resource Loading:** Godot can natively parse JSON into `Resource` objects.
- **Entity ID Mapping:** 
  - `ObjectID: "blue_feather_001"` maps to `res://assets/decorations/blue_feather.png` in the APK.
  - In Godot, the same ID maps to `res://scenes/objects/blue_feather.tscn` (a 3D model with physics).

## 3. World Reconstitution
When a ForgeProfile is imported into the Godot RPG:
- **The Hub:** The 2D coordinates of the APK Hub are translated into 3D world coordinates.
- **The House:** The Godot engine reads the `houseLayout` and spawns the 3D versions of the earned furniture and trophies.
- **The Wisps:** The vibrancy and closeness levels from the APK are used to initialize the Wisp's AI behavior trees and visual effects.

## 4. Narrative Continuity
- **Dialog System:** Use a shared narrative engine (e.g., a custom JSON-based branching story format) that both Android (Kotlin) and Godot (GDScript/C#) can interpret.
- **Spark's Memory:** The `legacy` logs (voice notes, photos) are accessible in the Godot world via a "Memory Mirror" object, allowing Spark to reference the child's real-life history from years ago.

## 5. Engineering for Parity
- **AIDL to IPC:** While the APK uses AIDL for local sync, the Godot version will use a shared "Forge Bridge" library (likely C# or a C++ GDExtension) to read the same SQLite databases used by the Teacher Edition.
- **Asset Pipelines:** Source assets (textures, sounds) should be stored in high-resolution master files and exported to the appropriate format for each platform (WebP for APK, VRAM-compressed for Godot).
