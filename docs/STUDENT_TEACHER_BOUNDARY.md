# Student ↔ Teacher Boundary Specification

This document defines the strict functional boundaries between the Student Edition (Experience) and the Teacher Edition (Mentorship).

## 1. The Student Domain
**Role:** Exploration, Discovery, Creation, Reflection.
- **Responsibilities:**
  - Navigating the **Hearthhome** world.
  - Participating in **PCAS-generated Quests**.
  - Capturing real-world moments via the **Discovery Journal**.
  - Sharing daily reflections via the **Victory Journal**.
  - Interacting with **Spark** and the **Wisps**.
- **Data Access:** Read-only access to approved CMOS records. Write access only to the **MemoryEvent Buffer**.

## 2. The Teacher Domain
**Role:** Mentorship, Curation, Curation, Preservation.
- **Responsibilities:**
  - **Expedition Mentorship:** Viewing student emotional check-ins, grades, and lesson progress in a read-only snapshot, plus marking tiles complete on the expedition board with evidence/notes.
  - **Mythology Management:** Registering landmarks, traditions, and family lore.
  - **Identity Confirmation:** Reviewing and confirming "Threads" discovered by the IFE.
  - **Archive Operations:** Exporting/Importing PFC bundles and executing the Handover Protocol.
  - **Narrative Guidance:** Setting the "maturation tier" for Spark.
- **Data Access:** Read/Write access to **Mentorship Metadata** stored in Teacher Edition (board notes + completion marks). Emotional check-ins, grades, and lesson progress are **read-only** views of student-owned data.

## 3. The Immutable Boundary
- **Student cannot delete history:** The child can "hide" something in their world, but the record stays in the CMOS until the parent/adult self makes a archival decision.
- **Teacher cannot rewrite memory:** The parent can add a "Mentor Note" or an "Alternative Title," but the child's original voice note and photo remain the primary, immutable evidence of the event.
- **Ownership Shift:** At the Handover (Age 18/25), the Teacher Domain controls are disabled, and the Student Domain evolves into the **Sovereign Legacy Suite**.
