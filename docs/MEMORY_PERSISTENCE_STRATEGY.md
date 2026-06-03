# Memory Persistence Strategy: The 20-Year Durability Plan

To ensure a childhood history survives for two decades, we must design for hardware failure, platform obsolescence, and format shifts.

## 1. Format-Agnostic Storage (The Seed)
- **Base Format:** Everything is stored as **JSON (UTF-8)** with absolute paths for media.
- **Master Registry:** A `MANIFEST.json` file at the root of the **ForgeProfile** archive that tracks every file and its checksum.
- **Human-Readable Schema:** The schema (defined in `shared/schemas/`) is bundled with the data, ensuring any future developer can interpret the raw text files.

## 2. Media Robustness
- **Format:** Prefer **Standard, High-Quality** formats.
  - **Photos:** Original JPG/PNG (High Res) + WebP Thumbnails.
  - **Audio:** Original WAV/FLAC + OGG/MP3 Previews.
  - **3D:** glTF/GLB (Universal Standard) for project captures.
- **Redundancy:** Media is stored in a flat directory structure (`/media/artifacts/`) to avoid deep nesting issues in different filesystems.

## 3. The Migration Bridge (PFC Bundle)
The `.pfc` (Phoenix Forge Chronicle) bundle is the 20-year vehicle.
- **Structure:** A ZIP archive containing:
  - `profile.json` (The ForgeProfile)
  - `chronicle.db` (The SQLite Database)
  - `mythology.json`
  - `/media/` (All assets)
  - `/docs/` (The original schemas)
- **Versioning:** Every bundle includes a `PROTOCOL_VERSION`. Future platforms (e.g., Godot) will contain "Bridge Interpreters" for versions 1, 2, 3, etc.

## 4. Hardware Portability
- **The "External Heart":** The system encourages parents to periodically export the `.pfc` bundle to an external physical drive (SSD/SD Card) or a home NAS (Sovereign Deck).
- **No DRM:** The data is never locked to a specific device ID or vendor. It belongs to the child.

## 5. Continuity Validation
Every major platform shift (e.g., APK to Godot) includes a **"Legacy Walkthrough."**
- **Step 1:** Import `.pfc`.
- **Step 2:** System runs a "Consistency Check" (all media present? all references valid?).
- **Step 3:** System generates "Legacy Objects" for the new engine (2D sprites -> 3D meshes).
- **Step 4:** Spark performs a "Recap Session" to confirm the new world "remembers" correctly.

## 6. The 2046 Test
We design by asking: "Can a standard computer in the year 2046 read a ZIP file and a JSON document?" 
- The answer is **Yes**. 
- Therefore, we avoid proprietary binary formats or cloud-only APIs.
