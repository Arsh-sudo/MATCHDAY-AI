const fs = require('fs');
let code = fs.readFileSync('app/src/main/java/com/example/ui/screens/DashboardScreen.kt', 'utf8');

code = code.replace(/Icons\.Default\.GraphicEq/g, 'Icons.Default.Adjust');

fs.writeFileSync('app/src/main/java/com/example/ui/screens/DashboardScreen.kt', code);
