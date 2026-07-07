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
import androidx.compose.ui.graphics.StrokeCap
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
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // AI Monitoring Bars
        item {
            AiMonitoringBar()
            Spacer(modifier = Modifier.height(8.dp))
        }

        // KPI Row
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                KpiBox("Attendance", "${state.attendance}", Modifier.weight(1f))
                KpiBox("Incidents", if(state.surgeMinutes < 10) "1" else "0", Modifier.weight(1f))
                AiScoreBox(state.aiScore, Modifier.weight(1.2f))
            }
        }

        // Emergency Wow Feature
        if (state.surgeMinutes < 10) {
            item {
                EmergencyAlertCard(state.surgeMinutes, onDeploy = { viewModel.resolveEmergency() })
            }
        }

        // Crowd Density with Circular Ring
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                DashboardMetricCard(
                    title = "Crowd Density",
                    value = "${state.gate7Density}%",
                    trend = "+6%",
                    progress = state.gate7Density / 100f,
                    status = if (state.gate7Density > 80) "HIGH" else "NORMAL",
                    statusColor = if (state.gate7Density > 80) ColorCritical else ColorSafe,
                    glowColor = if (state.gate7Density > 80) ColorCriticalDark else ColorSafeDark,
                    modifier = Modifier.weight(1f)
                )
                DashboardMetricCard(
                    title = "Parking Fill",
                    value = "${state.parkingFill}%",
                    trend = "+2%",
                    progress = state.parkingFill / 100f,
                    status = "FILLING",
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
            MonitoringPill("Crowd", 0.7f, true)
            MonitoringPill("Transport", 0.3f, false)
            MonitoringPill("Security", 0.2f, false)
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
                    .fillMaxWidth(fill)
                    .background(if (isHighlighted) ColorAiBlue else GlassBorder, RoundedCornerShape(2.dp))
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(label, fontSize = 12.sp, color = TextSecondary)
    }
}

@Composable
fun KpiBox(title: String, value: String, modifier: Modifier = Modifier) {
    GlassCard(modifier) {
        Column(modifier = Modifier.padding(16.dp).height(60.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(title, style = MaterialTheme.typography.labelMedium, color = TextSecondary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextPrimary)
        }
    }
}

@Composable
fun AiScoreBox(score: Int, modifier: Modifier = Modifier) {
    GlassCard(modifier) {
        Column(modifier = Modifier.padding(16.dp).height(60.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text("AI Score", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
            Spacer(modifier = Modifier.height(8.dp))
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth().height(40.dp)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val strokeWidth = 8.dp.toPx()
                    val diameter = size.height * 2
                    drawArc(
                        brush = Brush.horizontalGradient(listOf(ColorCritical, ColorAttention, ColorSafe)),
                        startAngle = 180f,
                        sweepAngle = 180f,
                        useCenter = false,
                        topLeft = Offset((size.width - diameter)/2, 0f),
                        size = Size(diameter, diameter),
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }
                Text("$score%", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextPrimary, modifier = Modifier.align(Alignment.BottomCenter))
            }
        }
    }
}

@Composable
fun DashboardMetricCard(title: String, value: String, trend: String, progress: Float, status: String, statusColor: Color, glowColor: Color, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        // Glow effect
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(glowColor.copy(alpha = 0.5f), Color.Transparent)
                    )
                )
        )
        Surface(
            color = GlassBg,
            border = androidx.compose.foundation.BorderStroke(1.dp, statusColor.copy(alpha = 0.3f)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(title, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Column {
                        Text(value, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(trend, style = MaterialTheme.typography.labelSmall, color = ColorSafe)
                    }
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(50.dp)) {
                        CircularProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxSize(), color = statusColor, strokeWidth = 4.dp, trackColor = GlassBorder, strokeCap = StrokeCap.Round)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(status, style = MaterialTheme.typography.labelMedium, color = statusColor, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun EmergencyAlertCard(mins: Int, onDeploy: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "shake")
    val offset by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(150, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "shake_offset"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .offset(x = offset.dp)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(ColorCriticalDark.copy(alpha = 0.8f), Color.Transparent),
                    radius = 800f
                )
            )
            .background(GlassBg, RoundedCornerShape(16.dp))
            .border(
                width = 2.dp,
                brush = Brush.verticalGradient(listOf(ColorCritical, ColorCritical.copy(alpha = 0.2f))),
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // A makeshift stadium icon
                Icon(Icons.Default.Stadium, contentDescription = "Alert", tint = ColorCritical, modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text("Crowd Surge Detected", fontWeight = FontWeight.Bold, color = ColorCritical, style = MaterialTheme.typography.titleLarge)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Gate 7", color = TextPrimary, style = MaterialTheme.typography.titleMedium)
                Box(modifier = Modifier.width(1.dp).height(20.dp).background(GlassBorder))
                Text("Expected in $mins mins", color = TextPrimary, style = MaterialTheme.typography.titleMedium)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Confidence", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                Text("96%", color = ColorAiBlue, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = GlassBorder)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Recommended Actions", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = ColorAiPurple, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Open Exit B", color = TextPrimary, style = MaterialTheme.typography.bodyLarge)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = ColorAiPurple, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Deploy 3 Volunteers", color = TextPrimary, style = MaterialTheme.typography.bodyLarge)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onDeploy, 
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(
                        brush = Brush.horizontalGradient(listOf(ColorCritical, ColorHeavy)),
                        shape = RoundedCornerShape(28.dp)
                    ),
                shape = RoundedCornerShape(28.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Text("Deploy & Execute", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
            }
        }
    }
}

@Composable
fun GlassCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(
        color = GlassBg,
        border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        content()
    }
}
