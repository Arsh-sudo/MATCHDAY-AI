const fs = require('fs');
const content = fs.readFileSync('app/src/main/java/com/example/ui/screens/MapScreen.kt', 'utf8');
let depth = 0;
const lines = content.split('\n');
for (let i = 0; i < lines.length; i++) {
    const line = lines[i];
    for (let char of line) {
        if (char === '{') depth++;
        if (char === '}') depth--;
    }
    if (line.includes('item {')) {
        console.log((i+1) + ': ' + line.trim() + ' => depth ' + depth);
    }
}
