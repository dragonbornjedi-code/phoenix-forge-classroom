#!/usr/bin/env python3
"""Smoke test Chariot sage_session export shape (no JVM required)."""

from __future__ import annotations

import json
import sys
from pathlib import Path

REPO = Path(__file__).resolve().parents[1]
SESSIONS = REPO / "shared" / "chariot" / "sessions" / "sage-sessions-salvage.json"


def build_stack(session: dict, weather: str) -> dict:
    missions = []
    for act in session.get("activities", []):
        missions.append({
            "title": act.get("activity_type", session["title"]),
            "student_mission": act.get("narration_text", ""),
            "narration_clip_id": act.get("narration_clip_id", ""),
            "voice_id": act.get("voice_id", ""),
            "input_type": act.get("input_type", "done"),
            "xp_reward": act.get("xp", 0),
        })
    return {
        "schema_version": 1,
        "export_mode": "sage_session",
        "session_id": session["id"],
        "weather": weather,
        "welcome_clip_id": session.get("welcome_clip_id", ""),
        "celebration_clip_id": session.get("celebration_clip_id", ""),
        "missions": missions,
    }


def main() -> int:
    sessions = json.loads(SESSIONS.read_text(encoding="utf-8"))
    sunny = next(s for s in sessions if "sunny" in s.get("body_weather_flags", []))
    stack = build_stack(sunny, "sunny")
    errors = []
    if stack["export_mode"] != "sage_session":
        errors.append("wrong export_mode")
    if not stack["missions"]:
        errors.append("empty missions")
    if not stack["welcome_clip_id"]:
        errors.append("missing welcome_clip_id")
    for m in stack["missions"]:
        if not m.get("narration_clip_id"):
            errors.append("mission missing narration_clip_id")
    if errors:
        print(json.dumps({"ok": False, "errors": errors}, indent=2))
        return 1
    print("SMOKE_CHARIOT_EXPORT: PASS (%d missions)" % len(stack["missions"]))
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
