#!/usr/bin/env python3
"""Classify Classroom goal → work_type + leaf subagent (SRS-style, context-safe)."""
from __future__ import annotations

import json
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]

ROUTES: list[tuple[str, str, float, list[str]]] = [
    ("verify_test", "sub_verify_runner", 0.94, [
        "drift check", "cline essence", "verify all", "unit test suite", "verify gates",
        "testdebugunittest", "student-app test",
    ]),
    ("gradle_build", "sub_gradle_build", 0.9, [
        "gradle", "assembledebug", "compile error", "build.gradle", "jdk", "dependency",
    ]),
    ("chariot", "sub_chariot_sessions", 0.88, [
        "chariot", "sage-sessions", "audio path map", "chariot_session",
    ]),
    ("sage_advisor", "sub_sage_advisor", 0.97, [
        "sageadvisor", "sage advisor", "sage_apply", "sagechat", "long press", "expedition tile draft",
        "aiprovidercatalog", "sagetileactions", "sage tile", "structured json expedition",
        "apply expedition tile", "sageadvisorviewmodel", "provider catalog",
    ]),
    ("sync_events", "sub_sync_events", 0.9, [
        "eventwriter", "evt_", "logicalclock", "public scope", "forgeeventmodels",
        "events folder", "quest_started", "quest_completed",
    ]),
    ("cross_app_contract", "sub_sync_events", 0.88, [
        "public_state", "publicstate", "sync folder", "syncthing", "cross-app", "room_unlocked",
    ]),
    ("forge_profile", "sub_forge_profile_core", 0.94, [
        "eventingester", "eventfilewatcher", "forge_events", "contentprovider", "forge profile app",
        "eventingestcoordinator", "watch events folder",
    ]),
    ("profile_ingest", "sub_forge_profile_core", 0.88, [
        "publicstateeventprojector", "stateprojector", "room db", "timeline ingest",
    ]),
    ("lesson_manifest", "sub_curriculum_manifest", 0.94, [
        "lesson_manifest", "manifestwriter", "today's stack", "today stack", "push manifest",
        "push lesson manifest",
    ]),
    ("curriculum", "sub_curriculum_manifest", 0.88, [
        "curriculum", "subdomain", "lesson objectives", "curriculumdomain",
    ]),
    ("expedition", "sub_expedition_board", 0.96, [
        "expeditionboard", "expedition board", "field guide", "start day", "fab create tile",
        "startdaybottomsheet", "export daily stack", "expeditionboardscreen", "fab new tile",
        "expedition tile field guide", "coaching save",
    ]),
    ("student_quests", "sub_student_quests", 0.94, [
        "questsviewmodel", "progressionengine", "quest complete", "student quest", "today's expedition",
        "questsscreen", "mark mission complete",
    ]),
    ("progression", "sub_student_quests", 0.82, [
        "progressionengine", "xp reward", "unlock garden", "garden merchant student",
    ]),
    ("compose_ui", "sub_compose_architect", 0.9, [
        "navigation graph", "navhost", "plan compose", "screen plan", "teachernavgraph",
    ]),
    ("compose_ui", "sub_compose_builder", 0.92, [
        "digitalhome", "digital home", "homescreen", "compose layout", "jetpack compose screen",
        "zone gate", "garden unlock", "gardenmerchant", "merchant screen", "purchase currency",
    ]),
    ("kotlin_domain", "sub_kotlin_domain", 0.92, [
        "viewmodel", "digitalhomeviewmodel", "repository", "stateflow", "state flow",
        "domain engine", "kotlin fix",
    ]),
]


def score(prompt: str, keywords: list[str]) -> float:
    p = prompt.lower()
    hits = sum(1 for k in keywords if k in p)
    if hits == 0:
        return 0.0
    return min(1.0, hits / max(2, len(keywords) * 0.35))


def classify(prompt: str) -> dict:
    best = ("kotlin_domain", "sub_kotlin_domain", 0.25)
    for work_type, leaf, base, kws in ROUTES:
        s = base * 0.4 + score(prompt, kws) * 0.6
        if s > best[2]:
            best = (work_type, leaf, s)

    work_type, leaf, srs = best
    return {
        "prompt": prompt,
        "work_type": work_type,
        "parent_agent_id": "ao_classroom_bridge",
        "pedagogy_parent": "co_sage_coordinator" if leaf == "sub_sage_advisor" else None,
        "leaf_agent_id": leaf,
        "srs_estimate": round(srs, 3),
        "activation": "primary" if srs >= 0.55 else "suggest",
        "skills_to_load": _skills_for(work_type, leaf),
        "verify_gates": _verify_for(work_type, leaf),
        "context_budget": str(ROOT / "registry" / "classroom_context_budget.yaml"),
    }


def _skills_for(work_type: str, leaf: str) -> list[str]:
    m = {
        "compose_ui": ["frontend-design", "android-kotlin-development"],
        "kotlin_domain": ["android-kotlin-core", "android-kotlin-development"],
        "sync_events": ["cline-essence", "android-kotlin-core"],
        "cross_app_contract": ["cline-essence"],
        "sage_advisor": ["frontend-design", "sage-persona"],
        "curriculum": ["cline-essence"],
        "lesson_manifest": ["cline-essence"],
        "expedition": ["frontend-design", "cline-essence"],
        "forge_profile": ["android-kotlin-development"],
        "profile_ingest": ["android-kotlin-development"],
        "student_quests": ["android-kotlin-development", "frontend-design"],
        "progression": ["android-kotlin-development"],
        "chariot": ["cline-essence"],
        "gradle_build": ["android-gradle-build-logic"],
        "verify_test": ["cline-essence"],
    }
    base = m.get(work_type, ["cline-essence"])
    if leaf == "sub_compose_architect":
        return ["frontend-design", "cline-essence"]
    return base


def _verify_for(work_type: str, leaf: str) -> list[str]:
    m = {
        "sync_events": [
            "phoenix-forge-classroom-forge-profile/gradlew :student-app:testDebugUnitTest",
            "phoenix-forge-classroom-forge-profile/gradlew :forge-profile-core:test",
        ],
        "sage_advisor": ["phoenix-forge-classroom-forge-profile/gradlew :teacher-app:testDebugUnitTest"],
        "gradle_build": ["phoenix-forge-classroom-forge-profile/gradlew assembleDebug"],
        "verify_test": ["scripts/cline-essence-drift-check.sh", "scripts/phoenix-forge-classroom-verify-all.sh"],
    }
    return m.get(work_type, ["scripts/cline-essence-drift-check.sh"])


def main() -> int:
    prompt = " ".join(sys.argv[1:]).strip() or sys.stdin.read().strip()
    if not prompt:
        print("usage: classroom-route-classify.py <goal text>", file=sys.stderr)
        return 1
    print(json.dumps(classify(prompt), indent=2))
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
