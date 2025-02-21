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
import java.io.File


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RowScope.notesDisplay(song: Song) {
    Box(
        modifier = Modifier
            .weight(1f)
            .height(160.dp).padding(8.dp,0.dp, 0.dp, 0.dp)
    ) {
        val scrollState = rememberScrollState()
        val coroutineScope = rememberCoroutineScope()

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
                // Each bar needs at least 300dp width for better visibility, multiply by number of bars and add padding
                val minWidth = (song.bars.size * 300 + 100).dp
                MusicStaff(
                    song = song,
                    modifier = Modifier.width(minWidth)
                )
            }

            HorizontalScrollbar(
                modifier = Modifier.fillMaxWidth(),
                adapter = ScrollbarAdapter(scrollState)
            )
        }
    }
}

fun createBarsFromNotes(notes: List<Note>, notesPerBar: Int = 4): List<Bar> {
    return notes.chunked(notesPerBar).map { notesInBar ->
        Bar(sign = "4/4", notes = notesInBar)
    }
}

fun readNotesFromFile(filePath: String): List<Note> {
    val file = File(filePath)
    if (!file.exists()) return emptyList()

    return file.readLines()
        .flatMap { line ->
            line.split(",").mapNotNull { rawNote ->
                if (rawNote.length > 1) {
                    val durationSymbol = rawNote.first().toString()
                    val pitch = rawNote.drop(1)

                    val duration = when (durationSymbol) {
                        "W" -> 8 // Ganze Note
                        "H" -> 4 // Halbe Note
                        "Q" -> 2 // Viertelnote
                        "E" -> 1 // Achtelnote
                        else -> null
                    }

                    if (duration != null && pitch.matches(Regex("[A-G][#b]?\\d"))) {
                        Note(duration = duration, pitch = pitch)
                    } else {
                        println("Ungültige Note ignoriert: $rawNote")
                        null
                    }
                } else {
                    println("Ungültige Note ignoriert: $rawNote")
                    null
                }
            }
        }
}