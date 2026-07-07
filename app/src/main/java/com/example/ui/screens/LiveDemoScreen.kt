package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.MainViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun LiveDemoScreen(viewModel: MainViewModel, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(bottom = 120.dp),
        horizontalAlignment = Alignment.CenterHorizontally
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
                Text("GEMINI OPERATIONS ASSISTANT", style = MaterialTheme.typography.titleMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
                Text("AI Copilot", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
            }
            IconButton(onClick = {}) {
                Icon(Icons.Default.MoreHoriz, contentDescription = "More", tint = TextPrimary)
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Robot Avatar
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Brush.radialGradient(listOf(ColorAiBlue.copy(alpha = 0.5f), Color.Transparent)))
                .clip(CircleShape)
                .border(2.dp, ColorAiBlue, CircleShape)
                .background(Color(0xFF0F172A)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.AutoAwesome, contentDescription = "Robot", tint = ColorAiBlue, modifier = Modifier.size(40.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Chat Bubble
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFF1E293B).copy(alpha = 0.85f),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp, bottomEnd = 24.dp, bottomStart = 4.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Hello Operations Staff! 👋", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Text("I am MATCHDAY AI.\nConnected to:", color = TextSecondary, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                
                val capabilities = listOf("CCTV Cameras", "Crowd Sensors", "Transit APIs", "Weather Systems", "Medical Teams")
                capabilities.forEach { cap ->
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = ColorSafe, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(cap, color = TextPrimary, fontSize = 14.sp)
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                Text("How can I assist you today?", color = TextPrimary, fontSize = 14.sp)
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))

        // Suggested Actions
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
            Text("Suggested Actions", color = TextSecondary, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(12.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                ActionChip("Show Crowd Status")
                ActionChip("Traffic Update")
                ActionChip("Medical Emergencies")
                ActionChip("Open Incidents")
                ActionChip("Volunteer Deployment")
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))

        // Input Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color(0xFF1E293B).copy(alpha = 0.6f), RoundedCornerShape(28.dp))
                .border(1.dp, GlassBorder, RoundedCornerShape(28.dp))
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Ask MATCHDAY AI...", color = TextSecondary, fontSize = 14.sp, modifier = Modifier.weight(1f).padding(start = 16.dp))
            Icon(Icons.Default.Mic, contentDescription = "Mic", tint = TextSecondary, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(listOf(ColorAiPurple, ColorAiBlue))),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ActionChip(text: String) {
    Surface(
        color = Color.Transparent,
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
    ) {
        Text(text, color = TextPrimary, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
    }
}
