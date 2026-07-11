const fs = require('fs');
let code = fs.readFileSync('app/src/main/java/com/example/ui/screens/DashboardScreen.kt', 'utf8');

const pitchCode = `
@Composable
fun FootballPitchBackground() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val columns = 6
        val rows = 12
        val cellWidth = size.width / columns
        val cellHeight = size.height / rows

        // Checkerboard
        for (r in 0 until rows) {
            for (c in 0 until columns) {
                val isDark = (r + c) % 2 == 0
                drawRect(
                    color = if (isDark) Color(0xFF071D0E) else Color(0xFF0A2612),
                    topLeft = Offset(c * cellWidth, r * cellHeight),
                    size = Size(cellWidth, cellHeight)
                )
            }
        }

        val strokeWidth = 1.5.dp.toPx()
        val lineColor = Color.White.copy(alpha = 0.25f)

        val padding = 24.dp.toPx()
        val pitchWidth = size.width - padding * 2
        val pitchHeight = size.height - padding * 2

        // Outer boundary
        drawRect(
            color = lineColor,
            topLeft = Offset(padding, padding),
            size = Size(pitchWidth, pitchHeight),
            style = Stroke(width = strokeWidth)
        )

        val centerX = size.width / 2
        val centerY = size.height / 2

        // Center line
        drawLine(
            color = lineColor,
            start = Offset(padding, centerY),
            end = Offset(size.width - padding, centerY),
            strokeWidth = strokeWidth
        )

        // Center circle
        val centerCircleRadius = pitchWidth * 0.15f
        drawCircle(
            color = lineColor,
            radius = centerCircleRadius,
            center = Offset(centerX, centerY),
            style = Stroke(width = strokeWidth)
        )
        drawCircle(
            color = lineColor,
            radius = strokeWidth * 2,
            center = Offset(centerX, centerY)
        )

        val penaltyAreaWidth = pitchWidth * 0.5f
        val penaltyAreaHeight = pitchHeight * 0.12f
        val penaltyAreaX = padding + (pitchWidth - penaltyAreaWidth) / 2

        val goalAreaWidth = pitchWidth * 0.22f
        val goalAreaHeight = pitchHeight * 0.04f
        val goalAreaX = padding + (pitchWidth - goalAreaWidth) / 2

        val arcRadius = pitchWidth * 0.15f

        // Top Side
        drawRect(
            color = lineColor,
            topLeft = Offset(penaltyAreaX, padding),
            size = Size(penaltyAreaWidth, penaltyAreaHeight),
            style = Stroke(width = strokeWidth)
        )
        drawRect(
            color = lineColor,
            topLeft = Offset(goalAreaX, padding),
            size = Size(goalAreaWidth, goalAreaHeight),
            style = Stroke(width = strokeWidth)
        )
        
        val topPenaltySpotY = padding + penaltyAreaHeight * 0.75f
        drawCircle(
            color = lineColor,
            radius = strokeWidth * 2,
            center = Offset(centerX, topPenaltySpotY)
        )
        androidx.compose.ui.graphics.drawscope.clipRect(
            top = padding + penaltyAreaHeight
        ) {
            drawArc(
                color = lineColor,
                startAngle = 0f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(centerX - arcRadius, topPenaltySpotY - arcRadius),
                size = Size(arcRadius * 2, arcRadius * 2),
                style = Stroke(width = strokeWidth)
            )
        }

        // Bottom Side
        drawRect(
            color = lineColor,
            topLeft = Offset(penaltyAreaX, size.height - padding - penaltyAreaHeight),
            size = Size(penaltyAreaWidth, penaltyAreaHeight),
            style = Stroke(width = strokeWidth)
        )
        drawRect(
            color = lineColor,
            topLeft = Offset(goalAreaX, size.height - padding - goalAreaHeight),
            size = Size(goalAreaWidth, goalAreaHeight),
            style = Stroke(width = strokeWidth)
        )
        
        val bottomPenaltySpotY = size.height - padding - penaltyAreaHeight * 0.75f
        drawCircle(
            color = lineColor,
            radius = strokeWidth * 2,
            center = Offset(centerX, bottomPenaltySpotY)
        )
        androidx.compose.ui.graphics.drawscope.clipRect(
            bottom = size.height - padding - penaltyAreaHeight
        ) {
            drawArc(
                color = lineColor,
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(centerX - arcRadius, bottomPenaltySpotY - arcRadius),
                size = Size(arcRadius * 2, arcRadius * 2),
                style = Stroke(width = strokeWidth)
            )
        }
        
        // Corner arcs
        val cornerRadius = pitchWidth * 0.04f
        drawArc(
            color = lineColor,
            startAngle = 0f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = Offset(padding - cornerRadius, padding - cornerRadius),
            size = Size(cornerRadius * 2, cornerRadius * 2),
            style = Stroke(width = strokeWidth)
        )
        drawArc(
            color = lineColor,
            startAngle = 90f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = Offset(size.width - padding - cornerRadius, padding - cornerRadius),
            size = Size(cornerRadius * 2, cornerRadius * 2),
            style = Stroke(width = strokeWidth)
        )
        drawArc(
            color = lineColor,
            startAngle = 180f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = Offset(size.width - padding - cornerRadius, size.height - padding - cornerRadius),
            size = Size(cornerRadius * 2, cornerRadius * 2),
            style = Stroke(width = strokeWidth)
        )
        drawArc(
            color = lineColor,
            startAngle = 270f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = Offset(padding - cornerRadius, size.height - padding - cornerRadius),
            size = Size(cornerRadius * 2, cornerRadius * 2),
            style = Stroke(width = strokeWidth)
        )
    }
}
`;

code = code + '\n' + pitchCode;

code = code.replace(
`    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 0.dp, bottom = 120.dp)
    ) {`,
`    Box(modifier = Modifier.fillMaxSize()) {
        FootballPitchBackground()
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 0.dp, bottom = 120.dp)
        ) {`);

// Close the Box at the end of the DashboardScreen function.
// We need to find the end of the DashboardScreen function.
// Wait, replacing the end of DashboardScreen is tricky with simple replace.
// Let's use string manipulation based on known end of DashboardScreen.
const matchStr = '        }\n    }\n}\n\n@Composable';
code = code.replace(matchStr, '        }\n    }\n    }\n}\n\n@Composable');

fs.writeFileSync('app/src/main/java/com/example/ui/screens/DashboardScreen.kt', code);
