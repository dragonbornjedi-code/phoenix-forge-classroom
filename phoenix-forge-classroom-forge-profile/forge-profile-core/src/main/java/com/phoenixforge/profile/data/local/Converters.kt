package com.phoenixforge.profile.data.local

import androidx.room.TypeConverter
import com.phoenixforge.profile.domain.model.ArtifactSource
import com.phoenixforge.profile.domain.model.ArtifactType
import com.phoenixforge.profile.domain.model.EventType
import com.phoenixforge.profile.domain.model.MemoryCategory
import java.time.Instant

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Instant? {
        return value?.let { Instant.ofEpochMilli(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Instant?): Long? {
        return date?.toEpochMilli()
    }

    @TypeConverter
    fun fromArtifactType(value: ArtifactType): String {
        return value.name
    }

    @TypeConverter
    fun toArtifactType(value: String): ArtifactType {
        return ArtifactType.valueOf(value)
    }

    @TypeConverter
    fun fromEventType(value: EventType): String {
        return value.name
    }

    @TypeConverter
    fun toEventType(value: String): EventType {
        return EventType.valueOf(value)
    }

    @TypeConverter
    fun fromMemoryCategory(value: MemoryCategory): String = value.name

    @TypeConverter
    fun toMemoryCategory(value: String): MemoryCategory = MemoryCategory.valueOf(value)

    @TypeConverter
    fun fromArtifactSource(value: ArtifactSource): String = value.name

    @TypeConverter
    fun toArtifactSource(value: String): ArtifactSource = ArtifactSource.valueOf(value)
}
