#!/usr/bin/env bash
MARKER="/var/lib/phoenix-ai/workspace/phoenix-forge-classroom/.cursor/.classroom-loop-active"
[[ -f "$MARKER" ]] || { echo '{}'; exit 0; }
GOAL="$(grep '^goal=' "$MARKER" 2>/dev/null | cut -d= -f2- || echo '')"
python3 -c "import json; print(json.dumps({'followup_message': 'Classroom loop incomplete. Run phoenix-forge-classroom-verify-all.sh then phoenix-forge-classroom-finish.sh for: ${GOAL}'}))"
