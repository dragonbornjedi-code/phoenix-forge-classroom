#!/usr/bin/env bash
# End of autonomous Classroom loop — verify all, update docs.
set -uo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
GOAL="${1:-}"
SUMMARY="${2:-}"
WHY="${3:-}"

VERIFY_FLAG="--verify-fail"
if bash "${ROOT}/scripts/phoenix-forge-classroom-verify-all.sh"; then
  VERIFY_FLAG="--verify-pass"
fi

bash "${ROOT}/scripts/update-classroom-handoff.sh" \
  --goal "$GOAL" \
  --summary "$SUMMARY" \
  --why "$WHY" \
  "$VERIFY_FLAG"

rm -f "${ROOT}/.cursor/.classroom-loop-active" 2>/dev/null || true

python3 - <<PY
import json, pathlib
root = pathlib.Path("${ROOT}")
verify_path = root / "registry/handoffs/classroom_verify_last.json"
verify = json.loads(verify_path.read_text()) if verify_path.is_file() else {"overall": "UNKNOWN", "gates": []}
print(json.dumps({
  "status": verify.get("overall", "UNKNOWN"),
  "gates": verify.get("gates", []),
  "run_md": str(root / "registry/handoffs/classroom_run_latest.md"),
  "handoff_updated": True,
}, indent=2))
PY
