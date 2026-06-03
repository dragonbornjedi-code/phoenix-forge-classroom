# Childhood Time Capsule Specification

The Childhood Time Capsule is the physical and digital export mechanism that allows a 10-year history to be handed over to the child as an adult.

## 1. The Capsule Object (.pfc)
A single, self-contained file (Compressed Archive) that contains the entirety of the **ForgeProfile** and the **Memory Engine** state.

### Contents:
- **Core State:** `profile.json`, `mythology.json`, `chronicle.db`.
- **Media Assets:** `/media/photos/`, `/media/audio/`, `/media/projects/`.
- **Schema Bundle:** `/docs/schemas/` (so the data remains interpretable).
- **Reader Tool:** A lightweight, platform-independent HTML/JS viewer that can run in any browser to browse the Chronicle without needing the Phoenix Forge software.

## 2. Handover Protocol (Age 18/25)
- **The Golden Key:** Parents define a "Handover Date" in the Teacher Edition. 
- **The Spark's Farewell:** On the handover date, Spark performs a "Final Chapter" session, summarizing the 10-year journey and presenting the Time Capsule.
- **Data Sovereignty:** At this point, the parent's control via the Teacher Edition is severed, and the ForgeProfile becomes the sole property of the child.

## 3. The Physical Bridge
The system encourages creating a **Physical Companion** for the digital archive.
- **Artifact Printouts:** High-quality prints of the "Chronicle Highlights."
- **QR Key:** A physical object (e.g., a 3D printed key) with a QR code that points to the local path of the digital Time Capsule on the home NAS (Sovereign Deck).

## 4. Multi-Platform Support
- **Android APK:** Captures the "Seed" (Ages 5-7).
- **Godot RPG:** Builds the "Tree" (Ages 8-13).
- **Legacy Suite:** Browses the "Forest" (Ages 14-25+).

## 5. Privacy Integrity
- **Encrypted Archives:** Handover archives are encrypted. The key is managed by the Parent until the handover date.
- **Redaction:** Parents have a one-time option to "Archive but Hide" specific sensitive memories before the final handover.
