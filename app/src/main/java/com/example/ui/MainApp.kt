package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.ChatBubble
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.LiveDemoScreen
import com.example.ui.screens.MapScreen
import com.example.ui.screens.AlertsScreen
import com.example.viewmodel.MainViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.theme.*
import androidx.compose.animation.core.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(viewModel: MainViewModel = viewModel()) {
    var selectedTab by remember { mutableStateOf(0) }
    var showClearConfirm by remember { mutableStateOf(false) }
    
    if (showClearConfirm) {
        AlertDialog(
            onDismissRequest = { showClearConfirm = false },
            title = { Text("Clear chat history?") },
            text = { Text("This cannot be undone.") },
            confirmButton = {
                TextButton(onClick = { viewModel.clearHistory(); showClearConfirm = false }) {
                    Text("Clear")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirm = false }) { Text("Cancel") }
            }
        )
    }
    
    Scaffold(
        modifier = Modifier.fillMaxSize().background(BgMain).windowInsetsPadding(WindowInsets.safeDrawing),
        containerColor = Color.Transparent,
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("MATCHDAY AI", fontWeight = FontWeight.ExtraBold, color = TextPrimary, fontSize = 20.sp, letterSpacing = 1.sp)
                        }
                        Text("Powered by Gemini", color = ColorAiPurple, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(horizontalAlignment = Alignment.End) {
                            Text("LIVE", color = ColorSafe, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                            Text("02:08 PM", color = TextPrimary, fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        GlowingIndicator()
                    }
                }
            }
        },
        bottomBar = {
            // Floating Navigation
            Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                NavigationBar(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .clip(CircleShape)
                        .background(GlassBg)
                        .border(1.dp, GlassBorder, CircleShape),
                    containerColor = Color.Transparent,
                    contentColor = TextPrimary,
                    tonalElevation = 0.dp
                ) {
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        icon = { Icon(if(selectedTab == 0) Icons.Filled.Analytics else Icons.Outlined.Analytics, contentDescription = "Dashboard") },
                        label = { if(selectedTab == 0) Text("Home", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ColorAiBlue,
                            unselectedIconColor = TextSecondary,
                            indicatorColor = Color.Transparent
                        )
                    )
                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = { Icon(if(selectedTab == 1) Icons.Filled.Map else Icons.Outlined.Map, contentDescription = "Map") },
                        label = { if(selectedTab == 1) Text("Stadium", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ColorAiBlue,
                            unselectedIconColor = TextSecondary,
                            indicatorColor = Color.Transparent
                        )
                    )
                    NavigationBarItem(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        icon = { Icon(if(selectedTab == 2) Icons.Filled.ChatBubble else Icons.Outlined.ChatBubble, contentDescription = "AI") },
                        label = { if(selectedTab == 2) Text("AI Copilot", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ColorAiPurple,
                            unselectedIconColor = TextSecondary,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BgMain)
                .padding(paddingValues)
        ) {
            when (selectedTab) {
                0 -> DashboardScreen(viewModel)
                1 -> MapScreen(viewModel)
                2 -> LiveDemoScreen(viewModel)
            }
        }
    }
}

@Composable
fun GlowingIndicator() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "pulse_alpha"
    )

    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(16.dp)) {
        Box(modifier = Modifier.size(16.dp).clip(CircleShape).background(ColorSafe.copy(alpha = alpha * 0.4f)))
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(ColorSafe))
    }
}
