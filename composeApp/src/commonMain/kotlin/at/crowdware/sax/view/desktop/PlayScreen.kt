package at.crowdware.sax.view.desktop

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun RowScope.playScreen() {
    var totalHeight by remember { mutableStateOf(0f) }
    Column( modifier = Modifier.weight(1f).fillMaxHeight().background(color = MaterialTheme.colors.primary).padding(4.dp)
        .onGloballyPositioned { coordinates ->
            totalHeight = coordinates.size.height.toFloat()
        }) {
        BasicText(
            text = "Song",
            modifier = Modifier.padding(8.dp),
            maxLines = 1,
            style = TextStyle(color = MaterialTheme.colors.onPrimary),
            overflow = TextOverflow.Ellipsis
        )
        Canvas(modifier = Modifier.fillMaxSize().background(Color.Black)) {
            val circleData = listOf(
                Pair(Color(0xFFFBF68C), listOf(150f, 220f, 290f)),
                Pair(Color(0xFF9DD9FD), listOf(380f, 450f, 520f))
            )

            circleData.forEach { (color, positions) ->
                positions.forEach { yPos ->
                    drawCircle(
                        color = color,
                        radius = 24f,
                        center = Offset(50f, yPos),
                        style = Stroke(width = 4f)
                    )
                }
            }
        }
    }
}