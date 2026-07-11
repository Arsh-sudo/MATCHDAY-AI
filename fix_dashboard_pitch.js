const fs = require('fs');
let code = fs.readFileSync('app/src/main/java/com/example/ui/screens/DashboardScreen.kt', 'utf8');

code = code.replace(/FootballPitchBackground/g, 'DashboardPitchBackground');

if (!code.includes('import androidx.compose.ui.geometry.Offset')) {
    code = code.replace('import androidx.compose.ui.graphics.*', 'import androidx.compose.ui.graphics.*\nimport androidx.compose.ui.geometry.Offset\nimport androidx.compose.ui.geometry.Size');
}

fs.writeFileSync('app/src/main/java/com/example/ui/screens/DashboardScreen.kt', code);
