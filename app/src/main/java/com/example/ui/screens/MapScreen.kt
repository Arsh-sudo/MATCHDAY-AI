package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.MainViewModel
import com.example.ui.theme.*

@Composable
fun MapScreen(viewModel: MainViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(bottom = 120.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("STADIUM DIGITAL TWIN", style = MaterialTheme.typography.titleMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
        Text("Live Operational View", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
        
        Spacer(modifier = Modifier.height(24.dp))

        // Stadium Visual
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2, size.height / 2)
                
                // Outer Stadium Ring
                drawRoundRect(
                    color = Color(0xFF1E293B),
                    topLeft = Offset(center.x - 140.dp.toPx(), center.y - 200.dp.toPx()),
                    size = Size(280.dp.toPx(), 400.dp.toPx()),
                    cornerRadius = CornerRadius(140.dp.toPx(), 140.dp.toPx()),
                    style = Stroke(width = 20.dp.toPx())
                )
                
                // Inner Pitch
                drawRoundRect(
                    color = ColorSafeDark.copy(alpha = 0.5f),
                    topLeft = Offset(center.x - 60.dp.toPx(), center.y - 100.dp.toPx()),
                    size = Size(120.dp.toPx(), 200.dp.toPx()),
                    cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx()),
                    style = Stroke(width = 2.dp.toPx())
                )
                
                // Draw connecting lines and gates
                val gates = listOf(
                    Pair(Offset(center.x, center.y - 200.dp.toPx()), ColorAiPurple), // Gate 1
                    Pair(Offset(center.x + 100.dp.toPx(), center.y - 140.dp.toPx()), ColorAiBlue), // Gate 2
                    Pair(Offset(center.x + 140.dp.toPx(), center.y), ColorAttention), // Gate 3
                    Pair(Offset(center.x, center.y + 200.dp.toPx()), ColorAiPurple), // Gate 4
                    Pair(Offset(center.x - 140.dp.toPx(), center.y - 140.dp.toPx()), ColorSafe), // Gate 5
                    Pair(Offset(center.x - 100.dp.toPx(), center.y + 140.dp.toPx()), ColorAttention), // Gate 6
                    Pair(Offset(center.x - 70.dp.toPx(), center.y - 200.dp.toPx()), ColorCritical), // Gate 7 (Surge)
                )
                
                gates.forEachIndexed { index, pair ->
                    val pos = pair.first
                    val col = pair.second
                    // Line to center
                    drawLine(col.copy(alpha = 0.3f), start = pos, end = center, strokeWidth = 1.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f))
                    // Glow node
                    drawCircle(col.copy(alpha = 0.2f), radius = 20.dp.toPx(), center = pos)
                    drawCircle(col, radius = 6.dp.toPx(), center = pos)
                }
            }
            
            // Overlays for Gate Labels
            GateLabel("GATE 1", Modifier.align(Alignment.TopCenter).offset(y = (-40).dp), ColorAiPurple)
            GateLabel("GATE 7", Modifier.align(Alignment.TopStart).offset(x = 60.dp, y = (-20).dp), ColorCritical)
            GateLabel("GATE 5", Modifier.align(Alignment.CenterStart).offset(x = (-10).dp, y = (-100).dp), ColorSafe)
            GateLabel("GATE 6", Modifier.align(Alignment.BottomStart).offset(x = 30.dp, y = (-20).dp), ColorAttention)
            GateLabel("GATE 4", Modifier.align(Alignment.BottomCenter).offset(y = (-40).dp), ColorAiPurple)
            GateLabel("GATE 3", Modifier.align(Alignment.CenterEnd).offset(x = 10.dp), ColorAttention)
            GateLabel("GATE 2", Modifier.align(Alignment.TopEnd).offset(x = (-30).dp, y = (-20).dp), ColorAiBlue)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Legend
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text("LOW", color = TextSecondary, fontSize = 12.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.width(150.dp).height(4.dp).background(Brush.horizontalGradient(listOf(ColorSafe, ColorAttention, ColorCritical)), RoundedCornerShape(2.dp)))
            Spacer(modifier = Modifier.width(8.dp))
            Text("HIGH", color = TextSecondary, fontSize = 12.sp)
        }
        Text("Tap on a gate to view live insights", color = TextSecondary, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))

        Spacer(modifier = Modifier.height(24.dp))

        // Selected Gate Info (Gate 7)
        Surface(
            color = Color(0xFF1E293B).copy(alpha = 0.85f),
            border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("GATE 7", style = MaterialTheme.typography.titleLarge, color = TextPrimary, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.Default.Warning, contentDescription = null, tint = ColorCritical, modifier = Modifier.size(16.dp))
                        }
                        Text("HIGH CONGESTION", color = ColorCritical, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextSecondary)
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Expected peak in 6 mins", color = TextPrimary, fontSize = 14.sp)
                        Text("Confidence 96%", color = TextSecondary, fontSize = 12.sp)
                    }
                    
                    // Sparkline
                    Box(modifier = Modifier.width(80.dp).height(30.dp)) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val path = Path()
                            path.moveTo(0f, size.height)
                            path.quadraticBezierTo(size.width * 0.25f, size.height, size.width * 0.5f, size.height * 0.5f)
                            path.quadraticBezierTo(size.width * 0.75f, 0f, size.width, 0f)
                            drawPath(path, ColorCritical, style = Stroke(width = 2.dp.toPx()))
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    GateStatItem(Icons.Default.Group, "People", "4,782")
                    GateStatItem(Icons.Default.AccessTime, "Wait Time", "18 min")
                    GateStatItem(Icons.Default.Person, "Volunteers", "3")
                    GateStatItem(Icons.Default.MeetingRoom, "Exits Open", "2/4")
                }
            }
        }
    }
}

@Composable
fun GateLabel(text: String, modifier: Modifier, color: Color) {
    Surface(
        color = Color(0xFF0F172A).copy(alpha = 0.8f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Text(text, color = TextPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
    }
}

@Composable
fun GateStatItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, color = TextSecondary, fontSize = 10.sp)
        Text(value, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}
