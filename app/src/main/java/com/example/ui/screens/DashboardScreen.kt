package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
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
import androidx.compose.ui.platform.testTag

import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Accessible
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material.icons.filled.EnergySavingsLeaf
import androidx.compose.material.icons.filled.Co2
import androidx.compose.material.icons.filled.Speed

@Composable
fun DashboardScreen(
    viewModel: MainViewModel,
    onNavigateToFeeds: () -> Unit,
    onNavigateToAICopilot: () -> Unit
) {
    val state by viewModel.dashboardState.collectAsStateWithLifecycle()
    val loggedInUserType by viewModel.loggedInUserType.collectAsStateWithLifecycle()
    val fanName by viewModel.fanName.collectAsStateWithLifecycle()
    val fanSeat by viewModel.fanSeat.collectAsStateWithLifecycle()
    val fanTicketId by viewModel.fanTicketId.collectAsStateWithLifecycle()

    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isVisible = true
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 0.dp, bottom = 120.dp)
    ) {
        // Stadium Hero Image & Match Header
        item {
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(tween(800)),
                modifier = Modifier.fillMaxWidth()
            ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            ) {
                // Background Image
                androidx.compose.foundation.Image(
                    painter = androidx.compose.ui.res.painterResource(id = com.example.R.drawable.stadium_hero_1783434489967),
                    contentDescription = "Stadium",
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                
                // Bottom Gradient Fade
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Transparent, BgMain),
                                startY = 0f,
                                endY = Float.POSITIVE_INFINITY
                            )
                        )
                )

                // Match Header Overlaid
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally, 
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text("FIFA WORLD CUP 2026™", color = TextSecondary, fontSize = 12.sp, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                        Box(modifier = Modifier.size(40.dp).background(Color.White, RoundedCornerShape(20.dp)), contentAlignment = Alignment.Center) {
                            Text("🇲🇽", fontSize = 24.sp)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("MEXICO", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text(" vs ", color = TextSecondary, fontSize = 16.sp)
                        Text("BRAZIL", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(16.dp))
                        Box(modifier = Modifier.size(40.dp).background(Color.White, RoundedCornerShape(20.dp)), contentAlignment = Alignment.Center) {
                            Text("🇧🇷", fontSize = 24.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("${state.matchMinutes}'", color = ColorSafe, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text("SECOND HALF", color = ColorAiBlue, fontSize = 10.sp, letterSpacing = 1.sp)
                }
            }
            }
        }

        // Real-Time Telemetry Card
        item {
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInHorizontally(initialOffsetX = { -it }) + fadeIn(tween(800, delayMillis = 200)),
                modifier = Modifier.fillMaxWidth()
            ) {
            TelemetryCard(viewModel = viewModel)
            }
        }

        // Sustainability Card
        item {
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(tween(800, delayMillis = 300)),
                modifier = Modifier.fillMaxWidth()
            ) {
            SustainabilityCard(state = state)
            }
        }

        if (loggedInUserType == "fan") {
            // ================== FAN-SPECIFIC EXPERIENCE ==================
            
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInVertically(initialOffsetY = { it }) + fadeIn(tween(800, delayMillis = 400)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            viewModel.sendMessage("Show me the accessible route to my seat.")
                            onNavigateToAICopilot()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ColorAiBlue),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Accessible, contentDescription = "Accessible Route")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Accessible Route to Seat")
                    }
                }
                }
            }
            
            // 1. Digital Match ticket & Seat Guide Card
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInVertically(initialOffsetY = { it }) + fadeIn(tween(800, delayMillis = 500)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .liquidGlass()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("YOUR DIGITAL MATCH PASS", color = ColorAiBlue, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ColorSafe.copy(alpha = 0.15f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text("ACTIVE", color = ColorSafe, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(fanName.uppercase(), color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                                Text("Ticket ID: $fanTicketId", color = TextSecondary, fontSize = 12.sp)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("BLOCK / SEAT", color = TextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                Text(fanSeat, color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = GlassBorder)
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // AI Wayfinding Suggestion
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            val gate3Density = remember { (15..25).random() }
                            val aiTipText = if (state.gate7Density >= 75) {
                                "AI Wayfinding Tip: Gate 3 has normal density ($gate3Density%). Avoid Gate 7 (${state.gate7Density}% congestion)."
                            } else {
                                "AI Wayfinding Tip: All gates operating normally. Gate 4 recommended for quickest entry."
                            }
                            Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = ColorAiPurple, modifier = Modifier.size(20.dp))
                            Text(
                                text = aiTipText,
                                color = TextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                lineHeight = 16.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
                }
            }

            // 2. Convenience & Concessions Live Status Row
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInVertically(initialOffsetY = { it }) + fadeIn(tween(800, delayMillis = 600)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = "STAND 4 QUEUE",
                        icon = null,
                        iconColor = ColorSafe,
                        value = "2 Min",
                        subValue = "TACO BAR & BEER",
                        subValueColor = ColorSafe,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "GATE 3 ENTRY",
                        icon = null,
                        iconColor = ColorSafe,
                        value = "3 Min",
                        subValue = "RECOMMENDED",
                        subValueColor = ColorSafe,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "RESTROOMS (B1)",
                        icon = null,
                        iconColor = ColorAttention,
                        value = "4 Min",
                        subValue = "MODERATE QUEUE",
                        subValueColor = ColorAttention,
                        modifier = Modifier.weight(1f)
                    )
                }
                }
            }

            // 3. Match Statistics (Fan Engagement)
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInVertically(initialOffsetY = { it }) + fadeIn(tween(800, delayMillis = 700)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .liquidGlass()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("MATCH STATS AT ${state.matchMinutes}'", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Possession bar
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("MEX ${state.mexPossessionPct}%", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("Ball Possession", color = TextSecondary, fontSize = 12.sp)
                            Text("BRA ${100 - state.mexPossessionPct}%", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)).background(Color(0xFF0F172A))) {
                            Box(modifier = Modifier.weight(state.mexPossessionPct / 100f).fillMaxHeight().background(Color(0xFF006633)))
                            Box(modifier = Modifier.weight((100 - state.mexPossessionPct) / 100f).fillMaxHeight().background(Color(0xFFFFCC00)))
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Other stats
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("${state.mexShots}", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Text("Shots on Target", color = TextSecondary, fontSize = 10.sp)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("${state.mexCorners}", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Text("Corner Kicks", color = TextSecondary, fontSize = 10.sp)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("${state.mexFouls}", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Text("Fouls committed", color = TextSecondary, fontSize = 10.sp)
                            }
                        }
                    }
                }
                }
            }

            // 4. Group Standings
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInVertically(initialOffsetY = { it }) + fadeIn(tween(800, delayMillis = 800)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .liquidGlass()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("GROUP A LIVE STANDINGS", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("1", color = AccentLight, fontWeight = FontWeight.Bold, modifier = Modifier.width(20.dp), fontSize = 12.sp)
                            Text("🇲🇽 MEXICO", color = TextPrimary, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), fontSize = 12.sp)
                            Text("3  2  1  0  +3  7 pts", color = TextPrimary, fontSize = 12.sp)
                        }
                        HorizontalDivider(color = GlassBorder.copy(alpha = 0.5f))
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("2", color = TextSecondary, fontWeight = FontWeight.Bold, modifier = Modifier.width(20.dp), fontSize = 12.sp)
                            Text("🇧🇷 BRAZIL", color = TextPrimary, modifier = Modifier.weight(1f), fontSize = 12.sp)
                            Text("3  2  0  1  +2  6 pts", color = TextPrimary, fontSize = 12.sp)
                        }
                        HorizontalDivider(color = GlassBorder.copy(alpha = 0.5f))
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("3", color = TextSecondary, fontWeight = FontWeight.Bold, modifier = Modifier.width(20.dp), fontSize = 12.sp)
                            Text("🇸🇪 SWEDEN", color = TextSecondary, modifier = Modifier.weight(1f), fontSize = 12.sp)
                            Text("3  1  0  2  -1  3 pts", color = TextSecondary, fontSize = 12.sp)
                        }
                    }
                }
                }
            }
            
        } else {
            // ================== STAFF / OPERATIONS EXPERIENCE ==================
            
            // AI Stadium Score
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInVertically(initialOffsetY = { it }) + fadeIn(tween(800, delayMillis = 400)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .liquidGlass()
                ) {
                    Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("AI STADIUM SCORE", color = ColorSafe, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(state.aiScore.toString(), color = TextPrimary, fontSize = 48.sp, fontWeight = FontWeight.Bold)
                                Text("/100", color = TextSecondary, fontSize = 16.sp, modifier = Modifier.padding(bottom = 8.dp))
                            }
                            Text(
                                if (state.activeIncidentsCount > 0) "Minor gate congestion detected" else "Everything operating smoothly", 
                                color = TextSecondary, 
                                fontSize = 12.sp
                            )
                        }
                        // Mini graph
                        Box(modifier = Modifier.width(80.dp).height(40.dp)) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val path = Path()
                                path.moveTo(0f, size.height * 0.8f)
                                path.quadraticBezierTo(size.width * 0.25f, size.height, size.width * 0.5f, size.height * 0.4f)
                                path.quadraticBezierTo(size.width * 0.75f, 0f, size.width, size.height * 0.2f)
                                drawPath(path, ColorSafe, style = Stroke(width = 2.dp.toPx()))
                            }
                        }
                    }
                }
                }
            }

            // Stats Row
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInVertically(initialOffsetY = { it }) + fadeIn(tween(800, delayMillis = 500)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard(
                        title = "ATTENDANCE",
                        icon = Icons.Default.Group,
                        iconColor = ColorAiBlue,
                        value = String.format("%,d", state.attendance),
                        subValue = "+4.3%",
                        subValueColor = ColorSafe,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "CROWD DENSITY",
                        icon = null,
                        iconColor = if (state.gate7Density >= 80) ColorCritical else if (state.gate7Density >= 50) ColorAttention else ColorSafe,
                        value = "${state.gate7Density}%",
                        subValue = if (state.gate7Density >= 80) "HIGH" else if (state.gate7Density >= 50) "MODERATE" else "NORMAL",
                        subValueColor = if (state.gate7Density >= 80) ColorCritical else if (state.gate7Density >= 50) ColorAttention else ColorSafe,
                        isCircular = true,
                        progress = state.gate7Density / 100f,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "INCIDENTS",
                        icon = Icons.Default.Warning,
                        iconColor = if (state.activeIncidentsCount > 0) ColorCritical else ColorSafe,
                        value = state.activeIncidentsCount.toString(),
                        subValue = if (state.activeIncidentsCount > 0) "ACTIVE" else "SECURE",
                        subValueColor = if (state.activeIncidentsCount > 0) ColorCritical else ColorSafe,
                        modifier = Modifier.weight(1f)
                    )
                }
                }
            }

            // AI Prediction
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInVertically(initialOffsetY = { it }) + fadeIn(tween(800, delayMillis = 600)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                val isIncident = state.activeIncidentsCount > 0
                val cardBorder = if (isIncident) ColorCritical.copy(alpha = 0.3f) else ColorSafe.copy(alpha = 0.3f)
                val radialColor = if (isIncident) ColorCriticalDark.copy(alpha = 0.5f) else ColorSafeDark.copy(alpha = 0.3f)
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .liquidGlass(
                            topBorderColor = if (isIncident) ColorCritical.copy(alpha = 0.45f) else ColorSafe.copy(alpha = 0.45f),
                            bottomBorderColor = if (isIncident) ColorCritical.copy(alpha = 0.1f) else ColorSafe.copy(alpha = 0.1f)
                        )
                ) {
                    Box(modifier = Modifier.fillMaxWidth().background(Brush.radialGradient(listOf(radialColor, Color.Transparent), radius = 500f))) {
                        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("AI PREDICTION", color = if (isIncident) ColorCritical else ColorSafe, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    if (isIncident) "Crowd surge expected at Gate 7 in ${state.surgeMinutes} minutes" else "Normal egress flows predicted. Gate 7 is clear.",
                                    color = if (isIncident) ColorCritical else TextPrimary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Confidence 96%", color = TextSecondary, fontSize = 12.sp)
                            }
                            TextButton(onClick = onNavigateToFeeds) {
                                Text("View Details", color = TextSecondary, fontSize = 12.sp)
                            }
                        }
                    }
                }
                }
            }

            // Live CCTV Feeds Preview Section
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInVertically(initialOffsetY = { it }) + fadeIn(tween(800, delayMillis = 700)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .liquidGlass()
                        .clickable { onNavigateToFeeds() }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(ColorCritical)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "LIVE CCTV FEEDS",
                                    color = TextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                            }
                            Text(
                                "View All (6)",
                                color = ColorAiBlue,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Preview 1 (Gate 7)
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(80.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            ) {
                                Image(
                                    painter = androidx.compose.ui.res.painterResource(id = com.example.R.drawable.img_cctv_gate7_1783444051163),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))))
                                )
                                Text(
                                    "Gate 7",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(8.dp)
                                        .testTag("cctv_preview_gate7")
                                )
                            }

                            // Preview 2 (Gate 3)
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(80.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            ) {
                                Image(
                                    painter = androidx.compose.ui.res.painterResource(id = com.example.R.drawable.img_cctv_gate3_1783444067059),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))))
                                )
                                Text(
                                    "Gate 3",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(8.dp)
                                        .testTag("cctv_preview_gate3")
                                )
                            }
                        }
                    }
                }
                }
            }
        }
    }
}

@Composable
fun StatCard(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector?, iconColor: Color, value: String, subValue: String, subValueColor: Color, isCircular: Boolean = false, progress: Float = 0.8f, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(120.dp)
            .liquidGlass(shape = RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                color = TextSecondary,
                fontSize = 10.sp,
                letterSpacing = 0.5.sp,
                maxLines = 1
            )
            
            if (isCircular) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(46.dp)) {
                    CircularProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxSize(),
                        color = iconColor,
                        strokeWidth = 3.5.dp,
                        trackColor = Color(0xFF0F172A),
                        strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                    )
                    Text(value, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (icon != null) {
                        Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(22.dp))
                    }
                    Text(value, color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }
            
            Text(
                text = subValue,
                color = subValueColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}

@Composable
fun TelemetryCard(viewModel: MainViewModel) {
    val useRealSensors by viewModel.useRealSensors.collectAsStateWithLifecycle()
    val liveLightLux by viewModel.liveLightLux.collectAsStateWithLifecycle()
    val livePressureHpa by viewModel.livePressureHpa.collectAsStateWithLifecycle()
    val liveVibrationMagnitude by viewModel.liveVibrationMagnitude.collectAsStateWithLifecycle()

    val hasLightSensor by viewModel.hasLightSensor.collectAsStateWithLifecycle()
    val hasPressureSensor by viewModel.hasPressureSensor.collectAsStateWithLifecycle()
    val hasAccelerometer by viewModel.hasAccelerometer.collectAsStateWithLifecycle()

    val diff = (liveVibrationMagnitude - 9.81f).coerceAtLeast(0f)
    val cheeringIndex = (diff * 15f).coerceIn(0f, 100f).toInt()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .liquidGlass()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Sensors,
                        contentDescription = "Sensors",
                        tint = ColorAiBlue,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "LIVE DEVICE TELEMETRY",
                        color = TextPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                    val alpha by infiniteTransition.animateFloat(
                        initialValue = 0.3f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1000, easing = LinearEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "pulseAlpha"
                    )
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(ColorSafe.copy(alpha = alpha))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (useRealSensors) "HARDWARE ACTIVE" else "PAUSED",
                        color = if (useRealSensors) ColorSafe else TextSecondary,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Show real-time readings from your device's built-in sensors instead of stadium simulations.",
                color = TextSecondary,
                fontSize = 11.sp,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0F172A).copy(alpha = 0.5f))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Stream Hardware Telemetry",
                    color = TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Switch(
                    checked = useRealSensors,
                    onCheckedChange = { viewModel.setUseRealSensors(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = ColorSafe,
                        checkedTrackColor = ColorSafe.copy(alpha = 0.3f),
                        uncheckedThumbColor = TextSecondary,
                        uncheckedTrackColor = Color(0xFF1E293B)
                    )
                )
            }

            if (useRealSensors) {
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TelemetryStatItem(
                        title = "FAN CHEERING",
                        value = if (hasAccelerometer) "$cheeringIndex%" else "Offline",
                        subValue = if (!hasAccelerometer) "No Accelerometer" else if (cheeringIndex > 15) "SHAKING! 🎉" else "STABLE 💺",
                        icon = Icons.Default.Sensors,
                        iconColor = ColorAiPurple,
                        modifier = Modifier.weight(1f)
                    )

                    TelemetryStatItem(
                        title = "ILLUM.",
                        value = if (hasLightSensor) "${liveLightLux.toInt()} lx" else "Offline",
                        subValue = if (!hasLightSensor) "No Light Sensor" else if (liveLightLux > 120) "BRIGHT 💡" else "DIM LIGHTS 🌑",
                        icon = Icons.Default.WbSunny,
                        iconColor = Color(0xFFFBBF24),
                        modifier = Modifier.weight(1f)
                    )

                    TelemetryStatItem(
                        title = "AZTECA ALT.",
                        value = if (hasPressureSensor) "${livePressureHpa.toInt()} hPa" else "Offline",
                        subValue = if (!hasPressureSensor) "No Barometer" else if (livePressureHpa < 850) "HIGH CDMX ALT ⛰️" else "STANDARD PRESS 🌊",
                        icon = Icons.Default.Speed,
                        iconColor = ColorAiBlue,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun TelemetryStatItem(
    title: String,
    value: String,
    subValue: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(108.dp)
            .liquidGlass(
                shape = RoundedCornerShape(12.dp),
                bgStartColor = Color(0xFF0F172A).copy(alpha = 0.60f),
                bgEndColor = Color(0xFF07090F).copy(alpha = 0.80f),
                topBorderColor = Color.White.copy(alpha = 0.20f),
                bottomBorderColor = Color.White.copy(alpha = 0.03f)
            )
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                color = TextSecondary,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
                maxLines = 1
            )
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(16.dp))
                Text(
                    text = value,
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 1
                )
            }
            
            Text(
                text = subValue,
                color = if (value != "Offline") iconColor else TextSecondary,
                fontSize = 8.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
        }
    }
}

@Composable
fun SustainabilityCard(state: com.example.viewmodel.DashboardState) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .liquidGlass()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.EnergySavingsLeaf,
                        contentDescription = "Sustainability",
                        tint = ColorSafe,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SUSTAINABILITY METRICS",
                        color = TextPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TelemetryStatItem(
                    title = "RENEWABLE ENERGY",
                    value = "${state.renewableEnergyPct}%",
                    subValue = "SOLAR ACTIVE",
                    icon = Icons.Default.EnergySavingsLeaf,
                    iconColor = ColorSafe,
                    modifier = Modifier.weight(1f)
                )

                TelemetryStatItem(
                    title = "WASTE DIVERSION",
                    value = "${state.wasteDiversionPct}%",
                    subValue = "COMPOST & RECYCLE",
                    icon = Icons.Default.Recycling,
                    iconColor = ColorAiBlue,
                    modifier = Modifier.weight(1f)
                )

                TelemetryStatItem(
                    title = "CARBON OFFSET",
                    value = String.format("%.1ft", state.carbonOffsetTonnes),
                    subValue = "TRANSIT SAVINGS",
                    icon = Icons.Default.Co2,
                    iconColor = ColorAiPurple,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
