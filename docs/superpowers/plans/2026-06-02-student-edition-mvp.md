# Student Edition MVP Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build a functional Student Edition prototype featuring the Hearthhome hub and a Universal Matching Engine (UME).

**Architecture:** A Jetpack Compose-based Android app using a "World as Menu" navigation pattern. It uses an engine-based approach where gameplay is decoupled from curriculum content (provided via shared schemas).

**Tech Stack:** Kotlin, Jetpack Compose, Material3, ViewModel, StateFlow.

---

### Task 1: Project Scaffolding & Shared Data Models

**Files:**
- Create: `shared/src/main/java/com/phoenixforge/shared/models/EngineModels.kt`
- Create: `shared/src/main/java/com/phoenixforge/shared/models/MissionModels.kt`
- Create: `phoenix-forge-classroom-student-edition/android/build.gradle.kts`
- Create: `phoenix-forge-classroom-student-edition/android/app/src/main/java/com/phoenixforge/student/MainActivity.kt`

- [ ] **Step 1: Define Shared Engine Models**
Create the base data classes for the Universal Engines in the shared layer.

```kotlin
package com.phoenixforge.shared.models

enum class EngineType { UME, USE, SANDBOX, MARKETPLACE }

data class ContentPack(
    val id: String,
    val engine: EngineType,
    val items: List<ContentItem>
)

data class ContentItem(
    val source: Resource,
    val target: Resource
)

data class Resource(
    val type: ResourceType,
    val value: String,
    val id: String
)

enum class ResourceType { IMAGE, TEXT, AUDIO }
```

- [ ] **Step 2: Define Shared Mission Models**
Create the mission wrapper that links engines to narrative.

```kotlin
package com.phoenixforge.shared.models

data class Mission(
    val id: String,
    val title: String,
    val instruction: String,
    val engineType: EngineType,
    val contentPackId: String,
    val rewardId: String? = null
)
```

- [ ] **Step 3: Scaffold Student App MainActivity**
Set up the entry point for the Student Edition.

```kotlin
package com.phoenixforge.student

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    HearthhomeHub()
                }
            }
        }
    }
}
```

---

### Task 2: Hearthhome Hub Implementation

**Files:**
- Create: `phoenix-forge-classroom-student-edition/android/app/src/main/java/com/phoenixforge/student/ui/hub/HearthhomeHub.kt`
- Create: `phoenix-forge-classroom-student-edition/android/app/src/main/java/com/phoenixforge/student/ui/hub/HubLocation.kt`

- [ ] **Step 1: Define Hub Locations**
Create a sealed class or enum for the interactive locations in the world.

```kotlin
sealed class HubLocation(val title: String, val icon: String) {
    object MessengerPost : HubLocation("Messenger Post", "🐦")
    object ClockTower : HubLocation("Clock Tower", "⏰")
    object MarketSquare : HubLocation("Market Square", "💰")
    object BuilderWorkshop : HubLocation("Workshop", "🔨")
}
```

- [ ] **Step 2: Build Hub UI**
Create a visually engaging (even with placeholders) grid or map for the hub.

```kotlin
@Composable
fun HearthhomeHub() {
    Column {
        Text("Welcome to Hearthhome", style = MaterialTheme.typography.headlineLarge)
        LazyVerticalGrid(columns = GridCells.Fixed(2)) {
            items(listOf(HubLocation.MessengerPost, HubLocation.ClockTower, HubLocation.MarketSquare, HubLocation.BuilderWorkshop)) { location ->
                HubCard(location)
            }
        }
    }
}

@Composable
fun HubCard(location: HubLocation) {
    Card(onClick = { /* Navigate to Engine */ }) {
        Column {
            Text(location.icon)
            Text(location.title)
        }
    }
}
```

---

### Task 3: Universal Matching Engine (UME) - Prototype

**Files:**
- Create: `phoenix-forge-classroom-student-edition/android/app/src/main/java/com/phoenixforge/student/ui/engines/ume/MatchingEngine.kt`
- Create: `phoenix-forge-classroom-student-edition/android/app/src/main/java/com/phoenixforge/student/ui/engines/ume/UMEViewModel.kt`

- [ ] **Step 1: Create UME ViewModel**
Handle the state of matching items.

```kotlin
class UMEViewModel : ViewModel() {
    private val _state = MutableStateFlow(UMEState())
    val state = _state.asStateFlow()

    fun onMatchAttempt(sourceId: String, targetId: String) {
        if (sourceId == targetId) {
            // Success logic
        }
    }
}
```

- [ ] **Step 2: Create UME UI**
Implement a simple drag-and-drop or tap-to-match interface.

```kotlin
@Composable
fun MatchingEngine(contentPack: ContentPack) {
    Row {
        Column { /* Source Items */ }
        Column { /* Target Items */ }
    }
}
```

---

### Task 4: Daily Rituals - Mood Monster MVP

**Files:**
- Create: `phoenix-forge-classroom-student-edition/android/app/src/main/java/com/phoenixforge/student/ui/rituals/MoodMonster.kt`

- [ ] **Step 1: Implement Mood Monster**
Simple emoji selector that saves an event.

```kotlin
@Composable
fun MoodMonster(onMoodSelected: (String) -> Unit) {
    Row {
        listOf("😊", "😐", "😔", "😡").forEach { emoji ->
            Button(onClick = { onMoodSelected(emoji) }) {
                Text(emoji)
            }
        }
    }
}
```

---

### Task 5: Testing & Verification

- [ ] **Step 1: Unit Test Shared Models**
Verify that the shared models serialize/deserialize correctly (mocked).

- [ ] **Step 2: Hub Navigation Test**
Verify that tapping a hub location transitions to the correct engine placeholder.

- [ ] **Step 3: UME Success Verification**
Verify that matching correct IDs triggers a success state in the ViewModel.
