package at.crowdware.sax.view.desktop

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import at.crowdware.sax.ui.MusicStaff
import at.crowdware.sax.utils.Bar
import at.crowdware.sax.utils.Note
import at.crowdware.sax.utils.Song

@Composable
fun RowScope.notesDisplay() {
    Column(modifier = Modifier.weight(1F).height(160.dp)) {
        val exampleSong = Song(
            name = "Test Song",
            bars = listOf(
                Bar(
                    sign = "4/4",
                    notes = listOf(
                        Note(duration = 2, pitch = "C4"),
                        Note(duration = 2, pitch = "C4"),
                        Note(duration = 2, pitch = "D4"),
                        Note(duration = 2, pitch = "D4"),
                        Note(duration = 2, pitch = "E4"),
                        Note(duration = 2, pitch = "E4"),
                        Note(duration = 2, pitch = "F4"),
                        Note(duration = 2, pitch = "F4")
                    )
                ),
                Bar(
                    sign = "4/4",
                    notes = listOf(
                        Note(duration = 4, pitch = "G4"),
                        Note(duration = 4, pitch = "A4"),
                        Note(duration = 4, pitch = "B4"),
                        Note(duration = 4, pitch = "C5")
                    )
                ),
                Bar(
                    sign = "4/4",
                    notes = listOf(
                        Note(duration = 8, pitch = "G4"),
                        Note(duration = 8, pitch = "A4")
                    ),
                    ),
                Bar(
                    sign = "4/4",
                    notes = listOf(
                        Note(duration = 8, pitch = "B4"),
                        Note(duration = 8, pitch = "C5")
                    ),
                ),
                Bar(
                    sign = "4/4",
                    notes = listOf(
                        Note(duration = 16, pitch = "D5")
                    ),
                ),
                Bar(
                    sign = "4/4",
                    notes = listOf(
                        Note(duration = 16, pitch = "E5")
                    ),
                )
            )
        )
        MusicStaff(exampleSong)
    }
}