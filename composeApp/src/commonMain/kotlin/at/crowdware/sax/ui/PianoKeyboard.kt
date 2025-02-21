/*
 * Copyright (C) 2025 CrowdWare
 *
 * This file is part of Sax.
 *
 *  Sax is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Sax is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Sax.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.crowdware.sax.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.dp
import org.jetbrains.skia.*

@Composable
fun PianoKeyboard(onNoteClick: (String) -> Unit) {
    val hoveredNote: MutableState<String?> = remember { mutableStateOf(null) }
    val pressedNote: MutableState<String?> = remember { mutableStateOf(null) }
    
    val whiteNotes = arrayOf(
        "C4", "D4", "E4", "F4", "G4", "A4", "B4",
        "C5", "D5", "E5", "F5", "G5", "A5", "B5"
    )

    val blackNotes = arrayOf(
        "C#4", "D#4", "F#4", "G#4", "A#4", "", "",
        "C#5", "D#5", "F#5", "G#5", "A#5"
    )

    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
        .pointerInput(Unit) {
            detectTapGestures { offset ->
                val whiteKeyWidth = size.width / 14
                val blackKeyWidth = whiteKeyWidth * 0.6f

                // Check black keys first
                val blackKeyOffsets = arrayOf(0, 1, 3, 4, 5)
                var i = 0
                while (i < 2) { // For each octave
                    var j = 0
                    while (j < blackKeyOffsets.size) {
                        val pos = blackKeyOffsets[j]
                        val x = (pos + i * 7 + 0.7f) * whiteKeyWidth
                        val rect = Rect(
                            offset = Offset(x, 0f),
                            size = Size(blackKeyWidth, size.height * 0.6f)
                        )
                        if (rect.contains(offset)) {
                            val noteIndex = i * 7 + j
                            val note = if (noteIndex < blackNotes.size) blackNotes[noteIndex] else null
                            if (note != null && note.isNotEmpty()) {
                                pressedNote.value = note
                                onNoteClick(note)
                                return@detectTapGestures
                            }
                        }
                        j++
                    }
                    i++
                }

                // Check white keys
                val whiteKeyIndex = (offset.x / whiteKeyWidth).toInt()
                if (whiteKeyIndex >= 0 && whiteKeyIndex < whiteNotes.size) {
                    val note = whiteNotes[whiteKeyIndex]
                    pressedNote.value = note
                    onNoteClick(note)
                }
            }
        }
    ) {
        val whiteKeyWidth = size.width / 14
        val blackKeyWidth = whiteKeyWidth * 0.6f
        val blackKeyHeight = size.height * 0.6f

        // Draw white keys
        var i = 0
        while (i < whiteNotes.size) {
            val isHovered = whiteNotes[i] == hoveredNote.value
            val isPressed = whiteNotes[i] == pressedNote.value
            val keyColor = when {
                isPressed -> Color(0xFFE0E0E0)  // Light gray when pressed
                isHovered -> Color(0xFFF5F5F5)  // Very light gray when hovered
                else -> Color.White
            }
            drawRect(
                color = keyColor,
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
            // Draw note text
            val note = whiteNotes[i][0].toString()
            drawNoteText(note, i * whiteKeyWidth + whiteKeyWidth / 2 - 7, size.height - 8)
            i++
        }

        // Draw black keys
        val blackKeyOffsets = arrayOf(0, 1, 3, 4, 5)
        i = 0
        while (i < 2) { // For each octave
            var j = 0
            while (j < blackKeyOffsets.size) {
                val pos = blackKeyOffsets[j]
                val x = (pos + i * 7 + 0.7f) * whiteKeyWidth
                val noteIndex = i * 7 + j
                if (noteIndex < blackNotes.size) {
                    val note = blackNotes[noteIndex]
                    if (note.isNotEmpty()) {
                        val isHovered = note == hoveredNote.value
                        val isPressed = note == pressedNote.value
                        val keyColor = when {
                            isPressed -> Color(0xFF404040)  // Lighter black when pressed
                            isHovered -> Color(0xFF303030)  // Dark gray when hovered
                            else -> Color.Black
                        }
                        drawRect(
                            color = keyColor,
                            topLeft = Offset(x, 0f),
                            size = Size(blackKeyWidth, blackKeyHeight)
                        )
                    }
                }
                j++
            }
            i++
        }
    }
}

fun DrawScope.drawNoteText(note: String, x: Float, y: Float) {
    drawIntoCanvas { canvas ->
        val font = Font()
        font.size = 18F
        val paint = Paint()
        paint.color = org.jetbrains.skia.Color.BLACK
        val textLine = TextLine.make(note, font)
        canvas.nativeCanvas.drawTextLine(textLine, x, y, paint)
    }
}
