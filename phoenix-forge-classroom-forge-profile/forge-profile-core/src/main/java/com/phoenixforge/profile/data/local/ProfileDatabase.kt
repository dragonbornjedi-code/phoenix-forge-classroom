package com.phoenixforge.profile.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.phoenixforge.profile.data.local.dao.ProfileDao
import com.phoenixforge.profile.data.local.entity.*

@Database(
    entities = [
        ProfileEntity::class,
        AvatarEntity::class,
        IdentitySnapshotEntity::class,
        MemoryArtifactEntity::class,
        TimelineEventEntity::class,
        TeacherMetadataEntity::class,
        AboutMeEntryEntity::class,
        FavoriteEntryEntity::class,
        DreamEntryEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ProfileDatabase : RoomDatabase() {
    abstract val dao: ProfileDao
}
