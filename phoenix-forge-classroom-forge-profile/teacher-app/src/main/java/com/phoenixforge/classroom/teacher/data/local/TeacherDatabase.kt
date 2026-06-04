package com.phoenixforge.classroom.teacher.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.phoenixforge.classroom.teacher.domain.model.IntentTile

@Database(
    entities = [IntentTile::class],
    version = 1,
    exportSchema = false
)
abstract class TeacherDatabase : RoomDatabase() {
    abstract fun intentTileDao(): IntentTileDao
}
