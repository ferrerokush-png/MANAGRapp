package com.managr.app.core.design.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.managr.app.core.design.animations.AnimationSpecs
import com.managr.app.core.design.error.ValidationHelper
import com.managr.app.core.design.error.ValidationResult

/**
 * Validated text field with error handling
 */
@Composable
fun ValidatedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String,
    placeholder: String = "",
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    enabled: Boolean = true,
    validationRules: List<(String) -> ValidationResult> = emptyList(),
    errorMessage: String? = null,
    isRequired: Boolean = false
) {
    var showError by remember { mutableStateOf(false) }
    var currentError by remember { mutableStateOf<String?>(null) }
    
    // Validate on value change
    LaunchedEffect(value) {
        if (value.isNotEmpty()) {
            val validationResult = validateField(value, validationRules, isRequired)
            currentError = if (validationResult is ValidationResult.Invalid) {
                validationResult.message
            } else null
            showError = currentError != null
        } else {
            currentError = null
            showError = false
        }
    }
    
    // Use provided error message or validation error
    val displayError = errorMessage ?: currentError
    
    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { 
                Text(
                    text = if (isRequired) "$label *" else label,
                    color = if (showError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            placeholder = { Text(placeholder) },
            leadingIcon = leadingIcon?.let { icon ->
                {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (showError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            trailingIcon = trailingIcon?.let { icon ->
                {
                    IconButton(onClick = { onTrailingIconClick?.invoke() }) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (showError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            singleLine = singleLine,
            maxLines = maxLines,
            enabled = enabled,
            isError = showError,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (showError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = if (showError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline,
                errorBorderColor = MaterialTheme.colorScheme.error,
                focusedLabelColor = if (showError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = if (showError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
            ),
            shape = RoundedCornerShape(12.dp)
        )
        
        // Error message
        AnimatedVisibility(
            visible = showError && displayError != null,
            enter = slideInVertically(
                animationSpec = tween(AnimationSpecs.ShortDuration),
                initialOffsetY = { -it }
            ) + fadeIn(animationSpec = tween(AnimationSpecs.ShortDuration)),
            exit = slideOutVertically() + fadeOut()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, start = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Error,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.error
                )
                
                Spacer(modifier = Modifier.width(4.dp))
                
                Text(
                    text = displayError ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

/**
 * Email field with validation
 */
@Composable
fun EmailField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Email",
    placeholder: String = "Enter your email",
    enabled: Boolean = true,
    isRequired: Boolean = true,
    errorMessage: String? = null
) {
    ValidatedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        leadingIcon = Icons.Outlined.Email,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        enabled = enabled,
        isRequired = isRequired,
        validationRules = listOf { ValidationHelper.validateEmail(it) },
        errorMessage = errorMessage
    )
}

/**
 * Password field with validation
 */
@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Password",
    placeholder: String = "Enter your password",
    enabled: Boolean = true,
    isRequired: Boolean = true,
    errorMessage: String? = null
) {
    var showPassword by remember { mutableStateOf(false) }
    
    ValidatedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        leadingIcon = Icons.Outlined.Lock,
        trailingIcon = if (showPassword) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
        onTrailingIconClick = { showPassword = !showPassword },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (showPassword) VisualTransformation.None else androidx.compose.ui.text.input.PasswordVisualTransformation(),
        enabled = enabled,
        isRequired = isRequired,
        validationRules = listOf { ValidationHelper.validatePassword(it) },
        errorMessage = errorMessage
    )
}

/**
 * Required text field
 */
@Composable
fun RequiredTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String,
    placeholder: String = "",
    leadingIcon: ImageVector? = null,
    enabled: Boolean = true,
    errorMessage: String? = null
) {
    ValidatedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        enabled = enabled,
        isRequired = true,
        validationRules = listOf { ValidationHelper.validateRequired(it, label) },
        errorMessage = errorMessage
    )
}

/**
 * Number field with validation
 */
@Composable
fun NumberField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String,
    placeholder: String = "",
    leadingIcon: ImageVector? = null,
    enabled: Boolean = true,
    isRequired: Boolean = true,
    minValue: Int? = null,
    maxValue: Int? = null,
    errorMessage: String? = null
) {
    ValidatedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        enabled = enabled,
        isRequired = isRequired,
        validationRules = listOf { validateNumber(it, minValue, maxValue) },
        errorMessage = errorMessage
    )
}

/**
 * Form validation state
 */
@Composable
fun FormValidationState(
    isValid: Boolean,
    errorMessage: String? = null,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = !isValid && errorMessage != null,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut(),
        modifier = modifier
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Warning,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = errorMessage ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}

/**
 * Validate field with multiple rules
 */
private fun validateField(
    value: String,
    rules: List<(String) -> ValidationResult>,
    isRequired: Boolean
): ValidationResult {
    // Check if required field is empty
    if (isRequired && value.isBlank()) {
        return ValidationResult.Invalid("This field is required")
    }
    
    // If field is empty and not required, it's valid
    if (value.isBlank()) {
        return ValidationResult.Valid
    }
    
    // Check all validation rules
    for (rule in rules) {
        val result = rule(value)
        if (result is ValidationResult.Invalid) {
            return result
        }
    }
    
    return ValidationResult.Valid
}

/**
 * Validate number field
 */
private fun validateNumber(
    value: String,
    minValue: Int?,
    maxValue: Int?
): ValidationResult {
    val number = value.toIntOrNull()
    if (number == null) {
        return ValidationResult.Invalid("Please enter a valid number")
    }
    
    minValue?.let { min ->
        if (number < min) {
            return ValidationResult.Invalid("Value must be at least $min")
        }
    }
    
    maxValue?.let { max ->
        if (number > max) {
            return ValidationResult.Invalid("Value must be no more than $max")
        }
    }
    
    return ValidationResult.Valid
}

/**
 * Form validation helper
 */
object FormValidationHelper {
    fun validateForm(fields: Map<String, String>, rules: Map<String, List<(String) -> ValidationResult>>): Map<String, String?> {
        val errors = mutableMapOf<String, String?>()
        
        for ((fieldName, value) in fields) {
            val fieldRules = rules[fieldName] ?: emptyList()
            val result = validateField(value, fieldRules, true)
            if (result is ValidationResult.Invalid) {
                errors[fieldName] = result.message
            }
        }
        
        return errors
    }
    
    fun isFormValid(fields: Map<String, String>, rules: Map<String, List<(String) -> ValidationResult>>): Boolean {
        return validateForm(fields, rules).isEmpty()
    }
}
