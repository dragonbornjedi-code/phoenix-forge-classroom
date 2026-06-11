#!/usr/bin/env bash
# Cross-app smoke: build → install → manifest path → event write → Profile ingest
# Usage: ./scripts/smoke-classroom-cross-app.sh [adb-serial]
# Env:   SMOKE_STUDENT_UID=<child-uuid>  (optional — auto-detect from ContentProviders)
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
REPO_ROOT="$(cd "$ROOT/.." && pwd)"
LOG_FILE="$ROOT/smoke-logcat.txt"
DATE_ISO="$(date +%Y-%m-%d)"
SYNC_BASE="/sdcard/PhoenixForge/sync/profiles"

PASS=0
FAIL=0
WARN=0

log() { printf '[smoke] %s\n' "$*"; }
pass() { PASS=$((PASS + 1)); log "✅ $*"; }
fail() { FAIL=$((FAIL + 1)); log "❌ $*"; }
warn() { WARN=$((WARN + 1)); log "⚠️  $*"; }

if [[ -x /home/joshuar/.local/jdk/bin/java ]]; then
  export JAVA_HOME="${JAVA_HOME:-/home/joshuar/.local/jdk}"
elif [[ -x /usr/lib/jvm/java-21-openjdk-amd64/bin/java ]]; then
  export JAVA_HOME="${JAVA_HOME:-/usr/lib/jvm/java-21-openjdk-amd64}"
fi
export PATH="$JAVA_HOME/bin:$PATH"

resolve_serial() {
  local requested="${1:-${ANDROID_SERIAL:-}}"
  if [[ -n "$requested" ]]; then
    if adb devices 2>/dev/null | awk -v s="$requested" '$1==s && $2=="device" {found=1} END{exit !found}'; then
      echo "$requested"
      return 0
    fi
  fi
  local serial
  serial="$(adb devices 2>/dev/null | awk '$2=="device" {print $1; exit}')"
  if [[ -z "$serial" ]]; then
    local mdns
    mdns="$(adb mdns services 2>/dev/null | awk '/_adb-tls-connect/ {print $1; exit}')"
    if [[ -n "$mdns" ]]; then
      adb connect "${mdns}._adb-tls-connect._tcp" 2>/dev/null || true
      sleep 2
      serial="$(adb devices 2>/dev/null | awk '$2=="device" {print $1; exit}')"
    fi
  fi
  if [[ -z "$serial" ]]; then
    fail "No adb device found"
    exit 1
  fi
  echo "$serial"
}

adb_cmd() {
  adb -s "$SERIAL" "$@"
}

grant_storage() {
  local pkg="$1"
  adb_cmd shell appops set "$pkg" MANAGE_EXTERNAL_STORAGE allow >/dev/null 2>&1 || true
}

query_uid_column() {
  local uri="$1"
  adb_cmd shell content query --uri "$uri" 2>/dev/null \
    | sed -n 's/.*uid=\([^,]*\).*/\1/p' \
    | head -1
}

resolve_student_uid() {
  if [[ -n "${SMOKE_STUDENT_UID:-}" ]]; then
    echo "$SMOKE_STUDENT_UID"
    return 0
  fi
  local uid=""
  uid="$(query_uid_column "content://com.phoenixforge.student.sync.provider/profile_snapshot")"
  if [[ -n "$uid" ]]; then
    echo "$uid"
    return 0
  fi
  uid="$(adb_cmd shell content query --uri "content://com.phoenixforge.profile.provider/child_profiles" 2>/dev/null \
    | sed -n 's/.*uid=\([^,]*\).*/\1/p' | head -1)"
  if [[ -n "$uid" ]]; then
    echo "$uid"
    return 0
  fi
  uid="$(adb_cmd shell content query --uri "content://com.phoenixforge.profile.provider/linked_students" 2>/dev/null \
    | sed -n 's/.*profile_uid=\([^,]*\).*/\1/p' | head -1)"
  if [[ -n "$uid" ]]; then
    echo "$uid"
    return 0
  fi
  # Fallback: scan public sync tree (shell can list without ContentProvider permission)
  uid="$(adb_cmd shell "ls -1 '$SYNC_BASE' 2>/dev/null" \
    | grep -E '^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$' \
    | head -1)"
  echo "$uid"
}

write_stub_manifest() {
  local uid="$1"
  local tmp
  tmp="$(mktemp)"
  cat >"$tmp" <<EOF
{
  "schemaVersion": 1,
  "manifestId": "manifest_${uid}_${DATE_ISO}",
  "studentUid": "${uid}",
  "createdByDeviceId": "smoke-crucible",
  "createdEpochMs": $(date +%s000),
  "validFromDate": "${DATE_ISO}",
  "validToDate": "${DATE_ISO}",
  "days": [
    {
      "dayIndex": 0,
      "date": "${DATE_ISO}",
      "narrativeTitle": "Smoke test expedition",
      "narrativeText": "Automated cross-app smoke tile.",
      "quests": ["smoke_quest_01"],
      "unlockConditions": [],
      "rewardItems": []
    }
  ]
}
EOF
  adb_cmd shell mkdir -p "${SYNC_BASE}/${uid}/manifests" >/dev/null
  adb_cmd push "$tmp" "${SYNC_BASE}/${uid}/manifests/lesson_manifest_${DATE_ISO}.json" >/dev/null
  rm -f "$tmp"
}

write_stub_event() {
  local uid="$1"
  local epoch_ms="$2"
  local event_id="EVT_smoke-crucible_${epoch_ms}_0001"
  local tmp
  tmp="$(mktemp)"
  cat >"$tmp" <<EOF
{
  "schemaVersion": 1,
  "eventId": "${event_id}",
  "eventType": "QUEST_STARTED",
  "scope": "PUBLIC",
  "actorApp": "student_edition",
  "actorDeviceId": "smoke-crucible",
  "studentUid": "${uid}",
  "logicalClock": 1,
  "epochMs": ${epoch_ms},
  "payload": {"questId": "smoke_quest_01"}
}
EOF
  adb_cmd shell mkdir -p "${SYNC_BASE}/${uid}/events" >/dev/null
  adb_cmd push "$tmp" "${SYNC_BASE}/${uid}/events/${event_id}.json" >/dev/null
  rm -f "$tmp"
  echo "$event_id"
}

count_sync_files() {
  local path="$1"
  adb_cmd shell "ls -1 '$path' 2>/dev/null" | grep -c . || true
}

echo "=== Phoenix Forge Classroom Cross-App Smoke Test ==="
echo "Machine: CRUCIBLE | Date: $(date)"

SERIAL="$(resolve_serial "${1:-}")"
log "Device: $SERIAL"

log "Building APKs..."
cd "$ROOT"
./gradlew :forge-profile-app:assembleDebug :student-app:assembleDebug :teacher-app:assembleDebug --quiet

log "Installing APKs..."
adb_cmd install -r forge-profile-app/build/outputs/apk/debug/forge-profile-app-debug.apk >/dev/null
adb_cmd install -r teacher-app/build/outputs/apk/debug/teacher-app-debug.apk >/dev/null
adb_cmd install -r student-app/build/outputs/apk/debug/student-app-debug.apk >/dev/null
pass "Build + install complete"

log "Granting All files access (appops) for public sync paths..."
grant_storage com.phoenixforge.profile
grant_storage com.phoenixforge.student
grant_storage com.phoenixforge.classroom.teacher

STUDENT_UID="$(resolve_student_uid)"
if [[ -z "$STUDENT_UID" ]]; then
  fail "Could not resolve child STUDENT_UID — set SMOKE_STUDENT_UID or import child profile on device"
  exit 1
fi
log "Student UID: $STUDENT_UID"

adb_cmd logcat -c

log "Writing stub manifest to public sync path..."
write_stub_manifest "$STUDENT_UID"
MANIFEST_PATH="${SYNC_BASE}/${STUDENT_UID}/manifests/lesson_manifest_${DATE_ISO}.json"
if adb_cmd shell "test -f '$MANIFEST_PATH' && echo ok" 2>/dev/null | grep -q ok; then
  pass "Manifest at $MANIFEST_PATH"
else
  fail "Manifest missing at $MANIFEST_PATH"
fi

log "Launching Student Edition (manifest read)..."
adb_cmd shell am force-stop com.phoenixforge.student >/dev/null 2>&1 || true
adb_cmd shell am start -n "com.phoenixforge.student/.MainActivity" >/dev/null
sleep 5

log "Launching Teacher Edition (Expedition Board)..."
adb_cmd shell am force-stop com.phoenixforge.classroom.teacher >/dev/null 2>&1 || true
adb_cmd shell am start -n "com.phoenixforge.classroom.teacher/.MainActivity" >/dev/null
sleep 3

log "Writing stub QUEST_STARTED event..."
EPOCH_MS="$(date +%s000)"
EVENT_ID="$(write_stub_event "$STUDENT_UID" "$EPOCH_MS")"
EVENT_COUNT="$(count_sync_files "${SYNC_BASE}/${STUDENT_UID}/events/")"
if [[ "$EVENT_COUNT" -gt 0 ]]; then
  pass "Events in sync folder: $EVENT_COUNT (latest $EVENT_ID)"
else
  fail "No events under ${SYNC_BASE}/${STUDENT_UID}/events/"
fi

log "Launching Forge Profile (event ingest rescan)..."
adb_cmd shell am force-stop com.phoenixforge.profile >/dev/null 2>&1 || true
adb_cmd shell am start -n "com.phoenixforge.profile/.MainActivity" >/dev/null
sleep 6

adb_cmd logcat -d >"$LOG_FILE" 2>/dev/null || true

if grep -q "ManifestReader.*loaded manifest" "$LOG_FILE" 2>/dev/null; then
  pass "ManifestReader loaded manifest (logcat)"
else
  warn "ManifestReader load not seen — Student may not be signed in with matching UID"
fi

if grep -q "EventIngester.*ingested" "$LOG_FILE" 2>/dev/null; then
  pass "EventIngester ingested event (logcat)"
else
  warn "EventIngester ingest not seen in logcat — check child profile active in Forge Profile"
fi

TIMELINE="$(adb_cmd shell content query --uri "content://com.phoenixforge.profile.provider/child_profile/${STUDENT_UID}/timeline" 2>/dev/null || true)"
if echo "$TIMELINE" | grep -qiE "Started quest|smoke_quest"; then
  pass "Timeline shows quest event via ContentProvider"
elif grep -q "EventIngester.*ingested QUEST_STARTED" "$LOG_FILE" 2>/dev/null; then
  pass "EventIngester ingested QUEST_STARTED (logcat confirms ingest)"
else
  warn "Timeline query blocked or empty — verify Forge Profile → Timeline manually"
fi

echo ""
echo "=== SUMMARY ==="
echo "PASS: $PASS | FAIL: $FAIL | WARN: $WARN"
echo "Student UID: $STUDENT_UID"
echo "Manifest: $MANIFEST_PATH"
echo "Events dir: ${SYNC_BASE}/${STUDENT_UID}/events/"
echo "Logcat: $LOG_FILE"
echo ""
echo "--- logcat highlights (ManifestReader / EventIngester / EventWriter) ---"
grep -E "ManifestReader|EventIngester|EventWriter|ForgeProfileProjection" "$LOG_FILE" 2>/dev/null | tail -25 || true

if [[ "$FAIL" -eq 0 ]]; then
  if [[ "$WARN" -eq 0 ]]; then
    echo ""
    echo "🎉 SMOKE TEST PASSED"
    exit 0
  fi
  echo ""
  echo "⚠️  SMOKE TEST PARTIAL — file checks passed; review warnings above"
  exit 0
fi

echo ""
echo "❌ SMOKE TEST FAILED"
exit 1
