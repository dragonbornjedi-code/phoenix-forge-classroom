# Teacher Edition Product Spec

Teacher Edition is the adult/admin APK. It plans the homeschool day, manages students, receives Student Edition data, and keeps private teacher notes.

## Primary Tabs

### Today
Landing page for the whole day.

Purpose:
- Show the complete daily itinerary.
- Surface materials, meals, travel, lessons, activities, chores, breaks, and recovery windows.
- Show Student Edition sync status for each student.
- Help the teacher run the day without opening several screens.

Core fields:
- Date.
- Day title.
- Daily theme.
- Students included.
- Weather/context notes.
- Teacher readiness notes.
- Wake/breakfast/start anchors.
- Time blocks.
- Lessons.
- Activities.
- Materials needed.
- Food and hydration plan.
- Destinations and travel plan.
- Field trip details.
- Chores and life-skills practice.
- Outdoor time.
- Movement/physical mastery.
- Quiet/rest time.
- Read-aloud/story time.
- Maker/build time.
- Screen/app time.
- Social/community time.
- Emotional check-in plan.
- Recovery tools available.
- Assessment/check-for-understanding points.
- Evidence to collect.
- End-of-day reflection.
- Carryover notes for tomorrow.

Time block fields:
- Start time.
- End time.
- Block type: lesson, meal, travel, field trip, activity, movement, rest, chore, maker, free play, appointment, review.
- Title.
- Student.
- Location.
- Objective.
- Materials.
- Instructions.
- Teacher notes.
- Student-facing mission link.
- Expected output.
- Assessment method.
- Differentiation/adaptation options.
- Transition cue.
- Recovery option.
- Completion status.
- Actual start/end.
- What changed.

### Students
Manage student profiles and cross-app linking.

Student profile fields:
- Student display name.
- Birthdate or age.
- Grade/stage.
- Avatar/photo.
- Interests and motivators.
- Current skill goals.
- Reading/math stage notes.
- Sensory preferences.
- Regulation supports that work.
- Overload cues to watch for.
- Food/allergy notes.
- Medical/safety notes.
- Emergency contacts.
- Privacy/export permissions.
- Linked Student Edition device/app identity.
- Sync status.
- Last import time.
- Last export time.

Student data imported from Student Edition:
- Assignment completion.
- Lesson/game scores.
- Attempts.
- Time spent.
- Hints used.
- Retry count.
- Emotional check-in data.
- Energy check-ins.
- Break requests.
- Favorite activities.
- Friction points.
- Student self-reflections.
- Badge/reward progress.
- Offline activity logs.
- Error reports or blocked tasks.

Student data exported to Student Edition:
- Assigned missions.
- Lesson plans converted to child-facing tasks.
- Game/activity queue.
- Simple schedule blocks.
- Reward goals.
- Visual cues.
- Allowed content.
- Teacher-approved messages.

Sync actions:
- Link Student Edition.
- Import student data.
- Export assignments.
- Resolve conflicts.
- View sync history.
- Unlink device.
- Manual backup.

### Lesson Plans
Plan by day, week, month, or category.

Subsections:
- Daily Itinerary.
- Weekly Overview.
- Monthly Overview.
- Categories.

Daily Itinerary view:
- Same data model as the Today landing page.
- Editable planning mode.
- Duplicate/copy from another day.
- Convert lessons into Student Edition missions.
- Print/export day plan.

Weekly Overview fields:
- Week start/end.
- Weekly theme.
- Big goals.
- Daily focus for each day.
- Lessons per day.
- Activities per day.
- Materials to gather for the week.
- Food/meal prep notes.
- Destinations/field trips.
- Appointments/classes.
- Chores/life skills.
- Outdoor/community time.
- Assessment checkpoints.
- Flexible/catch-up block.
- Teacher prep checklist.

Monthly Overview fields:
- Month.
- Monthly theme.
- Core skills.
- Books/media.
- Major projects.
- Field trips/destinations.
- Holidays/events.
- Materials budget.
- Habit focus.
- Progress goals.
- Weekly themes.
- End-of-month review.

Categories view:
- Search and filter lesson plans by category.
- Show readiness, duration, materials, energy level, indoor/outdoor, and Student Edition availability.
- Categories follow the seven Curriculum Of Life sections.
- Allow custom categories such as math, reading, STEM, life skills, cooking, field trips, maker projects, emotional regulation, physical mastery, civic duty, and free play.

Lesson plan fields:
- Title.
- Category.
- Age/stage.
- Duration.
- Energy level.
- Indoor/outdoor.
- Objective.
- Vocabulary.
- Materials.
- Setup.
- Steps.
- Questions to ask.
- Student-facing mission text.
- Assessment/check-for-understanding.
- Differentiation/adaptation options.
- Behavioral cues to watch.
- Recovery option.
- Extension activity.
- Evidence to collect.
- Teacher reflection.
- Linked Student Edition game/activity.

### Insights
Adult-only behavior and progress review.

Views:
- Student progress timeline.
- Emotional check-in trends.
- Lesson completion and score trends.
- Friction/recovery pattern review.
- Method effectiveness review.
- Evidence gallery.
- Weekly audit.

### Library
Reusable plans, materials, destinations, meals, activities, games, and routines.

Library item types:
- Lesson templates.
- Activity templates.
- Material kits.
- Meal/snack plans.
- Destination profiles.
- Field trip checklists.
- Recovery tools.
- Morning routines.
- Weekly/monthly themes.

## Daily Itinerary Reference Fields

Educator and homeschool planning commonly includes timing, objectives, materials, procedures/activities, assessment, modifications, reflection, meals/breaks, field trips, and flexible routines. Teacher Edition should support all of those without forcing every field every day.

Recommended default itinerary sections:
- Morning routine.
- Breakfast/food prep.
- Emotional and energy check-in.
- Core learning block.
- Movement/outdoor block.
- Snack/hydration.
- Hands-on activity.
- Life skill/chore.
- Lunch.
- Rest/quiet/read-aloud.
- Field trip/destination or community block.
- Maker/game/project block.
- Review/reflection.
- Free play.
- Dinner/family wrap-up notes.
- Tomorrow prep.

## Cross-App Rule

Teacher Edition owns planning, adult notes, interpretation, and exports.

Student Edition owns child-facing interaction, completion events, scores, check-ins, and local play/lesson data.

Neither app should require cloud sync for the core homeschool workflow.

## Planning References

Reference patterns used for this spec:
- Daily lesson-plan templates commonly track lesson objectives, materials, assessment, procedures/activities, closure, notes, and reflection.
- Teacher planner examples commonly include focus/goals, standards/objectives, materials, activities, assessment, differentiation, and reflection.
- Homeschool schedule examples commonly include morning routine, core lessons, creative play, lunch, rest/quiet time, outdoor learning, life skills, field trips, meals, breaks, and flexible weekly planning.
- Field-trip planning examples commonly include destinations, class/activity rotations, meals, group logistics, and schedule blocks.
