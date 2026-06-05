# PHOENIX FORGE CLASSROOM — UNIVERSAL AI MASTER INSTRUCTIONS
**Paste this at the start of any session with any AI agent (Cursor, Claude, Gemini, Copilot, Cline, etc.)**

> Save this file to: `docs/UNIVERSAL_AI_INSTRUCTIONS.md` AND `/mnt/digital-home/vault/PHOENIX_FORGE_UNIVERSAL_AI_INSTRUCTIONS.md`
> Never edit the structure of this document without also updating `docs/AI_AGENT_UNIVERSAL_RULES.md`.
> Phantom files never existed — names listed in §5.8 and §10 are forbidden citations only.

---

## 0. WHO YOU ARE AND WHAT THIS IS

You are a coding agent working on **Phoenix Forge Classroom**, a three-app Android monorepo
building a Sovereign Childhood Archive for a child named **Ezra**.

**Three products (canonical names — never rename):**
- **Forge Profile** — Ezra's lifelong identity record (`forge-profile-app` + `forge-profile-core`)
- **Phoenix Forge Classroom Student Edition** — child experience shell (`student-app`)
- **Phoenix Forge Classroom Teacher Edition** — parent expedition board (`teacher-app`)

**All three APKs live under one Gradle root:**
```
phoenix-forge-classroom-forge-profile/
  forge-profile-app/
  forge-profile-core/
  student-app/
  teacher-app/
```

**Forbidden build paths** (empty placeholders — never use as targets):
- `phoenix-forge-classroom-teacher-edition/android/`
- `phoenix-forge-classroom-student-edition/android/`

**Project root:** `/var/lib/phoenix-ai/workspace/phoenix-forge-classroom`
**Vault backup:** `/mnt/digital-home/vault/` (Git-backed; save final artifacts here too)

---

## 1. ENVIRONMENT — SET IN EVERY SHELL BEFORE ANY COMMAND

```bash
export JAVA_HOME=/home/joshuar/.local/jdk
export PATH="$JAVA_HOME/bin:$PATH"
export PROJECT=/var/lib/phoenix-ai/workspace/phoenix-forge-classroom
export GRADLE_ROOT=$PROJECT/phoenix-forge-classroom-forge-profile
cd "$PROJECT"
```

**Verify environment is clean before any work:**
```bash
git status                            # must show nothing uncommitted
git log -1 --format='%H %s'          # note this hash — it is your baseline
./scripts/cline-essence-drift-check.sh  # must exit 0
```

If `git status` shows uncommitted changes: **commit them first** (see Section 6).
If drift check fails: **fix it before proceeding** (see Section 8).

---

## 2. READ-FIRST PROTOCOL — MANDATORY BEFORE TOUCHING ANYTHING

Read these files **in this order** before writing a single line of code or docs.
Never skip this. These files are your ground truth.

```
1.  registry/phoenix-forge-classroom.yaml        → current_step (your starting point)
2.  docs/roadmaps/00_MASTER_ROADMAP.md           → full ledger, your target step row
3.  docs/cline_essence/SKILL.md                  → workflow, verification contract
4.  docs/cline_essence/MASTER_PROTOCOL.md        → Phoenix bindings, forbidden actions
5.  docs/CONSTITUTION.md                         → Tier 0 pointer
6.  docs/DEPLOYMENT_REALITY.md                   → APK paths, install order
7.  docs/AUTHORITY_AND_REALITY_MAPPING.md        → what is actually implemented vs documented
8.  The matching sub-roadmap for your target step:
      Profile steps  → docs/roadmaps/01_FORGE_PROFILE_ROADMAP.md
      Student steps  → docs/roadmaps/02_STUDENT_EDITION_ROADMAP.md
      Teacher steps  → docs/roadmaps/03_TEACHER_EDITION_ROADMAP.md
      Cross steps    → docs/roadmaps/04_CROSS_APP_INTEGRATION_ROADMAP.md
```

**Quick commands to read them:**
```bash
cat registry/phoenix-forge-classroom.yaml | grep current_step
grep "Current step" docs/roadmaps/00_MASTER_ROADMAP.md | head -2
```

---

## 3. STEP EXECUTION PROTOCOL — ONE STEP AT A TIME

### 3.1 Identify your target step
```bash
# Find the next pending step
grep "pending" docs/roadmaps/00_MASTER_ROADMAP.md | head -1
```
The output gives you `X.XX | App | Deliverable | Verification | pending`.
That is your **only** target for this work unit.

### 3.2 Create a worktree
```bash
STEP="X.XX"   # replace with actual step number, e.g. 0.13
git worktree add ~/.config/superpowers/worktrees/phoenix-forge-classroom/feat-$STEP \
  -b feat/$STEP
# All implementation work happens inside the worktree
WT=~/.config/superpowers/worktrees/phoenix-forge-classroom/feat-$STEP
```

### 3.3 Implement inside the worktree
- Read the deliverable from the roadmap row
- Read the verification method from the roadmap row
- Read linked docs from the matching sub-roadmap
- Make changes only inside the worktree path

### 3.4 Run the verification proof
Every step has a verification column. Use it literally:

| Verification column says | What to run |
|--------------------------|-------------|
| `gradle` / `assembleDebug` | `./gradlew :module:assembleDebug` → must say BUILD SUCCESSFUL |
| `Phone` | Build APK + attempt `adb install` if device connected; otherwise verify nav code exists + build succeeds |
| `strings.xml` | `grep -r "target_string" .../res/values/ --include="*.xml"` |
| `grep UI` | `grep -r "DigitalHouse\|Digital House" .../src --include="*.kt" --include="*.xml"` |
| `Read paths` | `ls -la` on documented paths; confirm file exists |
| `File` | File must exist on disk at documented path |
| `Log/no crash` | Build + adb logcat -d \| grep -i exception \| head -20 |
| `chmod + run` | `chmod +x script && ./script` → must exit 0 |
| `Checklist` | Every sub-step in the range must be `[VERIFIED]` |
| `Compile` | `./gradlew :module:compileDebugKotlin` |
| `Doc pass` | Read file; confirm content matches specification |

**If verification fails: fix the code, do not update the roadmap. Never mark [VERIFIED] without proof.**

### 3.5 Run build verification after every Kotlin change
```bash
cd $GRADLE_ROOT
export JAVA_HOME=/home/joshuar/.local/jdk && export PATH="$JAVA_HOME/bin:$PATH"
./gradlew :forge-profile-app:assembleDebug \
          :student-app:assembleDebug \
          :teacher-app:assembleDebug 2>&1 | tail -20
# All three must say BUILD SUCCESSFUL
# If any fails, fix before proceeding
```

### 3.6 Update documentation (cascade — see Section 5)
Before marking verified, apply all documentation cascades triggered by your changes.

### 3.7 Mark verified and sync to PROJECT
```bash
# In the WORKTREE: update roadmap row
# Change: | X.XX | ... | pending |
# To:     | X.XX | ... | [VERIFIED] |
# Also update the header: "Current step: X.XX — next: X+0.01"

# In the WORKTREE: update registry
# Change: current_step: "X.XX-previous"
# To:     current_step: "X.XX"

# Copy to PROJECT
cp $WT/docs/roadmaps/00_MASTER_ROADMAP.md $PROJECT/docs/roadmaps/00_MASTER_ROADMAP.md
cp $WT/registry/phoenix-forge-classroom.yaml $PROJECT/registry/phoenix-forge-classroom.yaml
# Copy any other changed files
```

### 3.8 Drift check + commit + clean up worktree
```bash
cd $PROJECT
./scripts/cline-essence-drift-check.sh    # must exit 0

git add -A
git commit \
  --trailer "Co-authored-by: Cursor <cursoragent@cursor.com>" \
  -m "COMMIT_MESSAGE_PER_STEP_TABLE_BELOW"

git log -1 --format='%H %s'   # record this hash

git worktree remove --force ~/.config/superpowers/worktrees/phoenix-forge-classroom/feat-$STEP
git branch -d feat/$STEP 2>/dev/null || true
git worktree list              # must show only main PROJECT worktree
```

**Commit message format per type:**
| Step type | Message format |
|-----------|----------------|
| Doc/roadmap only | `chore(roadmap): mark X.XX [VERIFIED], bump current_step to X.XX` |
| Code + roadmap | `feat(app): short description (step X.XX [VERIFIED])` |
| Fix | `fix(module): short description (step X.XX [VERIFIED])` |
| Strings/labels | `chore(strings): short description (step X.XX [VERIFIED])` |
| Script | `chore(scripts): short description (step X.XX [VERIFIED])` |

---

## 4. GIT DISCIPLINE — NON-NEGOTIABLE

| Rule | Detail |
|------|--------|
| **One commit per step** | Never batch multiple verified steps into one commit |
| **Commit immediately** | Never leave a [VERIFIED] step uncommitted |
| **Worktree per step** | Create / remove worktree for each step |
| **Never commit to main from worktree** | Copy files to $PROJECT, commit there |
| **No orphaned worktrees** | `git worktree list` must show only main after each step |
| **Baseline before work** | `git status` must be clean before starting any step |

---

## 5. DOCUMENTATION CASCADE MATRIX — ALWAYS APPLY

When you change File A, you **must** also update all files in the "Also update" column.
Apply all cascades **before** committing. Never commit a half-cascade.

### 5.1 Roadmap + Registry cascade (every step)
| Changed | Also update |
|---------|-------------|
| `docs/roadmaps/00_MASTER_ROADMAP.md` row status | `registry/phoenix-forge-classroom.yaml` → `current_step` |
| `docs/roadmaps/00_MASTER_ROADMAP.md` header | Same registry + both `Current step:` lines in roadmap header |

### 5.2 Kotlin code cascade
| Changed | Also update |
|---------|-------------|
| Any `.kt` file in `forge-profile-app/` or `forge-profile-core/` | `docs/AUTHORITY_AND_REALITY_MAPPING.md` → Forge Profile table row(s) for affected subsystem |
| Any `.kt` file in `forge-profile-app/` or `forge-profile-core/` | `docs/PHOENIX_FORGE_SYSTEM_ATLAS.md` → Part 2 Forge Profile table |
| Any `.kt` file in `student-app/` | `docs/AUTHORITY_AND_REALITY_MAPPING.md` → Student Edition table row(s) |
| Any `.kt` file in `student-app/` | `docs/PHOENIX_FORGE_SYSTEM_ATLAS.md` → Part 2 Student Edition table |
| Any `.kt` file in `teacher-app/` | `docs/AUTHORITY_AND_REALITY_MAPPING.md` → Teacher Edition table row(s) |
| Any `.kt` file in `teacher-app/` | `docs/PHOENIX_FORGE_SYSTEM_ATLAS.md` → Part 2 Teacher Edition table |
| Data model / Room entity changed | `docs/FORGEPROFILE_SPEC.md` if it touches ForgeProfile schema |
| New Kotlin module created | `docs/REPOSITORY_CENSUS_AND_CONNECTIONS.md` → Layer 8 entry |
| New Kotlin module created | `phoenix-forge-classroom-forge-profile/README.md` |

### 5.3 String / label cascade
| Changed | Also update |
|---------|-------------|
| `strings.xml` launcher label | `docs/DEPLOYMENT_REALITY.md` → canonical app names table |
| `strings.xml` launcher label | `docs/REPOSITORY_CENSUS_AND_CONNECTIONS.md` → product name note at top |
| `strings.xml` — removed "Digital House" | `docs/DOCUMENTATION_ALIGNMENT_REPORT.md` "Known intentional gaps" or alignment status |

### 5.4 APK path / build path cascade
| Changed | Also update |
|---------|-------------|
| APK output path changes | `docs/DEPLOYMENT_REALITY.md` → "What actually builds today" table |
| APK output path changes | `scripts/install-phone-apks.sh` → install commands |
| APK output path changes | `docs/PHOENIX_FORGE_SYSTEM_ATLAS.md` → "Runnable modules" table |

### 5.5 Script cascade
| Changed | Also update |
|---------|-------------|
| `scripts/install-phone-apks.sh` changed | `docs/DEPLOYMENT_REALITY.md` → install commands block |
| New script added under `scripts/` | `scripts/README.md` |
| Systemd file added/changed | `docs/roadmaps/00_MASTER_ROADMAP.md` step 0.20 note; `scripts/README.md` |

### 5.6 Contract doc cascade
| Changed | Also update |
|---------|-------------|
| Any file in `docs/contracts/` | `docs/AUTHORITY_AND_REALITY_MAPPING.md` → cross-cutting table |
| Any file in `docs/contracts/` | `docs/roadmaps/04_CROSS_APP_INTEGRATION_ROADMAP.md` → contracts section |
| `docs/contracts/reference-tiles/secret-label-decoder.yaml` | `docs/PHOENIX_FORGE_SYSTEM_ATLAS.md` Part 3 |

### 5.7 AUTHORITY_AND_REALITY_MAPPING state values (use exactly these)
`IMPLEMENTED` | `PARTIAL` | `DOCUMENTED` | `DEPRECATED` | `UNKNOWN` | `ORPHANED`

Rule: **Exists ≠ Integrated.** File present but unwired = `PARTIAL`, not `IMPLEMENTED`.
Rule: Never upgrade a subsystem from `PARTIAL` to `IMPLEMENTED` without build + integration proof.

### 5.8 What you must NEVER do to documentation
- Never create a new `.md` file unless a roadmap step explicitly requires it
- Never rename existing documentation files
- Never create a competing roadmap or timeline — only `00_MASTER_ROADMAP.md`
- Never add sub-roadmap rows with step IDs — sub-roadmaps are indexes only
- Never cite phantom files: `END_GOAL_AND_NORTH_STAR.md`, `ALIGNMENT_AUDIT_2026-06-04.md`, `student-edition/docs/experience-boundary.md`, `teacher-edition/docs/product-spec.md`
- Never mark a step `[VERIFIED]` or `[TESTED]` without a proof command that ran successfully

---

## 6. HANDLING UNCOMMITTED CHANGES AT SESSION START

If `git status` shows uncommitted files from a prior session:

```bash
# Step 1: identify what changed
git diff --stat

# Step 2: check if it's roadmap + registry only (from a prior verified step)
git diff docs/roadmaps/00_MASTER_ROADMAP.md | grep "VERIFIED\|Current step"
git diff registry/phoenix-forge-classroom.yaml | grep "current_step"

# Step 3: if yes, commit immediately before starting new work
git add docs/roadmaps/00_MASTER_ROADMAP.md registry/phoenix-forge-classroom.yaml
git commit -m "chore(roadmap): mark X.XX [VERIFIED], bump current_step to X.XX"

# Step 4: run drift check to confirm clean state
./scripts/cline-essence-drift-check.sh    # must exit 0

# Step 5: only then begin new step work
```

If there are uncommitted Kotlin files from an incomplete prior step:
```bash
# Determine if the prior step was verified or abandoned
grep "X.XX" docs/roadmaps/00_MASTER_ROADMAP.md

# If pending: evaluate whether to complete or revert
# If the build was broken: git stash or git checkout -- . to clean up first
# Never carry broken uncommitted code into a new step
```

---

## 7. ROADMAP NUMBERING RULES — NEVER VIOLATE

| Rule | Detail |
|------|--------|
| Format | `X.XX` only — `0.13`, `2.07`, `4.51` — never `0.13-A` or `0.13a` |
| Range | `0.00` to `5.00` inclusive |
| Increment | Each new step ≥ +0.01 above the highest existing step |
| Forward only | Never renumber or reuse a step number |
| New work | Add the row to `00_MASTER_ROADMAP.md` FIRST, then code |
| Insert point | New steps go before `5.00`; use gaps like `4.57`–`4.91` |
| Sub-roadmaps | `01_FORGE_PROFILE_ROADMAP.md` etc. are **indexes only** — no step IDs, no scheduling |
| Done | A row is done when it is `[VERIFIED]` or `[TESTED]` AND the commit exists |

**Adding a new step:**
```
1. Add row to 00_MASTER_ROADMAP.md (next free decimal)
2. Run drift check
3. Add file hints to matching sub-roadmap (01–04)
4. Then implement
```

---

## 8. DRIFT CHECK — WHAT IT MEANS

```bash
./scripts/cline-essence-drift-check.sh
```

- Exit 0 = clean; proceed
- Non-zero = something is misaligned

**Most common drift failures and fixes:**

| Failure message | Fix |
|-----------------|-----|
| `current_step mismatch` | Sync `registry/phoenix-forge-classroom.yaml` `current_step` to match roadmap header |
| `forbidden build path referenced` | Remove any reference to `*/android/` as a build target |
| `phantom file referenced` | Remove citation of files that don't exist |
| `roadmap header mismatch` | Both `Current step:` lines in `00_MASTER_ROADMAP.md` header must agree |

Run drift check: (a) after any doc/path edit, (b) before any commit, (c) at the start of every session.

---

## 9. BUILD COMMANDS REFERENCE

```bash
# Set environment first (always)
export JAVA_HOME=/home/joshuar/.local/jdk && export PATH="$JAVA_HOME/bin:$PATH"
cd /var/lib/phoenix-ai/workspace/phoenix-forge-classroom/phoenix-forge-classroom-forge-profile

# Build all three APKs
./gradlew :forge-profile-app:assembleDebug :student-app:assembleDebug :teacher-app:assembleDebug

# Build one APK
./gradlew :forge-profile-app:assembleDebug
./gradlew :student-app:assembleDebug
./gradlew :teacher-app:assembleDebug

# Compile only (no APK, faster for type-check)
./gradlew :forge-profile-app:compileDebugKotlin
./gradlew :student-app:compileDebugKotlin
./gradlew :teacher-app:compileDebugKotlin

# Clean build (use when cache seems wrong)
./gradlew clean :forge-profile-app:assembleDebug

# Install to connected phone (adb must show device)
SERIAL=$(adb devices | grep device | grep -v List | awk '{print $1}' | head -1)
BASE=/var/lib/phoenix-ai/workspace/phoenix-forge-classroom/phoenix-forge-classroom-forge-profile
adb -s $SERIAL install -r $BASE/forge-profile-app/build/outputs/apk/debug/forge-profile-app-debug.apk
adb -s $SERIAL install -r $BASE/student-app/build/outputs/apk/debug/student-app-debug.apk
adb -s $SERIAL install -r $BASE/teacher-app/build/outputs/apk/debug/teacher-app-debug.apk

# Or use the install script
./scripts/install-phone-apks.sh $SERIAL

# Check logcat for crashes
adb logcat -s AndroidRuntime:E | head -40
```

**APK locations (verified 2026-06-04):**
```
forge-profile-app/build/outputs/apk/debug/forge-profile-app-debug.apk  (~20 MB)
student-app/build/outputs/apk/debug/student-app-debug.apk               (~16 MB)
teacher-app/build/outputs/apk/debug/teacher-app-debug.apk               (~16 MB)
```

---

## 10. FORBIDDEN ACTIONS — HARD STOPS

If you are about to do any of the following, **stop immediately** and report instead of doing it.

| Forbidden | Why |
|-----------|-----|
| Write to `phoenix-forge-classroom-teacher-edition/android/` | Empty placeholder — not a build target |
| Write to `phoenix-forge-classroom-student-edition/android/` | Empty placeholder — not a build target |
| Create a new `*_ROADMAP.md` file | Only one master roadmap (`00_MASTER_ROADMAP.md`) plus 4 existing sub-indexes |
| Create a new `*_AUDIT*.md` or `*_ALIGNMENT*.md` file | Update existing `DOCUMENTATION_ALIGNMENT_REPORT.md` instead |
| Use P0/P1/P1a as task scheduling IDs | Map to master decimals only |
| Mark `[VERIFIED]` without running the verification command | Integrity violation |
| Skip a step to work on a higher one | Forward-only, no skipping |
| Work on P3 vision depth while P0 of any app is still failing | Master order-of-operations |
| Batch two steps into one commit | One commit per step, always |
| Reference a phantom file | See phantom list in `docs/AI_AGENT_UNIVERSAL_RULES.md` |
| Rename any of the three products | Names are constitutional |
| Delete or overwrite `WorldOrchestrator.kt` | Protect simulation spine |
| Delete or overwrite the CMOS data layer | No child shell may delete validated CMOS |

---

## 11. CROSS-DOMAIN CONTEXT RULE (Digital Home mesh)

When implementing steps that touch data structures, always check for cross-domain links:
- Does this data shape relate to a Godot export? → Check `docs/GODOT_MIGRATION_STRATEGY.md`
- Does this touch ContentProvider? → Check `docs/contracts/INTENT_TILE_CONTRACT.md`
- Does this touch avatar data? → Check `docs/FORGEPROFILE_SPEC.md` § Avatar Studio
- Does this touch memory events? → Check `docs/contracts/MEMORY_EVENT_CONTRACT.md`
- Does this touch teacher→student flow? → Check `docs/STUDENT_TEACHER_BOUNDARY.md`

The Sovereign Deck (NAS), Godot, and home automation live in separate repos.
This repo only owns the three APKs and their contracts. Reference Tailscale IPs
(100.x.x.x) are for the mesh but not in-scope for Phoenix Forge Classroom code.

---

## 12. SESSION END PROTOCOL — MANDATORY BEFORE STOPPING

**Every session must end with this checklist completed in order:**

```
☐ 1. All started steps are either [VERIFIED]+committed OR reverted (no half-done steps in tree)
☐ 2. git status shows clean tree (nothing uncommitted)
☐ 3. ./scripts/cline-essence-drift-check.sh exits 0
☐ 4. git log -1 shows the correct latest commit
☐ 5. git worktree list shows only the main PROJECT worktree
☐ 6. All documentation cascades from this session have been applied (Section 5)
☐ 7. SESSION BRIDGE BLOCK output (below)
```

**Then output the SESSION BRIDGE BLOCK** (copy this template, fill in the blanks):

```
╔══════════════════════════════════════════════════════════════════════╗
║  PHOENIX FORGE SESSION BRIDGE — paste this to start next session     ║
╠══════════════════════════════════════════════════════════════════════╣
║  LAST COMMIT:  [HASH]  [MESSAGE]                                      ║
║  STEP VERIFIED: [X.XX]  —  NEXT STEP: [X+0.01]                       ║
║  NEXT DELIVERABLE: [copy from roadmap row]                            ║
║  NEXT VERIFICATION: [copy from roadmap row]                           ║
║  CLEAN TREE: yes / DRIFT CHECK: exit 0 / WORKTREES: none             ║
║  DEFERRED FROM THIS SESSION: [none | describe what was not finished]  ║
╠══════════════════════════════════════════════════════════════════════╣
║  PASTE THIS INTO CURSOR / CLAUDE / GEMINI:                            ║
╚══════════════════════════════════════════════════════════════════════╝

/subagent-driven-development follow \
  /var/lib/phoenix-ai/workspace/phoenix-forge-classroom/docs/cline_essence \
  /var/lib/phoenix-ai/workspace/phoenix-forge-classroom/docs/roadmaps \
  advance roadmap step [NEXT_STEP] only — stop and report after the verified commit

## Environment — set in every shell before any command
export JAVA_HOME=/home/joshuar/.local/jdk
export PATH="$JAVA_HOME/bin:$PATH"
export PROJECT=/var/lib/phoenix-ai/workspace/phoenix-forge-classroom
cd "$PROJECT"

## Baseline check (do this FIRST, before any work)
git status                              # must be clean
git log -1 --format='%H %s'            # must show [HASH] / [MESSAGE]
./scripts/cline-essence-drift-check.sh # must exit 0

## Step [NEXT_STEP] spec
App:          [copy App column from roadmap]
Deliverable:  [copy Deliverable column from roadmap]
Verification: [copy Verification column from roadmap — run it literally]
Sub-roadmap:  [docs/roadmaps/0X_*_ROADMAP.md section for this step]

## Documentation cascade (apply before committing)
[List the specific cascade rules from Section 5 that apply to this step]

## Git discipline
After [VERIFIED]:
  git add -A
  git commit --trailer "Co-authored-by: Cursor <cursoragent@cursor.com>" \
    -m "[commit message per type table]"
  ./scripts/cline-essence-drift-check.sh   # exit 0 required
  git worktree remove --force ...
  git log -1 --format='%H %s'              # report this hash

Stop here. Report commit hash + verification proof. Do not continue to [NEXT_STEP + 0.01].
```

---

## 13. QUICK REFERENCE — CURRENT PROJECT STATE

*(Update this block at the end of every session)*

```
Last known good commit:  de20c36  feat(profile): all bottom-nav tabs wired (step 0.12 [VERIFIED])
Current step in registry: 0.12
Next pending step:        0.13  ← UPDATE THIS EACH SESSION
Next deliverable:         Launcher label final (Student Edition)
Next verification:        strings.xml
Roadmap sub-index:        docs/roadmaps/02_STUDENT_EDITION_ROADMAP.md
```

*(If this block is stale, run: `grep "Current step" docs/roadmaps/00_MASTER_ROADMAP.md`
and `cat registry/phoenix-forge-classroom.yaml | grep current_step`)*

---

## 14. MILESTONE SUMMARY (orientation at a glance)

```
0.00  Genesis complete                → 0.01–0.06 all [VERIFIED]
0.25  Device P0: 3 APKs on phone     → 0.26–0.49 all pending
0.50  Standalone P1: usable apps     → 0.51–0.74 all pending
0.75  Contracts: Kotlin types wired  → 0.76–0.85 all pending
1.00  Integration scaffold           → 1.01–1.06 all pending
1.50  Loop 50%: tile→quest chain     → 1.51–1.55 all pending
2.00  Loop proven: full E2E          → 2.01–2.07 all pending  ← irreversible milestone
2.50  Profile depth + Avatar         → 2.51–2.57 all pending
3.00  Student depth + Hearthhome     → 3.01–3.06 all pending
3.50  Teacher depth + Compass        → 3.51–3.58 all pending
4.00  CMOS bridge                    → 4.01–4.06 all pending
4.50  Chronicle tier                 → 4.51–4.56 all pending
5.00  FINISH LINE                    → everything [VERIFIED]
```

**Order of operations law (never violate):**
```
All three APKs build (0.07–0.09) BEFORE device P0 (0.25)
Device P0 (0.25) BEFORE standalone P1 (0.50)
Contracts (0.75) BEFORE deep UI polish
Loop proven (2.00) BEFORE P3 vision depth (2.50+)
```

---

## 15. EMERGENCY PROCEDURES

### Build breaks after your change
```bash
# Identify what broke
cd $GRADLE_ROOT && ./gradlew :MODULE:assembleDebug 2>&1 | grep -E "error:|Error" | head -20

# If it was working before your change: revert to baseline
git diff --stat   # see what you changed
git checkout -- path/to/broken/file.kt

# Rebuild to confirm revert works
./gradlew :MODULE:assembleDebug 2>&1 | tail -5
```

### Drift check fails after your session
```bash
# Find the mismatch
./scripts/cline-essence-drift-check.sh 2>&1

# Most common: registry doesn't match roadmap header
cat registry/phoenix-forge-classroom.yaml | grep current_step
grep "Current step" docs/roadmaps/00_MASTER_ROADMAP.md | head -2
# Fix the one that's wrong; commit
```

### Orphaned worktree detected
```bash
git worktree list    # shows extra entries
git worktree remove --force ~/.config/superpowers/worktrees/phoenix-forge-classroom/feat-X.XX
git branch -d feat/X.XX 2>/dev/null || true
git worktree list    # confirm clean
```

### Prior session left half-finished Kotlin work
```bash
# Option A: finish the step now (verify + commit)
# Option B: revert cleanly
git checkout -- .   # reverts all uncommitted changes
git clean -fd       # removes untracked files (careful — check git status first)
git status          # confirm clean
```

---

*Document version: 1.0 · 2026-06-04 · Save to docs/ AND /mnt/digital-home/vault/*
*To update this document: edit in place — never create a replacement file.*
