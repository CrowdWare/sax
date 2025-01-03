package at.crowdware.sax.view.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import at.crowdware.sax.ui.PianoKeyboard


@Composable
fun RowScope.keyboard() {
    var notes by remember { mutableStateOf(TextFieldValue("")) }
    var selectedNoteDuration by remember { mutableStateOf("1/4") }

    val durationMap = mapOf(
        "1" to "W",    // Ganze Note
        "1/2" to "H",  // Halbe Note
        "1/4" to "Q",  // Viertelnote
        "1/8" to "E",  // Achtelnote
        "1/16" to "S", // Sechzehntelnote
        "1/32" to "T", // Zweiunddreißigstel
        "1/64" to "X"  // Vierundsechzigstel
    )

    Row(modifier = Modifier.height(200.dp).padding(4.dp)) {
        // NoteDurationSelector links
        Column(modifier = Modifier.padding(8.dp).width(450.dp)) {
            // Umschalter für Notenwert
            /*NoteDurationSelector(
                selectedDuration = selectedNoteDuration,
                onDurationChange = { newDuration ->
                    selectedNoteDuration = newDuration
                }
            )*/
            NoteDurationSelector(onNoteDurationChange = {})
        }

        Column(modifier = Modifier.padding(8.dp).width(500.dp).fillMaxHeight()) {
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))

            PianoKeyboard { note ->
                val durationKuerzel = durationMap[selectedNoteDuration] ?: selectedNoteDuration
                val noteWithDuration = "$durationKuerzel$note"
                val cursorPosition = notes.selection.start
                val newText =
                    notes.text.substring(0, cursorPosition) + noteWithDuration + "," + notes.text.substring(cursorPosition)
                notes = TextFieldValue(newText, TextRange(cursorPosition + noteWithDuration.length + 1))
                println("Note clicked: $note")
            }
        }
    }
}

@Composable
fun NoteDurationSelector(
    selectedDuration: String,
    onDurationChange: (String) -> Unit
) {
    Column {
        Text("Choose note length:")

        Row(modifier = Modifier.padding(top = 8.dp)) {
            // Erste Spalte
            Column(modifier = Modifier.weight(1f)) {
                listOf("1/32", "1/16", "1/8").forEach { duration ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedDuration == duration,
                            onClick = { onDurationChange(duration) }
                        )
                        Text(duration)
                    }
                }
            }

            // Zweite Spalte
            Column(modifier = Modifier.weight(1f)) {
                listOf("1/4", "1/2", "1").forEach { duration ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedDuration == duration,
                            onClick = { onDurationChange(duration) }
                        )
                        Text(duration)
                    }
                }
            }
        }
    }
}
/*
Note,Dauer
C4,W    // Ganze Note
D4,H    // Halbe Note
E4,Q    // Viertelnote
F4,E    // Achtelnote
G4,S    // Sechzehntelnote
A4,T    // Zweiunddreißigstel
B4,X    // Vierundsechzigstel

WC4,HD#4,EC4
 */



@Composable
fun NoteDurationSelector(
    onNoteDurationChange: (Int) -> Unit // Rückmeldung für die ausgewählte Notenlänge
) {
    val maxSquares = 16 // Insgesamt 16 Quadrate
    var selectedSquares by remember { mutableStateOf(0) } // Anzahl ausgewählter Quadrate

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Selected Duration: ${durationToText(selectedSquares)}")

        Spacer(modifier = Modifier.height(8.dp))

        // Anzeige der 16 Quadrate
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            repeat(maxSquares) { index ->
                val isSelected = index < selectedSquares
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(if (isSelected) Color.Green else Color.Gray)
                        .clickable {
                            selectedSquares = if (index + 1 == selectedSquares) {
                                0 // Deselektieren
                            } else {
                                index + 1 // Auswählen bis zu diesem Quadrat
                            }
                            onNoteDurationChange(selectedSquares) // Callback mit neuer Länge
                        }
                )
            }
        }
    }
}

fun durationToText(duration: Int): String {
    return when (duration) {
        2 -> "1/8"
        3 -> "1/8 Punktiert"
        4 -> "1/4"
        6 -> "1/4 Punktiert"
        8 -> "1/2"
        12 -> "1/2 Punktiert"
        16 -> "1"
        else -> "${duration}/16" // Benutzerdefinierte Länge
    }
}


