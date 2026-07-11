package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.viewmodel.MainViewModel
import com.example.ui.theme.*

@Composable
fun AlertsScreen(viewModel: MainViewModel, onBack: () -> Unit, onNavigateToFeeds: () -> Unit) {
    val state by viewModel.dashboardState.collectAsStateWithLifecycle()

    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isVisible = true
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 120.dp)
    ) {
        item {
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(tween(800)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ChevronLeft, contentDescription = "Back", tint = TextPrimary)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("ALERTS CENTER", style = MaterialTheme.typography.titleMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
                            Text("Live Notifications", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
                        }
                        Spacer(modifier = Modifier.width(48.dp))
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }

        if (state.activeIncidentsCount > 0) {
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInHorizontally(initialOffsetX = { -it }) + fadeIn(tween(800, delayMillis = 200)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    EmergencyAlertCard(mins = state.surgeMinutes, onDeploy = { viewModel.resolveEmergency() }, onNavigateToFeeds = onNavigateToFeeds)
                }
            }
        } else {
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInHorizontally(initialOffsetX = { -it }) + fadeIn(tween(800, delayMillis = 200)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .liquidGlass(
                                shape = RoundedCornerShape(24.dp),
                                bgStartColor = ColorSafeDark.copy(alpha = 0.35f),
                                bgEndColor = ColorSafeDark.copy(alpha = 0.15f),
                                topBorderColor = ColorSafe.copy(alpha = 0.6f),
                                bottomBorderColor = ColorSafe.copy(alpha = 0.1f)
                            )
                    ) {
                        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier.size(56.dp).background(ColorSafe.copy(alpha = 0.15f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("✓", color = ColorSafe, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("No Active Gate Alerts", fontWeight = FontWeight.Bold, color = ColorSafe, style = MaterialTheme.typography.titleLarge)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("All egress pathways and terminal turnstiles are operating within safe baseline densities.", color = TextSecondary, style = MaterialTheme.typography.bodyMedium, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                        }
                    }
                }
            }
        }

        item {
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(tween(800, delayMillis = 300)),
                modifier = Modifier.fillMaxWidth()
            ) {
                AlertCard(
                    emoji = "🚇",
                    title = "Metro Delay",
                    subtitle = "Line 3 - South Station",
                    severityColor = ColorAttention,
                    severityColorDark = ColorAttentionDark,
                    details = "Delay: 8 mins   •   Impact: Moderate",
                    actionText = "Update digital signage",
                    onNavigateToFeeds = onNavigateToFeeds
                )
            }
        }

        item {
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(tween(800, delayMillis = 400)),
                modifier = Modifier.fillMaxWidth()
            ) {
                AlertCard(
                    emoji = "🌧️",
                    title = "Weather Update",
                    subtitle = "Light Rain Expected",
                    severityColor = ColorSafe,
                    severityColorDark = ColorSafeDark,
                    details = "Duration: Next 2 hours",
                    actionText = "Close roof",
                    onNavigateToFeeds = onNavigateToFeeds
                )
            }
        }
    }
}

@Composable
fun AlertCard(
    emoji: String,
    title: String, 
    subtitle: String, 
    severityColor: Color, 
    severityColorDark: Color, 
    details: String, 
    actionText: String, 
    onNavigateToFeeds: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .liquidGlass(
                shape = RoundedCornerShape(20.dp),
                bgStartColor = severityColor.copy(alpha = 0.5f),
                bgEndColor = severityColorDark.copy(alpha = 0.3f),
                topBorderColor = severityColor.copy(alpha = 0.6f),
                bottomBorderColor = severityColorDark.copy(alpha = 0.1f)
            )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(), 
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Text(emoji, fontSize = 32.sp)
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(title, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(subtitle, color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    }
                }
                
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color.White.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.ChevronRight, contentDescription = "More", tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(details, color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp, fontWeight = FontWeight.Medium)
            
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.15f))
            Spacer(modifier = Modifier.height(16.dp))
            
            Text("Suggested Action", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(actionText, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            
            Spacer(modifier = Modifier.height(20.dp))
            
            OutlinedButton(
                onClick = onNavigateToFeeds, 
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("View Live Camera", fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
fun EmergencyAlertCard(mins: Int, onDeploy: () -> Unit, onNavigateToFeeds: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .liquidGlass(
                shape = RoundedCornerShape(24.dp),
                borderWidth = 1.dp,
                bgStartColor = ColorCritical.copy(alpha = 0.7f),
                bgEndColor = ColorCriticalDark.copy(alpha = 0.5f),
                topBorderColor = ColorCritical.copy(alpha = 0.8f),
                bottomBorderColor = ColorCriticalDark.copy(alpha = 0.3f)
            )
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // White rounded box with warning icon
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = "Alert", tint = ColorCritical, modifier = Modifier.size(28.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Crowd Surge Detected", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Gate 7", color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp)
                    }
                }
                
                // High Priority Tag
                Box(
                    modifier = Modifier
                        .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text("HIGH PRIORITY", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text("Expected in $mins mins", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Confidence: 95%", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
            
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onDeploy, 
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text("Deploy & Execute", fontWeight = FontWeight.Bold, color = ColorCritical)
                }
                
                OutlinedButton(
                    onClick = onNavigateToFeeds,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White),
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text("View CCTV", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
