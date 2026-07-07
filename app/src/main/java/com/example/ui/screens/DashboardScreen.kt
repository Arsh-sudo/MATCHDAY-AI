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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // AI Monitoring Bars
        item {
            AiMonitoringBar()
            Spacer(modifier = Modifier.height(16.dp))
        }

        // KPI Row
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                KpiBox("Attendance", "${state.attendance}", Modifier.weight(1f))
                KpiBox("Incidents", if(state.surgeMinutes < 10) "1" else "0", Modifier.weight(1f))
                KpiBox("AI Score", "${state.aiScore}%", Modifier.weight(1f), isAi = true)
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
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                DashboardMetricCard(
                    title = "Crowd Density",
                    value = "${state.gate7Density}%",
                    trend = "+6%",
                    progress = state.gate7Density / 100f,
                    status = if (state.gate7Density > 80) "HIGH" else "NORMAL",
                    statusColor = if (state.gate7Density > 80) ColorCritical else ColorSafe,
                    modifier = Modifier.weight(1f)
                )
                DashboardMetricCard(
                    title = "Parking Fill",
                    value = "${state.parkingFill}%",
                    trend = "+2%",
                    progress = state.parkingFill / 100f,
                    status = "FILLING",
                    statusColor = ColorAttention,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Transport Intelligence
        item {
            GlassCard {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Transport Intelligence", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        TransportItem("Metro", "${state.transitWaitMins} min", if(state.transitWaitMins > 5) ColorAttention else ColorSafe)
                        TransportItem("Ride Share", "Heavy", ColorHeavy)
                        TransportItem("Bus", "On Time", ColorSafe)
                    }
                }
            }
        }

        // Weather Card
        item {
            GlassCard {
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("Weather", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("${state.temperature}°C", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Humidity: ${state.humidity}%", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                        Text("Rain: 12%", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                        Text("Impact: Low", style = MaterialTheme.typography.bodyMedium, color = ColorSafe)
                    }
                }
            }
        }
    }
}

@Composable
fun AiMonitoringBar() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "pulse_alpha"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("AI Monitoring", style = MaterialTheme.typography.labelMedium, color = ColorAiBlue, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            MonitoringPill("Crowd", 0.7f, alpha)
            MonitoringPill("Transport", 0.9f, alpha)
            MonitoringPill("Security", 0.4f, alpha)
            MonitoringPill("Weather", 0.8f, alpha)
        }
    }
}

@Composable
fun MonitoringPill(label: String, fill: Float, alpha: Float) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .width(60.dp)
                .height(4.dp)
                .background(GlassBg, RoundedCornerShape(2.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fill)
                    .background(ColorAiBlue.copy(alpha = alpha), RoundedCornerShape(2.dp))
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, fontSize = 10.sp, color = TextSecondary)
    }
}

@Composable
fun KpiBox(title: String, value: String, modifier: Modifier = Modifier, isAi: Boolean = false) {
    Surface(
        color = if (isAi) ColorAiPurple.copy(alpha = 0.1f) else GlassBg,
        border = androidx.compose.foundation.BorderStroke(1.dp, if (isAi) ColorAiPurple.copy(alpha = 0.3f) else GlassBorder),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = if (isAi) ColorAiPurple else TextPrimary)
        }
    }
}

@Composable
fun DashboardMetricCard(title: String, value: String, trend: String, progress: Float, status: String, statusColor: Color, modifier: Modifier = Modifier) {
    GlassCard(modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleSmall, color = TextSecondary)
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text(trend, style = MaterialTheme.typography.labelSmall, color = ColorSafe)
                }
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(50.dp)) {
                    CircularProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxSize(), color = statusColor, strokeWidth = 4.dp, trackColor = GlassBg)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(status, style = MaterialTheme.typography.labelMedium, color = statusColor, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun EmergencyAlertCard(mins: Int, onDeploy: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "shake")
    val offset by infiniteTransition.animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(100, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "shake_offset"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .offset(x = offset.dp),
        colors = CardDefaults.cardColors(containerColor = ColorCritical.copy(alpha = 0.15f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, ColorCritical.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Warning, contentDescription = "Alert", tint = ColorCritical)
                Spacer(modifier = Modifier.width(8.dp))
                Text("⚠ Crowd Surge Detected", fontWeight = FontWeight.Bold, color = ColorCritical, style = MaterialTheme.typography.titleMedium)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Gate 7", color = TextPrimary)
                Text("Expected in $mins mins", color = ColorCritical, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Confidence", color = TextSecondary, style = MaterialTheme.typography.labelMedium)
                Text("96%", color = ColorAiBlue, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("Recommended Actions", color = TextSecondary, style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = ColorAiPurple, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Open Exit B", color = TextPrimary, style = MaterialTheme.typography.bodyMedium)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = ColorAiPurple, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Deploy 3 Volunteers", color = TextPrimary, style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onDeploy, 
                colors = ButtonDefaults.buttonColors(containerColor = ColorCritical),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Deploy & Execute")
            }
        }
    }
}

@Composable
fun TransportItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = TextSecondary)
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
fun GlassCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(
        color = GlassBg,
        border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        content()
    }
}
