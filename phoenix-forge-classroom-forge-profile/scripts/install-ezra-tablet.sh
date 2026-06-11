#!/usr/bin/env bash
# Install Forge Profile + Student Edition on Ezra's tablet (Fire or any Android).
# Run on Crucible with tablet on USB or wireless ADB.
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
export JAVA_HOME="${JAVA_HOME:-/home/joshuar/.local/jdk}"
export PATH="$JAVA_HOME/bin:$PATH"

SERIAL="${ADB_SERIAL:-}"
if [ -z "$SERIAL" ]; then
  SERIAL="$(adb devices | awk '/device$/{print $1; exit}')"
fi
if [ -z "$SERIAL" ]; then
  echo "No ADB device. Enable USB/wireless debugging on the tablet first."
  exit 1
fi

echo "Building APKs…"
cd "$ROOT"
./gradlew :forge-profile-app:assembleDebug :student-app:assembleDebug

PROFILE_APK="$ROOT/forge-profile-app/build/outputs/apk/debug/forge-profile-app-debug.apk"
STUDENT_APK="$ROOT/student-app/build/outputs/apk/debug/student-app-debug.apk"

echo "Installing on $SERIAL (use -r to keep data)…"
adb -s "$SERIAL" install -r "$PROFILE_APK"
adb -s "$SERIAL" install -r "$STUDENT_APK"

echo "Done. On tablet: open Forge Profile first, then Student Edition → More → Sync my hero."
