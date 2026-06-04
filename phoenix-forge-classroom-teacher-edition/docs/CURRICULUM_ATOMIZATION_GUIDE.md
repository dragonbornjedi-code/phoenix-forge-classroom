# Curriculum Atomization Guide

How to turn [curriculum-of-life.md](curriculum-of-life.md) into operational data **without rewriting or replacing** canonical curriculum text.

**Canonical sources (do not compete with this file):**

| Document | Role |
|----------|------|
| [curriculum-of-life.md](curriculum-of-life.md) | Pedagogy, lesson patterns, metrics, cues, supports |
| [curriculum-taxonomy.md](curriculum-taxonomy.md) | Subcategories, tags, lesson seeds |
| [starter-lessons-pack-01.md](starter-lessons-pack-01.md) | Reference lesson objects (objective, steps, evidence) |
| [CURRICULUM_OS_SCHEMA.md](../../docs/contracts/CURRICULUM_OS_SCHEMA.md) | Kotlin/Room types and Compass aggregates |

This guide only defines **how to decompose** existing content into the OS layer.

---

## 1. Hierarchy

```text
CurriculumDomain (7 fixed sections)
  └── CurriculumSubcategory (from taxonomy)
        └── LessonPattern (from curriculum-of-life "Lesson patterns")
              └── SkillAtom (smallest measurable unit)
                    ├── DevelopmentalCapability (what Compass tracks longitudinally)
                    ├── DifficultyLevel L1–L4
                    ├── LessonSignal (engagement, frustration, curiosity, independence, recovery)
                    ├── EvidenceType (observation, photo, prompt level, …)
                    ├── FailureSignature (overload | confusion | fatigue | avoidance | skill_gap)
                    └── MemoryHook (narrative + simulation continuity)
```

**Rule:** One lesson pattern maps to **many** skill atoms. One capability is exercised by **many** lesson patterns.

Example:

| Lesson pattern | Capability (shared) |
|----------------|---------------------|
| Label Hunt | `VISUAL_DISCRIMINATION`, `SYMBOL_TO_OBJECT_MAPPING` |
| Lego Manual Mission | `MULTI_STEP_INSTRUCTION_MEMORY`, `VISUAL_DISCRIMINATION` |
| System Map | `PATTERN_GENERALIZATION`, `OBJECT_FUNCTION_MAPPING` |

The Compass measures capability trajectories, not "did we run Label Hunt this week?"

---

## 2. When to atomize

Atomize when:

- A lesson pattern enters the **Curriculum Library** as an Intent Tile template.
- A **starter lesson** is promoted to a repeatable tile (see starter-lessons-pack-01).
- Compass shows **noisy sunlight** (frequent lessons but flat capability trend → atoms may be wrong or missing).

Do **not** atomize hundreds of lessons upfront. Atomize **on demand** at tile definition time.

---

## 3. Step-by-step: from Label Hunt to atoms

**Source (curriculum-of-life, §1):** Label Hunt — label objects; find, match, explain.

### Step 1 — Identify capabilities (stable IDs)

| Capability | Why |
|------------|-----|
| `VISUAL_DISCRIMINATION` | Match label to object |
| `SYMBOL_TO_OBJECT_MAPPING` | Word ↔ object |
| `CATEGORY_GROUPING` | Same type of objects |
| `VERBAL_RETRIEVAL` | "Which one says book?" |
| `OBJECT_FUNCTION_MAPPING` | "What job does this do?" |

### Step 2 — Create skill atoms (one observable per atom)

| Atom ID | Capability | L1 success | L4 success |
|---------|------------|------------|------------|
| `label_hunt.match_with_model` | `SYMBOL_TO_OBJECT_MAPPING` | Places label with full model | Independent match, 5+ labels |
| `label_hunt.explain_job` | `OBJECT_FUNCTION_MAPPING` | Echoes adult job phrase | Explains job in own words |
| `label_hunt.retrieval` | `VERBAL_RETRIEVAL` | Points when asked | Names label from object |

Use [CURRICULUM_OS_SCHEMA.md](../../docs/contracts/CURRICULUM_OS_SCHEMA.md) `DifficultyLevel` for full L1–L4 criteria text per atom.

### Step 3 — Map failure signatures (from teacher cues)

| Cue in curriculum | FailureType | Reset |
|-------------------|-------------|-------|
| "Guesses without looking" | `CONFUSION` | "What do your eyes notice?" |
| "Gets stuck" | `SKILL_GAP` or `OVERLOAD` | Reduce step size (L1) |
| "Memorizes but cannot transfer" | `SKILL_GAP` | New object, same pattern (L4 probe) |
| "Frustration rises" | `FATIGUE` / `AVOIDANCE` | Physical version; regulate first |

### Step 4 — Attach lesson signals (session emission)

After a session, emit (teacher observation or reflection):

- `ENGAGEMENT` — stayed with matching task
- `FRUSTRATION` — refusal, throwing labels
- `CURIOSITY` — unprompted "what job does…"
- `INDEPENDENCE` — prompt level → independent on an atom
- `RECOVERY` — returned after pause / Recovery Hub

### Step 5 — Memory hooks (narrative + Student bridge)

| Field | Example |
|-------|---------|
| `emotionalAnchor` | "First time he explained 'light' as wake-up the room" |
| `narrativeSeedPhrase` | "The house learned new words on the walls" |
| `npcReactionTrigger` | Spark: "That label is a secret code only you decoded" |
| `repeatExposureSuggestion` | "3 labels, 3 minutes — low energy window" |

### Step 6 — Register on Intent Tile

See [INTENT_TILE_CONTRACT.md](../../docs/contracts/INTENT_TILE_CONTRACT.md): `skill_atom_ids`, `capability_ids`, `memory_hooks`, `failure_signatures`.

---

## 4. Difficulty progression (universal L1–L4)

Apply the **same** ladder to every atom:

| Level | Name | Parent action | Evidence |
|-------|------|---------------|----------|
| L1 | Guided imitation | Full model, child copies | Prompt level = model |
| L2 | Prompted execution | Verbal/gesture cue, child acts | Prompt level = verbal/gesture |
| L3 | Partial independence | Fade prompts; 2–4 step sequence | Fewer prompts, same task |
| L4 | Transfer / generalization | New object, setting, or person | Success without prior example |

Starter lessons already encode this as "Make easier / Make harder" — map those blocks directly to L1/L2 and L3/L4 criteria on specific atoms.

**Prompt level log** (from starter-lessons): `model | verbal prompt | gesture | independent` → store on `AtomOutcome.promptLevel`.

---

## 5. Cross-domain coupling (plan generation + Compass)

Seed rules in OS schema; reference when interpreting signals:

| Source | Target | Rule |
|--------|--------|------|
| Emotional regulation | Cognitive | Red/yellow state reduces effective working memory — defer hard cognitive tiles |
| Physical mastery | Social | Fatigue increases grab/impulse — prefer turn timer before team build |
| Creative expression | Emotional | Making after upset can process feeling — offer maker tile after recovery |
| Practical life | Autonomy confidence | Routine success boosts willingness on social tiles |
| Ethics / repair | Social | Repair depth supports conflict resolution tiles |

When `LessonSignal.FRUSTRATION` + `FailureType.OVERLOAD` fires in emotional domain, Plan Generation should **not** stack high `EXECUTIVE_FUNCTION` cognitive tiles same session.

---

## 6. Evidence collection (aligned with starter lessons)

Every session should produce at least one:

| Evidence | Stored on |
|----------|-----------|
| Photo of labeled objects | `LessonSessionEvidence` + CMOS asset |
| Prompt level summary | `AtomOutcome` per atom |
| Teacher note | `AtomOutcome.notes` |
| Quick/deep reflection | `LessonSignal` + `MemoryEvent` |

Student Edition missions consume **student-facing mission text only**; evidence and signals stay in Teacher / CMOS.

---

## 7. Weekly Teacher Audit → Compass

Map [curriculum-of-life.md § Weekly Teacher Audit](curriculum-of-life.md) fields:

| Audit field | Compass / OS output |
|-------------|---------------------|
| One win observed | `CapabilityTrend` positive delta |
| One friction point | `FailureType` density |
| One method that worked | `linkedSupportMethods` on next tile |
| One metric to track | Target `SkillAtom` for next week |
| Overload signs | `emotionalLoadTrend` |
| V1.1 adjustment | `default_difficulty` or atom subset change |

---

## 8. Quality checklist (before publishing a tile)

- [ ] `lesson_pattern_id` points to curriculum-of-life pattern name
- [ ] At least 2 `skill_atom_ids` with distinct `capability_id`s
- [ ] Every atom has L1 and L3 success criteria written
- [ ] All five `FailureType`s considered; at least 2 `failure_signatures` attached
- [ ] `memory_hooks.narrativeSeedPhrase` present for Narrative Wrapper
- [ ] `lesson_signals` template documents what teacher should log post-session
- [ ] Student mission text does not leak diagnostic or audit language

---

## 9. What not to do

- Do not create parallel curriculum markdown files for "expansions" — add subcategories to taxonomy when needed; prose stays in curriculum-of-life.
- Do not track lesson completion as the primary Compass metric.
- Do not atomize without a capability link — orphan atoms create noisy drift.
- Do not teach during `FailureType` red-state equivalents — regulation first (curriculum rule = OS rule).

---

## 10. Runtime flow

Full pipeline and authority boundaries: [CURRICULUM_RUNTIME_FLOW.md](../../docs/contracts/CURRICULUM_RUNTIME_FLOW.md).

Next implementation step: reference Intent Tile **Secret Label Decoder** (see runtime doc §6).
