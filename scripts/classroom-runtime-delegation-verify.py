#!/usr/bin/env python3
"""Layer C runtime delegation verify — Classroom lane."""
from __future__ import annotations

import json
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
HOME_LIB = Path("/var/lib/phoenix-ai/workspace/phoenix-forge-home/scripts/lib")
sys.path.insert(0, str(HOME_LIB))

from agency_turn_telemetry import analyze_turns, load_turns  # noqa: E402
from runtime_delegation_verify import corpus_evaluate, merge_runtime_corpus, run_classifier  # noqa: E402

CORPUS = ROOT / "registry/classroom_routing_corpus.json"
CLASSIFIER = ROOT / "scripts/classroom-route-classify.py"
OUT = ROOT / "registry/handoffs/classroom_runtime_delegation_last.json"


def main() -> int:
    runtime = analyze_turns(load_turns("classroom"), live_only=True)
    corpus = corpus_evaluate(CORPUS, lambda p: run_classifier(CLASSIFIER, p))
    report = merge_runtime_corpus(runtime, corpus, live_only=True)
    report["lane"] = "classroom"
    report["parent_agent_id"] = "ao_classroom_bridge"
    OUT.parent.mkdir(parents=True, exist_ok=True)
    OUT.write_text(json.dumps(report, indent=2), encoding="utf-8")
    print(json.dumps(report, indent=2))
    return 0 if report["overall"] == "PASS" else 1


if __name__ == "__main__":
    raise SystemExit(main())
