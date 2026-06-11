package com.phoenixforge.classroom.teacher.data.bridge

import com.phoenixforge.classroom.teacher.data.students.StudentBehaviorSignalsSnapshot
import com.phoenixforge.classroom.teacher.data.students.StudentProfileSnapshot
import com.phoenixforge.classroom.teacher.data.students.StudentProgressSnapshot
import com.phoenixforge.classroom.teacher.data.students.StudentStoryFragmentSnapshot
import com.phoenixforge.classroom.teacher.data.students.StudentSyncReader
import javax.inject.Inject
import javax.inject.Singleton

data class ChildBridgeSnapshot(
    val resolved: ResolvedChildUid,
    val profile: StudentProfileSnapshot?,
    val progress: StudentProgressSnapshot?,
    val behaviorSignals: StudentBehaviorSignalsSnapshot?,
    val emotionalCheckIns: List<StudentStoryFragmentSnapshot>,
    val forgeEvents: List<ForgeEventRow>,
    val timelineRows: List<ForgeTimelineRow>,
    val dataSources: List<String>,
    val lanEventCount: Int?,
)

@Singleton
class ChildProfileBridge @Inject constructor(
    private val childStudentUidResolver: ChildStudentUidResolver,
    private val forgeProfileChildReader: ForgeProfileChildReader,
    private val forgeProfileLanEventsClient: ForgeProfileLanEventsClient,
    private val forgeProfileLanDiscovery: ForgeProfileLanDiscovery,
    private val studentSyncReader: StudentSyncReader,
) {
    suspend fun loadSnapshot(): ChildBridgeSnapshot? {
        val resolved = childStudentUidResolver.resolve() ?: return null

        val sources = mutableListOf<String>()
        sources += resolved.source.label

        val forgeProfile = forgeProfileChildReader.readChildProfile(resolved.studentUid)
            ?: forgeProfileChildReader.listChildProfiles()
                .firstOrNull { it.uid == resolved.studentUid }
        if (forgeProfile != null) {
            sources += "Forge Profile profile"
        }

        var forgeEvents = forgeProfileChildReader.readForgeEvents(resolved.studentUid)
        if (forgeEvents.isNotEmpty()) {
            sources += "Forge Profile events (${forgeEvents.size})"
        }

        val timelineRows = forgeProfileChildReader.readChildTimeline(resolved.studentUid)
        if (timelineRows.isNotEmpty()) {
            sources += "Forge Profile timeline (${timelineRows.size})"
        }

        var lanEventCount: Int? = null
        val lanHosts = buildList {
            add("127.0.0.1")
            forgeProfileLanDiscovery.discoverPeers().forEach { peer -> add(peer.host) }
        }.distinct()
        for (host in lanHosts) {
            if (!forgeProfileLanEventsClient.ping(host)) continue
            val lan = forgeProfileLanEventsClient.fetchEvents(resolved.studentUid, host) ?: continue
            if (lan.events.isEmpty()) continue
            lanEventCount = (lanEventCount ?: 0) + lan.eventCount
            val label = if (host == "127.0.0.1") "LAN :7433 (this phone)" else "LAN :7433 ($host)"
            sources += "$label (${lan.eventCount})"
            if (forgeEvents.isEmpty()) {
                forgeEvents = lan.events.map { event ->
                    ForgeEventRow(
                        eventId = event.eventId,
                        eventType = event.eventType,
                        scope = event.scope,
                        actorApp = event.actorApp,
                        logicalClock = event.logicalClock,
                        epochMs = event.epochMs,
                    )
                }
            }
        }

        val studentProfile = studentSyncReader.readProfileSnapshot()
        val studentMatches = studentProfile?.uid == resolved.studentUid
        val progress = if (studentMatches) studentSyncReader.readProgress() else null
        val behavior = if (studentMatches) studentSyncReader.readBehaviorSignals() else null
        val checkIns = if (studentMatches) studentSyncReader.readStoryFragments() else emptyList()
        if (studentMatches) {
            sources += "Student Edition session"
        }

        val profile = when {
            forgeProfile != null -> StudentProfileSnapshot(
                uid = forgeProfile.uid,
                forgeName = forgeProfile.forgeName,
                currentStage = forgeProfile.currentStage,
                currentTitle = forgeProfile.currentTitle,
                avatarSummary = studentProfile?.takeIf { studentMatches }?.avatarSummary,
                timelineSummary = timelineRows.firstOrNull()?.title,
                importedAtEpochMillis = studentProfile?.takeIf { studentMatches }?.importedAtEpochMillis,
            )
            studentMatches -> studentProfile
            else -> StudentProfileSnapshot(
                uid = resolved.studentUid,
                forgeName = resolved.displayName,
                currentStage = null,
                currentTitle = null,
                avatarSummary = null,
                timelineSummary = timelineRows.firstOrNull()?.title,
                importedAtEpochMillis = null,
            )
        }

        return ChildBridgeSnapshot(
            resolved = resolved,
            profile = profile,
            progress = progress,
            behaviorSignals = behavior,
            emotionalCheckIns = checkIns,
            forgeEvents = forgeEvents.sortedByDescending { it.logicalClock },
            timelineRows = timelineRows,
            dataSources = sources.distinct(),
            lanEventCount = lanEventCount,
        )
    }
}
