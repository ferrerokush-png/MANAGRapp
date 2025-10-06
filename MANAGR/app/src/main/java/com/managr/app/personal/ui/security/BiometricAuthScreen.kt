package com.managr.app.personal.ui.security

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.managr.app.personal.security.BiometricAuthManager
import com.managr.app.personal.security.BiometricPermissionManager
import kotlinx.coroutines.launch

/**
 * Biometric Authentication Screen
 * Handles biometric authentication with proper permission handling for Android 13+
 */
@Composable
fun BiometricAuthScreen(
    biometricAuthManager: BiometricAuthManager,
    biometricPermissionManager: BiometricPermissionManager,
    onAuthSuccess: () -> Unit,
    onAuthFailed: () -> Unit,
    title: String = "Authentication Required",
    subtitle: String = "Verify your identity to continue"
) {
    val context = LocalContext.current
    val activity = context as? FragmentActivity
    val scope = rememberCoroutineScope()
    
    var showPermissionDialog by remember { mutableStateOf(false) }
    var authError by remember { mutableStateOf<String?>(null) }
    
    // Permission launcher for Android 13+
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (allGranted) {
            // Permission granted, proceed with authentication
            activity?.let {
                performBiometricAuth(
                    it,
                    biometricAuthManager,
                    title,
                    subtitle,
                    onSuccess = onAuthSuccess,
                    onError = { _, msg -> authError = msg },
                    onFailed = onAuthFailed
                )
            }
        } else {
            // Permission denied
            authError = "Biometric permission denied"
        }
    }
    
    LaunchedEffect(Unit) {
        scope.launch {
            // Check if permission is needed
            if (!biometricPermissionManager.hasBiometricPermission()) {
                if (activity != null && biometricPermissionManager.shouldShowPermissionRationale(activity)) {
                    showPermissionDialog = true
                } else {
                    // Request permission
                    val permissions = biometricPermissionManager.getRequiredPermissions()
                    if (permissions.isNotEmpty()) {
                        permissionLauncher.launch(permissions)
                    }
                }
            } else {
                // Permission already granted or not required, proceed with auth
                activity?.let {
                    performBiometricAuth(
                        it,
                        biometricAuthManager,
                        title,
                        subtitle,
                        onSuccess = onAuthSuccess,
                        onError = { _, msg -> authError = msg },
                        onFailed = onAuthFailed
                    )
                }
            }
        }
    }
    
    // Permission rationale dialog
    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
            title = { Text("Biometric Permission Required") },
            text = {
                Text(
                    "MANAGR uses biometric authentication to secure your music release data. " +
                    "This keeps your projects, analytics, and sensitive information protected."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showPermissionDialog = false
                        val permissions = biometricPermissionManager.getRequiredPermissions()
                        if (permissions.isNotEmpty()) {
                            permissionLauncher.launch(permissions)
                        }
                    }
                ) {
                    Text("Allow")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showPermissionDialog = false
                    onAuthFailed()
                }) {
                    Text("Deny")
                }
            }
        )
    }
    
    // Show auth screen UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Fingerprint,
            contentDescription = "Biometric",
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        authError?.let { error ->
            Spacer(modifier = Modifier.height(24.dp))
            
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = error,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = {
                    authError = null
                    activity?.let {
                        performBiometricAuth(
                            it,
                            biometricAuthManager,
                            title,
                            subtitle,
                            onSuccess = onAuthSuccess,
                            onError = { _, msg -> authError = msg },
                            onFailed = onAuthFailed
                        )
                    }
                }
            ) {
                Text("Try Again")
            }
        }
    }
}

/**
 * Perform biometric authentication
 */
private fun performBiometricAuth(
    activity: FragmentActivity,
    biometricAuthManager: BiometricAuthManager,
    title: String,
    subtitle: String,
    onSuccess: () -> Unit,
    onError: (Int, String) -> Unit,
    onFailed: () -> Unit
) {
    biometricAuthManager.authenticate(
        activity = activity,
        title = title,
        subtitle = subtitle,
        onSuccess = onSuccess,
        onError = onError,
        onFailed = onFailed
    )
}

/**
 * Composable wrapper for biometric authentication with permission handling
 */
@Composable
fun rememberBiometricAuthState(
    biometricPermissionManager: BiometricPermissionManager,
    biometricAuthManager: BiometricAuthManager
): BiometricAuthState {
    val hasPermission = remember {
        biometricPermissionManager.hasBiometricPermission()
    }
    
    val isAvailable = remember {
        biometricAuthManager.isBiometricAvailable().isAvailable
    }
    
    return BiometricAuthState(
        hasPermission = hasPermission,
        isAvailable = isAvailable,
        canAuthenticate = hasPermission && isAvailable
    )
}

data class BiometricAuthState(
    val hasPermission: Boolean,
    val isAvailable: Boolean,
    val canAuthenticate: Boolean
)


