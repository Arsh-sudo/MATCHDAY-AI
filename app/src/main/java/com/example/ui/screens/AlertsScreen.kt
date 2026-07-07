package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.MainViewModel
import com.example.ui.theme.*

@Composable
fun AlertsScreen(viewModel: MainViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 120.dp)
    ) {
        item {
            Text("ALERTS CENTER", style = MaterialTheme.typography.titleLarge, color = TextPrimary, fontWeight = FontWeight.Bold)
            Text("Live Notifications", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            EmergencyAlertCard(mins = 6, onDeploy = { viewModel.resolveEmergency() })
        }

        item {
            AlertCard(
                title = "Metro Delay",
                subtitle = "Line 3 - South Station",
                severityColor = ColorAttention,
                severityColorDark = ColorAttentionDark,
                description = "Delay: 8 mins\nImpact: Moderate",
                actionText = "Update digital signage"
            )
        }

        item {
            AlertCard(
                title = "Weather Update",
                subtitle = "Light Rain Expected",
                severityColor = ColorSafe,
                severityColorDark = ColorSafeDark,
                description = "Duration: Next 2 hours",
                actionText = "Close roof"
            )
        }
    }
}

@Composable
fun AlertCard(title: String, subtitle: String, severityColor: Color, severityColorDark: Color, description: String, actionText: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1E293B).copy(alpha = 0.85f), RoundedCornerShape(20.dp))
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(listOf(severityColor, severityColorDark)),
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Warning, contentDescription = "Alert", tint = severityColor, modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Text(title, fontWeight = FontWeight.Bold, color = severityColor, style = MaterialTheme.typography.titleMedium)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(subtitle, color = TextPrimary, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(description, color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = GlassBorder)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Suggested Action", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(actionText, color = TextPrimary, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {}, 
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .border(1.dp, severityColor, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("View Details", fontWeight = FontWeight.Bold, color = severityColor)
            }
        }
    }
}

@Composable
fun EmergencyAlertCard(mins: Int, onDeploy: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1E293B).copy(alpha = 0.85f), RoundedCornerShape(24.dp))
            .border(
                width = 2.dp,
                brush = Brush.verticalGradient(listOf(ColorCritical, ColorCritical.copy(alpha = 0.2f))),
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Warning, contentDescription = "Alert", tint = ColorCritical, modifier = Modifier.size(48.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Text("Crowd Surge Detected", fontWeight = FontWeight.Bold, color = ColorCritical, style = MaterialTheme.typography.titleLarge)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Gate 7", color = TextPrimary, style = MaterialTheme.typography.titleMedium)
                Text("Expected in $mins mins", color = TextPrimary, style = MaterialTheme.typography.titleMedium)
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = onDeploy, 
                colors = ButtonDefaults.buttonColors(containerColor = ColorCritical),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Deploy & Execute", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}
