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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Security
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.MainViewModel
import com.example.ui.theme.*
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.abs
import android.graphics.Bitmap
import android.graphics.Canvas as AndroidCanvas
import android.graphics.Paint
import android.graphics.PorterDuff
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.toArgb
import androidx.compose.runtime.mutableStateListOf

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

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Flat 2D football ground (pitch) with white lines, goal posts, corner flags and a phone-tilt moving 2D ball
        FootballPitchBackground()

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
                        .background(BgMain, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    CustomLogo(
                        modifier = Modifier.size(44.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Brand Header
            Text(
                text = "FIFA WORLD CUP 2026™",
                color = com.example.ui.theme.AccentGold,
                fontSize = 15.6.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            Text(
                text = "MATCHDAY AI",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
            Text(
                text = "Your AI powered companion for a smarter, smoother match experience",
                color = TextSecondary,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
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
                        targetValue = if (activeTab == 0) Color(0xFF153D1C) else Color.Transparent,
                        label = "fan_bg"
                    )
                    val fanTabTextColor by animateColorAsState(
                        targetValue = if (activeTab == 0) Color.White else TextSecondary,
                        label = "fan_text"
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(16.dp))
                            .background(fanTabBg)
                            .border(if (activeTab == 0) 1.dp else 0.dp, if (activeTab == 0) com.example.ui.theme.AccentGold else Color.Transparent, RoundedCornerShape(16.dp))
                            .clickable { activeTab = 0 }
                            .testTag("tab_fan"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "FAN PORTAL",
                            color = fanTabTextColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.6.sp
                        )
                    }

                    // Operations tab
                    val opsTabBg by animateColorAsState(
                        targetValue = if (activeTab == 1) Color(0xFF153D1C) else Color.Transparent,
                        label = "ops_bg"
                    )
                    val opsTabTextColor by animateColorAsState(
                        targetValue = if (activeTab == 1) Color.White else TextSecondary,
                        label = "ops_text"
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(16.dp))
                            .background(opsTabBg)
                            .border(if (activeTab == 1) 1.dp else 0.dp, if (activeTab == 1) com.example.ui.theme.AccentGold else Color.Transparent, RoundedCornerShape(16.dp))
                            .clickable { activeTab = 1 }
                            .testTag("tab_staff"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "OPERATIONS",
                            color = opsTabTextColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.6.sp
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .liquidGlass(shape = RoundedCornerShape(24.dp))
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Welcome! 👋",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Enter your details to access your personalized Matchday AI experience.",
                            color = TextSecondary,
                            fontSize = 15.6.sp,
                            lineHeight = 23.4.sp
                        )

                        OutlinedTextField(
                            value = fanName,
                            onValueChange = { fanName = it; fanError = "" },
                            label = { Text("Full Name", color = Color.White, fontWeight = FontWeight.SemiBold) },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = com.example.ui.theme.AccentGold) },
                            singleLine = true,
                            placeholder = { Text("Enter your full name", color = TextSecondary) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedLabelColor = Color.White,
                                unfocusedLabelColor = Color.White,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .liquidGlass(shape = RoundedCornerShape(16.dp))
                                .testTag("fan_name_input")
                        )

                        OutlinedTextField(
                            value = fanTicketId,
                            onValueChange = { fanTicketId = it; fanError = "" },
                            label = { Text("Ticket Reference Number", color = Color.White, fontWeight = FontWeight.SemiBold) },
                            leadingIcon = { Icon(Icons.Default.ConfirmationNumber, contentDescription = null, tint = com.example.ui.theme.AccentGold) },
                            singleLine = true,
                            placeholder = { Text("Enter ticket ref (e.g. WC-CDMX-402)", color = TextSecondary) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedLabelColor = Color.White,
                                unfocusedLabelColor = Color.White,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .liquidGlass(shape = RoundedCornerShape(16.dp))
                                .testTag("fan_ticket_input")
                        )

                        OutlinedTextField(
                            value = fanSeat,
                            onValueChange = { fanSeat = it; fanError = "" },
                            label = { Text("Seat Block Number", color = Color.White, fontWeight = FontWeight.SemiBold) },
                            leadingIcon = { Icon(Icons.Default.EventSeat, contentDescription = null, tint = com.example.ui.theme.AccentGold) },
                            singleLine = true,
                            placeholder = { Text("Enter seat block (e.g. 104-B)", color = TextSecondary) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedLabelColor = Color.White,
                                unfocusedLabelColor = Color.White,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .liquidGlass(shape = RoundedCornerShape(16.dp))
                                .testTag("fan_seat_input")
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
                                .height(64.dp)
                                .testTag("fan_login_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues(),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .liquidGlass(
                                        shape = RoundedCornerShape(24.dp),
                                        topBorderColor = com.example.ui.theme.AccentGold.copy(alpha = 0.8f),
                                        bottomBorderColor = com.example.ui.theme.AccentGoldDark.copy(alpha = 0.4f),
                                        bgStartColor = com.example.ui.theme.AccentGold.copy(alpha = 0.7f),
                                        bgEndColor = com.example.ui.theme.AccentGoldDark.copy(alpha = 0.9f)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Icon(Icons.Default.Lock, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                                    Text("ACCESS MATCHDAY AI", color = Color.White, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .border(1.dp, Color.White.copy(alpha = 0.5f), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                        
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = com.example.ui.theme.AccentGold, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Your information is secure and used only\nto personalize your stadium experience.",
                                color = TextSecondary,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }
                } else {
                    // OPERATIONS PORTAL VIEW
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .liquidGlass(shape = RoundedCornerShape(24.dp))
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Welcome! 👋",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Enter your details to access your personalized Matchday AI experience.",
                            color = TextSecondary,
                            fontSize = 15.6.sp,
                            lineHeight = 23.4.sp
                        )

                        OutlinedTextField(
                            value = staffId,
                            onValueChange = { staffId = it; staffError = "" },
                            label = { Text("Operations ID", color = Color.White, fontWeight = FontWeight.SemiBold) },
                            leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null, tint = com.example.ui.theme.AccentGold) },
                            singleLine = true,
                            placeholder = { Text("Enter ID (e.g. STAFF-2026)", color = TextSecondary) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedLabelColor = Color.White,
                                unfocusedLabelColor = Color.White,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .liquidGlass(shape = RoundedCornerShape(16.dp))
                                .testTag("staff_id_input")
                        )

                        OutlinedTextField(
                            value = staffPin,
                            onValueChange = { staffPin = it; staffError = "" },
                            label = { Text("Security Pin", color = Color.White, fontWeight = FontWeight.SemiBold) },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = com.example.ui.theme.AccentGold) },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            placeholder = { Text("Enter security pin", color = TextSecondary) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedLabelColor = Color.White,
                                unfocusedLabelColor = Color.White,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .liquidGlass(shape = RoundedCornerShape(16.dp))
                                .testTag("staff_pin_input")
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
                                        Text("ASSIGNED OPERATIONAL ROLE", fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                                        Text(selectedRole, fontSize = 14.sp, color = TextSecondary, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 2.dp))
                                    }
                                    Text("▾", color = TextSecondary, fontSize = 16.sp)
                                }
                            }
                            DropdownMenu(
                                expanded = showRoleMenu,
                                onDismissRequest = { showRoleMenu = false },
                                modifier = Modifier.background(BgMain)
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
                                .height(64.dp)
                                .testTag("staff_login_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues(),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .liquidGlass(
                                        shape = RoundedCornerShape(24.dp),
                                        topBorderColor = com.example.ui.theme.AccentGold.copy(alpha = 0.8f),
                                        bottomBorderColor = com.example.ui.theme.AccentGoldDark.copy(alpha = 0.4f),
                                        bgStartColor = com.example.ui.theme.AccentGold.copy(alpha = 0.7f),
                                        bgEndColor = com.example.ui.theme.AccentGoldDark.copy(alpha = 0.9f)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Icon(Icons.Default.Lock, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                                    Text("AUTHORIZE ACCESS", color = Color.White, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .border(1.dp, Color.White.copy(alpha = 0.5f), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                        
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = com.example.ui.theme.AccentGold, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Your information is secure and used only\nto personalize your stadium experience.",
                                color = TextSecondary,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }
            }
        }
    }
}
}

class RetroPaintResources {
    val paintGrass = Paint()
    val linePaint = Paint().apply {
        color = android.graphics.Color.argb((0.45f * 255).toInt(), 255, 255, 255)
        style = Paint.Style.STROKE
        strokeWidth = 1f
    }
    val fillWhitePaint = Paint().apply {
        color = android.graphics.Color.argb((0.45f * 255).toInt(), 255, 255, 255)
        style = Paint.Style.FILL
    }
    val arcPaint = Paint().apply {
        color = android.graphics.Color.argb((0.45f * 255).toInt(), 255, 255, 255)
        style = Paint.Style.STROKE
        strokeWidth = 1f
    }
    val goalPaint = Paint().apply {
        color = android.graphics.Color.argb((0.85f * 255).toInt(), 255, 255, 255)
        style = Paint.Style.STROKE
        strokeWidth = 1f
    }
    val netPaint = Paint().apply {
        color = android.graphics.Color.argb((0.25f * 255).toInt(), 255, 255, 255)
        strokeWidth = 0.5f
    }
    val polePaint = Paint().apply {
        color = 0xFFE29578.toInt()
        strokeWidth = 0.5f
    }
    val flagPaint = Paint().apply {
        color = 0xFFEF4444.toInt()
        style = Paint.Style.FILL
    }
    val shadowPaint = Paint().apply {
        color = android.graphics.Color.argb((0.35f * 255).toInt(), 0, 0, 0)
        style = Paint.Style.FILL
    }
    val whiteBallPaint = Paint().apply {
        color = android.graphics.Color.WHITE
        style = Paint.Style.FILL
    }
    val blackBallPaint = Paint().apply {
        color = 0xFF1E293B.toInt()
        style = Paint.Style.FILL
    }
    val confettiPaint = Paint().apply {
        style = Paint.Style.FILL
    }
    val textShadowPaint = Paint()
    val textMainPaint = Paint()
}

@Composable
fun FootballPitchBackground() {
    val context = LocalContext.current
    val sensorValues = remember { FloatArray(2) }

    DisposableEffect(context) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event != null) {
                    sensorValues[0] = -event.values[0] * 0.25f
                    sensorValues[1] = event.values[1] * 0.25f
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
        if (sensor != null) {
            sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_GAME)
        }
        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val density = LocalDensity.current
        val widthPx = with(density) { maxWidth.toPx() }
        val heightPx = with(density) { maxHeight.toPx() }

        // Low-res pixelated scale
        val pixelScale = 8f
        val w = (widthPx / pixelScale).toInt().coerceAtLeast(1)
        val h = (heightPx / pixelScale).toInt().coerceAtLeast(1)

        var ballX by remember { mutableStateOf(0f) }
        var ballY by remember { mutableStateOf(0f) }
        var vx by remember { mutableStateOf(0f) }
        var vy by remember { mutableStateOf(0f) }
        val ballRadius = 3.5f

        val confettiList = remember { mutableStateListOf<ConfettiParticle>() }
        val goalState = remember { GoalAnimationState() }

        var initialized by remember { mutableStateOf(false) }
        if (!initialized && w > 1 && h > 1) {
            ballX = w / 2f
            ballY = h / 2f
            initialized = true
        }

        // Physics Update loop using withFrameNanos
        var lastTimeNanos by remember { mutableStateOf(0L) }
        LaunchedEffect(initialized, w, h) {
            if (!initialized) return@LaunchedEffect
            lastTimeNanos = System.nanoTime()
            while (true) {
                withFrameNanos { frameTimeNanos ->
                    if (lastTimeNanos == 0L) {
                        lastTimeNanos = frameTimeNanos
                        return@withFrameNanos
                    }
                    val dt = ((frameTimeNanos - lastTimeNanos) / 1_000_000_000f).coerceIn(0.005f, 0.033f)
                    lastTimeNanos = frameTimeNanos

                    val bounceElasticity = 0.5f
                    val friction = 0.97f

                    // Retrieve current sensor values inside physics loop
                    val ax = sensorValues[0]
                    val ay = sensorValues[1]
                    val targetAx = if (abs(ax) < 0.05f && abs(ay) < 0.05f) 0f else ax
                    val targetAy = if (abs(ax) < 0.05f && abs(ay) < 0.05f) 0f else ay

                    // Accelerate ball with tilting (scaled for low-res width/height)
                    vx = (vx + targetAx * dt * 450f) * friction
                    vy = (vy + targetAy * dt * 450f) * friction

                    ballX += vx * dt
                    ballY += vy * dt

                    // Update goal state animation
                    if (goalState.active) {
                        goalState.timer += dt
                        val t = goalState.timer
                        if (t < 0.3f) {
                            val progress = t / 0.3f
                            goalState.alpha = progress
                            goalState.scale = if (progress < 0.7f) {
                                (progress / 0.7f) * 1.3f
                            } else {
                                1.3f - ((progress - 0.7f) / 0.3f) * 0.3f
                            }
                        } else if (t < 2.0f) {
                            goalState.alpha = 1f
                            goalState.scale = 1f
                        } else if (t < 2.5f) {
                            val progress = (t - 2.0f) / 0.5f
                            goalState.alpha = 1f - progress
                            goalState.scale = 1f
                        } else {
                            // Reset everything
                            goalState.active = false
                            ballX = w / 2f
                            ballY = h / 2f
                            vx = 0f
                            vy = 0f
                        }
                    }

                    // Update confetti particles
                    if (confettiList.isNotEmpty()) {
                        val iterator = confettiList.iterator()
                        while (iterator.hasNext()) {
                            val p = iterator.next()
                            p.y += p.vy * dt
                            p.x += p.vx * dt + sin(p.y * 0.15f) * 0.25f
                            if (p.y > h / 2f + 15f) {
                                iterator.remove()
                            }
                        }
                    }

                    // Pitch Lines Padding & Dimensions in low-res pixels
                    val pad = 4f
                    val pw = w - 2 * pad
                    val ph = h - 2 * pad
                    val goalWidth = pw * 0.22f
                    val goalDepth = 3f
                    val goalLeft = pad + (pw - goalWidth) / 2f
                    val goalRight = goalLeft + goalWidth

                    // Collision bounds & goal logic
                    if (w > 0 && h > 0) {
                        // Horizontal Bounds
                        if (ballX < ballRadius) {
                            ballX = ballRadius
                            vx = -vx * bounceElasticity
                        } else if (ballX > w - ballRadius) {
                            ballX = w - ballRadius
                            vx = -vx * bounceElasticity
                        }

                        // Vertical Bounds & Goal Detection
                        val isBetweenGoalPosts = ballX >= goalLeft && ballX <= goalRight
                        if (isBetweenGoalPosts) {
                            // Top goal detection
                            if (ballY < pad) {
                                if (ballY < pad - goalDepth + ballRadius) {
                                    ballY = pad - goalDepth + ballRadius
                                    vy = -vy * bounceElasticity
                                }
                                // Trigger Goal Event!
                                if (!goalState.active) {
                                    triggerGoal(
                                        isTopGoal = true,
                                        w = w.toFloat(),
                                        goalState = goalState,
                                        confettiList = confettiList
                                    )
                                }
                            } else if (ballY > h - pad) { // Bottom goal detection
                                if (ballY > h - pad + goalDepth - ballRadius) {
                                    ballY = h - pad + goalDepth - ballRadius
                                    vy = -vy * bounceElasticity
                                }
                                // Trigger Goal Event!
                                if (!goalState.active) {
                                    triggerGoal(
                                        isTopGoal = false,
                                        w = w.toFloat(),
                                        goalState = goalState,
                                        confettiList = confettiList
                                    )
                                }
                            }
                        } else {
                            // Normal bounds collision
                            if (ballY < pad + ballRadius) {
                                ballY = pad + ballRadius
                                vy = -vy * bounceElasticity
                            } else if (ballY > h - pad - ballRadius) {
                                ballY = h - pad - ballRadius
                                vy = -vy * bounceElasticity
                            }
                        }
                    }
                }
            }
        }

        // Low-resolution Bitmap for Pixelated styling
        val retroBitmap = remember(w, h) {
            if (w > 0 && h > 0) {
                Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            } else {
                null
            }
        }

        val paints = remember { RetroPaintResources() }

        // Draw low-res bitmap scaled up using Nearest-Neighbor interpolation (FilterQuality.None)
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { tapOffset ->
                        val lowResTapX = tapOffset.x / pixelScale
                        val lowResTapY = tapOffset.y / pixelScale
                        val dx = ballX - lowResTapX
                        val dy = ballY - lowResTapY
                        val distance = sqrt(dx * dx + dy * dy)
                        if (distance > 0f) {
                            // Apply a gentle kick force inversely proportional to tap distance
                            val force = (160f / (distance / 5f + 1f)).coerceIn(15f, 220f)
                            vx += (dx / distance) * force
                            vy += (dy / distance) * force
                        }
                    }
                }
        ) {
            // Read state values inside drawing block to trigger draw phase only, bypassing recomposition
            val bx = ballX
            val by = ballY
            val gsActive = goalState.active
            val gsIsTop = goalState.isTop
            val gsScale = goalState.scale
            val gsAlpha = goalState.alpha

            retroBitmap?.let { bitmap ->
                val canvas = android.graphics.Canvas(bitmap)
                canvas.drawColor(android.graphics.Color.TRANSPARENT, android.graphics.PorterDuff.Mode.CLEAR)

                val lowResW = w.toFloat()
                val lowResH = h.toFloat()

                // 1. Draw grass stripes
                val stripesCount = 14
                val stripeHeight = lowResH / stripesCount
                for (i in 0 until stripesCount) {
                    paints.paintGrass.color = if (i % 2 == 0) 0xFF133F17.toInt() else 0xFF103613.toInt()
                    canvas.drawRect(0f, i * stripeHeight, lowResW, (i + 1) * stripeHeight, paints.paintGrass)
                }

                // 2. White Lines (Field marking paint)
                val pad = 4f
                val pw = lowResW - 2 * pad
                val ph = lowResH - 2 * pad

                // Outer pitch line
                canvas.drawRect(pad, pad, lowResW - pad, lowResH - pad, paints.linePaint)

                // Midfield line
                val midY = lowResH / 2f
                canvas.drawLine(pad, midY, lowResW - pad, midY, paints.linePaint)

                // Center spot and circle
                val centerCircleRadius = 12f
                canvas.drawCircle(lowResW / 2f, midY, centerCircleRadius, paints.linePaint)
                canvas.drawCircle(lowResW / 2f, midY, 1f, paints.fillWhitePaint)

                // Penalty boxes
                val penBoxWidth = pw * 0.60f
                val penBoxHeight = ph * 0.12f
                val penBoxLeft = pad + (pw - penBoxWidth) / 2f
                canvas.drawRect(penBoxLeft, pad, penBoxLeft + penBoxWidth, pad + penBoxHeight, paints.linePaint)
                canvas.drawRect(penBoxLeft, lowResH - pad - penBoxHeight, penBoxLeft + penBoxWidth, lowResH - pad, paints.linePaint)

                // Goal areas
                val goalAreaWidth = pw * 0.28f
                val goalAreaHeight = ph * 0.04f
                val goalAreaLeft = pad + (pw - goalAreaWidth) / 2f
                canvas.drawRect(goalAreaLeft, pad, goalAreaLeft + goalAreaWidth, pad + goalAreaHeight, paints.linePaint)
                canvas.drawRect(goalAreaLeft, lowResH - pad - goalAreaHeight, goalAreaLeft + goalAreaWidth, lowResH - pad, paints.linePaint)

                // Penalty spots
                val penaltySpotDist = ph * 0.085f
                canvas.drawCircle(lowResW / 2f, pad + penaltySpotDist, 0.75f, paints.fillWhitePaint)
                canvas.drawCircle(lowResW / 2f, lowResH - pad - penaltySpotDist, 0.75f, paints.fillWhitePaint)

                // Corner Arcs
                val cornerRadius = 3f
                // Top-left
                canvas.drawArc(pad - cornerRadius, pad - cornerRadius, pad + cornerRadius, pad + cornerRadius, 0f, 90f, false, paints.arcPaint)
                // Top-right
                canvas.drawArc(lowResW - pad - cornerRadius, pad - cornerRadius, lowResW - pad + cornerRadius, pad + cornerRadius, 90f, 90f, false, paints.arcPaint)
                // Bottom-left
                canvas.drawArc(pad - cornerRadius, lowResH - pad - cornerRadius, pad + cornerRadius, lowResH - pad + cornerRadius, 270f, 90f, false, paints.arcPaint)
                // Bottom-right
                canvas.drawArc(lowResW - pad - cornerRadius, lowResH - pad - cornerRadius, lowResW - pad + cornerRadius, lowResH - pad + cornerRadius, 180f, 90f, false, paints.arcPaint)

                // 3. Goal posts
                val goalWidth = pw * 0.22f
                val goalDepth = 3f
                val goalLeft = pad + (pw - goalWidth) / 2f
                val goalRight = goalLeft + goalWidth

                // Top goal
                canvas.drawRect(goalLeft, pad - goalDepth, goalRight, pad, paints.goalPaint)
                // Net mesh vertical lines
                for (gx in 1..4) {
                    val stepX = goalWidth / 5f
                    canvas.drawLine(goalLeft + gx * stepX, pad, goalLeft + gx * stepX, pad - goalDepth, paints.netPaint)
                }

                // Bottom goal
                canvas.drawRect(goalLeft, lowResH - pad, goalRight, lowResH - pad + goalDepth, paints.goalPaint)
                for (gx in 1..4) {
                    val stepX = goalWidth / 5f
                    canvas.drawLine(goalLeft + gx * stepX, lowResH - pad, goalLeft + gx * stepX, lowResH - pad + goalDepth, paints.netPaint)
                }

                // 4. Corner Flags
                val flagPoleHeight = 3f
                val flagSize = 1.5f
                val cornerCoords = listOf(
                    Offset(pad, pad),
                    Offset(lowResW - pad, pad),
                    Offset(pad, lowResH - pad),
                    Offset(lowResW - pad, lowResH - pad)
                )
                cornerCoords.forEach { coord ->
                    // Pole
                    canvas.drawLine(coord.x, coord.y, coord.x, coord.y - flagPoleHeight, paints.polePaint)
                    // Red flag (pixel rect)
                    canvas.drawRect(coord.x, coord.y - flagPoleHeight, coord.x + flagSize, coord.y - flagPoleHeight + flagSize, paints.flagPaint)
                }

                // 5. Draw the 2D pixelated football!
                if (initialized) {
                    // Ball shadow
                    canvas.drawRect(bx - ballRadius, by - ballRadius + 0.5f, bx + ballRadius, by + ballRadius + 0.5f, paints.shadowPaint)

                    // White base (using rects for blocky style)
                    // Rounded blocky circle
                    canvas.drawRect(bx - 2.5f, by - 3.5f, bx + 2.5f, by + 3.5f, paints.whiteBallPaint)
                    canvas.drawRect(bx - 3.5f, by - 2.5f, bx + 3.5f, by + 2.5f, paints.whiteBallPaint)

                    // Inner retro patterns (pixel detailing)
                    // Central pixel block
                    canvas.drawRect(bx - 1f, by - 1f, bx + 1f, by + 1f, paints.blackBallPaint)
                    // Diagonal pixels to look like classic football panels
                    canvas.drawRect(bx - 2.5f, by - 2.5f, bx - 1.5f, by - 1.5f, paints.blackBallPaint)
                    canvas.drawRect(bx + 1.5f, by - 2.5f, bx + 2.5f, by - 1.5f, paints.blackBallPaint)
                    canvas.drawRect(bx - 2.5f, by + 1.5f, bx - 1.5f, by + 2.5f, paints.blackBallPaint)
                    canvas.drawRect(bx + 1.5f, by + 1.5f, bx + 2.5f, by + 2.5f, paints.blackBallPaint)
                }

                // 6. Confetti falling slowly (slow, colorful pixel squares)
                confettiList.forEach { p ->
                    paints.confettiPaint.color = p.color
                    canvas.drawRect(p.x, p.y, p.x + p.size, p.y + p.size, paints.confettiPaint)
                }

                // 7. GOAL! Text pop, fade-in, fade-out animation
                if (gsActive) {
                    val textY = if (gsIsTop) pad + 15f else lowResH - pad - 20f
                    drawPixelText(
                        text = "GOAL!",
                        centerX = lowResW / 2f,
                        centerY = textY,
                        size = 1.2f, // scaled retro letter pixels
                        color = 0xFFFFEB3B.toInt(), // Retro arcade yellow
                        scale = gsScale,
                        alpha = gsAlpha,
                        canvas = canvas,
                        shadowPaint = paints.textShadowPaint,
                        textPaint = paints.textMainPaint
                    )
                }
            }

            retroBitmap?.let { bitmap ->
                drawImage(
                    image = bitmap.asImageBitmap(),
                    dstSize = IntSize(size.width.toInt(), size.height.toInt()),
                    filterQuality = FilterQuality.None
                )
            }
        }
    }
}

// Helper trigger for Goals
fun triggerGoal(isTopGoal: Boolean, w: Float, goalState: GoalAnimationState, confettiList: MutableList<ConfettiParticle>) {
    if (goalState.active) return
    goalState.active = true
    goalState.isTop = isTopGoal
    goalState.timer = 0f
    goalState.scale = 0f
    goalState.alpha = 0f

    // Spawn 60 retro pixelated confetti pieces
    confettiList.clear()
    val random = java.util.Random()
    val colors = intArrayOf(
        0xFFFF5252.toInt(), // Vibrant Red
        0xFFFFEB3B.toInt(), // Arcade Yellow
        0xFF00E676.toInt(), // Bright Green
        0xFF40C4FF.toInt(), // Light Blue
        0xFFE040FB.toInt(), // Neon Pink
        0xFFFF9100.toInt()  // Bright Orange
    )

    for (i in 0 until 55) {
        val px = random.nextFloat() * w
        val py = -random.nextFloat() * 10f // Staggered spawn heights
        val pColor = colors[random.nextInt(colors.size)]
        val pvx = (random.nextFloat() - 0.5f) * 8f
        val pvy = 8f + random.nextFloat() * 12f // Fall slowly
        val pSize = 1f + random.nextFloat() * 1.5f

        confettiList.add(
            ConfettiParticle(
                x = px,
                y = py,
                color = pColor,
                vx = pvx,
                vy = pvy,
                size = pSize,
                rotation = 0f,
                rotationSpeed = 0f
            )
        )
    }
}

// Pixel letter rendering logic
fun drawPixelLetter(char: Char, x: Float, y: Float, size: Float, paint: android.graphics.Paint, canvas: android.graphics.Canvas) {
    val matrix = when (char.uppercaseChar()) {
        'G' -> intArrayOf(14, 17, 19, 17, 14)
        'O' -> intArrayOf(14, 17, 17, 17, 14)
        'A' -> intArrayOf(4, 10, 31, 17, 17)
        'L' -> intArrayOf(16, 16, 16, 16, 31)
        '!' -> intArrayOf(4, 4, 4, 0, 4)
        else -> intArrayOf(0, 0, 0, 0, 0)
    }
    for (row in 0 until 5) {
        val rowVal = matrix[row]
        for (col in 0 until 5) {
            val bit = (rowVal shr (4 - col)) and 1
            if (bit == 1) {
                canvas.drawRect(
                    x + col * size,
                    y + row * size,
                    x + (col + 1) * size,
                    y + (row + 1) * size,
                    paint
                )
            }
        }
    }
}

// Pixel text rendering logic
fun drawPixelText(
    text: String,
    centerX: Float,
    centerY: Float,
    size: Float,
    color: Int,
    scale: Float,
    alpha: Float,
    canvas: android.graphics.Canvas,
    shadowPaint: android.graphics.Paint,
    textPaint: android.graphics.Paint
) {
    val letterWidth = 5f * size
    val gap = 1f * size
    val totalWidth = text.length * letterWidth + (text.length - 1) * gap
    val startX = centerX - totalWidth / 2f
    val startY = centerY - (5f * size) / 2f

    canvas.save()
    canvas.scale(scale, scale, centerX, centerY)

    shadowPaint.color = android.graphics.Color.argb((alpha * 220).toInt(), 0, 0, 0)
    textPaint.color = android.graphics.Color.argb((alpha * 255).toInt(), 
        android.graphics.Color.red(color), 
        android.graphics.Color.green(color), 
        android.graphics.Color.blue(color))

    // Draw shadow first (bottom right offset by 0.6f pixel size)
    var curX = startX
    text.forEach { char ->
        drawPixelLetter(char, curX + 0.6f, startY + 0.6f, size, shadowPaint, canvas)
        curX += letterWidth + gap
    }

    // Draw main text
    curX = startX
    text.forEach { char ->
        drawPixelLetter(char, curX, startY, size, textPaint, canvas)
        curX += letterWidth + gap
    }

    canvas.restore()
}

data class ConfettiParticle(
    var x: Float,
    var y: Float,
    val color: Int,
    var vx: Float,
    var vy: Float,
    val size: Float,
    val rotation: Float,
    val rotationSpeed: Float
)

class GoalAnimationState {
    var active by mutableStateOf(false)
    var isTop by mutableStateOf(false)
    var scale by mutableStateOf(0f)
    var alpha by mutableStateOf(0f)
    var timer by mutableStateOf(0f)
}

@Composable
fun CustomLogo(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val radius = size.minDimension / 2
        val center = Offset(size.width / 2, size.height / 2)
        
        drawCircle(
            color = com.example.ui.theme.AccentGold,
            radius = radius * 0.9f,
            center = center,
            style = Stroke(width = (radius * 0.1f))
        )
        
        val pentagonRadius = radius * 0.35f
        val pentagonPath = Path()
        val angles = listOf(
            -Math.PI / 2,
            -Math.PI / 2 + 2 * Math.PI / 5,
            -Math.PI / 2 + 4 * Math.PI / 5,
            -Math.PI / 2 + 6 * Math.PI / 5,
            -Math.PI / 2 + 8 * Math.PI / 5,
        )
        
        angles.forEachIndexed { index, angle ->
            val x = center.x + (pentagonRadius * kotlin.math.cos(angle)).toFloat()
            val y = center.y + (pentagonRadius * kotlin.math.sin(angle)).toFloat()
            if (index == 0) {
                pentagonPath.moveTo(x, y)
            } else {
                pentagonPath.lineTo(x, y)
            }
        }
        pentagonPath.close()
        
        drawPath(
            path = pentagonPath,
            color = Color.White
        )
        
        val outerRadius = radius * 0.9f
        angles.forEach { angle ->
            val innerX = center.x + (pentagonRadius * kotlin.math.cos(angle)).toFloat()
            val innerY = center.y + (pentagonRadius * kotlin.math.sin(angle)).toFloat()
            val outerX = center.x + (outerRadius * kotlin.math.cos(angle)).toFloat()
            val outerY = center.y + (outerRadius * kotlin.math.sin(angle)).toFloat()
            
            drawLine(
                color = Color.White,
                start = Offset(innerX, innerY),
                end = Offset(outerX, outerY),
                strokeWidth = (radius * 0.1f)
            )
        }
    }
}
