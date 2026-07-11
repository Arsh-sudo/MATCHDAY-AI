const fs = require('fs');
let code = fs.readFileSync('app/src/main/java/com/example/ui/screens/LoginScreen.kt', 'utf8');

code = code.replace(
`                    // Fan tab
                    val fanTabBg by animateColorAsState(
                        targetValue = if (activeTab == 0) Color(0xFF153D1C) else Color.Transparent,
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
                            fontSize = 15.6.sp
                        )
                    }

                    // Operations tab
                    val opsTabBg by animateColorAsState(
                        targetValue = if (activeTab == 1) Color(0xFF153D1C) else Color.Transparent,
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
                            .testTag("tab_ops"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "OPERATIONS",
                            color = opsTabTextColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.6.sp
                        )
                    }`,
`                    // Fan tab
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
                            .testTag("tab_ops"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "OPERATIONS",
                            color = opsTabTextColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.6.sp
                        )
                    }`);

fs.writeFileSync('app/src/main/java/com/example/ui/screens/LoginScreen.kt', code);
