package com.phoenixforge.student.ui.navigation

object StudentRoutes {
    const val HOME = "home"
    const val DIGITAL_HOME = "digital_home"
    const val GARDEN_MERCHANT = "garden_merchant"
    const val GALLERY = "gallery"
    const val VAULT = "vault"
    const val QUESTS = "quests"
    const val TODAY = "today"
    const val NPC = "npc"
    const val IMPORT = "import"
    const val SETTINGS = "settings"
    const val STORY_ARCHIVE = "story_archive"
    const val DREAMS = "dreams"
    const val INBOX = "inbox"

    const val DIGITAL_HOME_HUB = "digital_home/hub"
    const val DIGITAL_HOME_ROOM = "digital_home/room/{roomId}"
    const val DIGITAL_HOME_GARDEN = "digital_home/garden"
    const val DIGITAL_HOME_GALLERY = "digital_home/gallery"
    const val DIGITAL_HOME_VAULT = "digital_home/vault"
    const val DIGITAL_HOME_QUEST_BOARD = "digital_home/quest_board"
    const val DIGITAL_HOME_COMPANIONS = "digital_home/companions"
    const val DIGITAL_HOME_PET_SPACE = "digital_home/pet_space"

    val digitalHomeStack = setOf(
        DIGITAL_HOME_HUB,
        DIGITAL_HOME_ROOM,
        DIGITAL_HOME_GARDEN,
        DIGITAL_HOME_GALLERY,
        DIGITAL_HOME_VAULT,
        DIGITAL_HOME_QUEST_BOARD,
        DIGITAL_HOME_COMPANIONS,
        DIGITAL_HOME_PET_SPACE,
    )

    fun digitalRoom(roomId: String) = "digital_home/room/$roomId"

    fun isDigitalHomeRoute(route: String?): Boolean =
        route != null && (route == DIGITAL_HOME || route.startsWith("digital_home/"))

    fun showBottomBar(route: String?): Boolean {
        if (route == null) return false
        if (isDigitalHomeRoute(route)) {
            return route == DIGITAL_HOME || route == DIGITAL_HOME_HUB
        }
        return route in rootBottomBarRoutes
    }

    private val rootBottomBarRoutes = setOf(
        HOME,
        DIGITAL_HOME,
        DIGITAL_HOME_HUB,
        QUESTS,
        TODAY,
        DREAMS,
        STORY_ARCHIVE,
        SETTINGS,
        INBOX,
    )
}
