# Registry

Structured **content packs** for future Student missions (lessons, games, skill atoms) — not identity data.

**Identity, avatar, and chronicle** live in **Forge Profile** only ([docs/FORGEPROFILE_SPEC.md](../docs/FORGEPROFILE_SPEC.md)). Cross-app spine contracts live in [docs/contracts/](../docs/contracts/) and [shared/](../shared/).

## Planned contents

| Registry area | Examples | Roadmap |
|---------------|----------|---------|
| Lesson packs | starter-lessons-pack-01 derivatives | Teacher P3 |
| Skill atoms | curriculum OS atom IDs | CURRICULUM_OS_SCHEMA |
| Game engines | UME/USE-style packs (deferred) | Student P3+ |

Pre-pivot Universal Matching / Sequencing Engine JSON samples are **not** current source of truth — see [shared/README.md](../shared/README.md) § Deferred.

## When implementing

- Pack IDs must reference `docs/contracts/CURRICULUM_OS_SCHEMA.md` capability/skill atoms.
- Missions emit **MemoryEvents** per [MEMORY_EVENT_CONTRACT.md](../docs/contracts/MEMORY_EVENT_CONTRACT.md).
- Child-facing copy pulls avatar unlock flags from Forge Profile export — never a second avatar store.
