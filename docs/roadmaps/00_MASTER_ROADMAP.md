# Master Roadmap — Phoenix Forge Classroom (repo-wide `0.00`–`5.00`)

| Field | Value |
|-------|--------|
| **Current step** | `0.00` — Genesis (protocol + build truth) |

Current step: 0.00
| **Protocol** | [cline_essence/MASTER_PROTOCOL.md](../cline_essence/MASTER_PROTOCOL.md) |
| **Tier 0** | [CONSTITUTION.md](../CONSTITUTION.md) · [registry/phoenix-forge-classroom.yaml](../../registry/phoenix-forge-classroom.yaml) |
| **Center** | Ezra’s **Forge Profile** (Avatar, chronicle, export) |
| **Products** | Forge Profile · Student Edition · Teacher Edition |

**Rule:** Nothing is “done” until `[VERIFIED]` on this step’s checklist. Sub-roadmaps (`01`–`04`) add file-level detail **only** for the active step — they do not define a competing global timeline.

**Census:** [REPOSITORY_CENSUS_AND_CONNECTIONS.md](../REPOSITORY_CENSUS_AND_CONNECTIONS.md)  
**Deploy:** [DEPLOYMENT_REALITY.md](../DEPLOYMENT_REALITY.md)  
**Drift guard:** `./scripts/cline-essence-drift-check.sh`

---

## How to advance a step

1. Complete **all** required rows for that step (Profile + Student + Teacher + Cross where marked).  
2. Run verification commands; attach proof to [DEPLOYMENT_REALITY.md](../DEPLOYMENT_REALITY.md) or PR notes.  
3. Update **Current step** in this file header + `registry/phoenix-forge-classroom.yaml` `current_step`.  
4. Update [PHOENIX_FORGE_SYSTEM_ATLAS.md](../PHOENIX_FORGE_SYSTEM_ATLAS.md) / [AUTHORITY_AND_REALITY_MAPPING.md](../AUTHORITY_AND_REALITY_MAPPING.md) if status claims change.

---

## Timeline overview

```text
0.00 Genesis ──► 0.25 Device P0 ──► 0.50 Standalone P1 ──► 0.75 Contracts
      ──► 1.00 Integration ──► 1.50 Loop partial ──► 2.00 LOOP VERIFIED
      ──► 2.50 Profile depth ──► 3.00 Student depth ──► 3.50 Teacher depth
      ──► 4.00 CMOS bridge ──► 4.50 Chronicle ──► 5.00 REPO 100% COMPLETE
```

| Step | Milestone | Forge Profile | Student Edition | Teacher Edition | Cross-app |
|------|-----------|---------------|-----------------|-----------------|-----------|
| **0.00** | Genesis | — | — | — | Protocol live |
| **0.25** | P0 device truth | Nav + cold start | Launcher + cold start | Board persist phone | Install 3 APKs |
| **0.50** | P1 standalone | Avatar P1-A + capture stub | Home/quests stable | P1c drag + detail | Provider read OK |
| **0.75** | Contracts | Export DTOs align | QuestPayload types | YAML tile load | Shared Kotlin |
| **1.00** | Integration scaffold | Provider write spec | Quest from tile stub | Send tile stub | Transport chosen |
| **1.50** | Loop 50% | Timeline on complete | Quest UI from tile | Receive completion | 3/5 stages |
| **2.00** | **Loop proven** | MemoryEvent row | Quest complete flow | Compass signal min | 10-step checklist |
| **2.50** | Profile depth | AvatarConfig full | Avatar import full | Profile viewer | Export JSON |
| **3.00** | Student depth | Chronicle read | Hearthhome hub | — | Loop stable |
| **3.50** | Teacher depth | Steward analytics | — | 7-day + search | Auto tile→event |
| **4.00** | CMOS bridge | Unified ingest | LifeEvent bridge | Approval UI | TILE_EVENT_MAP |
| **4.50** | Chronicle tier | .pfc export | Story chronicle | Compass snapshot | Significance |
| **5.00** | **Repo 100%** | CMOS P3 in-repo | Shell spec met | Product-spec core | Sync contract |

**At `5.00`:** All in-repo north-star scope `[VERIFIED]`. Godot/Sovereign repos explicitly **out of scope** but import contracts `[VERIFIED]`.

---

## Step `0.00` — Genesis (YOU ARE HERE)

**Objective:** Agents, registry, builds, and docs agree on one path before feature work counts.

| ID | Deliverable | Verification | Status |
|----|-------------|--------------|--------|
| 0.00-A | Cline Essence protocol + drift script | `./scripts/cline-essence-drift-check.sh` exit 0 | [TESTED] |
| 0.00-B | Tier 0 `CONSTITUTION.md` + registry YAML | Files exist | [VERIFIED] |
| 0.00-C | Census + alignment report current | No phantom doc links | [VERIFIED] |
| 0.00-D | All 3 modules `assembleDebug` | registry `verification_commands.build_all` | [TESTED] |
| 0.00-E | Master roadmap `0.00`–`5.00` published | This file | [VERIFIED] |

**Advance to `0.25` when:** 0.00-D remains green and 0.25-A started on device.

**Detail:** [01](01_FORGE_PROFILE_ROADMAP.md) P0 · [02](02_STUDENT_EDITION_ROADMAP.md) P0 · [03](03_TEACHER_EDITION_ROADMAP.md) P0

---

## Step `0.25` — Device truth (all apps P0)

| ID | Forge Profile | Student | Teacher | Cross |
|----|---------------|---------|---------|-------|
| 0.25-A | Bottom nav works | Correct launcher name | Expedition board opens | `install-phone-apks.sh` |
| 0.25-B | Cold start no crash | Cold start no crash | Create tile + persist | DEPLOYMENT matrix row |
| 0.25-C | Provider read smoke | Import screen reads profile | Field guide save/complete | 3 APKs same device |

**Advance to `0.50` when:** All 0.25 rows `[VERIFIED]` on parent phone.

---

## Step `0.50` — Standalone P1 (each app valuable alone)

| ID | Forge Profile | Student | Teacher | Cross |
|----|---------------|---------|---------|-------|
| 0.50-A | Avatar P1-A: layered preview + auto-save | Home + quests daily-usable | P1a–P1b already; **P1c drag** | — |
| 0.50-B | Artifact capture (photo/audio) | Import verified E2E | Tile detail all fields | — |
| 0.50-C | Timeline from real events | NPC/quest no crash paths | Seed + reorder persist | Provider stable |

**Advance to `0.75` when:** 0.50 complete; **do not** start Hearthhome/Godot depth before 0.75.

**Detail:** [01](01_FORGE_PROFILE_ROADMAP.md) P1-A · [03](03_TEACHER_EDITION_ROADMAP.md) P1c

---

## Step `0.75` — Contract layer (shared truth)

| ID | All modules |
|----|-------------|
| 0.75-A | `IntentTile`, `MemoryEvent`, `QuestPayload` Kotlin ↔ contracts |
| 0.75-B | `secret-label-decoder.yaml` loads in Teacher |
| 0.75-C | `shared/` or `forge-profile-core` export package compiles |
| 0.75-D | Atlas rows updated PARTIAL→IMPLEMENTED only with proof |

**Advance to `1.00` when:** 0.75 `[TESTED]` compile + loader unit test.

**Detail:** [04](04_CROSS_APP_INTEGRATION_ROADMAP.md) P1

---

## Step `1.00` — Integration scaffold

| ID | Deliverable |
|----|-------------|
| 1.00-A | Transport decision (provider / file / both) documented |
| 1.00-B | Teacher send-tile → Student receives stub payload |
| 1.00-C | Profile write path for MemoryEvent (guarded) |

---

## Step `1.50` — Loop 50%

| ID | Stage |
|----|-------|
| 1.50-A | Teacher create/send tile |
| 1.50-B | Student quest from tile |
| 1.50-C | Complete → local event (not yet CMOS) |

---

## Step `2.00` — Loop proven (irreversible)

| ID | Deliverable | Proof |
|----|-------------|-------|
| 2.00-A | Full Tile → Quest → MemoryEvent → Profile timeline → Teacher signal | [reference-tiles/README.md](../contracts/reference-tiles/README.md) 10-step checklist `[VERIFIED]` |
| 2.00-B | Screenshots or written field notes in DEPLOYMENT_REALITY | Steward sign-off |

**Advance to `2.50` only after 2.00.** This is the product’s magic moment.

---

## Step `2.50` — Profile depth (Ezra identity)

| ID | Deliverable |
|----|-------------|
| 2.50-A | `AvatarConfig` + export JSON + Godot hints |
| 2.50-B | Student `/avatar` full payload |
| 2.50-C | Teacher profile viewer complete |

**Detail:** [01](01_FORGE_PROFILE_ROADMAP.md) P1-A complete · [FORGEPROFILE_SPEC.md](../FORGEPROFILE_SPEC.md)

---

## Step `3.00` — Student depth

| ID | Deliverable |
|----|-------------|
| 3.00-A | Hearthhome hub per EXPERIENCE_SHELL_SPEC |
| 3.00-B | Spark companion stateful |
| 3.00-C | Identity lenses UI (IFE-linked) |

**Detail:** [02](02_STUDENT_EDITION_ROADMAP.md) P2–P3

---

## Step `3.50` — Teacher depth

| ID | Deliverable |
|----|-------------|
| 3.50-A | 7-day + monthly views |
| 3.50-B | Search + narrative wrapper accept |
| 3.50-C | Compass snapshot minimal |

**Detail:** [03](03_TEACHER_EDITION_ROADMAP.md) P2–P3

---

## Step `4.00` — CMOS bridge

| ID | Deliverable |
|----|-------------|
| 4.00-A | Unified MemoryEvent pipeline |
| 4.00-B | TILE_EVENT_MAPPING implemented |
| 4.00-C | Child cannot delete validated CMOS |

---

## Step `4.50` — Chronicle tier

| ID | Deliverable |
|----|-------------|
| 4.50-A | .pfc export smoke `[VERIFIED]` |
| 4.50-B | Chronicle promotion + significance rank |
| 4.50-C | sync-contract implemented |

---

## Step `5.00` — Repo-wide 100% complete

**Definition:** Every row in [REPOSITORY_CENSUS](../REPOSITORY_CENSUS_AND_CONNECTIONS.md) for in-repo apps is **IMPLEMENTED** or tagged **DOC-COMPLETE** with code `[VERIFIED]`; no CONTRADICTION tags; drift script daily green; DEPLOYMENT matrix full Pass.

| App | 5.00 exit |
|-----|-----------|
| **Forge Profile** | Avatar Studio, artifacts, timeline, export, bonds/threads per spec |
| **Student Edition** | EXPERIENCE_SHELL_SPEC + HEARTHHOME + SPARK met |
| **Teacher Edition** | product-spec core tabs + Expedition + Compass seed |
| **Cross-app** | Loop + sync automated offline |

**Out of repo at 5.00:** Godot world binary, Sovereign Deck — only **import contracts** must be `[VERIFIED]`.

---

## Sub-roadmap index (detail only)

| Track | Document | Use when master step ≥ |
|-------|----------|------------------------|
| A | [01_FORGE_PROFILE_ROADMAP.md](01_FORGE_PROFILE_ROADMAP.md) | 0.25+ |
| B | [02_STUDENT_EDITION_ROADMAP.md](02_STUDENT_EDITION_ROADMAP.md) | 0.25+ |
| C | [03_TEACHER_EDITION_ROADMAP.md](03_TEACHER_EDITION_ROADMAP.md) | 0.25+ |
| D | [04_CROSS_APP_INTEGRATION_ROADMAP.md](04_CROSS_APP_INTEGRATION_ROADMAP.md) | 0.75+ |

---

## Legacy phase names (mapped)

| Old phase | Master step |
|-----------|-------------|
| Phase 0 P0 | 0.25 |
| Phase 1 P1 | 0.50 |
| Phase 2 contracts | 0.75 |
| Phase 3 one loop | 2.00 |
| Phase 4 depth | 2.50–4.50 |
| Phase 5 external | 5.00+ (external repos) |

---

*Last protocol integration: 2026-06-04 · cline-essence v1.0.0*
