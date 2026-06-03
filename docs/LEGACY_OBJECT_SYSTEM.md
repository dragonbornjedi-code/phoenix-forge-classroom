# Legacy Object System

Humans bond with objects, not databases. The Legacy Object System ensures that digital artifacts in Phoenix Forge feel like tangible, evolving heirlooms.

## 1. Object Lifecycle
1. **The Discovery:** A raw asset (photo) is captured.
2. **The Artifact:** The asset is registered in CMOS with a `universal_id`.
3. **The Manifestation:** 
   - **APK Shell (Age 5):** Rendered as a 2D sprite with a glow effect.
   - **Godot Shell (Age 10):** Rendered as a 3D mesh (procedural or static) that the child can pick up and move.
   - **XR Museum (Age 18):** Rendered as a high-fidelity virtual object in an immersive space.

## 2. Evolving Representation
As the tech stack evolves, the *representation* of the object updates, but the *identity* remains fixed.
- **ID: `nature_feather_blue`**
- **APK:** `res://icons/feather_blue.png`
- **Godot:** `res://models/artifacts/feather_blue.glb`
- **Logic:** The CMOS `ArtifactRegistry` stores the ID; the Shell provides the highest-quality visualization it can support.

## 3. Interactivity & Lore
Legacy Objects are more than visuals; they accumulate lore.
- **Story Anchors:** Objects can be "Attached" to Chronicle Chapters.
- **Companion Bond:** Spark might have specific dialogue associated with an object ("I remember how windy it was when we found this!").
- **Gifting:** Wisps can "Bless" an object, adding magical visual effects (particles, sound loops) based on the child's identity threads.

## 4. Physical Bridges
The system encourages the "Digital to Physical" transition.
- **3D Printing:** Exporting Legacy Objects (like a Sandbox build) for 3D printing.
- **Memory Cards:** Printing 2D "Artifact Cards" with QR codes that, when scanned by the Student Edition, summon the object's digital manifestation in Hearthhome.
