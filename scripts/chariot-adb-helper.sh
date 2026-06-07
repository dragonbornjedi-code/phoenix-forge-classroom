#!/usr/bin/env bash
# Chariot ADB helper — Kia Soul head unit (car dashboard)
# Ported from archive/phoenix-forge-old/automations/car-quest/
# Usage: ./scripts/chariot-adb-helper.sh [connect|status|listening|celebrate|deck|push-stack FILE]

set -euo pipefail

CAR_IP="${CHARIOT_IP:-10.197.129.220}"
CAR_PORT="${CHARIOT_PORT:-5555}"
CAR_HOST="${CAR_IP}:${CAR_PORT}"
CAR_STACK_DIR="/sdcard/PhoenixForge/Chariot"
DEEP_LINK_SCHEME="phoenixforge://chariot"

log() { printf '[chariot] %s\n' "$*"; }

car_connect() {
  if adb devices 2>/dev/null | grep -q "${CAR_HOST}.*device"; then
    return 0
  fi
  adb connect "$CAR_HOST" >/dev/null 2>&1 || true
  sleep 1
  adb devices 2>/dev/null | grep -q "${CAR_HOST}.*device"
}

car_shell() {
  adb -s "$CAR_HOST" shell "$@"
}

open_deep_link() {
  local path="$1"
  car_shell am start -a android.intent.action.VIEW -d "${DEEP_LINK_SCHEME}/${path}" >/dev/null
}

cmd_connect() {
  if car_connect; then
    log "Connected to Chariot at ${CAR_HOST}"
  else
    log "Failed to connect. Set CHARIOT_IP / CHARIOT_PORT and ensure wireless ADB is on."
    exit 1
  fi
}

cmd_status() {
  if car_connect; then
    log "Chariot: CONNECTED (${CAR_HOST})"
  else
    log "Chariot: NOT CONNECTED"
    exit 1
  fi
}

cmd_listening() {
  cmd_connect
  open_deep_link "listening"
  log "Opened listening display on Chariot"
}

cmd_celebrate() {
  local xp="${1:-25}"
  local quest="${2:-Car Quest}"
  cmd_connect
  open_deep_link "celebration?xp=${xp}&quest=$(python3 -c "import urllib.parse,sys; print(urllib.parse.quote(sys.argv[1]))" "$quest")"
  log "Celebration: +${xp} XP — ${quest}"
}

cmd_deck() {
  cmd_connect
  open_deep_link "deck"
  log "Opened quest deck on Chariot"
}

cmd_push_stack() {
  local file="${1:-}"
  if [[ -z "$file" || ! -f "$file" ]]; then
    log "Usage: push-stack PATH/to/quest-stack.json"
    exit 1
  fi
  cmd_connect
  car_shell mkdir -p "$CAR_STACK_DIR"
  adb -s "$CAR_HOST" push "$file" "${CAR_STACK_DIR}/quest-stack.json" >/dev/null
  log "Pushed quest stack to ${CAR_STACK_DIR}/quest-stack.json"
  open_deep_link "deck"
}

cmd_help() {
  cat <<EOF
Chariot ADB Helper — Phoenix Forge Classroom

  connect              Connect wireless ADB to Kia Soul head unit
  status               Show connection status
  listening            Open Student Edition listening display
  celebrate [xp] [q]   Open celebration display (default 25 XP)
  deck                 Open car quest deck
  push-stack FILE      Push quest-stack.json and open deck

Environment:
  CHARIOT_IP (default ${CAR_IP})
  CHARIOT_PORT (default ${CAR_PORT})

Requires Student Edition APK with ChariotDisplayActivity installed on Chariot.
EOF
}

main() {
  case "${1:-help}" in
    connect) cmd_connect ;;
    status) cmd_status ;;
    listening) cmd_listening ;;
    celebrate) cmd_celebrate "${2:-25}" "${3:-Car Quest}" ;;
    deck) cmd_deck ;;
    push-stack) cmd_push_stack "${2:-}" ;;
    help|-h|--help) cmd_help ;;
    *) log "Unknown command: $1"; cmd_help; exit 1 ;;
  esac
}

main "$@"
