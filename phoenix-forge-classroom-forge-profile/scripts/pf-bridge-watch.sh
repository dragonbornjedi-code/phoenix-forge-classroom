#!/usr/bin/env bash
# Watch steward phone for new manifests and bridge to Ezra tablet.
# Run on Crucible while both devices are on adb (phone wireless + tablet USB).
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
INTERVAL="${PF_BRIDGE_INTERVAL:-15}"
EZRA_UID="${EZRA_UID:-751aafb8-de36-4749-a311-f4977177e7b4}"

echo "Pf-bridge watch — every ${INTERVAL}s (Ctrl+C to stop)"
echo "Ezra UID: $EZRA_UID"

LAST_HASH=""
while true; do
  PHONE="$(adb devices | awk '/adb-R5CX|192\.168\.1\.25/ && $2=="device" {print $1; exit}')"
  TABLET="$(adb devices | awk '$2=="device" && $1 !~ /adb-R5CX/ && $1 !~ /_adb-tls/ {print $1; exit}')"
  if [[ -n "$PHONE" && -n "$TABLET" ]]; then
    HASH="$(adb -s "$PHONE" shell "find /sdcard/PhoenixForge/sync/profiles/$EZRA_UID -type f 2>/dev/null | sort | xargs md5sum 2>/dev/null" 2>/dev/null | md5sum | awk '{print $1}')"
    if [[ -n "$HASH" && "$HASH" != "$LAST_HASH" ]]; then
      echo "$(date '+%H:%M:%S') Change detected — bridging…"
      EZRA_UID="$EZRA_UID" PHONE_SERIAL="$PHONE" TABLET_SERIAL="$TABLET" \
        "$SCRIPT_DIR/pf-bridge-phone-tablet.sh" || true
      LAST_HASH="$HASH"
    fi
  else
    echo "$(date '+%H:%M:%S') Waiting for phone+tablet adb…"
  fi
  sleep "$INTERVAL"
done
