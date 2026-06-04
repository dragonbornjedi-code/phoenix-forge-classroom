package com.phoenixforge.profile.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TimelineMetadata(
    val source: String,
    val category: String,
    val value: String? = null
)
