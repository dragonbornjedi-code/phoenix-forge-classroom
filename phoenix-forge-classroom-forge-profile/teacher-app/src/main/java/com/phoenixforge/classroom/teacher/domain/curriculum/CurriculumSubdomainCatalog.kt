package com.phoenixforge.classroom.teacher.domain.curriculum

/**
 * Subdomains within each Curriculum Of Life domain — the teachable skill groups
 * (reading, money, breathwork, telling time, etc.) before lesson-level activities.
 */
object CurriculumSubdomainCatalog {

    fun forDomain(domainId: CurriculumDomainId): List<CurriculumSubdomain> = when (domainId) {
        CurriculumDomainId.COGNITIVE_ACADEMIC -> cognitiveAcademic()
        CurriculumDomainId.EMOTIONAL_REGULATION -> emotionalRegulation()
        CurriculumDomainId.PRACTICAL_LIFE -> practicalLife()
        CurriculumDomainId.SOCIAL_DYNAMICS -> socialDynamics()
        CurriculumDomainId.PHYSICAL_MASTERY -> physicalMastery()
        CurriculumDomainId.CREATIVE_CURIOSITY -> creativeCuriosity()
        CurriculumDomainId.ETHICS_CIVIC -> ethicsCivic()
    }

    private fun cognitiveAcademic() = listOf(
        sub(
            "literacy_reading",
            "Reading & Literacy",
            "Decoding, comprehension, and using reading as a key to independent research.",
            "Reading", "Phonics", "Sight words", "Label reading", "Documentation literacy"
        ),
        sub(
            "literacy_writing",
            "Writing & Communication",
            "Expressing ideas on paper and in speech with growing clarity.",
            "Writing", "English", "Storytelling", "Journaling", "Letter formation"
        ),
        sub(
            "numeracy_math",
            "Mathematics & Numeracy",
            "Number sense, patterns, measurement, and everyday math fluency.",
            "Mathematics", "Counting", "Patterns", "Telling time", "Money prep", "Measurement"
        ),
        sub(
            "scientific_literacy",
            "Scientific Literacy",
            "Guess-and-check science, nature study, and the scientific method as a habit.",
            "Biology", "Nature study", "Chemistry basics", "Engineering principles", "Hypothesis testing"
        ),
        sub(
            "critical_thinking",
            "Critical Thinking & Logic",
            "First-principles reasoning, spotting errors, and breaking problems into parts.",
            "Logic", "Problem-solving", "Spot the error", "Five whys", "Analytical thinking"
        ),
        sub(
            "history_culture",
            "Historical & Cultural Context",
            "Geography, history as systems of ideas, and cultural awareness.",
            "Geography", "History", "Then and now", "Maps", "Sign language basics"
        ),
        sub(
            "tech_literacy",
            "Technological Literacy",
            "Age-appropriate exposure to how tools, code, and information systems work.",
            "Programming basics", "Developer workflows", "AI literacy", "LLM interaction", "Hardware literacy"
        ),
        sub(
            "abstraction_mapping",
            "Abstraction & Systems Mapping",
            "Seeing parts, jobs, and layers — chairs, circuits, bedrooms, and workflows.",
            "System maps", "Parts and jobs", "Layers", "Tool swap thinking", "Category sorting"
        )
    )

    private fun emotionalRegulation() = listOf(
        sub(
            "emotional_literacy",
            "Emotional Literacy",
            "Naming feelings, reading body signals, and building a vocabulary of emotions.",
            "Emotional regulation", "Feeling charts", "Color-your-mood", "Body signals", "Emotion charades"
        ),
        sub(
            "breathwork_regulation",
            "Breathwork & Self-Regulation",
            "Breathing exercises, pause protocols, and resetting before retrying.",
            "Breathwork", "Deep breath", "Feather blow", "Box breathing", "10-second wait"
        ),
        sub(
            "pause_protocols",
            "Pause & Protocol Execution",
            "Formal delay before reacting — red light/green light for the nervous system.",
            "Pause button", "Robot freeze", "Recovery hub", "Safe fort", "Co-regulation"
        ),
        sub(
            "self_concept",
            "Self-Concept & Agency",
            "Internal locus of control — successes and setbacks as learnable, not fixed.",
            "Confidence building", "Success jar", "I did this", "Agency talk", "Growth mindset"
        ),
        sub(
            "mindfulness",
            "Mindfulness & Presence",
            "Checking internal state like a system status report.",
            "Meditation", "Body check", "Energy meter", "Listen game", "Focused awareness"
        ),
        sub(
            "shadow_processing",
            "Shadow Work & Pattern Processing",
            "Gently noticing recurring triggers and subconscious patterns (teacher-framed).",
            "Pattern noticing", "Trigger mapping", "Post-moment review", "Repair scripts"
        )
    )

    private fun practicalLife() = listOf(
        sub(
            "nutrition_self_care",
            "Nutrition & Self-Care",
            "Treating the body as hardware — fuel, hygiene, and daily maintenance.",
            "Nutrition", "Meal planning", "Sleep hygiene", "Hygiene routines", "Fuel sort"
        ),
        sub(
            "physical_fitness",
            "Physical Fitness & PE",
            "Movement, strength, and energy management through play.",
            "Physical fitness", "PE", "Active play", "Stretching", "Energy output"
        ),
        sub(
            "financial_literacy",
            "Financial Literacy",
            "Money, saving, budgeting, and understanding value exchange.",
            "Money", "Saving", "Budgeting", "Token economy", "Piggy bank", "Investing basics", "Credit awareness"
        ),
        sub(
            "household_competence",
            "Household Competence",
            "Cooking, cleaning, repairs, and maintaining shared spaces.",
            "Cooking", "Cleaning", "Repair basics", "Room audit", "Tool safety", "Fix-it station"
        ),
        sub(
            "business_economics",
            "Business & Economics",
            "How value moves — shops, trades, cycles, and simple market dynamics.",
            "Business cycles", "Value exchange", "Market dynamics", "Needs vs wants"
        ),
        sub(
            "digital_citizenship",
            "Digital Citizenship",
            "Privacy, trusted adults, and guarding personal information.",
            "Privacy", "Cybersecurity basics", "Trusted team", "Secret keeper rules", "Screen boundaries"
        ),
        sub(
            "resource_management",
            "Resource Management",
            "Coupons, deals, planning, and stretching materials wisely.",
            "Coupon clipping", "Finding deals", "Resource allocation", "Planning ahead"
        )
    )

    private fun socialDynamics() = listOf(
        sub(
            "empathy_compassion",
            "Empathy & Compassion",
            "Perspective-taking and caring response to others' states.",
            "Empathy", "Social skills", "Active listening", "Puppet POV", "Helping hand"
        ),
        sub(
            "conflict_resolution",
            "Conflict Resolution",
            "Negotiation, de-escalation, and structured talk-it-out protocols.",
            "Negotiation", "De-escalation", "Turn timer", "Repeat back", "Peace mat"
        ),
        sub(
            "boundaries",
            "Boundaries & Consent",
            "Personal space, assertiveness, and access control to time and body.",
            "Boundaries", "Consent", "Personal space", "My bubble", "Ask first", "Stop sign"
        ),
        sub(
            "collaboration",
            "Collaboration & Teamwork",
            "Shared tasks, role division, and distributed problem-solving.",
            "Team coordination", "Manners", "Helper missions", "Build-a-block", "Heavy lift"
        ),
        sub(
            "social_etiquette",
            "Social Etiquette",
            "Greetings, turns, and navigating multi-player social environments.",
            "Manners", "Greetings", "Turn-taking", "Table manners", "Playground rules"
        )
    )

    private fun physicalMastery() = listOf(
        sub(
            "kinesthetic_fitness",
            "Kinesthetic Awareness & PE",
            "Balance, coordination, body control, and proprioceptive training.",
            "Physical fitness", "Coordination", "Bear walk", "Tape line", "Statue game"
        ),
        sub(
            "manual_dexterity",
            "Manual Dexterity",
            "Fine motor skills for building, threading, and precise tool use.",
            "Fine motor", "Bead stringing", "Nut and bolt", "Clay work", "Micro-assembly"
        ),
        sub(
            "environmental_stewardship",
            "Environmental Stewardship",
            "Resource loops — sort, conserve, and care for shared ecosystems.",
            "Stewardship", "Recycling", "Composting", "Light patrol", "Litter pickup"
        ),
        sub(
            "outdoor_survival",
            "Outdoor Survival & Safety",
            "Navigation, emergency awareness, and knowing safe spots at home and outside.",
            "Field navigation", "Emergency protocols", "House map", "Safe spot", "Plant ID"
        ),
        sub(
            "situational_awareness",
            "Situational Awareness",
            "Noticing surroundings, exits, and changes in the environment.",
            "Awareness drills", "Exit knowledge", "Weather cues", "Stranger safety basics"
        )
    )

    private fun creativeCuriosity() = listOf(
        sub(
            "artistic_expression",
            "Artistic Expression",
            "Music, visual art, performance, and procedural pattern beauty.",
            "Music", "Learning an instrument", "Drawing", "Sticker patterns", "Fractal fun"
        ),
        sub(
            "innovation_maker",
            "Innovation & Maker Pipeline",
            "Concept → prototype → iteration → final build.",
            "Maker-space", "Draw the plan", "Mockup", "Upgrade pass", "Creative building"
        ),
        sub(
            "philosophical_inquiry",
            "Philosophical Inquiry",
            "The great why — ethics, imagination, and open questions.",
            "Ethics of invention", "What-if stories", "Good/bad choices", "The great why"
        ),
        sub(
            "lifelong_learning",
            "Lifelong Learning & Meta-Learning",
            "How to teach yourself — docs, communities, trial and error.",
            "Research habits", "Try-it skill", "Watch and do", "Ask an expert"
        ),
        sub(
            "failure_recovery",
            "Failure Recovery & Oops Patch",
            "Dropping, fixing, retrying without shame.",
            "Oops patch", "Retry loop", "Look and learn", "Celebrate retries"
        )
    )

    private fun ethicsCivic() = listOf(
        sub(
            "moral_reasoning",
            "Moral Reasoning & Ethics",
            "Golden rule, platinum rule, and fairness in concrete situations.",
            "Integrity", "Fairness games", "Ethics in technology", "Ask what they want"
        ),
        sub(
            "integrity",
            "Integrity & Hash Verification",
            "Actions matching stated values — promises kept or repaired.",
            "Daily promise", "Truth box", "Values chat", "Promise repair"
        ),
        sub(
            "civic_responsibility",
            "Civic Responsibility",
            "Community participation as a helpful node in the network.",
            "Voting basics", "Governance awareness", "Neighborhood care", "Helper node"
        ),
        sub(
            "service",
            "Service & Community Contribution",
            "Pro-bono spirit, sharing, and lifting the group.",
            "Helping others", "Service missions", "Share a toy", "Family chores"
        ),
        sub(
            "strategic_planning",
            "Strategic Planning & Adventure Budgets",
            "Road trips, logistics, and combining travel with resource allocation.",
            "Road trip planning", "Adventure budgets", "Two paths", "Consequence thinking"
        ),
        sub(
            "calibration",
            "Calibration & Version Control",
            "Tracking improvement over time — version 1.1 thinking.",
            "Star chart", "Version check", "Old drawing compare", "Progress log"
        )
    )

    private fun sub(
        id: String,
        name: String,
        summary: String,
        vararg topics: String
    ) = CurriculumSubdomain(
        id = id,
        name = name,
        summary = summary,
        topics = topics.toList()
    )
}
