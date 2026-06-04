# PHOENIX FORGE SYSTEM ATLAS

## Part 1: Repository Constitution
The Repository Constitution provides the foundational rules, principles, and architectural mandates that govern all development within the Phoenix Forge ecosystem.
- Authority Source (Doc): docs/REPOSITORY_CONSTITUTION.md
- Implementation Source (Code): README.md
- State: DOCUMENTED

## Part 2: Current State of Apps
The ecosystem comprises three primary application editions tailored to different roles in the pedagogical process: Forge Profile (Infrastructure), Student Edition (Simulation/Experience), and Teacher Edition (Curriculum Management).
- Authority Source (Doc): docs/PHOENIX_FORGE_SYSTEM_ATLAS.md
- Implementation Source (Code): phoenix-forge-classroom-forge-profile/, student-app/, phoenix-forge-classroom-teacher-edition/
- State: IMPLEMENTED

## Part 3: Cross-App Contracts
Cross-application communication relies on strictly defined contracts to ensure consistency in data interchange and operational state transitions.
- Authority Source (Doc): docs/contracts/CURRICULUM_OS_SCHEMA.md, docs/contracts/INTENT_TILE_CONTRACT.md, shared/sync-contract.md
- Implementation Source (Code): shared/sync-contract.md, docs/contracts/reference-tiles/secret-label-decoder.yaml
- State: IMPLEMENTED

## Part 4: Curriculum OS
The Curriculum OS is the central engine for managing educational pathways, activities, and structural components of the teaching pipeline within the Teacher Edition.
- Authority Source (Doc): docs/contracts/CURRICULUM_OS_SCHEMA.md
- Implementation Source (Code): phoenix-forge-classroom-teacher-edition/android/
- State: IMPLEMENTED

## Part 5: Student Simulation
The Student Simulation engine facilitates identity formation and experiential learning by modeling student progression through curated curriculum experiences.
- Authority Source (Doc): docs/IDENTITY_FORMATION_ENGINE.md
- Implementation Source (Code): phoenix-forge-classroom-forge-profile/student-app/src/main/java/com/phoenixforge/student/domain/engine
- State: PARTIAL

## Part 6: Chronicle Architecture
The Chronicle Architecture serves as the long-term data persistence and querying system for student experiences and significant historical moments (PCAS).
- Authority Source (Doc): docs/CHRONICLE_QUERY_ENGINE.md
- Implementation Source (Code): shared/schemas/PCAS_DB_SCHEMA.sql
- State: PARTIAL

## Part 7: Authority Model
The operational spine dictates the flow of authority from conceptual curriculum down to physical artifact preservation.

Operational Spine Diagram:
Curriculum Of Life
│
Curriculum OS
│
Compass
│
Intent Tiles
│
Expedition Planning
│
Lesson Evidence
│
Memory Event
│
Student Simulation
│
Chronicle Artifact

- Authority Source (Doc): docs/AUTHORITY_AND_REALITY_MAPPING.md
- Implementation Source (Code): shared/schemas/PCAS_DB_SCHEMA.sql
- State: PARTIAL

## Part 8: Repository Audit
| Component | Status | Last Audit |
|-----------|--------|------------|
| Curriculum OS | IMPLEMENTED | 2026-06-03 |
| Childhood Compass | PARTIAL | 2026-06-03 |
| Intent Tiles | IMPLEMENTED | 2026-06-03 |
| PCAS | PARTIAL | 2026-06-03 |

## Part 9: Technical Debt
- PCAS full integration with Chronicle Query Engine remains incomplete (State: PARTIAL).
- Childhood Compass integration logic in Teacher Edition is in progress (State: PARTIAL).

## Part 10: Roadmap
- Stabilize Student Simulation engine (PCAS implementation).
- Complete migration of remaining Godot components (as per docs/GODOT_MIGRATION_STRATEGY.md).
