# Forge Profile — Roadmap (four paths)

**Product name:** Forge Profile (never “profile app” only in UX)  
**Code:** `forge-profile-core` + `forge-profile-app`  
**Package:** `com.phoenixforge.profile`

**Census:** [REPOSITORY_CENSUS_AND_CONNECTIONS.md](../REPOSITORY_CENSUS_AND_CONNECTIONS.md) — Layer 2

---

## Four paths

| Path | Goal | Exit criteria |
|------|------|---------------|
| **P0 Stabilize** | Trustworthy steward app on parent phone | Nav works, data persists, no crash on cold start |
| **P1 Core** | Daily identity + memory without CMOS completeness | Capture artifact, edit identity, timeline grows |
| **P2 Integrate** | Spine participant | Accept MemoryEvents from Student; expose read for Teacher |
| **P3 Vision** | CMOS / 2046 / Ezra-longevity | bonds, threads, .pfc export, chronicle promotion, Spark tier rules |

---

## P0 Stabilize

| Task | Doc / code | Status |
|------|------------|--------|
| Bottom navigation | `ProfileBottomBar.kt`, `MainActivity.kt` | Done 2026-06-04 |
| Reinstall on phone | [DEPLOYMENT_REALITY.md](../DEPLOYMENT_REALITY.md) | User verify |
| Bootstrap profile for Ezra | `ProfileBootstrap.kt` | Built — verify |
| Provider readable by Student | `ProfileContentProvider.kt` | Built — test import |

**Attached docs**

| Document | Purpose | Doc % | Action |
|----------|---------|-------|--------|
| [FORGEPROFILE_SPEC.md](../FORGEPROFILE_SPEC.md) | Target schema | 85% | **Evolve** after P1 capture |
| [DEPLOYMENT_REALITY.md](../DEPLOYMENT_REALITY.md) | Install | 95% | Fill matrix |

---

## P1 Core

| Task | Attached doc | Code % | Needs |
|------|--------------|--------|-------|
| Artifact capture (camera/audio) | FORGEPROFILE_SPEC, EVENT_CAPTURE_PROTOCOL | 10% | **Dev** |
| Memory capsule button | `MemoryCapsuleScreen.kt` | 0% | **Dev** |
| Export .pfc smoke test | MEMORY_PERSISTENCE_STRATEGY | 15% | **Dev** |
| Timeline from real events | MEMORY_ENGINE_ARCHITECTURE | 40% | **Improve** |
| Steward gate clarity | STUDENT_TEACHER_BOUNDARY | 50% | **Update** copy |

**100% doc / 0% code (plan only until P2)**

- [IDENTITY_FORMATION_ENGINE.md](../IDENTITY_FORMATION_ENGINE.md) — threads  
- [FAMILY_LEGACY_SYSTEM.md](../FAMILY_LEGACY_SYSTEM.md)  
- [AGE_25_BACKCAST_DESIGN.md](../AGE_25_BACKCAST_DESIGN.md)

---

## P2 Integrate

| Task | Attached doc | Connects |
|------|--------------|----------|
| MemoryEvent ingest API | MEMORY_EVENT_CONTRACT | Student → Profile |
| Promote buffer → timeline | CURRICULUM_RUNTIME_FLOW § chronicle | Teacher approval later |
| Provider write guardrails | STUDENT_TEACHER_BOUNDARY | Child cannot write CMOS |

**Cross-track:** [04_CROSS_APP_INTEGRATION_ROADMAP.md](04_CROSS_APP_INTEGRATION_ROADMAP.md)

---

## P3 Vision

| Task | Attached doc |
|------|--------------|
| bonds graph | FORGEPROFILE_SPEC |
| identity threads + parent confirm | IDENTITY_FORMATION_ENGINE |
| Artifact registry + checksums | MEMORY_ENGINE_ARCHITECTURE |
| Spark maturation tiers | PCAS_ARCHITECTURE |
| Handover bundle | CHILDHOOD_TIME_CAPSULE_SPEC |

---

## Code map (what exists)

| Component | Path | Path stage |
|-----------|------|------------|
| Room DB | `forge-profile-core/.../ProfileDatabase.kt` | P0 |
| ContentProvider | `ProfileContentProvider.kt` | P0 |
| Export DTOs | `ProfileExportReader.kt` | P1 |
| Dashboard / Studio / Timeline | `forge-profile-app/ui/*` | P0–P1 |

---

## Dependencies

- **Blocks Student P2:** provider must stay stable  
- **Blocks Teacher P2:** profile viewer for Ezra metadata  
- **Blocked by:** Student MemoryEvent emitter (Cross-app P2)
