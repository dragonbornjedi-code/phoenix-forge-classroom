package com.phoenixforge.classroom.teacher.data.students

import android.net.Uri

object StudentSyncContract {
    const val AUTHORITY = "com.phoenixforge.student.sync.provider"
    const val READ_PERMISSION = "com.phoenixforge.student.sync.READ"

    const val PATH_PROFILE_SNAPSHOT = "profile_snapshot"
    const val PATH_PROGRESS = "progress"
    const val PATH_BEHAVIOR_SIGNALS = "behavior_signals"
    const val PATH_STORY_FRAGMENTS = "story_fragments"
    const val PATH_LIFE_EVENTS = "life_events"

    val PROFILE_SNAPSHOT_URI: Uri = Uri.parse("content://$AUTHORITY/$PATH_PROFILE_SNAPSHOT")
    val PROGRESS_URI: Uri = Uri.parse("content://$AUTHORITY/$PATH_PROGRESS")
    val BEHAVIOR_SIGNALS_URI: Uri = Uri.parse("content://$AUTHORITY/$PATH_BEHAVIOR_SIGNALS")
    val STORY_FRAGMENTS_URI: Uri = Uri.parse("content://$AUTHORITY/$PATH_STORY_FRAGMENTS")
    val LIFE_EVENTS_URI: Uri = Uri.parse("content://$AUTHORITY/$PATH_LIFE_EVENTS")

    object Columns {
        const val UID = "uid"
        const val FORGE_NAME = "forge_name"
        const val CURRENT_STAGE = "current_stage"
        const val CURRENT_TITLE = "current_title"
        const val AVATAR_SUMMARY = "avatar_summary"
        const val TIMELINE_SUMMARY = "timeline_summary"
        const val IMPORTED_AT_EPOCH_MILLIS = "imported_at_epoch_millis"

        const val XP = "xp"
        const val LEVEL = "level"
        const val STREAK_DAYS = "streak_days"
        const val LAST_VISIT_EPOCH_MILLIS = "last_visit_epoch_millis"
        const val UNLOCK_FLAGS_JSON = "unlock_flags_json"
        const val ACHIEVEMENT_IDS_JSON = "achievement_ids_json"

        const val PHOTOS_THIS_WEEK = "photos_this_week"
        const val QUESTS_COMPLETED_THIS_WEEK = "quests_completed_this_week"
        const val RETURNS_THIS_WEEK = "returns_this_week"
        const val LAST_PHOTO_EPOCH_MILLIS = "last_photo_epoch_millis"
        const val LAST_QUEST_EPOCH_MILLIS = "last_quest_epoch_millis"
        // Reuse LAST_VISIT_EPOCH_MILLIS from the progress section.
        const val WEEK_ANCHOR_EPOCH_MILLIS = "week_anchor_epoch_millis"

        const val ID = "id"
        const val NARRATIVE = "narrative"
        const val WORLD_EVENT_TYPE = "world_event_type"
        const val XP_AWARDED = "xp_awarded"
        const val ROOM_UNLOCKED = "room_unlocked"
        const val NPC_SPEAKER = "npc_speaker"
        const val ENVIRONMENT_CHANGE = "environment_change"
        const val EMOTIONAL_IMPACT = "emotional_impact"
        const val TIMESTAMP_EPOCH_MILLIS = "timestamp_epoch_millis"

        const val TYPE = "type"
        const val PAYLOAD = "payload"
    }
}

