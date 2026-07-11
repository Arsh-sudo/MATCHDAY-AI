const fs = require('fs');
let code = fs.readFileSync('app/src/main/java/com/example/ui/screens/DashboardScreen.kt', 'utf8');

const goalPostsStr = `        // Goal posts
        val goalPostWidth = pitchWidth * 0.1f
        val goalPostHeight = pitchHeight * 0.015f
        val goalPostX = padding + (pitchWidth - goalPostWidth) / 2
        
        // Top Goal post
        drawRect(
            color = lineColor,
            topLeft = Offset(goalPostX, padding - goalPostHeight),
            size = Size(goalPostWidth, goalPostHeight),
            style = Stroke(width = strokeWidth)
        )
        
        // Bottom Goal post
        drawRect(
            color = lineColor,
            topLeft = Offset(goalPostX, size.height - padding),
            size = Size(goalPostWidth, goalPostHeight),
            style = Stroke(width = strokeWidth)
        )`;

code = code.replace(
    '        val topPenaltySpotY = padding + penaltyAreaHeight * 0.75f',
    goalPostsStr + '\n        val topPenaltySpotY = padding + penaltyAreaHeight * 0.75f'
);

fs.writeFileSync('app/src/main/java/com/example/ui/screens/DashboardScreen.kt', code);
