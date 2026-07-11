const fs = require('fs');
let code = fs.readFileSync('app/src/main/java/com/example/ui/screens/LoginScreen.kt', 'utf8');

code = code.replace(
`        drawCircle(
            color = com.example.ui.theme.AccentLight,
            radius = radius * 0.9f,
            center = center,
            style = Stroke(width = (radius * 0.1f))
        )`,
`        drawCircle(
            color = com.example.ui.theme.ColorSafe, // Green
            radius = radius * 0.9f,
            center = center,
            style = Stroke(width = (radius * 0.1f))
        )`);

code = code.replace(
`        drawPath(
            path = pentagonPath,
            color = com.example.ui.theme.AccentGold
        )`,
`        drawPath(
            path = pentagonPath,
            color = Color.White
        )`);

fs.writeFileSync('app/src/main/java/com/example/ui/screens/LoginScreen.kt', code);
