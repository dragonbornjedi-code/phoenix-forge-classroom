# Phoenix Forge Classroom — full autonomous loop (single turn)

## Mode detection

| Input | Mode |
|-------|------|
| `/phoenix-forge-classroom` only | **RECAP** |
| `/phoenix-forge-classroom <goal>` | **AUTONOMOUS** |

## AUTONOMOUS loop (same session)

### A0 — Bootstrap

```bash
cd /var/lib/phoenix-ai/workspace/phoenix-forge-classroom
./scripts/phoenix-forge-classroom-loop.sh "<GOAL>"
./scripts/classroom-context-pack.sh "<GOAL>"
```

Read: `classroom_session_latest.md`, `AUTONOMOUS_LOOP.md`, `SKILL_ROUTING.md`, `CLASSROOM_SCORING.md`

### A1 — Classify & route

1. Use route pack `leaf_agent_id`
2. Read Tier 0 for work_type (max 6 files per `classroom_context_budget.yaml`)
3. Load skills — Compose UI **must** read `frontend-design` skill
4. Sage work **must** read `SAGE_PERSONA.md`
5. Task subagent if large — inject leaf name in prompt

### A2 — Develop

- Module: `phoenix-forge-classroom-forge-profile/`
- Match existing Kotlin/Compose patterns
- Check `00_MASTER_ROADMAP.md` header for current step only — not full ledger

### A3 — Verify loop (max 8)

```bash
./scripts/phoenix-forge-classroom-verify-all.sh
```

| Result | Action |
|--------|--------|
| PASS | A4 |
| FAIL | Read `/tmp/pfc-verify-*.log`, fix, retry |

### A4 — Finish

```bash
./scripts/phoenix-forge-classroom-finish.sh "<GOAL>" "<summary>" "<why>"
```

### A5 — Score

```bash
./scripts/record-classroom-turn-score.sh \
  --srs 0.XX --fps 0.XX --pis 0.XX \
  --goal "<GOAL>" --leaf sub_* \
  --well "..." --improve "..." --lesson "..."
```

### A6 — Final delivery

```markdown
## Phoenix Forge Classroom — done

**Goal:** …
**Leaf:** sub_*
**Verify:** PASS | FAIL
**Step:** 0.68 → …

### Changed
- …

### Verify gates
- …

### classroom_turn_score
```yaml
srs: 0.XX
fps: 0.XX
pis: 0.XX
leaf_agent_id: sub_*
```
```
