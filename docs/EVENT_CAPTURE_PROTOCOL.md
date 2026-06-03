# Event Capture Protocol

The Event Capture Protocol defines how real-world life enters the digital archive. It ensures consistency across 10+ years of data ingestion.

## 1. The Capture Loop
1. **Trigger:** A real-world event occurs (found a bug, built a tower, a family joke).
2. **Interface:** Parent or child opens the "Capture Tool" (Discovery Journal).
3. **Primary Asset:** Capture **Audio first**. Voice is the highest-value asset.
4. **Secondary Asset:** Capture Photo or Video (if appropriate).
5. **Context Tagging:** System automatically attaches Time, Weather, and Location.
6. **Narrative Entry:** Spark asks a single, open-ended question ("What's the story of this?").

## 2. Source Classification
- **NATURE_FIND:** Real-world items (leaves, rocks, animals).
- **CREATIVE_WORK:** Projects (LEGO, drawings, cardboard builds).
- **SOCIAL_MOMENT:** Interactions (holiday jokes, grandparent stories).
- **MILESTONE:** Significant life changes (first lost tooth, first bike ride).
- **DAILY_REFLECTION:** Victory Journal entries.

## 3. Metadata Preservation
Every capture must include:
- `original_filename`
- `device_model`
- `human_contributor` (Who took the photo/audio?)
- `child_age_days` (To ensure precise chronological mapping later)

## 4. Integrity Check
Upon capture, the system generates a **SHA-256 Checksum** for the raw media file. This checksum is stored in the CMOS Artifact Registry to detect media rot or accidental tampering over the decades.
