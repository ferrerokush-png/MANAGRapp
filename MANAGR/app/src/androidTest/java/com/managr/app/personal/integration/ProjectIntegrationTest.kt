package com.managr.app.personal.integration

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.managr.app.core.data.local.db.AppDatabase
import com.managr.app.core.data.local.db.ProjectDao
import com.managr.app.core.data.local.db.ProjectEntity
import com.managr.app.core.domain.model.Project
import com.managr.app.core.domain.model.ProjectStatus
import com.managr.app.core.domain.model.ReleaseType
import com.managr.app.feature.projects.CreateProjectUseCase
import com.managr.app.feature.projects.GetProjectByIdUseCase
import com.managr.app.feature.projects.GetProjectsUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class ProjectIntegrationTest {
    
    private lateinit var database: AppDatabase
    private lateinit var projectDao: ProjectDao
    private lateinit var createProjectUseCase: CreateProjectUseCase
    private lateinit var getProjectByIdUseCase: GetProjectByIdUseCase
    private lateinit var getProjectsUseCase: GetProjectsUseCase
    
    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        
        projectDao = database.projectDao()
        
        // Create use cases with real repository implementation
        val projectRepository = com.managr.app.core.data.repository.ProjectRepositoryImpl(projectDao)
        createProjectUseCase = CreateProjectUseCase(projectRepository)
        getProjectByIdUseCase = GetProjectByIdUseCase(projectRepository)
        getProjectsUseCase = GetProjectsUseCase(projectRepository)
    }
    
    @After
    fun tearDown() {
        database.close()
    }
    
    @Test
    fun `createProject and getProjectById integration test`() = runTest {
        // Given
        val project = createTestProject()
        
        // When
        val createResult = createProjectUseCase(project)
        val projectId = createResult.getData()
        
        // Then
        assertTrue(createResult.isSuccess())
        assertNotNull(projectId)
        
        // When
        val retrievedProject = getProjectByIdUseCase(projectId!!).first()
        
        // Then
        assertNotNull(retrievedProject)
        assertEquals(project.title, retrievedProject?.title)
        assertEquals(project.type, retrievedProject?.type)
        assertEquals(project.status, retrievedProject?.status)
    }
    
    @Test
    fun `createMultipleProjects and getAllProjects integration test`() = runTest {
        // Given
        val projects = listOf(
            createTestProject(title = "Project 1"),
            createTestProject(title = "Project 2"),
            createTestProject(title = "Project 3")
        )
        
        // When
        val createdIds = mutableListOf<Long>()
        projects.forEach { project ->
            val result = createProjectUseCase(project)
            assertTrue(result.isSuccess())
            result.getData()?.let { createdIds.add(it) }
        }
        
        val allProjects = getProjectsUseCase.getAllProjects().first()
        
        // Then
        assertEquals(3, allProjects.size)
        assertEquals(3, createdIds.size)
        assertTrue(allProjects.any { it.title == "Project 1" })
        assertTrue(allProjects.any { it.title == "Project 2" })
        assertTrue(allProjects.any { it.title == "Project 3" })
    }
    
    @Test
    fun `getActiveProjects integration test`() = runTest {
        // Given
        val activeProject = createTestProject(title = "Active Project", status = ProjectStatus.IN_PROGRESS)
        val completedProject = createTestProject(title = "Completed Project", status = ProjectStatus.COMPLETED)
        
        // When
        createProjectUseCase(activeProject)
        createProjectUseCase(completedProject)
        
        val activeProjects = getProjectsUseCase.getActiveProjects().first()
        
        // Then
        assertEquals(1, activeProjects.size)
        assertEquals("Active Project", activeProjects.first().title)
        assertEquals(ProjectStatus.IN_PROGRESS, activeProjects.first().status)
    }
    
    @Test
    fun `getUpcomingReleases integration test`() = runTest {
        // Given
        val upcomingProject = createTestProject(
            title = "Upcoming Project",
            releaseDate = LocalDate.now().plusDays(7)
        )
        val pastProject = createTestProject(
            title = "Past Project",
            releaseDate = LocalDate.now().minusDays(7)
        )
        
        // When
        createProjectUseCase(upcomingProject)
        createProjectUseCase(pastProject)
        
        val upcomingReleases = getProjectsUseCase.getUpcomingReleases().first()
        
        // Then
        assertEquals(1, upcomingReleases.size)
        assertEquals("Upcoming Project", upcomingReleases.first().title)
    }
    
    @Test
    fun `getProjectsByStatus integration test`() = runTest {
        // Given
        val completedProject1 = createTestProject(title = "Completed 1", status = ProjectStatus.COMPLETED)
        val completedProject2 = createTestProject(title = "Completed 2", status = ProjectStatus.COMPLETED)
        val inProgressProject = createTestProject(title = "In Progress", status = ProjectStatus.IN_PROGRESS)
        
        // When
        createProjectUseCase(completedProject1)
        createProjectUseCase(completedProject2)
        createProjectUseCase(inProgressProject)
        
        val completedProjects = getProjectsUseCase.getProjectsByStatus(ProjectStatus.COMPLETED).first()
        
        // Then
        assertEquals(2, completedProjects.size)
        assertTrue(completedProjects.all { it.status == ProjectStatus.COMPLETED })
    }
    
    @Test
    fun `getProjectsByType integration test`() = runTest {
        // Given
        val singleProject = createTestProject(title = "Single", type = ReleaseType.SINGLE)
        val albumProject = createTestProject(title = "Album", type = ReleaseType.ALBUM)
        val epProject = createTestProject(title = "EP", type = ReleaseType.EP)
        
        // When
        createProjectUseCase(singleProject)
        createProjectUseCase(albumProject)
        createProjectUseCase(epProject)
        
        val singleProjects = getProjectsUseCase.getProjectsByType(ReleaseType.SINGLE).first()
        
        // Then
        assertEquals(1, singleProjects.size)
        assertEquals("Single", singleProjects.first().title)
        assertEquals(ReleaseType.SINGLE, singleProjects.first().type)
    }
    
    @Test
    fun `searchProjects integration test`() = runTest {
        // Given
        val rockProject = createTestProject(title = "Rock Album")
        val popProject = createTestProject(title = "Pop Single")
        val jazzProject = createTestProject(title = "Jazz EP")
        
        // When
        createProjectUseCase(rockProject)
        createProjectUseCase(popProject)
        createProjectUseCase(jazzProject)
        
        val rockProjects = getProjectsUseCase.searchProjects("Rock").first()
        val popProjects = getProjectsUseCase.searchProjects("Pop").first()
        val jazzProjects = getProjectsUseCase.searchProjects("Jazz").first()
        
        // Then
        assertEquals(1, rockProjects.size)
        assertEquals("Rock Album", rockProjects.first().title)
        
        assertEquals(1, popProjects.size)
        assertEquals("Pop Single", popProjects.first().title)
        
        assertEquals(1, jazzProjects.size)
        assertEquals("Jazz EP", jazzProjects.first().title)
    }
    
    @Test
    fun `project validation integration test`() = runTest {
        // Given
        val invalidProject = createTestProject(title = "") // Empty title should fail validation
        
        // When
        val result = createProjectUseCase(invalidProject)
        
        // Then
        assertTrue(result.isError())
        assertNotNull(result.getError())
    }
    
    private fun createTestProject(
        title: String = "Test Project",
        type: ReleaseType = ReleaseType.SINGLE,
        status: ProjectStatus = ProjectStatus.IN_PROGRESS,
        releaseDate: LocalDate = LocalDate.now().plusDays(30)
    ): Project {
        return Project(
            id = 0L, // Will be set by database
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
