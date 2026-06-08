#!/usr/bin/env bash
# Install Forge Profile, Student, and Teacher debug APKs on one device.
# Usage: ./scripts/install-phone-apks.sh [adb-serial]
set -euo pipefail

if [[ -x /usr/lib/jvm/java-21-openjdk-amd64/bin/java ]]; then
  export JAVA_HOME="${JAVA_HOME:-/usr/lib/jvm/java-21-openjdk-amd64}"
elif [[ -d /home/joshuar/.local/jdk ]]; then
  export JAVA_HOME="${JAVA_HOME:-/home/joshuar/.local/jdk}"
fi
export PATH="$JAVA_HOME/bin:$PATH"

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
GRADLE_DIR="$ROOT/phoenix-forge-classroom-forge-profile"

classroom_adb_connect() {
  adb start-server >/dev/null 2>&1 || true

  local serial
  serial="$(adb devices 2>/dev/null | awk '$2=="device" {print $1; exit}')"
  if [[ -n "$serial" ]]; then
    echo "ADB: using $serial"
    echo "$serial"
    return 0
  fi

  echo "ADB: no device — trying wireless mDNS reconnect…" >&2
  local mdns_name
  mdns_name="$(adb mdns services 2>/dev/null | awk '/_adb-tls-connect/ {print $1; exit}')"
  if [[ -n "$mdns_name" ]]; then
    adb connect "${mdns_name}._adb-tls-connect._tcp" 2>&1 || true
    sleep 2
  fi

  serial="$(adb devices 2>/dev/null | awk '$2=="device" {print $1; exit}')"
  if [[ -z "$serial" ]]; then
    echo "ERROR: No adb device. On phone: Wireless debugging ON, same Wi‑Fi as laptop." >&2
    echo "  adb mdns services" >&2
    echo "  adb connect <name>._adb-tls-connect._tcp" >&2
    adb devices -l >&2 || true
    return 1
  fi
  echo "ADB: connected $serial"
  echo "$serial"
}

install_apk() {
  local apk="$1"
  local pkg="$2"
  local label="$3"
  echo "Installing $label…"
  local out
  if out="$("${ADB[@]}" install -r "$apk" 2>&1)"; then
    echo "$out"
    return 0
  fi
  if echo "$out" | grep -q 'INSTALL_FAILED_UPDATE_INCOMPATIBLE'; then
    echo "Signature mismatch for $pkg — uninstalling old build, reinstalling…" >&2
    "${ADB[@]}" uninstall "$pkg" >/dev/null 2>&1 || true
    "${ADB[@]}" install -r "$apk"
    return 0
  fi
  echo "$out" >&2
  return 1
}

REQUESTED_SERIAL="${1:-${ANDROID_SERIAL:-}}"
if [[ -n "$REQUESTED_SERIAL" ]]; then
  SERIAL="$REQUESTED_SERIAL"
  if ! adb devices 2>/dev/null | awk -v s="$SERIAL" '$1==s && $2=="device" {found=1} END{exit !found}'; then
    echo "Requested serial $SERIAL not online — reconnecting…" >&2
    SERIAL="$(classroom_adb_connect)"
  fi
else
  SERIAL="$(classroom_adb_connect)"
fi

ADB=(adb -s "$SERIAL")

echo "Building APKs..."
cd "$GRADLE_DIR"
./gradlew :forge-profile-app:assembleDebug :student-app:assembleDebug :teacher-app:assembleDebug

PROFILE_APK="$GRADLE_DIR/forge-profile-app/build/outputs/apk/debug/forge-profile-app-debug.apk"
STUDENT_APK="$GRADLE_DIR/student-app/build/outputs/apk/debug/student-app-debug.apk"
TEACHER_APK="$GRADLE_DIR/teacher-app/build/outputs/apk/debug/teacher-app-debug.apk"

# Re-check connection after long build
if ! adb devices 2>/dev/null | awk -v s="$SERIAL" '$1==s && $2=="device" {found=1} END{exit !found}'; then
  echo "ADB dropped during build — reconnecting…" >&2
  SERIAL="$(classroom_adb_connect)"
  ADB=(adb -s "$SERIAL")
fi

install_apk "$PROFILE_APK" com.phoenixforge.profile "Forge Profile"
install_apk "$STUDENT_APK" com.phoenixforge.student "Student Edition"
install_apk "$TEACHER_APK" com.phoenixforge.classroom.teacher "Teacher Edition"

echo "Installed packages:"
"${ADB[@]}" shell pm list packages 2>/dev/null | grep phoenixforge || true

echo "Done. On phone: Forge Profile, Phoenix Forge Classroom Student Edition, Phoenix Forge Classroom Teacher Edition."
