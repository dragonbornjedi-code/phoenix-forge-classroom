# Forge Profile (Android monorepo)

**Central intelligence layer** for Ezra’s childhood OS — Room persistence, ContentProvider, and the **Avatar Studio** module other apps and Godot import from.

## Modules

| Module | Role |
|--------|------|
| **forge-profile-core** | Room, repositories, `ForgeProfile` + `Avatar`, ContentProvider, export DTOs |
| **forge-profile-app** | Steward APK — Dashboard, **Avatar Studio**, Timeline, Memories, Identity |
| **student-app** | Phoenix Forge Classroom Student Edition |
| **teacher-app** | Phoenix Forge Classroom Teacher Edition (Expedition Board MVP) |

```text
forge-profile-app  →  forge-profile-core
student-app        →  reads profile via ContentProvider (optional import)
teacher-app        →  standalone today; profile viewer + future coaching overlay
```

## Build

```bash
cp local.properties.example local.properties   # set sdk.dir
./gradlew :forge-profile-app:assembleDebug :student-app:assembleDebug :teacher-app:assembleDebug
```

Or from repo root: `./scripts/install-phone-apks.sh`

## Cross-app API

Authority: `com.phoenixforge.profile.provider`

| Path | Data | Target consumers |
|------|------|------------------|
| `/profile` | uid, forge_name, stage, title | Student import, Teacher viewer |
| `/avatar` | hair, eyes, skin, clothing, version | Student summary → full `AvatarConfig` export |
| `/timeline` | title, type, timestamp | Chronicle direction |

**Godot:** export `avatar_config.json` with `godotMeshHints` per [docs/GODOT_MIGRATION_STRATEGY.md](../docs/GODOT_MIGRATION_STRATEGY.md).

## Avatar Studio (code + roadmap)

| Today | Next (P1-A in [01_FORGE_PROFILE_ROADMAP.md](../docs/roadmaps/01_FORGE_PROFILE_ROADMAP.md)) |
|-------|--------------------------------|
| `ui/studio/AvatarStudioScreen.kt` — basic chips | Layered preview, accessories, shards, export JSON |
| `Avatar` model — 5 fields | `AvatarConfig` per [FORGEPROFILE_SPEC.md](../docs/FORGEPROFILE_SPEC.md) |

Specs: [docs/FORGEPROFILE_SPEC.md](../docs/FORGEPROFILE_SPEC.md) · Shared contracts: [shared/README.md](../shared/README.md)
