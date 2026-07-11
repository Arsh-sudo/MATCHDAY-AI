const fs = require('fs');
let code = fs.readFileSync('app/src/main/java/com/example/ui/MainApp.kt', 'utf8');

if (!code.includes('import com.example.ui.screens.DashboardPitchBackground')) {
    code = code.replace(
        'import com.example.ui.screens.*',
        'import com.example.ui.screens.*\nimport com.example.ui.screens.DashboardPitchBackground'
    );
}

const gridCanvas = `            // Subtle background grid
            Canvas(modifier = Modifier.fillMaxSize()) {
                val step = 40.dp.toPx()
                for (x in 0..size.width.toInt() step step.toInt()) {
                    drawLine(Color.White.copy(alpha = 0.02f), Offset(x.toFloat(), 0f), Offset(x.toFloat(), size.height))
                }
                for (y in 0..size.height.toInt() step step.toInt()) {
                    drawLine(Color.White.copy(alpha = 0.02f), Offset(0f, y.toFloat()), Offset(size.width, y.toFloat()))
                }
            }`;

code = code.replace(gridCanvas, '            DashboardPitchBackground()');

fs.writeFileSync('app/src/main/java/com/example/ui/MainApp.kt', code);
