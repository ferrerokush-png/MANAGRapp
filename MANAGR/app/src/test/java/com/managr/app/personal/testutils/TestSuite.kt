package com.managr.app.personal.testutils

import org.junit.runner.RunWith
import org.junit.runners.Suite

/**
 * Test suite for all unit tests
 */
@RunWith(Suite::class)
@Suite.SuiteClasses(
    // Domain tests
    ProjectUseCaseTest::class,
    
    // Repository tests
    // ProjectRepositoryTest::class,
    // TaskRepositoryTest::class,
    // AnalyticsRepositoryTest::class,
    
    // ViewModel tests
    // ProjectsViewModelTest::class,
    // TasksViewModelTest::class,
    // AnalyticsViewModelTest::class,
    
    // Utility tests
    // ValidationHelperTest::class,
    // DateUtilsTest::class,
    // StringUtilsTest::class,
    
    // Error handling tests
    // ErrorHandlerTest::class,
    // GlobalExceptionHandlerTest::class,
    
    // Animation tests
    // AnimationExtensionsTest::class,
    // RecyclerViewAnimationsTest::class,
    
    // Component tests
    // FormValidationTest::class,
    // ErrorStatesTest::class,
    // AnimatedComponentsTest::class
)
class UnitTestSuite

/**
 * Test suite for all integration tests
 */
@RunWith(Suite::class)
@Suite.SuiteClasses(
    // Integration tests
    // ProjectIntegrationTest::class,
    // TaskIntegrationTest::class,
    // AnalyticsIntegrationTest::class,
    
    // Database integration tests
    // DatabaseIntegrationTest::class,
    
    // Network integration tests
    // NetworkIntegrationTest::class,
    
    // File system integration tests
    // FileSystemIntegrationTest::class
)
class IntegrationTestSuite

/**
 * Test suite for all UI tests
 */
@RunWith(Suite::class)
@Suite.SuiteClasses(
    // UI tests
    // ProjectListScreenTest::class,
    // ProjectDetailScreenTest::class,
    // TaskListScreenTest::class,
    // AnalyticsScreenTest::class,
    // SettingsScreenTest::class,
    
    // Navigation tests
    // NavigationTest::class,
    
    // Theme tests
    // ThemeTest::class,
    
    // Accessibility tests
    // AccessibilityTest::class
)
class UITestSuite

/**
 * Test suite for all performance tests
 */
@RunWith(Suite::class)
@Suite.SuiteClasses(
    // Performance tests
    // MemoryPerformanceTest::class,
    // CpuPerformanceTest::class,
    // NetworkPerformanceTest::class,
    // DatabasePerformanceTest::class,
    
    // Load tests
    // LoadTest::class,
    
    // Stress tests
    // StressTest::class
)
class PerformanceTestSuite

/**
 * Test suite for all security tests
 */
@RunWith(Suite::class)
@Suite.SuiteClasses(
    // Security tests
    // EncryptionTest::class,
    // AuthenticationTest::class,
    // AuthorizationTest::class,
    // DataProtectionTest::class,
    
    // Vulnerability tests
    // VulnerabilityTest::class
)
class SecurityTestSuite

/**
 * Test suite for all accessibility tests
 */
@RunWith(Suite::class)
@Suite.SuiteClasses(
    // Accessibility tests
    // ScreenReaderTest::class,
    // HighContrastTest::class,
    // LargeTextTest::class,
    // TouchTargetTest::class,
    
    // Navigation tests
    // KeyboardNavigationTest::class,
    // VoiceNavigationTest::class
)
class AccessibilityTestSuite

/**
 * Test suite for all end-to-end tests
 */
@RunWith(Suite::class)
@Suite.SuiteClasses(
    // E2E tests
    // UserJourneyTest::class,
    // CompleteWorkflowTest::class,
    // CrossFeatureTest::class,
    
    // Regression tests
    // RegressionTest::class
)
class E2ETestSuite

/**
 * Master test suite that runs all tests
 */
@RunWith(Suite::class)
@Suite.SuiteClasses(
    UnitTestSuite::class,
    IntegrationTestSuite::class,
    UITestSuite::class,
    PerformanceTestSuite::class,
    SecurityTestSuite::class,
    AccessibilityTestSuite::class,
    E2ETestSuite::class
)
class MasterTestSuite
