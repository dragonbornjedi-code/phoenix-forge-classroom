#!/usr/bin/env bash
# Copy curated assets from Embral into phoenix-forge-world (no repo dependency).
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
EMBRAL="${EMBRAL_ROOT:-/var/lib/phoenix-ai/workspace/embral}"
WORLD="${PHOENIX_FORGE_WORLD_ROOT:-/var/lib/phoenix-ai/workspace/phoenix-forge-world}"
DEST="$WORLD/assets/salvage"

if [[ ! -d "$EMBRAL" ]]; then
  echo "Embral not found at $EMBRAL — set EMBRAL_ROOT" >&2
  exit 1
fi

mkdir -p "$DEST/models/kaykit_adventurers" "$DEST/shaders"

copy_if() {
  local src="$1" rel="$2"
  if [[ -e "$src" ]]; then
    mkdir -p "$(dirname "$DEST/$rel")"
    cp -a "$src" "$DEST/$rel"
    echo "OK $rel"
  else
    echo "SKIP missing $src"
  fi
}

echo "=== Salvage Embral → phoenix-forge-world ==="
for f in Knight Barbarian Mage Rogue_Hooded Rogue; do
  copy_if "$EMBRAL/assets/models/kaykit_adventurers/${f}.glb" "models/kaykit_adventurers/${f}.glb"
done

copy_if "$EMBRAL/assets/shaders/character_tint.gdshader" "shaders/character_tint.gdshader"
copy_if "$EMBRAL/assets/models/kaykit_adventurers/LICENSE.txt" "models/kaykit_adventurers/LICENSE.txt"

# Copy into live assets path expected by hero_catalog.gd
LIVE="$WORLD/assets/models/kaykit_adventurers"
mkdir -p "$LIVE"
for f in Knight Barbarian Mage Rogue_Hooded Rogue; do
  src="$DEST/models/kaykit_adventurers/${f}.glb"
  if [[ -f "$src" ]]; then
    if [[ "$src" -ef "$LIVE/${f}.glb" ]]; then
      echo "OK live ${f}.glb (already in place)"
    else
      cp -a "$src" "$LIVE/${f}.glb"
    fi
  fi
done
if [[ -f "$DEST/models/kaykit_adventurers/LICENSE.txt" ]]; then
  cp -a "$DEST/models/kaykit_adventurers/LICENSE.txt" "$LIVE/LICENSE.txt"
fi

echo "Done. Open Godot at: $WORLD"
