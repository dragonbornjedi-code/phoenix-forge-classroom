package com.phoenixforge.student.domain.dreams

object DreamBoardCatalog {
    const val SHORT_TERM = "short_term"
    const val LONG_TERM = "long_term"

    data class Horizon(val type: String, val title: String, val addButtonLabel: String)

    val horizons = listOf(
        Horizon(SHORT_TERM, "Short-Term Dreams", "Add short term dream"),
        Horizon(LONG_TERM, "Long-Term Dreams", "Add long term dream")
    )

    data class SuggestionCategory(
        val id: String,
        val title: String,
        val subtitle: String,
        val examples: List<String>
    )

    val suggestionCategories = listOf(
        SuggestionCategory(
            id = "inspirational",
            title = "Inspirational",
            subtitle = "People, moments, and ideas that light you up",
            examples = listOf(
                "Learn from someone who does work I admire",
                "Visit a place that feels full of possibility",
                "Start a small project because someone believed I could"
            )
        ),
        SuggestionCategory(
            id = "creative",
            title = "Creative",
            subtitle = "Making, building, and expressing",
            examples = listOf(
                "Draw or design something only I would think of",
                "Build a model, machine, or invention",
                "Write a story, song, or comic"
            )
        ),
        SuggestionCategory(
            id = "self_delving",
            title = "Self-Delving",
            subtitle = "Understanding yourself — lite but honest",
            examples = listOf(
                "Notice what I reach for when I have free time",
                "Name one skill I want just for me, not for a grade",
                "Track one question I keep coming back to"
            )
        )
    )

    fun horizonTitle(type: String): String = horizons.firstOrNull { it.type == type }?.title ?: type

    fun categoryById(id: String): SuggestionCategory? = suggestionCategories.firstOrNull { it.id == id }
}
