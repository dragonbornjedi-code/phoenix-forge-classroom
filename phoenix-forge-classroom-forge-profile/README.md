# Forge Profile (Android)

Three-layer monorepo for the Phoenix Forge identity backbone.

## Modules

| Module | Role |
|--------|------|
| **forge-profile-core** | Android library — Room, repositories, domain models, ContentProvider, serialization. No Compose. |
| **forge-profile-app** | Steward UI APK — identity, timeline, memories. |
| **student-app** | Phoenix Forge Classroom Student Edition APK. |
| **teacher-app** | Phoenix Forge Classroom Teacher Edition APK (MVP shell → Expedition Board). |
| **teacher-app** | Teacher Edition MVP shell — Expedition Board placeholder. |

```
forge-profile-app  →  forge-profile-core
student-app        →  (standalone; reads profile via ContentProvider)
teacher-app        →  (standalone MVP; full board in progress)
```

## Build

```bash
cp local.properties.example local.properties   # set sdk.dir
./gradlew clean assembleDebug
```

APK: `forge-profile-app/build/outputs/apk/debug/forge-profile-app-debug.apk`

From monorepo root: `./gradlew assembleDebug` (delegates into this directory).

## Cross-app API

Authority: `com.phoenixforge.profile.provider`

| Path | Data |
|------|------|
| `/profile` | uid, forge_name, current_stage, current_title |
| `/avatar` | hair, eyes, skin, clothing |
| `/timeline` | title, type, timestamp |

Spec: [docs/FORGEPROFILE_SPEC.md](../docs/FORGEPROFILE_SPEC.md)
