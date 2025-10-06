package com.managr.app.personal.ui.navigation

object AppDestinations {
    // Top-level tabs
    const val HOME = "home"
    const val PROJECTS = "projects"
    const val CALENDAR = "calendar"
    const val ANALYTICS = "analytics"
    const val HUB = "hub"

    // Settings routes
    const val SETTINGS = "settings"
    const val DATA_MANAGEMENT = "data_management"

    // Start destination
    const val START = HOME

    // Detail routes
    const val PROJECT_DETAIL = "project_detail"
    const val PROJECT_DETAIL_ROUTE = "$PROJECT_DETAIL/{projectId}"

    fun projectDetailRoute(projectId: Long): String = "$PROJECT_DETAIL/$projectId"

    // Deep links
    const val DEEP_LINK_SCHEME = "managr://"
    const val DL_HOME = DEEP_LINK_SCHEME + HOME
    const val DL_PROJECTS = DEEP_LINK_SCHEME + PROJECTS
    const val DL_CALENDAR = DEEP_LINK_SCHEME + CALENDAR
    const val DL_ANALYTICS = DEEP_LINK_SCHEME + ANALYTICS
    const val DL_HUB = DEEP_LINK_SCHEME + HUB
    const val DL_SETTINGS = DEEP_LINK_SCHEME + SETTINGS
}
