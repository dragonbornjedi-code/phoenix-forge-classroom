package com.phoenixforge.student.ui.navigation

import com.phoenixforge.student.domain.model.HouseRoomType
import com.phoenixforge.student.sync.DigitalHomeWire

object DigitalHomeNavigation {
    fun routeForRoom(room: HouseRoomType, unlocked: Boolean): String? = when (room) {
        HouseRoomType.BEDROOM -> StudentRoutes.digitalRoom(DigitalHomeWire.Rooms.BEDROOM)
        HouseRoomType.STUDY -> StudentRoutes.digitalRoom(DigitalHomeWire.Rooms.STUDY)
        HouseRoomType.GARDEN -> if (unlocked) StudentRoutes.DIGITAL_HOME_GARDEN else null
        HouseRoomType.MEMORY_VAULT -> if (unlocked) StudentRoutes.DIGITAL_HOME_VAULT else null
        HouseRoomType.GALLERY -> if (unlocked) StudentRoutes.DIGITAL_HOME_GALLERY else null
        HouseRoomType.QUEST_BOARD -> StudentRoutes.DIGITAL_HOME_QUEST_BOARD
        HouseRoomType.PET_SPACE -> if (unlocked) StudentRoutes.DIGITAL_HOME_PET_SPACE else null
    }
}
