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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import com.phoenixforge.profile.domain.model.ForgeProfile
import com.phoenixforge.profile.domain.model.ProfileRole

@Composable
fun ProfileSignInScreen(
    existingProfile: ForgeProfile?,
    onContinueExisting: (rememberDevice: Boolean) -> Unit,
    onCreateProfile: (
        forgeName: String,
        ageYears: Int?,
        profileRole: ProfileRole,
        rememberDevice: Boolean
    ) -> Unit,
    onUseDifferentProfile: () -> Unit
) {
    var forgeName by rememberSaveable { mutableStateOf("") }
    var ageText by rememberSaveable { mutableStateOf("") }
    var profileRole by rememberSaveable { mutableStateOf(ProfileRole.STUDENT_SELF) }
    var rememberDevice by rememberSaveable { mutableStateOf(true) }

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

        if (existingProfile != null) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Profile on this device", style = MaterialTheme.typography.titleMedium)
                    Text(existingProfile.forgeName, style = MaterialTheme.typography.headlineSmall)
                    RememberDeviceRow(
                        checked = rememberDevice,
                        onCheckedChange = { rememberDevice = it }
                    )
                    Button(
                        onClick = { onContinueExisting(rememberDevice) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Continue as ${existingProfile.forgeName}")
                    }
                    OutlinedButton(
                        onClick = onUseDifferentProfile,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Use a different profile")
                    }
                }
            }
        } else {
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

                    Text("Who is this profile for?", style = MaterialTheme.typography.labelLarge)
                    ProfileRole.entries.forEach { role ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = profileRole == role,
                                    onClick = { profileRole = role },
                                    role = Role.RadioButton
                                )
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = profileRole == role,
                                onClick = null
                            )
                            Text(role.label, modifier = Modifier.padding(start = 8.dp))
                        }
                    }

                    RememberDeviceRow(
                        checked = rememberDevice,
                        onCheckedChange = { rememberDevice = it }
                    )

                    val ageRequired = profileRole != ProfileRole.TEACHER_SELF
                    Button(
                        onClick = {
                            onCreateProfile(
                                forgeName,
                                ageText.toIntOrNull(),
                                profileRole,
                                rememberDevice
                            )
                        },
                        enabled = forgeName.isNotBlank() && (!ageRequired || ageText.isNotBlank()),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Create & continue")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Keep me signed in is for devices only one person uses. Uncheck on shared family tablets.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
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
