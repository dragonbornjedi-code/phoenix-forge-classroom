package com.phoenixforge.student.sync

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Lamport logical clock persisted per device install.
 * Rule: logicalClock = max(localCounter, highestSeenInEventsDir) + 1
 */
@Singleton
class LogicalClockStore @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val prefs = context.getSharedPreferences(PREFS_LOGICAL_CLOCK, Context.MODE_PRIVATE)
    private val json = Json { ignoreUnknownKeys = true }

    fun nextClock(eventsDir: File?): Long {
        val local = prefs.getLong(KEY_LAST_CLOCK, 0L)
        val seen = eventsDir?.let { LogicalClockLogic.scanMaxLogicalClock(it, json) } ?: 0L
        val next = maxOf(local, seen) + 1
        prefs.edit().putLong(KEY_LAST_CLOCK, next).apply()
        return next
    }

    private companion object {
        const val PREFS_LOGICAL_CLOCK = "forge_event_logical_clock"
        const val KEY_LAST_CLOCK = "last_logical_clock"
    }
}

object LogicalClockLogic {
    fun scanMaxLogicalClock(eventsDir: File, json: Json): Long {
        if (!eventsDir.isDirectory) return 0L
        return eventsDir.listFiles()
            ?.filter { it.isFile && it.name.startsWith("EVT_") && it.name.endsWith(".json") }
            ?.maxOfOrNull { file ->
                runCatching {
                    json.decodeFromString(ForgeEventRecord.serializer(), file.readText()).logicalClock
                }.getOrDefault(0L)
            }
            ?: 0L
    }
}
