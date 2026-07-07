package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.gemini.GeminiApi
import com.example.viewmodel.MainViewModel
import kotlinx.coroutines.launch

data class StadiumZone(val id: String, val name: String, val density: Float)

@Composable
fun MapScreen(viewModel: MainViewModel) {
    val coroutineScope = rememberCoroutineScope()
    var selectedZone by remember { mutableStateOf<StadiumZone?>(null) }
    var zoneAnalysis by remember { mutableStateOf<String?>(null) }
    var isAnalyzing by remember { mutableStateOf(false) }

    val zones = listOf(
        StadiumZone("NW", "North West Concourse", 0.3f),
        StadiumZone("N", "North Gate", 0.6f),
        StadiumZone("NE", "North East Concourse", 0.2f),
        StadiumZone("W", "West Ramp", 0.4f),
        StadiumZone("PITCH", "Pitch Area", 0.1f),
        StadiumZone("E", "East Ramp", 0.5f),
        StadiumZone("SW", "South West Gate", 0.7f),
        StadiumZone("S", "Gate 7 (South)", 0.95f),
        StadiumZone("SE", "South East Transit", 0.8f)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Digital Twin: Stadium View", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Interactive Heatmap - Tap a zone for AI analysis", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.large)
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(zones) { zone ->
                val densityColor = when {
                    zone.density > 0.8f -> Color(0xFFE53935)
                    zone.density > 0.5f -> Color(0xFFFDD835)
                    else -> Color(0xFF43A047)
                }
                
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .background(densityColor.copy(alpha = 0.8f), MaterialTheme.shapes.small)
                        .clickable {
                            selectedZone = zone
                            isAnalyzing = true
                            zoneAnalysis = null
                            coroutineScope.launch {
                                val prompt = "Provide a 2-sentence real-time crowd analysis and operational recommendation for the ${zone.name} zone at a FIFA World Cup stadium. Current density is ${(zone.density * 100).toInt()}%."
                                val response = GeminiApi.generateContent(prompt)
                                zoneAnalysis = response
                                isAnalyzing = false
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = zone.id,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (selectedZone != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shape = MaterialTheme.shapes.large
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = "Info", tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Analysis: ${selectedZone!!.name}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    if (isAnalyzing) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    } else {
                        Text(zoneAnalysis ?: "Could not fetch analysis.", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shape = MaterialTheme.shapes.large
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Select a zone above to view live GenAI operational analysis.", style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}
