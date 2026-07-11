const fs = require('fs');
let code = fs.readFileSync('app/src/main/java/com/example/ui/screens/DashboardScreen.kt', 'utf8');

code = code.replace(
`    Box(modifier = Modifier.fillMaxSize()) {
        DashboardPitchBackground()
        
        LazyColumn(`,
`    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(`
);

fs.writeFileSync('app/src/main/java/com/example/ui/screens/DashboardScreen.kt', code);
