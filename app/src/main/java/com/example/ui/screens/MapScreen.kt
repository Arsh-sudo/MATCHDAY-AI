package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.viewmodel.MainViewModel
import com.example.ui.theme.*

class GateInfo(
    val name: String,
    val status: String,
    val statusColor: Color,
    val isWarning: Boolean,
    val expectedPeak: String,
    val confidence: String,
    val people: String,
    val waitTime: String,
    val volunteers: String,
    val exits: String,
    val trendPoints: List<Float>
)

@Composable
fun MapScreen(viewModel: MainViewModel, onBack: () -> Unit, onNavigateToFeeds: () -> Unit) {
    var selectedGate by remember { mutableStateOf("GATE 7") }

    val state by viewModel.dashboardState.collectAsStateWithLifecycle()

    val gateData = state.gates.mapValues { (_, gateState) ->
        val statusColor = when (gateState.status) {
            "HIGH CONGESTION" -> ColorCritical
            "MODERATE CONGESTION" -> ColorAttention
            else -> ColorSafe
        }
        GateInfo(
            name = gateState.name,
            status = gateState.status,
            statusColor = statusColor,
            isWarning = gateState.isWarning,
            expectedPeak = gateState.expectedPeak,
            confidence = gateState.confidence,
            people = gateState.people,
            waitTime = gateState.waitTime,
            volunteers = gateState.volunteers,
            exits = gateState.exits,
            trendPoints = gateState.trendPoints
        )
    }

    val currentGateInfo = gateData[selectedGate] ?: GateInfo("GATE 7", "NORMAL DENSITY", ColorSafe, false, "Stable flow expected", "Confidence 96%", "0", "0 min", "0", "4/4", listOf(0f))

    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isVisible = true
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 120.dp)
    ) {
        // Custom Header with Back Button and Settings
        item {
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(tween(800)),
                modifier = Modifier.fillMaxWidth()
            ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ChevronLeft, contentDescription = "Back", tint = TextPrimary)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "STADIUM DIGITAL TWIN",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        "Live Operational View",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextSecondary
                    )
                }
                Spacer(modifier = Modifier.width(48.dp))
            }
            }
        }

        // Stadium Visual Map
        item {
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInHorizontally(initialOffsetX = { -it }) + fadeIn(tween(800, delayMillis = 200)),
                modifier = Modifier.fillMaxWidth()
            ) {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center
            ) {
                val width = maxWidth
                val height = maxHeight

                // Background Image
                androidx.compose.foundation.Image(
                    painter = androidx.compose.ui.res.painterResource(id = com.example.R.drawable.stadium_top_down_1783435115330),
                    contentDescription = "Stadium Map",
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(24.dp))
                )

                // Vignette overlay to blend edges with background
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.radialGradient(
                                colors = listOf(Color.Transparent, BgMain.copy(alpha = 0.3f), BgMain),
                                radius = 450f
                            )
                        )
                )

                // Canvas for drawing connection lines and glowing dots
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    val cx = w / 2f
                    val cy = h / 2f

                    val gatePositions = listOf(
                        Triple(0.50f, 0.16f, gateData["GATE 1"]?.statusColor ?: ColorSafe),   // GATE 1
                        Triple(0.76f, 0.22f, gateData["GATE 2"]?.statusColor ?: ColorSafe),   // GATE 2
                        Triple(0.88f, 0.48f, gateData["GATE 3"]?.statusColor ?: ColorSafe),   // GATE 3
                        Triple(0.50f, 0.82f, gateData["GATE 4"]?.statusColor ?: ColorSafe),   // GATE 4
                        Triple(0.12f, 0.45f, gateData["GATE 5"]?.statusColor ?: ColorSafe),   // GATE 5
                        Triple(0.20f, 0.74f, gateData["GATE 6"]?.statusColor ?: ColorSafe),   // GATE 6
                        Triple(0.22f, 0.18f, gateData["GATE 7"]?.statusColor ?: ColorSafe)    // GATE 7
                    )

                    gatePositions.forEach { (fx, fy, col) ->
                        val gx = w * fx
                        val gy = h * fy
                        drawLine(
                            color = col.copy(alpha = 0.4f),
                            start = Offset(cx, cy),
                            end = Offset(gx, gy),
                            strokeWidth = 1.5.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                        )
                        drawCircle(col.copy(alpha = 0.3f), radius = 16.dp.toPx(), center = Offset(gx, gy))
                        drawCircle(col, radius = 5.dp.toPx(), center = Offset(gx, gy))
                    }
                }

                // Place GateLabel precisely using BiasAlignment and custom offset!
                // GATE 1: Top Center
                Box(modifier = Modifier.align(BiasAlignment(0f, -0.68f)).offset(y = (-24).dp)) {
                    GateLabel("GATE 1", gateData["GATE 1"]?.statusColor ?: ColorSafe, selectedGate == "GATE 1") { selectedGate = "GATE 1" }
                }

                // GATE 2: Top Right
                Box(modifier = Modifier.align(BiasAlignment(0.52f, -0.56f)).offset(y = (-24).dp)) {
                    GateLabel("GATE 2", gateData["GATE 2"]?.statusColor ?: ColorSafe, selectedGate == "GATE 2") { selectedGate = "GATE 2" }
                }

                // GATE 3: Right Center
                Box(modifier = Modifier.align(BiasAlignment(0.76f, -0.04f)).offset(x = 24.dp)) {
                    GateLabel("GATE 3", gateData["GATE 3"]?.statusColor ?: ColorSafe, selectedGate == "GATE 3") { selectedGate = "GATE 3" }
                }

                // GATE 4: Bottom Center
                Box(modifier = Modifier.align(BiasAlignment(0f, 0.64f)).offset(y = 24.dp)) {
                    GateLabel("GATE 4", gateData["GATE 4"]?.statusColor ?: ColorSafe, selectedGate == "GATE 4") { selectedGate = "GATE 4" }
                }

                // GATE 5: Left Center
                Box(modifier = Modifier.align(BiasAlignment(-0.76f, -0.10f)).offset(x = (-24).dp)) {
                    GateLabel("GATE 5", gateData["GATE 5"]?.statusColor ?: ColorSafe, selectedGate == "GATE 5") { selectedGate = "GATE 5" }
                }

                // GATE 6: Bottom Left-ish
                Box(modifier = Modifier.align(BiasAlignment(-0.60f, 0.48f)).offset(y = 24.dp)) {
                    GateLabel("GATE 6", gateData["GATE 6"]?.statusColor ?: ColorSafe, selectedGate == "GATE 6") { selectedGate = "GATE 6" }
                }

                // GATE 7: Top Left Surge
                Box(modifier = Modifier.align(BiasAlignment(-0.56f, -0.64f)).offset(y = (-24).dp)) {
                    GateLabel("GATE 7", gateData["GATE 7"]?.statusColor ?: ColorSafe, selectedGate == "GATE 7") { selectedGate = "GATE 7" }
                }
            }
            }
        }

        // Legend row
        item {
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(tween(800, delayMillis = 300)),
                modifier = Modifier.fillMaxWidth()
            ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("LOW", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(
                        modifier = Modifier
                            .width(160.dp)
                            .height(6.dp)
                            .background(
                                Brush.horizontalGradient(listOf(ColorSafe, ColorAttention, ColorCritical)),
                                RoundedCornerShape(3.dp)
                            )
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("HIGH", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    "Tap on a gate to view live insights",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            }
        }

        // Selected Gate Info Card
        item {
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(tween(800, delayMillis = 400)),
                modifier = Modifier.fillMaxWidth()
            ) {
            Surface(
                color = Color(0xFF1E293B).copy(alpha = 0.85f),
                border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    currentGateInfo.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                if (currentGateInfo.isWarning) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Icon(
                                        Icons.Default.Warning,
                                        contentDescription = "Warning",
                                        tint = currentGateInfo.statusColor,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Text(
                                currentGateInfo.status,
                                color = currentGateInfo.statusColor,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }

                        // Sparkline
                        Box(modifier = Modifier.width(100.dp).height(35.dp)) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val path = Path()
                                val points = currentGateInfo.trendPoints
                                if (points.size > 1) {
                                    val stepX = size.width / (points.size - 1)
                                    points.forEachIndexed { i, value ->
                                        val x = i * stepX
                                        val y = size.height - (value * size.height)
                                        if (i == 0) {
                                            path.moveTo(x, y)
                                        } else {
                                            path.lineTo(x, y)
                                        }
                                    }
                                    drawPath(
                                        path = path,
                                        color = currentGateInfo.statusColor,
                                        style = Stroke(width = 2.dp.toPx())
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToFeeds() },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(currentGateInfo.expectedPeak, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            Text(currentGateInfo.confidence, color = TextSecondary, fontSize = 12.sp)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("View CCTV", color = ColorAiBlue, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                Icons.Default.ChevronRight,
                                contentDescription = "Details",
                                tint = ColorAiBlue,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    HorizontalDivider(color = GlassBorder)
                    Spacer(modifier = Modifier.height(20.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        GateStatItem(Icons.Default.Group, "People", currentGateInfo.people)
                        GateStatItem(Icons.Default.AccessTime, "Wait Time", currentGateInfo.waitTime)
                        GateStatItem(Icons.Default.Person, "Volunteers", currentGateInfo.volunteers)
                        GateStatItem(Icons.Default.MeetingRoom, "Exits Open", currentGateInfo.exits)
                    }
                }
            }
            }
        }
    }
}

@Composable
fun GateLabel(text: String, color: Color, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        color = if (isSelected) color.copy(alpha = 0.25f) else Color(0xFF0F172A).copy(alpha = 0.85f),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) color else color.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Text(
            text = text,
            color = TextPrimary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun GateStatItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.height(6.dp))
        Text(label, color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Medium)
        Text(value, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}
