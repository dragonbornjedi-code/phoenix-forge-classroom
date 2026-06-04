# Forge Profile Application Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build the "Forge Profile" Android application as the canonical identity backbone for the Phoenix Forge ecosystem.

**Architecture:** Clean Architecture with MVVM, Hilt for DI, Room for local-first persistence, and Jetpack Compose for the UI. It features a dual-layer data model (Child-facing vs. Teacher-metadata) and an event-driven timeline.

**Tech Stack:** Kotlin, Jetpack Compose, Material3, Room, Hilt, Coroutines, StateFlow.

---

### Task 1: Project Scaffolding & Module Setup

**Files:**
- Create: `phoenix-forge-classroom-forge-profile/build.gradle.kts`
- Create: `phoenix-forge-classroom-forge-profile/app/build.gradle.kts`
- Create: `phoenix-forge-classroom-forge-profile/app/src/main/java/com/phoenixforge/profile/ForgeProfileApp.kt`

- [ ] **Step 1: Create project directory and root build script**
- [ ] **Step 2: Scaffold app module with Hilt and Compose dependencies**
- [ ] **Step 3: Define Application class with @HiltAndroidApp**

---

### Task 2: Domain Entities & Room Database

**Files:**
- Create: `phoenix-forge-classroom-forge-profile/app/src/main/java/com/phoenixforge/profile/domain/model/ForgeProfile.kt`
- Create: `phoenix-forge-classroom-forge-profile/app/src/main/java/com/phoenixforge/profile/data/local/ProfileDatabase.kt`
- Create: `phoenix-forge-classroom-forge-profile/app/src/main/java/com/phoenixforge/profile/data/local/entity/ProfileEntity.kt`

- [ ] **Step 1: Define core entities (Avatar, IdentitySnapshot, MemoryArtifact, etc.)**
- [ ] **Step 2: Create Room entities for persistent storage**
- [ ] **Step 3: Implement ProfileDatabase with necessary DAOs**

---

### Task 3: Repository Layer & Cross-App API

**Files:**
- Create: `phoenix-forge-classroom-forge-profile/app/src/main/java/com/phoenixforge/profile/domain/repository/ProfileRepository.kt`
- Create: `phoenix-forge-classroom-forge-profile/app/src/main/java/com/phoenixforge/profile/data/repository/ProfileRepositoryImpl.kt`
- Create: `phoenix-forge-classroom-forge-profile/app/src/main/java/com/phoenixforge/profile/data/provider/ProfileContentProvider.kt`

- [ ] **Step 1: Implement ProfileRepository with Room integration**
- [ ] **Step 2: Scaffold ContentProvider to expose profile data to other apps**
- [ ] **Step 3: Define AIDL interfaces for real-time identity queries**

---

### Task 4: UI Components & Navigation

**Files:**
- Create: `phoenix-forge-classroom-forge-profile/app/src/main/java/com/phoenixforge/profile/ui/navigation/ProfileNavigation.kt`
- Create: `phoenix-forge-classroom-forge-profile/app/src/main/java/com/phoenixforge/profile/ui/theme/Theme.kt`

- [ ] **Step 1: Setup NavHost with destinations (Dashboard, Studio, Timeline, etc.)**
- [ ] **Step 2: Implement "Ghibli-style" Material3 theme**

---

### Task 5: Avatar Studio

**Files:**
- Create: `phoenix-forge-classroom-forge-profile/app/src/main/java/com/phoenixforge/profile/ui/studio/AvatarStudioScreen.kt`
- Create: `phoenix-forge-classroom-forge-profile/app/src/main/java/com/phoenixforge/profile/ui/studio/AvatarViewModel.kt`

- [ ] **Step 1: Implement customization logic (Hair, Eyes, Clothing)**
- [ ] **Step 2: Create version history mechanism for avatars**
- [ ] **Step 3: Implement local asset storage for custom renderings**

---

### Task 6: Identity Card & About Me

**Files:**
- Create: `phoenix-forge-classroom-forge-profile/app/src/main/java/com/phoenixforge/profile/ui/identity/IdentityCardScreen.kt`
- Create: `phoenix-forge-classroom-forge-profile/app/src/main/java/com/phoenixforge/profile/ui/identity/IdentityViewModel.kt`

- [ ] **Step 1: Build editable identity fields with automatic snapshotting**
- [ ] **Step 2: Implement About Me prompt timeline**
- [ ] **Step 3: Create Favorites tracker with historical view**

---

### Task 7: Memory Capsule & Timeline

**Files:**
- Create: `phoenix-forge-classroom-forge-profile/app/src/main/java/com/phoenixforge/profile/ui/memory/MemoryCapsuleScreen.kt`
- Create: `phoenix-forge-classroom-forge-profile/app/src/main/java/com/phoenixforge/profile/ui/timeline/ChildhoodTimelineScreen.kt`

- [ ] **Step 1: Implement photo and audio artifact capture**
- [ ] **Step 2: Build the Childhood Timeline aggregating all events**
- [ ] **Step 3: Implement Dashboard with visual status summary**

---

### Task 8: Teacher Metadata (Hidden Layer)

**Files:**
- Create: `phoenix-forge-classroom-forge-profile/app/src/main/java/com/phoenixforge/profile/data/local/entity/TeacherMetadataEntity.kt`
- Create: `phoenix-forge-classroom-forge-profile/app/src/main/java/com/phoenixforge/profile/ui/teacher/TeacherGate.kt`

- [ ] **Step 1: Implement separate Room table for teacher metadata**
- [ ] **Step 2: Build parent-gated access screen (Hidden from child)**
- [ ] **Step 3: Implement export logic for academic records**

---

### Task 9: Verification & Integration

- [ ] **Step 1: Unit Test Room DAOs and Repositories**
- [ ] **Step 2: Instrumentation Test Navigation and ViewModels**
- [ ] **Step 3: Verify Cross-App data exposure (Mocked ContentProvider query)**
