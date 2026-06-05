# Optional systemd drift timer

Runs `./scripts/cline-essence-drift-check.sh` once per day on the build host.

## Install (user systemd)

```bash
mkdir -p ~/.config/systemd/user
cp scripts/systemd/phoenix-forge-drift-check.service ~/.config/systemd/user/
cp scripts/systemd/phoenix-forge-drift-check.timer ~/.config/systemd/user/
# If repo is not at ~/phoenix-forge-classroom, edit WorkingDirectory in the .service file.
systemctl --user daemon-reload
systemctl --user enable --now phoenix-forge-drift-check.timer
systemctl --user list-timers phoenix-forge-drift-check.timer
```

## Verify

```bash
systemctl --user start phoenix-forge-drift-check.service
echo $?   # expect 0
```

Master step: **0.20** in `docs/roadmaps/00_MASTER_ROADMAP.md`.
