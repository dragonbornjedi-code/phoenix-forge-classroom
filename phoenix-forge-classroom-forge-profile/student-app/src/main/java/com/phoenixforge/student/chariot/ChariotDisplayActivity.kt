package com.phoenixforge.student.chariot

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.phoenixforge.student.ui.theme.StudentHouseTheme

/**
 * Full-screen Chariot display for the Kia Soul head unit (no bottom nav).
 * Replaces legacy car-display/listening.html + celebration.html.
 *
 * Deep links:
 * - phoenixforge://chariot/listening
 * - phoenixforge://chariot/celebration?xp=25&quest=Secret+Label
 * - phoenixforge://chariot/deck
 * - phoenixforge://chariot/welcome?message=Car+quest+time
 */
class ChariotDisplayActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        render(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        render(intent)
    }

    private fun render(intent: Intent) {
        val args = ChariotDeepLink.parse(intent.data)
            ?: ChariotDisplayArgs(mode = ChariotMode.DECK)

        setContent {
            StudentHouseTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    var celebrateMission by remember { mutableStateOf<ChariotMission?>(null) }
                    val stack = remember { ChariotStackLoader.load() }

                    when {
                        celebrateMission != null -> {
                            ChariotCelebrationScreen(
                                xp = celebrateMission!!.xpReward,
                                questTitle = celebrateMission!!.title,
                            )
                        }
                        args.mode == ChariotMode.LISTENING -> ChariotListeningScreen()
                        args.mode == ChariotMode.CELEBRATION -> {
                            ChariotCelebrationScreen(
                                xp = args.xp,
                                questTitle = args.questTitle,
                            )
                        }
                        args.mode == ChariotMode.WELCOME -> {
                            ChariotWelcomeScreen(
                                message = args.message.ifBlank { stack.welcomeLine },
                            )
                        }
                        else -> {
                            ChariotDeckScreen(stack = stack) { mission ->
                                celebrateMission = mission
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        fun listeningIntent(): Intent =
            Intent(Intent.ACTION_VIEW, Uri.parse("phoenixforge://chariot/listening"))

        fun celebrationIntent(xp: Int, quest: String): Intent =
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("phoenixforge://chariot/celebration?xp=$xp&quest=${Uri.encode(quest)}"),
            )
    }
}
