package com.managr.app.personal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.managr.app.personal.ui.screens.analytics.AnalyticsScreen
import com.managr.app.personal.ui.screens.home.HomeScreen
import com.managr.app.personal.ui.screens.projects.ProjectDetailScreen
import com.managr.app.personal.ui.screens.projects.ProjectListScreen

@Composable
fun MANAGRNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onProjectsClick = { navController.navigate("projects") },
                onAnalyticsClick = { navController.navigate("analytics") }
            )
        }

        composable("projects") {
            ProjectListScreen(
                onProjectClick = { projectId ->
                    navController.navigate("project_detail/$projectId")
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("project_detail/{projectId}") { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId")?.toLongOrNull() ?: 0L
            ProjectDetailScreen(
                projectId = projectId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("analytics") {
            AnalyticsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
