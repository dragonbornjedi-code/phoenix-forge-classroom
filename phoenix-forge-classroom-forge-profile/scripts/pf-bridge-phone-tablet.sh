#!/usr/bin/env bash
# Bridge PhoenixForge sync tree from steward phone → Ezra tablet via adb.
# Use when Syncthing is not yet wired. Master step 2.01 cross-device handoff.
#
# Usage:
#   ./scripts/pf-bridge-phone-tablet.sh                    # auto-detect serials
#   EZRA_UID=751aafb8-... PHONE_SERIAL=... TABLET_SERIAL=... ./scripts/pf-bridge-phone-tablet.sh
#   ./scripts/pf-bridge-phone-tablet.sh --from-cache /tmp/phone-phoenixforge
#
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
CACHE_DIR="${PF_BRIDGE_CACHE:-/tmp/phone-phoenixforge}"
FROM_CACHE=""
PHONE_SERIAL="${PHONE_SERIAL:-}"
TABLET_SERIAL="${TABLET_SERIAL:-}"
EZRA_UID="${EZRA_UID:-751aafb8-de36-4749-a311-f4977177e7b4}"

usage() {
  sed -n '2,8p' "$0" | sed 's/^# \{0,1\}//'
  exit "${1:-0}"
}

while [[ $# -gt 0 ]]; do
  case "$1" in
    -h|--help) usage 0 ;;
    --from-cache)
      FROM_CACHE="${2:-}"
      shift 2
      ;;
    *)
      echo "Unknown arg: $1" >&2
      usage 1
      ;;
  esac
done

pick_serial() {
  local pattern="$1"
  adb devices | awk -v p="$pattern" '$2=="device" && $1 ~ p {print $1; exit}'
}

if [[ -z "$PHONE_SERIAL" ]]; then
  PHONE_SERIAL="$(pick_serial 'adb-R5CX|192\.168\.1\.25' || true)"
fi
if [[ -z "$TABLET_SERIAL" ]]; then
  TABLET_SERIAL="$(pick_serial 'GN434|192\.168\.1\.19|usb:' || true)"
  if [[ -z "$TABLET_SERIAL" ]]; then
    # Any usb device that is not the phone
    TABLET_SERIAL="$(adb devices | awk '$2=="device" && $1 !~ /adb-R5CX/ && $1 !~ /_adb-tls/ {print $1; exit}')"
  fi
fi

if [[ -z "$TABLET_SERIAL" ]]; then
  echo "No tablet adb device. Plug in Ezra tablet or set TABLET_SERIAL." >&2
  exit 1
fi

STAGING="$(mktemp -d /tmp/pf-bridge.XXXXXX)"
trap 'rm -rf "$STAGING"' EXIT

pull_phone_tree() {
  if [[ -n "$FROM_CACHE" ]]; then
    echo "Using cache: $FROM_CACHE"
    cp -a "$FROM_CACHE/." "$STAGING/"
    return
  fi
  if [[ -z "$PHONE_SERIAL" ]]; then
    if [[ -d "$CACHE_DIR/sync" ]]; then
      echo "Phone offline — falling back to cache $CACHE_DIR"
      cp -a "$CACHE_DIR/." "$STAGING/"
      return
    fi
    echo "Phone offline and no cache at $CACHE_DIR" >&2
    exit 1
  fi
  echo "Pulling /sdcard/PhoenixForge from phone ($PHONE_SERIAL)…"
  adb -s "$PHONE_SERIAL" pull /sdcard/PhoenixForge/ "$STAGING/" 2>/dev/null || true
  if [[ ! -d "$STAGING/sync/profiles/$EZRA_UID" ]]; then
    echo "No profile folder for $EZRA_UID on phone pull." >&2
    exit 1
  fi
}

push_to_tablet() {
  local rel_base="sync/profiles/$EZRA_UID"
  local roots=(
    "/sdcard/PhoenixForge"
    "/storage/emulated/0/PhoenixForge"
    "/storage/emulated/0/Documents/PhoenixForge"
  )

  echo "Tablet: $TABLET_SERIAL · Ezra UID: $EZRA_UID"

  for root in "${roots[@]}"; do
    adb -s "$TABLET_SERIAL" shell "mkdir -p $root/$rel_base/manifests $root/$rel_base/events" 2>/dev/null || true
  done

  if [[ -d "$STAGING/sync/profiles/$EZRA_UID" ]]; then
    for root in "${roots[@]}"; do
      echo "Pushing profile tree → $root/$rel_base"
      adb -s "$TABLET_SERIAL" push "$STAGING/sync/profiles/$EZRA_UID/." "$root/$rel_base/" 2>/dev/null || true
      # adb push leaves files unreadable by student/profile UIDs — world-readable sync tree
      adb -s "$TABLET_SERIAL" shell "chmod -R a+rX $root/$rel_base 2>/dev/null; find $root/$rel_base -type f -exec chmod 644 {} \\; 2>/dev/null" || true
    done
  fi

  if [[ -f "$STAGING/export/forge_profile_push.json" ]]; then
    for root in "${roots[@]}"; do
      adb -s "$TABLET_SERIAL" shell "mkdir -p $root/../export 2>/dev/null; mkdir -p ${root%/sync*}/export" 2>/dev/null || true
    done
    adb -s "$TABLET_SERIAL" shell "mkdir -p /sdcard/PhoenixForge/export"
    adb -s "$TABLET_SERIAL" push "$STAGING/export/forge_profile_push.json" /sdcard/PhoenixForge/export/forge_profile_push.json
    echo "Pushed forge_profile_push.json"
  fi
}

align_student_uid() {
  local prefs='<?xml version='"'"'1.0'"'"' encoding='"'"'utf-8'"'"' standalone='"'"'yes'"'"' ?>
<map>
    <string name="student_active_import_uid">'"$EZRA_UID"'</string>
    <boolean name="student_signed_in" value="true" />
</map>'
  echo "Aligning Student Edition import UID on tablet…"
  adb -s "$TABLET_SERIAL" shell "run-as com.phoenixforge.student sh -c 'mkdir -p shared_prefs && cat > shared_prefs/student_session.xml'" <<< "$prefs"
}

push_student_private_tree() {
  local src="$STAGING/sync/profiles/$EZRA_UID"
  [[ -d "$src" ]] || return 0
  echo "Mirroring into Student app-private sync (Fire tablet fallback)…"
  adb -s "$TABLET_SERIAL" push "$src/manifests/." /data/local/tmp/pf_manifests/ 2>/dev/null || true
  adb -s "$TABLET_SERIAL" push "$src/events/." /data/local/tmp/pf_events/ 2>/dev/null || true
  adb -s "$TABLET_SERIAL" shell "run-as com.phoenixforge.student mkdir -p files/PhoenixForge/sync/profiles/$EZRA_UID/manifests files/PhoenixForge/sync/profiles/$EZRA_UID/events"
  for f in "$src/manifests"/*; do
    [[ -f "$f" ]] || continue
    adb -s "$TABLET_SERIAL" push "$f" "/data/local/tmp/$(basename "$f")"
    adb -s "$TABLET_SERIAL" shell "cat /data/local/tmp/$(basename "$f") | run-as com.phoenixforge.student sh -c 'cat > files/PhoenixForge/sync/profiles/$EZRA_UID/manifests/$(basename "$f")'"
  done
}

grant_storage() {
  echo "Granting storage access for sync reads…"
  adb -s "$TABLET_SERIAL" shell appops set com.phoenixforge.student MANAGE_EXTERNAL_STORAGE allow 2>/dev/null || true
  adb -s "$TABLET_SERIAL" shell appops set com.phoenixforge.profile MANAGE_EXTERNAL_STORAGE allow 2>/dev/null || true
  adb -s "$TABLET_SERIAL" shell pm grant com.phoenixforge.student android.permission.READ_EXTERNAL_STORAGE 2>/dev/null || true
  adb -s "$TABLET_SERIAL" shell pm grant com.phoenixforge.student android.permission.WRITE_EXTERNAL_STORAGE 2>/dev/null || true
  push_student_private_tree
}

verify_tablet() {
  echo "=== Tablet sync check ==="
  adb -s "$TABLET_SERIAL" shell "ls -la /sdcard/PhoenixForge/sync/profiles/$EZRA_UID/manifests/ 2>/dev/null" || true
  adb -s "$TABLET_SERIAL" shell "run-as com.phoenixforge.student cat shared_prefs/student_session.xml 2>/dev/null" || true
  adb -s "$TABLET_SERIAL" shell am force-stop com.phoenixforge.student
  adb -s "$TABLET_SERIAL" shell am start -n com.phoenixforge.student/.MainActivity
  sleep 4
  if adb -s "$TABLET_SERIAL" logcat -d | grep -q "loaded manifest for $EZRA_UID"; then
    echo "PASS: ManifestReader loaded manifest for $EZRA_UID"
  else
    echo "CHECK: Open Student → Today's Expedition / Adventures on tablet"
    adb -s "$TABLET_SERIAL" logcat -d | grep -iE "ManifestReader|manifest" | tail -5 || true
  fi
}

main() {
  pull_phone_tree
  push_to_tablet
  align_student_uid
  grant_storage
  verify_tablet
  echo ""
  echo "Bridge complete. Teacher phone UID $EZRA_UID → tablet."
  echo "Re-run after each Teacher 'Push today\\'s stack' (or set up Syncthing on /PhoenixForge/sync/)."
}

main
