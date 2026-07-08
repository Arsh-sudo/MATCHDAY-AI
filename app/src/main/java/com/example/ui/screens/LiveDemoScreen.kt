package com.example.ui.screens

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.viewmodel.MainViewModel
import com.example.viewmodel.ChatMessage
import com.example.ui.theme.*


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun LiveDemoScreen(viewModel: MainViewModel, onBack: () -> Unit) {
    val messages by viewModel.messages.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val typingStatus by viewModel.typingStatus.collectAsStateWithLifecycle()
    val currentRoleState by viewModel.currentRole.collectAsStateWithLifecycle()
    val currentLanguageState by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val loggedInUserType by viewModel.loggedInUserType.collectAsStateWithLifecycle()
    val fanSeat by viewModel.fanSeat.collectAsStateWithLifecycle()
    val fanName by viewModel.fanName.collectAsStateWithLifecycle()
    
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    var showClearDialog by remember { mutableStateOf(false) }
    
    val context = androidx.compose.ui.platform.LocalContext.current
    val speechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val spokenText = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()
                if (!spokenText.isNullOrEmpty()) {
                    inputText = spokenText
                }
            }
        }
    )

    val recordAudioPermissionLauncher = rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, java.util.Locale.getDefault())
                    putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to MATCHDAY AI...")
                }
                try { speechRecognizerLauncher.launch(intent) } catch (e: Exception) { }
            }
        }
    )
    
    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Clear Chat History?", color = TextPrimary) },
            text = { Text("This action cannot be undone.", color = TextSecondary) },
            containerColor = Color(0xFF0C2410),
            confirmButton = {
                TextButton(onClick = {
                    viewModel.clearHistory()
                    showClearDialog = false
                }) {
                    Text("Clear", color = ColorCritical)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Cancel", color = TextPrimary)
                }
            }
        )
    }
    
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isVisible = true
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(bottom = 16.dp), // Position input box neatly above floating navigation bar
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Header Row
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(tween(800)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("back_button")
                ) {
                    Icon(Icons.Default.ChevronLeft, contentDescription = "Back", tint = TextPrimary)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "GEMINI OPERATIONS ASSISTANT",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "AI Copilot",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextSecondary
                    )
                }
                var showMenu by remember { mutableStateOf(false) }
                Box {
                    IconButton(
                        onClick = { showMenu = true },
                        modifier = Modifier.testTag("more_options_button")
                    ) {
                        Icon(Icons.Default.MoreHoriz, contentDescription = "More", tint = TextPrimary)
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        modifier = Modifier.background(BgMain)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Clear Chat History", color = ColorCritical) },
                            onClick = {
                                showClearDialog = true
                                showMenu = false
                            }
                        )
                    }
                }
            }
        }
        
        // Role & Language Dropdowns Row
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInHorizontally(initialOffsetX = { -it }) + fadeIn(tween(800, delayMillis = 150)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                var showRoleMenu by remember { mutableStateOf(false) }
                var showLangMenu by remember { mutableStateOf(false) }

                // Role Selector Pill
                Box(modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = loggedInUserType != "fan") { showRoleMenu = true }
                            .liquidGlass(shape = RoundedCornerShape(12.dp))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = if (loggedInUserType == "fan") "ASSIGNED SEAT" else "PERSONA", 
                                    fontSize = 9.sp, 
                                    color = TextSecondary, 
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (loggedInUserType == "fan") "Block $fanSeat" else currentRoleState, 
                                    fontSize = 12.sp, 
                                    color = TextPrimary, 
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            if (loggedInUserType != "fan") {
                                Text("▾", color = TextSecondary, fontSize = 14.sp)
                            }
                        }
                    }
                    if (loggedInUserType != "fan") {
                        DropdownMenu(
                            expanded = showRoleMenu,
                            onDismissRequest = { showRoleMenu = false },
                            modifier = Modifier.background(BgMain)
                        ) {
                            listOf("Operations Manager", "Security Lead", "Volunteer Lead", "Transit Coordinator").forEach { role ->
                                DropdownMenuItem(
                                    text = { Text(role, color = TextPrimary) },
                                    onClick = {
                                        viewModel.setRole(role)
                                        showRoleMenu = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Language Selector Pill
                Box(modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showLangMenu = true }
                            .liquidGlass(shape = RoundedCornerShape(12.dp))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("LANGUAGE", fontSize = 9.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                                Text(currentLanguageState, fontSize = 12.sp, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                            }
                            Text("▾", color = TextSecondary, fontSize = 14.sp)
                        }
                    }
                    DropdownMenu(
                        expanded = showLangMenu,
                        onDismissRequest = { showLangMenu = false },
                        modifier = Modifier.background(BgMain)
                    ) {
                        listOf("English", "Español", "Português", "Français").forEach { lang ->
                            DropdownMenuItem(
                                text = { Text(lang, color = TextPrimary) },
                                onClick = {
                                    viewModel.setLanguage(lang)
                                    showLangMenu = false
                                }
                            )
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // 2. Scrollable Messages View
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(tween(800, delayMillis = 300)),
            modifier = Modifier.weight(1f).fillMaxWidth()
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(messages) { message ->
                    ChatBubbleItem(message, loggedInUserType, fanName)
                }
                
                if (isLoading) {
                    item {
                        TypingIndicatorItem(typingStatus)
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 3. Suggested Actions
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(tween(800, delayMillis = 400)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text("Suggested Actions", color = TextSecondary, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(listOf("Show Crowd Status", "Traffic Update", "Medical Emergencies", "Open Incidents", "Volunteer Deployment")) { action ->
                        ActionChip(text = action, isLoading = isLoading, onClick = {
                            if (!isLoading) viewModel.sendMessage(action)
                        })
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 4. Input Bar with Native Voice Typing and Send functionality
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(tween(800, delayMillis = 500)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(BgCard, RoundedCornerShape(28.dp))
                    .border(1.dp, GlassBorder, RoundedCornerShape(28.dp))
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("Ask MATCHDAY AI...", color = TextSecondary, fontSize = 14.sp) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        cursorColor = ColorAiBlue
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                        .testTag("chat_input_field"),
                    singleLine = true
                )
                
                IconButton(
                    onClick = {
                        if (androidx.core.content.ContextCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                                putExtra(RecognizerIntent.EXTRA_LANGUAGE, java.util.Locale.getDefault())
                                putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to MATCHDAY AI...")
                            }
                            try {
                                speechRecognizerLauncher.launch(intent)
                            } catch (e: Exception) {
                                // Safe fallback if recognizer activity is unavailable
                            }
                        } else {
                            recordAudioPermissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
                        }
                    },
                    modifier = Modifier.testTag("mic_button")
                ) {
                    Icon(
                        Icons.Default.Mic,
                        contentDescription = "Mic",
                        tint = if (inputText.isEmpty()) TextSecondary else ColorAiPurple,
                        modifier = Modifier.size(24.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            viewModel.sendMessage(inputText)
                            inputText = ""
                        }
                    },
                    enabled = inputText.isNotBlank() && !isLoading,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            if (inputText.isNotBlank() && !isLoading) {
                                Brush.linearGradient(listOf(ColorAiPurple, ColorAiBlue))
                            } else {
                                Brush.linearGradient(listOf(Color(0xFF102F15), Color(0xFF06140A)))
                            }
                        )
                        .testTag("send_button")
                ) {
                    Icon(
                        Icons.Default.Send,
                        contentDescription = "Send",
                        tint = if (inputText.isNotBlank() && !isLoading) Color.White else TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ChatBubbleItem(message: ChatMessage, loggedInUserType: String, fanName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        if (!message.isUser) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(ColorAiBlue.copy(alpha = 0.15f))
                    .border(1.dp, ColorAiBlue.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = "AI", tint = ColorAiBlue, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .liquidGlass(
                    shape = if (message.isUser) {
                        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 4.dp)
                    } else {
                        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 4.dp)
                    },
                    topBorderColor = if (message.isUser) Color(0xFF34D399).copy(alpha = 0.5f) else Color(0xFF84CC16).copy(alpha = 0.4f),
                    bottomBorderColor = if (message.isUser) Color(0xFF10B981).copy(alpha = 0.2f) else Color(0xFF4D7C0F).copy(alpha = 0.1f),
                    bgStartColor = if (message.isUser) Color(0xFF15803D).copy(alpha = 0.85f) else Color(0xFF1A351E).copy(alpha = 0.70f),
                    bgEndColor = if (message.isUser) Color(0xFF0F5125).copy(alpha = 0.95f) else Color(0xFF0A1F0D).copy(alpha = 0.85f)
                )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = message.text,
                    color = if (message.isUser) Color.White else TextPrimary,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
        }

        if (message.isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            val avatarText = if (loggedInUserType == "fan") fanName.take(2).uppercase() else "OP"
            val avatarColor = if (loggedInUserType == "fan") ColorAiBlue else ColorAiPurple
            
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(avatarColor.copy(alpha = 0.15f))
                    .border(1.dp, avatarColor.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(avatarText, color = avatarColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun TypingIndicatorItem(typingStatus: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(ColorAiBlue.copy(alpha = 0.15f))
                .border(1.dp, ColorAiBlue.copy(alpha = 0.3f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.AutoAwesome, contentDescription = "AI", tint = ColorAiBlue, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .liquidGlass(
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 4.dp)
                )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = if (typingStatus.isNotEmpty()) typingStatus else "MATCHDAY AI is typing...",
                    color = TextSecondary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth().height(2.dp),
                    color = ColorAiBlue,
                    trackColor = Color(0xFF102F15)
                )
            }
        }
    }
}

@Composable
fun ActionChip(text: String, isLoading: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clickable(enabled = !isLoading, onClick = onClick)
            .liquidGlass(
                shape = RoundedCornerShape(20.dp),
                topBorderColor = Color.White.copy(alpha = if (isLoading) 0.1f else 0.35f),
                bottomBorderColor = Color.White.copy(alpha = 0.03f),
                bgStartColor = Color(0xFF163E1F).copy(alpha = if (isLoading) 0.15f else 0.45f),
                bgEndColor = Color(0xFF06140A).copy(alpha = if (isLoading) 0.25f else 0.65f)
            )
            .testTag("action_chip_${text.lowercase().replace(" ", "_")}")
    ) {
        Text(
            text = text,
            color = if (isLoading) TextSecondary.copy(alpha = 0.5f) else TextPrimary,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}
