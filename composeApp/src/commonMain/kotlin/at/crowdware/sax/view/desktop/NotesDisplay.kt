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

fun createBarsFromNotes(notes: List<Note>): List<Bar> {
    val bars = mutableListOf<Bar>()
    var currentBarNotes = mutableListOf<Note>()
    var currentBarDuration = 0

    for (note in notes) {
        currentBarNotes.add(note) // Pause wie eine Note behandeln
        currentBarDuration += note.duration
        println("dur: ${note.duration}")
        if (currentBarDuration >= 16) { // Ein 4/4-Takt besteht aus 8 Einheiten
            bars.add(Bar(sign = "4/4", notes = currentBarNotes.toList()))
            currentBarNotes.clear()
            currentBarDuration = 0
        }
    }

    if (currentBarNotes.isNotEmpty()) {
        bars.add(Bar(sign = "4/4", notes = currentBarNotes))
    }

    return bars
}

fun readNotesFromFile(filePath: String = "notes.txt"): List<Note> {
    val file = File(filePath)
    if (!file.exists()) return emptyList()

    return file.readText()
        .trimEnd(',')
        .split(",")
        .mapNotNull { entry ->
            if (entry.isEmpty()) return@mapNotNull null

            val durationSymbol = entry.first() // Erster Buchstabe gibt die Dauer an
            val noteName = entry.drop(1) // Rest des Strings ist der Notenname oder "R"

            val duration = when (durationSymbol) {
                'E' -> 2  // Achtelnote
                'e' -> 3  // Punktierte Achtelnote
                'Q' -> 4  // Viertelnote
                'q' -> 6  // Punktierte Viertelnote
                'H' -> 8  // Halbe Note
                'h' -> 12 // Punktierte Halbe Note
                'W' -> 16 // Ganze Note
                else -> null
            }

            if (duration != null) {
                Note(pitch = noteName, duration = duration) // Normale Note speichern
            } else {
                null // Ungültige Einträge ignorieren
            }
        }
}