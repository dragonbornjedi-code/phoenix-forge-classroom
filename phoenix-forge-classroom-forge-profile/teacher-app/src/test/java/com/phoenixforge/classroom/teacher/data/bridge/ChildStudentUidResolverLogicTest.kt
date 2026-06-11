package com.phoenixforge.classroom.teacher.data.bridge

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ChildStudentUidResolverLogicTest {

    @Test
    fun prefersLinkedStudentOverChildProfileFallback() {
        val resolved = resolveFromCandidates(
            childProfiles = listOf(ChildCandidate("child-uuid", "Ezra")),
            linkedStudents = listOf(ChildCandidate("linked-uuid", "Ezra linked")),
            studentImportUid = "student-uuid",
            rememberedUid = "remembered-uuid",
        )
        assertEquals("linked-uuid", resolved?.studentUid)
        assertEquals(ChildUidSource.FORGE_PROFILE_LINKED, resolved?.source)
    }

    @Test
    fun fallsBackToLinkedWhenNoChildProfile() {
        val resolved = resolveFromCandidates(
            childProfiles = emptyList(),
            linkedStudents = listOf(ChildCandidate("linked-uuid", "Ezra linked")),
            studentImportUid = "student-uuid",
            rememberedUid = null,
        )
        assertEquals("linked-uuid", resolved?.studentUid)
        assertEquals(ChildUidSource.FORGE_PROFILE_LINKED, resolved?.source)
    }

    @Test
    fun returnsNullWhenNoCandidates() {
        assertNull(
            resolveFromCandidates(
                childProfiles = emptyList(),
                linkedStudents = emptyList(),
                studentImportUid = null,
                rememberedUid = null,
            ),
        )
    }

    @Test
    fun ignoresRememberedPushUid() {
        assertNull(
            resolveFromCandidates(
                childProfiles = emptyList(),
                linkedStudents = emptyList(),
                studentImportUid = null,
                rememberedUid = "remembered-uuid",
            ),
        )
    }

    private data class ChildCandidate(val uid: String, val name: String)

    private fun resolveFromCandidates(
        childProfiles: List<ChildCandidate>,
        linkedStudents: List<ChildCandidate>,
        studentImportUid: String?,
        rememberedUid: String?,
    ): ResolvedChildUid? {
        linkedStudents.firstOrNull { it.uid.isNotBlank() }?.let { linked ->
            return ResolvedChildUid(linked.uid, linked.name, ChildUidSource.FORGE_PROFILE_LINKED)
        }
        childProfiles.firstOrNull { it.uid.isNotBlank() }?.let { child ->
            return ResolvedChildUid(child.uid, child.name, ChildUidSource.FORGE_PROFILE_CHILD)
        }
        studentImportUid?.trim()?.takeIf { it.isNotEmpty() }?.let { uid ->
            return ResolvedChildUid(uid, "Student import", ChildUidSource.STUDENT_EDITION)
        }
        return null
    }
}
