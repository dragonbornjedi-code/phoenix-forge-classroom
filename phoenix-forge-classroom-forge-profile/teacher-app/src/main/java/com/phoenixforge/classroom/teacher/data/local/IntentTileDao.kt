package com.phoenixforge.classroom.teacher.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.phoenixforge.classroom.teacher.domain.model.IntentTile
import kotlinx.coroutines.flow.Flow

@Dao
interface IntentTileDao {

    @Query("SELECT * FROM intent_tiles ORDER BY sortOrder ASC, createdAt ASC")
    fun observeAll(): Flow<List<IntentTile>>

    @Query("SELECT * FROM intent_tiles WHERE id = :id LIMIT 1")
    suspend fun findById(id: String): IntentTile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(tile: IntentTile)

    @Update
    suspend fun update(tile: IntentTile)

    @Update
    suspend fun updateAll(tiles: List<IntentTile>)

    @Query("DELETE FROM intent_tiles WHERE id = :id")
    suspend fun deleteById(id: String)
}
