# ForgeProfile Specification: The Childhood DNA

The ForgeProfile is the persistent digital identity of a child within the Phoenix Forge ecosystem. It is the **single source of truth** for **Ezra** — identity, avatar, chronicle, artifacts — across Forge Profile, Student Edition, Teacher Edition, and future **Godot 3D** reflection.

**Runnable today:** `forge-profile-core` (Room + ContentProvider) · **UI:** `forge-profile-app` including **Avatar Studio** (basic; expansion is P1 priority).

## Design Goals
- **Durability:** Survives platform migrations (Android to Godot and beyond).
- **Sovereignty:** 100% offline, local-first, child-owned.
- **The 2046 Test:** Stored in open JSON format readable by any standard utility in 20 years.

## Core Schema

### 1. Identity & State
```json
{
  "uid": "uuid-v4",
  "forgerName": "Ezra",
  "preferredPronouns": "he/him",
  "originDate": "2026-06-02T10:00:00Z",
  "currentStage": "EARLY_DISCOVERY",
  "sparkMaturationTier": 1
}
```

### 2. Bonds (Relational Graph)
Tracks the evolution of shared history with every entity.
```json
"bonds": {
  "spark": { "level": 12, "lastInteraction": "...", "traitFocus": "EMPATHY" },
  "mom": { "sharedEpisodes": 15, "lastVoiceNote": "..." }
}
```

### 3. Identity Threads (Evidence-Based)
Discovered by the **Identity Formation Engine** and confirmed by the parent.
```json
"identityThreads": [
  {
    "id": "thread_builder",
    "archetype": "EMBER",
    "confidence": 0.92,
    "firstSeen": "2026-06-02",
    "evidence": ["artifact_lego_bridge_01", "episode_cardboard_fort"]
  }
]
```

### 4. The Chronicle (Episodic Memory)
The narrative arcs of the child's life.
```json
"chronicle": {
  "chapters": [
    { "id": "ch_summer_26", "title": "The Summer of the Secret Fort", "episodes": ["ep_01", "ep_02"] }
  ],
  "episodes": [
    {
      "id": "ep_01",
      "narrativeTitle": "The Day of the Sky Feather",
      "timestamp": "2026-06-02T14:30:00Z",
      "artifactRefs": ["art_blue_feather_01"],
      "humanVoices": ["dad_commentary_01"],
      "wispAffinities": {"MOSS": 0.8}
    }
  ]
}
```

### 5. Artifact Registry (Legacy Objects)
Maps local media assets to universal world IDs.
```json
"artifacts": [
  {
    "id": "art_blue_feather_01",
    "universalId": "nature_feather_blue",
    "mediaPath": "media/artifacts/2026/06/feather.jpg",
    "checksum": "sha256-..."
  }
]
```

## Avatar Studio (identity anchor — high priority for Ezra)

Avatar is how Ezra **sees himself** in every shell. Studio lives in **Forge Profile**; other apps **read** (and eventually **import**) the same config — they do not fork a separate avatar DB.

### Implemented today (`forge-profile-core`)

```kotlin
// domain/model/ForgeProfile.kt — P0 fields
data class Avatar(
    val id: String,
    val hairType: String,
    val eyeColor: String,
    val skinTone: String,
    val clothingId: String,
    val version: Int,
    val timestamp: Instant
)
```

- **UI:** `AvatarStudioScreen.kt` — preview placeholder, hair/eyes/clothing chips (wire Save fully in P1a).
- **Persistence:** `AvatarEntity` + timeline `AVATAR_UPDATED` events.
- **Export surface:** ContentProvider `/avatar`; Student reads `avatarSummary` via `ForgeProfileImporter`.

### Target: `AvatarConfig` (P1 → P3)

Expand Room + JSON export to this shape so Student, Teacher analytics, and Godot share one contract:

```json
{
  "childId": "ezra",
  "schemaVersion": 2,
  "gender": "male",
  "bodyType": "kid",
  "ageAppearance": 6,
  "skinColor": "#E8B4A0",
  "hair": { "style": "curly", "color": "#3B2314" },
  "eyes": { "style": "round", "color": "#4A6741" },
  "mouth": "soft_smile",
  "nose": "small",
  "faceShape": "round",
  "clothing": { "preset": "explorer_tunic", "primaryColor": "#2D5A27" },
  "accessories": ["backpack_canvas", "wand_wood"],
  "shardLevel": 2,
  "identityThreadBias": { "explorer": 0.7, "builder": 0.5, "artist": 0.3, "regulator": 0.2 },
  "wackyModeUnlocked": false,
  "godotMeshHints": {
    "headScale": 1.0,
    "attachmentSlots": { "hat": "res://avatars/hats/none", "cape": null }
  }
}
```

| Feature | Path stage | Notes |
|---------|------------|-------|
| Layered Compose preview (head/hair/eyes/clothes/accessories) | P1a | Replace emoji placeholder |
| Color pickers + chip categories | P1b | Hair, eyes, face, body, clothing, accessories |
| Auto-save on change | P1b | Already partial via repository |
| Shard tiers 0–6 (age milestones) | P1c | Gates wacky traits |
| Wacky mode (Floating Crown, Rainbow Flames, …) | P2 | Child-safe silly unlocks |
| Thread-biased suggestions | P2 | Tie to Identity Formation Engine |
| Export JSON + share intent | P1b | Student import + file handoff |
| Randomize / reset / history | P1c | `getAvatarHistory()` exists |
| Godot `godotMeshHints` | P3 | See GODOT_MIGRATION_STRATEGY |

**Role-based views (same profile file):**

| Consumer | Sees | Writes |
|----------|------|--------|
| Forge Profile | Full Studio + steward tools | Avatar, identity, artifacts |
| Student Edition | Visual subset + unlock flags | No direct avatar DB write |
| Teacher Edition | Full + usage analytics overlay | Coaching metadata only |
| Godot | Full `AvatarConfig` + mesh hints | MemoryEvents only |

**Roadmap owner:** [roadmaps/01_FORGE_PROFILE_ROADMAP.md](roadmaps/01_FORGE_PROFILE_ROADMAP.md) § Avatar Studio.

---

## Future Mapping
Every entry in the `identityThreads` and `artifacts` maps to a **Universal Bridge ID** used by future shells (e.g., Godot) to render high-fidelity assets. **AvatarConfig** uses the same ID namespace for attachment slots and clothing presets.
