package com.phoenixforge.profile.data.memory

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.InputStream
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Copies inbound media into app-private storage so memories survive gallery churn.
 * Master steps 0.63–0.65.
 */
@Singleton
class MemoryArtifactStorage @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun memoriesDir(): File = File(context.filesDir, "memories").apply { mkdirs() }

    fun photoFile(artifactId: String): File = File(memoriesDir(), "$artifactId.jpg")

    fun audioFile(artifactId: String): File = File(memoriesDir(), "$artifactId.m4a")

    fun cameraCaptureUri(artifactId: String): Uri =
        androidx.core.content.FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            photoFile(artifactId),
        )

    fun copyFromUri(sourceUri: Uri, artifactId: String, extension: String): String {
        val dest = File(memoriesDir(), "$artifactId.$extension")
        context.contentResolver.openInputStream(sourceUri)?.use { input ->
            dest.outputStream().use { output -> input.copyTo(output) }
        } ?: error("Could not read $sourceUri")
        return dest.absolutePath
    }

    fun sha256(path: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        File(path).inputStream().use { input -> digestBytes(digest, input) }
        return digest.digest().joinToString("") { "%02x".format(it) }
    }

    private fun digestBytes(digest: MessageDigest, input: InputStream) {
        val buffer = ByteArray(8_192)
        var read = input.read(buffer)
        while (read > 0) {
            digest.update(buffer, 0, read)
            read = input.read(buffer)
        }
    }
}
