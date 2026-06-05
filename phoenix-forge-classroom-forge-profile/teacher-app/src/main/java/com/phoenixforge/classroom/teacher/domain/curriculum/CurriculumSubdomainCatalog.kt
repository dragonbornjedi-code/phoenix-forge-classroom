package com.phoenixforge.classroom.teacher.domain.curriculum

/**
 * Subdomains within each Curriculum Of Life domain — teachable skill groups
 * (reading, money, breathwork, memory systems, worldbuilding, etc.) before lesson activities.
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

    /** 12 subdomains */
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
            "Writing", "English", "Journaling", "Letter formation", "Clear expression"
        ),
        sub(
            "numeracy_math",
            "Mathematics, Numeracy & Telling Time",
            "Number sense, patterns, measurement, clocks, and everyday math fluency.",
            "Mathematics", "Counting", "Patterns", "Telling time", "Measurement", "Money prep"
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
        ),
        sub(
            "memory_retrieval",
            "Memory Architecture & Retrieval",
            "How to memorize, use flashcards, and organize internal data before touching AI systems.",
            "Flashcards", "Spaced repetition", "Chunking", "Recall drills", "Memory palaces (age-scaled)"
        ),
        sub(
            "spatial_geometry",
            "Spatial Reasoning & Geometry",
            "Bridges math with 3D design, blocks, orientation, and structural literacy.",
            "Geometry", "Blocks and builds", "3D orientation", "Spatial puzzles", "Structural literacy"
        ),
        sub(
            "research_literacy",
            "Research & Information Literacy",
            "Finding answers in books, docs, and trusted sources — not guessing.",
            "Look it up", "Index and TOC", "Ask an expert", "Compare sources", "Fact vs opinion"
        ),
        sub(
            "media_literacy",
            "Media Literacy",
            "Understanding stories, ads, and messages in media — distinct from digital citizenship.",
            "Ads vs facts", "Story tone", "Who made this?", "Bias spotting", "Healthy skepticism"
        )
    )

    /** 11 subdomains */
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
            "frustration_tolerance",
            "Frustration Tolerance (The Calibration Node)",
            "Walking away from a broken system without emotional crash — essential for coding and mechatronics.",
            "Step away", "Retry later", "Broken build calm", "99% fail is normal", "Recalibrate"
        ),
        sub(
            "resilience_recovery",
            "Resilience & Recovery",
            "Bouncing back after setbacks with repair, not shame.",
            "Oops and fix", "Try again tomorrow", "Repair script", "Celebrate effort", "Post-moment review"
        ),
        sub(
            "focus_attention",
            "Focus, Attention & Solitude Mastery",
            "Sitting quietly with a problem alone — deep focus without constant digital nudges.",
            "Deep focus", "Quiet work block", "Listen game", "Single-task play", "Solitude practice"
        ),
        sub(
            "delayed_gratification",
            "Delayed Gratification",
            "Waiting for bigger rewards and tolerating not-yet.",
            "Token save", "Wait for treat", "10-second wait", "Earn before spend", "Patience games"
        ),
        sub(
            "identity_self_concept",
            "Identity & Self-Concept",
            "Who Ezra is becoming — ties directly to Forge Profile identity formation.",
            "Forge name", "I am becoming", "Success jar", "Agency talk", "Growth narrative"
        ),
        sub(
            "self_agency",
            "Agency & Internal Locus of Control",
            "Successes and setbacks as learnable — not luck or fixed labels.",
            "I did this", "My choice", "What I can change", "Growth mindset", "Ownership"
        ),
        sub(
            "mindfulness",
            "Mindfulness & Presence",
            "Checking internal state like a system status report.",
            "Meditation", "Body check", "Energy meter", "System load", "Focused awareness"
        ),
        sub(
            "shadow_processing",
            "Shadow Work & Pattern Processing",
            "Gently noticing recurring triggers and subconscious patterns (teacher-framed).",
            "Pattern noticing", "Trigger mapping", "Yellow vs red state", "Repair after upset"
        )
    )

    /** 12 subdomains */
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
            "Money", "Saving", "Budgeting", "Token economy", "Piggy bank", "Investing basics"
        ),
        sub(
            "cooking_meals",
            "Cooking & Meal Preparation",
            "Safe kitchen skills, following steps, and feeding the organic hardware platform.",
            "Cooking", "Measuring", "Recipe steps", "Knife safety (age-scaled)", "Taste and adjust"
        ),
        sub(
            "home_stewardship",
            "Home Stewardship",
            "Cleaning, organization, laundry, and keeping shared spaces functional.",
            "Cleaning", "Laundry", "Organization", "Room audit", "Put-away systems"
        ),
        sub(
            "maintenance_mechanical",
            "Maintenance & Mechanical Literacy",
            "Caring for bikes, 3D printers, tools — tighten a bolt, check a battery terminal.",
            "Bolt tightening", "Battery check", "Tool care", "Bicycle basics", "3D printer upkeep"
        ),
        sub(
            "first_aid_triage",
            "First Aid & Emergency Response",
            "Basic physical intervention — scrape, burn, and when to call for help.",
            "Wash a scrape", "Burn basics", "When to call 911", "First aid kit", "Stay calm"
        ),
        sub(
            "tool_literacy",
            "Tool Literacy",
            "Choosing, carrying, and using household and maker tools safely.",
            "Screwdriver", "Pliers", "Tape measure", "Glue gun safety", "Right tool for job"
        ),
        sub(
            "transportation_literacy",
            "Transportation Literacy",
            "Maps, navigation, public spaces, and getting somewhere safely.",
            "Maps", "Street signs", "Bus or car rules", "Neighborhood navigation", "Public spaces"
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
            "Privacy, trusted adults, and guarding personal information online and off.",
            "Privacy", "Cybersecurity basics", "Trusted team", "Screen boundaries", "Secret keeper rules"
        ),
        sub(
            "resource_management",
            "Resource Management",
            "Coupons, deals, planning, and stretching materials wisely.",
            "Coupon clipping", "Finding deals", "Resource allocation", "Planning ahead"
        )
    )

    /** 9 subdomains */
    private fun socialDynamics() = listOf(
        sub(
            "empathy_compassion",
            "Empathy & Compassion",
            "Perspective-taking and caring response to others' states.",
            "Empathy", "Puppet POV", "Helping hand", "Sad friend", "Active listening"
        ),
        sub(
            "communication_conversation",
            "Communication & Conversation",
            "Starting, sustaining, and ending conversations with clarity and respect.",
            "Greetings", "Ask and answer", "Eye contact", "Turn in talk", "Clear requests"
        ),
        sub(
            "conflict_resolution",
            "Conflict Resolution",
            "De-escalation and structured talk-it-out without defaulting to authority.",
            "Peace mat", "Repeat back", "Turn timer", "Cool down", "Repair after fight"
        ),
        sub(
            "negotiation_trade",
            "Negotiation & Trade",
            "Fair value exchange with peers — trades, deals, and compromise.",
            "Fair trade", "Make a deal", "Both win", "Walk away option", "Value talk"
        ),
        sub(
            "boundaries",
            "Boundaries & Consent",
            "Personal space, assertiveness, and access control to time and body.",
            "My bubble", "Ask first", "Stop sign", "No means no", "Personal space"
        ),
        sub(
            "collaboration",
            "Collaboration & Teamwork",
            "Shared tasks, role division, and distributed problem-solving.",
            "Team build", "Heavy lift", "One piece each", "Helper missions", "Role swap"
        ),
        sub(
            "leadership_followership",
            "Leadership & Followership",
            "Knowing when to lead, when to follow, and how to switch roles.",
            "Take a turn leading", "Follow the plan", "Captain and crew", "Delegate", "Support leader"
        ),
        sub(
            "institutional_navigation",
            "Institutional Navigation",
            "Talking confidently to librarians, clerks, utility workers, and officers.",
            "Library visit", "Store clerk", "Ask for help", "Polite authority talk", "Community workers"
        ),
        sub(
            "hospitality_community",
            "Hospitality & Community Building",
            "Welcoming others, shared meals, and strengthening group belonging.",
            "Welcome guest", "Share snack", "Group ritual", "Thank-you notes", "Neighborhood hello"
        )
    )

    /** 9 subdomains */
    private fun physicalMastery() = listOf(
        sub(
            "body_coordination",
            "Body Awareness & Coordination",
            "Balance, proprioception, and controlling movement in space.",
            "Bear walk", "Tape line", "Statue game", "Balance beam", "Body control"
        ),
        sub(
            "kinesthetic_fitness",
            "Physical Fitness & PE",
            "Strength, endurance, and active play for growing bodies.",
            "Running", "Climbing", "Stretching", "Active play", "PE games"
        ),
        sub(
            "manual_dexterity",
            "Fine Motor & Manual Dexterity",
            "Threading, beading, clay, and precise hand work for builds.",
            "Bead stringing", "Nut and bolt", "Clay work", "Scissors", "Micro-assembly"
        ),
        sub(
            "tool_handling_physical",
            "Tool Handling & Physical Competence",
            "Safe grip, stance, and control with screwdrivers, glue guns, and real tools.",
            "Screwdriver grip", "Glue gun stance", "Hammer basics", "Steady hands", "Tool safety"
        ),
        sub(
            "environmental_stewardship",
            "Environmental Stewardship",
            "Resource loops — sort, conserve, and care for shared ecosystems.",
            "Recycling", "Composting", "Light patrol", "Litter pickup", "Water care"
        ),
        sub(
            "outdoor_navigation",
            "Outdoor Navigation",
            "Trail awareness, landmarks, and finding the way back.",
            "Trail markers", "Compass intro", "Landmarks", "Map on walk", "Sun position"
        ),
        sub(
            "outdoor_survival",
            "Outdoor Survival & Safety",
            "Shelter basics, safe spots, and emergency awareness outdoors.",
            "Safe spot", "House map", "Plant ID", "Weather watch", "Emergency bag"
        ),
        sub(
            "risk_assessment",
            "Risk Assessment",
            "Noticing danger, choosing safer paths, and asking before trying.",
            "Hot vs safe", "Height rules", "Water safety", "Stranger protocols", "Stop and think"
        ),
        sub(
            "health_recovery",
            "Health & Recovery",
            "Rest, hydration, stretching, and listening when the body needs downtime.",
            "Hydration", "Rest day", "Stretch", "Ice or heat basics", "Sleep as repair"
        )
    )

    /** 10 subdomains */
    private fun creativeCuriosity() = listOf(
        sub(
            "storytelling_narrative",
            "Storytelling & Narrative Design",
            "Plot, character, and consistent worlds — foundation for Ezra's Quest / Embral.",
            "Story arc", "Character wants", "Beginning middle end", "Tell a tale", "Plot consistency"
        ),
        sub(
            "narrative_worldbuilding",
            "Narrative Weaving & Worldbuilding",
            "Rules of imagination, lore, and tracking context across sessions.",
            "World rules", "Map of place", "Lore book", "Quest hooks", "Context tracking"
        ),
        sub(
            "music_rhythm",
            "Music & Rhythm",
            "Beat, pattern, instrument play, and rhythm as math made audible.",
            "Clapping patterns", "Instrument play", "Song making", "Rhythm copy", "Beat box"
        ),
        sub(
            "visual_design",
            "Visual Design",
            "Color, layout, contrast, and making things legible and beautiful.",
            "Color rules", "Layout", "Poster design", "Contrast", "Shape language"
        ),
        sub(
            "artistic_expression",
            "Artistic Expression",
            "Drawing, performance, and procedural pattern beauty.",
            "Drawing", "Sticker patterns", "Fractal fun", "Performance", "Rule art"
        ),
        sub(
            "innovation_maker",
            "Innovation & Maker Pipeline",
            "Concept → prototype → iteration → final build.",
            "Draw the plan", "Mockup", "Upgrade pass", "Maker-space", "Build pipeline"
        ),
        sub(
            "inventing_experimentation",
            "Inventing & Experimentation",
            "Trying wild ideas, testing one variable, and playful R&D.",
            "What if build", "One change test", "Prototype v2", "Lab notebook", "Wild idea hour"
        ),
        sub(
            "philosophical_inquiry",
            "Philosophical Inquiry",
            "The great why — ethics, imagination, and open questions.",
            "The great why", "What-if stories", "Good/bad choices", "Ethics play", "Big questions"
        ),
        sub(
            "play_imagination",
            "Play & Imagination",
            "Unstructured creation, pretend play, and following curiosity.",
            "Pretend play", "Free build", "Imagination hour", "Toy stories", "Curiosity chase"
        ),
        sub(
            "lifelong_learning",
            "Lifelong Learning & Meta-Learning",
            "How to teach yourself — docs, communities, trial and error.",
            "Try-it skill", "Watch and do", "Ask an expert", "Research habits", "Learn how to learn"
        ),
        sub(
            "failure_recovery",
            "Failure Recovery & Oops Patch",
            "Dropping, fixing, retrying creative work without shame — maker mindset.",
            "Oops patch", "Retry loop", "Look and learn", "Celebrate retries", "Version 2"
        )
    )

    /** 11 subdomains */
    private fun ethicsCivic() = listOf(
        sub(
            "moral_reasoning",
            "Moral Reasoning & Ethics",
            "Golden rule, platinum rule, and fairness in concrete situations.",
            "Fairness games", "Platinum rule", "Ethics in tech", "Ask what they want", "Right vs kind"
        ),
        sub(
            "justice_fairness",
            "Justice & Fairness",
            "Splitting fairly, spotting unfairness, and repairing imbalance.",
            "Fair split", "Take turns", "Unequal needs", "Repair unfairness", "Include everyone"
        ),
        sub(
            "integrity",
            "Integrity & Hash Verification",
            "Actions matching stated values — promises kept or repaired.",
            "Daily promise", "Truth box", "Values chat", "Promise repair", "Walk the talk"
        ),
        sub(
            "truth_seeking",
            "Truth Seeking",
            "Honesty, admitting mistakes, and correcting the record.",
            "Tell the truth", "Fix a mistake", "Correct the story", "I was wrong", "Fact check"
        ),
        sub(
            "responsibility_stewardship",
            "Responsibility & Stewardship",
            "Owning tasks, tools, and outcomes — not just participation.",
            "Chore ownership", "Care for tools", "Finish what you start", "Steward role", "Accountability"
        ),
        sub(
            "civic_responsibility",
            "Civic Responsibility",
            "Community participation as a helpful node in the network.",
            "Voting basics", "Neighborhood care", "Helper node", "Rules of the group", "Public good"
        ),
        sub(
            "service",
            "Service & Community Contribution",
            "Pro-bono spirit, sharing, and lifting the group.",
            "Service missions", "Share a toy", "Family chores", "Help a neighbor", "Give time"
        ),
        sub(
            "gratitude",
            "Gratitude",
            "Noticing gifts, saying thanks, and reciprocity.",
            "Thank-you", "Gratitude jar", "Notice helpers", "Write thanks", "Appreciation ritual"
        ),
        sub(
            "legacy_contribution",
            "Legacy & Contribution",
            "What Ezra leaves behind — artifacts, stories, and lasting help.",
            "Memory artifact", "Family story", "Build to last", "Teach a sibling", "Leave it better"
        ),
        sub(
            "strategic_planning",
            "Strategic Planning & Adventure Budgets",
            "Road trips, logistics, and combining travel with resource allocation.",
            "Road trip plan", "Adventure budget", "Two paths", "Pack list", "Consequence thinking"
        ),
        sub(
            "calibration",
            "Calibration & Version Control",
            "Tracking improvement over time — version 1.1 thinking.",
            "Star chart", "Version check", "Old drawing compare", "Progress log", "Better than yesterday"
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
