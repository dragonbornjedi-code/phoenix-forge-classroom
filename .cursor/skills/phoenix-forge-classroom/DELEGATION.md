# Delegation — ao_classroom_bridge → leaf

## Proof command

```bash
./scripts/classroom-delegation-verify.py
```

Gate: ≥92% leaf hits · report: `registry/handoffs/classroom_delegation_last.json`

## Routing table

| Leaf | Triggers |
|------|----------|
| `sub_compose_architect` | navigation graph, navhost, plan compose |
| `sub_compose_builder` | DigitalHome, HomeScreen, compose layout |
| `sub_kotlin_domain` | ViewModel, repository, stateflow |
| `sub_sync_events` | EventWriter, EVT_, logicalClock, PUBLIC |
| `sub_sage_advisor` | SageAdvisor, sage_apply, long press |
| `sub_curriculum_manifest` | Curriculum, ManifestWriter, lesson_manifest |
| `sub_expedition_board` | ExpeditionBoard, field guide, FAB tile |
| `sub_forge_profile_core` | EventIngester, Room, ContentProvider |
| `sub_student_quests` | Quests, ProgressionEngine |
| `sub_chariot_sessions` | chariot, sage-sessions |
| `sub_gradle_build` | gradle, assembleDebug |
| `sub_verify_runner` | drift check, verify all |

Classifier: `scripts/classroom-route-classify.py`

## Contract

1. Context pack → leaf
2. Parent coordinates only
3. Verify leaf gates before done
4. Score includes `leaf_agent_id`
