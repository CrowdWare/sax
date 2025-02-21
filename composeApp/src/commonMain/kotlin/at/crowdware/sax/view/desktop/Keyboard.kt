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

package at.crowdware.sax.view.desktop

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.crowdware.sax.ui.PianoKeyboard
import at.crowdware.sax.utils.Song
import java.io.File

fun appendNoteToFile(note: String, filePath: String = "notes.txt") {
    val file = File(filePath)
    file.appendText("$note,") // Schreibe die Note mit einem Komma getrennt
}

fun getLetterForDuration(duration: Int): String {
    return when(duration) {
        2 -> "E"    // Achtelnote
        3 -> "e"    // Punktierte Achtelnote
        4 -> "Q"    // Viertelnote
        6 -> "q"    // Punktierte Viertelnote
        8 -> "H"    // Halbe Note
        12 -> "h"   // Punktierte Halbe Note
        16 -> "W"   // Ganze Note
        else -> {"x"}
    }
}



@Composable
fun NoteStaffWithNotes(pitches: List<String>) {
    val notePositions = mapOf(
        "C4" to 71f, "D4" to 66f, "E4" to 61f, "F4" to 56f,
        "G4" to 51f, "A4" to 46f, "B4" to 41f, "C5" to 36f,
        "D5" to 31F, "E5" to 26F, "F5" to 21F, "G5" to 16F,
        "A5" to 11F, "B5" to 6F
    )

    Box(modifier = Modifier.fillMaxWidth().height(120.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val staffTop = 20f
            val lineSpacing = 10f

            // 🎼 5 Notenlinien zeichnen
            for (i in 0..4) {
                drawLine(
                    color = Color.Black,
                    start = Offset(0f, staffTop + i * lineSpacing),
                    end = Offset(size.width, staffTop + i * lineSpacing),
                    strokeWidth = 2f
                )
            }

            // 🎵 Noten für jede Taste darstellen
            val keySpacing = size.width / pitches.size
            pitches.forEachIndexed { index, pitch ->
                notePositions[pitch]?.let { yPosition ->
                    drawCircle(
                        color = Color(0xFF00EC4A),
                        radius = 5f,
                        center = Offset(index * keySpacing + keySpacing / 2, yPosition)
                    )
                }
            }
        }
    }
}

@Composable
fun RowScope.keyboard(onSongUpdated: (Song) -> Unit) {
    var notes by remember { mutableStateOf(TextFieldValue("")) }
    var selectedNoteDuration by remember { mutableStateOf(2) }

    Row(modifier = Modifier.height(190.dp).padding(4.dp)) {
        Column(modifier = Modifier.padding(8.dp).width(450.dp)) {
            NoteDurationSelector(
                onNoteDurationChange = { newDuration ->
                    selectedNoteDuration = newDuration
                },
                onUndoLastNote = {
                    removeLastNoteFromFile("notes.txt")

                    // Song neu laden und aktualisieren
                    val notes = readNotesFromFile("notes.txt")
                    val bars = createBarsFromNotes(notes)
                    val updatedSong = Song(name = "Loaded from File", bars = bars)
                    onSongUpdated(updatedSong)
                },
                onInsertRest = {
                    appendRestToFile(selectedNoteDuration, "notes.txt")

                    // Song neu laden und aktualisieren
                    val notes = readNotesFromFile("notes.txt")
                    val bars = createBarsFromNotes(notes)
                    val updatedSong = Song(name = "Loaded from File", bars = bars)
                    onSongUpdated(updatedSong)
                }
            )
        }

        Column(modifier = Modifier.padding(8.dp).width(500.dp).fillMaxHeight()) {
            val pianoKeys = listOf("C4", "D4", "E4", "F4", "G4", "A4", "B4", "C5", "D5", "E5", "F5", "G5", "A5", "B5")
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp) // ⬅️ Weniger Höhe für das Notensystem
            ) {
                NoteStaffWithNotes(pianoKeys)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F) // ⬅️ Sorgt dafür, dass das Klavier sich anpasst
            ) {
                PianoKeyboard { note ->
                    val durationSymbol = getLetterForDuration(selectedNoteDuration)
                    val noteWithDuration = "$durationSymbol$note"

                    val file = File("notes.txt")
                    file.appendText("$noteWithDuration,")

                    val notes = readNotesFromFile("notes.txt")
                    val bars = createBarsFromNotes(notes)
                    val exampleSong = Song(name = "Loaded from File", bars = bars)

                    onSongUpdated(exampleSong)
                }
            }
        }
    }
}

@Composable
fun NoteDurationSelector(
    onNoteDurationChange: (Int) -> Unit,
    onUndoLastNote: () -> Unit,
    onInsertRest: () -> Unit // Callback für Pause hinzufügen
) {
    val durationGroups = listOf(2, 3, 4, 6, 8, 12, 16)
    var selectedDuration by remember { mutableStateOf(2) }

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Selected Duration: ${durationToText(selectedDuration)}",
            style = TextStyle(color = MaterialTheme.colorScheme.onPrimary)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            durationGroups.forEach { duration ->
                val isSelected = selectedDuration >= duration
                Box(
                    modifier = Modifier
                        .width(
                            when (duration) {
                                2 -> 40.dp
                                3, 4 -> 20.dp
                                6, 8 -> 44.dp
                                12, 16 -> 92.dp
                                else -> 40.dp
                            }
                        )
                        .height(25.dp)
                        .background(if (isSelected) Color(0xFF00EC4A) else Color.Gray)
                        .clickable {
                            selectedDuration = duration
                            onNoteDurationChange(selectedDuration)
                        }
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        // Row für die Buttons, damit sie nebeneinander sind
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back-Button
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .background(Color.Red)
                    .clickable { onUndoLastNote() },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Back", color = Color.White, fontSize = 16.sp)
            }

            // Pause-Button
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .background(Color.Blue)
                    .clickable { onInsertRest() },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Pause", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}

fun durationToText(duration: Int): String {
    return when (duration) {
        2 -> "Eighth note"          // Achtelnote
        3 -> "Dotted eighth note"   // Punktierte Achtelnote
        4 -> "Quarter note"         // Viertelnote
        6 -> "Dotted quarter note"  // Punktierte Viertelnote
        8 -> "Half note"            // Halbe Note
        12 -> "Dotted half note"    // Punktierte Halbe Note
        16 -> "Whole note"          // Ganze Note
        else -> "Unknown"
    }
}

fun removeLastNoteFromFile(filePath: String = "notes.txt") {
    val file = File(filePath)
    if (!file.exists()) return

    val notes = file.readText().trimEnd(',').split(",").toMutableList()
    if (notes.isNotEmpty()) {
        notes.removeLast()
        file.writeText(notes.joinToString(",") + ",") // Neue Notenliste speichern
    }
}

fun appendRestToFile(duration: Int, filePath: String = "notes.txt") {
    val file = File(filePath)
    val durationSymbol = getLetterForDuration(duration)
    file.appendText("${durationSymbol}R,") // "R" steht für eine Pause
}