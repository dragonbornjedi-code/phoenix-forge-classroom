package com.phoenixforge.profile.domain.model

import java.time.Instant

data class TeacherMetadata(
    val id: String,
    val profileUid: String,
    val key: String,
    val value: String,
    val category: String,
    val timestamp: Instant
)
