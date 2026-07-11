const fs = require('fs');
let code = fs.readFileSync('app/src/main/java/com/example/ui/MainApp.kt', 'utf8');

code = code.replace('import androidx.compose.animation.core.*', 
`import androidx.compose.animation.core.*
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.core.tween
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import kotlinx.coroutines.launch`);

code = code.replace(
`    if (!isLoggedIn) {
        LoginScreen(viewModel = viewModel)
    } else {
        var currentScreen by remember { mutableStateOf(Screen.Home) }`,
`    if (!isLoggedIn) {
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
`);

code = code.replace(
`                            BottomNavItem(
                                icon = if(currentScreen == Screen.Home) Icons.Filled.Home else Icons.Outlined.Home,
                                label = "Home",
                                isSelected = currentScreen == Screen.Home,
                                onClick = { currentScreen = Screen.Home }
                            )`,
`                            BottomNavItem(
                                icon = if(currentScreen == Screen.Home) Icons.Filled.Home else Icons.Outlined.Home,
                                label = "Home",
                                isSelected = currentScreen == Screen.Home,
                                onClick = { 
                                    showLiveFeeds = false
                                    coroutineScope.launch { pagerState.animateScrollToPage(0) }
                                }
                            )`);

code = code.replace(
`                            BottomNavItem(
                                icon = if(currentScreen == Screen.Stadium) Icons.Filled.Map else Icons.Outlined.Map,
                                label = "Stadium",
                                isSelected = currentScreen == Screen.Stadium,
                                onClick = { currentScreen = Screen.Stadium }
                            )`,
`                            BottomNavItem(
                                icon = if(currentScreen == Screen.Stadium) Icons.Filled.Map else Icons.Outlined.Map,
                                label = "Stadium",
                                isSelected = currentScreen == Screen.Stadium,
                                onClick = { 
                                    showLiveFeeds = false
                                    coroutineScope.launch { pagerState.animateScrollToPage(1) }
                                }
                            )`);

code = code.replace(
`                            BottomNavItem(
                                icon = if(currentScreen == Screen.AICopilot) Icons.Filled.ChatBubble else Icons.Outlined.ChatBubble,
                                label = "AI Copilot",
                                isSelected = currentScreen == Screen.AICopilot,
                                onClick = { currentScreen = Screen.AICopilot }
                            )`,
`                            BottomNavItem(
                                icon = if(currentScreen == Screen.AICopilot) Icons.Filled.ChatBubble else Icons.Outlined.ChatBubble,
                                label = "AI Copilot",
                                isSelected = currentScreen == Screen.AICopilot,
                                onClick = { 
                                    showLiveFeeds = false
                                    coroutineScope.launch { pagerState.animateScrollToPage(3) }
                                }
                            )`);

code = code.replace(
`                            BottomNavItem(
                                icon = if(currentScreen == Screen.Alerts) Icons.Filled.Notifications else Icons.Outlined.Notifications,
                                label = "Alerts",
                                isSelected = currentScreen == Screen.Alerts,
                                onClick = { currentScreen = Screen.Alerts }
                            )`,
`                            BottomNavItem(
                                icon = if(currentScreen == Screen.Alerts) Icons.Filled.Notifications else Icons.Outlined.Notifications,
                                label = "Alerts",
                                isSelected = currentScreen == Screen.Alerts,
                                onClick = { 
                                    showLiveFeeds = false
                                    coroutineScope.launch { pagerState.animateScrollToPage(4) }
                                }
                            )`);

code = code.replace(
`                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = { currentScreen = Screen.MatchOverview }
                                )`,
`                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = { 
                                        showLiveFeeds = false
                                        coroutineScope.launch { pagerState.animateScrollToPage(2) }
                                    }
                                )`);

code = code.replace(
`            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (currentScreen) {
                    Screen.Home -> DashboardScreen(
                        viewModel = viewModel,
                        onNavigateToFeeds = { currentScreen = Screen.LiveFeeds },
                        onNavigateToAICopilot = { currentScreen = Screen.AICopilot }
                    )
                    Screen.Stadium -> MapScreen(viewModel, onBack = { currentScreen = Screen.Home }, onNavigateToFeeds = { currentScreen = Screen.LiveFeeds })
                    Screen.AICopilot -> LiveDemoScreen(viewModel, onBack = { currentScreen = Screen.Home })
                    Screen.Alerts -> AlertsScreen(viewModel, onBack = { currentScreen = Screen.Home }, onNavigateToFeeds = { currentScreen = Screen.LiveFeeds })
                    Screen.MatchOverview -> MatchOverviewScreen(viewModel, onBack = { currentScreen = Screen.Home })
                    Screen.LiveFeeds -> LiveFeedsScreen(viewModel, onBack = { currentScreen = Screen.Home })
                }
            }`,
`            Box(
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
            }`);

fs.writeFileSync('app/src/main/java/com/example/ui/MainApp.kt', code);
