package com.phoenixforge.classroom.teacher.data.bridge

import android.content.Context
import com.phoenixforge.classroom.teacher.data.students.StudentSyncReader
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

enum class ChildUidSource(val label: String) {
    FORGE_PROFILE_CHILD("Child on this phone only"),
    FORGE_PROFILE_LINKED("Linked in Forge Profile"),
    STUDENT_EDITION("Student import on this phone only"),
    REMEMBERED_PUSH("Stale push cache (ignored)"),
}

data class ResolvedChildUid(
    val studentUid: String,
    val displayName: String,
    val source: ChildUidSource,
)

@Singleton
class ChildStudentUidResolver @Inject constructor(
    @ApplicationContext private val context: Context,
    private val forgeProfileChildReader: ForgeProfileChildReader,
    private val studentSyncReader: StudentSyncReader,
) {
    suspend fun resolve(): ResolvedChildUid? {
        val linked = forgeProfileChildReader.listLinkedStudents()
            .filter { it.profileUid.isNotBlank() }
        if (linked.isNotEmpty()) {
            val row = linked.first()
            return ResolvedChildUid(
                studentUid = row.profileUid,
                displayName = row.displayName,
                source = ChildUidSource.FORGE_PROFILE_LINKED,
            )
        }

        val childProfiles = forgeProfileChildReader.listChildProfiles()
            .filter { it.uid.isNotBlank() }
        if (childProfiles.isNotEmpty()) {
            val child = childProfiles.first()
            return ResolvedChildUid(
                studentUid = child.uid,
                displayName = child.forgeName,
                source = ChildUidSource.FORGE_PROFILE_CHILD,
            )
        }

        val studentSnapshot = studentSyncReader.readProfileSnapshot()
        studentSnapshot
            ?.uid
            ?.trim()
            ?.takeIf { it.isNotEmpty() }
            ?.let { uid ->
                return ResolvedChildUid(
                    studentUid = uid,
                    displayName = studentSnapshot.forgeName.ifBlank { "Student import" },
                    source = ChildUidSource.STUDENT_EDITION,
                )
            }

        return null
    }

    /** Old manifest-push UID kept for diagnostics only — never used as an active student link. */
    fun peekRememberedStudentUid(): String? =
        context.getSharedPreferences(PREFS_MANIFEST_PUSH, Context.MODE_PRIVATE)
            .getString(KEY_STUDENT_UID, null)
            ?.trim()
            ?.takeIf { it.isNotEmpty() }

    fun clearRememberedStudentUid() {
        context.getSharedPreferences(PREFS_MANIFEST_PUSH, Context.MODE_PRIVATE)
            .edit()
            .remove(KEY_STUDENT_UID)
            .apply()
    }

    fun rememberStudentUid(uid: String) {
        context.getSharedPreferences(PREFS_MANIFEST_PUSH, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_STUDENT_UID, uid.trim())
            .apply()
    }

    companion object {
        const val PREFS_MANIFEST_PUSH = "manifest_push"
        const val KEY_STUDENT_UID = "manifest_push_student_uid"
    }
}
