const fs = require('fs');
let code = fs.readFileSync('app/src/main/java/com/example/ui/theme/Color.kt', 'utf8');

code = code.replace(
`val BorderColor = Color(0x334ADE80)`,
`val BorderColor = Color(0x33FBBF24)`);

code = code.replace(
`val GlassBorder = Color(0x284ADE80)`,
`val GlassBorder = Color(0x28FBBF24)`);

code = code.replace(
`val TextSecondary = Color(0xFFA7F3D0)`,
`val TextSecondary = Color(0xFFFEF3C7)`);

fs.writeFileSync('app/src/main/java/com/example/ui/theme/Color.kt', code);
