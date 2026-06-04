package com.phoenixforge.profile.data.serialization

import com.phoenixforge.profile.data.local.ChildhoodSnapshotPayload
import com.phoenixforge.profile.domain.model.TimelineMetadata
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Singleton
class ForgeProfileJson {

    val json: Json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    private val metadataListSerializer = ListSerializer(TimelineMetadata.serializer())
    private val legacyMetadataSerializer = MapSerializer(String.serializer(), String.serializer())

    fun encodeTimelineMetadata(metadata: List<TimelineMetadata>): String =
        json.encodeToString(metadataListSerializer, metadata)

    fun decodeTimelineMetadata(raw: String): List<TimelineMetadata> {
        if (raw.isBlank()) return emptyList()
        return runCatching { json.decodeFromString(metadataListSerializer, raw) }
            .recoverCatching { decodeLegacyTimelineMetadata(raw) }
            .getOrDefault(emptyList())
    }

    private fun decodeLegacyTimelineMetadata(raw: String): List<TimelineMetadata> {
        val map = json.decodeFromString(legacyMetadataSerializer, raw)
        return map.map { (category, value) ->
            TimelineMetadata(source = "forge_profile", category = category, value = value)
        }
    }

    fun encodeChildhoodSnapshot(payload: ChildhoodSnapshotPayload): String =
        json.encodeToString(ChildhoodSnapshotPayload.serializer(), payload)

    fun decodeChildhoodSnapshot(raw: String): ChildhoodSnapshotPayload? =
        runCatching { json.decodeFromString(ChildhoodSnapshotPayload.serializer(), raw) }.getOrNull()
}
