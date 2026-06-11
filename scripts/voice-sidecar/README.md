# Voice sidecar (Tier C — optional)

Detachable Piper cache branch. Pattern from `Downloads/voice_controller.py`.

**Not required** for quest spine or VoicePlayer playback. Human recordings and bundled quest mp3 take priority per `shared/voice/playback_policy.yaml`.

## Planned CLI

```bash
voice-sidecar synthesize --voice ignavarr --text "Welcome to the hearth"
voice-sidecar cache-path --voice steward_cognitive --text-hash abc123
```

Cache layout: `~/.phoenix-forge/voice-cache/{voice_id}/{text_hash}.wav`

Forge World probes cache before `piper_tts` stub. Faster Whisper listen branch writes `voice_transcript.ndjson` — document only until Phase 4.
