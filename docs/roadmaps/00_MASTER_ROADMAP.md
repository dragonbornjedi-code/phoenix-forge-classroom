# Master Roadmap — Phoenix Forge Classroom (`0.00` → `5.00`)

| Field | Value |
|-------|--------|
| **Milestone** | `0.00` Genesis |
| **Current step** | `0.04` — decimal roadmap published (next: `0.05`) |

Current step: 0.04

| **Protocol** | [cline_essence/SKILL.md](../cline_essence/SKILL.md) · [MASTER_PROTOCOL.md](../cline_essence/MASTER_PROTOCOL.md) |
| **Tier 0** | [CONSTITUTION.md](../CONSTITUTION.md) · [registry/phoenix-forge-classroom.yaml](../../registry/phoenix-forge-classroom.yaml) |
| **Center** | Ezra · **Forge Profile** (Avatar, chronicle, export) |
| **Products** | Forge Profile · Student Edition · Teacher Edition |

---

## Step numbering rules (non-negotiable)

1. **Format:** `X.XX` only — two decimal places (e.g. `0.01`, `2.53`, `4.92`).
2. **Never** append letters (`0.00-A`, `1.50-B`, etc.) — **forbidden**.
3. **Increment:** each new step is **≥ +0.01** above the previous step in this file.
4. **Gaps allowed:** skip numbers (e.g. `0.07` then `0.09`) to leave room for future inserts like `0.08`.
5. **Milestones:** round numbers (`0.25`, `0.50`, `1.00`, `2.00`, `5.00`) mark phase gates — still no letters.
6. **Single timeline:** only this file defines `0.00`–`5.00`. Sub-roadmaps `01`–`04` are detail, not step IDs.

**Advance `Current step`:** set to the **next incomplete** step after the last `[VERIFIED]` row. Update `registry/phoenix-forge-classroom.yaml` `current_step` to match.

**Drift check:** `./scripts/cline-essence-drift-check.sh` (fails if letter-suffix steps appear in this file).

---

## Milestone map

```text
0.00 ── 0.01…0.24 Genesis + device P0
0.25 ── 0.26…0.49 P0 verified on phone
0.50 ── 0.51…0.74 Standalone P1 (three apps)
0.75 ── 0.76…0.99 Shared contracts
1.00 ── 1.01…1.49 Integration scaffold
1.50 ── 1.51…1.99 Loop 50%
2.00 ── 2.01…2.49 Loop VERIFIED (irreversible)
2.50 ── 2.51…2.99 Forge Profile depth (Avatar, export)
3.00 ── 3.01…3.49 Student depth (Hearthhome, Spark)
3.50 ── 3.51…3.99 Teacher depth (7-day, compass)
4.00 ── 4.01…4.49 CMOS / MemoryEvent bridge
4.50 ── 4.51…4.99 Chronicle, .pfc, sync
5.00 ── Repo-wide 100% complete (in-repo scope)
```

---

## Master step ledger (repo-wide)

| Step | App | Deliverable | Verification | Status |
|------|-----|-------------|--------------|--------|
| **0.00** | — | **MILESTONE: Genesis zone** — protocol + truth on disk | — | [VERIFIED] |
| 0.01 | Cross | Cline Essence SKILL v1.1.0 in repo | `docs/cline_essence/SKILL.md` | [VERIFIED] |
| 0.02 | Cross | MASTER_PROTOCOL + SKILL_STACK + drift script | `cline-essence-drift-check.sh` exit 0 | [VERIFIED] |
| 0.03 | Cross | Tier 0 CONSTITUTION + registry YAML | Files exist | [VERIFIED] |
| 0.04 | Cross | Census + alignment; decimal-only roadmap | No `X.XX-A` IDs in this file | [VERIFIED] |
| 0.05 | Cross | All 3 modules `assembleDebug` | `registry` `build_all` command | [TESTED] |
| 0.06 | Cross | DEPLOYMENT_REALITY template ready | Matrix section exists | [VERIFIED] |
| | | | | |
| **0.25** | — | **MILESTONE: Device P0 gate** — all apps on parent phone | — | pending |
| 0.26 | Cross | Build + install 3 APKs | `./scripts/install-phone-apks.sh` | pending |
| 0.27 | Profile | Bottom navigation works | Phone cold start | pending |
| 0.28 | Profile | Cold start no crash | 3× launch | pending |
| 0.29 | Student | Correct launcher name | Settings → apps | pending |
| 0.30 | Student | Cold start no crash | 3× launch | pending |
| 0.31 | Teacher | Expedition board opens | Teacher APK | pending |
| 0.32 | Teacher | Create tile + persist after force-close | Room | pending |
| 0.33 | Teacher | Field guide save + mark complete | Tile detail | pending |
| 0.34 | Cross | Profile provider read smoke | Student import screen | pending |
| 0.35 | Cross | DEPLOYMENT matrix filled (Pass/Fail) | DEPLOYMENT_REALITY.md | pending |
| | | | | |
| **0.50** | — | **MILESTONE: Standalone P1** — each app useful alone | — | pending |
| 0.51 | Profile | Avatar layered preview (not emoji placeholder) | AvatarStudioScreen | pending |
| 0.52 | Profile | Avatar auto-save on change | Room history | pending |
| 0.53 | Profile | Artifact capture photo/audio | Memory capsule path | pending |
| 0.54 | Profile | Timeline from real events | TimelineRepository | pending |
| 0.55 | Student | Home + quests daily-usable | Field test | pending |
| 0.56 | Student | Forge import E2E verified | ImportForgeProfile | pending |
| 0.57 | Student | NPC/quest paths no crash | Smoke test | pending |
| 0.58 | Teacher | Tile drag reorder persist (P1c) | sortOrder | pending |
| 0.59 | Teacher | Tile detail all steward fields | EXPEDITION_BOARD_UX | pending |
| 0.60 | Cross | ContentProvider stable under P1 use | No regression | pending |
| | | | | |
| **0.75** | — | **MILESTONE: Contract layer** | — | pending |
| 0.76 | Cross | `IntentTile` Kotlin ↔ contract | Compile | pending |
| 0.77 | Cross | `MemoryEvent` Kotlin ↔ contract | Compile | pending |
| 0.78 | Cross | `QuestPayload` Kotlin ↔ contract | Compile | pending |
| 0.79 | Teacher | `secret-label-decoder.yaml` loads | Parser test | pending |
| 0.80 | Cross | Shared export package compiles | gradle | pending |
| 0.81 | Cross | Atlas IMPLEMENTED rows have proof | Authority map | pending |
| | | | | |
| **1.00** | — | **MILESTONE: Integration scaffold** | — | pending |
| 1.01 | Cross | Transport decision documented | sync-contract + OFFLINE doc | pending |
| 1.02 | Teacher | Send tile → Student stub payload | Handoff | pending |
| 1.03 | Profile | MemoryEvent write path (guarded) | Provider/Room | pending |
| | | | | |
| **1.50** | — | **MILESTONE: Loop 50%** | — | pending |
| 1.51 | Teacher | Create/send tile to Student | E2E stub | pending |
| 1.52 | Student | Quest appears from tile | UI | pending |
| 1.53 | Student | Complete → local event | Not CMOS yet | pending |
| | | | | |
| **2.00** | — | **MILESTONE: Loop proven** | — | pending |
| 2.01 | Cross | Tile → Quest → MemoryEvent → Profile → Teacher signal | reference-tiles 10-step | pending |
| 2.02 | Cross | Field notes + screenshots in DEPLOYMENT_REALITY | Steward sign-off | pending |
| | | | | |
| **2.50** | — | **MILESTONE: Profile depth** | — | pending |
| 2.51 | Profile | `AvatarConfig` + Godot hints in export | FORGEPROFILE_SPEC | pending |
| 2.52 | Profile | Avatar export JSON + share intent | File handoff | pending |
| 2.53 | Student | Full `/avatar` payload import | Provider | pending |
| 2.54 | Teacher | Forge Profile viewer complete | ForgeProfileViewerScreen | pending |
| | | | | |
| **3.00** | — | **MILESTONE: Student depth** | — | pending |
| 3.01 | Student | Hearthhome hub per shell spec | EXPERIENCE_SHELL_SPEC | pending |
| 3.02 | Student | Spark companion stateful | SPARK_COMPANION_UX | pending |
| 3.03 | Student | Identity lenses UI | IDENTITY_LENSES_UX | pending |
| | | | | |
| **3.50** | — | **MILESTONE: Teacher depth** | — | pending |
| 3.51 | Teacher | 7-day expedition overview | product-spec | pending |
| 3.52 | Teacher | Search + narrative wrapper accept | PLAN_GENERATION | pending |
| 3.53 | Teacher | Compass snapshot minimal | CHILDHOOD_COMPASS_ENGINE | pending |
| | | | | |
| **4.00** | — | **MILESTONE: CMOS bridge** | — | pending |
| 4.01 | Cross | Unified MemoryEvent pipeline | MEMORY_EVENT_CONTRACT | pending |
| 4.02 | Cross | TILE_EVENT_MAPPING in code | Emitters | pending |
| 4.03 | Cross | Child cannot delete validated CMOS | Boundary test | pending |
| | | | | |
| **4.50** | — | **MILESTONE: Chronicle tier** | — | pending |
| 4.51 | Profile | `.pfc` export smoke | Export UX | pending |
| 4.52 | Cross | Chronicle promotion + significance | Engines | pending |
| 4.53 | Cross | sync-contract implemented | shared/sync-contract.md | pending |
| 4.92 | Cross | *(reserved slot — insert step before 5.00 if needed)* | — | reserved |
| | | | | |
| **5.00** | — | **MILESTONE: Repo 100%** — in-repo north star complete | Census all green | pending |

**At `5.00`:** Forge Profile + Student + Teacher + cross-app loop `[VERIFIED]` per census; Godot/Sovereign binaries out of repo; import contracts `[VERIFIED]`.

---

## How to advance

1. Complete the row for step `N.NN`; mark `[VERIFIED]` or `[TESTED]` with proof.  
2. Set **Current step** to the next pending row (e.g. `0.05`).  
3. Update `registry/phoenix-forge-classroom.yaml` `current_step: "0.05"`.  
4. At each **MILESTONE** row, all steps since the previous milestone must be `[VERIFIED]` before claiming that milestone.

**Do not skip:** e.g. no `2.51` before `2.00` loop proven.

---

## Sub-roadmap index (detail only — not step IDs)

| Track | Document | Active from step |
|-------|----------|------------------|
| Profile | [01_FORGE_PROFILE_ROADMAP.md](01_FORGE_PROFILE_ROADMAP.md) | 0.27+ |
| Student | [02_STUDENT_EDITION_ROADMAP.md](02_STUDENT_EDITION_ROADMAP.md) | 0.29+ |
| Teacher | [03_TEACHER_EDITION_ROADMAP.md](03_TEACHER_EDITION_ROADMAP.md) | 0.31+ |
| Cross | [04_CROSS_APP_INTEGRATION_ROADMAP.md](04_CROSS_APP_INTEGRATION_ROADMAP.md) | 0.76+ |

---

*Timeline: decimal-only · cline-essence v1.1.0 · last numbering fix 2026-06-04*
