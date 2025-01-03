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

fun selectedSquaresToHex(selectedSquares: Int): String {
    // Ziehe 1 ab, bevor du den Wert in Hex umwandelst
    val adjustedValue = (selectedSquares - 1).coerceAtLeast(0)  // Stelle sicher, dass der Wert nicht negativ wird
    return adjustedValue.toString(16).uppercase(Locale.getDefault())
}

@Composable
fun RowScope.keyboard() {
    var notes by remember { mutableStateOf(TextFieldValue("")) }
    var selectedNoteDuration by remember { mutableStateOf(4) }

    Row(modifier = Modifier.height(200.dp).padding(4.dp)) {
        // NoteDurationSelector links
        Column(modifier = Modifier.padding(8.dp).width(450.dp)) {
            NoteDurationSelector( onNoteDurationChange = {newDuration ->
                selectedNoteDuration = newDuration})
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
                println("sel: $selectedNoteDuration")
                val durationKuerzel = selectedSquaresToHex(selectedNoteDuration)
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
    onNoteDurationChange: (Int) -> Unit // Rückmeldung für die ausgewählte Notenlänge
) {
    val maxSquares = 16 // Insgesamt 16 Quadrate
    var selectedSquares by remember { mutableStateOf(2) } // Mindestens die ersten beiden Quadrate sind immer ausgewählt

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
                            selectedSquares = if (index + 1 == selectedSquares && selectedSquares > 2) {
                                selectedSquares - 1 // Deselect, but don't go below 2
                            } else if (index + 1 >= 2) {
                                index + 1 // Wähle bis zum aktuellen Quadrat, aber nicht unter 2
                            } else {
                                selectedSquares // Falls kein gültiger Zustand erreicht wird, belassen wir den Wert
                            }
                            onNoteDurationChange(selectedSquares) // Callback mit neuer Länge
                        }
                )
            }
        }
    }
}

/*
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
*/
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


