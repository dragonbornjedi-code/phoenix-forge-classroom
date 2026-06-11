package com.phoenixforge.student

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.phoenixforge.student.ui.navigation.StudentBottomBar
import com.phoenixforge.student.ui.navigation.StudentNavHost
import com.phoenixforge.student.ui.navigation.StudentRoutes
import com.phoenixforge.student.ui.theme.StudentHouseTheme
import com.phoenixforge.student.ui.theme.StudentKidCopy
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudentHouseTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    val backStack by navController.currentBackStackEntryAsState()
                    val route = backStack?.destination?.route
                    val title = StudentKidCopy.screenTitle(route)
                    val showBottomBar = StudentRoutes.showBottomBar(route)

                    Scaffold(
                        topBar = {
                            CenterAlignedTopAppBar(
                                title = {
                                    Text(
                                        title,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                    )
                                },
                                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.background,
                                ),
                            )
                        },
                        bottomBar = {
                            if (showBottomBar) {
                                StudentBottomBar(navController)
                            }
                        },
                    ) { padding ->
                        StudentNavHost(
                            navController = navController,
                            modifier = Modifier.padding(padding),
                        )
                    }
                }
            }
        }
    }
}
