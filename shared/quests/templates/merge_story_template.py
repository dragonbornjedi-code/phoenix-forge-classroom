#!/usr/bin/env python3
"""Merge quest core (Layer 1) with story template (Layer 2) → gold-standard quest JSON."""

from __future__ import annotations

import argparse
import json
import re
import sys
from pathlib import Path
from typing import Any

REPO_ROOT = Path(__file__).resolve().parents[3]
SHARED_QUESTS = REPO_ROOT / "shared" / "quests"
TEMPLATES_DIR = SHARED_QUESTS / "story_templates"


def _load_json(path: Path) -> Any:
    with path.open(encoding="utf-8") as f:
        return json.load(f)


def _apply_tokens(text: str, tokens: dict[str, str]) -> str:
    out = text
    for key, value in tokens.items():
        out = out.replace("{" + key + "}", value)
    return out


def extract_core(quest: dict) -> dict:
    """Strip narrative skin fields; keep activity core."""
    core = {k: v for k, v in quest.items() if k not in ("story", "celebration")}
    return core


def merge_core_with_template(
    core: dict,
    template: dict,
    *,
    player: str = "Explorer",
) -> dict:
    title = str(core.get("title", "Quest"))
    realm = str(core.get("realm", ""))
    tokens = {"player": player, "title": title, "realm": realm}

    story_intro = str(template.get("story_intro", ""))
    story_outro = str(template.get("story_outro", ""))
    story_parts = [p for p in (story_intro, story_outro) if p]
    story = " ".join(story_parts) if story_parts else title

    celebration_skin = str(template.get("celebration_skin", "Well done, {player}!"))
    celebration = _apply_tokens(celebration_skin, tokens)

    merged = dict(core)
    merged["story"] = story
    merged["celebration"] = celebration
    merged["story_template_id"] = str(template.get("template_id", ""))
    merged["voice_id"] = str(template.get("voice_id", ""))
    merged["npc_guardian"] = str(template.get("npc_guardian", template.get("voice_id", "")))
    return merged


def build_export_bundle(quest: dict, *, export_kind: str = "sage_draft") -> dict:
    import time

    return {
        "schema_version": 1,
        "exported_at_epoch_millis": int(time.time() * 1000),
        "export_kind": export_kind,
        "story_template_id": quest.get("story_template_id"),
        "intent_tile_stub": {
            "title": quest.get("title"),
            "teacher_domain_id": _realm_to_domain(str(quest.get("realm", ""))),
            "subcategory": quest.get("subcategory"),
            "estimated_duration": "%d min" % int(quest.get("duration_minutes", 15)),
            "parent_notes": quest.get("parent_notes", ""),
        },
        "quest": extract_core(quest),
        "target_surfaces": ["student_edition", "forge_world"],
        "voice_line_ids": ["quest_welcome", "quest_step_progress", "quest_complete_celebration"],
    }


def _realm_to_domain(realm_id: str) -> str:
    mapping = {
        "thinking-tower": "COGNITIVE_ACADEMIC",
        "heart-garden": "EMOTIONAL_REGULATION",
        "life-lodge": "PRACTICAL_LIFE",
        "social-grove": "SOCIAL_DYNAMICS",
        "adventure-grounds": "PHYSICAL_MASTERY",
        "creation-studio": "CREATIVE_CURIOSITY",
        "civic-plaza": "ETHICS_CIVIC",
        "forge_hearth": "PRACTICAL_LIFE",
    }
    return mapping.get(realm_id, "COGNITIVE_ACADEMIC")


def main() -> int:
    parser = argparse.ArgumentParser(description="Merge quest core + story template")
    parser.add_argument("--core", required=True, help="Quest core JSON file or quest id from gold-standard array")
    parser.add_argument(
        "--template",
        required=True,
        help="Template id (discovery_mystery) or path to story template JSON",
    )
    parser.add_argument("--gold-standard", type=Path, help="Gold-standard array JSON (when --core is quest id)")
    parser.add_argument("--player", default="Explorer")
    parser.add_argument("--export-bundle", action="store_true", help="Emit quest_export bundle wrapper")
    parser.add_argument("-o", "--output", type=Path, help="Write merged quest JSON to file")
    args = parser.parse_args()

    core_path = Path(args.core)
    if core_path.exists():
        raw = _load_json(core_path)
        core = extract_core(raw[0] if isinstance(raw, list) else raw)
    elif args.gold_standard:
        quests = _load_json(args.gold_standard)
        if not isinstance(quests, list):
            print("gold-standard must be a JSON array", file=sys.stderr)
            return 1
        match = next((q for q in quests if str(q.get("id")) == args.core), None)
        if match is None:
            print("quest id not found: %s" % args.core, file=sys.stderr)
            return 1
        core = extract_core(match)
    else:
        print("--core must be a file path or quest id with --gold-standard", file=sys.stderr)
        return 1

    template_path = Path(args.template)
    if template_path.exists():
        template = _load_json(template_path)
    else:
        candidate = TEMPLATES_DIR / ("%s.json" % args.template)
        if not candidate.exists():
            print("template not found: %s" % args.template, file=sys.stderr)
            return 1
        template = _load_json(candidate)

    merged = merge_core_with_template(core, template, player=args.player)
    payload: Any = build_export_bundle(merged) if args.export_bundle else merged

    text = json.dumps(payload, indent=2, ensure_ascii=False) + "\n"
    if args.output:
        args.output.parent.mkdir(parents=True, exist_ok=True)
        args.output.write_text(text, encoding="utf-8")
        print("wrote %s" % args.output)
    else:
        print(text, end="")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
