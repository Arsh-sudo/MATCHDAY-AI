package com.example.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.ChatBubble
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.LiveDemoScreen
import com.example.ui.screens.MapScreen
import com.example.viewmodel.MainViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.theme.*
import androidx.compose.animation.core.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(viewModel: MainViewModel = viewModel()) {
    var selectedTab by remember { mutableStateOf(0) }
    
    Box(modifier = Modifier.fillMaxSize().background(BgMain)) {
        // Pitch Background
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 1.dp.toPx()
            val dashEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            val lineColor = GlassBorder.copy(alpha = 0.3f)
            
            // Center circle
            drawCircle(lineColor, radius = size.width / 4, center = center, style = Stroke(strokeWidth, pathEffect = dashEffect))
            // Center line
            drawLine(lineColor, start = Offset(0f, size.height / 2), end = Offset(size.width, size.height / 2), strokeWidth = strokeWidth, pathEffect = dashEffect)
        }

        Scaffold(
            modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing),
            containerColor = Color.Transparent,
            topBar = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("MATCHDAY AI", fontWeight = FontWeight.ExtraBold, color = TextPrimary, fontSize = 24.sp, letterSpacing = 0.5.sp)
                            }
                            Text("Powered by Gemini", color = ColorAiBlue, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        }
                        
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(horizontalAlignment = Alignment.End) {
                                Text("● LIVE", color = ColorSafe, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                                Text("02:08 PM", color = TextSecondary, fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            GlowingIndicator()
                        }
                    }
                }
            },
            bottomBar = {
                // Floating Navigation
                Box(modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, bottom = 24.dp), contentAlignment = Alignment.Center) {
                    NavigationBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(32.dp))
                            .background(Color(0xFF0F172A).copy(alpha = 0.85f))
                            .border(1.dp, GlassBorder, RoundedCornerShape(32.dp)),
                        containerColor = Color.Transparent,
                        contentColor = TextPrimary,
                        tonalElevation = 0.dp
                    ) {
                        NavigationBarItem(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            icon = { Icon(if(selectedTab == 0) Icons.Filled.Analytics else Icons.Outlined.Analytics, contentDescription = "Dashboard") },
                            label = { Text("Home", fontSize = 12.sp, fontWeight = if(selectedTab == 0) FontWeight.Bold else FontWeight.Normal) },
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
                            label = { Text("Stadium", fontSize = 12.sp, fontWeight = if(selectedTab == 1) FontWeight.Bold else FontWeight.Normal) },
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
                            label = { Text("AI Copilot", fontSize = 12.sp, fontWeight = if(selectedTab == 2) FontWeight.Bold else FontWeight.Normal) },
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

    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(20.dp)) {
        Box(modifier = Modifier.size(20.dp).clip(CircleShape).background(ColorSafeDark))
        Box(modifier = Modifier.size(14.dp).clip(CircleShape).background(ColorSafe.copy(alpha = alpha * 0.4f)))
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(ColorSafe))
    }
}
