package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.border
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gemini.GeminiApi
import com.example.viewmodel.MainViewModel
import com.example.ui.theme.*
import kotlinx.coroutines.launch

data class StadiumZone(val id: String, val name: String, val density: Float)

@Composable
fun MapScreen(viewModel: MainViewModel) {
    val coroutineScope = rememberCoroutineScope()
    var selectedZone by remember { mutableStateOf<StadiumZone?>(null) }
    var zoneAnalysis by remember { mutableStateOf<String?>(null) }
    var isAnalyzing by remember { mutableStateOf(false) }

    val state by viewModel.dashboardState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Digital Twin", style = MaterialTheme.typography.titleMedium, color = TextSecondary)
        Spacer(modifier = Modifier.height(16.dp))

        // Stadium Map
        StadiumMapLayout(state.gate7Density / 100f) { zone ->
            selectedZone = zone
            isAnalyzing = true
            zoneAnalysis = null
            coroutineScope.launch {
                val prompt = "Provide a 2-sentence real-time crowd analysis and operational recommendation for the ${zone.name} zone at a FIFA World Cup stadium. Current density is ${(zone.density * 100).toInt()}%."
                val response = GeminiApi.generateContent(prompt)
                zoneAnalysis = response
                isAnalyzing = false
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (selectedZone != null) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = GlassBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = "AI", tint = ColorAiPurple)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(selectedZone!!.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("Density", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
                            Text("${(selectedZone!!.density * 100).toInt()}%", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = if(selectedZone!!.density > 0.8f) ColorCritical else ColorSafe)
                        }
                        Column {
                            Text("Med Requests", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
                            Text("0", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                        Column {
                            Text("Est. Exit", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
                            Text(if(selectedZone!!.density > 0.8f) "14 min" else "4 min", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    if (isAnalyzing) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = ColorAiPurple)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Analyzing zone metrics...", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                        }
                    } else {
                        Text("AI Insight", color = ColorAiBlue, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(zoneAnalysis ?: "Could not fetch analysis.", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        } else {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = GlassBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Tap a stadium sector to view live telemetry and AI insights.", style = MaterialTheme.typography.bodyMedium, color = TextSecondary, textAlign = TextAlign.Center)
                }
            }
        }
    }
}

@Composable
fun StadiumMapLayout(gate7Density: Float, onZoneClick: (StadiumZone) -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "pulse_scale"
    )

    val zones = listOf(
        StadiumZone("NORTH", "North Stand", 0.4f),
        StadiumZone("WEST", "West Stand", 0.6f),
        StadiumZone("EAST", "East Stand", 0.5f),
        StadiumZone("SOUTH", "Gate 7 (South)", gate7Density)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.9f)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Pitch
        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight(0.7f)
                .background(ColorSafe.copy(alpha = 0.1f), RoundedCornerShape(24.dp))
                .border(2.dp, ColorSafe.copy(alpha = 0.3f), RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            Box(modifier = Modifier.size(60.dp).border(1.dp, ColorSafe.copy(alpha = 0.2f), RoundedCornerShape(30.dp)))
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(ColorSafe.copy(alpha = 0.2f)))
            Text("PITCH", color = ColorSafe.copy(alpha = 0.5f), fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
        }

        // North Stand
        StadiumZoneBox(zones[0], Modifier.align(Alignment.TopCenter).fillMaxWidth(0.7f).height(60.dp), onZoneClick)
        
        // South Stand
        StadiumZoneBox(zones[3], Modifier.align(Alignment.BottomCenter).fillMaxWidth(0.7f).height(60.dp), onZoneClick, scale = if (gate7Density > 0.8f) pulseScale else 1f)
        
        // West Stand
        StadiumZoneBox(zones[1], Modifier.align(Alignment.CenterStart).fillMaxHeight(0.7f).width(60.dp), onZoneClick)
        
        // East Stand
        StadiumZoneBox(zones[2], Modifier.align(Alignment.CenterEnd).fillMaxHeight(0.7f).width(60.dp), onZoneClick)
    }
}

@Composable
fun StadiumZoneBox(zone: StadiumZone, modifier: Modifier, onClick: (StadiumZone) -> Unit, scale: Float = 1f) {
    val color = when {
        zone.density > 0.8f -> ColorCritical
        zone.density > 0.6f -> ColorAttention
        else -> ColorSafe
    }
    
    Box(
        modifier = modifier
            .background(color.copy(alpha = 0.2f * scale), RoundedCornerShape(12.dp))
            .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .clickable { onClick(zone) },
        contentAlignment = Alignment.Center
    ) {
        Text(zone.id, color = color, fontWeight = FontWeight.Bold, fontSize = 12.sp)
    }
}
