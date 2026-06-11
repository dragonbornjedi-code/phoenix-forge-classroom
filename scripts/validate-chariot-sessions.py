#!/usr/bin/env python3
"""Validate chariot sessions: schema fields + audio clips exist on disk."""

from __future__ import annotations

import json
import sys
from pathlib import Path

REPO = Path(__file__).resolve().parents[1]
SESSIONS_PATH = REPO / "shared" / "chariot" / "sessions" / "sage-sessions-salvage.json"
AUDIO_MAP = REPO / "shared" / "chariot" / "audio_path_map.json"
MANIFEST_PATH = REPO.parent / "phoenix-forge-world" / "data" / "audio" / "quest_voice_manifest.json"
AUDIO_DIR = REPO.parent / "phoenix-forge-world" / "assets" / "audio" / "quests"

SESSION_REQUIRED = [
    "id", "title", "body_weather_flags", "welcome_voice_id", "welcome_clip_id",
    "celebration_clip_id", "story", "activities",
]
ACTIVITY_REQUIRED = [
    "realm", "voice_id", "narration_clip_id", "narration_category", "input_type",
]
BANNED_NPC = ("Owl Sage", "Brave Bear", "Spark Fox", "Gentle Giant", "Calm Chameleon", "Wise Raccoon")
VALID_VOICES = {
    "ignavarr", "steward_physical", "steward_cognitive", "steward_creative",
    "steward_emotional", "steward_practical", "spark",
}


def load_manifest_clips() -> set[str]:
    data = json.loads(MANIFEST_PATH.read_text(encoding="utf-8"))
    clips: set[str] = set()
    for files in data.get("categories", {}).values():
        clips.update(files)
    return clips


def validate_session(sess: dict, manifest_clips: set[str], errors: list[str]) -> None:
    sid = sess.get("id", "?")
    for field in SESSION_REQUIRED:
        if field not in sess:
            errors.append("%s: missing %s" % (sid, field))
    for banned in BANNED_NPC:
        blob = json.dumps(sess)
        if banned in blob:
            errors.append("%s: franchise NPC still present: %s" % (sid, banned))
    wv = str(sess.get("welcome_voice_id", ""))
    if wv and wv not in VALID_VOICES:
        errors.append("%s: invalid welcome_voice_id %s" % (sid, wv))
    for clip_key in ("welcome_clip_id", "celebration_clip_id"):
        clip = str(sess.get(clip_key, ""))
        if clip and clip not in manifest_clips:
            errors.append("%s: %s %s not in manifest" % (sid, clip_key, clip))
        if clip and not (AUDIO_DIR / clip).exists():
            errors.append("%s: missing audio file %s" % (sid, clip))
    for i, act in enumerate(sess.get("activities", [])):
        for field in ACTIVITY_REQUIRED:
            if field not in act:
                errors.append("%s activity %d: missing %s" % (sid, i, field))
        vid = str(act.get("voice_id", ""))
        if vid and vid not in VALID_VOICES:
            errors.append("%s activity %d: invalid voice_id %s" % (sid, i, vid))
        clip = str(act.get("narration_clip_id", ""))
        if clip and clip not in manifest_clips:
            errors.append("%s activity %d: clip %s not in manifest" % (sid, i, clip))
        if clip and not (AUDIO_DIR / clip).exists():
            errors.append("%s activity %d: missing audio %s" % (sid, i, clip))


def main() -> int:
    if not SESSIONS_PATH.exists():
        print("missing %s" % SESSIONS_PATH, file=sys.stderr)
        return 1
    sessions = json.loads(SESSIONS_PATH.read_text(encoding="utf-8"))
    if not isinstance(sessions, list):
        print("sessions must be array", file=sys.stderr)
        return 1
    manifest_clips = load_manifest_clips()
    errors: list[str] = []
    for sess in sessions:
        if isinstance(sess, dict):
            validate_session(sess, manifest_clips, errors)
    result = {"valid": len(errors) == 0, "session_count": len(sessions), "errors": errors}
    print(json.dumps(result, indent=2))
    if result["valid"]:
        print("VALIDATE_CHARIOT_SESSIONS: PASS")
        return 0
    print("VALIDATE_CHARIOT_SESSIONS: FAIL", file=sys.stderr)
    return 1


if __name__ == "__main__":
    raise SystemExit(main())
