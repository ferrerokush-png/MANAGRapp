# MANAGR API Documentation

## Overview

This document provides comprehensive API documentation for the MANAGR application, including all public interfaces, data models, and usage examples.

## Table of Contents

1. [Core Domain Models](#core-domain-models)
2. [Repository Interfaces](#repository-interfaces)
3. [Use Cases](#use-cases)
4. [UI Components](#ui-components)
5. [Design System](#design-system)
6. [Accessibility APIs](#accessibility-apis)
7. [Performance APIs](#performance-apis)
8. [Testing APIs](#testing-apis)

## Core Domain Models

### Project
```kotlin
data class Project(
    val id: Long = 0,
    val title: String,
    val artist: String,
    val genre: String,
    val releaseDate: LocalDate,
    val status: ProjectStatus,
    val description: String? = null,
    val coverArtUrl: String? = null,
    val streamingLinks: List<String> = emptyList(),
    val socialMediaLinks: List<String> = emptyList(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

enum class ProjectStatus {
    DRAFT, IN_PROGRESS, READY_FOR_RELEASE, RELEASED, CANCELLED
}
```

### Contact
```kotlin
data class Contact(
    val id: Long = 0,
    val name: String,
    val email: String? = null,
    val phone: String? = null,
    val company: String? = null,
    val role: String? = null,
    val notes: String? = null,
    val tags: List<String> = emptyList(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
```

### Analytics
```kotlin
data class StreamingMetrics(
    val id: Long = 0,
    val projectId: Long,
    val platform: String,
    val streams: Long,
    val listeners: Long,
    val revenue: Double,
    val date: LocalDate,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
```

## Repository Interfaces

### ProjectRepository
```kotlin
interface ProjectRepository {
    suspend fun getAllProjects(): Flow<List<Project>>
    suspend fun getProjectById(id: Long): Project?
    suspend fun insertProject(project: Project): Long
    suspend fun updateProject(project: Project)
    suspend fun deleteProject(id: Long)
    suspend fun getProjectsByStatus(status: ProjectStatus): Flow<List<Project>>
    suspend fun getProjectsByGenre(genre: String): Flow<List<Project>>
    suspend fun searchProjects(query: String): Flow<List<Project>>
    suspend fun deleteAllProjects()
}
```

### ContactRepository
```kotlin
interface ContactRepository {
    suspend fun getAllContacts(): Flow<List<Contact>>
    suspend fun getContactById(id: Long): Contact?
    suspend fun insertContact(contact: Contact): Long
    suspend fun updateContact(contact: Contact)
    suspend fun deleteContact(id: Long)
    suspend fun getContactsByTag(tag: String): Flow<List<Contact>>
    suspend fun searchContacts(query: String): Flow<List<Contact>>
    suspend fun deleteAllContacts()
}
```

### AnalyticsRepository
```kotlin
interface AnalyticsRepository {
    suspend fun getMetricsForProject(projectId: Long): Flow<List<StreamingMetrics>>
    suspend fun insertMetrics(metrics: StreamingMetrics)
    suspend fun updateMetrics(metrics: StreamingMetrics)
    suspend fun deleteMetrics(id: Long)
    suspend fun getTotalStreams(projectId: Long): Flow<Long>
    suspend fun getTotalRevenue(projectId: Long): Flow<Double>
    suspend fun getAllAnalytics(): Flow<List<StreamingMetrics>>
    suspend fun deleteAllAnalytics()
}
```

## Use Cases

### ProjectUseCases
```kotlin
class ProjectUseCases @Inject constructor(
    private val projectRepository: ProjectRepository
) {
    suspend fun getAllProjects(): Flow<List<Project>> = projectRepository.getAllProjects()
    
    suspend fun getProjectById(id: Long): Project? = projectRepository.getProjectById(id)
    
    suspend fun createProject(project: Project): Result<Long> = try {
        val id = projectRepository.insertProject(project)
        Result.success(id)
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    suspend fun updateProject(project: Project): Result<Unit> = try {
        projectRepository.updateProject(project)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    suspend fun deleteProject(id: Long): Result<Unit> = try {
        projectRepository.deleteProject(id)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
```

### ContactUseCases
```kotlin
class ContactUseCases @Inject constructor(
    private val contactRepository: ContactRepository
) {
    suspend fun getAllContacts(): Flow<List<Contact>> = contactRepository.getAllContacts()
    
    suspend fun getContactById(id: Long): Contact? = contactRepository.getContactById(id)
    
    suspend fun createContact(contact: Contact): Result<Long> = try {
        val id = contactRepository.insertContact(contact)
        Result.success(id)
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    suspend fun updateContact(contact: Contact): Result<Unit> = try {
        contactRepository.updateContact(contact)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    suspend fun deleteContact(id: Long): Result<Unit> = try {
        contactRepository.deleteProject(id)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
```

## UI Components

### GlassCard
```kotlin
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable () -> Unit
)
```

### RFButton
```kotlin
@Composable
fun RFButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: ButtonVariant = ButtonVariant.PRIMARY,
    size: ButtonSize = ButtonSize.MEDIUM,
    content: @Composable RowScope.() -> Unit
)
```

### EmptyState
```kotlin
@Composable
fun EmptyState(
    title: String,
    subtitle: String? = null,
    icon: ImageVector = Icons.Default.MusicNote,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
)
```

## Design System

### Colors
```kotlin
object ManagrColors {
    val Primary = Color(0xFF6750A4)
    val OnPrimary = Color(0xFFFFFFFF)
    val PrimaryContainer = Color(0xFFEADDFF)
    val OnPrimaryContainer = Color(0xFF21005D)
    val Secondary = Color(0xFF625B71)
    val OnSecondary = Color(0xFFFFFFFF)
    val SecondaryContainer = Color(0xFFE8DEF8)
    val OnSecondaryContainer = Color(0xFF1D192B)
    val Tertiary = Color(0xFF7D5260)
    val OnTertiary = Color(0xFFFFFFFF)
    val TertiaryContainer = Color(0xFFFFD8E4)
    val OnTertiaryContainer = Color(0xFF31111D)
    val Error = Color(0xFFBA1A1A)
    val OnError = Color(0xFFFFFFFF)
    val ErrorContainer = Color(0xFFFFDAD6)
    val OnErrorContainer = Color(0xFF410002)
    val Background = Color(0xFFFFFBFE)
    val OnBackground = Color(0xFF1C1B1F)
    val Surface = Color(0xFFFFFBFE)
    val OnSurface = Color(0xFF1C1B1F)
    val SurfaceVariant = Color(0xFFE7E0EC)
    val OnSurfaceVariant = Color(0xFF49454F)
    val Outline = Color(0xFF79747E)
    val OutlineVariant = Color(0xFFCAC4D0)
    val InverseSurface = Color(0xFF313033)
    val InverseOnSurface = Color(0xFFF4EFF4)
    val InversePrimary = Color(0xFFD0BCFF)
}
```

### Typography
```kotlin
val ManagrTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)
```

## Accessibility APIs

### AccessibilityUtils
```kotlin
class AccessibilityUtils @Inject constructor() {
    fun isAccessibilityEnabled(context: Context): Boolean
    fun isScreenReaderEnabled(context: Context): Boolean
    fun isHighContrastEnabled(context: Context): Boolean
    fun isLargeTextEnabled(context: Context): Boolean
    fun getAccessibilitySettings(context: Context): AccessibilitySettings
    fun getContentDescription(text: String, type: ContentType, state: String? = null): String
    fun getRole(type: ContentType): Role
}
```

### AccessibleComponents
```kotlin
@Composable
fun AccessibleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String,
    icon: ImageVector? = null,
    state: String? = null,
    isHighContrast: Boolean = false
)

@Composable
fun AccessibleCard(
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    content: @Composable () -> Unit,
    isHighContrast: Boolean = false
)
```

## Performance APIs

### PerformanceMonitor
```kotlin
class PerformanceMonitor @Inject constructor() {
    fun startMonitoring()
    fun stopMonitoring()
    fun getPerformanceMetrics(): PerformanceMetrics
    fun recordFrameTime(frameTime: Float)
    fun recordMemoryUsage(usage: Long)
    fun recordNetworkLatency(latency: Long)
    fun recordDatabaseQueryTime(time: Long)
}
```

### MemoryManager
```kotlin
class MemoryManager @Inject constructor() {
    fun startMonitoring(context: Context)
    fun stopMonitoring()
    fun clearMemory()
    fun forceGarbageCollection()
    fun isMemoryLow(): Boolean
    fun isMemoryCritical(): Boolean
    fun getMemoryRecommendations(): List<String>
}
```

## Testing APIs

### TestDataFactory
```kotlin
object TestDataFactory {
    fun createProject(
        id: Long = 0,
        title: String = "Test Project",
        artist: String = "Test Artist",
        genre: String = "Pop",
        releaseDate: LocalDate = LocalDate.now().plusDays(30),
        status: ProjectStatus = ProjectStatus.DRAFT
    ): Project
    
    fun createContact(
        id: Long = 0,
        name: String = "Test Contact",
        email: String = "test@example.com",
        company: String = "Test Company"
    ): Contact
    
    fun createStreamingMetrics(
        id: Long = 0,
        projectId: Long = 1,
        platform: String = "Spotify",
        streams: Long = 1000,
        listeners: Long = 500,
        revenue: Double = 10.0,
        date: LocalDate = LocalDate.now()
    ): StreamingMetrics
}
```

### TestRules
```kotlin
class HiltTestRule : TestRule {
    override fun apply(base: Statement, description: Description): Statement
}

class ComposeTestRule : TestRule {
    override fun apply(base: Statement, description: Description): Statement
}
```

## Usage Examples

### Creating a Project
```kotlin
@Composable
fun CreateProjectScreen(
    viewModel: CreateProjectViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Column {
        TextField(
            value = uiState.title,
            onValueChange = viewModel::updateTitle,
            label = { Text("Project Title") }
        )
        
        Button(
            onClick = { viewModel.createProject() }
        ) {
            Text("Create Project")
        }
    }
}
```

### Managing Contacts
```kotlin
@Composable
fun ContactListScreen(
    viewModel: ContactListViewModel = hiltViewModel()
) {
    val contacts by viewModel.contacts.collectAsState()
    
    LazyColumn {
        items(contacts) { contact ->
            ContactItem(
                contact = contact,
                onClick = { viewModel.selectContact(contact.id) }
            )
        }
    }
}
```

### Viewing Analytics
```kotlin
@Composable
fun AnalyticsScreen(
    viewModel: AnalyticsViewModel = hiltViewModel()
) {
    val metrics by viewModel.metrics.collectAsState()
    
    Column {
        Text("Total Streams: ${metrics.totalStreams}")
        Text("Total Revenue: $${metrics.totalRevenue}")
        
        // Chart components would go here
    }
}
```

## Error Handling

### Global Exception Handler
```kotlin
class GlobalExceptionHandler @Inject constructor(
    private val errorHandler: ErrorHandler
) {
    fun initialize()
    fun handleException(exception: Throwable)
}
```

### Error States
```kotlin
@Composable
fun ErrorState(
    error: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Error: $error")
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}
```

## Configuration

### App Configuration
```kotlin
@HiltAndroidApp
class MANAGRApp : Application() {
    @Inject lateinit var globalExceptionHandler: GlobalExceptionHandler
    @Inject lateinit var performanceOptimizationManager: PerformanceOptimizationManager
    @Inject lateinit var accessibilitySettings: AccessibilitySettings
    
    override fun onCreate() {
        super.onCreate()
        globalExceptionHandler.initialize()
        performanceOptimizationManager.startOptimization(this)
        accessibilitySettings.initialize(this)
    }
}
```

### Dependency Injection
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideProjectRepository(): ProjectRepository = ProjectRepositoryImpl()
    
    @Provides
    @Singleton
    fun provideContactRepository(): ContactRepository = ContactRepositoryImpl()
    
    @Provides
    @Singleton
    fun provideAnalyticsRepository(): AnalyticsRepository = AnalyticsRepositoryImpl()
}
```

This documentation provides a comprehensive overview of the MANAGR API, including all public interfaces, data models, and usage examples. For more specific implementation details, refer to the source code and inline documentation.
