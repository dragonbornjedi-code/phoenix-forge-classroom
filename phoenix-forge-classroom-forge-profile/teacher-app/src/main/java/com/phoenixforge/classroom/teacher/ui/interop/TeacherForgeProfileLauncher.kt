package com.phoenixforge.classroom.teacher.ui.interop

import android.content.Context
import android.content.Intent

object TeacherForgeProfileLauncher {
    private const val FORGE_PROFILE_PACKAGE = "com.phoenixforge.profile"
    private const val FORGE_PROFILE_MAIN = "com.phoenixforge.profile.MainActivity"

    fun openForgeProfile(context: Context): Boolean =
        runCatching {
            val intent = Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
                setClassName(FORGE_PROFILE_PACKAGE, FORGE_PROFILE_MAIN)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            true
        }.getOrDefault(false)
}
