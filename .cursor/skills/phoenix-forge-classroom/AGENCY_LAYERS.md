# Phoenix Forge Classroom — agency layers (ONE agency)

**Do not load entire taxonomy.** Use `registry/classroom_agent_manifest.yaml` + `classroom-context-pack.sh`.

## Active stack

```
Phoenix Forge Agency
  └── ao_classroom_bridge (parent — never answers alone if leaf matches)
        ├── sub_compose_architect    (nav plans, TeacherNavGraph)
        ├── sub_compose_builder      (Digital Home, Expedition UI)
        ├── sub_kotlin_domain        (ViewModels, repositories)
        ├── sub_sync_events          (EventWriter, PUBLIC, EVT_*)
        ├── sub_sage_advisor         → co_sage_coordinator pedagogy
        ├── sub_curriculum_manifest  (lesson manifest, curriculum)
        ├── sub_expedition_board     (tiles, start day)
        ├── sub_forge_profile_core   (Room ingest, projector)
        ├── sub_student_quests       (Quests, ProgressionEngine)
        ├── sub_chariot_sessions     (Chariot schemas/sessions)
        ├── sub_gradle_build         (assembleDebug, JDK)
        └── sub_verify_runner        (drift, tests, verify-all)
```

## Products (Layer 2)

| APK | Package | Role |
|-----|---------|------|
| Forge Profile | com.phoenixforge.profile | Authority, ingest, ContentProvider |
| Student | com.phoenixforge.student | PUBLIC read/write, Digital Home |
| Teacher | com.phoenixforge.classroom.teacher | Manifests, Sage, PROTECTED |

## LIVE (step 0.68)

- Digital Home hub + `public_state.json` parity — **[TESTED]**
- Sage long-press menu — LIVE
- EventWriter PUBLIC — LIVE
- Event ingest 1.04 — LIVE on disk
- **Next spine:** 0.69 QuestEngine

## Pedagogy bridge

`sub_sage_advisor` maps to Sovereign `co_sage_coordinator` leaves:
`co_sage_quest_designer`, `co_sage_lesson_planner`, `co_sage_storyteller`, `co_sage_parent_coach`

## Vision scale

Full catalog: `registry/classroom_agency_taxonomy.yaml` (index only in context)
