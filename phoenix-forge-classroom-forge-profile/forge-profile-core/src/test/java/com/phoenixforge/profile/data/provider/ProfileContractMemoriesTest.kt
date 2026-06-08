package com.phoenixforge.profile.data.provider

import org.junit.Assert.assertEquals
import org.junit.Test

class ProfileContractMemoriesTest {

    @Test
    fun memoriesProjectionIncludesCrossAppColumns() {
        val expected = listOf(
            "memory_id",
            "memory_type",
            "memory_category",
            "memory_source",
            "captured_at",
            "note",
            "checksum",
            "synced_to_student",
            "content_uri",
        )
        assertEquals(expected, ProfileContract.MemoriesProjection.COLUMNS.toList())
    }
}
