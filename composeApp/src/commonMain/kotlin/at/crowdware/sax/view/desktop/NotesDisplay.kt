package at.crowdware.sax.view.desktop

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.ScrollbarAdapter
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.dp
import at.crowdware.sax.ui.MusicStaff
import at.crowdware.sax.utils.Bar
import at.crowdware.sax.utils.Note
import at.crowdware.sax.utils.Song
import kotlinx.coroutines.launch

/*
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
                ),
                Bar(
                    sign = "4/4",
                    notes = listOf(
                        Note(duration = 16, pitch = "E5")
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
        // ScrollState für horizontales Scrollen
        val scrollState = rememberScrollState()

        // Scrollbarer Bereich
        Box(
            modifier = Modifier
                .fillMaxWidth() // Begrenze die Breite auf den Bildschirm
                .height(160.dp)
                .horizontalScroll(scrollState)
        ) {
            MusicStaff(exampleSong) // Dein Inhalt
        }

        // Scrollbar hinzufügen
        HorizontalScrollbar(
            modifier = Modifier.fillMaxWidth(),
            adapter = ScrollbarAdapter(scrollState)
        )
    }
}
*/
/*
@Composable
fun RowScope.notesDisplay() {
    Column(modifier = Modifier.weight(1F).height(160.dp)) {
        val scrollState = rememberScrollState()
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
                ),
                Bar(
                    sign = "4/4",
                    notes = listOf(
                        Note(duration = 16, pitch = "E5")
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
        Box(
            modifier = Modifier
                .weight(1f)  // Nimmt den verfügbaren Platz ein
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState)
                    .onPreviewMouseScroll {
                        coroutineScope.launch {
                            // Multipliziere mit einem Faktor für schnelleres/langsameres Scrollen
                            scrollState.scrollBy(it.y * 3)
                        }
                        true
                    }
            ) {
                Row {
                    // Ein wenig Platz am Anfang
                    Spacer(modifier = Modifier.width(16.dp))

                    MusicStaff(exampleSong)

                    // Ein wenig Platz am Ende
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }

            // Scrollbar am unteren Rand
            HorizontalScrollbar(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth(),
                adapter = ScrollbarAdapter(scrollState)
            )
        }
    }
}
*/

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RowScope.notesDisplay() {
    Box(
        modifier = Modifier
            .weight(1f)
            .height(160.dp)
    ) {
        val scrollState = rememberScrollState()
        val coroutineScope = rememberCoroutineScope()
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
                ),
                Bar(
                    sign = "4/4",
                    notes = listOf(
                        Note(duration = 16, pitch = "E5")
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
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .horizontalScroll(scrollState)
                    .onPointerEvent(PointerEventType.Scroll) {
                        coroutineScope.launch {
                            scrollState.scrollBy(it.changes.first().scrollDelta.y * 20)
                        }
                    }
            ) {
                MusicStaff(
                    song = exampleSong,
                    modifier = Modifier.width(1200.dp)
                )
            }

            HorizontalScrollbar(
                modifier = Modifier.fillMaxWidth(),
                adapter = ScrollbarAdapter(scrollState)
            )
        }
    }
}