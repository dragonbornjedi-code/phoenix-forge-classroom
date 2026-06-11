package com.phoenixforge.classroom.teacher.ui.students

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.content.Context
import com.phoenixforge.classroom.teacher.data.bridge.ChildProfileBridge
import com.phoenixforge.classroom.teacher.data.bridge.ChildStudentUidResolver
import com.phoenixforge.classroom.teacher.data.bridge.ForgeEventRow
import com.phoenixforge.classroom.teacher.data.bridge.ForgeProfileChildReader
import com.phoenixforge.classroom.teacher.data.bridge.ForgeProfileLanDiscovery
import com.phoenixforge.classroom.teacher.data.bridge.ForgeTimelineRow
import com.phoenixforge.classroom.teacher.data.bridge.StudentConnectionStatus
import com.phoenixforge.classroom.teacher.data.bridge.buildStudentConnectionStatus
import com.phoenixforge.classroom.teacher.ui.interop.TeacherForgeProfileLauncher
import dagger.hilt.android.qualifiers.ApplicationContext
import com.phoenixforge.classroom.teacher.data.notes.TeacherNotesStore
import com.phoenixforge.classroom.teacher.data.students.StudentBehaviorSignalsSnapshot
import com.phoenixforge.classroom.teacher.data.students.StudentProfileSnapshot
import com.phoenixforge.classroom.teacher.data.students.StudentProgressSnapshot
import com.phoenixforge.classroom.teacher.data.students.StudentStoryFragmentSnapshot
import com.phoenixforge.classroom.teacher.domain.manifest.ManifestPushCoordinator
import com.phoenixforge.classroom.teacher.domain.messages.MessageRelayCoordinator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class StudentSnapshotUiState(
    val profile: StudentProfileSnapshot? = null,
    val progress: StudentProgressSnapshot? = null,
    val behaviorSignals: StudentBehaviorSignalsSnapshot? = null,
    val emotionalCheckIns: List<StudentStoryFragmentSnapshot> = emptyList(),
    val forgeEvents: List<ForgeEventRow> = emptyList(),
    val timelineRows: List<ForgeTimelineRow> = emptyList(),
    val dataSources: List<String> = emptyList(),
    val uidSourceLabel: String? = null,
    val teacherNotes: String = "",
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val showSendDialog: Boolean = false,
    val isPushing: Boolean = false,
    val pushMessage: String? = null,
    val messageSubject: String = "",
    val messageBody: String = "",
    val isSendingMessage: Boolean = false,
    val messageStatus: String? = null,
    val connectionStatus: StudentConnectionStatus? = null,
    val linkedStudentUids: List<String> = emptyList(),
    val staleRememberedUid: String? = null,
    val lanPeerCount: Int = 0,
)

@HiltViewModel
class StudentSnapshotViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val childProfileBridge: ChildProfileBridge,
    private val childStudentUidResolver: ChildStudentUidResolver,
    private val forgeProfileChildReader: ForgeProfileChildReader,
    private val forgeProfileLanDiscovery: ForgeProfileLanDiscovery,
    private val teacherNotesStore: TeacherNotesStore,
    private val manifestPushCoordinator: ManifestPushCoordinator,
    private val messageRelayCoordinator: MessageRelayCoordinator,
) : ViewModel() {

    private val _state = MutableStateFlow(StudentSnapshotUiState())
    val state = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null, pushMessage = null)
            val linked = forgeProfileChildReader.listLinkedStudents()
            val staleUid = childStudentUidResolver.peekRememberedStudentUid()
            val lanPeers = forgeProfileLanDiscovery.discoverPeers()
            val snapshot = childProfileBridge.loadSnapshot()
            val connection = buildStudentConnectionStatus(
                resolved = snapshot?.resolved,
                linkedStudentCount = linked.size,
                staleRememberedUid = staleUid,
                lanPeerCount = lanPeers.size,
                lanEventCount = snapshot?.lanEventCount,
            )

            if (snapshot == null) {
                _state.value = StudentSnapshotUiState(
                    isLoading = false,
                    errorMessage = null,
                    connectionStatus = connection,
                    linkedStudentUids = linked.map { it.profileUid },
                    staleRememberedUid = staleUid,
                    lanPeerCount = lanPeers.size,
                )
                return@launch
            }

            val uid = snapshot.profile?.uid.orEmpty()
            _state.value = StudentSnapshotUiState(
                profile = snapshot.profile,
                progress = snapshot.progress,
                behaviorSignals = snapshot.behaviorSignals,
                emotionalCheckIns = snapshot.emotionalCheckIns,
                forgeEvents = snapshot.forgeEvents,
                timelineRows = snapshot.timelineRows,
                dataSources = snapshot.dataSources,
                uidSourceLabel = snapshot.resolved.source.label,
                teacherNotes = if (uid.isNotBlank()) teacherNotesStore.getNotes(uid) else "",
                isLoading = false,
                errorMessage = if (connection.isTrustworthy) null else connection.headline,
                connectionStatus = connection,
                linkedStudentUids = linked.map { it.profileUid },
                staleRememberedUid = staleUid,
                lanPeerCount = lanPeers.size,
            )
        }
    }

    fun clearStaleCache() {
        childStudentUidResolver.clearRememberedStudentUid()
        refresh()
    }

    fun openForgeProfileToLink() {
        TeacherForgeProfileLauncher.openForgeProfile(context)
    }

    fun updateTeacherNotes(value: String) {
        val uid = _state.value.profile?.uid.orEmpty()
        if (uid.isBlank()) return
        _state.value = _state.value.copy(teacherNotes = value)
        teacherNotesStore.setNotes(uid, value)
    }

    fun openSendDialog() {
        _state.value = _state.value.copy(showSendDialog = true, pushMessage = null)
    }

    fun dismissSendDialog() {
        _state.value = _state.value.copy(showSendDialog = false)
    }

    fun pushQuestsOnly() {
        pushToStudent(includeStoryAdventure = false)
    }

    fun pushQuestsAndStory() {
        pushToStudent(includeStoryAdventure = true)
    }

    fun updateMessageSubject(value: String) {
        _state.value = _state.value.copy(messageSubject = value)
    }

    fun updateMessageBody(value: String) {
        _state.value = _state.value.copy(messageBody = value)
    }

    fun sendPersonalMessage() {
        viewModelScope.launch {
            val subject = _state.value.messageSubject.trim()
            val body = _state.value.messageBody.trim()
            if (subject.isEmpty() || body.isEmpty()) {
                _state.value = _state.value.copy(messageStatus = "Write a subject and message first.")
                return@launch
            }
            _state.value = _state.value.copy(isSendingMessage = true, messageStatus = null)
            val result = messageRelayCoordinator.sendToStudent(subject = subject, body = body)
            _state.value = _state.value.copy(
                isSendingMessage = false,
                messageStatus = result?.message
                    ?: "No linked student. Set up Ezra's profile in Forge Profile first.",
                messageSubject = if (result != null) "" else subject,
                messageBody = if (result != null) "" else body,
            )
        }
    }

    private fun pushToStudent(includeStoryAdventure: Boolean) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isPushing = true, pushMessage = null)
            val result = manifestPushCoordinator.pushTodayStack()
            val message = result?.message
                ?: "No student profile ID. Import child profile in Student Edition on this phone, then retry."
            val suffix = if (includeStoryAdventure) {
                " Story adventure playalong will ride the same manifest in a later slice."
            } else {
                ""
            }
            _state.value = _state.value.copy(
                isPushing = false,
                showSendDialog = false,
                pushMessage = message + suffix,
            )
        }
    }
}
