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
fun RowScope.keyboard() {
    var notes by remember { mutableStateOf(TextFieldValue("")) }
    var selectedNoteDuration by remember { mutableStateOf(2) }

    Row(modifier = Modifier.height(160.dp).padding(4.dp)) {
        // NoteDurationSelector links
        Column(modifier = Modifier.padding(8.dp).width(450.dp)) {
            NoteDurationSelector(onNoteDurationChange = { newDuration ->
                selectedNoteDuration = newDuration
            })
            Spacer(modifier = Modifier.height(4.dp))
            Column(modifier = Modifier.background(color=MaterialTheme.colorScheme.background).padding(4.dp)) {
                BasicTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    modifier = Modifier.fillMaxWidth().background(Color.Transparent),
                    singleLine = true,
                    textStyle = TextStyle(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
                )
            }
        }

        Column(modifier = Modifier.padding(8.dp).width(500.dp).fillMaxHeight()) {
            PianoKeyboard { note ->
                val durationKuerzel = getLetterForDuration(selectedNoteDuration)
                val noteWithDuration = "$durationKuerzel$note"
                val cursorPosition = notes.selection.start
                val newText =
                    notes.text.substring(0, cursorPosition) + noteWithDuration + "," + notes.text.substring(cursorPosition)
                notes = TextFieldValue(newText, TextRange(cursorPosition + noteWithDuration.length + 1))
            }
        }
    }
}

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