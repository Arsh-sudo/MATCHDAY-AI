const fs = require('fs');
let code = fs.readFileSync('app/src/main/java/com/example/ui/screens/DashboardScreen.kt', 'utf8');

code = code.replace(
`fun TelemetryStatItem(
    title: String,
    value: String,
    subValue: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(108.dp)
            .liquidGlass(
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = title,
                color = TextSecondary,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
                maxLines = 1
            )
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(16.dp))
                Text(
                    text = value,
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 1
                )
            }
            
            Text(
                text = subValue,
                color = if (value != "Offline") iconColor else TextSecondary,
                fontSize = 8.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
        }
    }
}`,
`fun TelemetryStatItem(
    title: String,
    value: String,
    unit: String = "",
    subValue: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector?,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(108.dp)
            .liquidGlass(
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (icon != null) {
                    Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                }
                Text(
                    text = title,
                    color = TextPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
            }
            
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = value,
                    color = TextPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                if (unit.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = unit,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 3.dp)
                    )
                }
            }
            
            Text(
                text = subValue,
                color = if (value != "Offline") iconColor else TextSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )
        }
    }
}`);

// Fan Cheering
code = code.replace(
`                    TelemetryStatItem(
                        title = "FAN CHEERING",
                        value = if (hasAccelerometer) "$cheeringIndex%" else "Offline",
                        subValue = if (!hasAccelerometer) "No Accelerometer" else if (cheeringIndex > 15) "SHAKING! 🎉" else "STABLE 💺",
                        icon = Icons.Default.Sensors,
                        iconColor = ColorAiPurple,
                        modifier = Modifier.weight(1f)
                    )`,
`                    TelemetryStatItem(
                        title = "Fan Cheering",
                        value = if (hasAccelerometer) "$cheeringIndex" else "Offline",
                        unit = if (hasAccelerometer) "dB" else "",
                        subValue = if (!hasAccelerometer) "No Accelerometer" else if (cheeringIndex > 15) "High 📈" else "Stable 📉",
                        icon = Icons.Default.GraphicEq,
                        iconColor = ColorAiBlue,
                        modifier = Modifier.weight(1f)
                    )`);

// Illum
code = code.replace(
`                    TelemetryStatItem(
                        title = "ILLUM.",
                        value = if (hasLightSensor) "\${liveLightLux.toInt()} lx" else "Offline",
                        subValue = if (!hasLightSensor) "No Light Sensor" else if (liveLightLux > 120) "BRIGHT 💡" else "DIM LIGHTS 🌑",
                        icon = Icons.Default.WbSunny,
                        iconColor = Color(0xFFFBBF24),
                        modifier = Modifier.weight(1f)
                    )`,
`                    TelemetryStatItem(
                        title = "Illumination",
                        value = if (hasLightSensor) "\${liveLightLux.toInt()}" else "Offline",
                        unit = if (hasLightSensor) "lx" else "",
                        subValue = if (!hasLightSensor) "No Light Sensor" else if (liveLightLux > 120) "Optimal 📈" else "Low 📉",
                        icon = Icons.Default.WbSunny,
                        iconColor = Color(0xFFFBBF24),
                        modifier = Modifier.weight(1f)
                    )`);

// Azteca Alt.
code = code.replace(
`                    TelemetryStatItem(
                        title = "AZTECA ALT.",
                        value = if (hasPressureSensor) "\${livePressureHpa.toInt()} hPa" else "Offline",
                        subValue = if (!hasPressureSensor) "No Barometer" else if (livePressureHpa < 850) "HIGH CDMX ALT ⛰️" else "STANDARD PRESS 🌊",
                        icon = Icons.Default.Speed,
                        iconColor = ColorAiBlue,
                        modifier = Modifier.weight(1f)
                    )`,
`                    TelemetryStatItem(
                        title = "Azteca Alt.",
                        value = if (hasPressureSensor) "\${livePressureHpa.toInt()}" else "Offline",
                        unit = if (hasPressureSensor) "hPa" else "",
                        subValue = if (!hasPressureSensor) "No Barometer" else if (livePressureHpa < 850) "High 📈" else "Stable 📉",
                        icon = Icons.Default.Speed,
                        iconColor = ColorAiPurple,
                        modifier = Modifier.weight(1f)
                    )`);

// Renewable Energy
code = code.replace(
`                TelemetryStatItem(
                    title = "RENEWABLE ENERGY",
                    value = "\${state.renewableEnergyPct}%",
                    subValue = "SOLAR ACTIVE",
                    icon = Icons.Default.EnergySavingsLeaf,
                    iconColor = ColorSafe,
                    modifier = Modifier.weight(1f)
                )`,
`                TelemetryStatItem(
                    title = "Renewable Energy",
                    value = "\${state.renewableEnergyPct}",
                    unit = "%",
                    subValue = "Solar Active",
                    icon = null,
                    iconColor = ColorSafe,
                    modifier = Modifier.weight(1f)
                )`);

// Waste Diversion
code = code.replace(
`                TelemetryStatItem(
                    title = "WASTE DIVERSION",
                    value = "\${state.wasteDiversionPct}%",
                    subValue = "COMPOST & RECYCLE",
                    icon = Icons.Default.Recycling,
                    iconColor = ColorAiBlue,
                    modifier = Modifier.weight(1f)
                )`,
`                TelemetryStatItem(
                    title = "Waste Diversion",
                    value = "♻️ \${state.wasteDiversionPct}",
                    unit = "%",
                    subValue = "Compost & Recycle",
                    icon = null,
                    iconColor = ColorAiBlue,
                    modifier = Modifier.weight(1f)
                )`);

// Carbon Offset
code = code.replace(
`                TelemetryStatItem(
                    title = "CARBON OFFSET",
                    value = String.format("%.1ft", state.carbonOffsetTonnes),
                    subValue = "TRANSIT SAVINGS",
                    icon = Icons.Default.Co2,
                    iconColor = ColorAiPurple,
                    modifier = Modifier.weight(1f)
                )`,
`                TelemetryStatItem(
                    title = "Carbon Offset",
                    value = String.format("%.1f", state.carbonOffsetTonnes),
                    unit = "t",
                    subValue = "CO₂ Saved",
                    icon = null,
                    iconColor = ColorAiPurple,
                    modifier = Modifier.weight(1f)
                )`);

fs.writeFileSync('app/src/main/java/com/example/ui/screens/DashboardScreen.kt', code);
