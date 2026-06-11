#!/usr/bin/env bash
# Emit minimal context pack for a Classroom goal — avoids loading full agency taxonomy.
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
GOAL="${*:-}"
[[ -z "$GOAL" ]] && GOAL="$(cat)"

ROUTE_JSON="$(python3 "${ROOT}/scripts/classroom-route-classify.py" "$GOAL")"
WORK_TYPE="$(echo "$ROUTE_JSON" | python3 -c "import sys,json; print(json.load(sys.stdin)['work_type'])")"
LEAF="$(echo "$ROUTE_JSON" | python3 -c "import sys,json; print(json.load(sys.stdin)['leaf_agent_id'])")"
STEP="$(grep '^current_step:' "${ROOT}/registry/phoenix-forge-classroom.yaml" 2>/dev/null | awk '{print $2}' | tr -d '"')"

cat <<EOF
# Classroom context pack (minimal — do NOT load full taxonomy)
goal: $GOAL
work_type: $WORK_TYPE
parent: ao_classroom_bridge
leaf: $LEAF
roadmap_step: ${STEP:-unknown}

## Tier 0 (read these only)
- ${ROOT}/AGENTS.md
- ${ROOT}/registry/classroom_agent_manifest.yaml
- ${ROOT}/docs/PHOENIX_FORGE_MASTER_SPEC.md (if sync/events/profile)
- ${ROOT}/docs/SESSION_HANDOFF_20260609.md

## Skills (max 2 — first 400 lines each)
- ${ROOT}/docs/cline_essence/SKILL.md
- ~/.phoenix-forge-agent-skills/frontend-design/SKILL.md (if compose_ui / sage / expedition)
- ~/.phoenix-forge-agent-skills/android-kotlin-development/SKILL.md (if kotlin/sync)

## Gradle root (only module for leaf)
- phoenix-forge-classroom-forge-profile/ (NEVER teacher-edition/android or student-edition/android)

## Cross-repo (READ ONLY — World lane owns Godot)
- phoenix-forge-world/docs/DIGITAL_HOME_SYNC_HANDOFF.md
- shared/schemas/public_state.schema.json

## Verify gates for this route
$(echo "$ROUTE_JSON" | python3 -c "import sys,json; print('\\n'.join('- '+v for v in json.load(sys.stdin).get('verify_gates',[])))")

## Route JSON
\`\`\`json
$ROUTE_JSON
\`\`\`
EOF
