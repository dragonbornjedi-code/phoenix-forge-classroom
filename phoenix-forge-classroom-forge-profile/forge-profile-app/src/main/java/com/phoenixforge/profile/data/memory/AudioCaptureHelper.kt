package com.phoenixforge.profile.data.memory

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/** Minimal voice-memo capture for Memory Capsule (master step 0.64). */
@Singleton
class AudioCaptureHelper @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    private var recorder: MediaRecorder? = null

    fun start(outputPath: String) {
        stop()
        recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(outputPath)
            prepare()
            start()
        }
    }

    fun stop(): Boolean = runCatching {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
        true
    }.getOrDefault(false)
}
