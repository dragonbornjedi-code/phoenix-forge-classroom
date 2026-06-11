package com.phoenixforge.profile.ui.gate

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import com.phoenixforge.profile.ui.components.ForgePrimaryButton
import com.phoenixforge.profile.ui.components.ForgeSecondaryButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.phoenixforge.profile.domain.copy.AppBoundaryCopy
import com.phoenixforge.profile.domain.model.ForgeProfile
import com.phoenixforge.profile.domain.model.ProfileRole

private val defaultCreatableRoles = listOf(
    ProfileRole.STEWARD_FOR_STUDENT,
    ProfileRole.STUDENT_SELF,
)

@Composable
fun ProfileSignInScreen(
    existingProfile: ForgeProfile?,
    savedProfiles: List<ForgeProfile>,
    showCreateForm: Boolean,
    creatableRoles: List<ProfileRole> = defaultCreatableRoles,
    onContinueExisting: (rememberDevice: Boolean) -> Unit,
    onCreateProfile: (
        forgeName: String,
        ageYears: Int?,
        profileRole: ProfileRole,
        rememberDevice: Boolean
    ) -> Unit,
    onSwitchProfile: (profileUid: String, rememberDevice: Boolean) -> Unit,
    onUseDifferentProfile: () -> Unit,
    onShowCreateForm: () -> Unit,
    onEraseAllLocalData: () -> Unit
) {
    var forgeName by rememberSaveable { mutableStateOf("") }
    var ageText by rememberSaveable { mutableStateOf("") }
    var profileRole by rememberSaveable {
        mutableStateOf(creatableRoles.firstOrNull() ?: ProfileRole.STUDENT_SELF)
    }
    var rememberDevice by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Forge Profile", style = MaterialTheme.typography.headlineLarge)
        Text(
            "Choose or create the profile for this device. You bring the story — nothing is assumed.",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            AppBoundaryCopy.FORGE_PROFILE_OWNS,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            "Teacher planning lives in Teacher Edition (separate app). Forge Profile is identity and childhood record only.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        if (existingProfile != null) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Profile on this device", style = MaterialTheme.typography.titleMedium)
                    Text(existingProfile.forgeName, style = MaterialTheme.typography.headlineSmall)
                    profileRoleLabel(existingProfile.profileRole)?.let {
                        Text(it, style = MaterialTheme.typography.bodySmall)
                    }
                    RememberDeviceRow(
                        checked = rememberDevice,
                        onCheckedChange = { rememberDevice = it }
                    )
                    ForgePrimaryButton(
                        text = "Continue as ${existingProfile.forgeName}",
                        onClick = { onContinueExisting(rememberDevice) },
                    )
                    ForgeSecondaryButton(
                        text = "Switch profile",
                        onClick = onUseDifferentProfile,
                    )
                }
            }
        } else if (savedProfiles.isNotEmpty()) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Profiles on this device", style = MaterialTheme.typography.titleMedium)
                    RememberDeviceRow(
                        checked = rememberDevice,
                        onCheckedChange = { rememberDevice = it }
                    )
                    savedProfiles.forEach { profile ->
                        ForgePrimaryButton(
                            text = profile.forgeName + (profileRoleLabel(profile.profileRole)?.let { " · $it" } ?: ""),
                            onClick = { onSwitchProfile(profile.uid, rememberDevice) },
                        )
                    }
                    ForgeSecondaryButton(
                        text = "Create another profile",
                        onClick = onShowCreateForm,
                    )
                }
            }
        }

        if (showCreateForm || savedProfiles.isEmpty()) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Create a profile", style = MaterialTheme.typography.titleMedium)

                    OutlinedTextField(
                        value = forgeName,
                        onValueChange = { forgeName = it },
                        label = { Text("Name") },
                        placeholder = { Text("Your forge name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = ageText,
                        onValueChange = { ageText = it.filter { ch -> ch.isDigit() }.take(3) },
                        label = { Text("Age") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    Text("Profile tier", style = MaterialTheme.typography.labelLarge)
                    if (creatableRoles.size == 1) {
                        Text(
                            creatableRoles.single().label,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        Text(
                            AppBoundaryCopy.signInBoundaryHint(creatableRoles.single()),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    } else {
                        creatableRoles.forEach { role ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = profileRole == role,
                                    onClick = { profileRole = role },
                                    role = Role.RadioButton
                                )
                                .padding(vertical = 4.dp),
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = profileRole == role,
                                    onClick = null
                                )
                                Text(role.label, modifier = Modifier.padding(start = 8.dp))
                            }
                            if (profileRole == role) {
                                Text(
                                    AppBoundaryCopy.signInBoundaryHint(role),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(start = 48.dp, top = 2.dp),
                                )
                            }
                        }
                    }
                    }

                    RememberDeviceRow(
                        checked = rememberDevice,
                        onCheckedChange = { rememberDevice = it }
                    )

                    ForgePrimaryButton(
                        text = "Create & continue",
                        onClick = {
                            onCreateProfile(
                                forgeName,
                                ageText.toIntOrNull(),
                                profileRole,
                                rememberDevice
                            )
                        },
                        enabled = forgeName.isNotBlank() && ageText.isNotBlank(),
                    )
                }
            }
        }

        if (savedProfiles.isNotEmpty()) {
            ForgeSecondaryButton(
                text = "Erase all local Forge Profile data",
                onClick = onEraseAllLocalData,
            )
            Text(
                "Switch profile keeps every saved profile. Erase all is permanent on this device.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Keep me signed in is for devices only one person uses. Uncheck on shared family tablets.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun profileRoleLabel(roleKey: String?): String? = when (roleKey) {
    ProfileRole.STUDENT_SELF.storageKey -> "Child profile"
    ProfileRole.STEWARD_FOR_STUDENT.storageKey -> "Adult profile"
    ProfileRole.TEACHER_SELF.storageKey -> "Teacher profile (legacy)"
    else -> null
}

@Composable
private fun RememberDeviceRow(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Text(
            "Keep me signed in on this device",
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
