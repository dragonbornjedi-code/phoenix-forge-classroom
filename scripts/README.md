# Scripts

Workspace automation for Phoenix Forge Classroom.

| Script | Purpose |
|--------|---------|
| [cline-essence-drift-check.sh](cline-essence-drift-check.sh) | Repo-wide doc/path contradiction guard (Cline Essence) |
| [install-phone-apks.sh](install-phone-apks.sh) | Build and install all three APKs (Forge Profile, Student, Teacher) |
| [systemd/](systemd/) | Optional daily drift-check timer (user systemd unit) |

**Build truth:** [docs/DEPLOYMENT_REALITY.md](../docs/DEPLOYMENT_REALITY.md)  
**Gradle root:** `phoenix-forge-classroom-forge-profile/gradlew` (repo root `gradlew` delegates there)

Edition-specific scripts belong under the matching edition folder when added.
