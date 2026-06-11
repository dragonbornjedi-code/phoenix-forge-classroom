# Quest pipeline — shared contracts

Teacher → Student → Godot quest spine. **File handoff only** until loop 2.01–2.07 proven.

## Daily stack handoff (step 1.02 — live path)

**Transport (authoritative):** LESSON manifest JSON on the Syncthing tree:

```text
/sdcard/PhoenixForge/sync/profiles/{studentUid}/manifests/lesson_manifest_{yyyy-MM-dd}.json
```

| Writer | Reader | Scope |
|--------|--------|-------|
| Teacher Edition — `ManifestWriter.kt` | Student Edition — `ManifestReader.kt` | LESSON |

Steward smoke + adb fallback: **[docs/HANDOFF_1_02.md](../../docs/HANDOFF_1_02.md)**.

Stub manifest: one `days[]` entry per expedition tile (tile title → `narrativeTitle`, stub `quests[]` id). Full gold-standard export remains Layer 4 below.

**PUBLIC events (step 1.03+ / P2):** append-only JSON under:

```text
/sdcard/PhoenixForge/sync/profiles/{studentUid}/events/EVT_{deviceId}_{epochMs}_{seq}.json
```

Student `EventWriter.kt` emits `QUEST_STARTED` / `QUEST_COMPLETED` with `logicalClock` (no `syncVersion`). Forge Profile ingests in step `1.04`.

---

## Layer model

```text
LAYER 1 — Activity Core (quest_core.schema.json)
  steps[], deep_objectives, scaffolding, extension, sensory_load, ef_demand
LAYER 2 — Story Template (story_template.schema.json)
  story skin, celebration skin, voice_id — swap without changing steps
LAYER 3 — Teacher IntentTile (INTENT_TILE_CONTRACT.md)
  expedition metadata, parent_notes, real_world_connection
LAYER 4 — Runtime binding (quest_export.schema.json)
  Student QuestPayload + Godot data/quests/*.json + voice line_ids
```

## Authority

| Artifact | Canonical path |
|----------|----------------|
| Gold-standard quests (12) | `docs/content/quests/2026-02-gold-standard.json` |
| Godot runtime copy | `phoenix-forge-world/data/quests/2026-02-gold-standard.json` |
| Completion log schema | `shared/ledger/schemas/quest_log_event.schema.json` |
| Voice actors | `shared/voice/voice_actor_manifest.json` |

## Realm / subcategory wire format

Use `CurriculumDomainId.name` + kebab `realm_id` from `CROSS_SURFACE_ALIGNMENT.md`.

| realm_id | teacher_domain_id | Example subcategories |
|----------|-------------------|----------------------|
| thinking-tower | COGNITIVE_ACADEMIC | literacy, numeracy-pattern, science-discovery |
| heart-garden | EMOTIONAL_REGULATION | feelings, friendship, inner-peace |
| adventure-grounds | PHYSICAL_MASTERY | big-moves, tiny-moves |
| creation-studio | CREATIVE_CURIOSITY | art-making, sound-story |
| life-lodge | PRACTICAL_LIFE | home-skills, world-skills |
| civic-plaza | ETHICS_CIVIC | fair-split, helper-mission |
| forge_hearth | *(hub)* | daily-check-in |

**Do not** use franchise guardian names. Use `steward_{domain}` voice_ids. Ignavarr = narrator only.

## Sage workflow

1. Pick activity core (curriculum subdomain or existing gold-standard quest)
2. Apply story template: `python3 shared/quests/templates/merge_story_template.py --core X --template discovery_mystery`
3. Validate: `python3 scripts/quest-ledger-tool.py validate <quest.json>`
4. Import ledger: `python3 scripts/quest-ledger-tool.py import-gold-standard <json>`
5. Export handoff: `quest_export.json` → Student + Godot `data/quests/`

## Tools

```bash
python3 scripts/quest-ledger-tool.py import-gold-standard docs/content/quests/2026-02-gold-standard.json
python3 scripts/quest-ledger-tool.py export-teacher-pack --realm thinking-tower
python3 scripts/quest-ledger-tool.py append-completion event.json
python3 scripts/quest-ledger-tool.py export-ndjson quest_log.ndjson
python3 scripts/quest-ledger-tool.py validate docs/content/quests/2026-02-gold-standard.json
```

## Persona guardrail

Rewrite `story` and `celebration` per `SAGE_PERSONA.md`. Legacy Owl Sage voice is not canonical.
