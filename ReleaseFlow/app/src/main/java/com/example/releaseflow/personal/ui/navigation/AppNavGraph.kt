package com.example.releaseflow.personal.ui.navigation

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.releaseflow.feature.analytics.AnalyticsScreen
import com.example.releaseflow.feature.calendar.CalendarScreen
import com.example.releaseflow.feature.projects.CreateProjectScreen
import com.example.releaseflow.feature.projects.ProjectDetailScreen
import com.example.releaseflow.feature.projects.ProjectListScreen
import com.example.releaseflow.feature.promotions.HubScreen
import com.example.releaseflow.personal.ui.screens.home.EnhancedHomeScreen

private data class BottomDest(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val deepLink: String,
)

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val bottomDests = listOf(
        BottomDest(AppDestinations.HOME, "Home", Icons.Outlined.Home, AppDestinations.DL_HOME),
        BottomDest(AppDestinations.PROJECTS, "Projects", Icons.Outlined.LibraryMusic, AppDestinations.DL_PROJECTS),
        BottomDest(AppDestinations.CALENDAR, "Calendar", Icons.Outlined.CalendarMonth, AppDestinations.DL_CALENDAR),
        BottomDest(AppDestinations.ANALYTICS, "Analytics", Icons.Outlined.Analytics, AppDestinations.DL_ANALYTICS),
        BottomDest(AppDestinations.HUB, "Hub", Icons.Outlined.Notifications, AppDestinations.DL_HUB),
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            NavigationBar {
                bottomDests.forEach { dest ->
                    val selected = currentDestination.isInHierarchy(dest.route)
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(dest.route) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                            }
                        },
                        icon = { Icon(dest.icon, contentDescription = dest.label) },
                        label = { Text(dest.label) }
                    )
                }
            }
        }
    ) { innerPadding: PaddingValues ->
        Crossfade(
            targetState = navController.currentBackStackEntryAsState().value?.destination?.route,
            animationSpec = tween(durationMillis = 180),
            modifier = Modifier.padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = AppDestinations.START,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(AppDestinations.HOME) {
                    EnhancedHomeScreen(
                        onProjectsClick = { navController.navigate(AppDestinations.PROJECTS) },
                        onAnalyticsClick = { navController.navigate(AppDestinations.ANALYTICS) },
                        onAIAssistantClick = {},
                        onCreateProjectClick = { navController.navigate("projects/create") },
                        onCalendarClick = { navController.navigate(AppDestinations.CALENDAR) },
                        onHubClick = { navController.navigate(AppDestinations.HUB) }
                    )
                }

                composable(AppDestinations.PROJECTS) {
                    ProjectListScreen(
                        onProjectClick = { projectId ->
                            navController.navigate("projects/$projectId")
                        },
                        onCreateProjectClick = {
                            navController.navigate("projects/create")
                        }
                    )
                }

                composable("projects/create") {
                    CreateProjectScreen(
                        onNavigateBack = { navController.navigateUp() },
                        onProjectCreated = { projectId ->
                            navController.navigate("projects/$projectId") {
                                popUpTo(AppDestinations.PROJECTS)
                            }
                        }
                    )
                }

                composable(
                    route = "projects/{projectId}",
                    arguments = listOf(navArgument("projectId") { type = NavType.LongType })
                ) { backStackEntry ->
                    val projectId = backStackEntry.arguments?.getLong("projectId") ?: 0L
                    ProjectDetailScreen(
                        projectId = projectId,
                        onNavigateBack = { navController.navigateUp() }
                    )
                }

                composable(AppDestinations.CALENDAR) {
                    CalendarScreen()
                }

                composable(AppDestinations.ANALYTICS) {
                    AnalyticsScreen(onBackClick = { navController.navigateUp() })
                }

                composable(AppDestinations.HUB) {
                    HubScreen()
                }
            }
        }
    }
}

private fun NavDestination?.isInHierarchy(route: String): Boolean =
    this?.hierarchy?.any { it.route == route } == true