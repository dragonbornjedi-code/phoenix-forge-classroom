package com.phoenixforge.classroom.teacher.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "intent_tiles")
data class IntentTile(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String = "",
    val domain: String = ForgeDomain.LANGUAGE.name,
    val targetChildName: String = "Ezra",
    val status: String = TileStatus.PLANNED.name,
    val sortOrder: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null,
    val evidenceNotes: String = "",
    val sentToStudentAt: Long? = null,
    val coachingCues: String = "",
    val materials: String = "",
    val curriculumDomainId: String? = null,
    val starterLessonId: String? = null,
    val studentMission: String = "",
    val lessonPatternId: String = ""
)

enum class ForgeDomain(val emoji: String, val displayName: String) {
    LANGUAGE("📚", "Language"),
    MOTOR("🏃", "Motor"),
    EMOTIONAL("💙", "Emotional"),
    CREATIVE("🎨", "Creative"),
    SCIENTIFIC("🔬", "Scientific"),
    SOCIAL("🤝", "Social");

    companion object {
        fun fromName(name: String): ForgeDomain =
            entries.firstOrNull { it.name == name } ?: LANGUAGE
    }
}

enum class TileStatus(val displayName: String) {
    PLANNED("Planned"),
    SENT("Sent to Ezra"),
    ACTIVE("Active"),
    COMPLETED("Completed"),
    DEFERRED("Deferred");

    companion object {
        fun fromName(name: String): TileStatus =
            entries.firstOrNull { it.name == name } ?: PLANNED
    }
}
