package com.phoenixforge.student

import android.app.Application
import com.phoenixforge.student.domain.bootstrap.StudentWorldBootstrap
import com.phoenixforge.student.domain.engine.LifeEventCollector
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class StudentApp : Application() {

    @Inject lateinit var worldBootstrap: StudentWorldBootstrap
    @Inject lateinit var lifeEventCollector: LifeEventCollector

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override fun onCreate() {
        super.onCreate()
        appScope.launch {
            worldBootstrap.ensureWorldInitialized()
            lifeEventCollector.onAppOpened()
        }
    }
}
