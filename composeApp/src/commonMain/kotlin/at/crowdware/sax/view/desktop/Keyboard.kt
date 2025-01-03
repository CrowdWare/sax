package at.crowdware.sax.view.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import at.crowdware.sax.ui.PianoKeyboard


@Composable
fun RowScope.keyboard() {
    var notes by remember { mutableStateOf(TextFieldValue("")) }
    Column(modifier = Modifier.width(500.dp).background(MaterialTheme.colors.background).height(140.dp).padding(4.dp)) {
        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text("Notes") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
        PianoKeyboard { note ->
            val cursorPosition = notes.selection.start
            val newText = notes.text.substring(0, cursorPosition) + note + "," + notes.text.substring(cursorPosition)
            notes = TextFieldValue(newText, TextRange(cursorPosition + note.length + 1))
            println("Note clicked: $note")
        }

    }
}


