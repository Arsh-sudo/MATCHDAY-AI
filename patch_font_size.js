const fs = require('fs');
let code = fs.readFileSync('app/src/main/java/com/example/ui/screens/DashboardScreen.kt', 'utf8');

code = code.replace(
`            Text(
                text = "Show real-time readings from your device's built-in sensors instead of stadium simulations.",
                color = TextSecondary,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )`,
`            Text(
                text = "Show real-time readings from your device's built-in sensors instead of stadium simulations.",
                color = TextSecondary,
                fontSize = 16.sp,
                lineHeight = 24.sp
            )`);

fs.writeFileSync('app/src/main/java/com/example/ui/screens/DashboardScreen.kt', code);
