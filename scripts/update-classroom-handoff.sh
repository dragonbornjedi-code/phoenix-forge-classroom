#!/usr/bin/env bash
# Append autonomous run to Classroom handoff logs.
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
GOAL="" SUMMARY="" WHY="" VERIFY="UNKNOWN"
while [[ $# -gt 0 ]]; do
  case "$1" in
    --goal) GOAL="$2"; shift 2 ;;
    --summary) SUMMARY="$2"; shift 2 ;;
    --why) WHY="$2"; shift 2 ;;
    --verify-pass) VERIFY="PASS"; shift ;;
    --verify-fail) VERIFY="FAIL"; shift ;;
    *) shift ;;
  esac
done

TS="$(date -Iseconds)"
RUNS="${ROOT}/registry/handoffs/classroom_autonomous_runs.jsonl"
LATEST="${ROOT}/registry/handoffs/classroom_run_latest.md"
HANDOFF="${ROOT}/docs/SESSION_HANDOFF_20260609.md"

mkdir -p "${ROOT}/registry/handoffs"
python3 -c "import json; print(json.dumps({'ts':'$TS','goal':'''$GOAL''','summary':'''$SUMMARY''','why':'''$WHY''','verify':'$VERIFY'}))" >>"$RUNS"

cat >"$LATEST" <<EOF
# Classroom autonomous run — $TS

**Verify:** $VERIFY  
**Goal:** $GOAL

## Summary
$SUMMARY

## Why
$WHY
EOF

if [[ -f "$HANDOFF" ]]; then
  {
    echo ""
    echo "---"
    echo "## Autonomous run $TS"
    echo "- **Goal:** $GOAL"
    echo "- **Verify:** $VERIFY"
    echo "- $SUMMARY"
  } >>"$HANDOFF"
fi

"${ROOT}/scripts/phoenix-forge-classroom-session.sh" >/dev/null
