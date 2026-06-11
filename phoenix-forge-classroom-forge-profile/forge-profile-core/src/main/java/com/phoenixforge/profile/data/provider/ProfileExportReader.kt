package com.phoenixforge.profile.data.provider

import com.phoenixforge.profile.data.local.dao.EventRecordDao
import com.phoenixforge.profile.data.local.dao.MessageDao
import com.phoenixforge.profile.data.sync.ForgeMessageRecord
import com.phoenixforge.profile.domain.access.ProfileImportPolicy
import com.phoenixforge.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileExportReader @Inject constructor(
    private val repository: ProfileRepository,
    private val eventRecordDao: EventRecordDao,
    private val messageDao: MessageDao,
) {

    /** Child profile for Student Edition import — never returns adult/steward rows. */
    suspend fun readProfile(): ProfileExportDto? =
        repository.getImportableProfile()?.let { profile -> profile.toExportDto() }

    /** Active signed-in profile — used by Teacher Edition for adult identity. */
    suspend fun readActiveProfile(): ProfileExportDto? {
        repository.ensureActiveProfile()
        return repository.getProfile().firstOrNull()?.toExportDto()
    }

    suspend fun listChildProfiles(): List<ProfileExportDto> =
        repository.listProfiles().firstOrNull().orEmpty()
            .filter { ProfileImportPolicy.isImportableRole(it.profileRole) }
            .map { it.toExportDto() }

    suspend fun listLinkedStudents(): List<LinkedStudentExportDto> =
        repository.getLinkedStudents().firstOrNull().orEmpty().map { linked ->
            LinkedStudentExportDto(
                profileUid = linked.profileUid,
                displayName = linked.displayName,
                linkedAtEpochMillis = linked.linkedAt.toEpochMilli(),
                notes = linked.notes,
            )
        }

    suspend fun readProfileForUid(uid: String): ProfileExportDto? {
        val normalized = uid.trim()
        if (normalized.isEmpty()) return null
        return repository.listProfiles().firstOrNull()
            ?.firstOrNull { it.uid == normalized }
            ?.toExportDto()
    }

    suspend fun readLatestAvatar(): AvatarExportDto? {
        val profile = repository.getImportableProfile() ?: return null
        return readLatestAvatarForUid(profile.uid)
    }

    suspend fun readLatestAvatarForUid(uid: String): AvatarExportDto? {
        val profile = repository.listProfiles().firstOrNull()
            ?.firstOrNull { it.uid == uid.trim() }
            ?: return null
        val avatar = repository.getAvatarHistoryFor(profile.uid).firstOrNull()?.firstOrNull() ?: return null
        return AvatarExportPayload.build(profile.uid, profile.forgeName, avatar)
    }

    suspend fun readTimeline(): List<TimelineEventExportDto> {
        val profile = repository.getImportableProfile() ?: return emptyList()
        return readTimelineForUid(profile.uid)
    }

    suspend fun readTimelineForUid(uid: String): List<TimelineEventExportDto> =
        repository.getTimelineEventsFor(uid.trim()).firstOrNull().orEmpty().map { event ->
            TimelineEventExportDto(
                title = event.title,
                type = event.type.name,
                timestampEpochMillis = event.timestamp.toEpochMilli(),
            )
        }

    suspend fun readForgeEventsForUid(uid: String): List<ForgeEventExportDto> =
        eventRecordDao.listEventsForStudent(uid.trim()).map { row ->
            ForgeEventExportDto(
                eventId = row.eventId,
                eventType = row.eventType,
                scope = row.scope,
                actorApp = row.actorApp,
                logicalClock = row.logicalClock,
                epochMs = row.epochMs,
            )
        }

    suspend fun readMessagesForUid(uid: String, targetApp: String? = null): List<MessageExportDto> {
        val normalized = uid.trim()
        val rows = if (targetApp.isNullOrBlank()) {
            messageDao.listForStudent(normalized)
        } else {
            messageDao.listForStudentAndApp(normalized, targetApp.trim())
        }
        return rows.map { row ->
            MessageExportDto(
                messageId = row.messageId,
                threadId = row.threadId,
                direction = row.direction,
                fromDeviceId = row.fromDeviceId,
                fromDisplayName = row.fromDisplayName,
                toStudentUid = row.toStudentUid,
                targetApp = row.targetApp,
                epochMs = row.epochMs,
                subject = row.subject,
                bodyMarkdown = row.bodyMarkdown,
                readEpochMs = row.readEpochMs,
                replyToMessageId = row.replyToMessageId,
            )
        }
    }

    suspend fun readImportableMessages(targetApp: String? = ForgeMessageRecord.TARGET_STUDENT): List<MessageExportDto> {
        val profile = repository.getImportableProfile() ?: return emptyList()
        return readMessagesForUid(profile.uid, targetApp)
    }

    suspend fun readMemories(): List<MemoryExportDto> {
        val profile = repository.getImportableProfile() ?: return emptyList()
        return readMemoriesForUid(profile.uid)
    }

    suspend fun readMemoriesForUid(uid: String): List<MemoryExportDto> =
        repository.getMemoryArtifactsFor(uid.trim()).firstOrNull().orEmpty().map { artifact ->
            MemoryExportDto(
                id = artifact.id,
                type = artifact.type.name,
                category = artifact.category.name,
                source = artifact.source.name,
                capturedAtEpochMillis = artifact.capturedAt.toEpochMilli(),
                note = artifact.note,
                checksum = artifact.checksum,
                syncedToStudent = artifact.syncedToStudent,
                contentUri = ProfileContract.memoryFileUri(artifact.id).toString(),
            )
        }

    private fun com.phoenixforge.profile.domain.model.ForgeProfile.toExportDto() =
        ProfileExportDto(
            uid = uid,
            forgeName = forgeName,
            currentStage = currentStage,
            currentTitle = currentTitle,
            ageYears = ageYears,
            profileRole = profileRole,
        )
}
