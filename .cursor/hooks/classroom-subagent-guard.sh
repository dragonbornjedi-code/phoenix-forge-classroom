#!/usr/bin/env bash
set -euo pipefail
ROOT="/var/lib/phoenix-ai/workspace/phoenix-forge-classroom"
if [[ "$(pwd)" != *phoenix-forge-classroom* ]] && [[ ! -f "$ROOT/registry/phoenix-forge-classroom.yaml" ]]; then
  echo '{"permission":"allow"}'
  exit 0
fi
python3 -c "import json; print(json.dumps({'permission':'allow','agent_message':'CLASSROOM SUBAGENT: Leaf under ao_classroom_bridge. Gradle root forge-profile only. No forbidden android/ trees. No World Godot edits. Verify before done. End with classroom_turn_score YAML.'}))"
