const fs = require('fs');
const content = fs.readFileSync('app/src/main/java/com/example/ui/screens/DashboardScreen.kt', 'utf8');
let depth = 0;
const lines = content.split('\n');
for (let i = 0; i < lines.length; i++) {
    const line = lines[i];
    for (let char of line) {
        if (char === '{') depth++;
        if (char === '}') depth--;
    }
    if (line.includes('item {') || line.includes('} else {') || line.includes('// 1.') || line.includes('// 2.') || line.includes('// 3.') || line.includes('// 4.')) {
        console.log((i+1) + ': ' + line.trim() + ' => depth ' + depth);
    }
}
