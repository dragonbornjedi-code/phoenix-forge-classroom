package com.phoenixforge.profile.data.network

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ForgeProfileLanIdentity @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun deviceId(): String {
        val existing = prefs.getString(KEY_DEVICE_ID, null)?.trim().orEmpty()
        if (existing.isNotEmpty()) return existing
        val created = "forge-profile-${UUID.randomUUID()}"
        prefs.edit().putString(KEY_DEVICE_ID, created).apply()
        return created
    }

    private companion object {
        const val PREFS_NAME = "forge_profile_lan"
        const val KEY_DEVICE_ID = "device_id"
    }
}
