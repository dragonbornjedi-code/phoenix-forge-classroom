package com.phoenixforge.profile.data.provider

import android.net.Uri

object ProfileContract {
    const val AUTHORITY = "com.phoenixforge.profile.provider"
    const val READ_PERMISSION = "com.phoenixforge.profile.READ"
    val BASE_CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY")

    const val PATH_PROFILE = "profile"
    const val PATH_AVATAR = "avatar"
    const val PATH_TIMELINE = "timeline"

    const val MIME_PROFILE = "vnd.android.cursor.item/vnd.$AUTHORITY.$PATH_PROFILE"
    const val MIME_AVATAR = "vnd.android.cursor.item/vnd.$AUTHORITY.$PATH_AVATAR"
    const val MIME_TIMELINE = "vnd.android.cursor.dir/vnd.$AUTHORITY.$PATH_TIMELINE"

    val PROFILE_URI: Uri = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PROFILE).build()
    val AVATAR_URI: Uri = BASE_CONTENT_URI.buildUpon().appendPath(PATH_AVATAR).build()
    val TIMELINE_URI: Uri = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TIMELINE).build()

    object Columns {
        const val UID = "uid"
        const val FORGE_NAME = "forge_name"
        const val CURRENT_STAGE = "current_stage"
        const val CURRENT_TITLE = "current_title"
        const val AVATAR_HAIR = "hair_type"
        const val AVATAR_EYES = "eye_color"
        const val AVATAR_SKIN = "skin_tone"
        const val AVATAR_CLOTHING = "clothing_id"
        const val AVATAR_VERSION = "avatar_version"
        const val EVENT_TITLE = "title"
        const val EVENT_TYPE = "type"
        const val EVENT_TIMESTAMP = "timestamp"
    }

    object ProfileProjection {
        val COLUMNS = arrayOf(
            Columns.UID,
            Columns.FORGE_NAME,
            Columns.CURRENT_STAGE,
            Columns.CURRENT_TITLE
        )
    }

    object AvatarProjection {
        val COLUMNS = arrayOf(
            Columns.AVATAR_HAIR,
            Columns.AVATAR_EYES,
            Columns.AVATAR_SKIN,
            Columns.AVATAR_CLOTHING,
            Columns.AVATAR_VERSION
        )
    }

    object TimelineProjection {
        val COLUMNS = arrayOf(
            Columns.EVENT_TITLE,
            Columns.EVENT_TYPE,
            Columns.EVENT_TIMESTAMP
        )
    }
}
