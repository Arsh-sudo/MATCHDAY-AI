const fs = require('fs');
let code = fs.readFileSync('app/src/main/java/com/example/ui/screens/LoginScreen.kt', 'utf8');

// Logo color
code = code.replace(
`        drawCircle(
            color = Color.White,
            radius = radius * 0.9f,
            center = center,
            style = Stroke(width = (radius * 0.1f))
        )`,
`        drawCircle(
            color = com.example.ui.theme.AccentGold,
            radius = radius * 0.9f,
            center = center,
            style = Stroke(width = (radius * 0.1f))
        )`);

// Logo size (in the image it looks a bit larger, but size 44.dp is probably fine, let's keep size)
// Wait, the circle in Logo uses AccentGold.

// FIFA Text
code = code.replace(
`                text = "FIFA WORLD CUP 2026™",
                color = AccentLight,`,
`                text = "FIFA WORLD CUP 2026™",
                color = com.example.ui.theme.AccentGold,`);

// Tab Selection (Fan Portal active)
code = code.replace(
`                                .background(
                                    if (targetTab == 0) AccentLight.copy(alpha = 0.2f) else Color.Transparent,
                                    RoundedCornerShape(12.dp)
                                )
                                .border(
                                    1.dp,
                                    if (targetTab == 0) AccentLight else Color.Transparent,
                                    RoundedCornerShape(12.dp)
                                )`,
`                                .background(
                                    if (targetTab == 0) com.example.ui.theme.AccentGold.copy(alpha = 0.2f) else Color.Transparent,
                                    RoundedCornerShape(12.dp)
                                )
                                .border(
                                    1.dp,
                                    if (targetTab == 0) com.example.ui.theme.AccentGold else Color.Transparent,
                                    RoundedCornerShape(12.dp)
                                )`);

// Tab Selection (Operations active)
code = code.replace(
`                                .background(
                                    if (targetTab == 1) ColorAiPurple.copy(alpha = 0.2f) else Color.Transparent,
                                    RoundedCornerShape(12.dp)
                                )
                                .border(
                                    1.dp,
                                    if (targetTab == 1) ColorAiPurple else Color.Transparent,
                                    RoundedCornerShape(12.dp)
                                )`,
`                                .background(
                                    if (targetTab == 1) com.example.ui.theme.AccentGold.copy(alpha = 0.2f) else Color.Transparent,
                                    RoundedCornerShape(12.dp)
                                )
                                .border(
                                    1.dp,
                                    if (targetTab == 1) com.example.ui.theme.AccentGold else Color.Transparent,
                                    RoundedCornerShape(12.dp)
                                )`);

// Fan Input fields leading icons
code = code.replace(/leadingIcon = \{ Icon\(Icons\.Default\.Person, contentDescription = null, tint = AccentLight\) \}/g,
`leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = com.example.ui.theme.AccentGold) }`);

code = code.replace(/leadingIcon = \{ Icon\(Icons\.Default\.ConfirmationNumber, contentDescription = null, tint = AccentLight\) \}/g,
`leadingIcon = { Icon(Icons.Default.ConfirmationNumber, contentDescription = null, tint = com.example.ui.theme.AccentGold) }`);

code = code.replace(/leadingIcon = \{ Icon\(Icons\.Default\.EventSeat, contentDescription = null, tint = AccentLight\) \}/g,
`leadingIcon = { Icon(Icons.Default.EventSeat, contentDescription = null, tint = com.example.ui.theme.AccentGold) }`);

// Operations Input fields leading icons
code = code.replace(/leadingIcon = \{ Icon\(Icons\.Default\.Badge, contentDescription = null, tint = ColorAiPurple\) \}/g,
`leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null, tint = com.example.ui.theme.AccentGold) }`);

code = code.replace(/leadingIcon = \{ Icon\(Icons\.Default\.Lock, contentDescription = null, tint = ColorAiPurple\) \}/g,
`leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = com.example.ui.theme.AccentGold) }`);

// Fan button gradient
code = code.replace(
`                                        topBorderColor = AccentLight.copy(alpha = 0.5f),
                                        bottomBorderColor = AccentDark.copy(alpha = 0.2f),
                                        bgStartColor = AccentLight.copy(alpha = 0.4f),
                                        bgEndColor = AccentDark.copy(alpha = 0.7f)`,
`                                        topBorderColor = com.example.ui.theme.AccentGold.copy(alpha = 0.8f),
                                        bottomBorderColor = com.example.ui.theme.AccentGoldDark.copy(alpha = 0.4f),
                                        bgStartColor = com.example.ui.theme.AccentGold.copy(alpha = 0.7f),
                                        bgEndColor = com.example.ui.theme.AccentGoldDark.copy(alpha = 0.9f)`);

// Operations button gradient
code = code.replace(
`                                        topBorderColor = ColorAiPurple.copy(alpha = 0.5f),
                                        bottomBorderColor = ColorAiBlue.copy(alpha = 0.2f),
                                        bgStartColor = ColorAiPurple.copy(alpha = 0.4f),
                                        bgEndColor = ColorAiBlue.copy(alpha = 0.7f)`,
`                                        topBorderColor = com.example.ui.theme.AccentGold.copy(alpha = 0.8f),
                                        bottomBorderColor = com.example.ui.theme.AccentGoldDark.copy(alpha = 0.4f),
                                        bgStartColor = com.example.ui.theme.AccentGold.copy(alpha = 0.7f),
                                        bgEndColor = com.example.ui.theme.AccentGoldDark.copy(alpha = 0.9f)`);

// Fan security icon
code = code.replace(
`                            Icon(Icons.Default.Security, contentDescription = null, tint = AccentLight, modifier = Modifier.size(20.dp))`,
`                            Icon(Icons.Default.Security, contentDescription = null, tint = com.example.ui.theme.AccentGold, modifier = Modifier.size(20.dp))`);

// Operations security icon
code = code.replace(
`                            Icon(Icons.Default.Security, contentDescription = null, tint = ColorAiPurple, modifier = Modifier.size(20.dp))`,
`                            Icon(Icons.Default.Security, contentDescription = null, tint = com.example.ui.theme.AccentGold, modifier = Modifier.size(20.dp))`);

// OutlinedTextField borders
// Right now we have:
// modifier = Modifier
// .fillMaxWidth()
// .liquidGlass(shape = RoundedCornerShape(16.dp))
// 
// Looking at the image, text fields have a thin white-ish green border. The liquidGlass modifier by default applies a white border `border(1.dp, GlassBorder, shape)`.
// We can change liquidGlass usage for input fields or just set colors in OutlinedTextField.
// Currently OutlinedTextField border colors are Color.Transparent. I think it looks fine with liquidGlass border.

fs.writeFileSync('app/src/main/java/com/example/ui/screens/LoginScreen.kt', code);
