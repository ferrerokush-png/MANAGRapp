package com.managr.app.personal.error

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.managr.app.core.design.components.ErrorSnackbar
import com.managr.app.core.design.components.SuccessSnackbar
import com.managr.app.core.design.components.WarningSnackbar
import com.managr.app.core.design.error.AppError
import com.managr.app.core.design.error.ErrorHandler
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Global exception handler for the application
 */
@Singleton
class GlobalExceptionHandler @Inject constructor(
    private val errorHandler: ErrorHandler
) {
    private val coroutineScope = CoroutineScope(SupervisorJob())
    
    /**
     * Initialize the global exception handler
     */
    fun initialize() {
        // Set up global exception handler for uncaught exceptions
        Thread.setDefaultUncaughtExceptionHandler { thread, exception ->
            Log.e("GlobalExceptionHandler", "Uncaught exception in thread ${thread.name}", exception)
            
            // Handle the exception
            coroutineScope.launch {
                errorHandler.handleException(exception, "GlobalExceptionHandler")
            }
        }
    }
    
    /**
     * Get coroutine exception handler
     */
    fun getCoroutineExceptionHandler(): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, exception ->
            Log.e("GlobalExceptionHandler", "Uncaught exception in coroutine", exception)
            
            coroutineScope.launch {
                errorHandler.handleException(exception, "CoroutineExceptionHandler")
            }
        }
    }
}

/**
 * Composable for handling global errors with snackbar
 */
@Composable
fun GlobalErrorHandler(
    errorHandler: ErrorHandler,
    onShowSnackbar: (String, String?) -> Unit
) {
    val errors by errorHandler.errors.collectAsState(initial = null)
    
    LaunchedEffect(errors) {
        errors?.let { error ->
            val (message, actionText) = when (error) {
                is AppError.NetworkError -> {
                    "Network error: ${error.message}" to "Retry"
                }
                is AppError.ValidationError -> {
                    "Validation error: ${error.message}" to "Fix"
                }
                is AppError.DatabaseError -> {
                    "Database error: ${error.message}" to "Retry"
                }
                is AppError.AuthenticationError -> {
                    "Authentication error: ${error.message}" to "Login"
                }
                is AppError.PermissionError -> {
                    "Permission error: ${error.message}" to "Settings"
                }
                is AppError.UnknownError -> {
                    "Unknown error: ${error.message}" to "Retry"
                }
            }
            
            onShowSnackbar(message, actionText)
        }
    }
}

/**
 * Composable for handling global errors with different snackbar types
 */
@Composable
fun GlobalErrorHandlerWithTypes(
    errorHandler: ErrorHandler,
    onShowSuccessSnackbar: (String) -> Unit,
    onShowErrorSnackbar: (String, String?) -> Unit,
    onShowWarningSnackbar: (String, String?) -> Unit
) {
    val errors by errorHandler.errors.collectAsState(initial = null)
    
    LaunchedEffect(errors) {
        errors?.let { error ->
            when (error) {
                is AppError.NetworkError -> {
                    onShowErrorSnackbar("Network error: ${error.message}", "Retry")
                }
                is AppError.ValidationError -> {
                    onShowWarningSnackbar("Validation error: ${error.message}", "Fix")
                }
                is AppError.DatabaseError -> {
                    onShowErrorSnackbar("Database error: ${error.message}", "Retry")
                }
                is AppError.AuthenticationError -> {
                    onShowErrorSnackbar("Authentication error: ${error.message}", "Login")
                }
                is AppError.PermissionError -> {
                    onShowWarningSnackbar("Permission error: ${error.message}", "Settings")
                }
                is AppError.UnknownError -> {
                    onShowErrorSnackbar("Unknown error: ${error.message}", "Retry")
                }
            }
        }
    }
}

/**
 * Network error handler
 */
object NetworkErrorHandler {
    fun handleNetworkError(throwable: Throwable): AppError {
        return when {
            throwable.message?.contains("timeout", ignoreCase = true) == true -> {
                AppError.NetworkError("Request timed out. Please check your connection and try again.")
            }
            throwable.message?.contains("no internet", ignoreCase = true) == true -> {
                AppError.NetworkError("No internet connection. Please check your network settings.")
            }
            throwable.message?.contains("server error", ignoreCase = true) == true -> {
                AppError.NetworkError("Server error. Please try again later.")
            }
            throwable.message?.contains("not found", ignoreCase = true) == true -> {
                AppError.NetworkError("Resource not found. Please check your request.")
            }
            throwable.message?.contains("unauthorized", ignoreCase = true) == true -> {
                AppError.AuthenticationError("Unauthorized access. Please log in again.")
            }
            throwable.message?.contains("forbidden", ignoreCase = true) == true -> {
                AppError.PermissionError("Access forbidden. Please check your permissions.")
            }
            else -> {
                AppError.NetworkError("Network error occurred. Please try again.")
            }
        }
    }
}

/**
 * Database error handler
 */
object DatabaseErrorHandler {
    fun handleDatabaseError(throwable: Throwable): AppError {
        return when {
            throwable.message?.contains("constraint", ignoreCase = true) == true -> {
                AppError.DatabaseError("Data constraint violation. Please check your input.")
            }
            throwable.message?.contains("not found", ignoreCase = true) == true -> {
                AppError.DatabaseError("Data not found. Please refresh and try again.")
            }
            throwable.message?.contains("duplicate", ignoreCase = true) == true -> {
                AppError.DatabaseError("Duplicate entry. Please check your data.")
            }
            throwable.message?.contains("foreign key", ignoreCase = true) == true -> {
                AppError.DatabaseError("Related data not found. Please check your references.")
            }
            else -> {
                AppError.DatabaseError("Database error occurred. Please try again.")
            }
        }
    }
}

/**
 * Validation error handler
 */
object ValidationErrorHandler {
    fun handleValidationError(throwable: Throwable): AppError {
        return when {
            throwable.message?.contains("email", ignoreCase = true) == true -> {
                AppError.ValidationError("Please enter a valid email address", "email")
            }
            throwable.message?.contains("password", ignoreCase = true) == true -> {
                AppError.ValidationError("Password does not meet requirements", "password")
            }
            throwable.message?.contains("required", ignoreCase = true) == true -> {
                AppError.ValidationError("This field is required", null)
            }
            throwable.message?.contains("length", ignoreCase = true) == true -> {
                AppError.ValidationError("Invalid length. Please check the requirements", null)
            }
            else -> {
                AppError.ValidationError("Validation error occurred. Please check your input.")
            }
        }
    }
}

/**
 * Permission error handler
 */
object PermissionErrorHandler {
    fun handlePermissionError(throwable: Throwable): AppError {
        return when {
            throwable.message?.contains("camera", ignoreCase = true) == true -> {
                AppError.PermissionError("Camera permission is required to take photos")
            }
            throwable.message?.contains("storage", ignoreCase = true) == true -> {
                AppError.PermissionError("Storage permission is required to save files")
            }
            throwable.message?.contains("location", ignoreCase = true) == true -> {
                AppError.PermissionError("Location permission is required for this feature")
            }
            throwable.message?.contains("microphone", ignoreCase = true) == true -> {
                AppError.PermissionError("Microphone permission is required for audio recording")
            }
            else -> {
                AppError.PermissionError("Permission is required for this action")
            }
        }
    }
}

/**
 * Error logging helper
 */
object ErrorLogger {
    fun logError(tag: String, message: String, throwable: Throwable? = null) {
        if (throwable != null) {
            Log.e(tag, message, throwable)
        } else {
            Log.e(tag, message)
        }
    }
    
    fun logWarning(tag: String, message: String, throwable: Throwable? = null) {
        if (throwable != null) {
            Log.w(tag, message, throwable)
        } else {
            Log.w(tag, message)
        }
    }
    
    fun logInfo(tag: String, message: String) {
        Log.i(tag, message)
    }
    
    fun logDebug(tag: String, message: String) {
        Log.d(tag, message)
    }
}

/**
 * Error recovery strategies
 */
object ErrorRecoveryStrategies {
    /**
     * Retry strategy for network errors
     */
    suspend fun retryNetworkOperation(
        maxRetries: Int = 3,
        delayMs: Long = 1000,
        operation: suspend () -> Unit
    ) {
        var retryCount = 0
        var lastException: Throwable? = null
        
        while (retryCount < maxRetries) {
            try {
                operation()
                return // Success
            } catch (e: Exception) {
                lastException = e
                retryCount++
                if (retryCount < maxRetries) {
                    kotlinx.coroutines.delay(delayMs * retryCount) // Exponential backoff
                }
            }
        }
        
        // All retries failed
        throw lastException ?: Exception("Network operation failed after $maxRetries retries")
    }
    
    /**
     * Fallback strategy for database errors
     */
    suspend fun fallbackDatabaseOperation(
        primaryOperation: suspend () -> Unit,
        fallbackOperation: suspend () -> Unit
    ) {
        try {
            primaryOperation()
        } catch (e: Exception) {
            ErrorLogger.logWarning("DatabaseOperation", "Primary operation failed, trying fallback", e)
            try {
                fallbackOperation()
            } catch (fallbackException: Exception) {
                ErrorLogger.logError("DatabaseOperation", "Both primary and fallback operations failed", fallbackException)
                throw fallbackException
            }
        }
    }
}
