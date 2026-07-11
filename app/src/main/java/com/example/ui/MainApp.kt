package com.example.ui

import androidx.compose.animation.core.*
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.core.tween
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import kotlinx.coroutines.launch
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.outlined.ChatBubble
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.IntOffset
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.*
import com.example.viewmodel.MainViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(viewModel: MainViewModel = viewModel()) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val loggedInUserType by viewModel.loggedInUserType.collectAsState()
    val fanName by viewModel.fanName.collectAsState()
    val staffId by viewModel.staffId.collectAsState()
    val currentRole by viewModel.currentRole.collectAsState()
    
    if (!isLoggedIn) {
        LoginScreen(viewModel = viewModel)
    } else {
        val pagerScreens = listOf(Screen.Home, Screen.Stadium, Screen.MatchOverview, Screen.AICopilot, Screen.Alerts)
        val pagerState = rememberPagerState(pageCount = { pagerScreens.size })
        val coroutineScope = rememberCoroutineScope()
        var showLiveFeeds by remember { mutableStateOf(false) }
        var initialFadeIn by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            initialFadeIn = true
        }
        
        val activeScreen = if (showLiveFeeds) Screen.LiveFeeds else pagerScreens[pagerState.currentPage]
        // Instead of currentScreen, we use activeScreen for top bar logic
        val currentScreen = activeScreen

        
        Box(modifier = Modifier.fillMaxSize().background(BgMain)) {
            // Subtle background grid
            Canvas(modifier = Modifier.fillMaxSize()) {
                val step = 40.dp.toPx()
                for (x in 0..size.width.toInt() step step.toInt()) {
                    drawLine(Color.White.copy(alpha = 0.02f), Offset(x.toFloat(), 0f), Offset(x.toFloat(), size.height))
                }
                for (y in 0..size.height.toInt() step step.toInt()) {
                    drawLine(Color.White.copy(alpha = 0.02f), Offset(0f, y.toFloat()), Offset(size.width, y.toFloat()))
                }
            }

            Scaffold(
                modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing),
                containerColor = Color.Transparent,
                topBar = {
                    if (currentScreen == Screen.Home) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box {
                                        var showLogoutMenu by remember { mutableStateOf(false) }
                                        IconButton(onClick = { showLogoutMenu = true }, modifier = Modifier.size(24.dp)) {
                                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = TextPrimary)
                                        }
                                        DropdownMenu(
                                            expanded = showLogoutMenu,
                                            onDismissRequest = { showLogoutMenu = false },
                                            modifier = Modifier.background(BgMain)
                                        ) {
                                            DropdownMenuItem(
                                                text = {
                                                    Column {
                                                        Text(
                                                            text = if (loggedInUserType == "fan") "FAN ACCESS" else "OPERATIONS CONTROL",
                                                            fontSize = 10.sp,
                                                            color = if (loggedInUserType == "fan") ColorAiBlue else ColorAiPurple,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                        Text(
                                                            text = if (loggedInUserType == "fan") fanName else "Staff ID: $staffId",
                                                            fontSize = 14.sp,
                                                            color = TextPrimary,
                                                            fontWeight = FontWeight.SemiBold
                                                        )
                                                        if (loggedInUserType == "staff") {
                                                            Text(
                                                                text = currentRole,
                                                                fontSize = 11.sp,
                                                                color = TextSecondary
                                                            )
                                                        }
                                                    }
                                                },
                                                onClick = {},
                                                enabled = false
                                            )
                                            HorizontalDivider(color = GlassBorder)
                                            DropdownMenuItem(
                                                text = { Text("Log Out / Switch Account", color = ColorCritical) },
                                                onClick = {
                                                    showLogoutMenu = false
                                                    viewModel.logout()
                                                }
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = if (loggedInUserType == "fan") "FAN CONCIERGE" else "MATCHDAY AI", 
                                                fontWeight = FontWeight.ExtraBold, 
                                                color = TextPrimary, 
                                                fontSize = 20.sp, 
                                                letterSpacing = 0.5.sp
                                            )
                                        }
                                    }
                                }
                                
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Column(horizontalAlignment = Alignment.End) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text("LIVE", color = ColorSafe, fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 1.sp)
                                            Spacer(modifier = Modifier.width(6.dp))
                                            GlowingIndicator()
                                        }
                                        Spacer(modifier = Modifier.height(2.dp))
                                        var currentTime by remember { mutableStateOf(
                                            java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault()).format(java.util.Date())
                                        ) }
                                        LaunchedEffect(Unit) {
                                            while (true) {
                                                kotlinx.coroutines.delay(60_000L)
                                                currentTime = java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault()).format(java.util.Date())
                                            }
                                        }
                                        Text(currentTime, color = TextSecondary, fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                },
            bottomBar = {
                val targetPositions = remember { mutableStateListOf(0f, 0f, 0f, 0f, 0f) }
                val density = LocalDensity.current
                
                // Floating Navigation
                Box(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp), 
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(84.dp)
                            .clip(RoundedCornerShape(42.dp))
                            .background(BgMain.copy(alpha = 0.95f))
                            .border(1.dp, GlassBorder, RoundedCornerShape(42.dp))
                    ) {
                        
                        val pageFloat = (pagerState.currentPage + pagerState.currentPageOffsetFraction).coerceIn(0f, 4f)
                        val index = pageFloat.toInt().coerceIn(0, 3)
                        val fraction = pageFloat - index
                        val currentX = targetPositions[index]
                        val nextX = targetPositions[index + 1]
                        val bubbleX = currentX + (nextX - currentX) * fraction
                        
                        if (targetPositions[0] != 0f) {
                            Box(
                                modifier = Modifier
                                    .offset { 
                                        IntOffset(
                                            x = bubbleX.toInt() - with(density) { 24.dp.roundToPx() },
                                            y = with(density) { (84.dp / 2 - 24.dp).roundToPx() }
                                        ) 
                                    }
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(ColorAiBlue.copy(alpha = 0.2f))
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            BottomNavItem(
                                icon = if(currentScreen == Screen.Home) Icons.Filled.Home else Icons.Outlined.Home,
                                label = "Home",
                                isSelected = currentScreen == Screen.Home,
                                onClick = { 
                                    showLiveFeeds = false
                                    coroutineScope.launch { pagerState.animateScrollToPage(0) }
                                },
                                modifier = Modifier.onGloballyPositioned { targetPositions[0] = it.positionInParent().x + it.size.width / 2f }
                            )
                            BottomNavItem(
                                icon = if(currentScreen == Screen.Stadium) Icons.Filled.Map else Icons.Outlined.Map,
                                label = "Stadium",
                                isSelected = currentScreen == Screen.Stadium,
                                onClick = { 
                                    showLiveFeeds = false
                                    coroutineScope.launch { pagerState.animateScrollToPage(1) }
                                },
                                modifier = Modifier.onGloballyPositioned { targetPositions[1] = it.positionInParent().x + it.size.width / 2f }
                            )
                            
                            // Spacer for center button
                            Spacer(modifier = Modifier.width(56.dp).onGloballyPositioned { targetPositions[2] = it.positionInParent().x + it.size.width / 2f })
                            
                            BottomNavItem(
                                icon = if(currentScreen == Screen.AICopilot) Icons.Filled.ChatBubble else Icons.Outlined.ChatBubble,
                                label = "AI Copilot",
                                isSelected = currentScreen == Screen.AICopilot,
                                onClick = { 
                                    showLiveFeeds = false
                                    coroutineScope.launch { pagerState.animateScrollToPage(3) }
                                },
                                modifier = Modifier.onGloballyPositioned { targetPositions[3] = it.positionInParent().x + it.size.width / 2f }
                            )
                            BottomNavItem(
                                icon = if(currentScreen == Screen.Alerts) Icons.Filled.Notifications else Icons.Outlined.Notifications,
                                label = "Alerts",
                                isSelected = currentScreen == Screen.Alerts,
                                onClick = { 
                                    showLiveFeeds = false
                                    coroutineScope.launch { pagerState.animateScrollToPage(4) }
                                },
                                modifier = Modifier.onGloballyPositioned { targetPositions[4] = it.positionInParent().x + it.size.width / 2f }
                            )
                        }
                    }
                    
                    // Floating Center Button
                    Box(
                        modifier = Modifier
                            .offset(y = (-32).dp)
                            .size(72.dp)
                            .background(BgMain, CircleShape)
                            .padding(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(Brush.linearGradient(listOf(AccentDark, AccentLight)))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = { 
                                        showLiveFeeds = false
                                        coroutineScope.launch { pagerState.animateScrollToPage(2) }
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Brush.radialGradient(listOf(ColorAiBlue.copy(alpha = 0.6f), Color.Transparent)))
                            )
                            Icon(Icons.Filled.SportsSoccer, contentDescription = "Match Overview", tint = Color.White, modifier = Modifier.size(36.dp))
                        }
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                AnimatedVisibility(
                    visible = initialFadeIn,
                    enter = fadeIn(animationSpec = tween(1000))
                ) {
                    if (showLiveFeeds) {
                        LiveFeedsScreen(viewModel, onBack = { showLiveFeeds = false })
                    } else {
                        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
                            when (pagerScreens[page]) {
                                Screen.Home -> DashboardScreen(
                                    viewModel = viewModel,
                                    onNavigateToFeeds = { showLiveFeeds = true },
                                    onNavigateToAICopilot = { 
                                        coroutineScope.launch { pagerState.animateScrollToPage(3) }
                                    }
                                )
                                Screen.Stadium -> MapScreen(
                                    viewModel = viewModel, 
                                    onBack = { coroutineScope.launch { pagerState.animateScrollToPage(0) } }, 
                                    onNavigateToFeeds = { showLiveFeeds = true }
                                )
                                Screen.MatchOverview -> MatchOverviewScreen(
                                    viewModel = viewModel, 
                                    onBack = { coroutineScope.launch { pagerState.animateScrollToPage(0) } }
                                )
                                Screen.AICopilot -> LiveDemoScreen(
                                    viewModel = viewModel, 
                                    onBack = { coroutineScope.launch { pagerState.animateScrollToPage(0) } }
                                )
                                Screen.Alerts -> AlertsScreen(
                                    viewModel = viewModel, 
                                    onBack = { coroutineScope.launch { pagerState.animateScrollToPage(0) } }, 
                                    onNavigateToFeeds = { showLiveFeeds = true }
                                )
                                else -> {}
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
fun BottomNavItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        ).padding(8.dp)
    ) {
        Icon(icon, contentDescription = label, tint = if(isSelected) ColorAiBlue else TextSecondary, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, fontSize = 10.sp, color = if(isSelected) ColorAiBlue else TextSecondary, fontWeight = if(isSelected) FontWeight.Bold else FontWeight.Normal)
    }
}

@Composable
fun GlowingIndicator() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "pulse_alpha"
    )

    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(16.dp)) {
        Box(modifier = Modifier.size(16.dp).clip(CircleShape).background(ColorSafeDark))
        Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(ColorSafe.copy(alpha = alpha * 0.4f)))
        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(ColorSafe))
    }
}
