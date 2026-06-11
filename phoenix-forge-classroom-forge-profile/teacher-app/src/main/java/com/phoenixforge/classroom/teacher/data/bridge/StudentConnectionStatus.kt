package com.phoenixforge.classroom.teacher.data.bridge

data class StudentConnectionStatus(
    val headline: String,
    val detail: String,
    val isTrustworthy: Boolean,
    val setupSteps: List<String> = emptyList(),
)

fun buildStudentConnectionStatus(
    resolved: ResolvedChildUid?,
    linkedStudentCount: Int,
    staleRememberedUid: String?,
    lanPeerCount: Int,
    lanEventCount: Int?,
): StudentConnectionStatus {
  if (resolved == null) {
    val staleNote = staleRememberedUid?.let { uid ->
      " Old push cache ($uid) is ignored — link Ezra's real profile ID."
    }.orEmpty()
    return StudentConnectionStatus(
      headline = "Not connected",
      detail = "Teacher only sees a student after you link Ezra's profile ID in Forge Profile on this phone.$staleNote",
      isTrustworthy = false,
      setupSteps = listOf(
        "On Ezra's tablet: Forge Profile → open his child profile → copy Profile ID",
        "On your phone: Forge Profile → Students → Link a student profile → paste ID",
        "Return here and pull to refresh",
        "Same Wi‑Fi: events can also flow live when his tablet Forge Profile is open",
      ),
    )
  }

  return when (resolved.source) {
    ChildUidSource.FORGE_PROFILE_LINKED -> {
      val lan = when {
        (lanEventCount ?: 0) > 0 -> " Live events from ${lanEventCount} LAN source(s)."
        lanPeerCount > 0 -> " ${lanPeerCount} Forge Profile device(s) on Wi‑Fi — open his tablet app for live events."
        else -> " Manifests write to this phone's sync folder until Syncthing delivers them to his tablet."
      }
      StudentConnectionStatus(
        headline = "Linked to ${resolved.displayName}",
        detail = "Profile ID ${resolved.studentUid} is registered in your adult Forge Profile.$lan",
        isTrustworthy = true,
      )
    }
    ChildUidSource.FORGE_PROFILE_CHILD -> StudentConnectionStatus(
      headline = "Local phone child only",
      detail = "This child profile lives on your phone (${resolved.studentUid}). It is not Ezra's tablet unless you recreated him here on purpose.",
      isTrustworthy = false,
      setupSteps = listOf(
        "For Ezra's tablet: copy his Profile ID from his tablet Forge Profile",
        "Forge Profile → Students → Link a student profile",
      ),
    )
    ChildUidSource.STUDENT_EDITION -> StudentConnectionStatus(
      headline = "Local Student import only",
      detail = "Student Edition on this phone imported ${resolved.studentUid}. That does not connect to his tablet.",
      isTrustworthy = false,
      setupSteps = listOf(
        "Link his tablet Profile ID in Forge Profile → Students",
      ),
    )
    ChildUidSource.REMEMBERED_PUSH -> StudentConnectionStatus(
      headline = "Stale cache only",
      detail = "An old manifest UID was remembered but is not a real link.",
      isTrustworthy = false,
      setupSteps = listOf(
        "Tap Clear stale cache, then link Ezra in Forge Profile → Students",
      ),
    )
  }
}
