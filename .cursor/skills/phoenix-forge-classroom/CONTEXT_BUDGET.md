# Context budget — mandatory every Classroom turn

## Per-turn limits (`registry/classroom_context_budget.yaml`)

| Limit | Value |
|-------|------:|
| Tier-0 files | 6 max |
| Skill files | 2 max |
| Lines per skill | 400 max |
| Handoff excerpt | 80 lines |
| Cross-repo reads | 3 max |
| Task subagents | 2 max |

## Bootstrap

```bash
./scripts/classroom-context-pack.sh "<goal>"
```

## Mandatory by work type

| Work | Extra reads |
|------|-------------|
| compose_ui | `frontend-design` SKILL + `classroom-compose-ui.mdc` |
| sage_advisor | `SAGE_PERSONA.md` + `sage-persona-content.mdc` |
| sync_events | `PHOENIX_FORGE_MASTER_SPEC.md` event sections |
| cross_app | `shared/schemas/public_state.schema.json` |

## Never in same turn

- Full `classroom_agency_taxonomy.yaml`
- Full `00_MASTER_ROADMAP.md` ledger
- `PHOENIX_FORGE_SYSTEM_ATLAS.md`
- Bulk `phoenix-forge-world/` Godot trees

## Cross-repo

World GDScript: **read-only** unless Josh bridges.

## PIS penalty

Forbidden bulk load → PIS −0.40
