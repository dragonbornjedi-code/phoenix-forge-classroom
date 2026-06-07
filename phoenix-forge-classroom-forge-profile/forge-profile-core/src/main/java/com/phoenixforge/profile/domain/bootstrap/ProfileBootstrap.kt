package com.phoenixforge.profile.domain.bootstrap

import com.phoenixforge.profile.domain.model.ForgeProfile
import com.phoenixforge.profile.domain.model.ProfileRole
import com.phoenixforge.profile.domain.avatar.AvatarHeroCatalog
import com.phoenixforge.profile.domain.repository.ProfileRepository
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

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
        if (profileRole.isStudentProfile) {
            repository.saveAvatar(AvatarHeroCatalog.defaultAvatar())
        }
        return profile
    }

    suspend fun resetAllProfileData() {
        repository.clearAllData()
    }

    companion object {
        const val STAGE_JUST_BEGINNING = "JUST_BEGINNING"
    }
}
