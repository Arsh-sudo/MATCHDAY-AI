const fs = require('fs');
let code = fs.readFileSync('app/src/main/java/com/example/ui/theme/Color.kt', 'utf8');

code = code.replace(
`val AccentDark = Color(0xFF15803D) // Deep pitch green`,
`val AccentDark = Color(0xFF15803D) // Deep pitch green
val AccentGold = Color(0xFFFBBF24) // Golden yellow accent
val AccentGoldDark = Color(0xFFD97706) // Darker gold for gradients`
);

fs.writeFileSync('app/src/main/java/com/example/ui/theme/Color.kt', code);
