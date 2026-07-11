const fs = require('fs');
const content = fs.readFileSync('app/src/main/java/com/example/ui/MainApp.kt', 'utf8');
console.log("File read successfully.");
