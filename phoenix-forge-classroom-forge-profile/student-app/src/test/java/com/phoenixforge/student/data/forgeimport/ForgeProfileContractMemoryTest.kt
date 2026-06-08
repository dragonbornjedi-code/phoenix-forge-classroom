package com.phoenixforge.student.data.forgeimport

import org.junit.Assert.assertEquals
import org.junit.Test

class ForgeProfileContractMemoryTest {

    @Test
    fun memoryColumnsMirrorProfileProvider() {
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
        val actual = listOf(
            ForgeProfileContract.Columns.MEMORY_ID,
            ForgeProfileContract.Columns.MEMORY_TYPE,
            ForgeProfileContract.Columns.MEMORY_CATEGORY,
            ForgeProfileContract.Columns.MEMORY_SOURCE,
            ForgeProfileContract.Columns.MEMORY_CAPTURED_AT,
            ForgeProfileContract.Columns.MEMORY_NOTE,
            ForgeProfileContract.Columns.MEMORY_CHECKSUM,
            ForgeProfileContract.Columns.MEMORY_SYNCED_TO_STUDENT,
            ForgeProfileContract.Columns.MEMORY_CONTENT_URI,
        )
        assertEquals(expected, actual)
    }
}
