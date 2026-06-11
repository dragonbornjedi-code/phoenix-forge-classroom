package com.phoenixforge.classroom.teacher.domain.curriculum

import com.phoenixforge.classroom.teacher.domain.model.ForgeDomain

/**
 * Pre-filled field guides for expedition tiles so Teacher Edition is never blank on first open.
 */
object FieldGuideDefaults {

    data class SeedTile(
        val title: String,
        val description: String,
        val domain: ForgeDomain,
        val studentMission: String = "",
        val materials: String = "",
        val coachingCues: String = "",
        val examples: String = "",
        val evidenceNotes: String = "",
        val supports: String = "",
        val recovery: String = "",
        val lessonPatternId: String = "",
        val routineKind: String = "", // morning_routine | night_routine | daily_quest | ""
    )

    val seedTiles: List<SeedTile> = listOf(
        SeedTile(
            title = "Morning circle",
            description = "Calm connection routine — not a daily quest tile. Scheduled popup on child tablet (future).",
            domain = ForgeDomain.SOCIAL,
            studentMission = "Do morning circle with your grown-up: breathe, stretch, and share one feeling.",
            materials = "Soft mat or rug, water nearby, optional speaker/headphones for low-volume healing tones or kid songs.",
            coachingCues = """
                Open calm: "Is someone with you for morning circle?" (Find Me or Mom depending on whose house.)
                Pick a vibe: healing frequencies (very quiet), gentle kid song, or quiet stretch music.
                Vocal guide walks through playful stretches — pause between poses until everyone taps Start.
                Sprinkle encouragement between holds: breathing cues, proud words, silly compliments.
                Close with Questions Corner: one wonder, one grateful thing, one plan for the day.
            """.trimIndent(),
            examples = """
                Stretch flow (each pose: explain → everyone ready → Start → hold → next):
                1. Reach for the sun (tall mountain)
                2. Forward fold (dangle like a noodle)
                3. Star pose (wide and bright)
                4. Butterfly sit (flap slow wings)
                5. Resting rock (quiet belly breaths)
            """.trimIndent(),
            evidenceNotes = "Note which music option worked, who joined, and one quote from Questions Corner.",
            supports = "Visual: picture cards for poses. Sensory: dim light, weighted lap pad. Communication: yes/no for companion present. Transition: only after breakfast or at 10am routine slot.",
            recovery = "If energy is too high, switch to 3 breaths + one song only. If no grown-up available, save routine for later — never force.",
            lessonPatternId = "morning_circle_routine",
            routineKind = "morning_routine",
        ),
        SeedTile(
            title = "Night wind-down",
            description = "Calm closing routine — not a daily quest tile.",
            domain = ForgeDomain.EMOTIONAL,
            studentMission = "Pick one calm thing: bath, story, or quiet music — then lights out plan.",
            materials = "Dim light, book or audio, water, optional stuffed companion.",
            coachingCues = "Three slow breaths → one grateful thing → one tomorrow hope → goodnight.",
            examples = "Brush teeth → pajamas → story page → hug → lights.",
            evidenceNotes = "Note which step was hardest and what helped.",
            supports = "Sensory: weighted blanket. Communication: picture schedule for steps.",
            recovery = "Skip to 3 breaths + hug if overtired — never extend the routine.",
            lessonPatternId = "night_wind_down",
            routineKind = "night_routine",
        ),
        SeedTile(
            title = "Nature walk",
            description = "Outdoor motor and observation",
            domain = ForgeDomain.MOTOR,
            studentMission = "Find three living things and one texture surprise outside.",
            materials = "Shoes, optional collection bag, pencil, clipboard or phone for one photo.",
            coachingCues = """
                Set a tiny mission before leaving: "We're detectives for green things."
                Pause at each find — name it, touch rule (gentle), sketch or photo one item.
                Model curiosity: "I wonder why this leaf has dots."
            """.trimIndent(),
            examples = "Walk loop: doorstep → tree → crack in sidewalk → bird sound → back inside.",
            evidenceNotes = "Photo of best find; note independent observations vs prompted.",
            supports = "Sensory: allow running between stops. Communication: point-only mode OK. Transition: shoes on song.",
            recovery = "Rain or meltdown → window nature hunt (cloud, bird, plant) for 5 minutes.",
            lessonPatternId = "nature_detective",
        ),
        SeedTile(
            title = "Reading nest",
            description = "Literacy in a quiet space",
            domain = ForgeDomain.LANGUAGE,
            studentMission = "Build a nest, pick one book, read or retell one page.",
            materials = "Pillows, blanket, 3 books (easy / just right / challenge), snack optional.",
            coachingCues = """
                Child builds the nest — adult joins beside, not above.
                Let them pick the book; you model one sentence then hand off.
                End with: "What picture was your favorite?"
            """.trimIndent(),
            examples = "Picture walk first → echo read one page → child retells with stuffed animal audience.",
            evidenceNotes = "Title chosen; minutes engaged; new words attempted.",
            supports = "Visual: cover preview of each book. Sensory: dim nest. Communication: retell with props.",
            recovery = "If reading fights start, switch to audiobook snippet + draw favorite scene.",
            lessonPatternId = "reading_nest",
        ),
        SeedTile(
            title = "Build project",
            description = "Creation and problem-solving",
            domain = ForgeDomain.CREATIVE,
            studentMission = "Make something that solves a tiny problem at home.",
            materials = "Recyclables, tape, scissors (adult nearby), markers, 20-minute timer.",
            coachingCues = """
                Problem cards: "Toys keep rolling under couch" / "Cards slide off table."
                Sketch idea first; build version 1 fast — polish is version 2.
                Celebrate the attempt: "Your first version taught us what to fix."
            """.trimIndent(),
            examples = "Ramp for cars, door sign, LEGO holder, cardboard phone stand for grown-up.",
            evidenceNotes = "Photo of build; one sentence problem it solves.",
            supports = "Visual: step photos. Sensory: stand-up build table. Transition: timer visible.",
            recovery = "Frustration → shrink goal to one tape strip success, then stop on win.",
            lessonPatternId = "maker_sprint",
        ),
    )

    fun fromStarterLesson(lesson: StarterLesson): SeedTile = SeedTile(
        title = lesson.title,
        description = lesson.objective,
        domain = CurriculumDomainCatalog.forgeDomainFor(lesson.domainId),
        studentMission = lesson.studentMission,
        materials = lesson.materials,
        coachingCues = buildString {
            lesson.setup.takeIf { it.isNotBlank() }?.let { appendLine("Setup: $it") }
            lesson.steps.forEachIndexed { i, step -> appendLine("${i + 1}. $step") }
            if (lesson.questions.isNotEmpty()) {
                appendLine()
                append("Wonder questions: ")
                append(lesson.questions.joinToString(" · "))
            }
        }.trim(),
        examples = lesson.steps.joinToString("\n") { "• $it" },
        evidenceNotes = lesson.evidence,
        supports = lesson.supports,
        recovery = lesson.recovery,
        lessonPatternId = lesson.lessonPatternId,
    )
}
