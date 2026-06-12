# Stash & session handoff — 2026-06-11

**Operator:** Josh  
**Purpose:** Save state, document stashes, restore procedures. **No hardcoded profile UUIDs** in code or docs — only runtime resolution.

---

## Read first (next session)

| Priority | Doc | Repo |
|----------|-----|------|
| 1 | `docs/STASH_AND_SESSION_HANDOFF_20260611.md` | classroom (this file) |
| 2 | `docs/PRIMARY_DEVICE_AUTHORITY.md` | classroom |
| 3 | `docs/INBOX_ARCHITECTURE.md` | classroom |
| 4 | `docs/IDENTITY_CONTINUITY_AUDIT.md` | classroom |
| 5 | `../phoenix-forge-world/docs/SESSION_HANDOFF_20260609.md` | world (after stash pop) |

---

## World repo — GIT STASH (important)

### What was stashed

Repo: `phoenix-forge-world`  
Branch: `stage/offline`  
Stash name: **`world-polish-ezra-session-2026-06-10`**  
Stash index: **`stash@{0}`**

```bash
cd /var/lib/phoenix-ai/workspace/phoenix-forge-world
git stash list
# stash@{0}: On stage/offline: world-polish-ezra-session-2026-06-10
```

### What is inside the stash (42 tracked files, ~1561 insertions)

Character / tint / tutorial / hearth polish — **not committed**, safe in stash:

| Area | Key paths |
|------|-----------|
| Hero tint | `scripts/avatar/hero_tint.gd`, `hero_catalog.gd`, `hero_preview.gd`, `player_controller.gd` |
| Character config | `scripts/avatar/character_config.gd`, `scripts/ui/avatar_creator.gd`, `docs/CHARACTER_IDENTITY.md` |
| Forge import | `scripts/autoload/forge_import.gd` |
| Tutorial | `scripts/world/tutorial_manager.gd`, `data/tutorial/hearth_tutorial_layout.json`, `data/quests/hv_ignavarr_tutorial_01.json` |
| Hearth | `scripts/world/forge_hearth_manager.gd`, `domain_steward.gd`, combat, HUD |
| Verify | `scripts/verify_hero_tint_runtime.gd`, `scripts/verify-hero-tint-runtime.sh`, verify-all gate |
| Session | `scripts/progression/session_flags.gd`, docs handoff updates |
| Assets | KayKit `.glb` reimports, promote script extensions |

**Also stashed:** many untracked files from that session (digital home, house placer, registry yaml, design library, etc.) — restore with same `git stash pop`.

### What was NOT stashed (still on disk as untracked)

Git **ignores** nested git clones inside stash:

```
phoenix-forge-world/references/godot/godot-demo-projects/
phoenix-forge-world/references/godot/gdquest-3d-controller/
phoenix-forge-world/assets/salvage/downloads/platformer_kit/
```

These are reference/salvage clones — not part of the polish stash. Safe to leave or delete.

### Restore World work

```bash
cd /var/lib/phoenix-ai/workspace/phoenix-forge-world
git stash pop stash@{0}
# If conflicts: git stash list still has stash until drop; resolve then git stash drop
```

### Verify after restore

```bash
cd /var/lib/phoenix-ai/workspace/phoenix-forge-world
./scripts/phoenix-forge-world-verify-all.sh
./scripts/verify-hero-tint-runtime.sh
# Human: Godot F5 — Ezra hero must show KayKit texture + tint (not white blob)
```

### Older stash (do not pop unless intentional)

```
stash@{1}: student-polish-ezra-session-2026-06-10   # same branch, prior session
```

---

## Classroom repo — on disk (not in World stash)

Branch: `main`  
**Large WIP** remains **uncommitted** on disk (forge-sync-client, student/teacher inbox, profile projection, etc.). This commit session saves **architecture docs only** unless a follow-up commit bundles the Kotlin work.

Check before next edit:

```bash
cd /var/lib/phoenix-ai/workspace/phoenix-forge-classroom
git status -sb
```

---

## Architecture docs added this session (classroom)

| File | Topic |
|------|--------|
| `docs/PRIMARY_DEVICE_AUTHORITY.md` | One person → one profile → primary device → shells; dynamic handshake |
| `docs/INBOX_ARCHITECTURE.md` | `profiles/{studentUid}/inbox/MSG_*.json`; Syncthing tiers |
| `docs/IDENTITY_CONTINUITY_AUDIT.md` | UID duplication gaps + remediation |
| `docs/PLUGIN_CONTRACT.md` | forge-sync-client plugin (if present on disk) |

**Design rule (Josh feedback):** Examples use `{studentUid}` placeholders. Real UUIDs live only in Forge Profile Room + sync folder at runtime. Fresh install = new UUID. Profile deleted = re-link steward, never recompile apps.

---

## Syncthing + Forge Profile (short answer)

- **Today:** Syncthing-Fork syncs `/sdcard/PhoenixForge/sync/` (or `~/PhoenixForge/sync/` on laptop). Forge Profile **watches** that tree — it does not bundle Syncthing yet.
- **Target:** Forge Profile owns `syncRoot` (SAF / app storage); optional embedded sync later. All apps read `syncRoot` from provider — no hardcoded paths.
- **Inbox:** Just files under `profiles/{studentUid}/inbox/`; Profile ingests → Student shows card. No APK-to-APK messaging.

---

## Identity rules (never hardcode)

| Constant | Where it actually lives |
|----------|-------------------------|
| `studentUid` | `ForgeProfile.uid` in Room; `profile.uid` in push JSON |
| `deviceId` | `StudentDeviceIdStore` / `TeacherDeviceIdStore` / `user://device_id.txt` |
| `authorityDeviceId` | Linked-student metadata + `.device_registry/{deviceId}.json` |
| Debug vs production | Separate UUIDs; `profileTag: debug` (planned) — see audit doc |

---

## Commit / push record

| Repo | Action | Commit message (when pushed) |
|------|--------|------------------------------|
| `phoenix-forge-classroom` | docs + this handoff | See git log |
| `phoenix-forge-world` | **Stashed only** — no push until pop + commit |

---

## Next agent checklist

1. Read this file.
2. Decide lane: Classroom (sync/inbox) vs World (`git stash pop` first).
3. Never paste Ezra's UUID into code — use `ForgeSyncClient.activeStudentUid()` or provider.
4. Teacher push/manifest: require `FORGE_PROFILE_LINKED` (audit I-04).
5. World white-ghost: human Godot F5 before claiming fixed.
