package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.viewmodel.MainViewModel
import com.example.ui.theme.*

@Composable
fun MatchOverviewScreen(viewModel: MainViewModel, onBack: () -> Unit) {
    val state by viewModel.dashboardState.collectAsStateWithLifecycle()

    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isVisible = true
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
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
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ChevronLeft, contentDescription = "Back", tint = TextPrimary)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("MATCH OVERVIEW", style = MaterialTheme.typography.titleMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
                            Text("Live Match & Operations", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
                        }
                        Spacer(modifier = Modifier.width(48.dp))
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }

        // Scoreboard
        item {
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(tween(800, delayMillis = 200)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .liquidGlass()
                        .padding(vertical = 24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(modifier = Modifier.size(64.dp).background(Color.White, CircleShape), contentAlignment = Alignment.Center) {
                                Text("🇲🇽", fontSize = 36.sp)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("MEX", color = TextSecondary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("${state.mexScore} - ${state.braScore}", color = TextPrimary, fontSize = 48.sp, fontWeight = FontWeight.Bold)
                            Box(
                                modifier = Modifier
                                    .liquidGlass(
                                        shape = RoundedCornerShape(12.dp),
                                        bgStartColor = ColorSafeDark.copy(alpha = 0.5f),
                                        bgEndColor = ColorSafeDark.copy(alpha = 0.2f),
                                        topBorderColor = ColorSafe.copy(alpha = 0.6f),
                                        bottomBorderColor = ColorSafe.copy(alpha = 0.1f)
                                    )
                            ) {
                                val formattedSeconds = String.format("%02d", state.matchSeconds)
                                Text("${state.matchMinutes}:$formattedSeconds", color = ColorSafe, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp))
                            }
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(modifier = Modifier.size(64.dp).background(Color.White, CircleShape), contentAlignment = Alignment.Center) {
                                Text("🇧🇷", fontSize = 36.sp)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("BRA", color = TextSecondary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Match Stats
        item {
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInHorizontally(initialOffsetX = { -it }) + fadeIn(tween(800, delayMillis = 300)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .liquidGlass()
                        .padding(20.dp)
                ) {
                    Column {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("MATCH STATS", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            StatRing("POSSESSION", "${state.mexPossessionPct}%", state.mexPossessionPct / 100f, ColorSafe)
                            StatRing("ATTEMPTS", "${state.mexShots}", (state.mexShots / 15f).coerceAtMost(1f), ColorAiBlue)
                            StatRing("CORNERS", "${state.mexCorners}", (state.mexCorners / 12f).coerceAtMost(1f), ColorAiPurple)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Operations Snapshot
        item {
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(tween(800, delayMillis = 400)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .liquidGlass()
                        .padding(20.dp)
                ) {
                    Column {
                        val hasIncident = state.activeIncidentsCount > 0
                        Text("OPERATIONS SNAPSHOT", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        Text(if (hasIncident) "Active Gate Incident Detected" else "All Systems Operational", color = if (hasIncident) ColorCritical else TextSecondary, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        SnapshotItem("Security", if (hasIncident) "Alert" else "Normal", if (hasIncident) ColorCritical else ColorSafe)
                        SnapshotItem("Gate Flow", if (hasIncident) "Congested" else "Good", if (hasIncident) ColorCritical else ColorSafe)
                        SnapshotItem("Transportation", "Good", ColorSafe)
                        SnapshotItem("Medical", "Normal", ColorSafe)
                    }
                }
            }
        }
    }
}

@Composable
fun StatRing(label: String, value: String, progress: Float, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = TextSecondary, fontSize = 10.sp, letterSpacing = 1.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(80.dp)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 8.dp.toPx()
                drawArc(
                    color = Color(0xFF1E293B),
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = strokeWidth)
                )
                drawArc(
                    brush = Brush.sweepGradient(listOf(color.copy(alpha = 0.2f), color)),
                    startAngle = -90f,
                    sweepAngle = 360f * progress,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }
            Text(value, color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.width(80.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("${(progress*100).toInt()}%", color = color, fontSize = 10.sp)
            Text("${100 - (progress*100).toInt()}%", color = TextSecondary, fontSize = 10.sp)
        }
    }
}

@Composable
fun SnapshotItem(label: String, status: String, statusColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = TextPrimary, fontSize = 14.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(status, color = statusColor, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(statusColor))
        }
    }
}
