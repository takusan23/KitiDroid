package io.github.takusan23.kitidroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.takusan23.kitidroid.tool.PermissionCheckTool
import io.github.takusan23.kitidroid.ui.screen.HomeScreen
import io.github.takusan23.kitidroid.ui.screen.PermissionScreen
import io.github.takusan23.kitidroid.ui.theme.KitiDroidTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi

class MainActivity : ComponentActivity() {

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val context = LocalContext.current
            val startDestination = if (PermissionCheckTool.isGrantedLocationPermission(context)) "home" else "permission"

            KitiDroidTheme {
                Surface(color = MaterialTheme.colors.background) {
                    NavHost(navController = navController, startDestination = startDestination) {
                        composable("home") {
                            HomeScreen()
                        }
                        composable("permission") {
                            PermissionScreen {
                                navController.navigate("home")
                            }
                        }
                    }
                }
            }
        }

    }

}

