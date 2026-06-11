package com.phoenixforge.profile.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.phoenixforge.profile.data.local.entity.EventRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventRecordDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEvent(event: EventRecordEntity): Long

    @Query("SELECT * FROM forge_events WHERE studentUid = :studentUid ORDER BY logicalClock ASC, eventId ASC")
    fun observeEventsForStudent(studentUid: String): Flow<List<EventRecordEntity>>

    @Query("SELECT COUNT(*) FROM forge_events WHERE studentUid = :studentUid")
    suspend fun countEventsForStudent(studentUid: String): Int

    @Query("SELECT eventId FROM forge_events WHERE studentUid = :studentUid")
    suspend fun listEventIdsForStudent(studentUid: String): List<String>

    @Query("SELECT * FROM forge_events WHERE studentUid = :studentUid ORDER BY logicalClock ASC, eventId ASC")
    suspend fun listEventsForStudent(studentUid: String): List<EventRecordEntity>
}
