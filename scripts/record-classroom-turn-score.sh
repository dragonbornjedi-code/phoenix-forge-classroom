#!/usr/bin/env bash
# Append SRS/FPS/PIS turn score for Classroom lane.
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
LOG="${ROOT}/registry/handoffs/classroom_turn_scores.jsonl"
TS="$(date -Iseconds)"

SRS="" FPS="" PIS="" GOAL="" LEAF="" ACTIVATED="" WELL="" IMPROVE="" LESSON=""
PARENT="ao_classroom_bridge" FALLBACK=0 VERIFY=0 DURATION=0 TOOLS="" SOURCE="cursor_hook"
while [[ $# -gt 0 ]]; do
  case "$1" in
    --srs) SRS="$2"; shift 2 ;;
    --fps) FPS="$2"; shift 2 ;;
    --pis) PIS="$2"; shift 2 ;;
    --goal) GOAL="$2"; shift 2 ;;
    --leaf) LEAF="$2"; shift 2 ;;
    --expected-leaf) LEAF="$2"; shift 2 ;;
    --activated-leaf) ACTIVATED="$2"; shift 2 ;;
    --fallback) FALLBACK=1; shift ;;
    --verify-pass) VERIFY=1; shift ;;
    --duration-ms) DURATION="$2"; shift 2 ;;
    --tool-calls) TOOLS="$2"; shift 2 ;;
    --well) WELL="$2"; shift 2 ;;
    --improve) IMPROVE="$2"; shift 2 ;;
    --lesson) LESSON="$2"; shift 2 ;;
    *) shift ;;
  esac
done

if [[ -z "$LEAF" && -n "$GOAL" ]]; then
  LEAF="$(python3 "${ROOT}/scripts/classroom-route-classify.py" "$GOAL" 2>/dev/null | python3 -c "import sys,json; print(json.load(sys.stdin).get('leaf_agent_id',''))" || true)"
fi
[[ -z "$ACTIVATED" && -n "$LEAF" && "$FALLBACK" -eq 0 ]] && ACTIVATED="$LEAF"

TELEMETRY="/var/lib/phoenix-ai/workspace/phoenix-forge-home/scripts/lib/agency_turn_telemetry.py"
python3 "$TELEMETRY" record classroom \
  --parent "$PARENT" --expected-leaf "$LEAF" --activated-leaf "$ACTIVATED" \
  --tool-calls "$TOOLS" --duration-ms "$DURATION" --prompt "$GOAL" --source "$SOURCE" \
  $([[ "$FALLBACK" -eq 1 ]] && echo --fallback) \
  $([[ "$VERIFY" -eq 1 ]] && echo --verify-pass) >/dev/null

mkdir -p "${ROOT}/registry/handoffs"
python3 -c "import json; print(json.dumps({
  'ts': '$TS', 'lane': 'classroom',
  'parent_agent': '$PARENT', 'expected_leaf': '''$LEAF''', 'activated_leaf': '''$ACTIVATED''',
  'fallback_to_parent': bool($FALLBACK), 'leaf_agent_id': '''$LEAF''',
  'srs': '$SRS', 'fps': '$FPS', 'pis': '$PIS',
  'goal': '''$GOAL''', 'tool_calls': [t for t in '''$TOOLS'''.split(',') if t.strip()],
  'verification_pass': bool($VERIFY), 'duration_ms': int('$DURATION' or 0),
  'well': '''$WELL''', 'improve': '''$IMPROVE''', 'lesson': '''$LESSON''',
}))" >>"$LOG"
echo "Recorded to $LOG"
