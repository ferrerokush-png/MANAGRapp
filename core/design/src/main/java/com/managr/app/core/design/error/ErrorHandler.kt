package com.managr.app.core.design.error

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Global error handler for the application
 */
@Singleton
class ErrorHandler @Inject constructor() {
    private val _errors = MutableSharedFlow<AppError>()
    val errors: SharedFlow<AppError> = _errors.asSharedFlow()
    
    /**
     * Handle an error
     */
    suspend fun handleError(error: AppError) {
        _errors.emit(error)
    }
    
    /**
     * Handle an exception
     */
    suspend fun handleException(exception: Throwable, context: String = "") {
        val appError = when (exception) {
            is NetworkException -> AppError.NetworkError(
                message = exception.message ?: "Network error occurred",
                context = context
            )
            is ValidationException -> AppError.ValidationError(
                message = exception.message ?: "Validation error occurred",
                field = exception.field,
                context = context
            )
            is DatabaseException -> AppError.DatabaseError(
                message = exception.message ?: "Database error occurred",
                context = context
            )
            is AuthenticationException -> AppError.AuthenticationError(
                message = exception.message ?: "Authentication error occurred",
                context = context
            )
            is PermissionException -> AppError.PermissionError(
                message = exception.message ?: "Permission error occurred",
                context = context
            )
            else -> AppError.UnknownError(
                message = exception.message ?: "An unknown error occurred",
                context = context
            )
        }
        
        handleError(appError)
    }
}

/**
 * Application error types
 */
sealed class AppError(
    open val message: String,
    open val context: String = ""
) {
    data class NetworkError(
        override val message: String,
        override val context: String = ""
    ) : AppError(message, context)
    
    data class ValidationError(
        override val message: String,
        val field: String? = null,
        override val context: String = ""
    ) : AppError(message, context)
    
    data class DatabaseError(
        override val message: String,
        override val context: String = ""
    ) : AppError(message, context)
    
    data class AuthenticationError(
        override val message: String,
        override val context: String = ""
    ) : AppError(message, context)
    
    data class PermissionError(
        override val message: String,
        override val context: String = ""
    ) : AppError(message, context)
    
    data class UnknownError(
        override val message: String,
        override val context: String = ""
    ) : AppError(message, context)
}

/**
 * Custom exceptions
 */
class NetworkException(message: String, cause: Throwable? = null) : Exception(message, cause)
class ValidationException(message: String, val field: String? = null) : Exception(message)
class DatabaseException(message: String, cause: Throwable? = null) : Exception(message, cause)
class AuthenticationException(message: String) : Exception(message)
class PermissionException(message: String) : Exception(message)

/**
 * Composable for handling global errors
 */
@Composable
fun ErrorHandler(
    errorHandler: ErrorHandler,
    onError: (AppError) -> Unit
) {
    val errors by errorHandler.errors.collectAsState(initial = null)
    
    LaunchedEffect(errors) {
        errors?.let { error ->
            onError(error)
        }
    }
}

/**
 * Composable for handling errors with snackbar
 */
@Composable
fun ErrorHandlerWithSnackbar(
    errorHandler: ErrorHandler,
    onShowSnackbar: (String, String?) -> Unit
) {
    val errors by errorHandler.errors.collectAsState(initial = null)
    
    LaunchedEffect(errors) {
        errors?.let { error ->
            val actionText = when (error) {
                is AppError.NetworkError -> "Retry"
                is AppError.ValidationError -> "Fix"
                is AppError.DatabaseError -> "Retry"
                is AppError.AuthenticationError -> "Login"
                is AppError.PermissionError -> "Settings"
                is AppError.UnknownError -> "Retry"
            }
            
            onShowSnackbar(error.message, actionText)
        }
    }
}

/**
 * Result wrapper for operations that can fail
 */
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val error: AppError) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

/**
 * Extension functions for Result
 */
fun <T> Result<T>.isSuccess(): Boolean = this is Result.Success
fun <T> Result<T>.isError(): Boolean = this is Result.Error
fun <T> Result<T>.isLoading(): Boolean = this is Result.Loading

fun <T> Result<T>.getData(): T? = if (this is Result.Success) this.data else null
fun <T> Result<T>.getError(): AppError? = if (this is Result.Error) this.error else null

/**
 * Safe execution wrapper
 */
suspend fun <T> safeExecute(
    errorHandler: ErrorHandler,
    context: String = "",
    operation: suspend () -> T
): Result<T> {
    return try {
        Result.Success(operation())
    } catch (e: Exception) {
        errorHandler.handleException(e, context)
        Result.Error(
            when (e) {
                is NetworkException -> AppError.NetworkError(e.message ?: "Network error", context)
                is ValidationException -> AppError.ValidationError(e.message ?: "Validation error", e.field, context)
                is DatabaseException -> AppError.DatabaseError(e.message ?: "Database error", context)
                is AuthenticationException -> AppError.AuthenticationError(e.message ?: "Authentication error", context)
                is PermissionException -> AppError.PermissionError(e.message ?: "Permission error", context)
                else -> AppError.UnknownError(e.message ?: "Unknown error", context)
            }
        )
    }
}

/**
 * Validation helper
 */
object ValidationHelper {
    fun validateEmail(email: String): ValidationResult {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return if (emailRegex.matches(email)) {
            ValidationResult.Valid
        } else {
            ValidationResult.Invalid("Please enter a valid email address")
        }
    }
    
    fun validatePassword(password: String): ValidationResult {
        return when {
            password.length < 8 -> ValidationResult.Invalid("Password must be at least 8 characters long")
            !password.any { it.isDigit() } -> ValidationResult.Invalid("Password must contain at least one number")
            !password.any { it.isLetter() } -> ValidationResult.Invalid("Password must contain at least one letter")
            else -> ValidationResult.Valid
        }
    }
    
    fun validateRequired(value: String, fieldName: String): ValidationResult {
        return if (value.isBlank()) {
            ValidationResult.Invalid("$fieldName is required")
        } else {
            ValidationResult.Valid
        }
    }
    
    fun validateMinLength(value: String, minLength: Int, fieldName: String): ValidationResult {
        return if (value.length < minLength) {
            ValidationResult.Invalid("$fieldName must be at least $minLength characters long")
        } else {
            ValidationResult.Valid
        }
    }
    
    fun validateMaxLength(value: String, maxLength: Int, fieldName: String): ValidationResult {
        return if (value.length > maxLength) {
            ValidationResult.Invalid("$fieldName must be no more than $maxLength characters long")
        } else {
            ValidationResult.Valid
        }
    }
}

/**
 * Validation result
 */
sealed class ValidationResult {
    object Valid : ValidationResult()
    data class Invalid(val message: String) : ValidationResult()
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
            else -> {
                AppError.NetworkError("Network error occurred. Please try again.")
            }
        }
    }
}
