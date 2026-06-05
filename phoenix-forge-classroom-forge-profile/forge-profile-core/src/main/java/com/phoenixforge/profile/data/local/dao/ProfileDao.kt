package com.phoenixforge.profile.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.phoenixforge.profile.data.local.entity.AboutMeEntryEntity
import com.phoenixforge.profile.data.local.entity.AvatarEntity
import com.phoenixforge.profile.data.local.entity.DreamEntryEntity
import com.phoenixforge.profile.data.local.entity.FavoriteEntryEntity
import com.phoenixforge.profile.data.local.entity.IdentitySnapshotEntity
import com.phoenixforge.profile.data.local.entity.LinkedStudentEntity
import com.phoenixforge.profile.data.local.entity.MemoryArtifactEntity
import com.phoenixforge.profile.data.local.entity.ProfileEntity
import com.phoenixforge.profile.data.local.entity.TeacherMetadataEntity
import com.phoenixforge.profile.data.local.entity.TimelineEventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profiles LIMIT 1")
    fun getProfile(): Flow<ProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ProfileEntity)

    @Query("SELECT * FROM avatars ORDER BY version DESC")
    fun getAvatarHistory(): Flow<List<AvatarEntity>>

    @Query("SELECT * FROM avatars ORDER BY version DESC LIMIT 1")
    fun getLatestAvatar(): Flow<AvatarEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAvatar(avatar: AvatarEntity)

    @Query("SELECT * FROM timeline_events ORDER BY timestamp DESC")
    fun getTimelineEvents(): Flow<List<TimelineEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimelineEvent(event: TimelineEventEntity)

    @Query("SELECT * FROM memory_artifacts ORDER BY capturedAt DESC")
    fun getMemoryArtifacts(): Flow<List<MemoryArtifactEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemoryArtifact(artifact: MemoryArtifactEntity)

    @Query("SELECT * FROM identity_snapshots WHERE profileUid = :uid ORDER BY timestamp DESC")
    fun getIdentityHistory(uid: String): Flow<List<IdentitySnapshotEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIdentitySnapshot(snapshot: IdentitySnapshotEntity)

    @Query("SELECT * FROM teacher_metadata WHERE profileUid = :uid")
    fun getTeacherMetadata(uid: String): Flow<List<TeacherMetadataEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeacherMetadata(metadata: TeacherMetadataEntity)

    @Query("SELECT * FROM about_me ORDER BY timestamp DESC")
    fun getAboutMeEntries(): Flow<List<AboutMeEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAboutMeEntry(entry: AboutMeEntryEntity)

    @Query("SELECT * FROM favorites ORDER BY timestamp DESC")
    fun getFavoriteEntries(): Flow<List<FavoriteEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteEntry(entry: FavoriteEntryEntity)

    @Query("SELECT * FROM dreams ORDER BY timestamp DESC")
    fun getDreamEntries(): Flow<List<DreamEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDreamEntry(entry: DreamEntryEntity)

    @Query("DELETE FROM profiles")
    suspend fun deleteAllProfiles()

    @Query("DELETE FROM dreams")
    suspend fun deleteAllDreams()

    @Query("DELETE FROM about_me")
    suspend fun deleteAllAboutMe()

    @Query("DELETE FROM favorites")
    suspend fun deleteAllFavorites()

    @Query("DELETE FROM avatars")
    suspend fun deleteAllAvatars()

    @Query("DELETE FROM timeline_events")
    suspend fun deleteAllTimelineEvents()

    @Query("DELETE FROM memory_artifacts")
    suspend fun deleteAllMemoryArtifacts()

    @Query("DELETE FROM identity_snapshots")
    suspend fun deleteAllIdentitySnapshots()

    @Query("DELETE FROM teacher_metadata")
    suspend fun deleteAllTeacherMetadata()

    @Query("SELECT * FROM linked_students ORDER BY linkedAt DESC")
    fun getLinkedStudents(): Flow<List<LinkedStudentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLinkedStudent(student: LinkedStudentEntity)

    @Query("DELETE FROM linked_students WHERE profileUid = :profileUid")
    suspend fun deleteLinkedStudent(profileUid: String)

    @Query("DELETE FROM linked_students")
    suspend fun deleteAllLinkedStudents()
}
