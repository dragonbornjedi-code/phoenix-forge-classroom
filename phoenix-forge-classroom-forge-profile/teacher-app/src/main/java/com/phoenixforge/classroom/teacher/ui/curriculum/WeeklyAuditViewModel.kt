package com.phoenixforge.classroom.teacher.ui.curriculum

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenixforge.classroom.teacher.data.local.CurriculumAuditStore
import com.phoenixforge.classroom.teacher.domain.curriculum.CurriculumDomainId
import com.phoenixforge.classroom.teacher.domain.curriculum.WeeklyAuditDraft
import com.phoenixforge.classroom.teacher.domain.curriculum.WeeklyAuditOverall
import com.phoenixforge.classroom.teacher.domain.curriculum.WeeklyAuditSectionEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeeklyAuditViewModel @Inject constructor(
    private val auditStore: CurriculumAuditStore
) : ViewModel() {

    private val _draft = MutableStateFlow(auditStore.loadDraft())
    val draft: StateFlow<WeeklyAuditDraft> = _draft.asStateFlow()

    private val _saved = MutableStateFlow(false)
    val saved: StateFlow<Boolean> = _saved.asStateFlow()

    fun updateSection(domain: CurriculumDomainId, field: SectionField, value: String) {
        val current = _draft.value.sections[domain] ?: WeeklyAuditSectionEntry(domainId = domain)
        val updated = when (field) {
            SectionField.WIN -> current.copy(winObserved = value)
            SectionField.FRICTION -> current.copy(frictionPoint = value)
            SectionField.METHOD_WORKED -> current.copy(methodWorked = value)
            SectionField.METHOD_TRY_NEXT -> current.copy(methodToTryNext = value)
            SectionField.METRIC -> current.copy(metricToTrack = value)
        }
        _draft.value = _draft.value.copy(
            sections = _draft.value.sections + (domain to updated)
        )
        _saved.value = false
    }

    fun updateOverall(field: OverallField, value: String) {
        val current = _draft.value.overall
        val updated = when (field) {
            OverallField.ENERGY -> current.copy(bestEnergyWindow = value)
            OverallField.TRANSITION -> current.copy(hardestTransition = value)
            OverallField.MOTIVATOR -> current.copy(strongestMotivator = value)
            OverallField.OVERLOAD -> current.copy(overloadSigns = value)
            OverallField.ADJUSTMENT -> current.copy(nextAdjustment = value)
        }
        _draft.value = _draft.value.copy(overall = updated)
        _saved.value = false
    }

    fun save() {
        viewModelScope.launch {
            auditStore.saveDraft(_draft.value)
            _saved.value = true
        }
    }

    enum class SectionField { WIN, FRICTION, METHOD_WORKED, METHOD_TRY_NEXT, METRIC }
    enum class OverallField { ENERGY, TRANSITION, MOTIVATOR, OVERLOAD, ADJUSTMENT }
}
