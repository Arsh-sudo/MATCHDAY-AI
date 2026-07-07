package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.viewmodel.MainViewModel
import com.example.ui.theme.*

@Composable
fun LiveDemoScreen(viewModel: MainViewModel) {
    val messages by viewModel.messages.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val typingStatus by viewModel.typingStatus.collectAsStateWithLifecycle()
    
    var textState by remember { mutableStateOf("") }
    
    val quickPrompts = listOf(
        "Show Crowd", "Traffic Status", "Medical Emergencies", 
        "Open Incidents", "Security Risks", "Volunteer Allocation"
    )

    Column(modifier = Modifier.fillMaxSize()) {
        // Top AI branding
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = ColorAiPurple)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Gemini Operations Assistant", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = ColorAiBlue)
        }

        // Chat Area
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 8.dp),
            reverseLayout = true
        ) {
            if (isLoading && typingStatus.isNotBlank()) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = GlassBg,
                            border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
                        ) {
                            Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = ColorAiPurple)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(typingStatus, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                            }
                        }
                    }
                }
            }
            items(messages.reversed()) { message ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
                ) {
                    Box(
                        modifier = Modifier
                            .widthIn(max = 320.dp)
                            .background(
                                color = when {
                                    message.isError -> MaterialTheme.colorScheme.errorContainer
                                    message.isUser -> UserBubbleBg
                                    else -> GlassBg
                                },
                                shape = RoundedCornerShape(20.dp)
                            )
                            .then(
                                if (!message.isUser && !message.isError) {
                                    Modifier.border(1.dp, GlassBorder, RoundedCornerShape(20.dp))
                                } else Modifier
                            )
                            .padding(16.dp)
                    ) {
                        Text(
                            text = message.text,
                            color = when {
                                message.isError -> MaterialTheme.colorScheme.onErrorContainer
                                message.isUser -> UserBubbleText
                                else -> TextPrimary
                            }
                        )
                    }
                }
            }
        }
        
        // Quick Prompts
        LazyRow(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(quickPrompts) { prompt ->
                AssistChip(
                    onClick = { viewModel.sendMessage(prompt) },
                    label = { Text(prompt) },
                    enabled = !isLoading,
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = GlassBg,
                        labelColor = TextPrimary
                    ),
                    border = AssistChipDefaults.assistChipBorder(borderColor = GlassBorder, enabled = true)
                )
            }
        }

        // Input Area
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = textState,
                onValueChange = { textState = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Ask MATCHDAY AI...") },
                shape = RoundedCornerShape(24.dp),
                enabled = !isLoading,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = BgCardSecondary,
                    unfocusedContainerColor = BgCardSecondary,
                    focusedBorderColor = ColorAiBlue,
                    unfocusedBorderColor = BorderColor
                )
            )
            Spacer(modifier = Modifier.width(12.dp))
            FloatingActionButton(
                onClick = {
                    if (!isLoading && textState.isNotBlank()) {
                        viewModel.sendMessage(textState)
                        textState = ""
                    }
                },
                modifier = Modifier.size(56.dp),
                containerColor = Color.Transparent,
                elevation = FloatingActionButtonDefaults.elevation(0.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize().background(
                    Brush.linearGradient(listOf(ColorAiGradientStart, ColorAiGradientEnd)),
                    shape = RoundedCornerShape(28.dp)
                ), contentAlignment = Alignment.Center) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = Color.White)
                }
            }
        }
    }
}
