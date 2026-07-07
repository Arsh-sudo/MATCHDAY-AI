package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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

@Composable
fun DashboardScreen(viewModel: MainViewModel) {
    val state by viewModel.dashboardState.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 120.dp)
    ) {
        // Match Header Header
        item {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
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
                Text("72'", color = ColorSafe, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("SECOND HALF", color = ColorAiBlue, fontSize = 10.sp, letterSpacing = 1.sp)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // AI Stadium Score
        item {
            Surface(
                color = Color(0xFF1E293B).copy(alpha = 0.85f),
                border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("AI STADIUM SCORE", color = ColorSafe, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text("98", color = TextPrimary, fontSize = 48.sp, fontWeight = FontWeight.Bold)
                            Text("/100", color = TextSecondary, fontSize = 16.sp, modifier = Modifier.padding(bottom = 8.dp))
                        }
                        Text("Everything operating smoothly", color = TextSecondary, fontSize = 12.sp)
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

        // Stats Row
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard(
                    title = "ATTENDANCE",
                    icon = Icons.Default.Group,
                    iconColor = ColorAiBlue,
                    value = "58,225",
                    subValue = "+4.3%",
                    subValueColor = ColorSafe,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "CROWD DENSITY",
                    icon = null,
                    iconColor = ColorSafe,
                    value = "80%",
                    subValue = "NORMAL",
                    subValueColor = ColorSafe,
                    isCircular = true,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "INCIDENTS",
                    icon = Icons.Default.Warning,
                    iconColor = ColorCritical,
                    value = "1",
                    subValue = "ACTIVE",
                    subValueColor = ColorCritical,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // AI Prediction
        item {
            Surface(
                color = Color(0xFF1E293B).copy(alpha = 0.85f),
                border = androidx.compose.foundation.BorderStroke(1.dp, ColorCritical.copy(alpha = 0.3f)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.fillMaxWidth().background(Brush.radialGradient(listOf(ColorCriticalDark.copy(alpha = 0.5f), Color.Transparent), radius = 500f))) {
                    Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("AI PREDICTION", color = ColorCritical, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Crowd surge expected at Gate 7 in 6 minutes", color = ColorCritical, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Confidence 96%", color = TextSecondary, fontSize = 12.sp)
                        }
                        Text("View Details", color = TextSecondary, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector?, iconColor: Color, value: String, subValue: String, subValueColor: Color, isCircular: Boolean = false, modifier: Modifier = Modifier) {
    Surface(
        color = Color(0xFF1E293B).copy(alpha = 0.85f),
        border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.height(130.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
            Text(title, color = TextSecondary, fontSize = 10.sp, letterSpacing = 0.5.sp)
            
            if (isCircular) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(50.dp)) {
                    CircularProgressIndicator(progress = { 0.8f }, modifier = Modifier.fillMaxSize(), color = iconColor, strokeWidth = 4.dp, trackColor = Color(0xFF0F172A), strokeCap = androidx.compose.ui.graphics.StrokeCap.Round)
                    Text(value, color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                if (icon != null) {
                    Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(24.dp))
                }
                Text(value, color = TextPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            
            Text(subValue, color = subValueColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}
