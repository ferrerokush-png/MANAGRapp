# MANAGR - Multi-Module Architecture

## Overview
MANAGR is built using a modular architecture with MVVM + Repository pattern. The app is divided into core modules (shared functionality) and feature modules (isolated features).

## Module Structure

```
MANAGR/
├── app/                           # Main application module
├── core/
│   ├── design/                    # Design system & UI components
│   ├── data/                      # Data layer (Room, Retrofit, DataStore)
│   └── domain/                    # Business logic & use cases
└── feature/
    ├── projects/                  # Project & task management
    ├── calendar/                  # Release calendar
    ├── analytics/                 # Streaming & social analytics
    ├── promotions/                # Distribution hub & CRM
    └── assistant/                 # AI-powered recommendations
```

## Module Details

### `:app` - Application Module
- **Type**: Android Application
- **Purpose**: Application entry point and navigation orchestration
- **Dependencies**: All core and feature modules
- **Key Files**:
  - `MainActivity.kt` - Main activity with Hilt setup
  - `MANAGRApp.kt` - Application class
  - `AppNavGraph.kt` - Main navigation graph

### `:core:design` - Design System
- **Type**: Android Library
- **Purpose**: Shared UI components, theme, and design tokens
- **Dependencies**: None (only Compose dependencies)
- **Key Components**:
  - `MANAGRTheme` - Material 3 theme with light/dark mode
  - `Glassmorphism.kt` - Glass morphism effects
  - `Typography`, `Shapes`, `Colors` - Design tokens
- **Public API**:
  ```kotlin
  @Composable fun MANAGRTheme(content: @Composable () -> Unit)
  fun Modifier.glassmorphism(color: Color?, shadowElevation: Int)
  ```

### `:core:domain` - Domain Layer
- **Type**: Pure Kotlin Library (Java)
- **Purpose**: Business logic, domain models, and repository interfaces
- **Dependencies**: Only Kotlin coroutines
- **Key Components**:
  - Domain models: `Project`, `Task`, `Analytics`, etc.
  - Repository interfaces: `ProjectRepository`, `TaskRepository`, etc.
  - Use cases: Business logic operations
- **Public API**:
  ```kotlin
  data class Project(...)
  data class Task(...)
  interface ProjectRepository { ... }
  interface TaskRepository { ... }
  ```

### `:core:data` - Data Layer
- **Type**: Android Library
- **Purpose**: Data persistence, API calls, and repository implementations
- **Dependencies**: `:core:domain`
- **Key Components**:
  - Room database and DAOs
  - Repository implementations
  - Retrofit API services
  - DataStore for preferences
- **Technologies**: Room, Retrofit, DataStore, WorkManager, Hilt

### `:feature:projects` - Projects Feature
- **Type**: Android Library with Compose
- **Purpose**: Project and task management UI
- **Dependencies**: `:core:design`, `:core:domain`
- **Key Screens**:
  - Project list with artwork gallery
  - Project detail with task management
  - Project creation flow with templates
  - Gantt-style timeline view

### `:feature:calendar` - Calendar Feature
- **Type**: Android Library with Compose
- **Purpose**: Release calendar and deadline tracking
- **Dependencies**: `:core:design`, `:core:domain`
- **Key Screens**:
  - Month/week/agenda calendar views
  - Event detail and filtering
  - Notification scheduling

### `:feature:analytics` - Analytics Feature
- **Type**: Android Library with Compose
- **Purpose**: Streaming and social media analytics
- **Dependencies**: `:core:design`, `:core:domain`
- **Key Screens**:
  - Analytics dashboard with charts
  - Platform-specific metrics
  - Song-level analytics
  - Revenue tracking

### `:feature:promotions` - Promotions Feature
- **Type**: Android Library with Compose
- **Purpose**: Distribution hub and CRM
- **Dependencies**: `:core:design`, `:core:domain`
- **Key Screens**:
  - Distributor management
  - Promotional asset library
  - Artist CRM with contacts

### `:feature:assistant` - AI Assistant Feature
- **Type**: Android Library
- **Purpose**: AI-powered recommendations using Gemini
- **Dependencies**: `:core:domain`
- **Key Components**:
  - Gemini AI integration
  - Release strategy recommendations
  - Content suggestions

## Dependency Graph

```
                    ┌─────────┐
                    │   app   │
                    └────┬────┘
                         │
         ┌───────────────┼───────────────┐
         │               │               │
    ┌────▼────┐    ┌────▼────┐    ┌────▼────┐
    │  core:  │    │  core:  │    │  core:  │
    │ design  │    │  data   │    │ domain  │
    └────┬────┘    └────┬────┘    └─────────┘
         │              │               ▲
         │              └───────────────┘
         │
         └───────────────┬───────────────┐
                         │               │
         ┌───────────────┼───────────────┼───────────────┐
         │               │               │               │
    ┌────▼────┐    ┌────▼────┐    ┌────▼────┐    ┌────▼────┐
    │feature: │    │feature: │    │feature: │    │feature: │
    │projects │    │calendar │    │analytics│    │promotions│
    └─────────┘    └─────────┘    └─────────┘    └─────────┘
                                                        │
                                                   ┌────▼────┐
                                                   │feature: │
                                                   │assistant│
                                                   └─────────┘
```

## Module Communication Rules

1. **Feature modules CANNOT depend on other feature modules**
2. **All feature modules depend on `:core:design` and `:core:domain`**
3. **Only `:core:data` depends on Android framework and database libraries**
4. **`:core:domain` is pure Kotlin (no Android dependencies)**
5. **`:app` orchestrates navigation between features**
6. **Shared UI components live in `:core:design`**
7. **Business logic lives in `:core:domain` use cases**
8. **Data persistence lives in `:core:data`**

## Hilt Configuration

### Multi-Module Hilt Setup
Each module that uses Hilt includes:
```kotlin
plugins {
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.kapt")
}

dependencies {
    implementation("com.google.dagger:hilt-android:2.52")
    kapt("com.google.dagger:hilt-compiler:2.52")
}
```

### Hilt Modules
- `:app` - `@HiltAndroidApp` on Application class
- `:core:data` - `DataModule` for repositories and database
- `:feature:assistant` - `AssistantModule` for AI services
- Feature modules use `@HiltViewModel` for ViewModels

## Building the Project

### Prerequisites
- Android Studio Hedgehog or later
- JDK 17
- Android SDK 34
- Gradle 8.1.3+

### Build Commands
```bash
# Clean build
./gradlew clean build

# Build specific module
./gradlew :core:design:build
./gradlew :feature:projects:build

# Run tests
./gradlew test

# Install debug APK
./gradlew installDebug
```

## Next Steps

### Phase 1: Core Foundation
1. ✅ Set up multi-module architecture
2. ✅ Create core:design with theme system
3. ✅ Create core:domain with models and interfaces
4. ⏳ Implement core:data with Room database
5. ⏳ Create use cases in core:domain

### Phase 2: Feature Development
1. ⏳ Build feature:projects (list, detail, creation)
2. ⏳ Build feature:calendar (views, notifications)
3. ⏳ Build feature:analytics (dashboard, charts)
4. ⏳ Build feature:promotions (hub, CRM)
5. ⏳ Build feature:assistant (AI integration)

### Phase 3: Integration
1. ⏳ Update app navigation to use feature modules
2. ⏳ Implement shared element transitions
3. ⏳ Add WorkManager for notifications
4. ⏳ Performance optimization

### Phase 4: Polish
1. ⏳ Settings and preferences
2. ⏳ Onboarding flow
3. ⏳ Error handling and empty states
4. ⏳ Testing and QA
5. ⏳ Production readiness

## Architecture Benefits

### Modularity
- **Isolated features**: Each feature can be developed independently
- **Parallel development**: Multiple developers can work on different modules
- **Faster builds**: Only modified modules are rebuilt

### Testability
- **Unit testing**: Pure Kotlin domain layer is easy to test
- **UI testing**: Feature modules can be tested in isolation
- **Mock dependencies**: Easy to mock repositories and use cases

### Maintainability
- **Clear boundaries**: Each module has a single responsibility
- **Dependency control**: Enforced dependency rules prevent spaghetti code
- **Reusability**: Core modules can be reused across features

### Scalability
- **Easy to add features**: New features are just new modules
- **Easy to remove features**: Delete a module without affecting others
- **Dynamic delivery**: Modules can be delivered on-demand (future)

## Troubleshooting

### Build Issues
- Ensure all modules have `build.gradle.kts` files
- Check that `settings.gradle.kts` includes all modules
- Verify Hilt plugins are applied correctly
- Clean and rebuild: `./gradlew clean build`

### Dependency Issues
- Check module dependencies in `build.gradle.kts`
- Ensure `:core:domain` has no Android dependencies
- Verify feature modules don't depend on each other

### Hilt Issues
- Ensure `@HiltAndroidApp` is on Application class
- Check that all modules using Hilt have kapt plugin
- Rebuild project after adding Hilt annotations

## Resources

- [Android Multi-Module Architecture](https://developer.android.com/topic/modularization)
- [Hilt Multi-Module Setup](https://developer.android.com/training/dependency-injection/hilt-multi-module)
- [Material 3 Design System](https://m3.material.io/)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
