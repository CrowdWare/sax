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

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
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
fun RowScope.keyboard(onSongUpdated: (Song) -> Unit) {
    var notes by remember { mutableStateOf(TextFieldValue("")) }
    var selectedNoteDuration by remember { mutableStateOf(2) }

    Row(modifier = Modifier.height(160.dp).padding(4.dp)) {
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
                }
            )
        }

        Column(modifier = Modifier.padding(8.dp).width(500.dp).fillMaxHeight()) {
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

/*
@Composable
fun RowScope.keyboard(onSongUpdated: (Song) -> Unit) {
    var notes by remember { mutableStateOf(TextFieldValue("")) }
    var selectedNoteDuration by remember { mutableStateOf(2) }

    Row(modifier = Modifier.height(160.dp).padding(4.dp)) {
        // NoteDurationSelector links
        Column(modifier = Modifier.padding(8.dp).width(450.dp)) {
            NoteDurationSelector(onNoteDurationChange = { newDuration ->
                selectedNoteDuration = newDuration
            })
        }

        Column(modifier = Modifier.padding(8.dp).width(500.dp).fillMaxHeight()) {
            PianoKeyboard { note ->
                val durationSymbol = getLetterForDuration(selectedNoteDuration)
                val noteWithDuration = "$durationSymbol$note"

                // Neue Note in die Datei schreiben
                val file = File("notes.txt")
                file.appendText("$noteWithDuration,")

                // Song neu laden
                val notes = readNotesFromFile("notes.txt")
                val bars = createBarsFromNotes(notes)
                val exampleSong = Song(name = "Loaded from File", bars = bars)

                // Song aktualisieren
                onSongUpdated(exampleSong)
            }

        }
    }
}
*/
@Composable
fun NoteDurationSelector(
    onNoteDurationChange: (Int) -> Unit,
    onUndoLastNote: () -> Unit // Callback für Rückgängig-Aktion
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

        // Rückgängig-Button hinzufügen
        Spacer(modifier = Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .width(120.dp)
                .height(40.dp)
                .background(Color.Red)
                .clickable { onUndoLastNote() },
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Back", color = Color.White, fontSize = 16.sp)
        }
    }
}
/*
@Composable
fun NoteDurationSelector(
    onNoteDurationChange: (Int) -> Unit // Rückmeldung für die ausgewählte Notenlänge
) {
    val durationGroups = listOf(
        2,  // Achtelnote
        3,  // Punktierte Achtelnote
        4,  // Viertelnote
        6,  // Punktierte Viertelnote
        8,  // Halbe Note
        12, // Punktierte Halbe Note
        16  // Ganze Note
    )

    var selectedDuration by remember { mutableStateOf(2) } // Standardmäßig Achtelnote ausgewählt

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Selected Duration: ${durationToText(selectedDuration)}",
            style = TextStyle(color = MaterialTheme.colorScheme.onPrimary))

        Spacer(modifier = Modifier.height(4.dp))

        // Anzeige der Gruppen
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            durationGroups.forEach { duration ->
                val isSelected = selectedDuration >= duration
                Box(
                    modifier = Modifier
                        .width(
                            when (duration) {
                                2 -> 40.dp  // Achtelnote (2 Quadrate)
                                3 -> 20.dp  // Punktierte Achtelnote (3 Quadrate)
                                4 -> 20.dp  // Viertelnote (4 Quadrate)
                                6 -> 44.dp  // Punktierte Viertelnote (6 Quadrate)
                                8 -> 44.dp  // Halbe Note (8 Quadrate)
                                12 -> 92.dp // Punktierte Halbe Note (12 Quadrate)
                                16 -> 92.dp // Ganze Note (16 Quadrate)
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
    }
}
*/
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