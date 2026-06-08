package com.phoenixforge.profile.data.provider

import android.net.Uri

object ProfileContract {
    const val AUTHORITY = "com.phoenixforge.profile.provider"
    const val READ_PERMISSION = "com.phoenixforge.profile.READ"
    val BASE_CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY")

    const val PATH_PROFILE = "profile"
    const val PATH_AVATAR = "avatar"
    const val PATH_TIMELINE = "timeline"
    const val PATH_MEMORIES = "memories"
    const val PATH_MEMORY_FILE = "memories/file"

    const val MIME_PROFILE = "vnd.android.cursor.item/vnd.$AUTHORITY.$PATH_PROFILE"
    const val MIME_AVATAR = "vnd.android.cursor.item/vnd.$AUTHORITY.$PATH_AVATAR"
    const val MIME_TIMELINE = "vnd.android.cursor.dir/vnd.$AUTHORITY.$PATH_TIMELINE"
    const val MIME_MEMORIES = "vnd.android.cursor.dir/vnd.$AUTHORITY.$PATH_MEMORIES"
    const val MIME_MEMORY_FILE = "vnd.android.cursor.item/vnd.$AUTHORITY.$PATH_MEMORY_FILE"

    val PROFILE_URI: Uri = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PROFILE).build()
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
}
