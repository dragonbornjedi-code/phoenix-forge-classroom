package com.phoenixforge.profile.domain.bootstrap

import com.phoenixforge.profile.domain.model.ForgeProfile
import com.phoenixforge.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileBootstrap @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend fun ensureProfileExists() {
        if (repository.getProfile().firstOrNull() == null) {
            repository.updateProfile(defaultEzraProfile())
        }
    }

    private fun defaultEzraProfile(): ForgeProfile =
        ForgeProfile(
            uid = UUID.randomUUID().toString(),
            forgeName = DEFAULT_FORGE_NAME,
            realName = null,
            birthDate = null,
            pronouns = DEFAULT_PRONOUNS,
            favoriteColor = null,
            currentTitle = null,
            currentStage = DEFAULT_STAGE,
            sparkMaturationTier = DEFAULT_SPARK_TIER
        )

    private companion object {
        const val DEFAULT_FORGE_NAME = "Ezra"
        const val DEFAULT_PRONOUNS = "he/him"
        const val DEFAULT_STAGE = "EARLY_DISCOVERY"
        const val DEFAULT_SPARK_TIER = 1
    }
}
