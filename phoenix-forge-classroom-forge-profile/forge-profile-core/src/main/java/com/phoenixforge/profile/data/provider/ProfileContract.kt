package com.phoenixforge.profile.data.provider

import android.net.Uri

object ProfileContract {
    const val AUTHORITY = "com.phoenixforge.profile.provider"
    const val READ_PERMISSION = "com.phoenixforge.profile.READ"
    val BASE_CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY")

    const val PATH_PROFILE = "profile"
    const val PATH_ACTIVE_PROFILE = "active_profile"
    const val PATH_AVATAR = "avatar"
    const val PATH_TIMELINE = "timeline"
    const val PATH_MEMORIES = "memories"
    const val PATH_MEMORY_FILE = "memories/file"
    const val PATH_CHILD_PROFILES = "child_profiles"
    const val PATH_LINKED_STUDENTS = "linked_students"
    const val PATH_CHILD_PROFILE = "child_profile"
    const val PATH_MESSAGES = "messages"

    const val MIME_PROFILE = "vnd.android.cursor.item/vnd.$AUTHORITY.$PATH_PROFILE"
    const val MIME_AVATAR = "vnd.android.cursor.item/vnd.$AUTHORITY.$PATH_AVATAR"
    const val MIME_TIMELINE = "vnd.android.cursor.dir/vnd.$AUTHORITY.$PATH_TIMELINE"
    const val MIME_MEMORIES = "vnd.android.cursor.dir/vnd.$AUTHORITY.$PATH_MEMORIES"
    const val MIME_MEMORY_FILE = "vnd.android.cursor.item/vnd.$AUTHORITY.$PATH_MEMORY_FILE"
    const val MIME_CHILD_PROFILES = "vnd.android.cursor.dir/vnd.$AUTHORITY.$PATH_CHILD_PROFILES"
    const val MIME_LINKED_STUDENTS = "vnd.android.cursor.dir/vnd.$AUTHORITY.$PATH_LINKED_STUDENTS"
    const val MIME_CHILD_EVENTS = "vnd.android.cursor.dir/vnd.$AUTHORITY.child_events"
    const val MIME_MESSAGES = "vnd.android.cursor.dir/vnd.$AUTHORITY.$PATH_MESSAGES"

    val PROFILE_URI: Uri = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PROFILE).build()
    val MESSAGES_URI: Uri = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MESSAGES).build()
    val ACTIVE_PROFILE_URI: Uri = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ACTIVE_PROFILE).build()
    val CHILD_PROFILES_URI: Uri = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CHILD_PROFILES).build()
    val LINKED_STUDENTS_URI: Uri = BASE_CONTENT_URI.buildUpon().appendPath(PATH_LINKED_STUDENTS).build()
    val AVATAR_URI: Uri = BASE_CONTENT_URI.buildUpon().appendPath(PATH_AVATAR).build()
    val TIMELINE_URI: Uri = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TIMELINE).build()
    val MEMORIES_URI: Uri = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEMORIES).build()

    fun memoryFileUri(artifactId: String): Uri =
        BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_MEMORY_FILE)
            .appendPath(artifactId)
            .build()

    object Columns {
        const val UID = "uid"
        const val FORGE_NAME = "forge_name"
        const val CURRENT_STAGE = "current_stage"
        const val CURRENT_TITLE = "current_title"
        const val AGE_YEARS = "age_years"
        const val PROFILE_ROLE = "profile_role"
        const val AVATAR_HAIR = "hair_type"
        const val AVATAR_EYES = "eye_color"
        const val AVATAR_SKIN = "skin_tone"
        const val AVATAR_CLOTHING = "clothing_id"
        const val AVATAR_VERSION = "avatar_version"
        const val AVATAR_SHARD_LEVEL = "avatar_shard_level"
        const val HERO_STYLE = "hero_style"
        const val HERO_COLOR = "hero_color"
        const val GODOT_MODEL_PATH = "godot_model_path"
        const val AVATAR_SUMMARY = "avatar_summary"
        const val AVATAR_CONFIG_JSON = "avatar_config_json"
        const val EVENT_TITLE = "title"
        const val EVENT_TYPE = "type"
        const val EVENT_TIMESTAMP = "timestamp"
        const val MEMORY_ID = "memory_id"
        const val MEMORY_TYPE = "memory_type"
        const val MEMORY_CATEGORY = "memory_category"
        const val MEMORY_SOURCE = "memory_source"
        const val MEMORY_CAPTURED_AT = "captured_at"
        const val MEMORY_NOTE = "note"
        const val MEMORY_CHECKSUM = "checksum"
        const val MEMORY_SYNCED_TO_STUDENT = "synced_to_student"
        const val MEMORY_CONTENT_URI = "content_uri"
        const val LINKED_DISPLAY_NAME = "display_name"
        const val LINKED_AT_EPOCH_MILLIS = "linked_at_epoch_millis"
        const val LINKED_NOTES = "notes"
        const val FORGE_EVENT_ID = "event_id"
        const val FORGE_EVENT_TYPE = "event_type"
        const val FORGE_EVENT_SCOPE = "event_scope"
        const val FORGE_ACTOR_APP = "actor_app"
        const val FORGE_LOGICAL_CLOCK = "logical_clock"
        const val FORGE_EPOCH_MS = "epoch_ms"
        const val MESSAGE_ID = "message_id"
        const val MESSAGE_THREAD_ID = "thread_id"
        const val MESSAGE_DIRECTION = "direction"
        const val MESSAGE_FROM_DEVICE = "from_device_id"
        const val MESSAGE_FROM_NAME = "from_display_name"
        const val MESSAGE_TO_UID = "to_student_uid"
        const val MESSAGE_TARGET_APP = "target_app"
        const val MESSAGE_EPOCH_MS = "epoch_ms"
        const val MESSAGE_SUBJECT = "subject"
        const val MESSAGE_BODY = "body_markdown"
        const val MESSAGE_READ_MS = "read_epoch_ms"
        const val MESSAGE_REPLY_TO = "reply_to_message_id"
    }

    fun childProfileUri(studentUid: String): Uri =
        BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_CHILD_PROFILE)
            .appendPath(studentUid)
            .appendPath("profile")
            .build()

    fun childTimelineUri(studentUid: String): Uri =
        BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_CHILD_PROFILE)
            .appendPath(studentUid)
            .appendPath("timeline")
            .build()

    fun childEventsUri(studentUid: String): Uri =
        BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_CHILD_PROFILE)
            .appendPath(studentUid)
            .appendPath("events")
            .build()

    fun childMessagesUri(studentUid: String, targetApp: String? = null): Uri {
        val builder = BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_CHILD_PROFILE)
            .appendPath(studentUid)
            .appendPath(PATH_MESSAGES)
        if (!targetApp.isNullOrBlank()) {
            builder.appendQueryParameter("targetApp", targetApp)
        }
        return builder.build()
    }

    object ProfileProjection {
        val COLUMNS = arrayOf(
            Columns.UID,
            Columns.FORGE_NAME,
            Columns.CURRENT_STAGE,
            Columns.CURRENT_TITLE,
            Columns.AGE_YEARS,
            Columns.PROFILE_ROLE
        )
    }

    object AvatarProjection {
        val COLUMNS = arrayOf(
            Columns.AVATAR_HAIR,
            Columns.AVATAR_EYES,
            Columns.AVATAR_SKIN,
            Columns.AVATAR_CLOTHING,
            Columns.AVATAR_VERSION,
            Columns.AVATAR_SHARD_LEVEL,
            Columns.HERO_STYLE,
            Columns.HERO_COLOR,
            Columns.GODOT_MODEL_PATH,
            Columns.AVATAR_SUMMARY,
            Columns.AVATAR_CONFIG_JSON,
        )
    }

    object TimelineProjection {
        val COLUMNS = arrayOf(
            Columns.EVENT_TITLE,
            Columns.EVENT_TYPE,
            Columns.EVENT_TIMESTAMP
        )
    }

    object MemoriesProjection {
        val COLUMNS = arrayOf(
            Columns.MEMORY_ID,
            Columns.MEMORY_TYPE,
            Columns.MEMORY_CATEGORY,
            Columns.MEMORY_SOURCE,
            Columns.MEMORY_CAPTURED_AT,
            Columns.MEMORY_NOTE,
            Columns.MEMORY_CHECKSUM,
            Columns.MEMORY_SYNCED_TO_STUDENT,
            Columns.MEMORY_CONTENT_URI,
        )
    }

    object LinkedStudentProjection {
        val COLUMNS = arrayOf(
            Columns.UID,
            Columns.LINKED_DISPLAY_NAME,
            Columns.LINKED_AT_EPOCH_MILLIS,
            Columns.LINKED_NOTES,
        )
    }

    object ForgeEventProjection {
        val COLUMNS = arrayOf(
            Columns.FORGE_EVENT_ID,
            Columns.FORGE_EVENT_TYPE,
            Columns.FORGE_EVENT_SCOPE,
            Columns.FORGE_ACTOR_APP,
            Columns.FORGE_LOGICAL_CLOCK,
            Columns.FORGE_EPOCH_MS,
        )
    }

    object MessageProjection {
        val COLUMNS = arrayOf(
            Columns.MESSAGE_ID,
            Columns.MESSAGE_THREAD_ID,
            Columns.MESSAGE_DIRECTION,
            Columns.MESSAGE_FROM_DEVICE,
            Columns.MESSAGE_FROM_NAME,
            Columns.MESSAGE_TO_UID,
            Columns.MESSAGE_TARGET_APP,
            Columns.MESSAGE_EPOCH_MS,
            Columns.MESSAGE_SUBJECT,
            Columns.MESSAGE_BODY,
            Columns.MESSAGE_READ_MS,
            Columns.MESSAGE_REPLY_TO,
        )
    }
}
