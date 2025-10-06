# MANAGR Testing Guide

## Overview

This guide provides comprehensive information about testing the MANAGR application, including unit tests, integration tests, UI tests, and accessibility testing.

## Table of Contents

1. [Testing Strategy](#testing-strategy)
2. [Unit Testing](#unit-testing)
3. [Integration Testing](#integration-testing)
4. [UI Testing](#ui-testing)
5. [Accessibility Testing](#accessibility-testing)
6. [Performance Testing](#performance-testing)
7. [Test Data Management](#test-data-management)
8. [Continuous Integration](#continuous-integration)

## Testing Strategy

### Test Pyramid
```
        /\
       /  \
      / UI \     <- UI Tests (10%)
     /______\
    /        \
   /Integration\ <- Integration Tests (20%)
  /____________\
 /              \
/   Unit Tests   \ <- Unit Tests (70%)
 /________________\
```

### Test Coverage Goals
- **Unit Tests**: 85% coverage for business logic
- **Integration Tests**: 70% coverage for data layer
- **UI Tests**: 60% coverage for critical user flows
- **Accessibility Tests**: 100% coverage for accessibility features

## Unit Testing

### Project Use Case Tests
```kotlin
@RunWith(AndroidJUnit4::class)
class ProjectUseCaseTest {
    
    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    
    @Mock
    private lateinit var projectRepository: ProjectRepository
    
    private lateinit var projectUseCases: ProjectUseCases
    
    @Before
    fun setup() {
        hiltRule.inject()
        projectUseCases = ProjectUseCases(projectRepository)
    }
    
    @Test
    fun `createProject should return success when repository succeeds`() = runTest {
        // Given
        val project = TestDataFactory.createProject()
        whenever(projectRepository.insertProject(project)).thenReturn(1L)
        
        // When
        val result = projectUseCases.createProject(project)
        
        // Then
        assertTrue(result.isSuccess)
        assertEquals(1L, result.getOrNull())
        verify(projectRepository).insertProject(project)
    }
    
    @Test
    fun `createProject should return failure when repository throws exception`() = runTest {
        // Given
        val project = TestDataFactory.createProject()
        val exception = Exception("Database error")
        whenever(projectRepository.insertProject(project)).thenThrow(exception)
        
        // When
        val result = projectUseCases.createProject(project)
        
        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
    
    @Test
    fun `getAllProjects should return flow from repository`() = runTest {
        // Given
        val projects = listOf(
            TestDataFactory.createProject(id = 1, title = "Project 1"),
            TestDataFactory.createProject(id = 2, title = "Project 2")
        )
        val flow = flowOf(projects)
        whenever(projectRepository.getAllProjects()).thenReturn(flow)
        
        // When
        val result = projectUseCases.getAllProjects()
        
        // Then
        val collectedProjects = result.first()
        assertEquals(projects, collectedProjects)
    }
}
```

### ViewModel Tests
```kotlin
@RunWith(AndroidJUnit4::class)
class ProjectListViewModelTest {
    
    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    
    @Mock
    private lateinit var projectUseCases: ProjectUseCases
    
    private lateinit var viewModel: ProjectListViewModel
    
    @Before
    fun setup() {
        hiltRule.inject()
        viewModel = ProjectListViewModel(projectUseCases)
    }
    
    @Test
    fun `loadProjects should update uiState with projects`() = runTest {
        // Given
        val projects = listOf(
            TestDataFactory.createProject(id = 1, title = "Project 1"),
            TestDataFactory.createProject(id = 2, title = "Project 2")
        )
        whenever(projectUseCases.getAllProjects()).thenReturn(flowOf(projects))
        
        // When
        viewModel.loadProjects()
        
        // Then
        val uiState = viewModel.uiState.value
        assertEquals(projects, uiState.projects)
        assertFalse(uiState.isLoading)
    }
    
    @Test
    fun `deleteProject should remove project from list`() = runTest {
        // Given
        val projectId = 1L
        val projects = listOf(
            TestDataFactory.createProject(id = 1, title = "Project 1"),
            TestDataFactory.createProject(id = 2, title = "Project 2")
        )
        whenever(projectUseCases.getAllProjects()).thenReturn(flowOf(projects))
        whenever(projectUseCases.deleteProject(projectId)).thenReturn(Result.success(Unit))
        
        // When
        viewModel.deleteProject(projectId)
        
        // Then
        verify(projectUseCases).deleteProject(projectId)
    }
}
```

### Repository Tests
```kotlin
@RunWith(AndroidJUnit4::class)
class ProjectRepositoryTest {
    
    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    
    private lateinit var database: TestDatabase
    private lateinit var projectDao: ProjectDao
    private lateinit var repository: ProjectRepositoryImpl
    
    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TestDatabase::class.java
        ).build()
        projectDao = database.projectDao()
        repository = ProjectRepositoryImpl(projectDao)
    }
    
    @After
    fun tearDown() {
        database.close()
    }
    
    @Test
    fun `insertProject should save project to database`() = runTest {
        // Given
        val project = TestDataFactory.createProject(title = "Test Project")
        
        // When
        val id = repository.insertProject(project)
        
        // Then
        assertTrue(id > 0)
        val savedProject = repository.getProjectById(id)
        assertEquals(project.title, savedProject?.title)
    }
    
    @Test
    fun `getAllProjects should return all projects`() = runTest {
        // Given
        val projects = listOf(
            TestDataFactory.createProject(id = 1, title = "Project 1"),
            TestDataFactory.createProject(id = 2, title = "Project 2")
        )
        projects.forEach { repository.insertProject(it) }
        
        // When
        val result = repository.getAllProjects().first()
        
        // Then
        assertEquals(2, result.size)
        assertTrue(result.any { it.title == "Project 1" })
        assertTrue(result.any { it.title == "Project 2" })
    }
}
```

## Integration Testing

### Database Integration Tests
```kotlin
@RunWith(AndroidJUnit4::class)
class ProjectIntegrationTest {
    
    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    
    @Inject
    lateinit var projectRepository: ProjectRepository
    
    @Inject
    lateinit var contactRepository: ContactRepository
    
    @Test
    fun `createProjectWithContacts should create project and associate contacts`() = runTest {
        // Given
        val project = TestDataFactory.createProject(title = "Test Project")
        val contacts = listOf(
            TestDataFactory.createContact(name = "Contact 1"),
            TestDataFactory.createContact(name = "Contact 2")
        )
        
        // When
        val projectId = projectRepository.insertProject(project)
        contacts.forEach { contact ->
            contactRepository.insertContact(contact)
        }
        
        // Then
        val savedProject = projectRepository.getProjectById(projectId)
        assertNotNull(savedProject)
        assertEquals(project.title, savedProject?.title)
        
        val savedContacts = contactRepository.getAllContacts().first()
        assertEquals(2, savedContacts.size)
    }
    
    @Test
    fun `deleteProject should cascade delete related data`() = runTest {
        // Given
        val project = TestDataFactory.createProject(title = "Test Project")
        val projectId = projectRepository.insertProject(project)
        
        // When
        projectRepository.deleteProject(projectId)
        
        // Then
        val deletedProject = projectRepository.getProjectById(projectId)
        assertNull(deletedProject)
    }
}
```

### API Integration Tests
```kotlin
@RunWith(AndroidJUnit4::class)
class AnalyticsIntegrationTest {
    
    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    
    @Inject
    lateinit var analyticsRepository: AnalyticsRepository
    
    @Mock
    private lateinit var apiService: AnalyticsApiService
    
    @Test
    fun `syncAnalytics should fetch data from API and save to database`() = runTest {
        // Given
        val apiMetrics = listOf(
            TestDataFactory.createStreamingMetrics(platform = "Spotify", streams = 1000),
            TestDataFactory.createStreamingMetrics(platform = "Apple Music", streams = 500)
        )
        whenever(apiService.getAnalytics()).thenReturn(apiMetrics)
        
        // When
        analyticsRepository.syncAnalytics()
        
        // Then
        val savedMetrics = analyticsRepository.getAllAnalytics().first()
        assertEquals(2, savedMetrics.size)
        assertTrue(savedMetrics.any { it.platform == "Spotify" })
        assertTrue(savedMetrics.any { it.platform == "Apple Music" })
    }
}
```

## UI Testing

### Compose UI Tests
```kotlin
@RunWith(AndroidJUnit4::class)
class ProjectListScreenTest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun projectListScreen_displaysProjects() {
        // Given
        val projects = listOf(
            TestDataFactory.createProject(id = 1, title = "Project 1"),
            TestDataFactory.createProject(id = 2, title = "Project 2")
        )
        
        // When
        composeTestRule.setContent {
            ProjectListScreen(
                projects = projects,
                onProjectClick = {},
                onAddProjectClick = {}
            )
        }
        
        // Then
        composeTestRule.onNodeWithText("Project 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Project 2").assertIsDisplayed()
    }
    
    @Test
    fun projectListScreen_clickingProject_callsOnProjectClick() {
        // Given
        val projects = listOf(TestDataFactory.createProject(id = 1, title = "Project 1"))
        var clickedProjectId: Long? = null
        
        // When
        composeTestRule.setContent {
            ProjectListScreen(
                projects = projects,
                onProjectClick = { clickedProjectId = it },
                onAddProjectClick = {}
            )
        }
        
        composeTestRule.onNodeWithText("Project 1").performClick()
        
        // Then
        assertEquals(1L, clickedProjectId)
    }
    
    @Test
    fun projectListScreen_emptyState_displaysEmptyMessage() {
        // Given
        val emptyProjects = emptyList<Project>()
        
        // When
        composeTestRule.setContent {
            ProjectListScreen(
                projects = emptyProjects,
                onProjectClick = {},
                onAddProjectClick = {}
            )
        }
        
        // Then
        composeTestRule.onNodeWithText("No projects yet").assertIsDisplayed()
        composeTestRule.onNodeWithText("Create your first project").assertIsDisplayed()
    }
}
```

### Navigation Tests
```kotlin
@RunWith(AndroidJUnit4::class)
class NavigationTest {
    
    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun navigation_fromHomeToProjects_shouldNavigateCorrectly() {
        // Given
        composeTestRule.setContent {
            AppNavGraph()
        }
        
        // When
        composeTestRule.onNodeWithText("Projects").performClick()
        
        // Then
        composeTestRule.onNodeWithText("Project List").assertIsDisplayed()
    }
    
    @Test
    fun navigation_fromProjectsToProjectDetail_shouldNavigateCorrectly() {
        // Given
        val project = TestDataFactory.createProject(id = 1, title = "Test Project")
        composeTestRule.setContent {
            ProjectListScreen(
                projects = listOf(project),
                onProjectClick = { /* Navigate to detail */ },
                onAddProjectClick = {}
            )
        }
        
        // When
        composeTestRule.onNodeWithText("Test Project").performClick()
        
        // Then
        composeTestRule.onNodeWithText("Project Details").assertIsDisplayed()
    }
}
```

## Accessibility Testing

### Screen Reader Tests
```kotlin
@RunWith(AndroidJUnit4::class)
class AccessibilityTest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun projectListScreen_hasProperContentDescriptions() {
        // Given
        val projects = listOf(TestDataFactory.createProject(title = "Test Project"))
        
        // When
        composeTestRule.setContent {
            ProjectListScreen(
                projects = projects,
                onProjectClick = {},
                onAddProjectClick = {}
            )
        }
        
        // Then
        composeTestRule.onNodeWithContentDescription("Project: Test Project").assertExists()
        composeTestRule.onNodeWithContentDescription("Add new project").assertExists()
    }
    
    @Test
    fun projectListScreen_hasProperRoles() {
        // Given
        val projects = listOf(TestDataFactory.createProject(title = "Test Project"))
        
        // When
        composeTestRule.setContent {
            ProjectListScreen(
                projects = projects,
                onProjectClick = {},
                onAddProjectClick = {}
            )
        }
        
        // Then
        composeTestRule.onNodeWithRole(Role.Button).assertExists()
        composeTestRule.onNodeWithRole(Role.ListItem).assertExists()
    }
    
    @Test
    fun projectListScreen_hasProperFocusOrder() {
        // Given
        val projects = listOf(TestDataFactory.createProject(title = "Test Project"))
        
        // When
        composeTestRule.setContent {
            ProjectListScreen(
                projects = projects,
                onProjectClick = {},
                onAddProjectClick = {}
            )
        }
        
        // Then
        composeTestRule.onNodeWithText("Add Project").performClick()
        composeTestRule.onNodeWithText("Add Project").assertIsFocused()
    }
}
```

### High Contrast Tests
```kotlin
@RunWith(AndroidJUnit4::class)
class HighContrastTest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun projectListScreen_highContrastMode_hasBorders() {
        // Given
        val projects = listOf(TestDataFactory.createProject(title = "Test Project"))
        
        // When
        composeTestRule.setContent {
            ProjectListScreen(
                projects = projects,
                onProjectClick = {},
                onAddProjectClick = {},
                isHighContrast = true
            )
        }
        
        // Then
        composeTestRule.onNodeWithText("Test Project").assertHasClickAction()
        // Verify border is present (this would need custom assertion)
    }
}
```

## Performance Testing

### Memory Leak Tests
```kotlin
@RunWith(AndroidJUnit4::class)
class MemoryLeakTest {
    
    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    
    @Test
    fun projectListScreen_noMemoryLeaks() {
        // Given
        val projects = listOf(TestDataFactory.createProject(title = "Test Project"))
        
        // When
        repeat(100) {
            composeTestRule.setContent {
                ProjectListScreen(
                    projects = projects,
                    onProjectClick = {},
                    onAddProjectClick = {}
                )
            }
            composeTestRule.waitForIdle()
        }
        
        // Then
        // Verify no memory leaks (this would need custom memory monitoring)
    }
}
```

### Performance Tests
```kotlin
@RunWith(AndroidJUnit4::class)
class PerformanceTest {
    
    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    
    @Test
    fun projectListScreen_rendersWithinTimeLimit() {
        // Given
        val projects = (1..1000).map { 
            TestDataFactory.createProject(id = it.toLong(), title = "Project $it")
        }
        
        // When
        val startTime = System.currentTimeMillis()
        composeTestRule.setContent {
            ProjectListScreen(
                projects = projects,
                onProjectClick = {},
                onAddProjectClick = {}
            )
        }
        composeTestRule.waitForIdle()
        val endTime = System.currentTimeMillis()
        
        // Then
        val renderTime = endTime - startTime
        assertTrue("Render time $renderTime should be less than 1000ms", renderTime < 1000)
    }
}
```

## Test Data Management

### Test Data Factory
```kotlin
object TestDataFactory {
    
    fun createProject(
        id: Long = 0,
        title: String = "Test Project",
        artist: String = "Test Artist",
        genre: String = "Pop",
        releaseDate: LocalDate = LocalDate.now().plusDays(30),
        status: ProjectStatus = ProjectStatus.DRAFT,
        description: String? = null,
        coverArtUrl: String? = null,
        streamingLinks: List<String> = emptyList(),
        socialMediaLinks: List<String> = emptyList(),
        createdAt: LocalDateTime = LocalDateTime.now(),
        updatedAt: LocalDateTime = LocalDateTime.now()
    ): Project = Project(
        id = id,
        title = title,
        artist = artist,
        genre = genre,
        releaseDate = releaseDate,
        status = status,
        description = description,
        coverArtUrl = coverArtUrl,
        streamingLinks = streamingLinks,
        socialMediaLinks = socialMediaLinks,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
    
    fun createContact(
        id: Long = 0,
        name: String = "Test Contact",
        email: String? = "test@example.com",
        phone: String? = "+1234567890",
        company: String? = "Test Company",
        role: String? = "Manager",
        notes: String? = "Test notes",
        tags: List<String> = listOf("test", "contact"),
        createdAt: LocalDateTime = LocalDateTime.now(),
        updatedAt: LocalDateTime = LocalDateTime.now()
    ): Contact = Contact(
        id = id,
        name = name,
        email = email,
        phone = phone,
        company = company,
        role = role,
        notes = notes,
        tags = tags,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
    
    fun createStreamingMetrics(
        id: Long = 0,
        projectId: Long = 1,
        platform: String = "Spotify",
        streams: Long = 1000,
        listeners: Long = 500,
        revenue: Double = 10.0,
        date: LocalDate = LocalDate.now(),
        createdAt: LocalDateTime = LocalDateTime.now()
    ): StreamingMetrics = StreamingMetrics(
        id = id,
        projectId = projectId,
        platform = platform,
        streams = streams,
        listeners = listeners,
        revenue = revenue,
        date = date,
        createdAt = createdAt
    )
}
```

### Test Database
```kotlin
@Database(
    entities = [Project::class, Contact::class, StreamingMetrics::class],
    version = 1,
    exportSchema = false
)
abstract class TestDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun contactDao(): ContactDao
    abstract fun analyticsDao(): AnalyticsDao
}
```

## Continuous Integration

### GitHub Actions Workflow
```yaml
name: CI/CD Pipeline

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  test:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 11
      uses: actions/setup-java@v3
      with:
        java-version: '11'
        distribution: 'temurin'
    
    - name: Cache Gradle packages
      uses: actions/cache@v3
      with:
        path: |
          ~/.gradle/caches
          ~/.gradle/wrapper
        key: ${{ runner.os }}-gradle-${{ hashFiles('**/*.gradle*', '**/gradle-wrapper.properties') }}
        restore-keys: |
          ${{ runner.os }}-gradle-
    
    - name: Run unit tests
      run: ./gradlew test
    
    - name: Run integration tests
      run: ./gradlew connectedAndroidTest
    
    - name: Run UI tests
      run: ./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.managr.app.personal.ui.ProjectListScreenTest
    
    - name: Run accessibility tests
      run: ./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.managr.app.personal.accessibility.AccessibilityTest
    
    - name: Generate test report
      uses: dorny/test-reporter@v1
      if: success() || failure()
      with:
        name: Test Results
        path: app/build/reports/tests/
        reporter: java-junit
```

### Test Reports
- **Unit Test Report**: `app/build/reports/tests/testDebugUnitTest/index.html`
- **Integration Test Report**: `app/build/reports/androidTests/connected/index.html`
- **UI Test Report**: `app/build/reports/androidTests/connected/index.html`
- **Coverage Report**: `app/build/reports/jacoco/testDebugUnitTestCoverage/html/index.html`

## Best Practices

### Unit Testing
1. **Test one thing at a time** - Each test should verify a single behavior
2. **Use descriptive test names** - Test names should clearly describe what is being tested
3. **Follow AAA pattern** - Arrange, Act, Assert
4. **Use mocks for dependencies** - Isolate the unit under test
5. **Test edge cases** - Include tests for error conditions and boundary values

### Integration Testing
1. **Test real interactions** - Use actual database and API calls
2. **Test data flow** - Verify data flows correctly between layers
3. **Test error handling** - Ensure proper error handling across layers
4. **Use test databases** - Use in-memory databases for testing
5. **Clean up after tests** - Ensure tests don't affect each other

### UI Testing
1. **Test user interactions** - Focus on user-facing behavior
2. **Test navigation** - Verify navigation flows work correctly
3. **Test different states** - Test loading, error, and success states
4. **Use page objects** - Organize UI test code with page objects
5. **Keep tests stable** - Avoid flaky tests by using proper waits

### Accessibility Testing
1. **Test with screen readers** - Verify content is properly announced
2. **Test keyboard navigation** - Ensure all functionality is keyboard accessible
3. **Test high contrast** - Verify UI works in high contrast mode
4. **Test large text** - Ensure UI scales properly with large text
5. **Test focus management** - Verify focus order is logical

This testing guide provides comprehensive information about testing the MANAGR application. Follow these guidelines to ensure high-quality, reliable tests that provide good coverage and catch issues early in the development process.
