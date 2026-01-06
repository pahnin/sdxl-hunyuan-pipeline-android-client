package `in`.phanindra.sdxlhunyuanpipeline

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import `in`.phanindra.sdxlhunyuanpipeline.ui.JobViewModel
import `in`.phanindra.sdxlhunyuanpipeline.ui.navigation.Screen
import `in`.phanindra.sdxlhunyuanpipeline.ui.screens.*
import `in`.phanindra.sdxlhunyuanpipeline.ui.theme.SdxlHunyuanPipelineTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SdxlHunyuanPipelineTheme {
                val navController = rememberNavController()
                val viewModel: JobViewModel = viewModel()

                val errorMessage by viewModel.errorMessage.collectAsState()

                LaunchedEffect(errorMessage) {
                    if (errorMessage != null) {
                        // Show error snackbar
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar {
                            val currentRoute = navController.currentBackStackEntryFlow
                                .collectAsState(initial = null)
                                .value?.destination?.route

                            NavigationBarItem(
                                icon = { Text("➕") },
                                label = { Text("Create") },
                                selected = currentRoute == Screen.CreateJob.route,
                                onClick = { navController.navigate(Screen.CreateJob.route) }
                            )
                            NavigationBarItem(
                                icon = { Text("▶") },
                                label = { Text("Current") },
                                selected = currentRoute == Screen.CurrentJob.route,
                                onClick = { navController.navigate(Screen.CurrentJob.route) }
                            )
                            NavigationBarItem(
                                icon = { Text("📋") },
                                label = { Text("Queue") },
                                selected = currentRoute == Screen.JobQueue.route,
                                onClick = { navController.navigate(Screen.JobQueue.route) }
                            )
                            NavigationBarItem(
                                icon = { Text("⚙") },
                                label = { Text("Settings") },
                                selected = currentRoute == Screen.Settings.route,
                                onClick = { navController.navigate(Screen.Settings.route) }
                            )
                        }
                    }
                ) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.CreateJob.route,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        composable(Screen.CreateJob.route) {
                            CreateJobScreen(
                                viewModel = viewModel,
                                onJobCreated = { navController.navigate(Screen.CurrentJob.route) },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        composable(Screen.CurrentJob.route) {
                            CurrentJobScreen(
                                viewModel = viewModel,
                                onViewJobDetail = { jobId ->
                                    navController.navigate(Screen.JobDetail.createRoute(jobId))
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        composable(Screen.JobQueue.route) {
                            JobQueueScreen(
                                viewModel = viewModel,
                                onJobClick = { jobId ->
                                    navController.navigate(Screen.JobDetail.createRoute(jobId))
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        composable(Screen.Settings.route) {
                            SettingsScreen(
                                viewModel = viewModel,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        composable(
                            route = Screen.JobDetail.route,
                            arguments = listOf(navArgument("jobId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val jobId = backStackEntry.arguments?.getInt("jobId") ?: 0
                            JobDetailScreen(
                                jobId = jobId,
                                viewModel = viewModel,
                                onNavigateBack = { navController.popBackStack() },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}
