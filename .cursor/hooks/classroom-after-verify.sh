#!/usr/bin/env bash
set -euo pipefail
INPUT="$(cat)"
CMD="$(echo "$INPUT" | python3 -c "import sys,json; print(json.load(sys.stdin).get('command',''))" 2>/dev/null || echo "")"
ROOT="/var/lib/phoenix-ai/workspace/phoenix-forge-classroom"
CACHE="$ROOT/registry/handoffs/classroom_verify_last.txt"
[[ "$CMD" == *verify* ]] || [[ "$CMD" == *gradlew* ]] || { echo '{}'; exit 0; }

LIVE="/var/lib/phoenix-ai/workspace/phoenix-forge-home/scripts/lib/agency_live_turn.py"
python3 "$LIVE" verify --command "$CMD" >/dev/null 2>&1 || true
EXIT="$(echo "$INPUT" | python3 -c "import sys,json; print(json.load(sys.stdin).get('exit_code', 1))" 2>/dev/null || echo 1)"
TS="$(date -Iseconds)"
if [[ "$EXIT" == "0" ]]; then
  echo "PASS shell-verify $TS — $CMD" >"$CACHE"
else
  echo "FAIL shell-verify $TS — $CMD" >"$CACHE"
fi
python3 -c "import json; print(json.dumps({'additional_context': 'Classroom verify shell exited ${EXIT}'}))"
