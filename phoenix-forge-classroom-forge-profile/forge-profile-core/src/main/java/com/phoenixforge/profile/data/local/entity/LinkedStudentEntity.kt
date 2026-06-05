package com.phoenixforge.profile.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "linked_students")
data class LinkedStudentEntity(
    @PrimaryKey val profileUid: String,
    val displayName: String,
    val linkedAt: Long,
    val notes: String?
)
