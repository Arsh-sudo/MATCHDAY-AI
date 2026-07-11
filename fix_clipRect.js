const fs = require('fs');
let code = fs.readFileSync('app/src/main/java/com/example/ui/screens/DashboardScreen.kt', 'utf8');

code = code.replace(/androidx\.compose\.ui\.graphics\.drawscope\.clipRect/g, 'clipRect');

if (!code.includes('import androidx.compose.ui.graphics.drawscope.clipRect')) {
    code = code.replace('import androidx.compose.ui.graphics.drawscope.Stroke', 'import androidx.compose.ui.graphics.drawscope.Stroke\nimport androidx.compose.ui.graphics.drawscope.clipRect');
}

fs.writeFileSync('app/src/main/java/com/example/ui/screens/DashboardScreen.kt', code);
