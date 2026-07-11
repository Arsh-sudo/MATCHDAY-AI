const fs = require('fs');
let code = fs.readFileSync('app/src/main/java/com/example/ui/MainApp.kt', 'utf8');

code = code.replace(
`                val targetPositions = remember { mutableStateListOf(0f, 0f, 0f, 0f, 0f) }`,
`                val targetPositions = remember { mutableStateListOf(Offset.Zero, Offset.Zero, Offset.Zero, Offset.Zero, Offset.Zero) }`);

code = code.replace(
`                        val currentX = targetPositions[index]
                        val nextX = targetPositions[index + 1]
                        val bubbleX = currentX + (nextX - currentX) * fraction
                        
                        if (targetPositions[0] != 0f) {
                            Box(
                                modifier = Modifier
                                    .offset { 
                                        IntOffset(
                                            x = bubbleX.toInt() - with(density) { 24.dp.roundToPx() },
                                            y = with(density) { (84.dp / 2 - 24.dp).roundToPx() }
                                        ) 
                                    }
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(ColorAiBlue.copy(alpha = 0.2f))
                            )`,
`                        val currentX = targetPositions[index].x
                        val nextX = targetPositions[index + 1].x
                        val bubbleX = currentX + (nextX - currentX) * fraction
                        
                        val currentY = targetPositions[index].y
                        val nextY = targetPositions[index + 1].y
                        val bubbleY = currentY + (nextY - currentY) * fraction
                        
                        if (targetPositions[0] != Offset.Zero) {
                            val infiniteTransition = rememberInfiniteTransition(label = "wobble")
                            val scale by infiniteTransition.animateFloat(
                                initialValue = 0.95f,
                                targetValue = 1.05f,
                                animationSpec = infiniteRepeatable(
                                    animation = tween(800, easing = FastOutSlowInEasing),
                                    repeatMode = RepeatMode.Reverse
                                ), label = "wobble_scale"
                            )
                            val rotate by infiniteTransition.animateFloat(
                                initialValue = -5f,
                                targetValue = 5f,
                                animationSpec = infiniteRepeatable(
                                    animation = tween(1200, easing = LinearEasing),
                                    repeatMode = RepeatMode.Reverse
                                ), label = "wobble_rotate"
                            )

                            Box(
                                modifier = Modifier
                                    .offset { 
                                        IntOffset(
                                            x = bubbleX.toInt() - with(density) { 32.dp.roundToPx() },
                                            y = bubbleY.toInt() - with(density) { 32.dp.roundToPx() }
                                        ) 
                                    }
                                    .size(64.dp)
                                    .graphicsLayer {
                                        scaleX = scale
                                        scaleY = scale
                                        rotationZ = rotate
                                    }
                                    .clip(CircleShape)
                                    .background(ColorAiBlue.copy(alpha = 0.25f))
                            )`);

code = code.replace(
`                                modifier = Modifier.onGloballyPositioned { targetPositions[0] = it.positionInParent().x + it.size.width / 2f }`,
`                                modifier = Modifier.onGloballyPositioned { targetPositions[0] = Offset(it.positionInParent().x + it.size.width / 2f, it.positionInParent().y + it.size.height / 2f) }`);

code = code.replace(
`                                modifier = Modifier.onGloballyPositioned { targetPositions[1] = it.positionInParent().x + it.size.width / 2f }`,
`                                modifier = Modifier.onGloballyPositioned { targetPositions[1] = Offset(it.positionInParent().x + it.size.width / 2f, it.positionInParent().y + it.size.height / 2f) }`);

code = code.replace(
`                            Spacer(modifier = Modifier.width(56.dp).onGloballyPositioned { targetPositions[2] = it.positionInParent().x + it.size.width / 2f })`,
`                            Spacer(modifier = Modifier.width(56.dp).onGloballyPositioned { targetPositions[2] = Offset(it.positionInParent().x + it.size.width / 2f, it.positionInParent().y + it.size.height / 2f - with(density) { 32.dp.toPx() }) })`);

code = code.replace(
`                                modifier = Modifier.onGloballyPositioned { targetPositions[3] = it.positionInParent().x + it.size.width / 2f }`,
`                                modifier = Modifier.onGloballyPositioned { targetPositions[3] = Offset(it.positionInParent().x + it.size.width / 2f, it.positionInParent().y + it.size.height / 2f) }`);

code = code.replace(
`                                modifier = Modifier.onGloballyPositioned { targetPositions[4] = it.positionInParent().x + it.size.width / 2f }`,
`                                modifier = Modifier.onGloballyPositioned { targetPositions[4] = Offset(it.positionInParent().x + it.size.width / 2f, it.positionInParent().y + it.size.height / 2f) }`);

// Make sure to import graphicsLayer, scale, rememberInfiniteTransition etc.
code = code.replace(
`import androidx.compose.ui.Modifier`,
`import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.animation.core.*`);

fs.writeFileSync('app/src/main/java/com/example/ui/MainApp.kt', code);
