package com.phoenixforge.student.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.phoenixforge.student.ui.navigation.StudentRoutes
import com.phoenixforge.student.ui.theme.StudentKidCopy
import com.phoenixforge.student.ui.theme.StudentSparkGold

@Composable
fun HomeScreen(
    onNavigate: (String) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.worldState.collectAsState()
    val heroLook = ImportedHeroLookParser.parse(state.importedHeroSummary)

    StudentHearthBackground {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item {
                state.importedForgeName?.let { forgeName ->
                    HearthWelcomeCard(
                        forgeName = forgeName,
                        heroLook = heroLook,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        StudentKidCopy.levelLine(state.progress.level, state.progress.streakDays),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        StudentKidCopy.starsLine(state.progress.xp),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                } ?: run {
                    Text(
                        StudentKidCopy.homeNoProfileTitle(),
                        style = MaterialTheme.typography.headlineLarge,
                    )
                    Text("🏠", fontSize = 56.sp, modifier = Modifier.padding(vertical = 8.dp))
                    Text(
                        StudentKidCopy.homeNoProfileBody(),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }

            item {
                StudentPrimaryButton(
                    text = "Visit my house 🏡",
                    onClick = { onNavigate(StudentRoutes.DIGITAL_HOME_HUB) },
                )
            }

            item {
                StudentPrimaryButton(
                    text = "Open my messages 💌",
                    onClick = { onNavigate(StudentRoutes.INBOX) },
                )
            }

            state.latestStory?.let { story ->
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        ),
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text(
                                StudentKidCopy.storyTitle(),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                StudentKidCopy.sparkTaleLine(story.narrative, story.npcSpeaker),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(top = 6.dp),
                            )
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = StudentSparkGold.copy(alpha = 0.35f),
                    ),
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            StudentKidCopy.companionTitle(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                        )
                        val companion = state.activeCompanion
                        Text(
                            companion?.name ?: "Spark ✨",
                            style = MaterialTheme.typography.headlineSmall,
                        )
                        Text(
                            companion?.lastReaction?.let { reaction ->
                                StudentKidCopy.sparkTaleLine(reaction, companion.name)
                            } ?: StudentKidCopy.companionEmpty(),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(top = 6.dp),
                        )
                    }
                }
            }
        }
    }
}
