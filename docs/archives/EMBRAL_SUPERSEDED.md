# Embral — superseded by Phoenix Forge World

**Status:** ARCHIVED (superseded) · **Date:** 2026-06-05  
**Prior repo:** `/var/lib/phoenix-ai/workspace/embral`  
**Successor:** `/var/lib/phoenix-ai/workspace/phoenix-forge-world` (standalone Godot repo)

## Why

Embral was the third Godot attempt (after Ezra's Quest and Questaria). Phoenix Forge Classroom now owns:

- **Forge Profile** — identity + avatar authority  
- **Student / Teacher Edition** — role-based Android shells  
- **phoenix-forge-world** — offline 3D reflection shell (imports profile JSON; never owns CMOS)

Embral's missions, HA bridge, and parallel save systems are **not** carried forward. Salvaged: KayKit heroes, shaders, scene patterns, quest JSON shapes (reference only).

## Archive procedure

```bash
# Copy curated assets into the new Godot project (safe, repeatable)
./scripts/salvage-embral-assets.sh

# Optional: move Embral tree into workspace archive (steward-only)
./scripts/archive-embral-superseded.sh --dry-run
./scripts/archive-embral-superseded.sh --move
```

After `--move`, Embral lives under `workspace/archive/embral-superseded-YYYY-MM-DD/` with a stub `README.md` left in the old path.

## Integration rule (non-negotiable)

**No Gradle or Git dependency** between Android APKs and Godot. Integration is **file contract only**:

| File | Writer | Reader |
|------|--------|--------|
| `forge_profile_push.json` | Forge Profile app (manual push) | phoenix-forge-world |
| `avatar_config.json` | Forge Profile export (future) | phoenix-forge-world |
| ContentProvider `/avatar` | forge-profile-core | Student Edition |

See [shared/schemas/forge_profile_push.schema.json](../../shared/schemas/forge_profile_push.schema.json) and [GODOT_MIGRATION_STRATEGY.md](../GODOT_MIGRATION_STRATEGY.md).

## Prior attempts map

| Project | Location | Disposition |
|---------|----------|-------------|
| Ezra's Quest | `archive/phoenix-forge-old/1_EzrasQuest` | Archived |
| Questaria | `legacy_quarantine_questaria/` | Quarantined |
| Embral | `embral/` → archive | Superseded → salvage |
