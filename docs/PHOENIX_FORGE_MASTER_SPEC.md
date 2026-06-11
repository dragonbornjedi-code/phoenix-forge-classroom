# Phoenix Forge — Master Architecture Specification
**Version:** 1.0 | **Date:** 2026-06-09
**Purpose:** Complete system spec for Gemini/Cursor coding sessions.
Paste this entire document as context before any coding task.

---

## HONEST ASSESSMENT: Prior Plan vs Gemini Critique

### Gemini was CORRECT on 3 failures — accept all three:

| # | Failure | Why it's real |
|---|---------|--------------|
| A | `syncVersion` integer = data loss | Two offline devices both increment to v2. Syncthing sees hash conflict. Last write wins. Ezra loses either game progress OR lesson progress. **Fatal.** |
| B | Flat JSON on tablet = teacher data exposed | Behavioral notes, scores, diagnostics sitting on child's device. Privacy violation. Data lost on device reset. **Unacceptable for open-source release.** |
| C | IP scanning in Godot = DHCP flaky | Tablet wakes up with new DHCP lease. Hardcoded IPs miss it. Live sync fails silently. **Unreliable.** |

### Original plan was CORRECT on these — keep them:
- Syncthing as the file transport layer ✅
- Forge Profile APK as the single source of truth / authority ✅
- File contract model (no Gradle/runtime dependencies between apps) ✅
- LAN-first, offline-capable architecture ✅
- Three-autoload cap in Godot (HeroCatalog, ForgeImport, ForgeBridge) ✅

### Gemini's spec is INCOMPLETE on these — this doc fills the gaps:
- Event log compaction strategy (unbounded logs will bloat forever)
- Logical clocks for event ordering (wall clock unreliable on child's tablet)
- Godot event writer implementation (how does the game actually write events?)
- mDNS discovery in Godot without a native plugin (practical workaround)
- Exact Room schema and DAO contracts
- Syncthing folder structure (precise paths)

---

## SYSTEM OVERVIEW

```
╔══════════════════════════════════════════════════════════════════╗
║              SYNCTHING TRANSPORT LAYER (LAN / offline)           ║
║         /PhoenixForge/sync/ — shared across all devices          ║
╚══════════════════╤═══════════════════════════╤═══════════════════╝
                   │                           │
    ┌──────────────▼──────────────┐ ┌──────────▼──────────────────┐
    │    FORGE PROFILE APK        │ │    TEACHER EDITION APK       │
    │    (tablet — Ezra's device) │ │    (steward phone/laptop)    │
    │                             │ │                              │
    │  • Event Aggregator         │ │  • Lesson Manifest Creator   │
    │  • Room DB (local state)    │ │  • PROTECTED data viewer     │
    │  • LAN HTTP server :7433    │ │  • Student roster            │
    │  • mDNS broadcaster         │ │  • Reads ALL scopes          │
    │  • PUBLIC state only        │ └──────────────────────────────┘
    │    served to student apps   │
    └──────────┬──────────────────┘
               │  LAN HTTP (live session only)
    ┌──────────▼──────────────┐  ┌─────────────────────────────────┐
    │  STUDENT EDITION APK    │  │  PHOENIX FORGE WORLD (Godot 4.6)│
    │  (tablet)               │  │  (laptop)                       │
    │                         │  │                                 │
    │  • Reads PUBLIC state   │  │  • Reads PUBLIC state           │
    │  • Reads LESSON manifest│  │  • Reads LESSON manifest        │
    │  • Writes PUBLIC events │  │  • Writes PUBLIC events         │
    │  • CANNOT read PROTECTED│  │  • CANNOT read PROTECTED        │
    └─────────────────────────┘  └─────────────────────────────────┘
```

---

## COMPONENT ROLES

### 1. Forge Profile APK (`com.phoenixforge.profile`)
- **Single source of truth** for all student profile data
- Runs a foreground Android Service that:
  - Watches the Syncthing events folder for new event files
  - Ingests events into local Room database
  - Computes current aggregate state (StateProjector)
  - Exposes LAN HTTP server (NanoHTTPD, port 7433)
  - Registers via mDNS as `_forgeprofile._tcp.local`
- Periodically compacts old events into snapshots
- Lives on **Ezra's tablet** (primary) + **steward's phone** (secondary)

### 2. Student Edition APK (`com.phoenixforge.student`)
- Reads current PUBLIC state from Forge Profile (via ContentProvider locally, or LAN HTTP)
- Reads LESSON manifest from sync folder
- Writes student action events to sync folder (`EVT_STUDENT_*.json`)
- **Never reads PROTECTED events**
- Lives on **Ezra's tablet**

### 3. Teacher Edition APK (`com.phoenixforge.classroom.teacher`)
- Creates and pushes Lesson Manifests
- Reads ALL event scopes (PUBLIC + LESSON + PROTECTED)
- Writes PROTECTED events (behavioral notes, scores, assessments)
- Shows full student roster and per-student timeline
- Lives on **steward's phone or laptop**

### 4. Phoenix Forge World — Godot 4.6 (`phoenix-forge-world`)
- Reads PUBLIC state from Forge Profile (LAN HTTP when available, local file fallback)
- Reads LESSON manifest from sync folder
- Writes game events to sync folder via `ForgeImport.write_event()`
- **Never reads PROTECTED events**
- Lives on **laptop**

---

## DATA SCOPE PARTITIONING

```
┌─────────────────────────────────────────────────────────────┐
│ Scope.PUBLIC                                                 │
│   Level, XP, active quest, inventory, avatar,               │
│   companion names, unlock flags, achievement badges         │
│   Readable by: ALL four apps                                │
├─────────────────────────────────────────────────────────────┤
│ Scope.LESSON                                                 │
│   Day plan, narrative text, unlock conditions,              │
│   quest assignments, story segments                         │
│   Written by: Teacher Edition only                          │
│   Readable by: Student Edition, Forge World, Teacher Edition│
│   NOT readable by: Forge Profile internals (pass-through)   │
├─────────────────────────────────────────────────────────────┤
│ Scope.PROTECTED                                             │
│   Behavioral notes, accuracy timelines, attention scores,   │
│   diagnostic flags, steward private assessments            │
│   Written by: Teacher Edition only                          │
│   Readable by: Teacher Edition + Forge Profile only         │
│   NEVER readable by: Student Edition, Forge World           │
└─────────────────────────────────────────────────────────────┘
```

---

## SYNCTHING FOLDER STRUCTURE

Every device that participates syncs this entire tree:

```
/PhoenixForge/sync/
├── profiles/
│   └── {uid}/                        ← one folder per student (e.g. "ezra")
│       ├── events/
│       │   ├── EVT_{deviceId}_{epochMs}_{seq}.json
│       │   └── ...                   ← append-only, never modified
│       ├── manifests/
│       │   ├── lesson_manifest_{date}.json
│       │   └── ...
│       └── snapshots/
│           └── snapshot_{epochMs}.json  ← compacted state, written by Forge Profile
├── roster/
│   └── roster.json                   ← list of student UIDs (Teacher Edition writes)
└── .device_registry/
    └── {deviceId}.json               ← each device registers itself here
```

**Syncthing rules:**
- All devices share the FULL tree (Syncthing handles selective access via ignore rules)
- Laptop adds `.stignore` to exclude PROTECTED events from Student folders if needed
- No device ever modifies an existing event file — append only
- Conflict resolution is unnecessary because event filenames are globally unique

---

## EVENT SCHEMA

Every action from every app writes one of these JSON files:

```json
{
  "schemaVersion": 1,
  "eventId": "EVT_ezra-tablet_1749470400000_0003",
  "eventType": "XP_EARNED",
  "scope": "PUBLIC",
  "actorApp": "student_edition",
  "actorDeviceId": "ezra-tablet",
  "studentUid": "fixture-ezra",
  "logicalClock": 42,
  "epochMs": 1749470400000,
  "payload": {
    "xpAmount": 50,
    "reason": "completed_lesson_1_literacy",
    "questId": "hv_ignavarr_tutorial_01"
  }
}
```

**Event ID format:** `EVT_{deviceId}_{epochMs}_{seqPadded4}`
- `deviceId` = stable string set once per device install (UUID stored in SharedPreferences / Godot user:// )
- `epochMs` = Unix timestamp milliseconds
- `seqPadded4` = per-device counter that resets on each millisecond (handles burst writes)
- **Result:** Globally unique with zero coordination. Two devices offline writing simultaneously never collide.

**Logical clock rule:**
- Each device keeps its own Lamport counter in local storage
- On write: `logicalClock = max(localCounter, highestSeenClockInIncomingEvents) + 1`
- On replay: events sorted first by `logicalClock`, tie-broken by `deviceId` alphabetically
- **Result:** Deterministic ordering even when device clocks are wrong

### Event Types Reference

| eventType | scope | Writer | Payload keys |
|-----------|-------|--------|-------------|
| `XP_EARNED` | PUBLIC | Student Ed, Forge World | `xpAmount`, `reason`, `questId` |
| `ITEM_UNLOCKED` | PUBLIC | Student Ed, Forge World | `itemId`, `itemType` |
| `COMPANION_EARNED` | PUBLIC | Forge World | `companionId`, `companionName` |
| `QUEST_STARTED` | PUBLIC | Student Ed, Forge World | `questId` |
| `QUEST_COMPLETED` | PUBLIC | Student Ed, Forge World | `questId`, `score` |
| `LEVEL_UP` | PUBLIC | Forge Profile (computed) | `newLevel`, `previousLevel` |
| `AVATAR_UPDATED` | PUBLIC | Forge Profile | `avatarFields` (delta) |
| `LESSON_ASSIGNED` | LESSON | Teacher Edition | `manifestId`, `dayIndex`, `unlockConditions` |
| `LESSON_STARTED` | LESSON | Student Ed, Forge World | `manifestId`, `dayIndex` |
| `LESSON_COMPLETED` | LESSON | Student Ed, Forge World | `manifestId`, `dayIndex`, `durationMs` |
| `BEHAVIORAL_NOTE` | PROTECTED | Teacher Edition | `noteText`, `category`, `severity` |
| `ACCURACY_SCORE` | PROTECTED | Teacher Edition | `moduleId`, `scorePercent`, `attemptCount` |
| `ATTENTION_FLAG` | PROTECTED | Teacher Edition | `sessionId`, `flag`, `context` |

---

## LESSON MANIFEST SCHEMA

```json
{
  "schemaVersion": 1,
  "manifestId": "manifest_ezra_20260612",
  "studentUid": "fixture-ezra",
  "createdByDeviceId": "josh-phone",
  "createdEpochMs": 1749470400000,
  "validFromDate": "2026-06-12",
  "validToDate": "2026-06-14",
  "days": [
    {
      "dayIndex": 0,
      "date": "2026-06-12",
      "narrativeTitle": "The Dragon Wakes",
      "narrativeText": "Ignavarr stirs in the forge...",
      "quests": ["hv_ignavarr_tutorial_01"],
      "unlockConditions": [],
      "rewardItems": ["potion_health_01"]
    },
    {
      "dayIndex": 1,
      "date": "2026-06-13",
      "narrativeTitle": "The Second Trial",
      "narrativeText": "...",
      "quests": ["hv_ignavarr_tutorial_02"],
      "unlockConditions": [
        {"type": "QUEST_COMPLETED", "questId": "hv_ignavarr_tutorial_01"}
      ],
      "rewardItems": []
    }
  ]
}
```

Student Edition and Forge World read this file. They check system date against `dayIndex.date`, auto-unlock quests, and log `LESSON_STARTED` / `LESSON_COMPLETED` events back to the sync folder. No internet. No steward present. Works at mom's house.

---

## AGGREGATE STATE (What Forge Profile Computes)

After replaying all events, Forge Profile Room DB holds:

```kotlin
// PUBLIC — served to all apps
data class ProfilePublicState(
    val uid: String,
    val forgeName: String,
    val level: Int,
    val xp: Int,
    val avatar: AvatarState,
    val inventory: List<ItemState>,
    val companions: List<CompanionState>,
    val completedQuests: List<String>,
    val activeQuests: List<String>,
    val unlockedFlags: Map<String, Boolean>,
    val currentLesson: LessonManifestRef?,
    val lastUpdatedEpochMs: Long,
    val aggregatedFromEventCount: Int
)

// PROTECTED — Teacher Edition only
data class ProfileProtectedState(
    val uid: String,
    val behavioralNotes: List<BehavioralNote>,
    val accuracyTimeline: List<AccuracyEntry>,
    val attentionFlags: List<AttentionFlag>,
    val diagnosticSummary: DiagnosticSummary
)
```

---

## LAN HTTP API (Forge Profile APK — NanoHTTPD, port 7433)

```
GET  /api/v1/ping
     → 200 { "deviceId": "ezra-tablet", "studentUid": "fixture-ezra", "serverTimeMs": 0 }

GET  /api/v1/profile/public/{uid}
     → 200 ProfilePublicState JSON
     → 404 if uid not found

GET  /api/v1/manifest/{uid}/active
     → 200 LessonManifest JSON (today's manifest)
     → 204 if no active manifest

POST /api/v1/event
     Body: EventRecord JSON
     → 202 Accepted (writes event file to sync folder, then ingest)
     → Used by Godot when tablet is on same LAN (avoids waiting for Syncthing)

GET  /api/v1/roster                   ← Teacher Edition only
     → 200 Array<ProfilePublicState>
```

**Authentication:** None for v1 (LAN-only, trusted network). Add PIN in v2.

---

## mDNS DISCOVERY (No IP Guessing)

### Android side (Forge Profile APK):
```kotlin
// Registers as _forgeprofile._tcp.local on LAN
val serviceInfo = NsdServiceInfo().apply {
    serviceName = "ForgeProfile-${studentUid}"
    serviceType = "_forgeprofile._tcp."
    port = 7433
}
nsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, listener)
```

### Godot side — NO native plugin needed:
Godot cannot do mDNS natively. Use a **companion helper script** on the laptop:

```bash
# /home/joshuar/PhoenixForge/scripts/find_forge_profile.sh
# Runs once on laptop boot via systemd user service.
# Scans LAN for _forgeprofile._tcp, writes result to file that Godot reads.

avahi-browse -t -r _forgeprofile._tcp 2>/dev/null \
  | grep "address\|port" \
  | awk '/address/{ip=$NF} /port/{print ip":"$NF}' \
  | head -1 > /home/joshuar/PhoenixForge/.forge_profile_server.txt
```

In `forge_import.gd`:
```gdscript
func _discover_lan_server() -> String:
    var hint_path := "/home/joshuar/PhoenixForge/.forge_profile_server.txt"
    if FileAccess.file_exists(hint_path):
        var f := FileAccess.open(hint_path, FileAccess.READ)
        var addr := f.get_line().strip_edges()
        f.close()
        if addr.contains(":"):
            return "http://" + addr
    return ""  # fallback to Syncthing file
```

No plugin. No IP guessing. Avahi (pre-installed on Ubuntu) writes the address; Godot reads it.

---

## EVENT COMPACTION (Prevents Log Bloat)

Forge Profile APK runs compaction when:
- Event count for a student UID exceeds **500 events**, OR
- On each Monday morning (weekly scheduled job via WorkManager)

Compaction process:
1. Read all events for UID up to yesterday
2. Project them into a `ProfilePublicState` + `ProfileProtectedState` snapshot
3. Write `snapshots/snapshot_{epochMs}.json` to sync folder
4. Delete event files older than the snapshot's timestamp
5. Update Forge Profile Room DB with snapshot as new baseline

**Result:** Log folder stays small. Syncthing never has to sync thousands of tiny files.

---

## GODOT — ForgeImport Event Writer

Add to `scripts/autoload/forge_import.gd`:

```gdscript
# --- Event writing ---
const SYNC_EVENTS_BASE := "/home/joshuar/PhoenixForge/sync/profiles/"
var _device_id: String = ""
var _logical_clock: int = 0
var _event_seq: int = 0


func _init_device_id() -> void:
    var id_path := "user://device_id.txt"
    if FileAccess.file_exists(id_path):
        var f := FileAccess.open(id_path, FileAccess.READ)
        _device_id = f.get_line().strip_edges()
        f.close()
    else:
        _device_id = "godot-" + str(randi())
        var f := FileAccess.open(id_path, FileAccess.WRITE)
        f.store_line(_device_id)
        f.close()


func write_event(event_type: String, scope: String, payload: Dictionary) -> void:
    if bundle.is_empty():
        return
    var uid: String = str(bundle.get("profile", {}).get("uid", "unknown"))
    var epoch_ms: int = int(Time.get_unix_time_from_system() * 1000)
    _event_seq += 1
    var event_id := "EVT_%s_%d_%04d" % [_device_id, epoch_ms, _event_seq]
    _logical_clock += 1

    var event := {
        "schemaVersion": 1,
        "eventId": event_id,
        "eventType": event_type,
        "scope": scope,
        "actorApp": "forge_world",
        "actorDeviceId": _device_id,
        "studentUid": uid,
        "logicalClock": _logical_clock,
        "epochMs": epoch_ms,
        "payload": payload
    }

    var events_dir := SYNC_EVENTS_BASE + uid + "/events/"
    DirAccess.make_dir_recursive_absolute(events_dir)
    var event_path := events_dir + event_id + ".json"
    var f := FileAccess.open(event_path, FileAccess.WRITE)
    if f == null:
        push_warning("ForgeImport: cannot write event — sync folder missing?")
        return
    f.store_string(JSON.stringify(event, "\t"))
    f.close()

    # Also POST to LAN server if available (faster than waiting for Syncthing)
    var server_url := _discover_lan_server()
    if server_url != "":
        var http := HTTPRequest.new()
        add_child(http)
        http.request(
            server_url + "/api/v1/event",
            ["Content-Type: application/json"],
            HTTPClient.METHOD_POST,
            JSON.stringify(event)
        )
```

**Usage anywhere in Godot:**
```gdscript
# When player earns XP:
ForgeImport.write_event("XP_EARNED", "PUBLIC", {"xpAmount": 50, "reason": "defeated_boss"})

# When quest completes:
ForgeImport.write_event("QUEST_COMPLETED", "PUBLIC", {"questId": "hv_ignavarr_tutorial_01", "score": 95})
```

---

## MANIFEST READER (Godot)

Add to `forge_import.gd`:

```gdscript
const SYNC_MANIFESTS_BASE := "/home/joshuar/PhoenixForge/sync/profiles/"

func active_lesson_manifest() -> Dictionary:
    if bundle.is_empty():
        return {}
    var uid: String = str(bundle.get("profile", {}).get("uid", "unknown"))
    var manifest_dir := SYNC_MANIFESTS_BASE + uid + "/manifests/"
    var today := Time.get_date_string_from_system()  # "2026-06-09"
    
    # Find manifest valid for today
    var dir := DirAccess.open(manifest_dir)
    if dir == null:
        return {}
    dir.list_dir_begin()
    var fname := dir.get_next()
    while fname != "":
        if fname.begins_with("lesson_manifest_") and fname.ends_with(".json"):
            var text := FileAccess.get_file_as_string(manifest_dir + fname)
            var parsed: Variant = JSON.parse_string(text)
            if typeof(parsed) == TYPE_DICTIONARY:
                var from: String = str(parsed.get("validFromDate", ""))
                var to: String = str(parsed.get("validToDate", ""))
                if today >= from and today <= to:
                    return parsed
        fname = dir.get_next()
    return {}
```

---

## FORGE PROFILE APK — KEY ANDROID MODULES

```
forge-profile-core/
├── data/
│   ├── db/
│   │   ├── ForgeProfileDatabase.kt      ← Room database
│   │   ├── EventRecordDao.kt            ← Insert + query events
│   │   ├── ProfileSnapshotDao.kt        ← Read/write snapshots
│   │   └── entities/
│   │       ├── EventRecordEntity.kt
│   │       ├── PublicStateEntity.kt
│   │       └── ProtectedStateEntity.kt
│   ├── sync/
│   │   ├── EventFileWatcher.kt          ← Watches Syncthing folder via FileObserver
│   │   ├── EventIngester.kt             ← Reads new event JSONs → Room DB
│   │   └── EventCompactor.kt           ← Weekly compaction job (WorkManager)
│   └── projection/
│       ├── StateProjector.kt            ← Replays events → aggregate state
│       └── LessonManifestReader.kt      ← Reads active manifest from sync folder
├── network/
│   ├── ForgeProfileSyncServer.kt        ← NanoHTTPD HTTP server
│   └── NsdRegistrar.kt                 ← mDNS registration
└── service/
    └── ForgeProfileService.kt           ← Foreground service (keeps server alive)
```

### Room EventRecordEntity:
```kotlin
@Entity(tableName = "events")
data class EventRecordEntity(
    @PrimaryKey val eventId: String,
    val eventType: String,
    val scope: String,           // "PUBLIC" | "LESSON" | "PROTECTED"
    val actorApp: String,
    val actorDeviceId: String,
    val studentUid: String,
    val logicalClock: Long,
    val epochMs: Long,
    val payloadJson: String,
    val ingestedAtMs: Long = System.currentTimeMillis()
)
```

---

## STUDENT EDITION APK — KEY CHANGES

```
forge-classroom/ (student module)
├── data/
│   ├── ForgeProfileRepository.kt    ← Reads PUBLIC state from Forge Profile
│   │                                   (ContentProvider if same device,
│   │                                    LAN HTTP if cross-device)
│   ├── EventWriter.kt               ← Writes event JSONs to Syncthing folder
│   └── ManifestReader.kt           ← Reads active lesson manifest
└── ui/
    └── lesson/
        └── LessonViewModel.kt       ← Loads today's manifest, shows day plan
```

### EventWriter (Student Edition):
```kotlin
class EventWriter @Inject constructor(
    private val deviceId: String,
    private val syncBasePath: String  // "/sdcard/PhoenixForge/sync/profiles/"
) {
    private var logicalClock = AtomicLong(0)
    private var seqCounter = AtomicInteger(0)

    fun writeEvent(
        studentUid: String,
        eventType: String,
        scope: EventScope,
        payload: JSONObject
    ) {
        val epochMs = System.currentTimeMillis()
        val seq = seqCounter.incrementAndGet()
        val eventId = "EVT_${deviceId}_${epochMs}_${seq.toString().padStart(4, '0')}"
        val clock = logicalClock.incrementAndGet()

        val event = JSONObject().apply {
            put("schemaVersion", 1)
            put("eventId", eventId)
            put("eventType", eventType)
            put("scope", scope.name)
            put("actorApp", "student_edition")
            put("actorDeviceId", deviceId)
            put("studentUid", studentUid)
            put("logicalClock", clock)
            put("epochMs", epochMs)
            put("payload", payload)
        }

        val dir = File("$syncBasePath/$studentUid/events/")
        dir.mkdirs()
        File(dir, "$eventId.json").writeText(event.toString(2))
    }
}
```

---

## TEACHER EDITION APK — KEY MODULES

```
forge-classroom/ (teacher module)
├── data/
│   ├── StudentRosterRepository.kt   ← Reads roster.json from sync folder
│   ├── EventReader.kt               ← Reads ALL scopes per student
│   ├── StateProjector.kt            ← Same projection logic as Forge Profile
│   ├── ProtectedEventWriter.kt     ← Writes PROTECTED events
│   └── ManifestWriter.kt           ← Creates + writes lesson_manifest_{date}.json
└── ui/
    ├── roster/
    │   └── StudentsFragment.kt      ← List of all students
    ├── student/
    │   ├── StudentDetailFragment.kt ← Full profile (public + protected)
    │   └── TimelineFragment.kt      ← Chronological event log
    └── lesson/
        └── LessonPlanFragment.kt    ← Create + push 1-3 day manifest
```

---

## SYNCTHING SETUP — EXACT STEPS

### On laptop (Ubuntu):
```bash
sudo apt install syncthing avahi-utils
sudo systemctl enable syncthing@$USER
sudo systemctl start syncthing@$USER
# Web UI: http://127.0.0.1:8384

mkdir -p /home/joshuar/PhoenixForge/sync/profiles/fixture-ezra/{events,manifests,snapshots}
mkdir -p /home/joshuar/PhoenixForge/sync/roster
mkdir -p /home/joshuar/PhoenixForge/sync/.device_registry
```

Add shared folder in Syncthing UI:
- Path: `/home/joshuar/PhoenixForge/sync`
- Label: `phoenix-forge-sync`
- Share with: tablet device ID + phone device ID

### On tablet/phone (Android):
- Install **Syncthing-Fork** from F-Droid (not the deprecated Google Play version)
- Add device: scan laptop QR code
- Accept share invitation for `phoenix-forge-sync`
- Local path on Android: `/sdcard/PhoenixForge/sync/`

### mDNS companion script (laptop — runs on boot):
```bash
# /home/joshuar/PhoenixForge/scripts/find_forge_profile.sh
#!/bin/bash
while true; do
    avahi-browse -t -r _forgeprofile._tcp 2>/dev/null \
      | grep -A3 "hostname\|address\|port" \
      | awk '/address \[/{gsub(/[^0-9.]/,"",$NF); ip=$NF}
             /port \[/{gsub(/[^0-9]/,"",$NF); 
                        if(ip && $NF) print ip":"$NF}' \
      | head -1 \
      > /home/joshuar/PhoenixForge/.forge_profile_server.txt
    sleep 30
done
```

```bash
# Enable as systemd user service:
mkdir -p ~/.config/systemd/user/
cat > ~/.config/systemd/user/forge-mdns-watcher.service << 'EOF'
[Unit]
Description=Phoenix Forge mDNS watcher

[Service]
ExecStart=/home/joshuar/PhoenixForge/scripts/find_forge_profile.sh
Restart=always

[Install]
WantedBy=default.target
EOF

systemctl --user enable forge-mdns-watcher
systemctl --user start forge-mdns-watcher
```

---

## BUILD ORDER (P0 → P4)

### P0 — Foundation (do today, ~1 hour)
1. Set up Syncthing on laptop + tablet per steps above
2. Create folder structure: `/PhoenixForge/sync/profiles/fixture-ezra/{events,manifests,snapshots}`
3. Install Avahi + start mDNS watcher service on laptop
4. Drop `PHOENIX_FORGE_MASTER_SPEC.md` into both repos as `docs/MASTER_SPEC.md`

### P1 — Forge Profile APK core (~1 week, Cursor sessions)
1. Define `EventRecordEntity`, `EventScope` enum, `EventRecord` domain model
2. Implement `EventFileWatcher` (FileObserver on `/sdcard/PhoenixForge/sync/profiles/{uid}/events/`)
3. Implement `EventIngester` (reads new JSON files → Room DB)
4. Implement `StateProjector` (replays events → `ProfilePublicState`)
5. Implement `ForgeProfileSyncServer` (NanoHTTPD, endpoints above)
6. Implement `NsdRegistrar`
7. Wire into `ForgeProfileService` (foreground service)

### P2 — Student Edition writes (~3 sessions)
1. Add `EventWriter` class
2. Replace all `DataStore`/`SharedPreferences` profile writes with `EventWriter.writeEvent()`
3. Add `ManifestReader` — load today's lesson manifest on app open
4. Show day narrative + quest list from manifest

### P3 — Forge World Godot (~2 sessions)
1. Add `_init_device_id()` to `forge_import.gd`
2. Add `write_event()` to `forge_import.gd`
3. Add `active_lesson_manifest()` to `forge_import.gd`
4. Add mDNS hint file reader to `forge_import.gd`
5. Wire `write_event()` calls into `hero_spawner.gd` (level up, companion earned)
6. Add manifest-driven quest display to `hearth_entry.tscn`

### P4 — Teacher Edition (~1 week)
1. Add `StudentRosterRepository`
2. Add `EventReader` with scope filter (PUBLIC-only for student view, ALL for teacher view)
3. Build `StudentsFragment` (roster list)
4. Build `StudentDetailFragment` (full timeline)
5. Build `LessonPlanFragment` (create + write manifest file)
6. Add `ProtectedEventWriter` (behavioral notes)

---

## ARCHITECTURE LAWS (Never Violate)

1. **No Gradle dependencies between apps.** Forge Profile never imports Student Edition's code. They share only JSON files.
2. **Events are append-only.** Never modify or delete an event file (except after compaction snapshot is confirmed written).
3. **Godot 3-autoload cap:** `HeroCatalog`, `ForgeImport`, `ForgeBridge` — no additions.
4. **Scope enforcement:** PROTECTED events never leave Teacher Edition / Forge Profile. Student Edition and Forge World cannot query them.
5. **No Home Assistant runtime dependencies in Forge World.**
6. **No franchise IP** (pokemon, sonic, disney, dragon_ball, marvel, spiderman, nintendo, numberblocks, etc.) without explicit `ASSET_SALVAGE_INDEX.md` audit row.
7. **Syncthing is transport only.** Business logic never assumes Syncthing is running. Apps fall back to reading local files.
8. **LAN HTTP is enhancement only.** Godot and Student Edition work fully offline without it.

---

## FEEDING THIS INTO GEMINI / CURSOR

**For Gemini coding sessions**, paste this entire document, then append:
> "Your task is [specific module, e.g. 'implement EventWriter.kt for Student Edition']. Follow the schema and API contracts exactly as defined in this spec. Do not deviate from the data scope partitioning rules."

**For Cursor CLI**, add this file to your workspace:
```bash
cp PHOENIX_FORGE_MASTER_SPEC.md /var/lib/phoenix-ai/workspace/phoenix-forge-world/docs/
cp PHOENIX_FORGE_MASTER_SPEC.md /var/lib/phoenix-ai/workspace/phoenix-forge-classroom/docs/
```

Then in `.cursor/rules` or your Cursor system prompt file, reference it:
```
Always read docs/PHOENIX_FORGE_MASTER_SPEC.md before writing any code for this project.
```
