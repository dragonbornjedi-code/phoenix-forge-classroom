package com.phoenixforge.student.data.serialization

import com.phoenixforge.student.domain.model.NpcMemoryAnchor
import com.phoenixforge.student.domain.model.NpcMemoryNode
import com.phoenixforge.student.domain.model.InventoryItem
import com.phoenixforge.student.domain.model.WorldDriftState
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudentJson @Inject constructor() {
    val json: Json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    private val stringListSerializer = ListSerializer(String.serializer())
    private val intMapSerializer = MapSerializer(String.serializer(), Int.serializer())
    private val inventorySerializer = ListSerializer(InventoryItem.serializer())
    private val memoryGraphSerializer = ListSerializer(NpcMemoryNode.serializer())
    private val anchorSerializer = ListSerializer(NpcMemoryAnchor.serializer())

    fun encodeStringSet(values: Set<String>): String =
        json.encodeToString(stringListSerializer, values.toList())

    fun decodeStringSet(raw: String): Set<String> {
        if (raw.isBlank()) return emptySet()
        return runCatching { json.decodeFromString(stringListSerializer, raw).toSet() }
            .getOrDefault(emptySet())
    }

    fun encodeStringList(values: List<String>): String =
        json.encodeToString(stringListSerializer, values)

    fun decodeStringList(raw: String): List<String> {
        if (raw.isBlank()) return emptyList()
        return runCatching { json.decodeFromString(stringListSerializer, raw) }
            .getOrDefault(emptyList())
    }

    fun encodeMemoryGraph(nodes: List<NpcMemoryNode>): String =
        json.encodeToString(memoryGraphSerializer, nodes)

    fun decodeMemoryGraph(raw: String): List<NpcMemoryNode> {
        if (raw.isBlank()) return emptyList()
        return runCatching { json.decodeFromString(memoryGraphSerializer, raw) }
            .getOrDefault(emptyList())
    }

    fun encodeAnchors(anchors: List<NpcMemoryAnchor>): String =
        json.encodeToString(anchorSerializer, anchors)

    fun decodeAnchors(raw: String): List<NpcMemoryAnchor> {
        if (raw.isBlank()) return emptyList()
        return runCatching { json.decodeFromString(anchorSerializer, raw) }
            .getOrDefault(emptyList())
    }

    fun encodeDrift(drift: WorldDriftState): String =
        json.encodeToString(WorldDriftState.serializer(), drift)

    fun decodeDrift(raw: String): WorldDriftState? =
        if (raw.isBlank()) null
        else runCatching { json.decodeFromString(WorldDriftState.serializer(), raw) }.getOrNull()

    fun encodeCurrency(values: Map<String, Int>): String =
        json.encodeToString(intMapSerializer, values)

    fun decodeCurrency(raw: String): Map<String, Int> {
        if (raw.isBlank()) return emptyMap()
        return runCatching { json.decodeFromString(intMapSerializer, raw) }.getOrDefault(emptyMap())
    }

    fun encodeInventory(items: List<InventoryItem>): String =
        json.encodeToString(inventorySerializer, items)

    fun decodeInventory(raw: String): List<InventoryItem> {
        if (raw.isBlank()) return emptyList()
        return runCatching { json.decodeFromString(inventorySerializer, raw) }.getOrDefault(emptyList())
    }
}
