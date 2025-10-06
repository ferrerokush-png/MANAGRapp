package com.managr.app.personal.domain.usecase

import com.managr.app.core.domain.model.Project
import com.managr.app.core.domain.model.ProjectStatus
import com.managr.app.core.domain.model.ReleaseType
import com.managr.app.core.domain.repository.ProjectRepository
import com.managr.app.feature.projects.CreateProjectUseCase
import com.managr.app.feature.projects.GetProjectByIdUseCase
import com.managr.app.feature.projects.GetProjectsUseCase
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import java.time.LocalDate

class ProjectUseCaseTest {
    
    private lateinit var projectRepository: ProjectRepository
    private lateinit var createProjectUseCase: CreateProjectUseCase
    private lateinit var getProjectByIdUseCase: GetProjectByIdUseCase
    private lateinit var getProjectsUseCase: GetProjectsUseCase
    
    @Before
    fun setup() {
        projectRepository = mockk()
        createProjectUseCase = CreateProjectUseCase(projectRepository)
        getProjectByIdUseCase = GetProjectByIdUseCase(projectRepository)
        getProjectsUseCase = GetProjectsUseCase(projectRepository)
    }
    
    @Test
    fun `createProject should return success when project is valid`() = runTest {
        // Given
        val project = createTestProject()
        every { projectRepository.insertProject(any()) } returns 1L
        
        // When
        val result = createProjectUseCase(project)
        
        // Then
        assertTrue(result.isSuccess())
        assertEquals(1L, result.getData())
        verify { projectRepository.insertProject(project) }
    }
    
    @Test
    fun `createProject should return error when repository throws exception`() = runTest {
        // Given
        val project = createTestProject()
        every { projectRepository.insertProject(any()) } throws Exception("Database error")
        
        // When
        val result = createProjectUseCase(project)
        
        // Then
        assertTrue(result.isError())
        assertNotNull(result.getError())
        verify { projectRepository.insertProject(project) }
    }
    
    @Test
    fun `getProjectById should return project when found`() = runTest {
        // Given
        val projectId = 1L
        val project = createTestProject(id = projectId)
        every { projectRepository.getProjectById(projectId) } returns flowOf(project)
        
        // When
        val result = getProjectByIdUseCase(projectId)
        
        // Then
        val actualProject = result.first()
        assertEquals(project, actualProject)
        verify { projectRepository.getProjectById(projectId) }
    }
    
    @Test
    fun `getProjectById should return null when not found`() = runTest {
        // Given
        val projectId = 1L
        every { projectRepository.getProjectById(projectId) } returns flowOf(null)
        
        // When
        val result = getProjectByIdUseCase(projectId)
        
        // Then
        val actualProject = result.first()
        assertNull(actualProject)
        verify { projectRepository.getProjectById(projectId) }
    }
    
    @Test
    fun `getAllProjects should return all projects`() = runTest {
        // Given
        val projects = listOf(
            createTestProject(id = 1L, title = "Project 1"),
            createTestProject(id = 2L, title = "Project 2")
        )
        every { projectRepository.getAllProjects() } returns flowOf(projects)
        
        // When
        val result = getProjectsUseCase.getAllProjects()
        
        // Then
        val actualProjects = result.first()
        assertEquals(projects, actualProjects)
        verify { projectRepository.getAllProjects() }
    }
    
    @Test
    fun `getActiveProjects should return only active projects`() = runTest {
        // Given
        val allProjects = listOf(
            createTestProject(id = 1L, status = ProjectStatus.IN_PROGRESS),
            createTestProject(id = 2L, status = ProjectStatus.COMPLETED),
            createTestProject(id = 3L, status = ProjectStatus.IN_PROGRESS)
        )
        val activeProjects = listOf(
            createTestProject(id = 1L, status = ProjectStatus.IN_PROGRESS),
            createTestProject(id = 3L, status = ProjectStatus.IN_PROGRESS)
        )
        every { projectRepository.getActiveProjects() } returns flowOf(activeProjects)
        
        // When
        val result = getProjectsUseCase.getActiveProjects()
        
        // Then
        val actualProjects = result.first()
        assertEquals(activeProjects, actualProjects)
        verify { projectRepository.getActiveProjects() }
    }
    
    @Test
    fun `getUpcomingReleases should return upcoming releases`() = runTest {
        // Given
        val upcomingReleases = listOf(
            createTestProject(id = 1L, releaseDate = LocalDate.now().plusDays(7)),
            createTestProject(id = 2L, releaseDate = LocalDate.now().plusDays(14))
        )
        every { projectRepository.getUpcomingReleases() } returns flowOf(upcomingReleases)
        
        // When
        val result = getProjectsUseCase.getUpcomingReleases()
        
        // Then
        val actualProjects = result.first()
        assertEquals(upcomingReleases, actualProjects)
        verify { projectRepository.getUpcomingReleases() }
    }
    
    @Test
    fun `getProjectsByStatus should return projects with specific status`() = runTest {
        // Given
        val status = ProjectStatus.COMPLETED
        val completedProjects = listOf(
            createTestProject(id = 1L, status = status),
            createTestProject(id = 2L, status = status)
        )
        every { projectRepository.getProjectsByStatus(status) } returns flowOf(completedProjects)
        
        // When
        val result = getProjectsUseCase.getProjectsByStatus(status)
        
        // Then
        val actualProjects = result.first()
        assertEquals(completedProjects, actualProjects)
        verify { projectRepository.getProjectsByStatus(status) }
    }
    
    @Test
    fun `getProjectsByType should return projects with specific type`() = runTest {
        // Given
        val type = ReleaseType.SINGLE
        val singleProjects = listOf(
            createTestProject(id = 1L, type = type),
            createTestProject(id = 2L, type = type)
        )
        every { projectRepository.getProjectsByType(type) } returns flowOf(singleProjects)
        
        // When
        val result = getProjectsUseCase.getProjectsByType(type)
        
        // Then
        val actualProjects = result.first()
        assertEquals(singleProjects, actualProjects)
        verify { projectRepository.getProjectsByType(type) }
    }
    
    @Test
    fun `searchProjects should return matching projects`() = runTest {
        // Given
        val query = "test"
        val matchingProjects = listOf(
            createTestProject(id = 1L, title = "Test Project 1"),
            createTestProject(id = 2L, title = "Test Project 2")
        )
        every { projectRepository.searchProjects(query) } returns flowOf(matchingProjects)
        
        // When
        val result = getProjectsUseCase.searchProjects(query)
        
        // Then
        val actualProjects = result.first()
        assertEquals(matchingProjects, actualProjects)
        verify { projectRepository.searchProjects(query) }
    }
    
    private fun createTestProject(
        id: Long = 1L,
        title: String = "Test Project",
        type: ReleaseType = ReleaseType.SINGLE,
        status: ProjectStatus = ProjectStatus.IN_PROGRESS,
        releaseDate: LocalDate = LocalDate.now().plusDays(30)
    ): Project {
        return Project(
            id = id,
            title = title,
            type = type,
            status = status,
            releaseDate = releaseDate,
            distributorType = com.managr.app.core.domain.model.DistributorType.DISTROKID,
            trackCount = 1,
            completedTasks = 0,
            totalTasks = 10,
            completionPercentage = 0f,
            artworkUri = null,
            createdAt = java.time.LocalDateTime.now(),
            updatedAt = java.time.LocalDateTime.now()
        )
    }
}
