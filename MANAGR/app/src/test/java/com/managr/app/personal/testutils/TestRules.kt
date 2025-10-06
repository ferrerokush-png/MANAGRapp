package com.managr.app.personal.testutils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * Test rule for coroutines testing
 */
class CoroutineTestRule : TestRule {
    
    private val testDispatcher = StandardTestDispatcher()
    
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                Dispatchers.setMain(testDispatcher)
                try {
                    base.evaluate()
                } finally {
                    Dispatchers.resetMain()
                }
            }
        }
    }
    
    fun runTest(block: suspend TestScope.() -> Unit) {
        testDispatcher.runTest {
            block()
        }
    }
    
    fun advanceTimeBy(delayTimeMillis: Long) {
        testDispatcher.scheduler.advanceTimeBy(delayTimeMillis)
    }
    
    fun advanceUntilIdle() {
        testDispatcher.scheduler.advanceUntilIdle()
    }
}

/**
 * Test rule for database testing
 */
class DatabaseTestRule : TestRule {
    
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                try {
                    base.evaluate()
                } finally {
                    // Clean up database after each test
                    // This would be implemented based on your database setup
                }
            }
        }
    }
}

/**
 * Test rule for network testing
 */
class NetworkTestRule : TestRule {
    
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                try {
                    base.evaluate()
                } finally {
                    // Clean up network mocks after each test
                }
            }
        }
    }
}

/**
 * Test rule for Hilt testing
 */
class HiltTestRule : TestRule {
    
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                try {
                    base.evaluate()
                } finally {
                    // Clean up Hilt components after each test
                }
            }
        }
    }
}

/**
 * Test rule for UI testing
 */
class UITestRule : TestRule {
    
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                try {
                    base.evaluate()
                } finally {
                    // Clean up UI state after each test
                }
            }
        }
    }
}

/**
 * Test rule for file system testing
 */
class FileSystemTestRule : TestRule {
    
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                try {
                    base.evaluate()
                } finally {
                    // Clean up test files after each test
                }
            }
        }
    }
}

/**
 * Test rule for shared preferences testing
 */
class SharedPreferencesTestRule : TestRule {
    
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                try {
                    base.evaluate()
                } finally {
                    // Clean up shared preferences after each test
                }
            }
        }
    }
}

/**
 * Test rule for notification testing
 */
class NotificationTestRule : TestRule {
    
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                try {
                    base.evaluate()
                } finally {
                    // Clean up notifications after each test
                }
            }
        }
    }
}

/**
 * Test rule for permission testing
 */
class PermissionTestRule : TestRule {
    
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                try {
                    base.evaluate()
                } finally {
                    // Clean up permission state after each test
                }
            }
        }
    }
}

/**
 * Test rule for analytics testing
 */
class AnalyticsTestRule : TestRule {
    
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                try {
                    base.evaluate()
                } finally {
                    // Clean up analytics data after each test
                }
            }
        }
    }
}

/**
 * Test rule for error handling testing
 */
class ErrorHandlingTestRule : TestRule {
    
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                try {
                    base.evaluate()
                } finally {
                    // Clean up error state after each test
                }
            }
        }
    }
}

/**
 * Test rule for animation testing
 */
class AnimationTestRule : TestRule {
    
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                try {
                    base.evaluate()
                } finally {
                    // Clean up animation state after each test
                }
            }
        }
    }
}

/**
 * Test rule for theme testing
 */
class ThemeTestRule : TestRule {
    
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                try {
                    base.evaluate()
                } finally {
                    // Clean up theme state after each test
                }
            }
        }
    }
}

/**
 * Test rule for accessibility testing
 */
class AccessibilityTestRule : TestRule {
    
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                try {
                    base.evaluate()
                } finally {
                    // Clean up accessibility state after each test
                }
            }
        }
    }
}

/**
 * Test rule for performance testing
 */
class PerformanceTestRule : TestRule {
    
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                try {
                    base.evaluate()
                } finally {
                    // Clean up performance monitoring after each test
                }
            }
        }
    }
}

/**
 * Test rule for security testing
 */
class SecurityTestRule : TestRule {
    
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                try {
                    base.evaluate()
                } finally {
                    // Clean up security state after each test
                }
            }
        }
    }
}

/**
 * Test rule for integration testing
 */
class IntegrationTestRule : TestRule {
    
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                try {
                    base.evaluate()
                } finally {
                    // Clean up integration state after each test
                }
            }
        }
    }
}

/**
 * Test rule for end-to-end testing
 */
class E2ETestRule : TestRule {
    
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                try {
                    base.evaluate()
                } finally {
                    // Clean up E2E state after each test
                }
            }
        }
    }
}
