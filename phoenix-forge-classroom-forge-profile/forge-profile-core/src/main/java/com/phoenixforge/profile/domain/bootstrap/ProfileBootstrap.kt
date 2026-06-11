package com.phoenixforge.profile.domain.bootstrap

import com.phoenixforge.profile.domain.avatar.AvatarHeroCatalog
import com.phoenixforge.profile.domain.model.ForgeProfile
import com.phoenixforge.profile.domain.model.ProfileRole
import com.phoenixforge.profile.domain.repository.ProfileRepository
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.firstOrNull

@Singleton
class ProfileBootstrap @Inject constructor(
    private val repository: ProfileRepository
) {
    /**
     * Creates a blank profile from steward/learner input — no seeded names or facts.
     */
    suspend fun createBlankProfile(
        forgeName: String,
        ageYears: Int?,
        profileRole: ProfileRole
    ): ForgeProfile {
        val profile = ForgeProfile(
            uid = UUID.randomUUID().toString(),
            forgeName = forgeName.trim(),
            realName = null,
            birthDate = null,
            ageYears = ageYears,
            pronouns = null,
            favoriteColor = null,
            currentTitle = null,
            currentStage = STAGE_JUST_BEGINNING,
            sparkMaturationTier = 0,
            profileRole = profileRole.storageKey
        )
        repository.updateProfile(profile)
        repository.switchActiveProfile(profile.uid)
        if (profileRole.isStudentProfile) {
            repository.saveAvatar(AvatarHeroCatalog.defaultAvatar())
            autoLinkChildToAdultOnDevice(profile)
        }
        return profile
    }

    private suspend fun autoLinkChildToAdultOnDevice(child: ForgeProfile) {
        val hasAdult = repository.listProfiles().firstOrNull().orEmpty()
            .any { ProfileRole.fromStorageKey(it.profileRole)?.isAdultProfile == true }
        if (!hasAdult) return
        val alreadyLinked = repository.getLinkedStudents().firstOrNull().orEmpty()
            .any { it.profileUid == child.uid }
        if (alreadyLinked) return
        repository.linkStudentProfile(
            displayName = child.forgeName,
            profileUid = child.uid,
            notes = "Auto-linked — child profile on this device",
        )
    }

    suspend fun resetAllProfileData() {
        repository.clearAllData()
    }

    companion object {
        const val STAGE_JUST_BEGINNING = "JUST_BEGINNING"
    }
}
