package com.phoenixforge.classroom.teacher.data.forgeprofile

import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

object TeacherForgeProfileContract {
    const val AUTHORITY = "com.phoenixforge.profile.provider"
    const val READ_PERMISSION = "com.phoenixforge.profile.READ"
    val PROFILE_URI: Uri = Uri.parse("content://$AUTHORITY/profile")

    object Columns {
        const val UID = "uid"
        const val FORGE_NAME = "forge_name"
        const val CURRENT_STAGE = "current_stage"
        const val CURRENT_TITLE = "current_title"
        const val AGE_YEARS = "age_years"
        const val PROFILE_ROLE = "profile_role"
    }
}

data class TeacherForgeProfileSnapshot(
    val uid: String,
    val forgeName: String,
    val currentStage: String,
    val currentTitle: String?,
    val ageYears: Int?,
    val profileRole: String?,
    val isLinked: Boolean,
    val errorMessage: String?
)

@Singleton
class ForgeProfileTeacherReader @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun readLinkedTeacherProfile(): TeacherForgeProfileSnapshot {
        if (context.checkSelfPermission(TeacherForgeProfileContract.READ_PERMISSION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            return TeacherForgeProfileSnapshot(
                uid = "",
                forgeName = "",
                currentStage = "",
                currentTitle = null,
                ageYears = null,
                profileRole = null,
                isLinked = false,
                errorMessage = "Forge Profile read permission not granted."
            )
        }

        return try {
            context.contentResolver.query(
                TeacherForgeProfileContract.PROFILE_URI,
                arrayOf(
                    TeacherForgeProfileContract.Columns.UID,
                    TeacherForgeProfileContract.Columns.FORGE_NAME,
                    TeacherForgeProfileContract.Columns.CURRENT_STAGE,
                    TeacherForgeProfileContract.Columns.CURRENT_TITLE,
                    TeacherForgeProfileContract.Columns.AGE_YEARS,
                    TeacherForgeProfileContract.Columns.PROFILE_ROLE
                ),
                null,
                null,
                null
            )?.use { cursor ->
                if (!cursor.moveToFirst()) {
                    return TeacherForgeProfileSnapshot(
                        uid = "",
                        forgeName = "",
                        currentStage = "",
                        currentTitle = null,
                        ageYears = null,
                        profileRole = null,
                        isLinked = false,
                        errorMessage = "No Forge Profile on this device. Open Forge Profile and sign in as a teacher."
                    )
                }
                val role = cursor.getStringOrNull(TeacherForgeProfileContract.Columns.PROFILE_ROLE)
                val snapshot = TeacherForgeProfileSnapshot(
                    uid = cursor.getStringOrNull(TeacherForgeProfileContract.Columns.UID).orEmpty(),
                    forgeName = cursor.getStringOrNull(TeacherForgeProfileContract.Columns.FORGE_NAME).orEmpty(),
                    currentStage = cursor.getStringOrNull(TeacherForgeProfileContract.Columns.CURRENT_STAGE).orEmpty(),
                    currentTitle = cursor.getStringOrNull(TeacherForgeProfileContract.Columns.CURRENT_TITLE),
                    ageYears = cursor.getIntOrNull(TeacherForgeProfileContract.Columns.AGE_YEARS),
                    profileRole = role,
                    isLinked = role == "teacher_self",
                    errorMessage = if (role == "teacher_self") null else "Forge Profile on this device is not a teacher profile."
                )
                snapshot
            } ?: TeacherForgeProfileSnapshot(
                uid = "",
                forgeName = "",
                currentStage = "",
                currentTitle = null,
                ageYears = null,
                profileRole = null,
                isLinked = false,
                errorMessage = "Could not read Forge Profile provider."
            )
        } catch (e: Exception) {
            TeacherForgeProfileSnapshot(
                uid = "",
                forgeName = "",
                currentStage = "",
                currentTitle = null,
                ageYears = null,
                profileRole = null,
                isLinked = false,
                errorMessage = e.message ?: "Forge Profile read failed."
            )
        }
    }

    private fun Cursor.getStringOrNull(column: String): String? {
        val index = getColumnIndex(column)
        return if (index < 0 || isNull(index)) null else getString(index)
    }

    private fun Cursor.getIntOrNull(column: String): Int? {
        val index = getColumnIndex(column)
        return if (index < 0 || isNull(index)) null else getInt(index)
    }
}
