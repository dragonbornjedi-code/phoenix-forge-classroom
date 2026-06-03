# Offline Data & Sync Architecture

Phoenix Forge Classroom is built on the principle of **Absolute Autonomy**. It must function 100% offline, in remote locations, without any dependency on a central server or cloud account.

## 1. Local-First Storage
- **Platform:** Android (Primary) / Godot (Future Bridge).
- **Engine:** SQLite for structured data (ForgeProfile, Missions, Taxonomy).
- **Blob Store:** Local filesystem for media (photos, audio notes, screenshots).
- **Integrity:** Write-Ahead Logging (WAL) and atomic transactions to prevent data loss during power-offs.

## 2. Peer-to-Peer Sync (The Shared Layer)
Teacher and Student editions communicate directly on the same device or via local network (Tailscale/WiFi).

### Sync Directionality
- **Export (Teacher -> Student):**
  - Mission definitions.
  - Content Packs (JSON/Assets).
  - Parent-approved rewards.
  - World unlock signals.
- **Import (Student -> Teacher):**
  - ForgeProfile delta updates.
  - Mission completion events.
  - Emotional check-in logs.
  - Nature Journal discoveries (photos/audio).
  - Builder Sandbox screenshots.

### Transport Mechanisms
1. **AIDL (Android Interface Definition Language):** Direct service calls for live data exchange when both apps are running.
2. **ContentProvider:** For querying student progress from the Teacher Edition.
3. **File Bundle:** For "Deep Backups." The ForgeProfile and all media are bundled into a `.pfc` archive that can be manually moved to another device.

## 3. Conflict Resolution
- **Authoritative Source:** 
  - Teacher Edition owns the **Curriculum** and **Missions**.
  - Student Edition owns the **Discovery History** and **Emotional Logs**.
- **Merging:** ForgeProfile uses a "Causal Tree" or simple "Timestamp-Wins" logic for world-state properties. Every event has a unique UUID to prevent duplicates during multiple syncs.

## 4. Privacy & Security
- **No Cloud:** Data never leaves the device unless the parent explicitly exports a backup file.
- **Encryption:** ForgeProfile bundles are encrypted with a parent-defined passkey before being saved to external storage.
- **Adult-Only Data:** Teacher notes and behavioral analytics never sync to the Student Edition.
