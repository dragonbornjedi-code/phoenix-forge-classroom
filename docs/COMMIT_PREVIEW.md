# COMMIT_PREVIEW.md — Forge Profile

## Architecture summary

- **CMOS (Layer 1):** Room database with full childhood-state snapshots and field-level identity history.
- **Experience Shell (Layer 3):** Forge Profile Android app (Compose + MVVM + Hilt).
- **Cross-app API:** `ProfileContentProvider` exposes profile and avatar rows to Student/Teacher editions.

## Data model summary

- **Profile:** `ForgeProfile` (+ `currentTitle` for display identity).
- **History:** `IdentitySnapshotEntity` (per-field + full `childhood_state` JSON payload).
- **Timeline:** `TimelineEventEntity` with `kotlinx.serialization` metadata maps.
- **Child domains:** About Me, Favorites, Dreams, Avatars, Memory artifacts, Teacher metadata (separate table).

## Screens implemented

| Screen | Purpose |
|--------|---------|
| Dashboard | Identity hub / navigation |
| Avatar Studio | Look customization |
| Identity Card | Profile fields |
| Childhood Timeline | Event history |
| Memory Capsule | Artifacts |
| Dream Board | Goals / dreams |
| Teacher Gate | Parent-gated steward zone (no hardcoded password) |

## Build status

**Verified:** `./gradlew clean assembleDebug` → **BUILD SUCCESSFUL** (API 34).

Gradle wrapper lives in `phoenix-forge-classroom-forge-profile/`; repo root `gradlew` delegates there.

## Known limitations

- Teacher Gate: secure PIN/biometric storage not yet implemented (gate stays closed until wired).
- Avatar Studio: UI present; save wiring to `AvatarViewModel` is minimal.
- Identity Card: `updateField` not yet connected to repository helpers.

## Future work

- Encrypted parent PIN for Teacher Gate.
- Student/Teacher edition Gradle modules (separate apps, same monorepo pattern).
- Godot bridge via ContentProvider + export bundle.
- Unit tests for snapshot/timeline repository paths.
