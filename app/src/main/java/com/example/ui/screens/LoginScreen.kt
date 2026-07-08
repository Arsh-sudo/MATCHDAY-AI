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
                text = "MATCHDAY AI",
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

@Composable
fun FootballPitchBackground() {
    val context = LocalContext.current
    // Use a FloatArray to capture accelerometer values directly without triggering recompositions on every sensor change
    val sensorValues = remember { FloatArray(2) }

    DisposableEffect(context) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event != null) {
                    // On portrait screen, x-tilt moves ball horizontally, y-tilt moves ball vertically
                    // Invert x sensor so tilting right slides right (negative reading, so negated makes it positive)
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

        var ballX by remember { mutableStateOf(0f) }
        var ballY by remember { mutableStateOf(0f) }
        var vx by remember { mutableStateOf(0f) }
        var vy by remember { mutableStateOf(0f) }
        val ballRadius = 14.dp
        val ballRadiusPx = with(density) { ballRadius.toPx() }

        var initialized by remember { mutableStateOf(false) }
        if (!initialized && widthPx > 0f && heightPx > 0f) {
            ballX = widthPx / 2f
            ballY = heightPx / 2f
            initialized = true
        }

        // Physics Update loop using withFrameNanos
        var lastTimeNanos by remember { mutableStateOf(0L) }
        LaunchedEffect(initialized) {
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

                    // Retrieve current sensor values dynamically inside the physics loop
                    val ax = sensorValues[0]
                    val ay = sensorValues[1]
                    val targetAx = if (abs(ax) < 0.05f && abs(ay) < 0.05f) 0f else ax
                    val targetAy = if (abs(ax) < 0.05f && abs(ay) < 0.05f) 0f else ay

                    // Accelerate ball with tilting
                    vx = (vx + targetAx * dt * 3500f) * friction
                    vy = (vy + targetAy * dt * 3500f) * friction

                    ballX += vx * dt
                    ballY += vy * dt

                    // Boundary collision with elastic bounce
                    if (widthPx > 0f && heightPx > 0f) {
                        if (ballX < ballRadiusPx) {
                            ballX = ballRadiusPx
                            vx = -vx * bounceElasticity
                        } else if (ballX > widthPx - ballRadiusPx) {
                            ballX = widthPx - ballRadiusPx
                            vx = -vx * bounceElasticity
                        }

                        if (ballY < ballRadiusPx) {
                            ballY = ballRadiusPx
                            vy = -vy * bounceElasticity
                        } else if (ballY > heightPx - ballRadiusPx) {
                            ballY = heightPx - ballRadiusPx
                            vy = -vy * bounceElasticity
                        }
                    }
                }
            }
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { tapOffset ->
                        val dx = ballX - tapOffset.x
                        val dy = ballY - tapOffset.y
                        val distance = sqrt(dx * dx + dy * dy)
                        if (distance > 0f) {
                            // Apply a gentle kick force inversely proportional to tap distance
                            val force = (1200f / (distance / 40f + 1f)).coerceIn(100f, 1600f)
                            vx += (dx / distance) * force
                            vy += (dy / distance) * force
                        }
                    }
                }
        ) {
            val w = size.width
            val h = size.height

            // 1. Alternating Grass stripes
            val stripesCount = 14
            val stripeHeight = h / stripesCount
            for (i in 0 until stripesCount) {
                val color = if (i % 2 == 0) Color(0xFF133F17) else Color(0xFF103613)
                drawRect(
                    color = color,
                    topLeft = Offset(0f, i * stripeHeight),
                    size = androidx.compose.ui.geometry.Size(w, stripeHeight + 1f)
                )
            }

            // White paint lines setup
            val linePaintColor = Color.White.copy(alpha = 0.45f)
            val lineStroke = Stroke(width = 2.dp.toPx())

            // 2. Boundary Lines
            val pad = 18.dp.toPx()
            val pw = w - 2 * pad
            val ph = h - 2 * pad
            drawRect(
                color = linePaintColor,
                topLeft = Offset(pad, pad),
                size = androidx.compose.ui.geometry.Size(pw, ph),
                style = lineStroke
            )

            // 3. Midfield line
            val midY = h / 2f
            drawLine(
                color = linePaintColor,
                start = Offset(pad, midY),
                end = Offset(w - pad, midY),
                strokeWidth = 2.dp.toPx()
            )

            // 4. Center circle and center spot
            val centerCircleRadius = 45.dp.toPx()
            drawCircle(
                color = linePaintColor,
                radius = centerCircleRadius,
                center = Offset(w / 2f, midY),
                style = lineStroke
            )
            drawCircle(
                color = linePaintColor,
                radius = 3.5f.dp.toPx(),
                center = Offset(w / 2f, midY)
            )

            // 5. Penalty Boxes at Top and Bottom
            val penBoxWidth = pw * 0.60f
            val penBoxHeight = ph * 0.12f
            val penBoxLeft = pad + (pw - penBoxWidth) / 2f
            drawRect(
                color = linePaintColor,
                topLeft = Offset(penBoxLeft, pad),
                size = androidx.compose.ui.geometry.Size(penBoxWidth, penBoxHeight),
                style = lineStroke
            )
            drawRect(
                color = linePaintColor,
                topLeft = Offset(penBoxLeft, h - pad - penBoxHeight),
                size = androidx.compose.ui.geometry.Size(penBoxWidth, penBoxHeight),
                style = lineStroke
            )

            // 6. Goal Areas at Top and Bottom
            val goalAreaWidth = pw * 0.28f
            val goalAreaHeight = ph * 0.04f
            val goalAreaLeft = pad + (pw - goalAreaWidth) / 2f
            drawRect(
                color = linePaintColor,
                topLeft = Offset(goalAreaLeft, pad),
                size = androidx.compose.ui.geometry.Size(goalAreaWidth, goalAreaHeight),
                style = lineStroke
            )
            drawRect(
                color = linePaintColor,
                topLeft = Offset(goalAreaLeft, h - pad - goalAreaHeight),
                size = androidx.compose.ui.geometry.Size(goalAreaWidth, goalAreaHeight),
                style = lineStroke
            )

            // 7. Penalty Spots & Arcs
            val penaltySpotDist = ph * 0.085f
            drawCircle(
                color = linePaintColor,
                radius = 3.dp.toPx(),
                center = Offset(w / 2f, pad + penaltySpotDist)
            )
            drawCircle(
                color = linePaintColor,
                radius = 3.dp.toPx(),
                center = Offset(w / 2f, h - pad - penaltySpotDist)
            )

            // 8. Corner Arcs
            val cornerRadius = 10.dp.toPx()
            // Top-Left
            drawArc(
                color = linePaintColor,
                startAngle = 0f,
                sweepAngle = 90f,
                useCenter = false,
                topLeft = Offset(pad - cornerRadius, pad - cornerRadius),
                size = androidx.compose.ui.geometry.Size(cornerRadius * 2, cornerRadius * 2),
                style = lineStroke
            )
            // Top-Right
            drawArc(
                color = linePaintColor,
                startAngle = 90f,
                sweepAngle = 90f,
                useCenter = false,
                topLeft = Offset(w - pad - cornerRadius, pad - cornerRadius),
                size = androidx.compose.ui.geometry.Size(cornerRadius * 2, cornerRadius * 2),
                style = lineStroke
            )
            // Bottom-Left
            drawArc(
                color = linePaintColor,
                startAngle = 270f,
                sweepAngle = 90f,
                useCenter = false,
                topLeft = Offset(pad - cornerRadius, h - pad - cornerRadius),
                size = androidx.compose.ui.geometry.Size(cornerRadius * 2, cornerRadius * 2),
                style = lineStroke
            )
            // Bottom-Right
            drawArc(
                color = linePaintColor,
                startAngle = 180f,
                sweepAngle = 90f,
                useCenter = false,
                topLeft = Offset(w - pad - cornerRadius, h - pad - cornerRadius),
                size = androidx.compose.ui.geometry.Size(cornerRadius * 2, cornerRadius * 2),
                style = lineStroke
            )

            // 9. 2D Goal Posts centered at the top and bottom edges (projecting slightly outside boundary)
            val goalWidth = pw * 0.22f
            val goalDepth = 12.dp.toPx()
            val goalLeft = pad + (pw - goalWidth) / 2f

            // Top Goal Post Structure and Net
            val topGoalPath = Path().apply {
                moveTo(goalLeft, pad)
                lineTo(goalLeft, pad - goalDepth)
                lineTo(goalLeft + goalWidth, pad - goalDepth)
                lineTo(goalLeft + goalWidth, pad)
            }
            drawPath(
                path = topGoalPath,
                color = Color.White.copy(alpha = 0.8f),
                style = Stroke(width = 2.5f.dp.toPx())
            )
            // Goal nets
            for (gx in 1..4) {
                val stepX = goalWidth / 5f
                drawLine(
                    color = Color.White.copy(alpha = 0.25f),
                    start = Offset(goalLeft + gx * stepX, pad),
                    end = Offset(goalLeft + gx * stepX, pad - goalDepth),
                    strokeWidth = 1.dp.toPx()
                )
            }

            // Bottom Goal Post Structure and Net
            val bottomGoalPath = Path().apply {
                moveTo(goalLeft, h - pad)
                lineTo(goalLeft, h - pad + goalDepth)
                lineTo(goalLeft + goalWidth, h - pad + goalDepth)
                lineTo(goalLeft + goalWidth, h - pad)
            }
            drawPath(
                path = bottomGoalPath,
                color = Color.White.copy(alpha = 0.8f),
                style = Stroke(width = 2.5f.dp.toPx())
            )
            // Goal nets
            for (gx in 1..4) {
                val stepX = goalWidth / 5f
                drawLine(
                    color = Color.White.copy(alpha = 0.25f),
                    start = Offset(goalLeft + gx * stepX, h - pad),
                    end = Offset(goalLeft + gx * stepX, h - pad + goalDepth),
                    strokeWidth = 1.dp.toPx()
                )
            }

            // 10. Corner flags with small flagpoles and red flags
            val flagPoleHeight = 9.dp.toPx()
            val flagSize = 5.dp.toPx()
            val cornerCoords = listOf(
                Offset(pad, pad),
                Offset(w - pad, pad),
                Offset(pad, h - pad),
                Offset(w - pad, h - pad)
            )
            cornerCoords.forEach { coord ->
                // Flagpole
                drawLine(
                    color = Color(0xFFE29578),
                    start = coord,
                    end = Offset(coord.x, coord.y - flagPoleHeight),
                    strokeWidth = 1.5.dp.toPx()
                )
                // Tiny Red flag triangle
                val flagPath = Path().apply {
                    moveTo(coord.x, coord.y - flagPoleHeight)
                    lineTo(coord.x + flagSize, coord.y - flagPoleHeight + flagSize / 2f)
                    lineTo(coord.x, coord.y - flagPoleHeight + flagSize)
                    close()
                }
                drawPath(
                    path = flagPath,
                    color = Color(0xFFEF4444)
                )
            }

            // 11. Render the 2D Football at Offset(ballX, ballY)
            if (initialized) {
                // Ball Shadow
                drawCircle(
                    color = Color.Black.copy(alpha = 0.35f),
                    radius = ballRadiusPx + 1.5f.dp.toPx(),
                    center = Offset(ballX, ballY + 2f.dp.toPx())
                )

                // White Base
                drawCircle(
                    color = Color.White,
                    radius = ballRadiusPx,
                    center = Offset(ballX, ballY)
                )

                // Central Hexagon
                drawCircle(
                    color = Color(0xFF1E293B),
                    radius = ballRadiusPx * 0.32f,
                    center = Offset(ballX, ballY)
                )

                // Radial lines and surrounding panels
                val outerPanelRadius = ballRadiusPx * 0.72f
                for (angleDeg in listOf(30, 102, 174, 246, 318)) {
                    val angleRad = Math.toRadians(angleDeg.toDouble())
                    val outerPoint = Offset(
                        (ballX + cos(angleRad) * ballRadiusPx).toFloat(),
                        (ballY + sin(angleRad) * ballRadiusPx).toFloat()
                    )
                    val innerPoint = Offset(
                        (ballX + cos(angleRad) * ballRadiusPx * 0.32f).toFloat(),
                        (ballY + sin(angleRad) * ballRadiusPx * 0.32f).toFloat()
                    )
                    drawLine(
                        color = Color(0xFF1E293B),
                        start = innerPoint,
                        end = outerPoint,
                        strokeWidth = 1.5f.dp.toPx()
                    )
                    // Mini panel dots on outer edge
                    drawCircle(
                        color = Color(0xFF1E293B),
                        radius = ballRadiusPx * 0.14f,
                        center = Offset(
                            (ballX + cos(angleRad) * outerPanelRadius).toFloat(),
                            (ballY + sin(angleRad) * outerPanelRadius).toFloat()
                        )
                    )
                }

                // Outer boundary stroke for ball definition
                drawCircle(
                    color = Color(0xFF1E293B),
                    radius = ballRadiusPx,
                    center = Offset(ballX, ballY),
                    style = Stroke(width = 1.2f.dp.toPx())
                )
            }
        }
    }
}
