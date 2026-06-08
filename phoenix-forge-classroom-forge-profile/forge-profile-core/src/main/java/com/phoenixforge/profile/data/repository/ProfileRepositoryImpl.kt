package com.phoenixforge.profile.data.repository

import com.phoenixforge.profile.data.local.AboutMeSnapshot
import com.phoenixforge.profile.data.local.AvatarSnapshot
import com.phoenixforge.profile.data.local.ChildhoodSnapshotPayload
import com.phoenixforge.profile.data.local.DreamSnapshot
import com.phoenixforge.profile.data.local.FavoriteSnapshot
import com.phoenixforge.profile.data.local.dao.ProfileDao
import com.phoenixforge.profile.data.local.entity.AboutMeEntryEntity
import com.phoenixforge.profile.data.local.entity.AvatarEntity
import com.phoenixforge.profile.data.local.entity.DreamEntryEntity
import com.phoenixforge.profile.data.local.entity.FavoriteEntryEntity
import com.phoenixforge.profile.data.local.entity.IdentitySnapshotEntity
import com.phoenixforge.profile.data.local.entity.MemoryArtifactEntity
import com.phoenixforge.profile.data.local.entity.ProfileEntity
import com.phoenixforge.profile.data.local.entity.TeacherMetadataEntity
import com.phoenixforge.profile.data.local.entity.TimelineEventEntity
import com.phoenixforge.profile.data.serialization.ForgeProfileJson
import com.phoenixforge.profile.domain.model.AboutMeEntry
import com.phoenixforge.profile.domain.model.Avatar
import com.phoenixforge.profile.domain.model.DreamEntry
import com.phoenixforge.profile.domain.model.EventType
import com.phoenixforge.profile.domain.model.FavoriteEntry
import com.phoenixforge.profile.domain.model.ForgeProfile
import com.phoenixforge.profile.domain.model.LinkedStudentProfile
import com.phoenixforge.profile.data.local.entity.LinkedStudentEntity
import com.phoenixforge.profile.domain.model.MemoryArtifact
import com.phoenixforge.profile.domain.model.TeacherMetadata
import com.phoenixforge.profile.domain.model.TimelineEvent
import com.phoenixforge.profile.domain.model.TimelineMetadata
import com.phoenixforge.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val dao: ProfileDao,
    private val forgeProfileJson: ForgeProfileJson
) : ProfileRepository {

    override fun getProfile(): Flow<ForgeProfile?> =
        dao.getProfile().map { it?.toDomain() }

    override suspend fun updateProfile(profile: ForgeProfile) {
        val oldProfile = dao.getProfile().firstOrNull()
        val previousPayload = if (oldProfile != null) captureCurrentPayload() else null

        if (oldProfile != null) {
            diffField(profile.uid, "forgeName", oldProfile.forgeName, profile.forgeName)
            diffField(profile.uid, "favoriteColor", oldProfile.favoriteColor, profile.favoriteColor)
            diffField(profile.uid, "currentTitle", oldProfile.currentTitle, profile.currentTitle)
            diffField(profile.uid, "currentStage", oldProfile.currentStage, profile.currentStage)
            diffField(profile.uid, "pronouns", oldProfile.pronouns, profile.pronouns)
        }

        dao.insertProfile(profile.toEntity())
        persistFullChildhoodSnapshot(
            profileUid = profile.uid,
            previous = previousPayload,
            current = captureCurrentPayload(),
            eventTitle = if (oldProfile == null) "Profile Created" else "Profile Updated"
        )
    }

    override suspend fun saveAboutMeEntry(entry: AboutMeEntry) {
        requireInitializedProfile(dao.getProfile().firstOrNull()?.toDomain())

        val previous = dao.getAboutMeEntries().firstOrNull()
            ?.find { it.id == entry.id }
            ?.answer

        dao.insertAboutMeEntry(entry.toEntity())

        commitDomainMutation(
            profileUid = requireProfileUid(),
            fieldName = "aboutMe.${entry.prompt}",
            oldValue = previous,
            newValue = entry.answer,
            eventTitle = "About Me Updated",
            eventType = EventType.ABOUT_ME_UPDATED
        )
    }

    override suspend fun saveFavoriteEntry(entry: FavoriteEntry) {
        requireInitializedProfile(dao.getProfile().firstOrNull()?.toDomain())

        val previous = dao.getFavoriteEntries().firstOrNull()
            ?.find { it.id == entry.id }
            ?.item

        dao.insertFavoriteEntry(entry.toEntity())

        commitDomainMutation(
            profileUid = requireProfileUid(),
            fieldName = "favorite.${entry.category}",
            oldValue = previous,
            newValue = entry.item,
            eventTitle = "Favorite Updated",
            eventType = EventType.FAVORITE_UPDATED
        )
    }

    override suspend fun saveDreamEntry(entry: DreamEntry) {
        requireInitializedProfile(dao.getProfile().firstOrNull()?.toDomain())

        val previous = dao.getDreamEntries().firstOrNull()
            ?.find { it.id == entry.id }
            ?.content

        dao.insertDreamEntry(entry.toEntity())

        commitDomainMutation(
            profileUid = requireProfileUid(),
            fieldName = "dream.${entry.type}",
            oldValue = previous,
            newValue = entry.content,
            eventTitle = "Dream Updated",
            eventType = EventType.DREAM_UPDATED
        )
    }

    override fun getAvatarHistory(): Flow<List<Avatar>> =
        dao.getAvatarHistory().map { list -> list.map { it.toDomain() } }

    override suspend fun saveAvatar(avatar: Avatar) {
        requireInitializedProfile(dao.getProfile().firstOrNull()?.toDomain())

        val previous = dao.getLatestAvatar().firstOrNull()
        dao.insertAvatar(avatar.toEntity())

        val profileUid = requireProfileUid()
        val oldAvatarSummary = previous?.toSummary()
        val newAvatarSummary =
            "${avatar.hairType}|${avatar.eyeColor}|${avatar.skinTone}|${avatar.clothingId}|v${avatar.version}"

        commitDomainMutation(
            profileUid = profileUid,
            fieldName = if (previous != null) "avatar" else null,
            oldValue = oldAvatarSummary,
            newValue = newAvatarSummary,
            eventTitle = "Avatar Updated",
            eventType = EventType.AVATAR_UPDATED,
            extraMetadata = listOf(
                timelineMetadata("avatar", category = "version", value = avatar.version.toString())
            )
        )
    }

    override fun getTimelineEvents(): Flow<List<TimelineEvent>> =
        dao.getTimelineEvents().map { list -> list.map { it.toDomain(forgeProfileJson) } }

    override fun getTeacherMetadata(profileUid: String): Flow<List<TeacherMetadata>> =
        dao.getTeacherMetadata(profileUid).map { list -> list.map { it.toDomain() } }

    override fun getMemoryArtifacts(): Flow<List<MemoryArtifact>> =
        dao.getMemoryArtifacts().map { list ->
            list.map {
                MemoryArtifact(
                    id = it.id,
                    type = it.type,
                    localPath = it.localPath,
                    checksum = it.checksum,
                    capturedAt = Instant.ofEpochMilli(it.capturedAt),
                    note = it.note,
                    category = it.category,
                    source = it.source,
                    syncedToStudent = it.syncedToStudent,
                )
            }
        }

    override suspend fun saveMemoryArtifact(artifact: MemoryArtifact) {
        requireInitializedProfile(dao.getProfile().firstOrNull()?.toDomain())

        dao.insertMemoryArtifact(
            MemoryArtifactEntity(
                id = artifact.id,
                type = artifact.type,
                localPath = artifact.localPath,
                checksum = artifact.checksum,
                capturedAt = artifact.capturedAt.toEpochMilli(),
                note = artifact.note,
                category = artifact.category,
                source = artifact.source,
                syncedToStudent = artifact.syncedToStudent,
            )
        )
        insertTimelineEvent(
            TimelineEvent(
                id = UUID.randomUUID().toString(),
                title = "Memory: ${artifact.category.displayName}",
                type = EventType.MEMORY_CAPTURED,
                timestamp = Instant.now(),
                metadata = listOf(
                    timelineMetadata("artifact", category = "type", value = artifact.type.name),
                    timelineMetadata("artifact", category = "memory_category", value = artifact.category.name),
                    timelineMetadata("artifact", category = "source", value = artifact.source.name),
                )
            )
        )
        appendChildhoodStateSnapshot(requireProfileUid(), captureCurrentPayload())
    }

    override suspend fun markMemorySyncedToStudent(artifactId: String) {
        dao.markMemorySyncedToStudent(artifactId)
    }

    override fun getDreamEntries(): Flow<List<DreamEntry>> =
        dao.getDreamEntries().map { list ->
            list.map {
                DreamEntry(
                    id = it.id,
                    type = it.type,
                    content = it.content,
                    timestamp = Instant.ofEpochMilli(it.timestamp)
                )
            }
        }

    override fun getAboutMeEntries(): Flow<List<AboutMeEntry>> =
        dao.getAboutMeEntries().map { list ->
            list.map {
                AboutMeEntry(
                    id = it.id,
                    prompt = it.prompt,
                    answer = it.answer,
                    timestamp = Instant.ofEpochMilli(it.timestamp)
                )
            }
        }

    override suspend fun clearAllData() {
        dao.deleteAllDreams()
        dao.deleteAllAboutMe()
        dao.deleteAllFavorites()
        dao.deleteAllAvatars()
        dao.deleteAllTimelineEvents()
        dao.deleteAllMemoryArtifacts()
        dao.deleteAllIdentitySnapshots()
        dao.deleteAllTeacherMetadata()
        dao.deleteAllLinkedStudents()
        dao.deleteAllProfiles()
    }

    override fun getLinkedStudents(): Flow<List<LinkedStudentProfile>> =
        dao.getLinkedStudents().map { list ->
            list.map {
                LinkedStudentProfile(
                    profileUid = it.profileUid,
                    displayName = it.displayName,
                    linkedAt = Instant.ofEpochMilli(it.linkedAt),
                    notes = it.notes
                )
            }
        }

    override suspend fun linkStudentProfile(displayName: String, profileUid: String, notes: String?) {
        requireInitializedProfile(dao.getProfile().firstOrNull()?.toDomain())
        dao.insertLinkedStudent(
            LinkedStudentEntity(
                profileUid = profileUid.trim(),
                displayName = displayName.trim(),
                linkedAt = Instant.now().toEpochMilli(),
                notes = notes?.trim()?.takeIf { it.isNotEmpty() }
            )
        )
    }

    override suspend fun unlinkStudentProfile(profileUid: String) {
        dao.deleteLinkedStudent(profileUid)
    }

    private suspend fun commitDomainMutation(
        profileUid: String,
        fieldName: String?,
        oldValue: String?,
        newValue: String,
        eventTitle: String,
        eventType: EventType,
        extraMetadata: List<TimelineMetadata> = emptyList()
    ) {
        if (fieldName != null && oldValue != newValue) {
            createFieldSnapshot(profileUid, fieldName, oldValue, newValue)
        }
        val payload = captureCurrentPayload()
        insertTimelineEvent(
            TimelineEvent(
                id = UUID.randomUUID().toString(),
                title = eventTitle,
                type = eventType,
                timestamp = Instant.now(),
                metadata = extraMetadata + timelineMetadataFromPayload(payload)
            )
        )
        appendChildhoodStateSnapshot(profileUid, payload)
    }

    private suspend fun persistFullChildhoodSnapshot(
        profileUid: String,
        previous: ChildhoodSnapshotPayload?,
        current: ChildhoodSnapshotPayload,
        eventTitle: String
    ) {
        dao.insertIdentitySnapshot(
            IdentitySnapshotEntity(
                id = UUID.randomUUID().toString(),
                profileUid = profileUid,
                fieldName = "childhood_state",
                oldValue = previous?.let { forgeProfileJson.encodeChildhoodSnapshot(it) },
                newValue = forgeProfileJson.encodeChildhoodSnapshot(current),
                timestamp = Instant.now().toEpochMilli()
            )
        )
        insertTimelineEvent(
            TimelineEvent(
                id = UUID.randomUUID().toString(),
                title = eventTitle,
                type = EventType.PROFILE_CHANGE,
                timestamp = Instant.now(),
                metadata = timelineMetadataFromPayload(current)
            )
        )
    }

    private suspend fun appendChildhoodStateSnapshot(
        profileUid: String,
        payload: ChildhoodSnapshotPayload
    ) {
        dao.insertIdentitySnapshot(
            IdentitySnapshotEntity(
                id = UUID.randomUUID().toString(),
                profileUid = profileUid,
                fieldName = "childhood_state",
                oldValue = null,
                newValue = forgeProfileJson.encodeChildhoodSnapshot(payload),
                timestamp = Instant.now().toEpochMilli()
            )
        )
    }

    private suspend fun insertTimelineEvent(event: TimelineEvent) {
        dao.insertTimelineEvent(
            TimelineEventEntity(
                id = event.id,
                title = event.title,
                type = event.type,
                timestamp = event.timestamp.toEpochMilli(),
                metadataJson = forgeProfileJson.encodeTimelineMetadata(event.metadata)
            )
        )
    }

    private fun timelineMetadataFromPayload(payload: ChildhoodSnapshotPayload): List<TimelineMetadata> =
        listOf(
            timelineMetadata("snapshot", category = "forgeName", value = payload.forgeName),
            timelineMetadata("snapshot", category = "currentTitle", value = payload.currentTitle),
            timelineMetadata("snapshot", category = "avatarVersion", value = payload.avatar?.version?.toString()),
            timelineMetadata("snapshot", category = "aboutMeCount", value = payload.aboutMe.size.toString()),
            timelineMetadata("snapshot", category = "favoritesCount", value = payload.favorites.size.toString()),
            timelineMetadata("snapshot", category = "dreamsCount", value = payload.dreams.size.toString())
        )

    private fun timelineMetadata(
        source: String,
        category: String,
        value: String?
    ): TimelineMetadata = TimelineMetadata(source = source, category = category, value = value)

    private suspend fun captureCurrentPayload(): ChildhoodSnapshotPayload {
        val profile = dao.getProfile().firstOrNull()
        val avatar = dao.getLatestAvatar().firstOrNull()
        val aboutMe = dao.getAboutMeEntries().firstOrNull().orEmpty()
        val favorites = dao.getFavoriteEntries().firstOrNull().orEmpty()
        val dreams = dao.getDreamEntries().firstOrNull().orEmpty()

        return ChildhoodSnapshotPayload(
            forgeName = profile?.forgeName ?: "",
            currentTitle = profile?.currentTitle,
            currentStage = profile?.currentStage ?: "EARLY_DISCOVERY",
            aboutMe = aboutMe.map { AboutMeSnapshot(it.prompt, it.answer) },
            favorites = favorites.map { FavoriteSnapshot(it.category, it.item) },
            dreams = dreams.map { DreamSnapshot(it.type, it.content) },
            avatar = avatar?.let {
                AvatarSnapshot(
                    hairType = it.hairType,
                    eyeColor = it.eyeColor,
                    skinTone = it.skinTone,
                    clothingId = it.clothingId,
                    version = it.version,
                    shardLevel = it.shardLevel,
                )
            }
        )
    }

    private suspend fun diffField(
        profileUid: String,
        field: String,
        old: String?,
        new: String?
    ) {
        if (old != new) {
            createFieldSnapshot(profileUid, field, old, new ?: "")
        }
    }

    private suspend fun createFieldSnapshot(
        profileUid: String,
        field: String,
        old: String?,
        new: String
    ) {
        dao.insertIdentitySnapshot(
            IdentitySnapshotEntity(
                id = UUID.randomUUID().toString(),
                profileUid = profileUid,
                fieldName = field,
                oldValue = old,
                newValue = new,
                timestamp = Instant.now().toEpochMilli()
            )
        )
    }

    private suspend fun requireProfileUid(): String =
        dao.getProfile().firstOrNull()?.uid
            ?: throw IllegalStateException("Profile must exist before recording childhood history")

    private fun requireInitializedProfile(profile: ForgeProfile?) {
        require(profile != null) {
            "ForgeProfile not initialized — bootstrap required before mutations"
        }
    }

    private fun ProfileEntity.toDomain(): ForgeProfile = ForgeProfile(
        uid = uid,
        forgeName = forgeName,
        realName = realName,
        birthDate = birthDate?.let { Instant.ofEpochMilli(it) },
        ageYears = ageYears,
        pronouns = pronouns,
        favoriteColor = favoriteColor,
        currentTitle = currentTitle,
        currentStage = currentStage,
        sparkMaturationTier = sparkMaturationTier,
        profileRole = profileRole
    )

    private fun ForgeProfile.toEntity(): ProfileEntity = ProfileEntity(
        uid = uid,
        forgeName = forgeName,
        realName = realName,
        birthDate = birthDate?.toEpochMilli(),
        ageYears = ageYears,
        pronouns = null,
        favoriteColor = favoriteColor,
        currentTitle = currentTitle,
        currentStage = currentStage,
        sparkMaturationTier = sparkMaturationTier,
        profileRole = profileRole
    )

    private fun AvatarEntity.toDomain(): Avatar = Avatar(
        id = id,
        hairType = hairType,
        eyeColor = eyeColor,
        skinTone = skinTone,
        clothingId = clothingId,
        version = version,
        shardLevel = shardLevel,
        timestamp = Instant.ofEpochMilli(timestamp),
    )

    private fun AvatarEntity.toSummary(): String =
        "$hairType|$eyeColor|$skinTone|$clothingId|v$version"

    private fun Avatar.toEntity(): AvatarEntity = AvatarEntity(
        id = id,
        hairType = hairType,
        eyeColor = eyeColor,
        skinTone = skinTone,
        clothingId = clothingId,
        version = version,
        shardLevel = shardLevel,
        timestamp = timestamp.toEpochMilli(),
    )

    private fun AboutMeEntry.toEntity(): AboutMeEntryEntity = AboutMeEntryEntity(
        id = id,
        prompt = prompt,
        answer = answer,
        timestamp = timestamp.toEpochMilli()
    )

    private fun FavoriteEntry.toEntity(): FavoriteEntryEntity = FavoriteEntryEntity(
        id = id,
        category = category,
        item = item,
        timestamp = timestamp.toEpochMilli()
    )

    private fun DreamEntry.toEntity(): DreamEntryEntity = DreamEntryEntity(
        id = id,
        type = type,
        content = content,
        timestamp = timestamp.toEpochMilli()
    )

    private fun TeacherMetadataEntity.toDomain(): TeacherMetadata = TeacherMetadata(
        id = id,
        profileUid = profileUid,
        key = key,
        value = value,
        category = category,
        timestamp = Instant.ofEpochMilli(timestamp)
    )

    private fun TimelineEventEntity.toDomain(json: ForgeProfileJson): TimelineEvent =
        TimelineEvent(
            id = id,
            title = title,
            type = type,
            timestamp = Instant.ofEpochMilli(timestamp),
            metadata = json.decodeTimelineMetadata(metadataJson)
        )
}
