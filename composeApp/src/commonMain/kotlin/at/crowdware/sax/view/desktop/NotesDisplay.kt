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


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RowScope.notesDisplay() {
    Box(
        modifier = Modifier
            .weight(1f)
            .height(160.dp).padding(8.dp,0.dp, 0.dp, 0.dp)
    ) {
        val scrollState = rememberScrollState()
        val coroutineScope = rememberCoroutineScope()
        val exampleSong = Song(
            name = "Test Song",
            bars = listOf(
                /*
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
                ),*/
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