package com.phoenixforge.profile.data.local

import kotlinx.serialization.Serializable

@Serializable
data class ChildhoodSnapshotPayload(
    val forgeName: String,
    val currentTitle: String?,
    val currentStage: String,
    val aboutMe: List<AboutMeSnapshot>,
    val favorites: List<FavoriteSnapshot>,
    val dreams: List<DreamSnapshot>,
    val avatar: AvatarSnapshot?
)

@Serializable
data class AboutMeSnapshot(val prompt: String, val answer: String)

@Serializable
data class FavoriteSnapshot(val category: String, val item: String)

@Serializable
data class DreamSnapshot(val type: String, val content: String)

@Serializable
data class AvatarSnapshot(
    val hairType: String,
    val eyeColor: String,
    val skinTone: String,
    val clothingId: String,
    val version: Int,
    val shardLevel: Int = 0,
)
