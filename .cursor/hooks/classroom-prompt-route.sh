#!/usr/bin/env bash
set -euo pipefail
INPUT="$(cat)"
ROOT="/var/lib/phoenix-ai/workspace/phoenix-forge-classroom"
PROMPT="$(echo "$INPUT" | python3 -c "import sys,json; print(json.load(sys.stdin).get('prompt',''))" 2>/dev/null || echo "")"
[[ "$PROMPT" == /phoenix-forge-classroom* ]] || { echo '{"continue":true}'; exit 0; }

REST="${PROMPT#/phoenix-forge-classroom}"
REST="${REST# }"
MODE="RECAP"
[[ -n "$REST" ]] && MODE="AUTONOMOUS"

"$ROOT/scripts/phoenix-forge-classroom-session.sh" >/dev/null 2>&1 || true
PACK="$("$ROOT/scripts/classroom-context-pack.sh" "${REST:-classroom step 0.68 status}" 2>/dev/null || true)"
SESSION="$(head -80 "$ROOT/registry/handoffs/classroom_session_latest.md" 2>/dev/null || true)"

python3 - <<PY
import json
mode, rest, pack, session = "${MODE}", """${REST}""", """${PACK}""", """${SESSION}"""
if mode == "AUTONOMOUS":
    mandate = f"""AUTONOMOUS CLASSROOM LOOP — single turn. Goal: {rest or '(infer)'}

MANDATORY:
1. Use ROUTE PACK — leaf agent, NOT ao_classroom_bridge alone
2. CONTEXT_BUDGET.md — max 2 skills, 6 tier-0 files
3. Gradle: phoenix-forge-classroom-forge-profile ONLY
4. Develop → phoenix-forge-classroom-verify-all until PASS (max 8) → finish.sh
5. classroom_turn_score YAML with leaf_agent_id
6. classroom-delegation-verify.py if routing changed

FORBIDDEN: teacher-edition/android, student-edition/android, World Godot edits, full roadmap load
"""
else:
    mandate = "RECAP ONLY. Step 0.68 status, verify, next 0.69. Wait for /phoenix-forge-classroom <goal>"

print(json.dumps({"continue": True, "updated_prompt": f"[Phoenix Forge Classroom — {mode}]\\n{mandate}\\n\\n--- ROUTE PACK ---\\n{pack}\\n\\n--- SESSION ---\\n{session}\\n"}))
PY
