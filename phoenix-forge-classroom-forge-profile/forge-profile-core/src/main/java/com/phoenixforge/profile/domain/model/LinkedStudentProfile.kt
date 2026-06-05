package com.phoenixforge.profile.domain.model

import java.time.Instant

data class LinkedStudentProfile(
    val profileUid: String,
    val displayName: String,
    val linkedAt: Instant,
    val notes: String?
)
