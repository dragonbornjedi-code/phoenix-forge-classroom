# Scripts

Workspace automation for Phoenix Forge Classroom.

| Script | Purpose |
|--------|---------|
| [cline-essence-drift-check.sh](cline-essence-drift-check.sh) | Repo-wide doc/path contradiction guard (Cline Essence) |
| [install-phone-apks.sh](install-phone-apks.sh) | Build and install all three APKs (Forge Profile, Student, Teacher) |
| [salvage-embral-assets.sh](salvage-embral-assets.sh) | Copy KayKit heroes from Embral into `phoenix-forge-world/` |
| [archive-embral-superseded.sh](archive-embral-superseded.sh) | Move Embral tree to `workspace/archive/` (steward `--move`) |
| [chariot-adb-helper.sh](chariot-adb-helper.sh) | Car head-unit deep links + quest stack push |
| [systemd/](systemd/) | Optional daily drift-check timer (user systemd unit) |

**Build truth:** [docs/DEPLOYMENT_REALITY.md](../docs/DEPLOYMENT_REALITY.md)  
**Gradle root:** `phoenix-forge-classroom-forge-profile/gradlew` (repo root `gradlew` delegates there)

Edition-specific scripts belong under the matching edition folder when added.
