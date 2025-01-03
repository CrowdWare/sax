package at.crowdware.sax.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Paint

import androidx.compose.ui.unit.dp


import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.graphics.drawscope.DrawContext
import androidx.compose.ui.text.TextLayoutResult

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*

import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.skia.*

@Composable
fun StaticStaff(pressedNotes: List<String>) {
    Canvas(modifier = Modifier.fillMaxWidth().height(120.dp)) {
        val lineSpacing = size.height / 8 // Anpassung für zusätzlichen Platz
        val centerY = size.height / 2

        // Zeichne die 5 Notenlinien
        for (i in 0 until 5) {
            val y = centerY + (i - 2) * lineSpacing
            drawLine(
                color = Color.Black,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 1f
            )
        }

        // Map der Notenpositionen, inkl. Hilfslinien für C4
        val noteOffsets = mapOf(
            "C4" to centerY + 3f * lineSpacing,  // Auf der Hilfslinie unterhalb
            "C#4" to centerY + 3f * lineSpacing,
            "D4" to centerY + 2.5f * lineSpacing,
            "D#4" to centerY + 2.5f * lineSpacing,
            "E4" to centerY + 2f * lineSpacing,
            "F4" to centerY + 1.5f * lineSpacing,
            "F#4" to centerY + 1.5f * lineSpacing,
            "G4" to centerY + lineSpacing,
            "G#4" to centerY + lineSpacing,
            "A4" to centerY + 0.5f * lineSpacing,
            "A#4" to centerY + 0.5f * lineSpacing,
            "B4" to centerY,
            "C5" to centerY - 0.5f * lineSpacing,
            "C#5" to centerY - 0.5f * lineSpacing,
            "D5" to centerY - lineSpacing,
            "D#5" to centerY - lineSpacing,
            "E5" to centerY - 1.5f * lineSpacing,
            "F5" to centerY - 2f * lineSpacing,
            "F#5" to centerY - 2f * lineSpacing,
            "G5" to centerY - 2.5f * lineSpacing,
            "G#5" to centerY - 2.5f * lineSpacing,
            "A5" to centerY - 3f * lineSpacing,
            "A#5" to centerY - 3f * lineSpacing,
            "B5" to centerY - 3.5f * lineSpacing
        )


        // Zeichne die Noten
        val whiteKeyCount = 14 // 2 Oktaven (weiße Tasten)
        val whiteKeyWidth = size.width / whiteKeyCount


        // Zeichne die Hilfslinie für C4
        val c4Y = noteOffsets["C4"] ?: (centerY + 3f * lineSpacing)
        val c4Index = noteToKeyIndex("C4") // Hole die X-Position von C4
        if (c4Index != null) {
            val xStart = (c4Index + 0.25f) * whiteKeyWidth // Linker Rand der Note
            val xEnd = (c4Index + 0.75f) * whiteKeyWidth  // Rechter Rand der Note

            drawLine(
                color = Color.Black,
                start = Offset(xStart, c4Y),
                end = Offset(xEnd, c4Y),
                strokeWidth = 1f
            )
        }

        for ((note, y) in noteOffsets) {
            val index = noteToKeyIndex(note)
            if (index != null) {
                val x = (index + 0.5f) * whiteKeyWidth // Zentriert über der Taste
                val isPressed = pressedNotes.contains(note)
                drawCircle(
                    color = if (isPressed) Color.Red else Color.Black,
                    center = Offset(x, y),
                    radius = 7f
                )
            }
        }
    }
}

@Composable
fun PianoKeyboard(onNoteClick: (String) -> Unit) {
    val whiteNotes = listOf(
        "C4", "D4", "E4", "F4", "G4", "A4", "B4",  // Erste Oktave
        "C5", "D5", "E5", "F5", "G5", "A5", "B5"   // Zweite Oktave
    )

    val blackNotes = listOf(
        "C#4", "D#4", "F#4", "G#4", "A#4", "","", // Erste Oktave
        "C#5", "D#5", "F#5", "G#5", "A#5"   // Zweite Oktave
    )

    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
        .pointerInput(Unit) {
            detectTapGestures { offset ->
                val whiteKeyWidth = size.width / 14
                val blackKeyWidth = whiteKeyWidth * 0.6f

                // Check for black key clicks first
                val blackKeyOffsets = listOf(0, 1, 3, 4, 5) // Positions in an octave
                for (octave in 0 until 2) {
                    blackKeyOffsets.forEachIndexed { index, pos ->
                        val x = (pos + octave * 7 + 0.7f) * whiteKeyWidth
                        val rect = Rect(
                            offset = Offset(x, 0f),
                            size = Size(blackKeyWidth, size.height * 0.6f)
                        )
                        if (rect.contains(offset)) {
                            val noteIndex = octave * 7 + index
                            val note = blackNotes.getOrNull(noteIndex)
                            if (!note.isNullOrBlank()) {
                                onNoteClick(note)
                                return@detectTapGestures
                            }
                        }
                    }
                }

                // Check for white key clicks
                val whiteKeyIndex = (offset.x / whiteKeyWidth).toInt()
                val note = whiteNotes.getOrNull(whiteKeyIndex)
                if (!note.isNullOrBlank()) {
                    onNoteClick(note)
                }
            }
        }) {
        val whiteKeyWidth = size.width / 14
        val blackKeyWidth = whiteKeyWidth * 0.6f
        val blackKeyHeight = size.height * 0.6f

        // Draw white keys
        for (i in whiteNotes.indices) {
            drawRect(
                color = Color.White,
                topLeft = Offset(i * whiteKeyWidth, 0f),
                size = Size(whiteKeyWidth, size.height),
                style = Fill
            )
            drawRect(
                color = Color.Black,
                topLeft = Offset(i * whiteKeyWidth, 0f),
                size = Size(whiteKeyWidth, size.height),
                style = Stroke(width = 2f)
            )
            // Draw note text above the white keys
            val note = whiteNotes[i].substring(0,1)
            drawNoteText(note, i * whiteKeyWidth + whiteKeyWidth / 2 - 7, size.height -10)
        }

        // Draw black keys
        val blackKeyOffsets = listOf(0, 1, 3, 4, 5) // Positions in an octave
        for (octave in 0 until 2) {
            blackKeyOffsets.forEachIndexed { index, pos ->
                val x = (pos + octave * 7 + 0.7f) * whiteKeyWidth
                drawRect(
                    color = Color.Black,
                    topLeft = Offset(x, 0f),
                    size = Size(blackKeyWidth, blackKeyHeight)
                )
                // Draw note text above the black keys
                //val note = blackNotes.getOrNull(octave * 7 + index)
                //if (!note.isNullOrBlank()) {
                //    drawNoteText(note.substring(0,2), x + blackKeyWidth / 2 - 9, -10F) // Adjust y for positioning above key
                //}
            }
        }
    }
}

// Helper function to draw text on the canvas
fun DrawScope.drawNoteText(note: String, x: Float, y: Float) {
    drawIntoCanvas { canvas ->
        val font = Font()
        font.size = 18F
        val paint = org.jetbrains.skia.Paint().apply {
            color = org.jetbrains.skia.Color.BLACK
        }

        val textLine = TextLine.make(note, font)

        canvas.nativeCanvas.drawTextLine(
            textLine,
            x,
            y,
            paint
        )
    }
}
fun noteToKeyIndex(note: String): Int? {
    val allNotes = listOf(
        "C4", "D4", "E4", "F4", "G4", "A4", "B4",
        "C5", "D5", "E5", "F5", "G5", "A5", "B5"
    )
    return allNotes.indexOf(note)
}
/*
fun noteToKeyIndex(note: String): Int? {
    val allNotes = listOf(
        "C4", "C#4", "D4", "D#4", "E4",
        "F4", "F#4", "G4", "G#4", "A4", "A#4", "B4",
        "C5", "C#5", "D5", "D#5", "E5",
        "F5", "F#5", "G5", "G#5", "A5", "A#5", "B5"
    )
    return allNotes.indexOf(note)
}*/

