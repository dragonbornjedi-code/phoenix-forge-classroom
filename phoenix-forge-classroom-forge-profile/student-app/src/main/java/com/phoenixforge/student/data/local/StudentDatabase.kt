package com.phoenixforge.student.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.phoenixforge.student.data.local.dao.StudentDao
import com.phoenixforge.student.data.local.entity.BehaviorSignalsEntity
import com.phoenixforge.student.data.local.entity.DreamEntryEntity
import com.phoenixforge.student.data.local.entity.HouseStateEntity
import com.phoenixforge.student.data.local.entity.ImportedProfileSnapshotEntity
import com.phoenixforge.student.data.local.entity.LifeEventEntity
import com.phoenixforge.student.data.local.entity.MemoryArtifactEntity
import com.phoenixforge.student.data.local.entity.MemoryEventDraftEntity
import com.phoenixforge.student.data.local.entity.NpcEntity
import com.phoenixforge.student.data.local.entity.QuestEntity
import com.phoenixforge.student.data.local.entity.StoryFragmentEntity
import com.phoenixforge.student.data.local.entity.StudentProgressEntity
import com.phoenixforge.student.data.local.entity.WorldStateMetaEntity

@Database(
    entities = [
        StudentProgressEntity::class,
        HouseStateEntity::class,
        MemoryArtifactEntity::class,
        MemoryEventDraftEntity::class,
        NpcEntity::class,
        QuestEntity::class,
        ImportedProfileSnapshotEntity::class,
        LifeEventEntity::class,
        StoryFragmentEntity::class,
        WorldStateMetaEntity::class,
        BehaviorSignalsEntity::class,
        DreamEntryEntity::class
    ],
    version = 5,
    exportSchema = false
)
abstract class StudentDatabase : RoomDatabase() {
    abstract fun studentDao(): StudentDao
}
