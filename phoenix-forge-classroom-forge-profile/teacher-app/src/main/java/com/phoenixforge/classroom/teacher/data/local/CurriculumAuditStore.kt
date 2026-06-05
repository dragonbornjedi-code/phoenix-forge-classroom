package com.phoenixforge.classroom.teacher.data.local

import android.content.SharedPreferences
import com.phoenixforge.classroom.teacher.domain.curriculum.CurriculumDomainId
import com.phoenixforge.classroom.teacher.domain.curriculum.WeeklyAuditDraft
import com.phoenixforge.classroom.teacher.domain.curriculum.WeeklyAuditOverall
import com.phoenixforge.classroom.teacher.domain.curriculum.WeeklyAuditSectionEntry
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurriculumAuditStore @Inject constructor(
    private val prefs: SharedPreferences
) {
    fun loadDraft(): WeeklyAuditDraft {
        val sections = CurriculumDomainId.entries.associateWith { domain ->
            WeeklyAuditSectionEntry(
                domainId = domain,
                winObserved = prefs.getString(key(domain, "win"), "").orEmpty(),
                frictionPoint = prefs.getString(key(domain, "friction"), "").orEmpty(),
                methodWorked = prefs.getString(key(domain, "worked"), "").orEmpty(),
                methodToTryNext = prefs.getString(key(domain, "try_next"), "").orEmpty(),
                metricToTrack = prefs.getString(key(domain, "metric"), "").orEmpty()
            )
        }
        val overall = WeeklyAuditOverall(
            bestEnergyWindow = prefs.getString(KEY_OVERALL_ENERGY, "").orEmpty(),
            hardestTransition = prefs.getString(KEY_OVERALL_TRANSITION, "").orEmpty(),
            strongestMotivator = prefs.getString(KEY_OVERALL_MOTIVATOR, "").orEmpty(),
            overloadSigns = prefs.getString(KEY_OVERALL_OVERLOAD, "").orEmpty(),
            nextAdjustment = prefs.getString(KEY_OVERALL_ADJUST, "").orEmpty()
        )
        return WeeklyAuditDraft(
            sections = sections,
            overall = overall,
            savedAtEpochMs = prefs.getLong(KEY_SAVED_AT, 0L)
        )
    }

    fun saveDraft(draft: WeeklyAuditDraft) {
        val editor = prefs.edit()
        draft.sections.forEach { (domain, entry) ->
            editor.putString(key(domain, "win"), entry.winObserved)
            editor.putString(key(domain, "friction"), entry.frictionPoint)
            editor.putString(key(domain, "worked"), entry.methodWorked)
            editor.putString(key(domain, "try_next"), entry.methodToTryNext)
            editor.putString(key(domain, "metric"), entry.metricToTrack)
        }
        editor.putString(KEY_OVERALL_ENERGY, draft.overall.bestEnergyWindow)
        editor.putString(KEY_OVERALL_TRANSITION, draft.overall.hardestTransition)
        editor.putString(KEY_OVERALL_MOTIVATOR, draft.overall.strongestMotivator)
        editor.putString(KEY_OVERALL_OVERLOAD, draft.overall.overloadSigns)
        editor.putString(KEY_OVERALL_ADJUST, draft.overall.nextAdjustment)
        editor.putLong(KEY_SAVED_AT, System.currentTimeMillis())
        editor.apply()
    }

    fun clearDraft() {
        prefs.edit().clear().apply()
    }

    private fun key(domain: CurriculumDomainId, field: String): String =
        "audit_${domain.name}_$field"

    private companion object {
        const val KEY_OVERALL_ENERGY = "audit_overall_energy"
        const val KEY_OVERALL_TRANSITION = "audit_overall_transition"
        const val KEY_OVERALL_MOTIVATOR = "audit_overall_motivator"
        const val KEY_OVERALL_OVERLOAD = "audit_overall_overload"
        const val KEY_OVERALL_ADJUST = "audit_overall_adjust"
        const val KEY_SAVED_AT = "audit_saved_at"
    }
}
