# Classroom skill + verify routing matrix

| work_type | leaf | skills (max 2) | verify |
|-----------|------|----------------|--------|
| compose_ui (plan) | sub_compose_architect | frontend-design, cline-essence | cline-essence-drift |
| compose_ui (build) | sub_compose_builder | frontend-design, android-kotlin-development | :student-app:assembleDebug |
| kotlin_domain | sub_kotlin_domain | android-kotlin-core, android-kotlin-development | gradlew test |
| sync_events | sub_sync_events | cline-essence, android-kotlin-core | forge-profile-core:test + student unit |
| cross_app_contract | sub_sync_events | cline-essence | same |
| sage_advisor | sub_sage_advisor | frontend-design, sage-persona | :teacher-app:testDebugUnitTest |
| curriculum | sub_curriculum_manifest | cline-essence | teacher unit tests |
| lesson_manifest | sub_curriculum_manifest | cline-essence | teacher unit tests |
| expedition | sub_expedition_board | frontend-design, cline-essence | :teacher-app:assembleDebug |
| forge_profile | sub_forge_profile_core | android-kotlin-development | :forge-profile-app:assembleDebug |
| profile_ingest | sub_forge_profile_core | android-kotlin-development | forge-profile-core:test |
| student_quests | sub_student_quests | android-kotlin-development, frontend-design | :student-app:assembleDebug |
| progression | sub_student_quests | android-kotlin-development | student unit |
| chariot | sub_chariot_sessions | cline-essence | chariot-adb-helper (optional) |
| gradle_build | sub_gradle_build | android-gradle-build-logic | assembleDebug all |
| verify_test | sub_verify_runner | cline-essence | verify-all + drift |

## Skill paths

| Skill | Path |
|-------|------|
| cline-essence | `docs/cline_essence/SKILL.md` |
| frontend-design | `~/.phoenix-forge-agent-skills/frontend-design/SKILL.md` |
| android-kotlin-development | `~/.phoenix-forge-agent-skills/android-kotlin-development/SKILL.md` |
| android-kotlin-core | `~/.phoenix-forge-agent-skills/android-kotlin-core/SKILL.md` |
| android-gradle-build-logic | `~/.phoenix-forge-agent-skills/android-gradle-build-logic/SKILL.md` |
| sage-persona | `phoenix-forge-classroom-teacher-edition/docs/SAGE_PERSONA.md` |

## Forbidden paths

- `phoenix-forge-classroom-teacher-edition/android/`
- `phoenix-forge-classroom-student-edition/android/`
