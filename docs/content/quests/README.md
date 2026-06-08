# Gold-standard quest content — Classroom

Canonical **Sage quest schema** examples for Teacher Edition skills, Intent Tile handoff, and Student `QuestPayload` alignment.

## File

| File | Quests | Realms covered |
|------|--------|----------------|
| [2026-02-gold-standard.json](2026-02-gold-standard.json) | 12 | thinking-tower, heart-garden, adventure-grounds, creation-studio, life-lodge, sensory-sanctuary |

## Field contract (per quest)

```text
id, realm, subcategory, title, story
category_display, difficulty, xp, duration_minutes, materials
deep_objectives { executive_function, social_emotional, sensory, academic }
steps[] { instruction, timer, sensory_load, ef_demand }
scaffolding, extension, celebration, parent_notes, real_world_connection
```

## Ownership split

| Repo | Role |
|------|------|
| **phoenix-forge-classroom** (this path) | Schema reference, Sage skills, Teacher→Student contracts |
| **phoenix-forge-world** `data/quests/2026-02-gold-standard.json` | Runtime data copy for Godot / golden scrape / car quest stack |

## Persona guardrail

- **Use:** surface/deep layer shape, step ratings, parent_notes, neurodivergent scaffolding
- **Rewrite:** `story` and `celebration` — legacy "Owl Sage" voice is not canonical Sage
- **Canonical persona:** `phoenix-forge-classroom-teacher-edition/docs/SAGE_PERSONA.md`

## Architecture

Classification buckets: `sovereign-deck/docs/GOLDEN_HIERARCHY.md` § Phoenix Forge Classroom.
