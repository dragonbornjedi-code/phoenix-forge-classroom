#!/usr/bin/env python3
"""One-shot port: Downloads/sage-sessions.json → shared/chariot/sessions/sage-sessions-salvage.json"""

from __future__ import annotations

import json
import re
import sys
from pathlib import Path

REPO = Path(__file__).resolve().parents[1]
SRC = Path("/home/joshuar/Downloads/sage-sessions.json")
OUT = REPO / "shared" / "chariot" / "sessions" / "sage-sessions-salvage.json"

NPC_TO_VOICE = {
    "Brave Bear": "steward_physical",
    "Owl Sage": "steward_cognitive",
    "Spark Fox": "steward_creative",
    "Gentle Giant": "steward_emotional",
    "Calm Chameleon": "steward_emotional",
    "Wise Raccoon": "steward_practical",
}

STORY_REPLACEMENTS = [
    (r"Brave Bear", "the movement steward"),
    (r"Owl Sage", "the thinking steward"),
    (r"Spark Fox", "the creative steward"),
    (r"Gentle Giant", "the heart steward"),
    (r"Calm Chameleon", "the calm steward"),
    (r"Wise Raccoon", "the practical steward"),
    (r"Dragon Challenge", "adventure challenge"),
]


def clip_from_legacy(path: str) -> tuple[str, str]:
    """Return (category, clip_id basename)."""
    name = path.split("/")[-1]
    mapping = {
        "welcome.mp3": ("welcome", "welcome.mp3"),
        "welcome-alt1.mp3": ("welcome", "welcome-alt1.mp3"),
        "welcome-alt2.mp3": ("welcome", "welcome-alt2.mp3"),
        "welcome-alt3.mp3": ("welcome", "welcome-alt3.mp3"),
        "celebration-1.mp3": ("celebration", "celebration-1.mp3"),
        "celebration-2.mp3": ("celebration", "celebration-2.mp3"),
        "celebration-3.mp3": ("celebration", "celebration-3.mp3"),
        "celebration-4.mp3": ("celebration", "celebration-4.mp3"),
        "celebration-5.mp3": ("celebration", "celebration-5.mp3"),
        "celebration-6.mp3": ("celebration", "celebration-6.mp3"),
        "mid-check.mp3": ("progress", "mid-check.mp3"),
    }
    if name in mapping:
        return mapping[name]
    prefixes = [
        ("movement-", "movement"),
        ("math-", "math"),
        ("spelling-", "spelling"),
        ("emotion-", "emotion"),
        ("sensory-", "sensory"),
        ("creative-", "creative"),
        ("life-", "life"),
    ]
    for prefix, cat in prefixes:
        if name.startswith(prefix):
            return cat, name
    return "progress", name


def rewrite_text(text: str) -> str:
    out = text
    for pattern, repl in STORY_REPLACEMENTS:
        out = re.sub(pattern, repl, out)
    return out


def port_activity(act: dict) -> dict:
    npc = act.get("npc", "")
    voice_id = NPC_TO_VOICE.get(npc, "steward_cognitive")
    cat, clip = clip_from_legacy(str(act.get("narration_audio", "")))
    return {
        "realm": act.get("realm", ""),
        "voice_id": voice_id,
        "activity_type": act.get("type", ""),
        "narration_clip_id": clip,
        "narration_category": cat,
        "narration_text": rewrite_text(str(act.get("narration_text", ""))),
        "input_type": act.get("input_type", "done"),
        "xp": act.get("xp", 0),
        "sensory_load": act.get("sensory_load", 1),
        "ef_demand": act.get("ef_demand", 1),
        "deep_target": act.get("deep_target", ""),
        "correct_answer": act.get("correct_answer", ""),
        "hints": act.get("hints", []) or [],
        "choices": act.get("choices", []) or [],
    }


def port_session(sess: dict) -> dict:
    w_cat, w_clip = clip_from_legacy(str(sess.get("ignavarr_greeting", "greetings/welcome.mp3")))
    c_cat, c_clip = clip_from_legacy(str(sess.get("celebration_audio", "celebrations/celebration-1.mp3")))
    scaffolding = sess.get("scaffolding", {})
    if isinstance(scaffolding, dict):
        scaffolding = {k: rewrite_text(str(v)) for k, v in scaffolding.items()}
    return {
        "id": sess["id"],
        "title": sess["title"],
        "body_weather_flags": sess.get("target_weather", []),
        "duration_minutes": sess.get("duration_minutes", 15),
        "total_xp": sess.get("total_xp", 0),
        "welcome_voice_id": "ignavarr",
        "welcome_clip_id": w_clip,
        "welcome_category": w_cat,
        "celebration_clip_id": c_clip,
        "celebration_category": c_cat,
        "story": rewrite_text(str(sess.get("story", ""))),
        "deep_objectives": sess.get("deep_objectives", {}),
        "scaffolding": scaffolding,
        "parent_tips": [rewrite_text(str(t)) for t in sess.get("parent_tips", [])],
        "activities": [port_activity(a) for a in sess.get("activities", [])],
    }


def main() -> int:
    if not SRC.exists():
        print("missing source: %s" % SRC, file=sys.stderr)
        return 1
    sessions = json.loads(SRC.read_text(encoding="utf-8"))
    ported = [port_session(s) for s in sessions]
    OUT.parent.mkdir(parents=True, exist_ok=True)
    OUT.write_text(json.dumps(ported, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")
    print("ported %d sessions → %s" % (len(ported), OUT))
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
