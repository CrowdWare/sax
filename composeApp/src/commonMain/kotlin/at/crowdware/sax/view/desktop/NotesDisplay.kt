package at.crowdware.sax.view.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun RowScope.notesDisplay() {
    val value = mutableStateOf(TextFieldState())
    Column(modifier = Modifier.weight(1F).background(color = MaterialTheme.colors.background).height(240.dp)) {
        BasicText(
            text = "Notes",
            modifier = Modifier.padding(8.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        BasicTextField(
            state = value.value,
            modifier = Modifier.padding(8.dp),
        )
    }
}