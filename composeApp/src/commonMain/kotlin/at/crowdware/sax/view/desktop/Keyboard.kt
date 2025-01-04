package at.crowdware.sax.view.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import at.crowdware.sax.ui.PianoKeyboard
import java.util.*

fun getLetterForDuration(duration: Int): String {
    return when(duration) {
        2 -> "E"    // Achtelnote
        3 -> "e"    // Punktierte Achtelnote
        4 -> "Q"    // Viertelnote
        6 -> "q"     // Punktierte Viertelnote
        8 -> "H"     // Halbe Note
        12 -> "h"    // Punktierte Halbe Note
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
            NoteDurationSelector( onNoteDurationChange = {newDuration ->
                selectedNoteDuration = newDuration})
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes") },
                modifier = Modifier.fillMaxWidth()
            )
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
        2, // Achtelnote
        3, // Punktierte Achtelnote
        4, // Viertelnote
        6, // Punktierte Viertelnote
        8, // Halbe Note
        12, // Punktierte Halbe Note
        16 // Ganze Note
    )

    var selectedDuration by remember { mutableStateOf(2) } // Standardmäßig Achtelnote ausgewählt

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Selected Duration: ${durationToText(selectedDuration)}")

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
                                6 -> 44.dp // Punktierte Viertelnote (6 Quadrate)
                                8 -> 44.dp // Halbe Note (8 Quadrate)
                                12 -> 92.dp // Punktierte Halbe Note (12 Quadrate)
                                16 -> 92.dp // Ganze Note (16 Quadrate)
                                else -> 40.dp
                            }
                        )
                        .height(25.dp)
                        .background(if (isSelected) Color.Green else Color.Gray)
                        .clickable {
                            selectedDuration = duration
                            onNoteDurationChange(selectedDuration)
                        }
                )
            }
        }
    }
}

// Funktion zur Umwandlung der Dauer in Text (z. B. "1/4" oder "1/8")
fun durationToText(duration: Int): String {
    return when (duration) {
        2 -> "Eighth note"  // Achtelnote
        3 -> "Dotted eighth note" // Punktierte Achtelnote
        4 -> "Quarter note"  // Viertelnote
        6 -> "Dotted quarter note" // Punktierte Viertelnote
        8 -> "Half note"  // Halbe Note
        12 -> "Dotted half note" // Punktierte Halbe Note
        16 -> "Whole note"   // Ganze Note
        else -> "Unknown"
    }
}

/*
    1/8     E       XX                  0,1
    1/8.    e       XXX                 2
    1/4     Q       XXXX                3
    1/4.    q       XXXXXX              5
    1/2     H       XXXXXXXX            7
    1/2.    h       XXXXXXXXXXXX        11
    1/1     W       XXXXXXXXXXXXXXXX    15
 */