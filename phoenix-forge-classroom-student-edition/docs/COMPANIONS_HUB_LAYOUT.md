# Companions Hub Layout — Student Edition

Three zones on one screen. The child always knows who is with them, who is sleeping, and who lives in Pet Space.

## Zones

| Zone | Type | Capacity | Unlock |
|------|------|----------|--------|
| **Companion** | `COMPANION` | 1 featured | Bootstrap (`Spark`) |
| **Whisps** | `WHISP` | 0–N | Exploration events, imports |
| **Pet Space** | `PET` | 0–N | Level 4 / `PET_SPACE` room |

## Screen structure

```text
┌─────────────────────────────────────────┐
│ Companions                              │
│ Companions, whisps, and pets on your path │
├─────────────────────────────────────────┤
│ ★ COMPANION                             │
│ ┌─────────────────────────────────────┐ │
│ │ Spark · Stage N · mood              │ │
│ │ Last reaction dialogue              │ │
│ │ Phase: Playful Helper (ages 5–7)    │ │
│ └─────────────────────────────────────┘ │
├─────────────────────────────────────────┤
│ ✧ WHISPS                                │
│ [ Lumen — locked ] [ ??? — locked ]     │
├─────────────────────────────────────────┤
│ 🐾 PET SPACE                            │
│ Unlocks at Level 4 — or pet grid        │
└─────────────────────────────────────────┘
```

## Companion card (hero)

- **Name** + evolution stage + mood chip
- **Last reaction** — most recent `lastReaction` from `NPCEngine`
- **Phase label** — derived from student level (see `SPARK_COMPANION_UX.md` maturation phases)

## Whisp row

- Locked whisps: silhouette, name if known, hint (“Explore to wake whisps”)
- Unlocked: name, mood, last reaction, memory count

## Pet Space

- Before unlock: single card explaining Pet Space opens at Level 4
- After unlock: grid of `PET` npcs; empty state invites quest rewards

## Code

- UI: `student-app/.../ui/npc/CompanionsHubScreen.kt`
- Data: `NpcState`, `NpcType`, `NPCEngine`, `StudentWorldBootstrap`
