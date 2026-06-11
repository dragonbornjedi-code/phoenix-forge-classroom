#!/usr/bin/env python3
"""
Local Quest Ledger — port of Downloads/quest_manager.py for gold-standard quests.

SQLite definitions + completions + streaks. NDJSON export matches quest_log_event.schema.json.
No Google Sheets / Apps Script at runtime.
"""

from __future__ import annotations

import argparse
import csv
import json
import sqlite3
import sys
import time
import uuid
from datetime import datetime
from pathlib import Path
from typing import Any, Optional

REPO_ROOT = Path(__file__).resolve().parents[1]
DEFAULT_DB = REPO_ROOT / "data" / "quest_ledger.db"
GOLD_STANDARD_PATH = REPO_ROOT / "docs" / "content" / "quests" / "2026-02-gold-standard.json"

GOLD_REQUIRED = [
    "id",
    "realm",
    "subcategory",
    "title",
    "story",
    "category_display",
    "difficulty",
    "xp",
    "duration_minutes",
    "materials",
    "deep_objectives",
    "steps",
    "scaffolding",
    "extension",
    "celebration",
    "parent_notes",
    "real_world_connection",
]

REALM_TO_DOMAIN = {
    "thinking-tower": "COGNITIVE_ACADEMIC",
    "heart-garden": "EMOTIONAL_REGULATION",
    "life-lodge": "PRACTICAL_LIFE",
    "social-grove": "SOCIAL_DYNAMICS",
    "adventure-grounds": "PHYSICAL_MASTERY",
    "creation-studio": "CREATIVE_CURIOSITY",
    "civic-plaza": "ETHICS_CIVIC",
    "forge_hearth": "PRACTICAL_LIFE",
}

# Legacy CSV category codes → realm/subcategory (salvage mapping)
LEGACY_CATEGORY_MAP = {
    "L": ("thinking-tower", "literacy"),
    "M": ("thinking-tower", "numeracy-pattern"),
    "ER": ("heart-garden", "feelings"),
    "PM": ("adventure-grounds", "big-moves"),
    "CC": ("creation-studio", "art-making"),
    "PL": ("life-lodge", "home-skills"),
}


class QuestLedger:
    def __init__(self, db_path: Path) -> None:
        self.db_path = db_path
        self.db_path.parent.mkdir(parents=True, exist_ok=True)
        self._init_db()

    def _connect(self) -> sqlite3.Connection:
        conn = sqlite3.connect(self.db_path)
        conn.row_factory = sqlite3.Row
        return conn

    def _init_db(self) -> None:
        conn = self._connect()
        cur = conn.cursor()
        cur.execute(
            """
            CREATE TABLE IF NOT EXISTS quests (
                quest_id TEXT PRIMARY KEY,
                realm TEXT NOT NULL,
                subcategory TEXT,
                title TEXT NOT NULL,
                difficulty INTEGER DEFAULT 1,
                xp INTEGER DEFAULT 0,
                duration_minutes INTEGER DEFAULT 15,
                prerequisites TEXT DEFAULT '',
                quest_json TEXT NOT NULL,
                created_at TEXT DEFAULT CURRENT_TIMESTAMP,
                updated_at TEXT DEFAULT CURRENT_TIMESTAMP
            )
            """
        )
        cur.execute(
            """
            CREATE TABLE IF NOT EXISTS quest_completions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                event_id TEXT NOT NULL,
                quest_id TEXT NOT NULL,
                status TEXT NOT NULL,
                step_index INTEGER,
                realm_id TEXT,
                teacher_domain_id TEXT,
                xp_awarded INTEGER,
                gold_awarded INTEGER,
                source_surface TEXT DEFAULT 'operator_manual',
                child_id TEXT DEFAULT 'ezra',
                notes TEXT DEFAULT '',
                timestamp_epoch_millis INTEGER NOT NULL,
                event_json TEXT NOT NULL,
                FOREIGN KEY (quest_id) REFERENCES quests(quest_id)
            )
            """
        )
        cur.execute(
            """
            CREATE TABLE IF NOT EXISTS streak_tracker (
                category TEXT PRIMARY KEY,
                current_streak INTEGER DEFAULT 0,
                best_streak INTEGER DEFAULT 0,
                last_completion_date TEXT
            )
            """
        )
        conn.commit()
        conn.close()

    def validate_gold_quest(self, quest: dict) -> list[str]:
        errors: list[str] = []
        for field in GOLD_REQUIRED:
            if field not in quest:
                errors.append("missing field: %s" % field)
        steps = quest.get("steps", [])
        if not isinstance(steps, list) or len(steps) == 0:
            errors.append("steps must be non-empty array")
        else:
            for i, step in enumerate(steps):
                if not isinstance(step, dict) or "instruction" not in step:
                    errors.append("step %d missing instruction" % i)
        objectives = quest.get("deep_objectives", {})
        if not isinstance(objectives, dict):
            errors.append("deep_objectives must be object")
        else:
            for key in ("executive_function", "social_emotional", "sensory", "academic"):
                if key not in objectives:
                    errors.append("deep_objectives missing %s" % key)
        return errors

    def import_gold_standard(self, json_path: Path) -> dict[str, Any]:
        data = json.loads(json_path.read_text(encoding="utf-8"))
        if not isinstance(data, list):
            raise ValueError("gold-standard file must be a JSON array")
        imported = 0
        errors: list[str] = []
        conn = self._connect()
        cur = conn.cursor()
        for quest in data:
            if not isinstance(quest, dict):
                continue
            quest_errors = self.validate_gold_quest(quest)
            if quest_errors:
                errors.extend(["%s: %s" % (quest.get("id", "?"), e) for e in quest_errors])
                continue
            quest_id = str(quest["id"])
            prereqs = ",".join(quest.get("prerequisites", []) or [])
            cur.execute(
                """
                INSERT OR REPLACE INTO quests
                (quest_id, realm, subcategory, title, difficulty, xp, duration_minutes,
                 prerequisites, quest_json, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
                """,
                (
                    quest_id,
                    quest["realm"],
                    quest.get("subcategory", ""),
                    quest["title"],
                    int(quest.get("difficulty", 1)),
                    int(quest.get("xp", 0)),
                    int(quest.get("duration_minutes", 15)),
                    prereqs,
                    json.dumps(quest, ensure_ascii=False),
                ),
            )
            imported += 1
        conn.commit()
        conn.close()
        return {"imported": imported, "errors": errors, "total": len(data)}

    def import_legacy_csv(self, csv_path: Path) -> int:
        """Skeleton CSV → realm/subcategory stubs (not gold-standard validated)."""
        loaded = 0
        conn = self._connect()
        cur = conn.cursor()
        with csv_path.open(encoding="utf-8") as f:
            reader = csv.reader(f)
            next(reader, None)
            for row in reader:
                if len(row) < 6:
                    continue
                quest_id = row[0]
                name = row[1]
                category = row[3] if len(row) > 3 else ""
                realm, subcategory = LEGACY_CATEGORY_MAP.get(category, ("thinking-tower", "legacy"))
                stub = {
                    "id": quest_id,
                    "realm": realm,
                    "subcategory": subcategory,
                    "title": name,
                    "story": row[2] if len(row) > 2 else name,
                    "category_display": category,
                    "difficulty": 1,
                    "xp": int(row[5]) if row[5].isdigit() else 10,
                    "duration_minutes": 15,
                    "materials": [],
                    "deep_objectives": {
                        "executive_function": "legacy import",
                        "social_emotional": "legacy import",
                        "sensory": "legacy import",
                        "academic": "legacy import",
                    },
                    "steps": [{"instruction": name}],
                    "scaffolding": "",
                    "extension": "",
                    "celebration": "Quest complete!",
                    "parent_notes": "",
                    "real_world_connection": "",
                    "legacy_import": True,
                }
                prereqs = row[10] if len(row) > 10 else ""
                cur.execute(
                    """
                    INSERT OR REPLACE INTO quests
                    (quest_id, realm, subcategory, title, difficulty, xp, duration_minutes,
                     prerequisites, quest_json, updated_at)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
                    """,
                    (
                        quest_id,
                        realm,
                        subcategory,
                        name,
                        1,
                        stub["xp"],
                        15,
                        prereqs,
                        json.dumps(stub, ensure_ascii=False),
                    ),
                )
                loaded += 1
        conn.commit()
        conn.close()
        return loaded

    def get_quest_json(self, quest_id: str) -> Optional[dict]:
        conn = self._connect()
        cur = conn.cursor()
        cur.execute("SELECT quest_json FROM quests WHERE quest_id = ?", (quest_id,))
        row = cur.fetchone()
        conn.close()
        if row is None:
            return None
        return json.loads(row["quest_json"])

    def get_available_quests(self, completed_ids: list[str] | None = None) -> list[dict]:
        completed_set = set(completed_ids or [])
        conn = self._connect()
        cur = conn.cursor()
        cur.execute("SELECT quest_id, prerequisites, quest_json FROM quests")
        available: list[dict] = []
        for row in cur.fetchall():
            quest_id = row["quest_id"]
            if quest_id in completed_set:
                continue
            prereq_str = row["prerequisites"] or ""
            prereqs = [p for p in prereq_str.split(",") if p]
            if all(p in completed_set for p in prereqs):
                available.append(json.loads(row["quest_json"]))
        conn.close()
        return available

    def export_teacher_pack(self, realm: str | None = None) -> dict[str, Any]:
        conn = self._connect()
        cur = conn.cursor()
        if realm:
            cur.execute(
                "SELECT quest_json FROM quests WHERE realm = ? ORDER BY title",
                (realm,),
            )
        else:
            cur.execute("SELECT quest_json FROM quests ORDER BY realm, title")
        quests = [json.loads(r["quest_json"]) for r in cur.fetchall()]
        conn.close()
        return {
            "schema_version": 1,
            "exported_at_epoch_millis": int(time.time() * 1000),
            "realm_filter": realm,
            "quest_count": len(quests),
            "quests": quests,
        }

    def _update_streak(self, category: str) -> int:
        conn = self._connect()
        cur = conn.cursor()
        today = datetime.now().date().isoformat()
        cur.execute(
            "SELECT current_streak, last_completion_date FROM streak_tracker WHERE category = ?",
            (category,),
        )
        row = cur.fetchone()
        if row:
            current, last_date = row["current_streak"], row["last_completion_date"]
            if last_date == today:
                new_streak = current
            elif last_date and _days_between(last_date, today) == 1:
                new_streak = current + 1
            else:
                new_streak = 1
            best = max(new_streak, current)
            cur.execute(
                """
                UPDATE streak_tracker
                SET current_streak = ?, best_streak = MAX(best_streak, ?),
                    last_completion_date = ?, updated_at = CURRENT_TIMESTAMP
                WHERE category = ?
                """,
                (new_streak, best, today, category),
            )
        else:
            new_streak = 1
            cur.execute(
                """
                INSERT INTO streak_tracker (category, current_streak, best_streak, last_completion_date)
                VALUES (?, 1, 1, ?)
                """,
                (category, today),
            )
        conn.commit()
        conn.close()
        return new_streak

    def append_completion(self, event: dict) -> dict[str, Any]:
        required = ["quest_id", "status"]
        for field in required:
            if field not in event:
                raise ValueError("event missing %s" % field)

        quest_id = str(event["quest_id"])
        quest = self.get_quest_json(quest_id)
        realm_id = str(event.get("realm_id") or (quest or {}).get("realm", ""))
        domain = str(
            event.get("teacher_domain_id") or REALM_TO_DOMAIN.get(realm_id, "COGNITIVE_ACADEMIC")
        )
        ts = int(event.get("timestamp_epoch_millis") or int(time.time() * 1000))
        event_id = str(event.get("event_id") or ("evt_%s" % uuid.uuid4().hex[:12]))
        status = str(event["status"])

        xp_awarded = event.get("xp_awarded")
        if status == "completed" and xp_awarded is None and quest:
            streak = self._update_streak(realm_id or "general")
            multiplier = min(1.5, 1.0 + streak * 0.1)
            xp_awarded = int(int(quest.get("xp", 0)) * multiplier)
            event["xp_awarded"] = xp_awarded
            event["notes"] = (event.get("notes") or "") + " streak=%d" % streak

        normalized = {
            "event_id": event_id,
            "timestamp_epoch_millis": ts,
            "quest_id": quest_id,
            "quest_title": str(event.get("quest_title") or (quest or {}).get("title", quest_id)),
            "status": status,
            "step_index": event.get("step_index"),
            "realm_id": realm_id,
            "teacher_domain_id": domain,
            "xp_awarded": xp_awarded,
            "gold_awarded": event.get("gold_awarded"),
            "source_surface": str(event.get("source_surface", "operator_manual")),
            "child_id": str(event.get("child_id", "ezra")),
            "notes": str(event.get("notes", "")),
        }

        conn = self._connect()
        cur = conn.cursor()
        cur.execute(
            """
            INSERT INTO quest_completions
            (event_id, quest_id, status, step_index, realm_id, teacher_domain_id,
             xp_awarded, gold_awarded, source_surface, child_id, notes,
             timestamp_epoch_millis, event_json)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """,
            (
                normalized["event_id"],
                normalized["quest_id"],
                normalized["status"],
                normalized["step_index"],
                normalized["realm_id"],
                normalized["teacher_domain_id"],
                normalized["xp_awarded"],
                normalized["gold_awarded"],
                normalized["source_surface"],
                normalized["child_id"],
                normalized["notes"],
                normalized["timestamp_epoch_millis"],
                json.dumps(normalized, ensure_ascii=False),
            ),
        )
        conn.commit()
        conn.close()
        return normalized

    def export_ndjson(self, out_path: Path) -> int:
        conn = self._connect()
        cur = conn.cursor()
        cur.execute(
            "SELECT event_json FROM quest_completions ORDER BY timestamp_epoch_millis ASC"
        )
        lines = [r["event_json"] for r in cur.fetchall()]
        conn.close()
        out_path.parent.mkdir(parents=True, exist_ok=True)
        with out_path.open("w", encoding="utf-8") as f:
            for line in lines:
                f.write(line.strip() + "\n")
        return len(lines)

    def validate_file(self, json_path: Path) -> dict[str, Any]:
        data = json.loads(json_path.read_text(encoding="utf-8"))
        quests = data if isinstance(data, list) else [data.get("quest", data)]
        all_errors: list[str] = []
        for quest in quests:
            if not isinstance(quest, dict):
                all_errors.append("entry is not an object")
                continue
            all_errors.extend(
                ["%s: %s" % (quest.get("id", "?"), e) for e in self.validate_gold_quest(quest)]
            )
        return {"valid": len(all_errors) == 0, "errors": all_errors, "count": len(quests)}


def _days_between(a: str, b: str) -> int:
    d1 = datetime.strptime(a, "%Y-%m-%d").date()
    d2 = datetime.strptime(b, "%Y-%m-%d").date()
    return abs((d2 - d1).days)


def cmd_import_gold(args: argparse.Namespace) -> int:
    ledger = QuestLedger(Path(args.db))
    result = ledger.import_gold_standard(Path(args.json_path))
    print(json.dumps(result, indent=2))
    return 0 if not result["errors"] else 1


def cmd_validate(args: argparse.Namespace) -> int:
    ledger = QuestLedger(Path(args.db))
    result = ledger.validate_file(Path(args.json_path))
    print(json.dumps(result, indent=2))
    return 0 if result["valid"] else 1


def cmd_export_teacher_pack(args: argparse.Namespace) -> int:
    ledger = QuestLedger(Path(args.db))
    pack = ledger.export_teacher_pack(args.realm)
    if args.output:
        Path(args.output).write_text(json.dumps(pack, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")
        print("wrote %s (%d quests)" % (args.output, pack["quest_count"]))
    else:
        print(json.dumps(pack, indent=2))
    return 0


def cmd_append_completion(args: argparse.Namespace) -> int:
    ledger = QuestLedger(Path(args.db))
    event = json.loads(Path(args.event_json).read_text(encoding="utf-8"))
    result = ledger.append_completion(event)
    print(json.dumps(result, indent=2))
    return 0


def cmd_export_ndjson(args: argparse.Namespace) -> int:
    ledger = QuestLedger(Path(args.db))
    count = ledger.export_ndjson(Path(args.output))
    print("exported %d events to %s" % (count, args.output))
    return 0


def cmd_available(args: argparse.Namespace) -> int:
    ledger = QuestLedger(Path(args.db))
    completed = args.completed.split(",") if args.completed else []
    quests = ledger.get_available_quests([c for c in completed if c])
    print(json.dumps({"count": len(quests), "quest_ids": [q["id"] for q in quests]}, indent=2))
    return 0


def main() -> int:
    parser = argparse.ArgumentParser(description="Phoenix Forge Local Quest Ledger")
    parser.add_argument("--db", default=str(DEFAULT_DB), help="SQLite database path")
    sub = parser.add_subparsers(dest="command", required=True)

    p_import = sub.add_parser("import-gold-standard", help="Import gold-standard quest array")
    p_import.add_argument("json_path", nargs="?", default=str(GOLD_STANDARD_PATH))
    p_import.set_defaults(func=cmd_import_gold)

    p_val = sub.add_parser("validate", help="Validate quest JSON against gold-standard fields")
    p_val.add_argument("json_path")
    p_val.set_defaults(func=cmd_validate)

    p_pack = sub.add_parser("export-teacher-pack", help="Export quests for Teacher expedition board")
    p_pack.add_argument("--realm", help="Filter by realm_id (e.g. thinking-tower)")
    p_pack.add_argument("-o", "--output", help="Write JSON to file")
    p_pack.set_defaults(func=cmd_export_teacher_pack)

    p_append = sub.add_parser("append-completion", help="Append quest_log_event completion row")
    p_append.add_argument("event_json", help="JSON file matching quest_log_event.schema.json")
    p_append.set_defaults(func=cmd_append_completion)

    p_ndjson = sub.add_parser("export-ndjson", help="Export completions as quest_log.ndjson")
    p_ndjson.add_argument("output", nargs="?", default=str(REPO_ROOT / "data" / "quest_log.ndjson"))
    p_ndjson.set_defaults(func=cmd_export_ndjson)

    p_avail = sub.add_parser("available", help="List quests with prerequisites met")
    p_avail.add_argument("--completed", default="", help="Comma-separated completed quest ids")
    p_avail.set_defaults(func=cmd_available)

    args = parser.parse_args()
    return args.func(args)


if __name__ == "__main__":
    raise SystemExit(main())
