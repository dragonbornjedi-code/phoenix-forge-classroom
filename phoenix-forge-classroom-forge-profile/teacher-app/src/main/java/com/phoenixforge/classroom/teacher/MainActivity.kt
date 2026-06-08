package com.phoenixforge.classroom.teacher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.phoenixforge.classroom.teacher.ui.navigation.TeacherBottomBar
import com.phoenixforge.classroom.teacher.ui.navigation.TeacherRoutes
import com.phoenixforge.classroom.teacher.ui.theme.TeacherEditionTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TeacherEditionTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    val backStack by navController.currentBackStackEntryAsState()
                    val showBottomBar = backStack?.destination?.route in TeacherRoutes.topLevel

                    Scaffold(
                        bottomBar = {
                            if (showBottomBar) {
                                TeacherBottomBar(navController)
                            }
                        },
                    ) { padding ->
                        TeacherNavGraph(
                            navController = navController,
                            modifier = Modifier.padding(padding),
                        )
                    }
                }
            }
        }
    }
}
