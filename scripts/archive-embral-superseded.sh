#!/usr/bin/env bash
# Archive Embral to workspace/archive after supersession by phoenix-forge-world.
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
EMBRAL="${EMBRAL_ROOT:-/var/lib/phoenix-ai/workspace/embral}"
ARCHIVE_ROOT="${ARCHIVE_ROOT:-/var/lib/phoenix-ai/workspace/archive}"
STAMP="$(date +%Y-%m-%d)"
DEST="$ARCHIVE_ROOT/embral-superseded-$STAMP"
DRY=0
MOVE=0

for arg in "$@"; do
  case "$arg" in
    --dry-run) DRY=1 ;;
    --move) MOVE=1 ;;
  esac
done

if [[ ! -d "$EMBRAL" ]]; then
  echo "Embral not found at $EMBRAL" >&2
  exit 1
fi

echo "Source:  $EMBRAL"
echo "Dest:    $DEST"
echo "Mode:    dry_run=$DRY move=$MOVE"
echo "Successor: $ROOT/phoenix-forge-world/"
echo "Doc:     $ROOT/docs/archives/EMBRAL_SUPERSEDED.md"

if [[ "$DRY" -eq 1 ]]; then
  echo "[dry-run] would rsync -a --exclude .git --exclude .godot \"$EMBRAL/\" \"$DEST/\""
  exit 0
fi

if [[ "$MOVE" -ne 1 ]]; then
  echo "Pass --move to perform archive (after running salvage-embral-assets.sh)" >&2
  exit 2
fi

mkdir -p "$ARCHIVE_ROOT"
rsync -a --exclude .git --exclude .godot "$EMBRAL/" "$DEST/"

cat > "$EMBRAL/README.md" <<EOF
# Embral — ARCHIVED

This project was superseded on $STAMP.

- **Archive copy:** \`$DEST\`
- **Active Godot shell:** \`$ROOT/phoenix-forge-world/\`
- **Android identity OS:** \`$ROOT/phoenix-forge-classroom-forge-profile/\`

See \`$ROOT/docs/archives/EMBRAL_SUPERSEDED.md\`.
EOF

echo "Archived to $DEST"
echo "Stub README written to $EMBRAL/README.md"
