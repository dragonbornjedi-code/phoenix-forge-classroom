# Shared Schemas & Contracts

This directory contains the source-of-truth definitions for data exchanged between Teacher and Student apps.

## Content Packs
Content packs are the "fuel" for the Universal Engines.

### Universal Matching Engine (UME) Schema
```json
{
  "id": "ume_colors_01",
  "engine": "UME",
  "variant": "ONE_TO_ONE",
  "items": [
    {
      "source": {"type": "IMAGE", "value": "res/berry_blue.png", "id": "blue"},
      "target": {"type": "IMAGE", "value": "res/bird_blue.png", "id": "blue"}
    },
    {
      "source": {"type": "IMAGE", "value": "res/berry_red.png", "id": "red"},
      "target": {"type": "IMAGE", "value": "res/bird_red.png", "id": "red"}
    }
  ],
  "feedback": {
    "success": "Great job matching the colors!",
    "hint": "Try matching the berry to the bird of the same color."
  }
}
```

### Universal Sequencing Engine (USE) Schema
```json
{
  "id": "use_counting_01",
  "engine": "USE",
  "sequence": [
    {"type": "TEXT", "value": "1"},
    {"type": "TEXT", "value": "2"},
    {"type": "TEXT", "value": "3"},
    {"type": "TEXT", "value": "4"}
  ],
  "shuffled": true
}
```

## Mission Definitions
Missions wrap Content Packs in a narrative.

```json
{
  "id": "mission_001",
  "childTitle": "The Blue Bird's Snack",
  "childInstruction": "The blue birds are hungry! Can you find their favorite berries?",
  "engineType": "UME",
  "contentPackRef": "ume_colors_01",
  "wispType": "MOSS",
  "reward": {"type": "FLOWER", "id": "blue_rose"}
}
```
