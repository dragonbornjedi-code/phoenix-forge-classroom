# Curriculum OS Schema

Operational data model for Phoenix Forge Teacher + Student systems. This schema does **not** replace human-readable curriculum text. It indexes and reasons over:

- [curriculum-of-life.md](../../phoenix-forge-classroom-teacher-edition/docs/curriculum-of-life.md) (canonical pedagogy)
- [curriculum-taxonomy.md](../../phoenix-forge-classroom-teacher-edition/docs/curriculum-taxonomy.md) (subcategories and tags)
- [INTENT_TILE_CONTRACT.md](INTENT_TILE_CONTRACT.md) (planning unit)
- Student simulation spine (`BehaviorSignals`, `EmotionalImpactSpine`, `StoryGraphEngine`, `WorldDriftEngine`)

**Design principle:** Lessons change. **Capabilities** persist. The Childhood Compass tracks capability growth more heavily than lesson completion.

---

## 1. Runtime bridge (target pipeline)

```text
Curriculum (human) ──atomize──▶ SkillAtoms + Capabilities
                                      │
                                      ▼
                              LessonSignals (per session)
                                      │
                    ┌─────────────────┼─────────────────┐
                    ▼                 ▼                 ▼
            Childhood Compass   Plan Generation    Evidence Store
                    │                 │
                    ▼                 ▼
            CompassSnapshot    Intent Tiles (ACTIVE)
                                      │
                                      ▼
                         MemoryEvent / WorldEvent
                                      │
                    ┌─────────────────┴─────────────────┐
                    ▼                                   ▼
            Narrative Wrapper                  Student Orchestrator
            (story_hooks + memory_hooks)       (drift + story graph)
```

See [CURRICULUM_ATOMIZATION_GUIDE.md](../../phoenix-forge-classroom-teacher-edition/docs/CURRICULUM_ATOMIZATION_GUIDE.md) for how to decompose existing lesson patterns without editing canonical prose.

---

## 2. Kotlin domain model (reference)

Packages are indicative for a future `:curriculum-core` or Teacher Edition module.

```kotlin
// ── Stable taxonomy (7 domains, fixed) ─────────────────────────────────────

enum class CurriculumDomainId {
    COGNITIVE_ACADEMIC,
    EMOTIONAL_REGULATION,
    PRACTICAL_LIFE,
    SOCIAL_DYNAMICS,
    PHYSICAL_MASTERY,
    CREATIVE_CURIOSITY,
    ETHICS_CIVIC
}

data class CurriculumDomain(
    val id: CurriculumDomainId,
    val displayName: String,           // matches curriculum-of-life section title
    val teacherFraming: String,        // excerpt key, not full prose storage
    val studentFraming: String
)

data class CurriculumSubcategory(
    val id: String,                    // e.g. "early_literacy"
    val domainId: CurriculumDomainId,
    val displayName: String,           // from curriculum-taxonomy.md
    val purpose: String
)

// ── Lesson pattern (curriculum-of-life unit, not replaced) ───────────────────

data class LessonPattern(
    val id: String,                    // e.g. "label_hunt"
    val domainId: CurriculumDomainId,
    val displayName: String,           // e.g. "Label Hunt"
    val subcategoryIds: List<String>,  // taxonomy links
    val sourceRef: String              // "curriculum-of-life.md#1-cognitive-..."
)

// ── Capabilities (longitudinal — Compass primary axis) ───────────────────────

enum class CapabilityId {
    VISUAL_DISCRIMINATION,
    SYMBOL_TO_OBJECT_MAPPING,
    CATEGORY_GROUPING,
    VERBAL_RETRIEVAL,
    OBJECT_FUNCTION_MAPPING,
    WORKING_MEMORY,
    MULTI_STEP_INSTRUCTION_MEMORY,
    PATTERN_GENERALIZATION,
    CAUSE_EFFECT_REASONING,
    ERROR_DETECTION_CORRECTION,
    COGNITIVE_FLEXIBILITY_SWITCHING,
    FEELING_IDENTIFICATION,
    BODY_SIGNAL_AWARENESS,
    EMOTIONAL_TRIGGER_RECOGNITION,
    EMOTIONAL_RECOVERY_SEQUENCING,
    ANTICIPATORY_REGULATION,
    CO_REGULATION_DEPENDENCY_REDUCTION,
    EMOTION_TO_ACTION_MAPPING,
    ROUTINE_AUTONOMY,
    RESOURCE_AWARENESS,
    RESPONSIBILITY_OWNERSHIP,
    MICRO_DECISION_STACKING,
    CONSEQUENCE_TRACING,
    EMPATHY_PERSPECTIVE,
    SOCIAL_PREDICTION,
    REPAIR_BEHAVIOR,
    ROLE_FLEXIBILITY,
    GROUP_STABILITY_AWARENESS,
    BOUNDARY_NEGOTIATION,
    TURN_TAKING,
    IMPULSE_DELAY,
    GROSS_MOTOR_CONTROL,
    FINE_MOTOR_PRECISION,
    PROPrioceptive_CALIBRATION,
    ENVIRONMENTAL_RISK_SCANNING,
    TOOL_ADAPTATION,
    TERRAIN_INTERPRETATION,
    CONSTRAINT_INVENTION,
    ITERATION_AWARENESS,
    IDEA_TRANSFER_CROSS_DOMAIN,
    NARRATIVE_CAUSALITY_CREATION,
    ABSTRACT_SIMPLIFICATION,
    INTENT_OUTCOME_SEPARATION,
    COLLECTIVE_CONSEQUENCE_AWARENESS,
    FAIRNESS_UNDER_CONSTRAINT,
    IDENTITY_VS_ACTION_SEPARATION,
    CURIOSITY_PURSUIT,
    INDEPENDENCE_EXECUTION
}

data class DevelopmentalCapability(
    val id: CapabilityId,
    val displayName: String,
    val domainIds: List<CurriculumDomainId>,  // primary + secondary exposure
    val description: String,
    val observableIndicators: List<String>     // Compass + evidence rubric
)

// ── Skill atoms (smallest measurable unit under a lesson pattern) ────────────

enum class DifficultyLevel {
    L1_GUIDED_IMITATION,
    L2_PROMPTED_EXECUTION,
    L3_PARTIAL_INDEPENDENCE,
    L4_TRANSFER_GENERALIZATION
}

data class SkillAtom(
    val id: String,                           // stable slug, e.g. "label_hunt.visual_discrimination"
    val lessonPatternId: String,
    val capabilityId: CapabilityId,          // what Compass actually tracks
    val displayName: String,
    val difficultyLevels: Map<DifficultyLevel, String>,  // level-specific success criteria
    val evidenceTypes: List<EvidenceType>,
    val defaultDifficulty: DifficultyLevel = DifficultyLevel.L1_GUIDED_IMITATION
)

enum class EvidenceType {
    OBSERVATION,
    PHOTO,
    SCORE,
    DRAWING,
    BUILD_ARTIFACT,
    AUDIO_NOTE,
    STUDENT_REFLECTION,
    PROMPT_LEVEL_LOG              // model | verbal | gesture | independent
}

// ── Failure taxonomy (plugs into EmotionalImpactSpine + drift) ───────────────

enum class FailureType {
    OVERLOAD,           // too much input
    CONFUSION,          // instruction mismatch
    FATIGUE,            // energy depletion
    AVOIDANCE,          // emotional resistance
    SKILL_GAP           // missing prerequisite atom
}

data class FailureSignature(
    val type: FailureType,
    val domainId: CurriculumDomainId,
    val observables: List<String>,            // what it looks like in this domain
    val likelyCauses: List<String>,
    val resetIntervention: String,            // regulation-first reset path
    val linkedSupportMethods: List<String>    // keys into curriculum support methods
)

// ── Behavioral signal layer (per lesson session) ─────────────────────────────

enum class LessonSignalType {
    ENGAGEMENT,
    FRUSTRATION,
    CURIOSITY,
    INDEPENDENCE,
    RECOVERY
}

data class LessonSignal(
    val type: LessonSignalType,
    val strength: Float,                      // 0.0 – 1.0
    val observedAtEpochMillis: Long,
    val source: LessonSignalSource,
    val atomId: String?,                      // optional atom attribution
    val failureType: FailureType?             // set when frustration/recovery maps to failure
)

enum class LessonSignalSource {
    TEACHER_OBSERVATION,
    TILE_COMPLETION_REFLECTION,
    STUDENT_SHELL_INFERENCE,
    PROMPT_LEVEL_DELTA                        // e.g. L1 → L3 on same atom
}

// ── Memory hooks (narrative + Student simulation) ────────────────────────────

data class MemoryHook(
    val emotionalAnchor: String,              // moment to remember emotionally
    val narrativeSeedPhrase: String,          // StoryGraph continuity seed
    val npcReactionTrigger: String?,          // Spark / companion line trigger
    val worldDriftBias: WorldDriftBias?,      // optional drift nudge
    val repeatExposureSuggestion: String?     // "retry label hunt with 2 objects"
)

enum class WorldDriftBias {
    GALLERY_VITALITY_UP,
    WORLD_QUIETNESS_UP,
    TEMPORAL_DISTORTION_UP,
    THEME_INTENSITY_UP
}

// ── Cross-domain coupling (system rules, not lesson text) ────────────────────

data class DomainCouplingRule(
    val id: String,
    val sourceDomainId: CurriculumDomainId,
    val targetDomainId: CurriculumDomainId,
    val rule: String,                         // human-readable
    val compassEffect: String,                // e.g. "reduces effective cognitive capacity"
    val planGenerationBias: String?,          // tile selection hint
    val signalModifier: Float?                // multiplier on target-domain frustration threshold
)

// ── Session evidence (one tile execution) ────────────────────────────────────

data class LessonSessionEvidence(
    val sessionId: String,
    val intentTileId: String,
    val lessonPatternId: String,
    val startedAtEpochMillis: Long,
    val completedAtEpochMillis: Long?,
    val difficultyReached: DifficultyLevel,
    val atomOutcomes: List<AtomOutcome>,
    val signals: List<LessonSignal>,
    val failureEncountered: FailureType?,
    val recoveryApplied: Boolean,
    val promptLevelSummary: String?           // from starter-lessons evidence model
)

data class AtomOutcome(
    val atomId: String,
    val capabilityId: CapabilityId,
    val difficulty: DifficultyLevel,
    val success: Boolean,
    val promptLevel: String?,                 // model | verbal | gesture | independent
    val notes: String?
)

// ── Childhood Compass aggregates (heat map inputs) ───────────────────────────

data class CompassSnapshot(
    val computedAtEpochMillis: Long,
    val windowDays: Int,
    val domainHealth: Map<CurriculumDomainId, DomainHealth>,
    val capabilityGrowth: Map<CapabilityId, CapabilityTrend>,
    val independenceTrend: Float,             // L3/L4 atom success rate delta
    val emotionalLoadTrend: Float,            // frustration + overload signal density
    val curiosityTrend: Float,
    val recoveryTrend: Float,                 // recovery signals / failure encounters
    val narrativeRichness: Float              // memory hooks + story graph continuity depth
)

data class DomainHealth(
    val domainId: CurriculumDomainId,
    val sunlightStatus: SunlightStatus,       // Compass UX label
    val atomCoverage: Float,                  // distinct atoms touched / atoms in domain library
    val failureDensity: Float,
    val capabilityBalance: Float              // evenness across capabilities in domain
)

enum class SunlightStatus {
    FREQUENT,
    BALANCED,
    NEEDS_MORE_SUNLIGHT,
    QUIET_THIS_MONTH
}

data class CapabilityTrend(
    val capabilityId: CapabilityId,
    val sessionsObserved: Int,
    val highestDifficultyReached: DifficultyLevel,
    val independenceRate: Float,              // L3/L4 successes
    val lastEvidenceAtEpochMillis: Long?
)
```

---

## 3. Room persistence sketch (Teacher Edition)

Normalized tables for queryable Compass analytics. Full lesson prose stays in markdown or blob JSON on tiles.

| Table | Primary key | Notes |
|-------|-------------|-------|
| `curriculum_domains` | `id` | Seed 7 rows |
| `curriculum_subcategories` | `id` | FK → domain |
| `lesson_patterns` | `id` | FK → domain |
| `developmental_capabilities` | `id` | Cross-domain FK via join |
| `skill_atoms` | `id` | FK → pattern, capability |
| `failure_signatures` | `id` | FK → domain, type |
| `domain_coupling_rules` | `id` | Static seed |
| `intent_tiles` | `id` | See Intent Tile contract |
| `intent_tile_atoms` | `(tile_id, atom_id)` | M:N |
| `intent_tile_capabilities` | `(tile_id, capability_id)` | M:N |
| `lesson_sessions` | `session_id` | One expedition tile run |
| `lesson_session_signals` | `id` | FK → session |
| `atom_outcomes` | `id` | FK → session, atom |
| `compass_snapshots` | `id` | JSON blob for aggregates |

Student Edition may mirror a **subset** (`LessonSignal`, `MemoryHook`, `FailureType`) into existing `behavior_signals` / story graph — see §5.

---

## 4. Intent Tile embedding

Intent tiles reference the OS layer; they do not duplicate curriculum prose.

Required new fields (detail in [INTENT_TILE_CONTRACT.md](INTENT_TILE_CONTRACT.md)):

- `lesson_pattern_id`
- `skill_atom_ids[]`
- `capability_ids[]` (denormalized from atoms for Compass queries)
- `lesson_signals[]` (template defaults + session overrides)
- `memory_hooks` (aligned with `MemoryHook`)
- `failure_signatures[]` (subset relevant to this tile)
- `default_difficulty: L1 | L2 | L3 | L4`

Existing `story_hooks` remain; `memory_hooks` extend narrative continuity into Student `StoryFragment.continuityThread` and `callbackLine`.

---

## 5. Student simulation mapping

| Curriculum OS | Student Edition (implemented) |
|---------------|-------------------------------|
| `LessonSignal.FRUSTRATION` + `FailureType` | `EmotionalImpactSpine` valence/intensity |
| `LessonSignal.ENGAGEMENT` / `CURIOSITY` | `BehaviorSignals` weekly counters |
| `LessonSignal.INDEPENDENCE` | `NpcMemoryCompressor` anchor promotion |
| `LessonSignal.RECOVERY` | `WorldEventType.ABSENCE_RETURNED`, recovery narrative |
| `MemoryHook.narrativeSeedPhrase` | `StoryFragment.continuityThread` |
| `MemoryHook.emotionalAnchor` | `StoryFragment.emotionalImpact` |
| `WorldDriftBias` | `WorldDriftEngine` gallery/quietness/temporal |
| Tile completion → `MemoryEvent` | `WorldOrchestrator.dispatch` via [TILE_EVENT_MAPPING.md](TILE_EVENT_MAPPING.md) |

---

## 6. Compass heat map (Teacher UI — future)

Panel reads `CompassSnapshot`, not raw lesson lists:

| Row | Source |
|-----|--------|
| Domain Health | `domainHealth` + `SunlightStatus` |
| Capability Growth | `capabilityGrowth` trends |
| Independence Trend | `independenceTrend` |
| Emotional Load | `emotionalLoadTrend` |
| Curiosity Trend | `curiosityTrend` |
| Recovery Trend | `recoveryTrend` |

Philosophy encoded: **balanced life capabilities**, not checklist completion.

---

## 7. Seed data policy

1. **Domains / subcategories:** Import from `curriculum-taxonomy.md` (stable IDs).
2. **Lesson patterns:** One row per pattern in `curriculum-of-life.md` (7 sections).
3. **Capabilities:** Start with catalog in §2 `CapabilityId`; grow without renaming IDs.
4. **Skill atoms:** Atomize on demand per [CURRICULUM_ATOMIZATION_GUIDE.md](../../phoenix-forge-classroom-teacher-edition/docs/CURRICULUM_ATOMIZATION_GUIDE.md); do not bulk-generate hundreds upfront.
5. **Coupling rules:** Seed ~12 cross-domain rules (emotion→cognitive, physical→social, etc.).
6. **Failure signatures:** Five `FailureType` × domain-specific observables from curriculum teacher cues.

---

## 8. Versioning

- Schema `version: "1.0"` on serialized snapshots and tile payloads.
- Capability and atom IDs are **immutable** once published; deprecate with `supersededBy` rather than rename.
- Curriculum prose edits do not bump schema version; atom graph edits do.

---

## 9. Related documents

- [CURRICULUM_ATOMIZATION_GUIDE.md](../../phoenix-forge-classroom-teacher-edition/docs/CURRICULUM_ATOMIZATION_GUIDE.md)
- [INTENT_TILE_CONTRACT.md](INTENT_TILE_CONTRACT.md)
- [CHILDHOOD_COMPASS_ENGINE.md](../../phoenix-forge-classroom-teacher-edition/docs/CHILDHOOD_COMPASS_ENGINE.md)
- [TILE_EVENT_MAPPING.md](TILE_EVENT_MAPPING.md)
- [curriculum-of-life.md](../../phoenix-forge-classroom-teacher-edition/docs/curriculum-of-life.md) — canonical, do not fork
