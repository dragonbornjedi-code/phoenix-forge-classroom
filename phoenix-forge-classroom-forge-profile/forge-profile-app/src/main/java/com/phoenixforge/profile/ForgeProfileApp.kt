package com.phoenixforge.profile

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.phoenixforge.profile.data.projection.ForgeProfileProjectionCoordinator
import com.phoenixforge.profile.data.sync.EventIngestCoordinator
import com.phoenixforge.profile.data.sync.MessageIngestCoordinator
import com.phoenixforge.profile.domain.bootstrap.LinkedStudentAutoLinker
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@HiltAndroidApp
class ForgeProfileApp : Application() {

    @Inject lateinit var eventIngestCoordinator: EventIngestCoordinator
    @Inject lateinit var messageIngestCoordinator: MessageIngestCoordinator
    @Inject lateinit var projectionCoordinator: ForgeProfileProjectionCoordinator
    @Inject lateinit var linkedStudentAutoLinker: LinkedStudentAutoLinker

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        eventIngestCoordinator.start()
        messageIngestCoordinator.start()
        projectionCoordinator.start()
        appScope.launch { linkedStudentAutoLinker.linkAllChildrenOnDevice() }
        ProcessLifecycleOwner.get().lifecycle.addObserver(
            object : DefaultLifecycleObserver {
                override fun onStart(owner: LifecycleOwner) {
                    eventIngestCoordinator.rescanAll()
                    messageIngestCoordinator.rescanAll()
                    appScope.launch { linkedStudentAutoLinker.linkAllChildrenOnDevice() }
                }
            },
        )
    }
}
