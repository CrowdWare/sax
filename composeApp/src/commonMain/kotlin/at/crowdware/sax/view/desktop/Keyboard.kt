package at.crowdware.sax.view.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import at.crowdware.sax.ui.PianoKeyboard
import at.crowdware.sax.ui.StaticStaff


@Composable
fun RowScope.keyboard() {
    Column(modifier = Modifier.width(500.dp).background(MaterialTheme.colors.background).height(240.dp)) {
            var pressedNotes by remember { mutableStateOf<List<String>>(emptyList()) }
            Column(modifier = Modifier.fillMaxWidth()) {
                StaticStaff(pressedNotes)
                Spacer(modifier = Modifier.height(16.dp))
                PianoKeyboard { note ->
                    pressedNotes = listOf(note)
                }
            }
        }
    }


