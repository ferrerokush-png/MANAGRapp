package com.example.releaseflow.feature.assistant

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.releaseflow.core.design.component.EmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIAssistantScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Assistant") }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            EmptyState(
                title = "AI Assistant",
                description = "AI-powered insights and recommendations coming soon",
                icon = Icons.Default.SmartToy
            )
        }
    }
}
