# Lesson Planner & Quest Generation — Teacher Edition

Planner output is **geared to the bundled Curriculum Of Life**: seven domains, all subdomains in `CurriculumSubdomainCatalog`, Pack 01 starter lessons, and expedition tiles.

## Planner flow

1. **Pick domain** — one of seven `CurriculumDomainId` values
2. **Pick subdomain** — skill group within domain (reading, money, breathwork, etc.)
3. **Generate draft** — `LessonPlannerEngine` builds a structured plan
4. **Append quest wrapper** — `QuestDraftGenerator` adds narrative hook + student mission
5. **Send to board** — creates `IntentTile` on Expedition Board

## Lesson plan sections (draft)

| Section | Source |
|---------|--------|
| Title | Subdomain name + domain pattern |
| Objective | Subdomain summary + domain progress metric |
| Student mission | Kid-language quest hook |
| Materials | Topics from subdomain chip list |
| Steps (5–7) | Domain `lessonPatterns` + subdomain topics |
| Questions | Domain `teacherCues` adapted |
| Duration | 10–20 min standard; low-demand variant |
| Supports / recovery | Domain `supportMethods` + design rules |
| Narrative hook | Quest engine story integration stub |
| Quest title + description | Student Edition handoff shape |

## Monthly rhythm (teacher framing)

- **Week 1–2:** Cognitive + Practical Life emphasis
- **Week 3:** Social + Emotional regulation
- **Week 4:** Monthly eval + compass adjustment (pairs with Sage Advisor)

## Domain coverage

All plans tag:

- `curriculumDomainId` — enum name
- `subdomainId` — catalog id
- `lessonPatternId` — slug from domain patterns
- `studentMission` — quest payload seed

## Quest generation appendix

Quest drafts are **not** a replacement for parent intent. They translate subdomain focus into:

```text
Narrative hook → Student mission → XP reward stub → Whisp/Spark reaction seed
```

Example (Practical Life · Telling Time):

- **Hook:** “The house clock lost its numbers overnight.”
- **Mission:** “Match three daily moments to clock cards: breakfast, outside, story.”
- **Tile fields:** materials, coaching cues, evidence note slot

## Code

- `domain/lesson/LessonPlannerEngine.kt`
- `domain/lesson/QuestDraftGenerator.kt`
- `ui/lesson/LessonPlannerScreen.kt`
- `TileRepository.createFromLessonPlan()`
