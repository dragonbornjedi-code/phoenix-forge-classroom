package com.phoenixforge.classroom.teacher.domain.curriculum

/**
 * Pack 01 — one starter lesson per Curriculum Of Life domain.
 * Source: phoenix-forge-classroom-teacher-edition/docs/starter-lessons-pack-01.md
 */
object StarterLessonsPack01 {

    const val PACK_ID = "pack_01"

    val lessons: List<StarterLesson> = listOf(
        StarterLesson(
            id = "pack01_secret_label_decoder",
            title = "Secret Label Decoder",
            domainId = CurriculumDomainId.COGNITIVE_ACADEMIC,
            subcategories = listOf("Early Literacy", "Documentation Literacy", "Abstraction Mapping"),
            primaryMethods = listOf("Explicit teaching", "Play-based learning", "Retrieval practice", "UDL visual supports"),
            studentMission = "Find the secret labels around the room and match them to the right objects.",
            durationStandard = "10-15 minutes",
            durationLowDemand = "3 labels in 3 minutes",
            materials = "Sticky notes or index cards, marker, 5-10 household objects, optional picture cards, finished box.",
            objective = "Ezra will match printed labels to real objects and explain one object by its job.",
            whyItMatters = "This connects reading to useful information. Words become tools for finding, organizing, and solving.",
            setup = "Write simple labels: cup, door, bed, light, book, tool, sock, box. Add one picture if needed.",
            steps = listOf(
                "Show one label and say, \"This word is a clue. It says cup.\"",
                "Model matching the label to the cup.",
                "Ask him to place the next label.",
                "When he places it, ask, \"What job does this thing do?\"",
                "Put completed labels in the finished box or leave them attached.",
                "End with retrieval: point to 3 labels and ask, \"Which one says book?\""
            ),
            questions = listOf(
                "What do your eyes notice?",
                "What sound does this word start with?",
                "What job does this object do?",
                "Is this object also part of a bigger system?"
            ),
            metric = "Labels matched; prompt level (model, verbal, gesture, independent); explains object job.",
            evidence = "Photo of labeled objects; teacher note on independent labels.",
            supports = "Visual: picture label. Sensory: stand and move. Communication: point. Transition: first 3 labels, then build.",
            makeEasier = "2 labels and obvious objects; pictures beside words; choose between two objects.",
            makeHarder = "Remove pictures; add beginning sound sorting; create a new label.",
            recovery = "If frustration rises, adult labels wrong object and he fixes it.",
            studentEditionNotes = "3 mission cards: Find Cup, Find Book, Find Light. Reward: Decoder Badge.",
            lessonPatternId = "label_hunt"
        ),
        StarterLesson(
            id = "pack01_system_temp_gauge",
            title = "Build The System Temp Gauge",
            domainId = CurriculumDomainId.EMOTIONAL_REGULATION,
            subcategories = listOf("Feeling Identification", "Energy And Arousal", "Independent Regulation", "Safety During Big Feelings"),
            primaryMethods = listOf("Visual supports", "Co-regulation", "Explicit teaching", "Roleplay"),
            studentMission = "Build your color gauge: blue, yellow, red.",
            durationStandard = "15 minutes",
            durationLowDemand = "Point to one color and take one breath",
            materials = "Paper plate or paper, blue/yellow/red crayons, clothespin or arrow marker, optional emotion face cards.",
            objective = "Ezra will identify a body/emotion state using blue/yellow/red and practice one matching support.",
            whyItMatters = "The color system creates shared language before emotions get too big for words.",
            setup = "Draw three zones: blue calm, yellow heating, red too much. Keep wording simple.",
            steps = listOf(
                "Explain: \"This is not good or bad. It tells us what your system needs.\"",
                "Model yourself: \"I am blue because my body feels calm.\"",
                "Roleplay yellow: tense hands, frustrated face. Move marker to yellow.",
                "Practice support: pause, wall push, feather breath, or water break.",
                "Roleplay red with a puppet. Puppet goes to Recovery Hub.",
                "Ask him to point to how he feels now."
            ),
            questions = listOf(
                "What color is your engine?",
                "What does yellow need?",
                "Where can red go to get safe?",
                "What helped your body?"
            ),
            metric = "Identifies color; accepts one support; matches support to color.",
            evidence = "Photo of gauge; teacher note on preferred support.",
            supports = "Visual: color gauge. Sensory: movement or pressure. Communication: point. Transition: gauge before hard tasks.",
            makeEasier = "Only blue and yellow today; adult models, child points.",
            makeHarder = "Add body signals; add \"what helped?\" after a real moment.",
            recovery = "If he enters red, stop lesson. Use the gauge later when calm.",
            studentEditionNotes = "Daily check-in: three big buttons Blue, Yellow, Red. No adult interpretation.",
            lessonPatternId = "system_temp"
        ),
        StarterLesson(
            id = "pack01_snack_shop_token_pitch",
            title = "Snack Shop Token Pitch",
            domainId = CurriculumDomainId.PRACTICAL_LIFE,
            subcategories = listOf("Financial Literacy", "Nutrition And Hydration", "Decision Architecture", "Oral Language"),
            primaryMethods = listOf("CRA", "Play-based learning", "Explicit teaching", "Retrieval practice"),
            studentMission = "Open the snack shop. Choose, count, and explain your snack.",
            durationStandard = "15-20 minutes",
            durationLowDemand = "Choose one snack and count one token",
            materials = "3 snack choices, 5 tokens, paper shop signs, cup of water, optional small ledger.",
            objective = "Ezra will use tokens to make a choice and give one reason for the choice.",
            whyItMatters = "Connects money sense, food planning, agency, and simple reasoning.",
            setup = "Token costs: apple slices 1, crackers 1, cheese 2, special treat 3.",
            steps = listOf(
                "Show tokens and say, \"Tokens help us choose.\"",
                "Model buying water for 0 tokens because the body needs it.",
                "Let him choose a snack.",
                "Count tokens together.",
                "Ask for a pitch: \"Why does your body or brain need this?\"",
                "Record choice with a mark or sticker.",
                "End with recall: \"What did you buy? How many tokens?\""
            ),
            questions = listOf(
                "How many tokens does it cost?",
                "What does this food do for your body?",
                "Do you want to spend or save?",
                "What happens if you save one token?"
            ),
            metric = "Counts tokens one-to-one; gives one reason; accepts spend/save choice.",
            evidence = "Ledger mark; teacher note on counting and reason.",
            supports = "Visual: prices with dots. Sensory: crunchy/soft options. Communication: point. Transition: after lesson block.",
            makeEasier = "Two choices, same cost; adult counts aloud.",
            makeHarder = "Add save-for-later; \"you have 3 tokens — what can you buy?\"",
            recovery = "If choice overload, reduce to two options and remove treat category.",
            studentEditionNotes = "Child sees snack cards and token count. Teacher keeps nutrition notes.",
            lessonPatternId = "token_ledger"
        ),
        StarterLesson(
            id = "pack01_puppet_peace_protocol",
            title = "Puppet Peace Protocol",
            domainId = CurriculumDomainId.SOCIAL_DYNAMICS,
            subcategories = listOf("Empathy And Perspective", "Conflict Resolution", "Boundaries And Consent", "Listening And Reflecting"),
            primaryMethods = listOf("Social narrative", "Roleplay", "Co-regulation", "Explicit teaching"),
            studentMission = "Help two puppets solve a toy problem.",
            durationStandard = "10-15 minutes",
            durationLowDemand = "Practice one phrase: \"Can I have a turn?\"",
            materials = "Two toys or puppets, one desirable object, timer, stop-sign card.",
            objective = "Ezra will practice one conflict script: stop, listen, ask, solve.",
            whyItMatters = "Conflict skills need rehearsal before real conflict. Puppets make it safer.",
            setup = "Two puppets both want the same block/tool.",
            steps = listOf(
                "Puppet A grabs. Puppet B says, \"Stop.\"",
                "Ask, \"What does Puppet B want?\"",
                "Model repeat-back: \"You wanted the block.\"",
                "Offer two solutions: timer turn or trade.",
                "Let Ezra choose a solution.",
                "Practice the phrase with his voice or puppet voice.",
                "Run the scene again with him as helper."
            ),
            questions = listOf(
                "What does each puppet want?",
                "What can they say?",
                "What solution helps both?",
                "How do we repair if grabbing happened?"
            ),
            metric = "Identifies want/feeling; uses boundary phrase; chooses a solution.",
            evidence = "Teacher note on phrase used; optional photo of peace mat/timer.",
            supports = "Visual: stop card. Sensory: peace mat. Communication: puppet speaks. Transition: after sharing practice.",
            makeEasier = "Adult narrates; child chooses solution card.",
            makeHarder = "Add apology/repair; role swap.",
            recovery = "If too real or frustrating, switch to adult-only puppet show.",
            studentEditionNotes = "Puppet faces and solution buttons: Timer, Trade, Ask First.",
            lessonPatternId = "puppet_pov"
        ),
        StarterLesson(
            id = "pack01_field_kit_safe_map",
            title = "Field Kit And Safe Map",
            domainId = CurriculumDomainId.PHYSICAL_MASTERY,
            subcategories = listOf("Situational Awareness", "Field Survival Basics", "Travel Readiness", "Gross Motor"),
            primaryMethods = listOf("Task analysis", "Visual supports", "Explicit teaching", "Play-based movement"),
            studentMission = "Pack your field kit and find the safe spots.",
            durationStandard = "20 minutes",
            durationLowDemand = "Pack 3 items",
            materials = "Small backpack, water bottle, snack, hat/jacket, simple map paper, crayons.",
            objective = "Ezra will pack a basic outing bag and identify one safe adult/safe spot.",
            whyItMatters = "Travel readiness builds autonomy, safety, planning, and confidence.",
            setup = "Choose a real or pretend destination: yard, park, library, car walk.",
            steps = listOf(
                "Show destination: \"We are going to the yard/park/library.\"",
                "Ask, \"What does our body need?\"",
                "Pack water, snack, clothing item, and comfort/safety item.",
                "Draw a simple map: start, path, destination, safe spot.",
                "Walk the path or pretend it with toys.",
                "Practice rule: \"If lost, stop and call for my adult.\""
            ),
            questions = listOf(
                "What do we need if we get thirsty?",
                "Where is the safe spot?",
                "Who is your safe adult?",
                "What do you do if you cannot see me?"
            ),
            metric = "Packs items with prompt level; identifies safe spot/adult; follows route with support.",
            evidence = "Photo of packed kit or map; teacher safety note.",
            supports = "Visual: packing checklist. Sensory: comfort item. Communication: point to safe spot. Transition: preview before leaving.",
            makeEasier = "Pretend trip inside; 3-item checklist.",
            makeHarder = "Check weather and choose jacket/hat; add map landmarks.",
            recovery = "If safety talk creates anxiety, return to helper packing mission; keep tone calm.",
            studentEditionNotes = "Checklist icons: Water, Snack, Hat, Adult.",
            lessonPatternId = "home_map"
        ),
        StarterLesson(
            id = "pack01_cardboard_prototype_lab",
            title = "Cardboard Prototype Lab",
            domainId = CurriculumDomainId.CREATIVE_CURIOSITY,
            subcategories = listOf("Maker Pipeline", "Design Thinking", "Failure Recovery", "Presentation And Sharing"),
            primaryMethods = listOf("Project-based learning", "Productive struggle", "Task analysis", "Play-based learning"),
            studentMission = "Build V1, test it, then add one upgrade.",
            durationStandard = "25-35 minutes",
            durationLowDemand = "Build with blocks 5 minutes and add one upgrade",
            materials = "Cardboard scraps, tape, child-safe scissors, crayons/markers, toy user, camera for V1/V2.",
            objective = "Ezra will create a prototype, test it with a toy user, and add one improvement.",
            whyItMatters = "Builds the maker pipeline: idea, prototype, test, upgrade, explain.",
            setup = "Prompt: \"Build something that helps this toy cross, carry, sit, hide, or travel.\"",
            steps = listOf(
                "Choose the toy user.",
                "Ask, \"What does the toy need?\"",
                "Draw a fast plan or point to materials.",
                "Build V1.",
                "Test with the toy.",
                "Name one problem or one cool thing.",
                "Add one upgrade.",
                "Photograph V1 and V2.",
                "Present: \"I built __. I changed __.\""
            ),
            questions = listOf(
                "Who is this for?",
                "What job does it do?",
                "What did V1 do well?",
                "What is one upgrade?"
            ),
            metric = "Idea to build; tests with toy; adds one change; explains change.",
            evidence = "V1/V2 photo; teacher note on persistence and support level.",
            supports = "Visual: draw first. Sensory: tape instead of glue. Communication: point to upgrade. Transition: timer before cleanup.",
            makeEasier = "Use blocks instead of cardboard; adult tapes while he places.",
            makeHarder = "Constraint: hold 3 blocks, fit in a box, or only 5 pieces.",
            recovery = "\"Great test. The test found the patch.\" Reduce challenge.",
            studentEditionNotes = "Badge: Prototype Lab. Show V1, Test, Upgrade, Show.",
            lessonPatternId = "maker_pipeline"
        ),
        StarterLesson(
            id = "pack01_fair_split_mission",
            title = "Fair Split Mission",
            domainId = CurriculumDomainId.ETHICS_CIVIC,
            subcategories = listOf("Moral Reasoning", "Golden And Platinum Rule", "Decision Architecture", "Service And Contribution"),
            primaryMethods = listOf("CRA", "Social narrative", "Play-based learning", "Explicit teaching"),
            studentMission = "Help split the treasure fairly.",
            durationStandard = "10-15 minutes",
            durationLowDemand = "Split 4 items between 2 people",
            materials = "Small snack pieces, blocks, stickers, or pretend treasure; two plates; puppet or family member.",
            objective = "Ezra will split items fairly and discuss whether fair always means the same.",
            whyItMatters = "Fairness is easiest to learn with concrete objects before abstract morality.",
            setup = "Use 6-10 identical items. Add a puppet who likes or needs something different.",
            steps = listOf(
                "Say, \"We are making it fair.\"",
                "Split 4 items equally: one for you, one for me.",
                "Ask if it is fair.",
                "Add twist: puppet wants only one or needs the bigger piece.",
                "Talk about same vs what someone needs.",
                "End with a helper action: give someone their share kindly."
            ),
            questions = listOf(
                "Did everyone get enough?",
                "Is fair always the same?",
                "What did the other person want?",
                "What is one kind way to give it?"
            ),
            metric = "Splits one-to-one; identifies same/equal; notices another preference or need.",
            evidence = "Teacher note on fairness reasoning.",
            supports = "Visual: two plates. Sensory: non-food if snack distracts. Communication: point fair/not fair. Transition: before snack.",
            makeEasier = "4 objects, equal split only.",
            makeHarder = "Odd number; add preference/need; invent a fair solution.",
            recovery = "If food sharing creates distress, use blocks or stickers.",
            studentEditionNotes = "Treasure and two bowls. Simple wording: \"Help everyone get what they need.\"",
            lessonPatternId = "fair_split"
        )
    )

    fun lessonById(id: String): StarterLesson? = lessons.firstOrNull { it.id == id }

    fun lessonsForDomain(domainId: CurriculumDomainId): List<StarterLesson> =
        lessons.filter { it.domainId == domainId }
}
