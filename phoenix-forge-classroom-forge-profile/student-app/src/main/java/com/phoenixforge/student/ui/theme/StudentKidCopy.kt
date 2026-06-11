package com.phoenixforge.student.ui.theme

import com.phoenixforge.student.domain.model.HouseRoomType
import com.phoenixforge.student.sync.DigitalHomeWire
import com.phoenixforge.student.ui.navigation.StudentRoutes

/**
 * Kid-facing copy for Student Edition (ages ~5–8). Keep technical terms out of the UI.
 */
object StudentKidCopy {
    fun screenTitle(route: String?): String = when (route) {
        StudentRoutes.HOME -> "Home"
        StudentRoutes.DIGITAL_HOME, StudentRoutes.DIGITAL_HOME_HUB -> "My House"
        StudentRoutes.DIGITAL_HOME_GARDEN, StudentRoutes.GARDEN_MERCHANT -> "Garden Shop"
        StudentRoutes.DIGITAL_HOME_COMPANIONS, StudentRoutes.NPC -> "Friends"
        StudentRoutes.DIGITAL_HOME_GALLERY, StudentRoutes.GALLERY -> "Treasures"
        StudentRoutes.DIGITAL_HOME_VAULT, StudentRoutes.VAULT -> "Memory Chest"
        StudentRoutes.DIGITAL_HOME_QUEST_BOARD, StudentRoutes.QUESTS, StudentRoutes.TODAY -> "Adventures"
        StudentRoutes.DIGITAL_HOME_PET_SPACE -> "Pet Yard"
        StudentRoutes.DREAMS -> "Dream Board"
        StudentRoutes.SETTINGS -> "More"
        StudentRoutes.IMPORT -> "Wake Up Hero"
        StudentRoutes.STORY_ARCHIVE -> "Story Time"
        StudentRoutes.INBOX -> "Messages"
        else -> when {
            route?.startsWith("digital_home/room/") == true -> "My House"
            else -> "Phoenix Forge"
        }
    }

    fun homeGreeting(forgeName: String): String = "Hi, $forgeName!"

    fun homeNoProfileTitle(): String = "Hi, friend!"

    fun homeNoProfileBody(): String =
        "Spark is keeping your hearth warm. Ask a grown-up to wake up your hero when you're ready."

    fun levelLine(level: Int, streakDays: Int): String =
        "Level $level ${starBurst(level)} · $streakDays-day streak"

    fun starsLine(xp: Int): String = "$xp adventure stars collected"

    fun companionTitle(): String = "Your buddy Spark"

    fun companionEmpty(): String = "Spark is hiding somewhere fun — go on an adventure to meet them!"

    fun storyTitle(): String = "Today's tale"

    fun digitalHomeTitle(): String = "My Magical House"

    fun digitalHomeSubtitle(): String =
        "Pick a room and go explore!"

    fun digitalHomeProgress(level: Int, xp: Int, tokens: Int): String =
        "Level $level ${starBurst(level)} · $xp stars · $tokens shiny tokens"

    fun zoneSectionTitle(): String = "Rooms"

    fun friendsSectionTitle(): String = "Friends & pets"

    fun companionVisit(): String = "Say hi"

    fun whispsLine(unlocked: Int, total: Int): String = "Little whisps awake: $unlocked of $total"

    fun petSpaceOpen(): String = "Pet yard is open — come play!"

    fun petSpaceLocked(): String = "Pet yard is almost ready — keep adventuring! 🐾"

    fun roomOpen(): String = "Tap to go in!"

    fun roomLockedAtLevel(level: Int): String = "Almost ready! Keep playing ✨"

    fun roomAlmostReady(): String = "Almost ready! Keep playing ✨"

    fun heroBadge(style: String, color: String): String =
        "${styleEmoji(style)} ${colorEmoji(color)} hero"

    fun sparkTaleLine(raw: String, speaker: String?): String {
        if (raw.contains("Welcome back", ignoreCase = true) || raw.contains("moments from this week", ignoreCase = true)) {
            return "Spark says: I'm so happy you're back! 🌟"
        }
        val sparkQuote = Regex("Spark[^']*'([^']+)'").find(raw)?.groupValues?.getOrNull(1)
        if (!sparkQuote.isNullOrBlank()) return "Spark says: $sparkQuote 🌟"
        val cleaned = raw
            .replace(Regex("This happened because of what happened before: [^.]+\\.?"), "")
            .replace(Regex("Spark remembers: "), "Spark says: ")
            .replace(Regex("\\s+"), " ")
            .trim()
        if (cleaned.length in 8..120 && !cleaned.contains("daily return", ignoreCase = true)) {
            return cleaned
        }
        return "Spark has a story for you today! Tap House to explore 🏡"
    }

    fun questFoundSpark(): String = "You found a quest! 🌟"

    fun questLetterFromDad(): String = "Letter from Dad 💌"

    fun questSideQuestTitle(): String = "Bonus fun!"

    fun questSideQuestHint(): String = "Spark hid extra surprises in your house."

    fun questStatusNotStarted(): String = "New!"
    fun questStatusInProgress(): String = "Go go!"
    fun questStatusDone(): String = "Yay! ⭐"

    fun questEmptyDaily(): String = "No letters yet. Dad will send more soon! 💌"
    fun questEmptyMorning(): String = "Morning hugs are ready when you wake up! 🌅"
    fun questEmptyNight(): String = "Cozy night quests come at bedtime! 🌙"
    fun questNeedHero(): String = "Ask a grown-up to wake up your hero first ✨"

    fun questCategoryMorning(): String = "Good morning"
    fun questCategoryDaily(): String = "Dad's letters"
    fun questCategoryNight(): String = "Sleepy time"

    fun questCategoryMorningHint(): String = "Stretch, smile, share one feeling"
    fun questCategoryDailyHint(): String = "Dad left notes for you!"
    fun questCategoryNightHint(): String = "Calm down before bed"

    fun questLoading(): String = "Spark is finding your adventures…"

    fun questRefresh(): String = "Look again"
    fun questWriting(): String = "Spark is cheering…"
    fun questLetsGo(): String = "Let's go! 🚀"
    fun questDone(): String = "I did it! ⭐"
    fun questClose(): String = "Not yet"

    fun questRewardStars(amount: Int): String = "+$amount stars ⭐"

    fun importTitle(): String = "Wake Up My Hero"

    fun importSubtitle(): String =
        "Spark will wake up your hero. A grown-up can help the first time."

    fun importChecking(): String = "Spark is looking for your hero…"

    fun importUnavailable(): String = "Hero not ready yet — ask a grown-up for help ✨"

    fun importFound(name: String): String = "Found $name!"

    fun importSyncButton(importing: Boolean): String =
        if (importing) "Waking up…" else "Wake up my hero ✨"

    fun importRefresh(): String = "Look again"

    fun settingsSyncHero(): String = "Wake up my hero ✨"

    fun settingsParentSection(): String = "Grown-up corner"

    fun settingsSignOut(): String = "Sign out (grown-up)"

    fun questsHubTitle(): String = "Adventures!"

    fun questsHubSubtitle(): String = "Pick something fun for today."

    private fun starBurst(level: Int): String =
        when {
            level >= 5 -> "⭐⭐⭐"
            level >= 3 -> "⭐⭐"
            else -> "⭐"
        }

    private fun styleEmoji(style: String): String = when (style.lowercase()) {
        "explorer" -> "🧭"
        "guardian" -> "🛡️"
        "scholar" -> "📚"
        else -> "⭐"
    }

    private fun colorEmoji(color: String): String = when (color.lowercase()) {
        "blue" -> "💙"
        "green" -> "💚"
        "gold" -> "💛"
        "pink" -> "💗"
        "purple" -> "💜"
        else -> "✨"
    }

    fun roomStory(roomId: String): Pair<String, String> = when (roomId) {
        DigitalHomeWire.Rooms.BEDROOM -> "Cozy Bedroom" to
            "This is the heart of your house. Your hero rests here and Spark watches the fireplace glow."
        DigitalHomeWire.Rooms.STUDY -> "Quiet Study" to
            "Stories and puzzles live here. Sit down when you want to learn something new."
        DigitalHomeWire.Rooms.GARDEN -> "Sunny Garden" to
            "Flowers, tokens, and friendly merchants. Plant ideas and watch them grow!"
        DigitalHomeWire.Rooms.MEMORY_VAULT -> "Memory Chest" to
            "Special moments you never want to forget are kept safe in here."
        DigitalHomeWire.Rooms.GALLERY -> "Treasure Gallery" to
            "Pictures and trophies from your adventures shine on these walls."
        DigitalHomeWire.Rooms.QUEST_BOARD -> "Quest Board" to
            "Today's missions are pinned here. Complete one and earn stars!"
        DigitalHomeWire.Rooms.PET_SPACE -> "Pet Yard" to
            "Furry friends play here once you've grown strong enough to care for them."
        else -> "Magic Room" to "Something wonderful is growing in this room."
    }

    fun kidRoomName(room: HouseRoomType): String = when (room) {
        HouseRoomType.BEDROOM -> "Cozy Bedroom"
        HouseRoomType.STUDY -> "Quiet Study"
        HouseRoomType.GARDEN -> "Sunny Garden"
        HouseRoomType.MEMORY_VAULT -> "Memory Chest"
        HouseRoomType.GALLERY -> "Treasure Gallery"
        HouseRoomType.QUEST_BOARD -> "Quest Board"
        HouseRoomType.PET_SPACE -> "Pet Yard"
    }
}
