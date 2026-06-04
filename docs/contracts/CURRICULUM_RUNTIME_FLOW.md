# Curriculum Runtime Flow

**Central question this document owns:**

> How does a real-world interaction become a permanent childhood artifact?

Pieces of the answer previously lived across Curriculum of Life, Intent Tile Contract, Childhood Compass, Expedition Board UX, and Student Simulation. This document unifies them into one closed loop.

**Prerequisites:**

- [CURRICULUM_OS_SCHEMA.md](CURRICULUM_OS_SCHEMA.md) — capabilities, atoms, signals, Compass aggregates
- [CURRICULUM_ATOMIZATION_GUIDE.md](../../phoenix-forge-classroom-teacher-edition/docs/CURRICULUM_ATOMIZATION_GUIDE.md) — decomposition rules
- [INTENT_TILE_CONTRACT.md](INTENT_TILE_CONTRACT.md) — planning unit
- [curriculum-of-life.md](../../phoenix-forge-classroom-teacher-edition/docs/curriculum-of-life.md) — canonical pedagogy (human, not replaced)

---

## 1. Authority boundaries

Multiple systems participate in the pipeline. Only one has final decision authority.

| System | Authority | May do | Must not do |
|--------|-----------|--------|-------------|
| **Curriculum of Life** | Defines possibilities | Lesson patterns, metrics, cues, supports, recovery paths | Prescribe today's plan or override parent |
| **Childhood Compass** | Recommends awareness | Surface sunlight status, capability trends, quiet domains | Say "Do this lesson today" |
| **Plan Generator** | Drafts | Propose tiles from theme + Compass + coupling rules | Lock or auto-accept a day plan |
| **Narrative Wrapper** | Decorates | Bind accepted intents into day story; skin with environment | Change which intents are on the board |
| **Student Simulation** | Reacts | Drift, NPC memory, story fragments from ingested events | Choose curriculum or replace parent plan |
| **Parent (Steward)** | **Decides** | Reorder, swap, defer, wildcard tiles; accept expedition; approve chronicle | — |

### Compass language rule

The Compass and heat map use **observation language only**:

| Allowed | Not allowed |
|---------|-------------|
| "Early Literacy capability has been quiet for 17 days." | "Do Label Hunt today." |
| "Visual discrimination: L2 stable, L4 not yet observed." | "You should focus on cognitive foundations." |
| "Emotional load trend elevated this week." | "Skip creative expression until behavior improves." |

Future AI features must inherit this boundary. Recommendations are **draft inputs** to Plan Generation; acceptance remains at the Steward Gate ([PLAN_GENERATION_RULES.md](../../phoenix-forge-classroom-teacher-edition/docs/PLAN_GENERATION_RULES.md) Step 4).

### Narrative vs intent rule

The story celebrates intent; it never dictates the plan ([QUEST_ENGINE_DESIGN.md](../QUEST_ENGINE_DESIGN.md)). `story_hooks` decorate the day; `memory_hooks` persist into the archive.

### Student vs Teacher rule

Student captures and plays; Teacher stewards and approves ([STUDENT_TEACHER_BOUNDARY.md](../STUDENT_TEACHER_BOUNDARY.md)). Child evidence is immutable primary record; parent adds annotation, not replacement.

---

## 2. End-to-end flow (closed loop)

```text
Curriculum Of Life          [human canonical — defines patterns + pedagogy]
        ↓
Lesson Pattern              [e.g. Label Hunt — stable ID in OS]
        ↓
Skill Atoms                 [smallest measurable units + L1–L4]
        ↓
Capabilities                [longitudinal growth unit — Compass primary axis]
        ↓
Compass Analysis            [CompassSnapshot from session history]
        ↓
Needs More Sunlight         [awareness signal — not a command]
        ↓
Plan Generation Rules       [draft day plan from theme + gaps + energy mix]
        ↓
Intent Tiles                [metadata-rich library objects]
        ↓
Parent Reorder              [Steward Gate — final intent of the day]
        ↓
Accept Expedition           [locks ACTIVE tiles → narrative input]
        ↓
Narrative Wrapper           [day story from story_hooks — decorates only]
        ↓
Lesson Session              [real-world activity with child]
        ↓
Evidence Capture            [LessonSessionEvidence + atom outcomes]
        ↓
Lesson Signals              [engagement, frustration, curiosity, independence, recovery]
        ↓
MemoryEvent                 [CMOS ingestion contract]
        ↓
WorldOrchestrator           [Student dispatch — if shell active]
        ↓
EmotionalImpactSpine        [shared emotional weight]
        ↓
StoryGraphEngine            [fragments, callbacks, continuity threads]
        ↓
WorldDriftEngine            [environmental drift from behavior]
        ↓
Living Chronicle            [approved permanent record]
        ↓
Identity Threads            [IFE reinforcement from capability evidence]
        ↓
Compass Recalibration       [loop closes — next draft plan informed]
```

---

## 3. Stage-by-stage (artifacts and owners)

### Phase A — Knowledge (offline / library)

| Stage | Input | Output | Owner |
|-------|-------|--------|-------|
| Curriculum Of Life | Family pedagogy | Lesson patterns, metrics, cues | Human canonical doc |
| Lesson Pattern | Pattern name in §1–7 | `lesson_pattern_id` | OS seed |
| Skill Atoms | Atomization guide | `SkillAtom` rows linked to capabilities | Curriculum Library maintainer |
| Capabilities | Capability catalog | `DevelopmentalCapability` — stable IDs | OS seed |

**Key invariant:** A child can fail *Label Hunt* while still growing `VISUAL_DISCRIMINATION`. Sessions record atom outcomes; Compass aggregates **capabilities**.

---

### Phase B — Planning (Teacher Edition)

| Stage | Input | Output | Owner |
|-------|-------|--------|-------|
| Compass Analysis | `LessonSessionEvidence`, prior snapshots | `CompassSnapshot` | CDNS / Compass Engine |
| Needs More Sunlight | Low `capabilityGrowth` or quiet domain | Bias flags for Plan Generator | Compass (recommend) |
| Plan Generation | Theme, Compass bias, coupling rules, ForgeProfile threads | Draft `IntentTile` list per day | Plan Generator (draft) |
| Intent Tiles | Curriculum Library | Tile objects with atoms, hooks, signals template | Library |
| Parent Reorder | Draft plan | Final tile order, swaps, deferrals, wildcards | **Parent** |
| Accept Expedition | Finalized ACTIVE tiles | Immutable day intent + narrative input | **Parent** |
| Narrative Wrapper | `story_hooks` + accepted list | Day narrative string, Spark mission skin | PCAS (decorate) |

**Artifacts:** `IntentTile` (states: DRAFT → ACTIVE → COMPLETED | DEFERRED). See [EXPEDITION_BOARD_UX.md](../../phoenix-forge-classroom-teacher-edition/docs/EXPEDITION_BOARD_UX.md).

---

### Phase C — Execution (real world)

| Stage | Input | Output | Owner |
|-------|-------|--------|-------|
| Lesson Session | ACTIVE tile `field_guide` | Lived experience | Parent + child |
| Evidence Capture | Metrics, photo, prompt levels | `LessonSessionEvidence`, `AtomOutcome[]` | Parent |
| Lesson Signals | Observation + reflection | `LessonSignal[]` with optional `FailureType` | Parent / tile completion UI |

**Regulation-first:** If session hits red-state equivalent (`FailureType` overload / avoidance during escalation), evidence capture pauses; recovery path from curriculum cues applies before teaching resumes.

---

### Phase D — Ingestion (CMOS + PCAS)

| Stage | Input | Output | Owner |
|-------|-------|--------|-------|
| MemoryEvent | Tile completion + evidence + hooks | Draft in CMOS buffer | Teacher/Student shell emit |
| Steward approval | Draft event | Promoted **Living Chronicle** entry | Parent |
| Identity Threads | Capability gains, deep reflection | Thread confidence updates | IFE (suggest) + parent confirm |

See [MEMORY_EVENT_CONTRACT.md](MEMORY_EVENT_CONTRACT.md), [TILE_EVENT_MAPPING.md](TILE_EVENT_MAPPING.md).

| Tile field | Chronicle / downstream |
|------------|------------------------|
| `memory_hooks.narrative_seed_phrase` | Keywords + Student `StoryFragment.continuityThread` |
| `memory_hooks.emotional_anchor` | Significance boost |
| `capability_ids` + `atom_outcomes` | Bundled metadata artifact (2046 replay) |
| `lesson_signals` (instance) | Compass recalibration inputs |
| Full `IntentTile` | Hidden metadata on MemoryEvent |

---

### Phase E — Student simulation (optional same day)

When Student Edition is active, ingestion also drives the sealed simulation loop:

| Stage | Input | Output |
|-------|-------|--------|
| WorldOrchestrator | `WorldEvent` mapped from tile / mission completion | `WorldEventResult` |
| EmotionalImpactSpine | Event + `BehaviorSignals` + absence | `EmotionalImpact` |
| StoryGraphEngine | Cause fragment + impact | `StoryFragment` with `callbackLine`, `causeFragmentId` |
| WorldDriftEngine | Weekly behavior + impact | `WorldDriftState` persisted |
| NPCEngine + memory compression | Impact strength | Compressed NPC graph, absence dialogue |

Student shell does **not** write chronicle directly; it reacts to approved or buffered events per boundary spec.

---

### Phase F — Recalibration (loop close)

| Stage | Input | Output |
|-------|-------|--------|
| Living Chronicle | Approved MemoryEvents | Long-term archive |
| Compass Recalibration | Sessions + chronicle + threads | Updated `CompassSnapshot`; new sunlight flags |

Next Plan Generation cycle reads recalibrated Compass — still draft-only until parent accepts.

---

## 4. What becomes permanent (2046 test)

A five-minute activity becomes a permanent childhood artifact when **all** of the following are true:

1. **Intent is recorded** — Accepted tile (or wildcard) documents *why* the activity existed.
2. **Evidence is captured** — At least one `AtomOutcome` or observation tied to a `capability_id`.
3. **Human voice is preserved** — Photo, note, or reflection on MemoryEvent; child capture if applicable.
4. **Steward approves** — Event promoted from buffer to Living Chronicle.
5. **Capability delta is computable** — Prompt level or difficulty level change is stored, not just "completed."
6. **Narrative thread is optional but valuable** — `memory_hooks` link the moment to StoryGraph / chronicle keywords.

Lesson checklist completion alone is **insufficient**. Capability movement + evidence is the bar.

---

## 5. Failure and recovery in the runtime

| FailureType | Session signal | Plan Generator bias | Simulation |
|-------------|----------------|---------------------|------------|
| OVERLOAD | FRUSTRATION high | Fewer high EF tiles next draft | Drift → world quietness |
| CONFUSION | FRUSTRATION + SKILL_GAP | Same capability, L1 tile variant | Reflective narrative tone |
| FATIGUE | FRUSTRATION | Low-energy tile, shorter duration | — |
| AVOIDANCE | FRUSTRATION / no ENGAGEMENT | Defer tile; regulation tile | — |
| SKILL_GAP | INDEPENDENCE low on atom | Repeat atom at L1–L2 | Memory anchor if breakthrough |

Recovery signal (`LessonSignal.RECOVERY`) closes the emotional loop and feeds `recoveryTrend` in Compass.

---

## 6. Reference path: Secret Label Decoder

First **reference tile** to prove the architecture (one lesson, not hundreds):

| Property | Value |
|----------|--------|
| Source | [starter-lessons-pack-01.md § Lesson 1](../../phoenix-forge-classroom-teacher-edition/docs/starter-lessons-pack-01.md) |
| Pattern | Label Hunt (`curriculum-of-life.md` §1) |
| `lesson_pattern_id` | `label_hunt` |
| Student mission | Find secret labels; match to objects |
| Capabilities exercised | `VISUAL_DISCRIMINATION`, `SYMBOL_TO_OBJECT_MAPPING`, `OBJECT_FUNCTION_MAPPING`, `VERBAL_RETRIEVAL` |
| Atoms | See [CURRICULUM_ATOMIZATION_GUIDE.md](../../phoenix-forge-classroom-teacher-edition/docs/CURRICULUM_ATOMIZATION_GUIDE.md) §3 |

### Proof checklist (end-to-end)

```text
Curriculum → Intent Tile (Secret Label Decoder)
          → Expedition ACTIVE
          → Session + evidence + signals
          → Compass capability delta
          → MemoryEvent (+ bundled tile metadata)
          → StoryGraph continuity (memory_hooks)
          → Chronicle approved
          → Compass recalibration
```

If this path fails, fix schema or contracts before expanding the lesson library.

**Reference tile (integration test):** [reference-tiles/secret-label-decoder.yaml](reference-tiles/secret-label-decoder.yaml) — run [proof checklist](reference-tiles/README.md) before scaling the lesson library.

---

## 7. Implementation order (repo)

| Order | Work | Status |
|-------|------|--------|
| 1 | CURRICULUM_OS_SCHEMA.md | Done |
| 2 | CURRICULUM_ATOMIZATION_GUIDE.md | Done |
| 3 | INTENT_TILE_CONTRACT.md (OS fields) | Done |
| 4 | **CURRICULUM_RUNTIME_FLOW.md** (this doc) | Done |
| 5 | Reference Intent Tile — Secret Label Decoder | Done — [secret-label-decoder.yaml](reference-tiles/secret-label-decoder.yaml) |
| 6 | End-to-end validation (manual or harness) | **Next** — [proof checklist](reference-tiles/README.md) |
| 7 | Remaining lesson library | After proof |
| 8 | Expedition Board UI | After proven data model |

---

## 8. Diagram: decision vs decoration vs reaction

```text
                    ┌─────────────────┐
                    │     PARENT      │
                    │  final authority │
                    └────────┬────────┘
                             │ accept / approve
         ┌───────────────────┼───────────────────┐
         ▼                   ▼                   ▼
   Expedition Board    Evidence + Signals   Chronicle approval
         │
         │ accepted intents only
         ▼
   Narrative Wrapper ──────────▶ day story (decorate)
         │
         ▼
   Lesson Session (real world)
         │
         ├──────────────────────▶ MemoryEvent ──▶ Living Chronicle
         │
         └──────────────────────▶ WorldOrchestrator (react)
                                      │
                                      ├─ EmotionalImpactSpine
                                      ├─ StoryGraphEngine
                                      └─ WorldDriftEngine

   Compass ◀── reads sessions + chronicle (recommend only)
         │
         └──▶ Plan Generator (draft) ──▶ new Intent Tiles
```

---

## 9. Related documents

| Document | Role in flow |
|----------|----------------|
| [CHILDHOOD_COMPASS_ENGINE.md](../../phoenix-forge-classroom-teacher-edition/docs/CHILDHOOD_COMPASS_ENGINE.md) | Compass + tiles + narrative workflow |
| [PLAN_GENERATION_RULES.md](../../phoenix-forge-classroom-teacher-edition/docs/PLAN_GENERATION_RULES.md) | Draft plan heuristics |
| [EXPEDITION_BOARD_UX.md](../../phoenix-forge-classroom-teacher-edition/docs/EXPEDITION_BOARD_UX.md) | Parent interaction surface |
| [QUEST_ENGINE_DESIGN.md](../QUEST_ENGINE_DESIGN.md) | Narrative Wrapper |
| [EVENT_CAPTURE_PROTOCOL.md](../EVENT_CAPTURE_PROTOCOL.md) | Life → archive entry |
| [IDENTITY_FORMATION_ENGINE.md](../IDENTITY_FORMATION_ENGINE.md) | Threads after chronicle |
| Student `WorldOrchestrator` | Phase E implementation (forge-profile student-app) |

---

## 10. Summary

The pipeline turns a short activity today into a twenty-year artifact by binding **capability evidence** to **steward-approved memory**, with narrative and simulation as reactions — not authorities.

The loop is only closed when Compass recalibrates from sessions and chronicle, and the next plan is still a **draft** until the parent accepts.
