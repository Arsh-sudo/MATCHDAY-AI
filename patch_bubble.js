const fs = require('fs');
let code = fs.readFileSync('app/src/main/java/com/example/ui/MainApp.kt', 'utf8');

code = code.replace(
`import androidx.compose.ui.Modifier`,
`import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.IntOffset
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.platform.LocalDensity`);

code = code.replace(
`            bottomBar = {
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
                        Row(`,
`            bottomBar = {
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

                        Row(`);

code = code.replace(
`                            BottomNavItem(
                                icon = if(currentScreen == Screen.Home) Icons.Filled.Home else Icons.Outlined.Home,
                                label = "Home",
                                isSelected = currentScreen == Screen.Home,
                                onClick = { 
                                    showLiveFeeds = false
                                    coroutineScope.launch { pagerState.animateScrollToPage(0) }
                                }
                            )`,
`                            BottomNavItem(
                                icon = if(currentScreen == Screen.Home) Icons.Filled.Home else Icons.Outlined.Home,
                                label = "Home",
                                isSelected = currentScreen == Screen.Home,
                                onClick = { 
                                    showLiveFeeds = false
                                    coroutineScope.launch { pagerState.animateScrollToPage(0) }
                                },
                                modifier = Modifier.onGloballyPositioned { targetPositions[0] = it.positionInParent().x + it.size.width / 2f }
                            )`);

code = code.replace(
`                            BottomNavItem(
                                icon = if(currentScreen == Screen.Stadium) Icons.Filled.Map else Icons.Outlined.Map,
                                label = "Stadium",
                                isSelected = currentScreen == Screen.Stadium,
                                onClick = { 
                                    showLiveFeeds = false
                                    coroutineScope.launch { pagerState.animateScrollToPage(1) }
                                }
                            )`,
`                            BottomNavItem(
                                icon = if(currentScreen == Screen.Stadium) Icons.Filled.Map else Icons.Outlined.Map,
                                label = "Stadium",
                                isSelected = currentScreen == Screen.Stadium,
                                onClick = { 
                                    showLiveFeeds = false
                                    coroutineScope.launch { pagerState.animateScrollToPage(1) }
                                },
                                modifier = Modifier.onGloballyPositioned { targetPositions[1] = it.positionInParent().x + it.size.width / 2f }
                            )`);

code = code.replace(
`                            // Spacer for center button
                            Spacer(modifier = Modifier.width(56.dp))`,
`                            // Spacer for center button
                            Spacer(modifier = Modifier.width(56.dp).onGloballyPositioned { targetPositions[2] = it.positionInParent().x + it.size.width / 2f })`);

code = code.replace(
`                            BottomNavItem(
                                icon = if(currentScreen == Screen.AICopilot) Icons.Filled.ChatBubble else Icons.Outlined.ChatBubble,
                                label = "AI Copilot",
                                isSelected = currentScreen == Screen.AICopilot,
                                onClick = { 
                                    showLiveFeeds = false
                                    coroutineScope.launch { pagerState.animateScrollToPage(3) }
                                }
                            )`,
`                            BottomNavItem(
                                icon = if(currentScreen == Screen.AICopilot) Icons.Filled.ChatBubble else Icons.Outlined.ChatBubble,
                                label = "AI Copilot",
                                isSelected = currentScreen == Screen.AICopilot,
                                onClick = { 
                                    showLiveFeeds = false
                                    coroutineScope.launch { pagerState.animateScrollToPage(3) }
                                },
                                modifier = Modifier.onGloballyPositioned { targetPositions[3] = it.positionInParent().x + it.size.width / 2f }
                            )`);

code = code.replace(
`                            BottomNavItem(
                                icon = if(currentScreen == Screen.Alerts) Icons.Filled.Notifications else Icons.Outlined.Notifications,
                                label = "Alerts",
                                isSelected = currentScreen == Screen.Alerts,
                                onClick = { 
                                    showLiveFeeds = false
                                    coroutineScope.launch { pagerState.animateScrollToPage(4) }
                                }
                            )`,
`                            BottomNavItem(
                                icon = if(currentScreen == Screen.Alerts) Icons.Filled.Notifications else Icons.Outlined.Notifications,
                                label = "Alerts",
                                isSelected = currentScreen == Screen.Alerts,
                                onClick = { 
                                    showLiveFeeds = false
                                    coroutineScope.launch { pagerState.animateScrollToPage(4) }
                                },
                                modifier = Modifier.onGloballyPositioned { targetPositions[4] = it.positionInParent().x + it.size.width / 2f }
                            )`);

fs.writeFileSync('app/src/main/java/com/example/ui/MainApp.kt', code);
