# Master Roadmap — Phoenix Forge Classroom

**The only repo-wide todo list.** Every feature, fix, doc, and integration step lives here as a decimal `X.XX`. Nothing else schedules work.

| Field | Value |
|-------|--------|
| **Finish line** | `5.00` |
| **Current step** | `0.24` — next: `0.25` |

Current step: 0.24 — next: 0.25

| **Protocol** | [cline_essence/SKILL.md](../cline_essence/SKILL.md) · [MASTER_PROTOCOL.md](../cline_essence/MASTER_PROTOCOL.md) |
| **Tier 0** | [CONSTITUTION.md](../CONSTITUTION.md) · [registry/phoenix-forge-classroom.yaml](../../registry/phoenix-forge-classroom.yaml) |
| **Sub-roadmaps** | [01](01_FORGE_PROFILE_ROADMAP.md) · [02](02_STUDENT_EDITION_ROADMAP.md) · [03](03_TEACHER_EDITION_ROADMAP.md) · [04](04_CROSS_APP_INTEGRATION_ROADMAP.md) — **indexes only**, no parallel IDs |

**Drift:** `./scripts/cline-essence-drift-check.sh` · **Deploy:** [DEPLOYMENT_REALITY.md](../DEPLOYMENT_REALITY.md)

---

## Numbering & insertion rules (read first)

| Rule | Meaning |
|------|---------|
| **Format** | `X.XX` only — never letters (`0.00-A` forbidden) |
| **Range** | `0.00` … `5.00` inclusive |
| **Increment** | Each new step ≥ **+0.01** above the highest step already in this file |
| **Forward only** | If you are at `3.24`, new work is `3.25`, `3.26`, … — **never** `3.23` or renumber old steps |
| **Insert before finish** | New ideas get the next free decimal **before** `5.00` (e.g. squeeze at `4.91` if `4.92` reserved) |
| **One path** | No alternate roadmaps; sub-roadmaps link here by step range |
| **Done** | Row is `[VERIFIED]` or `[TESTED]`; then bump `Current step` + registry `current_step` |

**New feature rule:** Add rows to this file → run drift check → implement → verify. Do not build “on the side” without a step number.

---

## Milestone ladder

```text
0.00 Genesis          0.01 ── 0.24
0.25 Device P0        0.26 ── 0.49
0.50 Standalone P1    0.51 ── 0.74
0.75 Contracts        0.76 ── 0.99
1.00 Integration      1.01 ── 1.24
1.50 Loop half        1.51 ── 1.74
2.00 Loop proven      2.01 ── 2.24
2.50 Profile depth    2.51 ── 2.74
3.00 Student depth    3.01 ── 3.24
3.50 Teacher depth    3.51 ── 3.74
4.00 CMOS bridge      4.01 ── 4.24
4.50 Chronicle        4.51 ── 4.91
5.00 Repo complete    (finish line)
```

---

## Master todo ledger

| Step | App | Deliverable | Verification | Status |
|------|-----|-------------|--------------|--------|
| **0.00** | — | **MILESTONE: Genesis** | — | [VERIFIED] |
| 0.01 | Cross | Cline Essence SKILL v1.1.0 in repo | `docs/cline_essence/SKILL.md` | [VERIFIED] |
| 0.02 | Cross | MASTER_PROTOCOL + SKILL_STACK + AGENT_EXTENSIONS | Files exist | [VERIFIED] |
| 0.03 | Cross | `cline-essence-drift-check.sh` passes | exit 0 | [VERIFIED] |
| 0.04 | Cross | CONSTITUTION + registry YAML Tier 0 | Files exist | [VERIFIED] |
| 0.05 | Cross | Census + alignment report current | No phantom doc links | [VERIFIED] |
| 0.06 | Cross | This file: single decimal ledger, no letter IDs | drift check | [VERIFIED] |
| 0.07 | Cross | `assembleDebug` forge-profile-app | gradle | [VERIFIED] |
| 0.08 | Cross | `assembleDebug` student-app | gradle | [VERIFIED] |
| 0.09 | Cross | `assembleDebug` teacher-app | gradle | [VERIFIED] |
| 0.10 | Cross | DEPLOYMENT_REALITY install section accurate | Read paths | [VERIFIED] |
| 0.11 | Profile | Bootstrap profile for Ezra on first launch | ProfileBootstrap | [VERIFIED] |
| 0.12 | Profile | Dashboard + all bottom-nav tabs navigate | Phone | [VERIFIED] |
| 0.13 | Student | Launcher label final | strings.xml | [VERIFIED] |
| 0.14 | Student | Remove “Digital House” from user-facing strings | grep UI | [VERIFIED] |
| 0.15 | Teacher | Launcher label final | strings.xml | [VERIFIED] |
| 0.16 | Cross | `install-phone-apks.sh` executable | chmod + run | [VERIFIED] |
| 0.17 | Cross | reference-tile YAML on disk | secret-label-decoder.yaml | [VERIFIED] |
| 0.18 | Cross | sync-contract + PCAS SQL present | shared/ | [VERIFIED] |
| 0.19 | Cross | Superpowers plans marked SUPERSEDED | plans/*.md headers | [VERIFIED] |
| 0.20 | Cross | Systemd drift timer documented | scripts/systemd/ | [VERIFIED] |
| 0.21 | Cross | Root + docs README point to this ledger | Links | [VERIFIED] |
| 0.22 | Cross | Sub-roadmaps 01–04 index-only (no P0/P1) | grep no “P1c” as ID | [VERIFIED] |
| 0.23 | Cross | Atlas + Authority rows match genesis truth | Doc pass | [VERIFIED] |
| 0.24 | Cross | Genesis complete: 0.01–0.23 verified | Checklist | [VERIFIED] |
| | | | | |
| **0.25** | — | **MILESTONE: Device P0 begins** | — | pending |
| 0.26 | Cross | Install 3 APKs on parent phone | install script | pending |
| 0.27 | Profile | Bottom navigation works | Phone | pending |
| 0.28 | Profile | Cold start no crash (3×) | Phone | pending |
| 0.29 | Profile | Identity card screen loads | Phone | pending |
| 0.30 | Student | Cold start no crash (3×) | Phone | pending |
| 0.31 | Student | StudentWorldBootstrap completes | Log/no crash | pending |
| 0.32 | Student | Home screen loads | Phone | pending |
| 0.33 | Teacher | Expedition board opens with seed tiles | Phone | pending |
| 0.34 | Teacher | Create new tile from FAB | Phone | pending |
| 0.35 | Teacher | Tile field guide: save materials/coaching | Phone | pending |
| 0.36 | Teacher | Mark tile complete + force-close persist | Phone | pending |
| 0.37 | Student | Import Forge Profile screen reads provider | Phone | pending |
| 0.38 | Cross | ContentProvider `/profile` read smoke | adb/log | pending |
| 0.39 | Cross | ContentProvider `/avatar` read smoke | adb/log | pending |
| 0.40 | Cross | DEPLOYMENT matrix Pass/Fail filled | DEPLOYMENT_REALITY | pending |
| 0.41 | Teacher | ForgeProfileViewerScreen opens | Phone | pending |
| 0.42 | Student | Quests screen runs one quest | Phone | pending |
| 0.43 | Student | Gallery + vault screens open | Phone | pending |
| 0.44 | Profile | Timeline screen shows events | Phone | pending |
| 0.45 | Profile | Memory capsule screen opens | Phone | pending |
| 0.46 | Student | Settings screen opens | Phone | pending |
| 0.47 | Student | Story archive opens | Phone | pending |
| 0.48 | Student | NPC room opens | Phone | pending |
| 0.49 | Cross | Device P0 complete: all 0.26–0.48 verified | Checklist | pending |
| | | | | |
| **0.50** | — | **MILESTONE: Standalone P1 begins** | — | pending |
| 0.51 | Teacher | Expedition board scroll + empty state | Phone | pending |
| 0.52 | Teacher | Drag-and-drop reorder UI | Phone | pending |
| 0.53 | Teacher | `sortOrder` persist after reorder | Phone | pending |
| 0.54 | Teacher | Steward L3 dimension fields on complete | TileDetail | pending |
| 0.55 | Teacher | “Start day” export list stub | system-initialization | pending |
| 0.56 | Profile | Avatar layered preview composable | AvatarStudio | pending |
| 0.57 | Profile | Avatar category panels + color pickers | AvatarStudio | pending |
| 0.58 | Profile | Avatar auto-save + Randomize | Room | pending |
| 0.59 | Profile | `Avatar` model → `AvatarConfig` schema | FORGEPROFILE_SPEC | pending |
| 0.60 | Profile | Avatar export JSON + share intent | File | pending |
| 0.61 | Profile | Provider `/avatar` full payload | Student import | pending |
| 0.62 | Profile | Avatar shard level 0–6 UI | AvatarStudio | pending |
| 0.63 | Profile | Photo artifact capture | Camera | pending |
| 0.64 | Profile | Audio artifact capture | Mic | pending |
| 0.65 | Profile | Memory capsule records artifact | MemoryCapsule | pending |
| 0.66 | Profile | Timeline from real capture events | Timeline | pending |
| 0.67 | Profile | Steward gate copy clear | STUDENT_TEACHER_BOUNDARY | pending |
| 0.68 | Student | Home usable 15 min parent-free | Field test | pending |
| 0.69 | Student | QuestEngine + Quests UI stable | Phone | pending |
| 0.70 | Student | WorldOrchestrator regression (no rip-out) | Unit/smoke | pending |
| 0.71 | Student | Gallery + vault reflect play | Phone | pending |
| 0.72 | Student | Story drift panel sane values | Phone | pending |
| 0.73 | Student | Settings offline-first copy | strings | pending |
| 0.74 | Cross | Standalone P1 complete: 0.51–0.73 verified | Checklist | pending |
| | | | | |
| **0.75** | — | **MILESTONE: Contracts** | — | pending |
| 0.76 | Cross | `IntentTile` Kotlin matches contract | Compile | pending |
| 0.77 | Cross | `MemoryEvent` Kotlin matches contract | Compile | pending |
| 0.78 | Cross | `QuestPayload` Kotlin matches contract | Compile | pending |
| 0.79 | Teacher | Load `secret-label-decoder.yaml` | Parser test | pending |
| 0.80 | Cross | Shared types module or forge-profile export | gradle | pending |
| 0.81 | Cross | Document transport: provider vs file vs both | OFFLINE + sync-contract | pending |
| 0.82 | Cross | TILE_EVENT_MAPPING reviewed for P2 | Doc | pending |
| 0.83 | Cross | CURRICULUM_OS_SCHEMA trimmed to P2 needs | Doc | pending |
| 0.84 | Cross | Atlas contract rows = PARTIAL until proof | Authority | pending |
| 0.85 | Cross | Contracts complete: 0.76–0.84 | Checklist | pending |
| | | | | |
| **1.00** | — | **MILESTONE: Integration scaffold** | — | pending |
| 1.01 | Cross | Handoff API chosen and documented | Doc | pending |
| 1.02 | Teacher | Send tile → Student stub payload | E2E stub | pending |
| 1.03 | Student | Receive quest payload route | Nav + VM | pending |
| 1.04 | Profile | MemoryEvent write API (steward-guarded) | Room | pending |
| 1.05 | Teacher | Read Student completion stub | UI | pending |
| 1.06 | Cross | Integration scaffold complete | Checklist | pending |
| | | | | |
| **1.50** | — | **MILESTONE: Loop 50%** | — | pending |
| 1.51 | Teacher | Create/send Observation-style tile | Phone | pending |
| 1.52 | Student | Quest appears from tile | Phone | pending |
| 1.53 | Student | Complete quest → local LifeEvent | Room | pending |
| 1.54 | Profile | Local event visible (pre-CMOS) | Timeline stub | pending |
| 1.55 | Cross | Loop 50% complete | Checklist | pending |
| | | | | |
| **2.00** | — | **MILESTONE: Loop proven** | — | pending |
| 2.01 | Teacher | Tile → export/handoff → Student | E2E | pending |
| 2.02 | Student | Quest complete → MemoryEvent shape | Contract | pending |
| 2.03 | Profile | MemoryEvent on Profile timeline | Timeline | pending |
| 2.04 | Teacher | Minimal compass signal after complete | UI stub | pending |
| 2.05 | Cross | reference-tiles 10-step checklist | README proof | pending |
| 2.06 | Cross | DEPLOYMENT field notes + screenshots | Doc | pending |
| 2.07 | Cross | Loop proven complete | Checklist | pending |
| | | | | |
| **2.50** | — | **MILESTONE: Profile depth** | — | pending |
| 2.51 | Profile | `godotMeshHints` in avatar export | JSON | pending |
| 2.52 | Profile | `.pfc` export smoke test | File | pending |
| 2.53 | Profile | Avatar thread-bias suggestions | IFE doc | pending |
| 2.54 | Student | Full avatar import (not summary string) | Provider | pending |
| 2.55 | Teacher | Profile viewer: identity + avatar | ForgeProfileViewer | pending |
| 2.56 | Profile | bonds graph stub in schema | FORGEPROFILE_SPEC | pending |
| 2.57 | Profile | Profile depth complete | Checklist | pending |
| | | | | |
| **3.00** | — | **MILESTONE: Student depth** | — | pending |
| 3.01 | Student | Hearthhome hub shell (world as menu) | HEARTHHOME UX | pending |
| 3.02 | Student | Spark companion stateful loop | SPARK UX | pending |
| 3.03 | Student | Identity lenses UI | IDENTITY_LENSES | pending |
| 3.04 | Student | Quest copy curriculum-aware stub | starter-lessons | pending |
| 3.05 | Student | Optional: rename DigitalHouse → StudentHome | Code cleanup | pending |
| 3.06 | Student | Student depth complete | Checklist | pending |
| | | | | |
| **3.50** | — | **MILESTONE: Teacher depth** | — | pending |
| 3.51 | Teacher | 7-day expedition overview | product-spec | pending |
| 3.52 | Teacher | Monthly thematic overview | thematic-playthroughs | pending |
| 3.53 | Teacher | Search/filter tiles | taxonomy | pending |
| 3.54 | Teacher | Narrative wrapper accept flow | QUEST_ENGINE | pending |
| 3.55 | Teacher | CompassSnapshot minimal compute | COMPASS_ENGINE | pending |
| 3.56 | Teacher | curriculum-of-life tags on tiles | Metadata | pending |
| 3.57 | Teacher | starter-lessons import path | Pack 01 | pending |
| 3.58 | Teacher | Teacher depth complete | Checklist | pending |
| | | | | |
| **4.00** | — | **MILESTONE: CMOS bridge** | — | pending |
| 4.01 | Cross | Unified MemoryEvent pipeline | MEMORY_EVENT | pending |
| 4.02 | Cross | TILE_EVENT_MAPPING implemented | Code | pending |
| 4.03 | Profile | Promote buffer → chronicle | CURRICULUM_RUNTIME | pending |
| 4.04 | Cross | Child cannot delete validated CMOS | Boundary test | pending |
| 4.05 | Teacher | Steward approves chronicle promotion | UI | pending |
| 4.06 | Cross | CMOS bridge complete | Checklist | pending |
| | | | | |
| **4.50** | — | **MILESTONE: Chronicle tier** | — | pending |
| 4.51 | Profile | Artifact registry + checksums | MEMORY_ENGINE | pending |
| 4.52 | Cross | Significance engine rank stub | SIGNIFICANCE | pending |
| 4.53 | Cross | sync-contract implemented | shared/ | pending |
| 4.54 | Cross | Identity threads parent confirm | IFE | pending |
| 4.55 | Cross | PCAS runtime DB eval (implement or defer doc) | PCAS SQL | pending |
| 4.56 | Cross | Chronicle tier complete | Checklist | pending |
| 4.57 | Profile | Sign-in gate + empty dream board + Help me think | gradle | [VERIFIED] |
| 4.58 | Teacher | Curriculum browser: 7 domains + Pack 01 + weekly audit | gradle | [VERIFIED] |
| 4.59 | Cross | Forge Profile identity-only; mentorship terminology purge; Student→Teacher read-only snapshot provider + UI | `rg -i no stewardship` + `./gradlew assembleDebug (3 APKs)` + `./scripts/cline-essence-drift-check.sh` | [TESTED] |
| 4.91 | Cross | *(gap before finish)* | — | reserved |
| 4.92 | Cross | *(gap before finish)* | — | reserved |
| | | | | |
| **5.00** | — | **MILESTONE: Repo 100% complete** | Census + drift + phone | pending |

---

## At `5.00` (finish line)

| Requirement | |
|-------------|--|
| All steps `0.01`–`4.99` required for in-repo scope are `[VERIFIED]` or `[TESTED]` |
| Forge Profile | Avatar Studio, artifacts, timeline, export, CMOS participant |
| Student Edition | Shell spec + Hearthhome + Spark + loop participant |
| Teacher Edition | Expedition board full + product-spec core + compass seed |
| Cross-app | Loop automated offline; contracts implemented |
| Out of repo | Godot binary, Sovereign Deck — import contracts only `[VERIFIED]` |

---

## How sub-roadmaps work now

| File | Role |
|------|------|
| [01_FORGE_PROFILE_ROADMAP.md](01_FORGE_PROFILE_ROADMAP.md) | Profile step ranges + file paths |
| [02_STUDENT_EDITION_ROADMAP.md](02_STUDENT_EDITION_ROADMAP.md) | Student step ranges + file paths |
| [03_TEACHER_EDITION_ROADMAP.md](03_TEACHER_EDITION_ROADMAP.md) | Teacher step ranges + file paths |
| [04_CROSS_APP_INTEGRATION_ROADMAP.md](04_CROSS_APP_INTEGRATION_ROADMAP.md) | Cross-app step ranges + contracts |

**Adding work:** edit **this file** first (new decimal row), then add file hints to the matching sub-roadmap index.

---

*Ledger version: 2.0 · decimal-only · forward insertion · 2026-06-04*
