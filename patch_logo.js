const fs = require('fs');
let code = fs.readFileSync('app/src/main/java/com/example/ui/screens/LoginScreen.kt', 'utf8');

code = code.replace(
`        drawCircle(
            color = com.example.ui.theme.AccentGold,
            radius = radius * 0.9f,
            center = center,
            style = Stroke(width = (radius * 0.1f))
        )`,
`        drawCircle(
            color = com.example.ui.theme.AccentLight,
            radius = radius * 0.9f,
            center = center,
            style = Stroke(width = (radius * 0.1f))
        )`);

code = code.replace(
`        drawPath(
            path = pentagonPath,
            color = Color.White
        )`,
`        drawPath(
            path = pentagonPath,
            color = com.example.ui.theme.AccentGold
        )`);

code = code.replace(
`            drawLine(
                color = Color.White,
                start = Offset(innerX, innerY),
                end = Offset(outerX, outerY),
                strokeWidth = (radius * 0.1f)
            )`,
`            drawLine(
                color = com.example.ui.theme.AccentGold,
                start = Offset(innerX, innerY),
                end = Offset(outerX, outerY),
                strokeWidth = (radius * 0.1f)
            )`);

fs.writeFileSync('app/src/main/java/com/example/ui/screens/LoginScreen.kt', code);
