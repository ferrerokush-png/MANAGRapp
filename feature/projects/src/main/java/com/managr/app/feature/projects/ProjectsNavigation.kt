package com.managr.app.feature.projects

/**
 * Navigation destinations for Projects feature
 */
object ProjectsDestinations {
    const val PROJECTS_LIST = "projects"
    const val PROJECT_DETAIL = "projects/{projectId}"
    const val CREATE_PROJECT = "projects/create"
    
    fun projectDetail(projectId: Long) = "projects/$projectId"
}
