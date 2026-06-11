#!/usr/bin/env bash
# Mark autonomous Classroom loop active + bootstrap.
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
MARKER="${ROOT}/.cursor/.classroom-loop-active"
GOAL="${*:-}"

mkdir -p "${ROOT}/.cursor"
{
  echo "goal=${GOAL}"
  echo "started=$(date -Iseconds)"
} >"$MARKER"

"${ROOT}/scripts/phoenix-forge-classroom-session.sh" >/dev/null
/var/lib/phoenix-ai/workspace/phoenix-forge-home/scripts/agency-preflight.sh --lane classroom --skip-jarvis-scan >/dev/null 2>&1 || true

python3 - <<PY
import json, pathlib
root = pathlib.Path("${ROOT}")
session = root / "registry/handoffs/classroom_session_latest.json"
data = json.loads(session.read_text()) if session.is_file() else {}
print(json.dumps({
  "mode": "autonomous_loop",
  "goal": """${GOAL}""",
  "session": data,
  "instructions": [
    "Read .cursor/skills/phoenix-forge-classroom/AUTONOMOUS_LOOP.md",
    "Run classroom-context-pack.sh; activate LEAF not ao_classroom_bridge alone",
    "Implement; run phoenix-forge-classroom-verify-all.sh after each fix cycle",
    "Repeat until PASS or max 8 iterations",
    "Run phoenix-forge-classroom-finish.sh",
    "Deliver final report with classroom_turn_score YAML",
  ],
  "max_iterations": 8,
}, indent=2))
PY
