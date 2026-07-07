package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.viewmodel.MainViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun DashboardScreen(viewModel: MainViewModel) {
    val state by viewModel.dashboardState.collectAsStateWithLifecycle()
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Operational Intelligence", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Real-time Stadium Knowledge Graph Data", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            DashboardCard(
                title = "Crowd Density - Gate 7",
                value = "${state.gate7Density}%",
                status = if (state.gate7Density > 80) "High Congestion Predicted" else "Normal Flow",
                statusColor = if (state.gate7Density > 80) Color(0xFFE53935) else Color(0xFF43A047)
            )
        }

        item {
            DashboardCard(
                title = "Transit Hub A",
                value = "Flowing",
                status = "Next train in 3 mins",
                statusColor = Color(0xFF43A047)
            )
        }

        item {
            DashboardCard(
                title = "Volunteer Deployment",
                value = "${state.volunteersActive}/50 Active",
                status = "Dispatching to Gate 7",
                statusColor = Color(0xFFFDD835)
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                shape = MaterialTheme.shapes.large
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Warning, contentDescription = "Alert", tint = MaterialTheme.colorScheme.onPrimaryContainer)
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("AI Prediction Alert", fontWeight = FontWeight.Bold)
                        Text("Halftime surge expected in ${state.surgeMinutes} mins. Recommend pre-staging crowd control at East Concourse.")
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardCard(title: String, value: String, status: String, statusColor: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        shape = MaterialTheme.shapes.large
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = statusColor,
                    modifier = Modifier.size(12.dp)
                ) {}
                Spacer(modifier = Modifier.width(8.dp))
                Text(status, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
