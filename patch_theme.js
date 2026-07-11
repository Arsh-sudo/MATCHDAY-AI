const fs = require('fs');
let code = fs.readFileSync('app/src/main/java/com/example/ui/theme/Color.kt', 'utf8');

// Replace AccentLight and AccentDark base
code = code.replace(
`val AccentLight = Color(0xFF22C55E) // Vibrant turf green`,
`val AccentLight = Color(0xFFFBBF24) // Yellow`);

code = code.replace(
`val AccentDark = Color(0xFF15803D) // Deep pitch green`,
`val AccentDark = Color(0xFFD97706) // Dark yellow`);

// Replace ColorAiBlue and ColorAiPurple
code = code.replace(
`val ColorAiBlue = Color(0xFF10B981) // Emerald green`,
`val ColorAiBlue = Color(0xFFFBBF24) // Yellow`);

code = code.replace(
`val ColorAiPurple = Color(0xFF84CC16) // Lime green`,
`val ColorAiPurple = Color(0xFFFCD34D) // Lighter yellow`);

// Replace ColorAiGradientStart and ColorAiGradientEnd
code = code.replace(
`val ColorAiGradientStart = Color(0xFF22C55E)`,
`val ColorAiGradientStart = Color(0xFFFBBF24)`);

code = code.replace(
`val ColorAiGradientEnd = Color(0xFF84CC16)`,
`val ColorAiGradientEnd = Color(0xFFD97706)`);

// Replace UserBubbleBg and AiBubbleBg
code = code.replace(
`val UserBubbleBg = Color(0x2222C55E)`,
`val UserBubbleBg = Color(0x22FBBF24)`);

code = code.replace(
`val AiBubbleBg = Color(0x2210B981) // Tinted dark emerald surface`,
`val AiBubbleBg = Color(0x22FBBF24) // Yellow surface`);

fs.writeFileSync('app/src/main/java/com/example/ui/theme/Color.kt', code);
