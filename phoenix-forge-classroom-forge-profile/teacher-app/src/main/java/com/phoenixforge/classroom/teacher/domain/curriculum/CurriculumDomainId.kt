package com.phoenixforge.classroom.teacher.domain.curriculum

enum class CurriculumDomainId(
    val sectionNumber: Int,
    val displayName: String,
    val emoji: String
) {
    COGNITIVE_ACADEMIC(1, "Cognitive & Academic Foundations", "🧠"),
    EMOTIONAL_REGULATION(2, "Emotional Intelligence & Self-Regulation", "💙"),
    PRACTICAL_LIFE(3, "Practical Life Skills", "🔧"),
    SOCIAL_DYNAMICS(4, "Interpersonal & Social Dynamics", "🤝"),
    PHYSICAL_MASTERY(5, "Physical Mastery & Environmental Stewardship", "🏃"),
    CREATIVE_CURIOSITY(6, "Creative Expression & Intellectual Curiosity", "🎨"),
    ETHICS_CIVIC(7, "Values, Ethics & Civic Duty", "⭐");

    companion object {
        fun fromId(id: String?): CurriculumDomainId? =
            entries.firstOrNull { it.name == id }
    }
}
