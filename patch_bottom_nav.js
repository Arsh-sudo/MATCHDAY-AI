const fs = require('fs');
let code = fs.readFileSync('app/src/main/java/com/example/ui/MainApp.kt', 'utf8');

code = code.replace(
`@Composable
fun BottomNavItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(`,
`@Composable
fun BottomNavItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.clickable(`);

fs.writeFileSync('app/src/main/java/com/example/ui/MainApp.kt', code);
