package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gemini.GeminiApi
import kotlinx.coroutines.launch

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@Composable
fun AlertsScreen() {
    var alertText by remember { mutableStateOf("") }
    var translatedAlerts by remember { mutableStateOf<String?>(null) }
    var isTranslating by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Multilingual Announcements", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Broadcast messages simultaneously in 6 languages across stadium signage.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)

        OutlinedTextField(
            value = alertText,
            onValueChange = { alertText = it },
            label = { Text("English Broadcast Message") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 5
        )

        Button(
            onClick = {
                if (alertText.isNotBlank()) {
                    isTranslating = true
                    coroutineScope.launch {
                        val prompt = "Translate the following stadium announcement into Spanish, French, Arabic, Portuguese, and Japanese. Format the output nicely with the language name in bold:\n\n\"$alertText\""
                        translatedAlerts = GeminiApi.generateContent(prompt)
                        isTranslating = false
                    }
                }
            },
            modifier = Modifier.align(Alignment.End),
            enabled = !isTranslating && alertText.isNotBlank()
        ) {
            Icon(Icons.Default.Translate, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Translate & Broadcast")
        }

        if (isTranslating) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (translatedAlerts != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Campaign, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Live on Digital Signage", fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(translatedAlerts!!)
                }
            }
        }
    }
}
