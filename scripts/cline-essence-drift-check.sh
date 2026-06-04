#!/usr/bin/env bash
# Phoenix Forge — Cline Essence drift guard (repo-wide).
# Exit 0 = pass, 1 = contradictions found.
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

FAIL=0
warn() { echo "DRIFT: $*" >&2; FAIL=1; }
ok() { echo "OK: $*"; }

echo "=== Cline Essence drift check ==="
echo "Root: $ROOT"

# --- Tier 0 files exist ---
for f in docs/CONSTITUTION.md docs/REPOSITORY_CONSTITUTION.md docs/UNIFIED_VISION.md \
         registry/phoenix-forge-classroom.yaml docs/cline_essence/SKILL.md \
         docs/cline_essence/MASTER_PROTOCOL.md docs/roadmaps/00_MASTER_ROADMAP.md; do
  [[ -f "$f" ]] || warn "Missing tier0/protocol file: $f"
done

# --- cline-essence version pin ---
if ! grep -q 'version: 1.1.0' docs/cline_essence/SKILL.md 2>/dev/null; then
  warn "docs/cline_essence/SKILL.md missing version: 1.1.0 frontmatter"
fi

# --- Forbidden build paths must not appear as instructions in docs ---
FORBIDDEN=(
  'phoenix-forge-classroom-teacher-edition/android/build'
  'phoenix-forge-classroom-student-edition/android/build'
  'teacher-edition/android/build.gradle'
  'student-edition/android/build.gradle'
)
while IFS= read -r -d '' f; do
  for pat in "${FORBIDDEN[@]}"; do
    if grep -q "$pat" "$f" 2>/dev/null; then
      warn "$f contains forbidden build path pattern: $pat"
    fi
  done
done < <(find . -name '*.md' ! -path './docs/superpowers/plans/*' -print0 2>/dev/null)

# Superpowers plans are SUPERSEDED — skip or only warn once
if grep -rl 'phoenix-forge-classroom-student-edition/android/build' docs/superpowers/plans/ >/dev/null 2>&1; then
  ok "superpowers plans contain legacy android paths (SUPERSEDED — expected)"
fi

# --- Phantom files must not be referenced as live ---
PHANTOMS=(
  'END_GOAL_AND_NORTH_STAR.md'
  'ALIGNMENT_AUDIT_2026-06-04.md'
  'student-experience-boundary.md'
)
while IFS= read -r -d '' f; do
  [[ "$f" == *DOCUMENTATION_ALIGNMENT_REPORT.md ]] && continue
  for p in "${PHANTOMS[@]}"; do
    if grep -q "$p" "$f" 2>/dev/null && ! grep -q 'Do not search for\|Phantom\|never existed\|SUPERSEDED' "$f" 2>/dev/null; then
      warn "$f references phantom file $p without negation context"
    fi
  done
done < <(find . -name '*.md' ! -path './docs/superpowers/plans/*' -print0 2>/dev/null)

# --- Gradle modules exist ---
for mod in forge-profile-core forge-profile-app student-app teacher-app; do
  [[ -d "phoenix-forge-classroom-forge-profile/$mod" ]] || warn "Missing module: $mod"
done

# --- Master step header present ---
if ! grep -qE 'Current step:\s*[0-9]+\.[0-9]{2}' docs/roadmaps/00_MASTER_ROADMAP.md; then
  warn "00_MASTER_ROADMAP.md missing 'Current step: X.XX' line"
fi

# --- shared/README must not lead with UME as spine ---
if head -20 shared/README.md | grep -q 'Universal Matching Engine' && ! head -5 shared/README.md | grep -q 'Forge Profile'; then
  warn "shared/README.md still presents UME as primary (pre-pivot)"
fi

if [[ "$FAIL" -eq 0 ]]; then
  ok "drift check passed"
  exit 0
fi

echo "=== Drift check FAILED ==="
exit 1
