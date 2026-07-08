package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.EventSeat
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.MainViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(viewModel: MainViewModel) {
    var activeTab by remember { mutableStateOf(0) } // 0 = Fan, 1 = Operations

    // Fan Fields
    var fanName by remember { mutableStateOf("") }
    var fanTicketId by remember { mutableStateOf("") }
    var fanSeat by remember { mutableStateOf("") }
    var fanError by remember { mutableStateOf("") }

    // Staff Fields
    var staffId by remember { mutableStateOf("") }
    var staffPin by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("Operations Manager") }
    var showRoleMenu by remember { mutableStateOf(false) }
    var staffError by remember { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current

    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isVisible = true
    }

    val infiniteTransition = rememberInfiniteTransition(label = "stadium_lights")
    val alphaLight by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "alpha_lights"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgMain)
    ) {
        // Stadium light beams background
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            
            // Draw radial beam paths representing Estadio Azteca arc lights
            val beamLeft = Path().apply {
                moveTo(0f, 0f)
                lineTo(width * 0.4f, height)
                lineTo(width * 0.2f, height)
                close()
            }
            val beamRight = Path().apply {
                moveTo(width, 0f)
                lineTo(width * 0.8f, height)
                lineTo(width * 0.6f, height)
                close()
            }
            
            drawPath(beamLeft, Brush.linearGradient(listOf(ColorAiBlue.copy(alpha = alphaLight), Color.Transparent)))
            drawPath(beamRight, Brush.linearGradient(listOf(ColorAiPurple.copy(alpha = alphaLight), Color.Transparent)))
        }

        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(animationSpec = tween(1000)) + slideInVertically(
                initialOffsetY = { 100 },
                animationSpec = tween(1000, easing = FastOutSlowInEasing)
            ),
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .windowInsetsPadding(WindowInsets.safeDrawing),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
            // Header Soccer Emblem
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Brush.linearGradient(listOf(ColorAiGradientStart, ColorAiGradientEnd)), CircleShape)
                    .padding(2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF0F172A), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.SportsSoccer, 
                        contentDescription = "Soccer logo", 
                        tint = Color.White, 
                        modifier = Modifier.size(44.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Brand Header
            Text(
                text = "FIFA WORLD CUP 2026™",
                color = AccentLight,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            Text(
                text = "MATCHDAY AI PORTAL",
                color = TextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp
            )
            Text(
                text = "Estadio Azteca • CDMX",
                color = TextSecondary,
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Selector Tabs (Fan vs Staff)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .liquidGlass(shape = RoundedCornerShape(20.dp))
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Fan tab
                    val fanTabBg by animateColorAsState(
                        targetValue = if (activeTab == 0) Color(0xFF1E293B) else Color.Transparent,
                        label = "fan_bg"
                    )
                    val fanTabTextColor by animateColorAsState(
                        targetValue = if (activeTab == 0) AccentLight else TextSecondary,
                        label = "fan_text"
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(16.dp))
                            .background(fanTabBg)
                            .clickable { activeTab = 0 }
                            .testTag("tab_fan"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "FAN PORTAL",
                            color = fanTabTextColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    // Operations tab
                    val opsTabBg by animateColorAsState(
                        targetValue = if (activeTab == 1) Color(0xFF1E293B) else Color.Transparent,
                        label = "ops_bg"
                    )
                    val opsTabTextColor by animateColorAsState(
                        targetValue = if (activeTab == 1) AccentLight else TextSecondary,
                        label = "ops_text"
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(16.dp))
                            .background(opsTabBg)
                            .clickable { activeTab = 1 }
                            .testTag("tab_staff"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "OPERATIONS",
                            color = opsTabTextColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Body Area (Dynamic Form content)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
            ) {
                AnimatedContent(
                    targetState = activeTab,
                    transitionSpec = {
                        (slideInHorizontally { width -> if (targetState > initialState) width else -width } + fadeIn()) togetherWith
                        (slideOutHorizontally { width -> if (targetState > initialState) -width else width } + fadeOut())
                    },
                    label = "tab_content_animation"
                ) { targetTab ->
                    if (targetTab == 0) {
                    // FAN PORTAL VIEW
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Check in with your ticket reference to access live assistant, personalized wayfinding, and seat-specific services.",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )

                        OutlinedTextField(
                            value = fanName,
                            onValueChange = { fanName = it; fanError = "" },
                            label = { Text("Full Name") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = AccentLight) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AccentLight,
                                unfocusedBorderColor = GlassBorder,
                                focusedLabelColor = AccentLight,
                                unfocusedLabelColor = TextSecondary,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth().testTag("fan_name_input")
                        )

                        OutlinedTextField(
                            value = fanTicketId,
                            onValueChange = { fanTicketId = it; fanError = "" },
                            label = { Text("Ticket Ref (e.g. WC-CDMX-402)") },
                            leadingIcon = { Icon(Icons.Default.ConfirmationNumber, contentDescription = null, tint = AccentLight) },
                            singleLine = true,
                            placeholder = { Text("WC-CDMX-402", color = TextSecondary.copy(alpha = 0.5f)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AccentLight,
                                unfocusedBorderColor = GlassBorder,
                                focusedLabelColor = AccentLight,
                                unfocusedLabelColor = TextSecondary,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth().testTag("fan_ticket_input")
                        )

                        OutlinedTextField(
                            value = fanSeat,
                            onValueChange = { fanSeat = it; fanError = "" },
                            label = { Text("Seat block (e.g. 104-B)") },
                            leadingIcon = { Icon(Icons.Default.EventSeat, contentDescription = null, tint = AccentLight) },
                            singleLine = true,
                            placeholder = { Text("104-B", color = TextSecondary.copy(alpha = 0.5f)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AccentLight,
                                unfocusedBorderColor = GlassBorder,
                                focusedLabelColor = AccentLight,
                                unfocusedLabelColor = TextSecondary,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth().testTag("fan_seat_input")
                        )

                        if (fanError.isNotEmpty()) {
                            Text(fanError, color = ColorCritical, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                keyboardController?.hide()
                                if (fanName.isBlank()) {
                                    fanError = "Please enter your name."
                                } else if (fanTicketId.isBlank()) {
                                    fanError = "Ticket reference ID is required."
                                } else if (fanSeat.isBlank()) {
                                    fanError = "Please assign a seat block to guide you."
                                } else {
                                    viewModel.loginAsFan(fanName, fanTicketId, fanSeat)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .testTag("fan_login_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues(),
                            shape = RoundedCornerShape(28.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Brush.linearGradient(listOf(Color(0xFF003366), Color(0xFF0055FF))))
                                    .border(1.dp, ColorAiBlue.copy(alpha = 0.4f), RoundedCornerShape(28.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("ENTER FAN EXPERIENCE", color = Color.White, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                } else {
                    // OPERATIONS PORTAL VIEW
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Access unified operational metrics, real-time gate surges, live HD CCTV feeds, and GenAI-powered dispatch intelligence.",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )

                        OutlinedTextField(
                            value = staffId,
                            onValueChange = { staffId = it; staffError = "" },
                            label = { Text("Operations ID") },
                            leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null, tint = ColorAiPurple) },
                            singleLine = true,
                            placeholder = { Text("STAFF-2026", color = TextSecondary.copy(alpha = 0.5f)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ColorAiPurple,
                                unfocusedBorderColor = GlassBorder,
                                focusedLabelColor = ColorAiPurple,
                                unfocusedLabelColor = TextSecondary,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth().testTag("staff_id_input")
                        )

                        OutlinedTextField(
                            value = staffPin,
                            onValueChange = { staffPin = it; staffError = "" },
                            label = { Text("Security Pin") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = ColorAiPurple) },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ColorAiPurple,
                                unfocusedBorderColor = GlassBorder,
                                focusedLabelColor = ColorAiPurple,
                                unfocusedLabelColor = TextSecondary,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth().testTag("staff_pin_input")
                        )

                        // Role Selector
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showRoleMenu = true }
                                    .liquidGlass(shape = RoundedCornerShape(16.dp))
                                    .testTag("staff_role_dropdown")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text("ASSIGNED OPERATIONAL ROLE", fontSize = 10.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                                        Text(selectedRole, fontSize = 14.sp, color = TextPrimary, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 2.dp))
                                    }
                                    Text("▾", color = TextSecondary, fontSize = 16.sp)
                                }
                            }
                            DropdownMenu(
                                expanded = showRoleMenu,
                                onDismissRequest = { showRoleMenu = false },
                                modifier = Modifier.background(Color(0xFF0F172A))
                            ) {
                                listOf("Operations Manager", "Security Lead", "Volunteer Lead", "Transit Coordinator").forEach { role ->
                                    DropdownMenuItem(
                                        text = { Text(role, color = TextPrimary) },
                                        onClick = {
                                            selectedRole = role
                                            showRoleMenu = false
                                        }
                                    )
                                }
                            }
                        }

                        if (staffError.isNotEmpty()) {
                            Text(staffError, color = ColorCritical, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                keyboardController?.hide()
                                if (staffId.isBlank()) {
                                    staffError = "Please specify your Operations ID."
                                } else if (staffPin.isBlank()) {
                                    staffError = "Please enter your security access code."
                                } else {
                                    viewModel.loginAsStaff(staffId, selectedRole)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .testTag("staff_login_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues(),
                            shape = RoundedCornerShape(28.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Brush.linearGradient(listOf(ColorAiPurple, Color(0xFF6D28D9))))
                                    .border(1.dp, ColorAiPurple.copy(alpha = 0.4f), RoundedCornerShape(28.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("AUTHORIZE OPERATIONS ACCESS", color = Color.White, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }
            }
        }
    }
}
}
