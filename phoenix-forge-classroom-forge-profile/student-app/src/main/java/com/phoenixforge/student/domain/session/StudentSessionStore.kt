package com.phoenixforge.student.domain.session

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudentSessionStore @Inject constructor(
    private val prefs: SharedPreferences
) {
    fun isSignedIn(): Boolean = prefs.getBoolean(KEY_SIGNED_IN, false)

    fun setSignedIn(signedIn: Boolean) {
        prefs.edit().putBoolean(KEY_SIGNED_IN, signedIn).apply()
    }

    fun signOut() {
        prefs.edit()
            .putBoolean(KEY_SIGNED_IN, false)
            .remove(KEY_ACTIVE_IMPORT_UID)
            .apply()
    }

    fun getActiveImportUid(): String? = prefs.getString(KEY_ACTIVE_IMPORT_UID, null)

    fun setActiveImportUid(uid: String) {
        prefs.edit()
            .putString(KEY_ACTIVE_IMPORT_UID, uid)
            .putBoolean(KEY_SIGNED_IN, true)
            .apply()
    }

    private companion object {
        const val KEY_SIGNED_IN = "student_signed_in"
        const val KEY_ACTIVE_IMPORT_UID = "student_active_import_uid"
    }
}
