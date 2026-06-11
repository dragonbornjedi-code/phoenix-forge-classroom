#!/usr/bin/env bash
# Chariot disk cleanup — ported from ~/Downloads/chariot-cleanup.sh
# Usage: ./scripts/chariot-cleanup.sh [--clean]
# Run on Pi/Termux via SSH or adb shell when maintaining car head unit.

set -euo pipefail

echo "=== CHARIOT DISK CLEANUP ==="
echo "=== Before ==="
df -h /data 2>/dev/null || df -h / 2>/dev/null
echo ""

echo "=== Top space users in HOME ==="
du -sh ~/* 2>/dev/null | sort -rh | head -20 || true
echo ""

echo "=== Safe to clean ==="
echo "1. apt clean"
echo "2. npm cache clean --force"
echo "3. truncate chariot logs"
echo "4. tmp files"
echo ""
echo "Run with --clean to execute."

if [[ "${1:-}" == "--clean" ]]; then
  echo ""
  echo "=== CLEANING ==="
  apt clean 2>/dev/null && echo "  apt cache cleared" || true
  npm cache clean --force 2>/dev/null && echo "  npm cache cleared" || true
  for log in ~/chariot-dash.log ~/chariot-watchdog.log; do
    [[ -f "$log" ]] && truncate -s 0 "$log" && echo "  truncated $log"
  done
  rm -rf /data/data/com.termux/files/usr/tmp/* 2>/dev/null && echo "  tmp cleared" || true
  echo "=== After ==="
  df -h /data 2>/dev/null || df -h / 2>/dev/null
fi
