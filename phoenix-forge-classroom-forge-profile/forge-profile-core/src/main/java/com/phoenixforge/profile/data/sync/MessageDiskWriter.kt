package com.phoenixforge.profile.data.sync

import com.phoenixforge.profile.data.serialization.ForgeProfileJson
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageDiskWriter @Inject constructor(
    private val forgeProfileJson: ForgeProfileJson,
) {
    fun write(record: ForgeMessageRecord): List<String> {
        val payload = forgeProfileJson.json.encodeToString(ForgeMessageRecord.serializer(), record)
        val fileName = SyncPaths.messageFileName(record.messageId)
        val written = mutableListOf<String>()
        SyncPaths.resolveMessagesDirs(record.toStudentUid).forEach { dir ->
            runCatching {
                dir.mkdirs()
                val file = File(dir, fileName)
                file.writeText(payload)
                written += file.absolutePath
            }
        }
        return written
    }
}
