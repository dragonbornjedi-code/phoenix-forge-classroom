# Shared — Cross-App Data Contracts

**Forge Profile is the center.** This directory holds portable definitions that every app and future shell must speak — not game-engine fuel from a pre-pivot design.

Ezra’s childhood data lives in **Forge Profile** (`forge-profile-core` Room + export). Student Edition and Teacher Edition are **role-based views** on that record. A future **Godot 3D reflection world** imports the same profile bundle (identity, avatar, chronicle refs) — see [docs/GODOT_MIGRATION_STRATEGY.md](../docs/GODOT_MIGRATION_STRATEGY.md).

---

## What lives here today

| File | Purpose | Consumed by |
|------|---------|-------------|
| [sync-contract.md](sync-contract.md) | Teacher ↔ Student local exchange (missions, completion, link model) | Roadmap P2 — not fully implemented |
| [schemas/PCAS_DB_SCHEMA.sql](schemas/PCAS_DB_SCHEMA.sql) | Long-horizon PCAS tables (spec) | Future CMOS runtime — not wired in APKs yet |
| [schemas/forge_profile_push.schema.json](schemas/forge_profile_push.schema.json) | Manual steward push + avatar v2 | Forge Profile writer, Forge World reader |

**Authoritative contracts (markdown):** [docs/contracts/](../docs/contracts/) — IntentTile, MemoryEvent, Curriculum OS, reference tiles.

**Authoritative identity + avatar:** [docs/FORGEPROFILE_SPEC.md](../docs/FORGEPROFILE_SPEC.md) — including **Avatar Studio** target schema and Godot export mapping.

---

## Forge Profile export surfaces (implemented path)

| Surface | Authority | Today |
|---------|-----------|--------|
| ContentProvider | `ProfileContract.kt` | `/profile`, `/avatar`, `/timeline` read |
| JSON export DTOs | `ProfileExportReader.kt` | Partial — extend for full `AvatarConfig` |
| Share intent / `.pfc` bundle | MEMORY_PERSISTENCE_STRATEGY | P1 roadmap |

**Avatar** is not a cosmetic extra — it is Ezra’s **visual identity anchor** across apps:

```text
Avatar Studio (Forge Profile app)
  → Room avatar history + timeline event
  → ContentProvider /avatar (Student reads summary today)
  → JSON AvatarConfig export (target: Student + Godot mesh mapping)
  → Chronicle + CDNS signals (future: missions reflect look choices)
```

Current Kotlin model (`Avatar`: hair, eyes, skin, clothing, version) is **P0**. Target `AvatarConfig` in FORGEPROFILE_SPEC is **P1–P3** (shards, accessories, Godot proportions).

---

## Role-based profile views (same child, different lens)

| App / shell | Reads from profile | Writes |
|-------------|-------------------|--------|
| **Forge Profile** | Full CMOS direction | Steward: identity, avatar, artifacts, timeline |
| **Student Edition** | Subset: visuals, quests, world state | MemoryEvents, completion (not CMOS delete) |
| **Teacher Edition** | Subset + coaching overlay | IntentTiles, expedition, evidence (local today) |
| **Godot (future)** | Full `AvatarConfig` + world IDs | Read-mostly reflection; emits MemoryEvents |

All apps remain **offline-first** and **standalone-installable**; sync is optional file/provider exchange, never cloud-required.

---

## Godot import contract (summary)

Godot does not own childhood data. It **imports** a Forge Profile export:

1. `forge_profile.json` — identity, stage, threads (when implemented)
2. `avatar_config.json` — layered appearance + `godotMeshHints` (bone scale, attachment slots)
3. `chronicle_index.json` — episode refs for Memory Mirror / museum placement
4. Universal IDs → `res://` scene paths ([GODOT_MIGRATION_STRATEGY.md](../docs/GODOT_MIGRATION_STRATEGY.md))

---

## Roadmaps that own this directory

- [docs/roadmaps/01_FORGE_PROFILE_ROADMAP.md](../docs/roadmaps/01_FORGE_PROFILE_ROADMAP.md) — Avatar Studio expansion, export
- [docs/roadmaps/04_CROSS_APP_INTEGRATION_ROADMAP.md](../docs/roadmaps/04_CROSS_APP_INTEGRATION_ROADMAP.md) — Tile → Quest → MemoryEvent
- [docs/DEVELOPMENT_RULES.md](../docs/DEVELOPMENT_RULES.md) — extend, do not redesign

---

## Deferred: Universal Engines (UME / USE)

Pre-pivot **Universal Matching Engine** and **Universal Sequencing Engine** JSON packs are **not** the current spine. When Student mission minigames return, their schemas belong under `registry/` (content packs), not as the identity contract for the OS. Until then, do not treat old UME/USE samples as source of truth.
