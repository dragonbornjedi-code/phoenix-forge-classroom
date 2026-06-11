package com.phoenixforge.classroom.teacher.data.notes

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/** Private teacher-only notes per child profile — never synced to Student Edition. */
@Singleton
class TeacherNotesStore @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val prefs by lazy {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    }

    fun getNotes(studentUid: String): String =
        prefs.getString(key(studentUid), "").orEmpty()

    fun setNotes(studentUid: String, notes: String) {
        prefs.edit().putString(key(studentUid), notes.trim()).apply()
    }

    private fun key(studentUid: String): String = "notes_${studentUid.trim()}"

    private companion object {
        const val PREFS = "teacher_private_notes"
    }
}
