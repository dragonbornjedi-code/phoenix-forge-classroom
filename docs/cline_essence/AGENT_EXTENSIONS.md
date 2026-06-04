# Cline Essence — Agent Extensions

Extends [MASTER_PROTOCOL.md](MASTER_PROTOCOL.md). Load master protocol first.

---

## Implementer agent

**Threat model focus:** Data loss, wrong module path, skipping master step, breaking ContentProvider contract.

| Guardrail | Verification |
|-----------|--------------|
| Edit only `phoenix-forge-classroom-forge-profile/*` for APK code | `gradlew :module:assembleDebug` |
| Room migrations version bump | App cold start [VERIFIED] |
| Child shell never deletes CMOS | STUDENT_TEACHER_BOUNDARY review |

**Forensic summary must include:** module name, APK package, master step delta.

---

## Documentation / alignment agent

**Threat model focus:** Phantom files, UME-era drift, Teacher “no APK” lies, duplicate north-star docs.

| Guardrail | Verification |
|-----------|--------------|
| No new audit markdown | Update CENSUS + ALIGNMENT_REPORT only |
| Atlas/Authority same status | Grep `teacher-edition/android` as build instruction = 0 hits |
| Code-lag labeled | Not marked CONTRADICTION |

**Verification:** `./scripts/cline-essence-drift-check.sh` exit 0.

---

## Reviewer / merge agent

**Threat model focus:** [UNTESTED] marked DONE, cross-app work before 0.75, Profile Godot export breaking Student import.

| Gate | Rule |
|------|------|
| Pre-merge | Drift script + assembleDebug all three modules |
| Step bump | Requires checklist in master roadmap for that step all [VERIFIED] |
| Rollback | Every PR notes revert command |

---

## Subagent dispatch rule

Parent agent passes: `master_step`, `products_touched[]`, `verification_commands_run[]`. Subagent returns with verification tags only — no untagged DONE.
