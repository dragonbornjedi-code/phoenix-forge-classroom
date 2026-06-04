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
            repository.updateProfile(
                ForgeProfile(
                    uid = UUID.randomUUID().toString(),
                    forgeName = "New Forger",
                    realName = null,
                    birthDate = null,
                    pronouns = null,
                    favoriteColor = null,
                    currentTitle = null,
                    currentStage = "EARLY_DISCOVERY",
                    sparkMaturationTier = 1
                )
            )
        }
    }
}
