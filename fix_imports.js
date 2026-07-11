const fs = require('fs');
let code = fs.readFileSync('app/src/main/java/com/example/ui/screens/DashboardScreen.kt', 'utf8');
code = code.replace(
    'import androidx.compose.material.icons.filled.WbSunny\\nimport androidx.compose.material.icons.filled.GraphicEq',
    'import androidx.compose.material.icons.filled.WbSunny\nimport androidx.compose.material.icons.filled.GraphicEq'
);
fs.writeFileSync('app/src/main/java/com/example/ui/screens/DashboardScreen.kt', code);
