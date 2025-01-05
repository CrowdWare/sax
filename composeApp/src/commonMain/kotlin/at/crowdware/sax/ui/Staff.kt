package at.crowdware.sax.ui


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import at.crowdware.sax.utils.Bar
import at.crowdware.sax.utils.Song


@Composable
fun MusicStaff(song: Song, modifier: Modifier = Modifier) {
    val barWidths = song.bars.map { calculateBarWidth(it) }
    val totalWidth = barWidths.sum()

    Row(
        modifier = modifier
    ) {
        song.bars.forEachIndexed { index, bar ->
            val barWidth = barWidths[index]
            BarStaff(
                bar = bar,
                startX = 10f,
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


@Composable
fun BarStaff(bar: Bar, startX: Float, modifier: Modifier = Modifier) {
    val staffHeight = 150f
    val staffLines = listOf(120f, 100f, 80f, 60f, 40f) // Die fünf Linien des Notensystems
    val rectHeight = 14f // Höhe der Rechtecke für Noten
    val ledgerLineLength = 20f // Länge der Hilfslinie
    
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
                if (note.pitch == "C4") {
                    drawLine(
                        color = Color.Black,
                        start = Offset(currentX - ledgerLineLength/2, y),
                        end = Offset(currentX + note.duration * stepX - 10 + ledgerLineLength/2, y),
                        strokeWidth = 2f
                    )
                }
                drawRoundRect(
                    color = Color.Green,
                    topLeft = Offset(
                        x = currentX,
                        y = y - rectHeight / 2 // Zentriere das Rechteck auf der Y-Achse
                    ),
                    size = Size(
                        width = note.duration * stepX - 10, // Breite proportional zur Dauer
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
/*
fun calculateBarWidth(bar: Bar): Float {
    val noteWidth = 50f // Breite einer Note
    val baseWidth = 60f // Basisbreite der Bar (für Balkenanfang und -ende)
    val barContentWidth = bar.notes.sumOf { it.duration } * noteWidth / 8 // Achtel-Dauer skaliert
    return baseWidth + barContentWidth
}
*/

fun calculateBarWidth(bar: Bar): Float {
    val noteWidth = 80f  // Erhöht für bessere Sichtbarkeit
    val baseWidth = 100f // Mehr Platz zwischen den Takten
    val barContentWidth = bar.notes.sumOf { it.duration } * noteWidth / 8
    return baseWidth + barContentWidth
}