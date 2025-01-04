package at.crowdware.sax.ui


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import at.crowdware.sax.utils.Bar
import at.crowdware.sax.utils.Note
import at.crowdware.sax.utils.Song



@Composable
fun MusicStaff(song: Song) {
    val barWidths = song.bars.map { calculateBarWidth(it) } // Berechne die Breiten der Bars

    // Nutze eine LazyRow für horizontales Layout
    LazyRow(modifier = Modifier.fillMaxWidth()) {
        items(song.bars.size) { index ->
            val bar = song.bars[index]
            val barWidth = barWidths[index]

            BarStaff(
                bar = bar,
                startX = 10f, // Lokale X-Koordinaten (wird innerhalb von BarStaff geregelt)
                modifier = Modifier.width(barWidth.dp)
            )
        }
    }
}

fun calculateNoteY(note: String): Float {
    val baseY = 100f // Basislinie für B4 (mittlere Linie)
    val step = 10f   // Abstand zwischen zwei Notenlinien oder Lücken

    // Mapping der Noten zu relativen Positionen
    val notePositionMap = mapOf(
        // Unterhalb der Basislinie (B4)
        "A4" to 1,  // Erste Lücke unter B4
        "G4" to 0,  // Zweite Linie unter B4
        "F4" to -1,  // Dritte Lücke unter B4
        "E4" to -2,  // Vierte Linie unter B4
        "D4" to -3,  // Fünfte Lücke unter B4
        "C4" to -4,  // Sechste Linie unter B4

        // Basislinie und oberhalb
        "B4" to 2,   // Basislinie
        "C5" to 3,   // Erste Lücke über B4
        "D5" to 4,   // Erste Linie über B4
        "E5" to 5,   // Zweite Lücke über B4
        "F5" to 6,   // Zweite Linie über B4
        "G5" to 7,   // Dritte Lücke über B4
        "A5" to 8    // Dritte Linie über B4
    )

    val position = notePositionMap[note] ?: error("Unknown note: $note")
    return baseY - (position * step) // Nach oben bewegen sich die Y-Werte nach unten
}

/*
@Composable
fun BarStaff(bar: Bar, startX: Float, modifier: Modifier = Modifier) {
    val staffHeight = 150f
    val staffLines = listOf(120f, 100f, 80f, 60f, 40f) // Die fünf Linien des Notensystems

    // Gesamtdauer der Noten in der Bar
    val totalDuration = bar.notes.sumOf { it.duration }

    Canvas(modifier = modifier.height(staffHeight.dp)) {
        // Zeichne die 5 Linien des Notensystems
        staffLines.forEach { y ->
            drawLine(
                color = Color.Black,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 2f
            )
        }

        // Berechne die horizontale Schrittweite pro Achtel
        val stepX = size.width / totalDuration

        // Zeichne die Noten
        var currentX = startX
        bar.notes.forEach { note ->
            if (note.pitch != null) {
                val y = calculateNoteY(note.pitch)
                drawCircle(
                    color = Color.Black,
                    radius = 8f,
                    center = Offset(currentX, y)
                )
            }

            // Aktualisiere die X-Position basierend auf der Notendauer
            currentX += note.duration * stepX
        }
    }
}*/

@Composable
fun BarStaff(bar: Bar, startX: Float, modifier: Modifier = Modifier) {
    val staffHeight = 150f
    val staffLines = listOf(120f, 100f, 80f, 60f, 40f) // Die fünf Linien des Notensystems
    val rectHeight = 14f // Höhe der Rechtecke für Noten

    // Gesamtdauer der Noten in der Bar
    val totalDuration = bar.notes.sumOf { it.duration }

    Canvas(modifier = modifier.height(staffHeight.dp)) {
        // Zeichne die 5 Linien des Notensystems
        staffLines.forEach { y ->
            drawLine(
                color = Color.Black,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 2f
            )
        }

        // Berechne die horizontale Schrittweite pro Achtel
        val stepX = size.width / totalDuration

        // Zeichne die Noten
        var currentX = startX
        bar.notes.forEach { note ->
            if (note.pitch != null) {
                val y = calculateNoteY(note.pitch)
                drawRoundRect(
                    color = Color.Green,
                    topLeft = Offset(
                        x = currentX,
                        y = y - rectHeight / 2 // Zentriere das Rechteck auf der Y-Achse
                    ),
                    size = Size(
                        width = note.duration * stepX -5, // Breite proportional zur Dauer
                        height = rectHeight // Konstante Höhe des Rechtecks
                    ),
                    cornerRadius = CornerRadius(x = 5f, y = 5f) // Abgerundete Ecken
                )
            }

            // Aktualisiere die X-Position basierend auf der Notendauer
            currentX += note.duration * stepX
        }
    }
}

fun calculateBarWidth(bar: Bar): Float {
    val noteWidth = 40f // Breite einer Note
    val baseWidth = 50f // Basisbreite der Bar (für Balkenanfang und -ende)
    val barContentWidth = bar.notes.sumOf { it.duration } * noteWidth / 8 // Achtel-Dauer skaliert
    return baseWidth + barContentWidth
}

fun DrawScope.drawNoteOnStaff(note: Note, lineSpacing: Float, positionX: Float) {
    if (note.pitch == null) {
        // Zeichne Pause
        val pauseY = size.height / 2
        drawLine(
            color = Color.Black,
            start = Offset(positionX - 5f, pauseY),
            end = Offset(positionX + 5f, pauseY),
            strokeWidth = 2f
        )
        return
    }

    val noteLine = noteToStaffLine(note.pitch)
    val noteY = noteLine * lineSpacing
    val noteRadius = 8f
    drawCircle(
        color = Color.Black,
        radius = noteRadius,
        center = Offset(positionX, noteY)
    )

    if (note.duration >= 4) {
        drawLine(
            color = Color.Black,
            start = Offset(positionX + noteRadius, noteY),
            end = Offset(positionX + noteRadius, noteY - 30f),
            strokeWidth = 2f
        )
    }

    if (note.isTied) {
        drawArc(
            color = Color.Black,
            startAngle = 0f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(positionX - 10f, noteY - 15f),
            size = Size(20f, 10f),
            style = Stroke(width = 2f)
        )
    }
}


// Bestimme, auf welcher Linie die Note gezeichnet werden soll (höhere Tonhöhe -> höher in der Liste)
fun noteToStaffLine(pitch: String): Int {
    val noteToLineMapping = mapOf(
        "C3" to 1, "D3" to 1, "E3" to 1, "F3" to 2, "G3" to 2, "A3" to 2, "B3" to 3,
        "C4" to 3, "D4" to 3, "E4" to 3, "F4" to 4, "G4" to 4, "A4" to 4, "B4" to 5,
        "C5" to 5, "D5" to 5, "E5" to 5, "F5" to 6, "G5" to 6, "A5" to 6, "B5" to 7
    )
    return noteToLineMapping[pitch] ?: 3 // Default zu Mittel-C (C4) auf Linie 3
}