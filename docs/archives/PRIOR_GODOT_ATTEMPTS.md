# Prior Godot attempts — salvage map

Single reference for agents salvaging art, scenes, and patterns into **phoenix-forge-world**.

| Attempt | Path | Keep | Leave behind |
|---------|------|------|--------------|
| Ezra's Quest | `workspace/archive/phoenix-forge-old/1_EzrasQuest` | Narrative tone, kid-safe quest framing | Old Android coupling |
| Questaria | `workspace/legacy_quarantine_questaria` | Tile/quest JSON ideas | UME/USE as identity spine |
| Embral | `workspace/embral` | KayKit GLBs, Hearthveil hub scenes, shaders, AvatarCreator mapping | SaveManager profiles, HA autoloads, Dialogic stub stack |
| godot-open-rpg | `workspace/godot-open-rpg` | Reference combat patterns only | Not Ezra-first |

**Authority for avatar hero styles:** `AvatarHeroCatalog.kt` (Forge Profile) ↔ `hero_catalog.gd` (Forge World). Same four styles, same KayKit paths under `assets/models/kaykit_adventurers/`.

**Scene salvage priority (Embral):**

1. `scenes/overworld/hearthveil/HubWorld.tscn` — hearth layout reference  
2. `scripts/ui/AvatarCreator.gd` — superseded by Forge Profile Studio + world importer  
3. `assets/shaders/character_tint.gdshader` — hero color tint in world  

Run `./scripts/salvage-embral-assets.sh` before hand-copying files.
