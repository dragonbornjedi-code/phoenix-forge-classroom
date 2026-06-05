package com.phoenixforge.classroom.teacher.domain.curriculum

import com.phoenixforge.classroom.teacher.domain.model.ForgeDomain

/**
 * Bundled Curriculum Of Life domains — mirrors
 * phoenix-forge-classroom-teacher-edition/docs/curriculum-of-life.md
 */
object CurriculumDomainCatalog {

    val designRules: List<String> = listOf(
        "Keep lessons concrete, playful, and brief.",
        "Use technical language here; kid language goes in Student Edition.",
        "Prefer physical interaction before screen interaction.",
        "Track observable behavior, not labels or diagnoses.",
        "Offer several teaching methods — one method will not work every day.",
        "Separate support from punishment. Regulation tools are not consequences.",
        "Treat progress as version control: observe, adjust, retry."
    )

    val domains: List<CurriculumDomain> = listOf(
        CurriculumDomain(
            id = CurriculumDomainId.COGNITIVE_ACADEMIC,
            focusLine = "Mastering the tools of thought, communication, and systematic inquiry.",
            teacherFraming = "Literacy, numeracy, scientific thinking, first-principles reasoning, history as systems, and abstraction mapping.",
            studentFraming = "Secret-code reading, guess-and-check science, spot-the-error games, then-and-now stories, and \"what job does this thing do?\"",
            lessonPatterns = listOf(
                "Label Hunt", "Guess And Check", "Manual Mission", "Then And Now", "System Map"
            ),
            progressMetrics = listOf(
                "Recognizes and uses new symbols or labels.",
                "Makes a prediction before testing.",
                "Changes an answer when evidence changes.",
                "Follows 2-4 visual steps with less help over time.",
                "Explains an object by job, parts, or category."
            ),
            teacherCues = listOf(
                "If he guesses without looking, ask: \"What do your eyes notice?\"",
                "If stuck, reduce the step size.",
                "If he memorizes but cannot transfer, try a new object with the same pattern.",
                "If frustration rises, switch to a physical version of the same idea."
            ),
            supportMethods = listOf(
                "Visual: picture cards, labels, maps, diagrams.",
                "Physical: sorting, building, tracing, pointing, moving pieces.",
                "Verbal: \"I wonder...\" prompts and simple why chains.",
                "Choice-based: offer two possible answers and let him test."
            ),
            subdomains = CurriculumSubdomainCatalog.forDomain(CurriculumDomainId.COGNITIVE_ACADEMIC)
        ),
        CurriculumDomain(
            id = CurriculumDomainId.EMOTIONAL_REGULATION,
            focusLine = "Managing the internal state and processing the human experience.",
            teacherFraming = "Emotional literacy, state awareness, protocol execution, self-regulation, agency, and mindfulness.",
            studentFraming = "System temperature, pause button, deep breath trick, safe fort, \"I did this,\" and body check.",
            lessonPatterns = listOf(
                "System Temp", "Pause Button", "Recovery Hub", "Energy Meter", "Success Jar"
            ),
            progressMetrics = listOf(
                "Names or points to a feeling color.",
                "Uses a pause cue with adult help.",
                "Returns from upset faster over time.",
                "Asks for help, food, rest, or space.",
                "Can say what helped after a hard moment."
            ),
            teacherCues = listOf(
                "Fast movement, louder voice, or clenched hands may mean yellow state.",
                "Red state is not the teaching window. Regulate first, discuss later.",
                "Repeated refusal may mean task size, sensory load, fatigue, or unclear instructions.",
                "If one breathing tool fails, switch methods instead of insisting."
            ),
            supportMethods = listOf(
                "Co-regulation: calm voice, fewer words, grounded posture.",
                "Movement: wall push, carry a weighted item, shake-out, walk.",
                "Breath: feather blow, candle breath, box breathing if tolerated.",
                "Sensory: dimmer light, headphones, blanket, water, quiet.",
                "Repair: short post-moment review — what happened, what helped, what next?"
            ),
            subdomains = CurriculumSubdomainCatalog.forDomain(CurriculumDomainId.EMOTIONAL_REGULATION)
        ),
        CurriculumDomain(
            id = CurriculumDomainId.PRACTICAL_LIFE,
            focusLine = "The mechanics of managing life as an independent agent.",
            teacherFraming = "Body maintenance, financial literacy, household competence, tool safety, and digital citizenship.",
            studentFraming = "Charge the battery, fuel sort, token shop, room bug hunt, fix-it station, and private-stuff rules.",
            lessonPatterns = listOf(
                "Body Battery", "Token Ledger", "System Audit", "Tool Drill", "Trusted Team"
            ),
            progressMetrics = listOf(
                "Completes one routine with fewer prompts.",
                "Saves tokens for a delayed reward.",
                "Gives a simple reason for a purchase or choice.",
                "Uses tools with safe handling reminders.",
                "Knows at least one private-information rule."
            ),
            teacherCues = listOf(
                "If routines collapse, check time of day, hunger, fatigue, and transition load.",
                "If tokens become stressful, simplify to fewer categories.",
                "If cleanup is overwhelming, reduce to \"find three things.\"",
                "If privacy lessons feel abstract, use roleplay with toys."
            ),
            supportMethods = listOf(
                "Visual checklist.", "First-then board.", "Timer or song.",
                "Token board.", "Model-copy-practice."
            ),
            subdomains = CurriculumSubdomainCatalog.forDomain(CurriculumDomainId.PRACTICAL_LIFE)
        ),
        CurriculumDomain(
            id = CurriculumDomainId.SOCIAL_DYNAMICS,
            focusLine = "Navigating the multi-player environment of society.",
            teacherFraming = "Empathy, perspective-taking, conflict resolution, boundaries, access control, and collaboration.",
            studentFraming = "Puppet feelings, talk-it-out, turn timer, my bubble, ask first, and helper missions.",
            lessonPatterns = listOf(
                "Puppet POV", "Repeat Back", "Turn Timer", "Bubble Map", "Team Build"
            ),
            progressMetrics = listOf(
                "Identifies another person's likely feeling or want.",
                "Uses a boundary phrase or stop signal.",
                "Waits for a turn with support.",
                "Repairs after conflict with a short action.",
                "Completes a shared task with a role."
            ),
            teacherCues = listOf(
                "Grabbing may mean impulse, unclear turn structure, or fear of losing access.",
                "Boundary refusal should be respected even when phrased imperfectly.",
                "If conflict language fails, switch to timer, visual turn card, or adult narration."
            ),
            supportMethods = listOf(
                "Social story.", "Puppet rehearsal.", "Visual turn cards.",
                "Timer.", "Role swap."
            ),
            subdomains = CurriculumSubdomainCatalog.forDomain(CurriculumDomainId.SOCIAL_DYNAMICS)
        ),
        CurriculumDomain(
            id = CurriculumDomainId.PHYSICAL_MASTERY,
            focusLine = "The hardware interface and environmental interaction.",
            teacherFraming = "Body control, proprioception, manual dexterity, resource loops, survival, and situational awareness.",
            studentFraming = "Balance machine, tiny-hands missions, trash sorting, light patrol, house map, and safe spot.",
            lessonPatterns = listOf(
                "Tape Line", "Micro Build", "Resource Sort", "Power Patrol", "Home Map"
            ),
            progressMetrics = listOf(
                "Holds balance longer or with better control.",
                "Completes fine-motor task with less dropping.",
                "Sorts common materials correctly.",
                "Identifies home exits and safe places.",
                "Uses body control near fragile tools or builds."
            ),
            teacherCues = listOf(
                "Clumsiness may increase with fatigue, excitement, hunger, or overstimulation.",
                "Fine motor work should stop before hands become frustrated.",
                "Survival lessons should feel secure, not scary."
            ),
            supportMethods = listOf(
                "Big movement before fine motor.", "Short repetitions.",
                "Heavier objects for proprioceptive input.", "Demonstrate slowly.",
                "Use real household jobs."
            ),
            subdomains = CurriculumSubdomainCatalog.forDomain(CurriculumDomainId.PHYSICAL_MASTERY)
        ),
        CurriculumDomain(
            id = CurriculumDomainId.CREATIVE_CURIOSITY,
            focusLine = "The software of innovation and self-actualization.",
            teacherFraming = "Procedural art, maker pipeline, philosophical inquiry, curiosity, and failure recovery.",
            studentFraming = "Pattern art, draw-plan-build-upgrade, great why, what-if stories, and oops patch.",
            lessonPatterns = listOf(
                "Rule Art", "Maker Pipeline", "Why Chain", "What If", "Oops Patch"
            ),
            progressMetrics = listOf(
                "Creates a pattern or follows a creative constraint.",
                "Moves from idea to prototype.",
                "Suggests one improvement.",
                "Uses \"oops\" without quitting.",
                "Asks a question and pursues an answer."
            ),
            teacherCues = listOf(
                "Perfectionism may show up as avoidance.",
                "Too many materials can block creativity; constrain choices.",
                "Curiosity often appears as mess, repetition, and odd questions."
            ),
            supportMethods = listOf(
                "Limited material sets.", "Before-and-after photos.",
                "One-upgrade rule.", "Adult scribing for ideas.",
                "Celebrate retries more than outcomes."
            ),
            subdomains = CurriculumSubdomainCatalog.forDomain(CurriculumDomainId.CREATIVE_CURIOSITY)
        ),
        CurriculumDomain(
            id = CurriculumDomainId.ETHICS_CIVIC,
            focusLine = "The kernel — decision-making framework for life.",
            teacherFraming = "Moral reasoning, integrity, decision architecture, community contribution, calibration, and version control.",
            studentFraming = "Fairness games, daily promise, truth box, two paths, helper mission, and version check.",
            lessonPatterns = listOf(
                "Fair Split", "Daily Promise", "Two Paths", "Helper Node", "Version Check"
            ),
            progressMetrics = listOf(
                "Keeps a small promise or repairs when he cannot.",
                "Identifies fair/unfair in concrete examples.",
                "Predicts one consequence of a choice.",
                "Helps with a family task.",
                "Names one thing he improved."
            ),
            teacherCues = listOf(
                "Shame blocks learning. Use repair and retry.",
                "Abstract morality needs concrete examples.",
                "Praise specific actions, not fixed identity.",
                "If he lies, investigate fear, impulse, or wishful thinking before consequences."
            ),
            supportMethods = listOf(
                "Story examples.", "Toy roleplay.", "Concrete fairness objects.",
                "Repair script.", "Star or version log."
            ),
            subdomains = CurriculumSubdomainCatalog.forDomain(CurriculumDomainId.ETHICS_CIVIC)
        )
    )

    fun domainById(id: CurriculumDomainId): CurriculumDomain? =
        domains.firstOrNull { it.id == id }

    fun forgeDomainFor(curriculumId: CurriculumDomainId): ForgeDomain = when (curriculumId) {
        CurriculumDomainId.COGNITIVE_ACADEMIC -> ForgeDomain.LANGUAGE
        CurriculumDomainId.EMOTIONAL_REGULATION -> ForgeDomain.EMOTIONAL
        CurriculumDomainId.PRACTICAL_LIFE -> ForgeDomain.SCIENTIFIC
        CurriculumDomainId.SOCIAL_DYNAMICS -> ForgeDomain.SOCIAL
        CurriculumDomainId.PHYSICAL_MASTERY -> ForgeDomain.MOTOR
        CurriculumDomainId.CREATIVE_CURIOSITY -> ForgeDomain.CREATIVE
        CurriculumDomainId.ETHICS_CIVIC -> ForgeDomain.SOCIAL
    }
}
