package com.phoenixforge.profile.ui.gate

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.phoenixforge.profile.ui.navigation.ProfileBottomBar
import com.phoenixforge.profile.ui.navigation.ProfileNavHost

@Composable
fun ProfileAppShell(
    gateViewModel: ProfileGateViewModel = hiltViewModel()
) {
    val gateState by gateViewModel.state.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when (gateState.phase) {
            ProfileGatePhase.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Loading…", style = MaterialTheme.typography.bodyLarge)
                }
            }
            ProfileGatePhase.SignIn -> {
                ProfileSignInScreen(
                    existingProfile = gateState.existingProfile,
                    onContinueExisting = gateViewModel::continueWithExistingProfile,
                    onCreateProfile = gateViewModel::createProfile,
                    onUseDifferentProfile = gateViewModel::useDifferentProfile
                )
            }
            ProfileGatePhase.Ready -> {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = { ProfileBottomBar(navController) }
                ) { padding ->
                    ProfileNavHost(
                        navController = navController,
                        modifier = Modifier.padding(padding),
                        onSignOut = gateViewModel::signOut
                    )
                }
            }
        }
    }
}
