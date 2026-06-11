package com.phoenixforge.student.ui.theme

import androidx.compose.ui.graphics.Color
import com.phoenixforge.student.domain.model.HouseRoomType
import com.phoenixforge.student.sync.DigitalHomeWire

object StudentRoomVisuals {
    fun emoji(room: HouseRoomType): String = when (room) {
        HouseRoomType.BEDROOM -> "🛏️"
        HouseRoomType.STUDY -> "📚"
        HouseRoomType.GARDEN -> "🌻"
        HouseRoomType.MEMORY_VAULT -> "💎"
        HouseRoomType.GALLERY -> "🖼️"
        HouseRoomType.QUEST_BOARD -> "📌"
        HouseRoomType.PET_SPACE -> "🐾"
    }

    fun emoji(roomId: String): String = when (roomId) {
        DigitalHomeWire.Rooms.BEDROOM -> "🛏️"
        DigitalHomeWire.Rooms.STUDY -> "📚"
        DigitalHomeWire.Rooms.GARDEN -> "🌻"
        DigitalHomeWire.Rooms.MEMORY_VAULT -> "💎"
        DigitalHomeWire.Rooms.GALLERY -> "🖼️"
        DigitalHomeWire.Rooms.QUEST_BOARD -> "📌"
        DigitalHomeWire.Rooms.PET_SPACE -> "🐾"
        else -> "✨"
    }

    fun accent(room: HouseRoomType): Color = when (room) {
        HouseRoomType.BEDROOM -> Color(0xFFFFE0B2)
        HouseRoomType.STUDY -> Color(0xFFC8E6FF)
        HouseRoomType.GARDEN -> Color(0xFFC8F7C5)
        HouseRoomType.MEMORY_VAULT -> Color(0xFFE1BEE7)
        HouseRoomType.GALLERY -> Color(0xFFFFF9C4)
        HouseRoomType.QUEST_BOARD -> Color(0xFFFFCCBC)
        HouseRoomType.PET_SPACE -> Color(0xFFB2EBF2)
    }

    fun lockedAccent(): Color = Color(0xFFECEFF1)
}
