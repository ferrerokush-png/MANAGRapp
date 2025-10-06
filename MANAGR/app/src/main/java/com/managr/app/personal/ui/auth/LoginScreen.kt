package com.managr.app.personal.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.managr.app.core.design.component.*

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.MusicNote,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "MANAGR",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Manage Your Music Releases",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(48.dp))

        RFTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email",
            placeholder = "artist@example.com",
            leadingIcon = Icons.Default.Email,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        RFTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            placeholder = "••••••••",
            leadingIcon = Icons.Default.Lock,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = "Sign In",
            onClick = {
                isLoading = true
                // Simulate login - replace with actual auth
                onLoginSuccess()
            },
            loading = isLoading,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        SecondaryButton(
            text = "Create Account",
            onClick = onNavigateToSignUp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        RFTextButton(
            text = "Forgot Password?",
            onClick = {}
        )
    }
}

@Composable
fun SignUpScreen(
    onSignUpSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var artistName by remember { mutableStateOf("") }
    var accountType by remember { mutableStateOf("ARTIST") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Create Account",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        RFTextField(
            value = artistName,
            onValueChange = { artistName = it },
            label = "Artist/Label Name",
            placeholder = "Your Name",
            leadingIcon = Icons.Default.Person,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        RFTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email",
            placeholder = "artist@example.com",
            leadingIcon = Icons.Default.Email,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        RFTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            placeholder = "••••••••",
            leadingIcon = Icons.Default.Lock,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Account Type", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FilterChip(
                selected = accountType == "ARTIST",
                onClick = { accountType = "ARTIST" },
                label = { Text("Artist") },
                modifier = Modifier.weight(1f)
            )
            FilterChip(
                selected = accountType == "LABEL",
                onClick = { accountType = "LABEL" },
                label = { Text("Label") },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        PrimaryButton(
            text = "Create Account",
            onClick = {
                isLoading = true
                onSignUpSuccess()
            },
            loading = isLoading,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        RFTextButton(
            text = "Already have an account? Sign in",
            onClick = onNavigateToLogin
        )
    }
}
