# Handoff 1.02 — Lesson manifest push (Teacher → Student)

**Master step:** `1.01` (transport doc) · `1.02` (Teacher write) · `1.03` (Student read)  
**Lane:** `phoenix-forge-classroom` only — file handoff, no Godot, no LAN HTTP in this slice.

---

## Canonical artifact

| Field | Value |
|-------|--------|
| **Filename** | `lesson_manifest_{yyyy-MM-dd}.json` |
| **Scope** | LESSON (Teacher writes; Student + Forge World read) |
| **Schema** | `docs/PHOENIX_FORGE_MASTER_SPEC.md` — LESSON MANIFEST SCHEMA |
| **Stub extension** | One `days[]` row per expedition tile (same `date`, sequential `dayIndex`) so Student can list tile titles without full `quest_export` |

---

## On-disk path (Syncthing tree)

```text
/sdcard/PhoenixForge/sync/profiles/{studentUid}/manifests/lesson_manifest_{yyyy-MM-dd}.json
```

Alternate Android path (same inode on most devices):

```text
/storage/emulated/0/PhoenixForge/sync/profiles/{studentUid}/manifests/lesson_manifest_{yyyy-MM-dd}.json
```

| Segment | Meaning |
|---------|---------|
| `{studentUid}` | Forge Profile UID of the **child** profile (UUID from import), **not** the steward profile |
| `{yyyy-MM-dd}` | Local date on the steward device when **Push today's stack** runs |

**Syncthing:** share `/PhoenixForge/sync/` (or phone equivalent) across steward phone + student tablet.  
**Append-only events** (`EVT_*` + `logicalClock`) live in sibling folder `.../events/` — not part of 1.02.

---

## Steward phone smoke (no Ezra reveal)

Use a **test child profile** UID until Josh says **"Ezra reveal"**.

1. **Student UID** — On the student session (test profile): Settings → import child Forge Profile → note profile ID, **or** Teacher → Student Snapshot (same phone, Student signed in).
2. **Teacher push** — Expedition Board → Start day (▶) → **Push today's stack** → confirm path in dialog.
3. **Same-phone shortcut** — If both APKs share storage, open Student → **Today's Expedition** (no copy needed).
4. **Cross-device fallback** — Manual copy:

```bash
ADB_SERIAL=$(adb devices | awk '/device$/{print $1; exit}')
STUDENT_UID="<child-profile-uuid>"
DATE=$(date +%Y-%m-%d)

adb -s "$ADB_SERIAL" shell mkdir -p "/sdcard/PhoenixForge/sync/profiles/${STUDENT_UID}/manifests"

adb -s "$ADB_SERIAL" push \
  "/tmp/lesson_manifest_${DATE}.json" \
  "/sdcard/PhoenixForge/sync/profiles/${STUDENT_UID}/manifests/lesson_manifest_${DATE}.json"
```

5. **Verify Student** — Student Edition → Today's Expedition → titles from today's PLANNED / ACTIVE / SENT tiles appear.
6. **Event smoke** — Open a tile → **Start mission** / **Mark complete** → verify `EVT_*.json` under `.../profiles/{studentUid}/events/` (see below).

---

## PUBLIC events (Student EventWriter — P2)

| Field | Value |
|-------|--------|
| **Directory** | `.../profiles/{studentUid}/events/` |
| **Filename** | `EVT_{deviceId}_{epochMs}_{seq}.json` |
| **Types** | `QUEST_STARTED`, `QUEST_COMPLETED` |
| **Scope** | `PUBLIC` only |
| **Ordering** | `logicalClock` — no `syncVersion` |

```bash
adb shell ls "/sdcard/PhoenixForge/sync/profiles/${STUDENT_UID}/events/"
```

---

## `{studentUid}` resolution (Teacher app)

Order used by `ManifestPushTargetResolver`:

1. Student Edition ContentProvider snapshot (`StudentSyncReader`) when Student is signed in on the same device.
2. Last successful push UID in Teacher SharedPreferences (`manifest_push_student_uid`).

If both fail, push shows an error — import child profile on Student first.

---

## Deferred (not 1.02)

- Full `quest_export.schema.json` pipeline
- `ChariotExport` → Student receive
- Forge Profile LAN HTTP / event ingest (`1.04`)
- Daily-brief popup (Student 3.x)

---

## Related

- [shared/quests/README.md](../shared/quests/README.md) — quest pipeline layers
- [00_MASTER_ROADMAP.md](roadmaps/00_MASTER_ROADMAP.md) — steps `1.01`–`1.06`
