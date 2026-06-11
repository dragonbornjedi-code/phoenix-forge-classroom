package com.phoenixforge.profile.ui.gate

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.phoenixforge.profile.ui.navigation.ProfileBottomBar
import com.phoenixforge.profile.ui.navigation.ProfileNavHost

@OptIn(ExperimentalMaterial3Api::class)
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
                    savedProfiles = gateState.savedProfiles,
                    showCreateForm = gateState.showCreateForm,
                    creatableRoles = gateState.creatableRoles,
                    onContinueExisting = gateViewModel::continueWithExistingProfile,
                    onCreateProfile = gateViewModel::createProfile,
                    onSwitchProfile = gateViewModel::switchToProfile,
                    onUseDifferentProfile = gateViewModel::useDifferentProfile,
                    onShowCreateForm = gateViewModel::showCreateProfileForm,
                    onEraseAllLocalData = gateViewModel::eraseAllLocalData
                )
            }
            ProfileGatePhase.Ready -> {
                val navController = rememberNavController()
                val profileName = gateState.existingProfile?.forgeName ?: "Forge Profile"
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text(profileName) },
                            actions = {
                                IconButton(onClick = gateViewModel::useDifferentProfile) {
                                    Icon(Icons.Default.SwapHoriz, contentDescription = "Switch profile")
                                }
                                TextButton(onClick = gateViewModel::signOut) {
                                    Row {
                                        Icon(
                                            Icons.AutoMirrored.Filled.Logout,
                                            contentDescription = null,
                                            modifier = Modifier.padding(end = 4.dp),
                                        )
                                        Text("Sign out")
                                    }
                                }
                            }
                        )
                    },
                    bottomBar = { ProfileBottomBar(navController) }
                ) { padding ->
                    ProfileNavHost(
                        navController = navController,
                        modifier = Modifier.padding(padding),
                        onSignOut = gateViewModel::signOut,
                        onSwitchProfile = gateViewModel::useDifferentProfile,
                    )
                }
            }
        }
    }
}
