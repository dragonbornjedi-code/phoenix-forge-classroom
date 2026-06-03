# ForgeProfile Specification: The Childhood DNA

The ForgeProfile is the persistent digital identity of a child within the Phoenix Forge ecosystem. It is the "Single Source of Truth" that survives for 10+ years across all experience shells.

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

## Future Mapping
Every entry in the `identityThreads` and `artifacts` maps to a **Universal Bridge ID** used by future shells (e.g., Godot) to render high-fidelity assets.
