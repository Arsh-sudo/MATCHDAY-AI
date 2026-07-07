package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Stadium
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.viewmodel.MainViewModel
import com.example.ui.theme.*

@Composable
fun DashboardScreen(viewModel: MainViewModel) {
    val state by viewModel.dashboardState.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 120.dp)
    ) {
        // AI Monitoring Bars
        item {
            AiMonitoringBar()
            Spacer(modifier = Modifier.height(4.dp))
        }

        // KPI Row
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                KpiBox("Attendance", "58225", Modifier.weight(1f))
                KpiBox("Incidents", "1", Modifier.weight(1f))
                AiScoreBox(99, Modifier.weight(1.2f))
            }
        }

        // Emergency Wow Feature
        item {
            EmergencyAlertCard(6, onDeploy = { viewModel.resolveEmergency() })
        }

        // Crowd Density with Circular Ring
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                DashboardMetricCard(
                    title = "Crowd Density",
                    value = "80%",
                    trend = "Trending Up +6%",
                    progress = 0.80f,
                    status = "NORMAL",
                    statusColor = ColorSafe,
                    glowColor = ColorSafeDark,
                    modifier = Modifier.weight(1f)
                )
                DashboardMetricCard(
                    title = "Parking Fill",
                    value = "76%",
                    trend = "+2%",
                    progress = 0.76f,
                    status = "FILLING (76%)",
                    statusColor = ColorAttention,
                    glowColor = ColorAttentionDark,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun AiMonitoringBar() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Live Stadium Insights", style = MaterialTheme.typography.titleLarge, color = TextPrimary, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            MonitoringPill("Crowd", 1f, true)
            MonitoringPill("Transport", 0.3f, false)
            MonitoringPill("Security", 0.3f, false)
            MonitoringPill("Weather", 0.6f, false)
        }
    }
}

@Composable
fun MonitoringPill(label: String, fill: Float, isHighlighted: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(70.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(GlassBg, RoundedCornerShape(2.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(if(isHighlighted) 1f else fill)
                    .background(if (isHighlighted) ColorAiBlue else GlassBorder, RoundedCornerShape(2.dp))
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(label, fontSize = 12.sp, color = if(isHighlighted) TextPrimary else TextSecondary)
    }
}

@Composable
fun KpiBox(title: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        color = Color(0xFF1E293B).copy(alpha = 0.6f),
        border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(vertical = 16.dp).height(64.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(title, style = MaterialTheme.typography.labelMedium, color = TextSecondary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextPrimary)
        }
    }
}

@Composable
fun AiScoreBox(score: Int, modifier: Modifier = Modifier) {
    Surface(
        color = Color(0xFF1E293B).copy(alpha = 0.6f),
        border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(vertical = 16.dp).height(64.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text("AI Score", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
            Spacer(modifier = Modifier.height(8.dp))
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth().height(40.dp)) {
                Canvas(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                    val strokeWidth = 8.dp.toPx()
                    val diameter = size.width
                    drawArc(
                        brush = Brush.horizontalGradient(listOf(ColorSafe, ColorSafe, ColorAttention)),
                        startAngle = 180f,
                        sweepAngle = 180f,
                        useCenter = false,
                        topLeft = Offset(0f, 0f),
                        size = Size(diameter, diameter),
                        style = Stroke(width = strokeWidth, cap = androidx.compose.ui.graphics.StrokeCap.Round)
                    )
                }
                Text("$score%", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextPrimary, modifier = Modifier.align(Alignment.BottomCenter).offset(y = 4.dp))
            }
        }
    }
}

@Composable
fun DashboardMetricCard(title: String, value: String, trend: String, progress: Float, status: String, statusColor: Color, glowColor: Color, modifier: Modifier = Modifier) {
    Box(modifier = modifier.height(140.dp)) {
        // Outer Glow effect
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(glowColor.copy(alpha = 0.5f), Color.Transparent),
                        radius = 300f
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
        )
        Surface(
            color = Color(0xFF1E293B).copy(alpha = 0.85f),
            border = androidx.compose.foundation.BorderStroke(1.dp, statusColor.copy(alpha = 0.3f)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(title, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Column {
                        Text(value, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(trend, style = MaterialTheme.typography.labelSmall, color = statusColor)
                    }
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(50.dp)) {
                        CircularProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxSize(), color = statusColor, strokeWidth = 6.dp, trackColor = Color(0xFF0F172A), strokeCap = androidx.compose.ui.graphics.StrokeCap.Round)
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(status, style = MaterialTheme.typography.labelMedium, color = statusColor, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun EmergencyAlertCard(mins: Int, onDeploy: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "pulse_alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(ColorCriticalDark.copy(alpha = glowAlpha), Color.Transparent),
                    radius = 800f
                )
            )
            .background(Color(0xFF1E293B).copy(alpha = 0.85f), RoundedCornerShape(24.dp))
            .border(
                width = 2.dp,
                brush = Brush.verticalGradient(listOf(ColorCritical, ColorCritical.copy(alpha = 0.2f))),
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        // Top-right red dot
        Box(modifier = Modifier.align(Alignment.TopEnd).padding(16.dp).size(8.dp).background(ColorCritical, RoundedCornerShape(4.dp)))
        
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Stadium, contentDescription = "Alert", tint = ColorCritical, modifier = Modifier.size(48.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Text("Crowd Surge Detected", fontWeight = FontWeight.Bold, color = ColorCritical, style = MaterialTheme.typography.headlineSmall)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Gate 7", color = TextPrimary, style = MaterialTheme.typography.titleLarge)
                Box(modifier = Modifier.width(1.dp).height(24.dp).background(GlassBorder))
                Text("Expected in $mins mins", color = TextPrimary, style = MaterialTheme.typography.titleLarge)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Confidence", color = TextSecondary, style = MaterialTheme.typography.bodyLarge)
                Text("96%", color = ColorAiBlue, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
            }
            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(color = GlassBorder)
            Spacer(modifier = Modifier.height(24.dp))
            Text("Recommended Actions", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = ColorAiPurple, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text("Open Exit B", color = TextPrimary, style = MaterialTheme.typography.bodyLarge)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = ColorAiPurple, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text("Deploy 3 Volunteers", color = TextPrimary, style = MaterialTheme.typography.bodyLarge)
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = onDeploy, 
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .background(
                        brush = Brush.horizontalGradient(listOf(ColorCritical, ColorHeavy)),
                        shape = RoundedCornerShape(32.dp)
                    ),
                shape = RoundedCornerShape(32.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Text("Deploy & Execute", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
            }
        }
    }
}
