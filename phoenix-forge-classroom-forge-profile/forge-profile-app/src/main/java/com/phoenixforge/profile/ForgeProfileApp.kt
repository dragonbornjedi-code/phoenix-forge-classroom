package com.phoenixforge.profile

import android.app.Application
import com.phoenixforge.profile.domain.bootstrap.ProfileBootstrap
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class ForgeProfileApp : Application() {

    @Inject lateinit var profileBootstrap: ProfileBootstrap

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override fun onCreate() {
        super.onCreate()
        appScope.launch {
            profileBootstrap.ensureProfileExists()
        }
    }
}
