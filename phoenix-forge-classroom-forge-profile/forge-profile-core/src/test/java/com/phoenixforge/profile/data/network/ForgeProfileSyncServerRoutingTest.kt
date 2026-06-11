package com.phoenixforge.profile.data.network

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ForgeProfileSyncServerRoutingTest {

    @Test
    fun studentUidPattern_acceptsUuid() {
        assertTrue(
            STUDENT_UID_PATTERN.matches("f4b1376b-9afc-459d-b84d-8b69116597ed"),
        )
    }

    @Test
    fun studentUidPattern_rejectsBlankOrShort() {
        assertFalse(STUDENT_UID_PATTERN.matches(""))
        assertFalse(STUDENT_UID_PATTERN.matches("not-a-uuid"))
    }

    private companion object {
        val STUDENT_UID_PATTERN = Regex("^[0-9a-fA-F-]{36}$")
    }
}
