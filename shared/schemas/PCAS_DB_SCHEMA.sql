# PCAS Database Schema: The Living Ledger

This schema defines the structure of the local SQLite database for the Phoenix Forge PCAS.

## 1. ForgeProfile Table
Tracks the core identity and bonds.
```sql
CREATE TABLE forge_profile (
    uid TEXT PRIMARY KEY,
    forger_name TEXT,
    pronouns TEXT,
    origin_date TIMESTAMP,
    current_age_stage TEXT,
    spark_maturation_tier INTEGER
);
```

## 2. Living Chronicle Table
Episodic memory store.
```sql
CREATE TABLE chronicle_episodes (
    id TEXT PRIMARY KEY,
    chapter_id TEXT,
    narrative_title TEXT,
    occurrence_date TIMESTAMP,
    summary TEXT,
    spark_reflection TEXT,
    wisp_impact_json TEXT -- Map<WispID, Score>
);

CREATE TABLE chronicle_chapters (
    id TEXT PRIMARY KEY,
    title TEXT,
    age_range TEXT,
    start_date TIMESTAMP,
    end_date TIMESTAMP
);
```

## 3. Legacy Artifacts Table
Media and discovery tracking.
```sql
CREATE TABLE legacy_artifacts (
    id TEXT PRIMARY KEY,
    episode_id TEXT, -- Link to chronicle
    artifact_type TEXT, -- NATURE_FIND, BUILD, MILESTONE
    real_world_path TEXT, -- Local media storage
    world_manifestation_id TEXT, -- For 2D/3D rendering
    metadata_json TEXT, -- GPS, Weather, Mood
    parent_approved BOOLEAN DEFAULT 0
);
```

## 4. Family Mythology Table
Lore and landmarks.
```sql
CREATE TABLE mythology (
    id TEXT PRIMARY KEY,
    myth_type TEXT, -- LANDMARK, TRADITION, COMPANION, JOKE
    real_world_name TEXT,
    world_lore_name TEXT,
    context_trigger_json TEXT -- Time/Location triggers
);
```

## 5. Mastery Metrics Table (Hidden)
Developmental tracking.
```sql
CREATE TABLE mastery_logs (
    domain_id TEXT,
    metric_key TEXT,
    value REAL,
    timestamp TIMESTAMP,
    PRIMARY KEY (domain_id, metric_key, timestamp)
);
```
