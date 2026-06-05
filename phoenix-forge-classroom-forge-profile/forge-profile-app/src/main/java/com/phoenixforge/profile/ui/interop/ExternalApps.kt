package com.phoenixforge.profile.ui.interop

import android.content.Context
import android.content.Intent
import android.net.Uri

object ExternalApps {
    const val STUDENT_EDITION_PACKAGE = "com.phoenixforge.student"
    const val TEACHER_EDITION_PACKAGE = "com.phoenixforge.classroom.teacher"

    fun launchTeacherEdition(context: Context): Boolean =
        launchPackage(context, TEACHER_EDITION_PACKAGE)

    fun launchStudentEdition(context: Context): Boolean =
        launchPackage(context, STUDENT_EDITION_PACKAGE)

    private fun launchPackage(context: Context, packageName: String): Boolean {
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
            ?: Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        return try {
            context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            true
        } catch (_: Exception) {
            false
        }
    }
}

