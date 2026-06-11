# Classroom turn scoring — SRS / FPS / PIS

Ported from Sovereign Deck scoring for lane parity.

## Dimensions

| Code | Name | Classroom meaning |
|------|------|-------------------|
| **SRS** | Semantic Routing Score | Correct `leaf_agent_id` + work_type for goal |
| **FPS** | Functional Pass Score | verify-all gates PASS; phone tests if claimed |
| **PIS** | Process Integrity Score | Context budget obeyed; forbidden paths avoided |

## Slots (self_eval)

- **director** — Did parent delegate to leaf?
- **executor** — Minimal correct diff in right module?
- **guardrail** — Constitution obeyed (scopes, PROTECTED, adult/child)?
- **self_eval** — Lesson captured for next turn?

## Penalties (PIS)

| Violation | Δ |
|-----------|---|
| Loaded full taxonomy or full roadmap | −0.40 |
| Built forbidden android/ trees | −0.50 |
| Edited World Godot without bridge | −0.30 |
| Claimed TESTED without verify PASS | −0.35 |
| Parent-only routing when leaf exists | −0.25 |

## Record

```bash
./scripts/record-classroom-turn-score.sh \
  --srs 0.92 --fps 1.0 --pis 0.95 \
  --goal "..." --leaf sub_sync_events \
  --well "..." --improve "..." --lesson "..."
```

## YAML block (required in final report)

```yaml
classroom_turn_score:
  srs: 0.92
  fps: 1.0
  pis: 0.95
  leaf_agent_id: sub_sync_events
  parent_agent_id: ao_classroom_bridge
  roadmap_step: "0.69"
```
