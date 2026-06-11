#!/usr/bin/env python3
"""Prove Parent→Leaf delegation using classroom_routing_corpus.json."""
from __future__ import annotations

import json
import subprocess
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CORPUS = ROOT / "registry" / "classroom_routing_corpus.json"
CLASSIFIER = ROOT / "scripts" / "classroom-route-classify.py"


def classify(prompt: str) -> dict:
    proc = subprocess.run(
        [sys.executable, str(CLASSIFIER), prompt],
        capture_output=True,
        text=True,
        check=True,
    )
    return json.loads(proc.stdout)


def main() -> int:
    corpus = json.loads(CORPUS.read_text(encoding="utf-8"))
    cases = corpus["cases"]
    gates = corpus.get("gates", {})
    min_ratio = gates.get("leaf_hit_min_ratio", 0.92)

    hits = 0
    parent_only = 0
    rows = []
    for case in cases:
        result = classify(case["prompt"])
        leaf = result.get("leaf_agent_id")
        expected = case["expected_leaf"]
        ok = leaf == expected
        if ok:
            hits += 1
        if leaf == "ao_classroom_bridge":
            parent_only += 1
        rows.append({
            "id": case["id"],
            "expected": expected,
            "got": leaf,
            "work_type": result.get("work_type"),
            "srs": result.get("srs_estimate"),
            "pass": ok,
        })

    ratio = hits / len(cases) if cases else 0
    report = {
        "schema_version": "classroom_delegation_report_v1",
        "total": len(cases),
        "leaf_hits": hits,
        "leaf_hit_ratio": round(ratio, 4),
        "parent_only": parent_only,
        "gate_min_ratio": min_ratio,
        "overall": "PASS" if ratio >= min_ratio else "FAIL",
        "cases": rows,
    }
    out = ROOT / "registry" / "handoffs" / "classroom_delegation_last.json"
    out.parent.mkdir(parents=True, exist_ok=True)
    out.write_text(json.dumps(report, indent=2), encoding="utf-8")
    print(json.dumps(report, indent=2))
    return 0 if report["overall"] == "PASS" else 1


if __name__ == "__main__":
    raise SystemExit(main())
