package com.phoenixforge.profile.ui.interop

import android.content.ComponentName
import android.content.Context
import android.content.Intent

object ExternalApps {
    const val STUDENT_EDITION_PACKAGE = "com.phoenixforge.student"
    const val TEACHER_EDITION_PACKAGE = "com.phoenixforge.classroom.teacher"

    private const val TEACHER_MAIN_ACTIVITY = "com.phoenixforge.classroom.teacher.MainActivity"
    private const val STUDENT_MAIN_ACTIVITY = "com.phoenixforge.student.MainActivity"

    sealed class LaunchResult {
        data object Launched : LaunchResult()
        data class NotInstalled(val packageName: String) : LaunchResult()
        data class Failed(val message: String) : LaunchResult()
    }

    fun isTeacherEditionInstalled(context: Context): Boolean =
        isPackageInstalled(context, TEACHER_EDITION_PACKAGE)

    fun isStudentEditionInstalled(context: Context): Boolean =
        isPackageInstalled(context, STUDENT_EDITION_PACKAGE)

    fun launchTeacherEdition(context: Context): LaunchResult =
        launchMainActivity(context, TEACHER_EDITION_PACKAGE, TEACHER_MAIN_ACTIVITY)

    fun launchStudentEdition(context: Context): LaunchResult =
        launchMainActivity(context, STUDENT_EDITION_PACKAGE, STUDENT_MAIN_ACTIVITY)

    private fun isPackageInstalled(context: Context, packageName: String): Boolean =
        runCatching {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        }.getOrDefault(false)

    private fun launchMainActivity(
        context: Context,
        packageName: String,
        activityClass: String,
    ): LaunchResult {
        if (!isPackageInstalled(context, packageName)) {
            return LaunchResult.NotInstalled(packageName)
        }

        val explicit = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
            component = ComponentName(packageName, activityClass)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        if (explicit.resolveActivity(context.packageManager) != null) {
            return runCatching {
                context.startActivity(explicit)
                LaunchResult.Launched
            }.getOrElse { error ->
                LaunchResult.Failed(error.message ?: "Could not open $packageName")
            }
        }

        val fallback = context.packageManager.getLaunchIntentForPackage(packageName)
            ?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        return if (fallback != null) {
            runCatching {
                context.startActivity(fallback)
                LaunchResult.Launched
            }.getOrElse { error ->
                LaunchResult.Failed(error.message ?: "Could not open $packageName")
            }
        } else {
            LaunchResult.Failed("Teacher Edition is installed but has no launcher activity.")
        }
    }
}
