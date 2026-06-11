package com.phoenixforge.profile.domain.session

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Persists whether this device should skip the sign-in gate on cold start.
 * Does not store profile data — Room remains the source of truth.
 */
@Singleton
class ProfileSessionStore @Inject constructor(
    private val prefs: SharedPreferences
) {
    fun isRememberDeviceEnabled(): Boolean =
        prefs.getBoolean(KEY_REMEMBER_DEVICE, false)

    fun setRememberDevice(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_REMEMBER_DEVICE, enabled).apply()
    }

    fun clearRememberDevice() {
        prefs.edit().remove(KEY_REMEMBER_DEVICE).apply()
    }

    fun getActiveProfileUid(): String? =
        prefs.getString(KEY_ACTIVE_PROFILE_UID, null)

    fun setActiveProfileUid(uid: String) {
        prefs.edit().putString(KEY_ACTIVE_PROFILE_UID, uid).apply()
    }

    fun clearActiveProfileUid() {
        prefs.edit().remove(KEY_ACTIVE_PROFILE_UID).apply()
    }

    private companion object {
        const val KEY_REMEMBER_DEVICE = "profile_remember_device"
        const val KEY_ACTIVE_PROFILE_UID = "profile_active_uid"
    }
}
