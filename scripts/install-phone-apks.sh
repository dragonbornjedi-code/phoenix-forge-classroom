#!/usr/bin/env bash
# Install Forge Profile, Student, and Teacher debug APKs on one device.
# Usage: ./scripts/install-phone-apks.sh [adb-serial]
set -euo pipefail

export JAVA_HOME="${JAVA_HOME:-/home/joshuar/.local/jdk}"
export PATH="$JAVA_HOME/bin:$PATH"

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
GRADLE_DIR="$ROOT/phoenix-forge-classroom-forge-profile"
SERIAL="${1:-}"

ADB=(adb)
if [[ -n "$SERIAL" ]]; then
  ADB=(adb -s "$SERIAL")
fi

echo "Building APKs..."
cd "$GRADLE_DIR"
./gradlew :forge-profile-app:assembleDebug :student-app:assembleDebug :teacher-app:assembleDebug

PROFILE_APK="$GRADLE_DIR/forge-profile-app/build/outputs/apk/debug/forge-profile-app-debug.apk"
STUDENT_APK="$GRADLE_DIR/student-app/build/outputs/apk/debug/student-app-debug.apk"
TEACHER_APK="$GRADLE_DIR/teacher-app/build/outputs/apk/debug/teacher-app-debug.apk"

echo "Installing on device..."
"${ADB[@]}" install -r "$PROFILE_APK"
"${ADB[@]}" install -r "$STUDENT_APK"
"${ADB[@]}" install -r "$TEACHER_APK"

echo "Installed packages:"
"${ADB[@]}" shell pm list packages | grep phoenixforge || true

echo "Done. On phone: Forge Profile, Phoenix Forge Classroom Student Edition, Phoenix Forge Classroom Teacher Edition."
