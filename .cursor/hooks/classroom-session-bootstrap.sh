#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/../.." && pwd)"
if [[ ! -f "$ROOT/registry/phoenix-forge-classroom.yaml" ]]; then
  echo '{}'
  exit 0
fi
"$ROOT/scripts/phoenix-forge-classroom-session.sh" >/dev/null 2>&1 || true
python3 -c "import json; print(json.dumps({'continue': True, 'additional_context': 'Classroom lane. Use /phoenix-forge-classroom. Context budget: registry/classroom_context_budget.yaml. Gradle root: phoenix-forge-classroom-forge-profile only.'}))"
