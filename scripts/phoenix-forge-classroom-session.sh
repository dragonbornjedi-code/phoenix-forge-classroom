#!/usr/bin/env bash
# Phoenix Forge Classroom — session bootstrap for /phoenix-forge-classroom and Cursor hooks.
set -euo pipefail

REPO="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
HOME_REPO="${PHOENIX_FORGE_HOME:-/var/lib/phoenix-ai/workspace/phoenix-forge-home}"
OUT_MD="${REPO}/registry/handoffs/classroom_session_latest.md"
OUT_JSON="${REPO}/registry/handoffs/classroom_session_latest.json"
GRADLE="${REPO}/phoenix-forge-classroom-forge-profile"
RUN_VERIFY="${PFC_SESSION_VERIFY:-0}"

HANDOFF="${REPO}/docs/SESSION_HANDOFF_20260609.md"
CURRENT_STEP="$(grep '^current_step:' "${REPO}/registry/phoenix-forge-classroom.yaml" 2>/dev/null | awk '{print $2}' | tr -d '"' || echo unknown)"

git_summary() {
  if ! git -C "$REPO" rev-parse --git-dir >/dev/null 2>&1; then
    echo "not a git repo"
    return
  fi
  {
    echo "branch: $(git -C "$REPO" branch --show-current 2>/dev/null || echo unknown)"
    echo "head: $(git -C "$REPO" rev-parse --short HEAD 2>/dev/null || echo unknown)"
    git -C "$REPO" status --short 2>/dev/null | head -30
  }
}

verify_status() {
  local cache="${REPO}/registry/handoffs/classroom_verify_last.txt"
  if [[ "$RUN_VERIFY" == "1" ]]; then
    if bash "${REPO}/scripts/cline-essence-drift-check.sh" >/tmp/pfc-drift.log 2>&1; then
      echo "PASS drift $(date -Iseconds)"
      echo "PASS drift $(date -Iseconds)" >"$cache"
    else
      echo "FAIL drift — see /tmp/pfc-drift.log"
      echo "FAIL drift $(date -Iseconds)" >"$cache"
    fi
    return
  fi
  [[ -f "$cache" ]] && cat "$cache" || echo "UNKNOWN — run: just classroom-verify-all"
}

adb_status() {
  if command -v adb >/dev/null 2>&1; then
    adb devices 2>/dev/null | tail -n +2 | head -5 || echo "adb: no devices"
  else
    echo "adb: not installed"
  fi
}

mkdir -p "${REPO}/registry/handoffs"
TS="$(date -Iseconds)"

cat >"$OUT_MD" <<EOF
# Classroom session — $TS

**Lane:** phoenix-forge-classroom · **Step:** $CURRENT_STEP · **Agency:** Phoenix Forge Agency

## Route bootstrap
\`\`\`bash
./scripts/classroom-context-pack.sh "<goal>"
./scripts/classroom-delegation-verify.py
\`\`\`

## Verify cache
$(verify_status)

## adb
$(adb_status)

## Git
\`\`\`
$(git_summary)
\`\`\`

## Handoff excerpt
$(head -60 "$HANDOFF" 2>/dev/null || echo "missing handoff")

## Forbidden
- teacher-edition/android · student-edition/android build trees
- Adult profile → Student import
- PROTECTED writers on Student
- Godot edits (World lane) unless bridged
EOF

python3 - <<PY
import json, pathlib, datetime
repo = pathlib.Path("${REPO}")
md = (repo / "registry/handoffs/classroom_session_latest.md").read_text(encoding="utf-8", errors="replace")
data = {
  "ts": "${TS}",
  "lane": "classroom",
  "current_step": "${CURRENT_STEP}",
  "handoff": "${HANDOFF}",
  "verify": """$(verify_status)""".strip(),
}
(repo / "registry/handoffs/classroom_session_latest.json").write_text(json.dumps(data, indent=2), encoding="utf-8")
print(md[:12000])
PY
