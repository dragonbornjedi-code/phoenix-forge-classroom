package com.phoenixforge.student.ui.importprofile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.phoenixforge.student.domain.avatar.ImportedHeroLookParser
import com.phoenixforge.student.ui.avatar.HearthWelcomeCard
import com.phoenixforge.student.ui.components.StudentHearthBackground
import com.phoenixforge.student.ui.components.StudentPrimaryButton
import com.phoenixforge.student.ui.components.StudentSecondaryButton
import com.phoenixforge.student.ui.theme.StudentKidCopy

@Composable
fun ImportForgeProfileScreen(viewModel: ImportForgeProfileViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val preview = state.preview

    StudentHearthBackground {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item {
                Text("✨", fontSize = 48.sp)
                Text(StudentKidCopy.importTitle(), style = MaterialTheme.typography.headlineLarge)
                Text(
                    StudentKidCopy.importSubtitle(),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    ),
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        if (preview == null) {
                            Text(StudentKidCopy.importChecking(), style = MaterialTheme.typography.bodyLarge)
                        } else if (!preview.isAvailable) {
                            Text(
                                preview.errorMessage ?: StudentKidCopy.importUnavailable(),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.error,
                            )
                        } else {
                            Text(
                                StudentKidCopy.importFound(preview.forgeName),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                            )
                            ImportedHeroLookParser.parse(preview.avatarSummary)?.let { look ->
                                HearthWelcomeCard(
                                    forgeName = preview.forgeName,
                                    heroLook = look,
                                    modifier = Modifier.padding(top = 4.dp),
                                )
                            }
                            StudentPrimaryButton(
                                text = StudentKidCopy.importSyncButton(state.isImporting),
                                onClick = viewModel::importSelectedProfile,
                                enabled = !state.isImporting,
                            )
                        }
                        StudentSecondaryButton(
                            text = StudentKidCopy.importRefresh(),
                            onClick = viewModel::refreshPreview,
                        )
                    }
                }
            }

            state.lastReward?.let { reward ->
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                    ) {
                        Text(
                            reward.story.narrative,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(18.dp),
                        )
                    }
                }
            }
        }
    }
}
