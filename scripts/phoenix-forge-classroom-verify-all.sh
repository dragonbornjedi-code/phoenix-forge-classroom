#!/usr/bin/env bash
# Run all Classroom verify gates; exit 0 only if ALL pass.
set -uo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
GRADLE="${ROOT}/phoenix-forge-classroom-forge-profile"
cd "$ROOT"
OUT_JSON="${ROOT}/registry/handoffs/classroom_verify_last.json"
OUT_TXT="${ROOT}/registry/handoffs/classroom_verify_last.txt"
GATES_NDJSON="/tmp/pfc-gates.ndjson"
TS="$(date -Iseconds)"
FAIL=0

: >"$GATES_NDJSON"

run_gate() {
  local name="$1"
  shift
  local log="/tmp/pfc-verify-${name//\//-}.log"
  local status="PASS"
  if "$@" >"$log" 2>&1; then
    echo "[PASS] $name"
  else
    status="FAIL"
    FAIL=1
    echo "[FAIL] $name (see $log)"
    tail -20 "$log" >&2 || true
  fi
  python3 -c "import json; print(json.dumps({'name': '''$name''', 'status': '''$status'''}))" >>"$GATES_NDJSON"
}

export JAVA_HOME="${JAVA_HOME:-}"
if [[ -z "$JAVA_HOME" && -x /home/joshuar/.local/jdk/bin/java ]]; then
  export JAVA_HOME=/home/joshuar/.local/jdk
elif [[ -z "$JAVA_HOME" && -d /usr/lib/jvm/java-21-openjdk-amd64 ]]; then
  export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
fi
export PATH="${JAVA_HOME:+$JAVA_HOME/bin:}$PATH"

GW() {
  if [[ -x "${GRADLE}/gradlew" ]]; then
    "${GRADLE}/gradlew" -p "$GRADLE" "$@"
  else
    return 1
  fi
}

echo "=== phoenix-forge-classroom-verify-all @ $TS ==="

run_gate "cline-essence-drift" bash "$ROOT/scripts/cline-essence-drift-check.sh"
run_gate "classroom-delegation-corpus" python3 "$ROOT/scripts/classroom-delegation-verify.py"
run_gate "gradle-forge-profile-core-test" GW :forge-profile-core:test
run_gate "gradle-student-unit-test" GW :student-app:testDebugUnitTest
run_gate "gradle-teacher-unit-test" GW :teacher-app:testDebugUnitTest
run_gate "gradle-assemble-forge-profile" GW :forge-profile-app:assembleDebug
run_gate "gradle-assemble-student" GW :student-app:assembleDebug
run_gate "gradle-assemble-teacher" GW :teacher-app:assembleDebug

python3 - <<PY
import json, pathlib, datetime
gates = [json.loads(l) for l in pathlib.Path("$GATES_NDJSON").read_text().splitlines() if l.strip()]
overall = "PASS" if all(g["status"] == "PASS" for g in gates) else "FAIL"
report = {"ts": "$TS", "overall": overall, "gates": gates}
pathlib.Path("$OUT_JSON").write_text(json.dumps(report, indent=2))
pathlib.Path("$OUT_TXT").write_text(f"{overall} classroom-verify-all $TS\n")
print(json.dumps(report, indent=2))
PY

exit "$FAIL"
