package com.phoenixforge.student.sync

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudentDeviceIdStore @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val prefs = context.getSharedPreferences(PREFS_DEVICE, Context.MODE_PRIVATE)

    fun getOrCreate(): String {
        prefs.getString(KEY_DEVICE_ID, null)?.takeIf { it.isNotBlank() }?.let { return it }
        val created = "student-${UUID.randomUUID()}"
        prefs.edit().putString(KEY_DEVICE_ID, created).apply()
        return created
    }

    private companion object {
        const val PREFS_DEVICE = "student_device"
        const val KEY_DEVICE_ID = "device_id"
    }
}
