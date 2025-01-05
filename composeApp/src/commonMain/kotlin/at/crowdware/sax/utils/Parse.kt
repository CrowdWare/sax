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

package at.crowdware.sax.utils


fun parseNotes(input: String): List<Note> {
    // Map für die Dauer-Codes mit der Länge in Achteln
    val durationMap = mapOf(
        "E" to 2,  // Achtelnote
        "e" to 3,  // Punktierte Achtelnote
        "Q" to 4,  // Viertelnote
        "q" to 6,  // Punktierte Viertelnote
        "H" to 8,  // Halbe Note
        "h" to 12, // Punktierte Halbe Note
        "W" to 16  // Ganze Note
    )

    val notes = mutableListOf<Note>()
    val entries = input.split(",") // Noten durch Kommas trennen

    for (entry in entries) {
        // Prüfen, ob es sich um eine Pause handelt
        val isPause = entry.endsWith("P") // Endet die Eingabe auf "P"?
        val core = if (isPause) entry.dropLast(1) else entry // "P" entfernen, wenn es eine Pause ist

        // Erste Zeichenkette ist die Dauer (z. B. "E" oder "e")
        val durationKey = core.substring(0, 1)

        // Rest sind die Noten, getrennt durch "-"
        val notePart = if (core.length > 1) core.substring(1) else null
        val pitches = notePart?.split("-") // Noten durch "-" trennen, falls vorhanden

        if (durationMap.containsKey(durationKey)) {
            // Tied Note prüfen (falls Noten mit Bindestrich verbunden sind)
            val pitch = pitches?.first() // Nur die erste Note wird als pitch verwendet
            val isTied = pitches?.size == 2

            notes.add(
                Note(
                    duration = durationMap[durationKey]!!, // Länge in Achteln
                    pitch = if (isPause) null else pitch,  // Null für Pause, sonst die erste Note
                    isTied = isTied // Tied Note Flag setzen
                )
            )
        } else {
            throw IllegalArgumentException("Unknown note duration: $durationKey")
        }
    }

    return notes
}
/*
fun distributeNotesAcrossBars(
    notes: List<Note>,
    timeSignature: Pair<Int, Int>
): List<Bar> {
    val barLength = timeSignature.first * (16 / timeSignature.second) // Gesamtlänge des Taktes in Achteln
    val bars = mutableListOf<Bar>()
    var currentBar = mutableListOf<Note>()
    var remainingDurationInBar = barLength

    for (note in notes) {
        var noteDuration = note.duration

        while (noteDuration > 0) {
            if (noteDuration <= remainingDurationInBar) {
                // Die Note passt vollständig in den aktuellen Takt
                currentBar.add(note.copy(duration = noteDuration))
                remainingDurationInBar -= noteDuration
                noteDuration = 0
            } else {
                // Die Note muss aufgeteilt werden
                currentBar.add(note.copy(duration = remainingDurationInBar, isTied = true))
                noteDuration -= remainingDurationInBar
                bars.add(Bar(sign = timeSignature, notes = currentBar))
                currentBar = mutableListOf()
                remainingDurationInBar = barLength
            }
        }
    }

    // Füge den letzten Takt hinzu, falls er Noten enthält
    if (currentBar.isNotEmpty()) {
        bars.add(Bar(sign = timeSignature, notes = currentBar))
    }

    return bars
}

*/
/*
fun generateSml(song: Song): String {
    val builder = StringBuilder()
    builder.appendLine("Song {")
    builder.appendLine("    name: \"${song.name}\"")
    builder.appendLine()

    song.bars.forEach { bar ->
        builder.appendLine("    Bar {")
        builder.appendLine("        sign: \"${bar.sign.first}/${bar.sign.second}\"")
        builder.appendLine("        notes: \"${bar.notes.joinToString(",") { noteToString(it) }}\"")
        builder.appendLine("    }")
        builder.appendLine()
    }

    builder.appendLine("}")
    return builder.toString()
}*/

fun noteToString(note: Note): String {
    val durationPrefix = when (note.duration) {
        2 -> "E"    // Achtelnote
        3 -> "e"    // Punktierte Achtelnote
        4 -> "Q"    // Viertelnote
        6 -> "q"    // Punktierte Viertelnote
        8 -> "H"    // Halbe Note
        12 -> "h"   // Punktierte Halbe Note
        16 -> "W"   // Ganze Note
        else -> throw IllegalArgumentException("Invalid duration: ${note.duration}")
    }

    val pitch = note.pitch ?: "P" // "P" für Pause
    return if (note.isTied) "$durationPrefix$pitch-$durationPrefix$pitch" else "$durationPrefix$pitch"
}
/*
fun parseBars(input: String): List<Bar> {
    // Map für die Dauer-Codes mit der Länge in Achteln
    val durationMap = mapOf(
        "E" to 2,  // Achtelnote
        "e" to 3,  // Punktierte Achtelnote
        "Q" to 4,  // Viertelnote
        "q" to 6,  // Punktierte Viertelnote
        "H" to 8,  // Halbe Note
        "h" to 12, // Punktierte Halbe Note
        "W" to 16  // Ganze Note
    )

    val bars = mutableListOf<Bar>()
    var currentBarNotes = mutableListOf<Note>()
    var currentBarDuration = 0  // Summe der Notenlängen in der aktuellen Bar
    var currentSign = "4/4"  // Standard-Taktart

    val entries = input.split(",") // Noten durch Kommas trennen

    for (entry in entries) {
        // Prüfen, ob es sich um eine Pause handelt
        val isPause = entry.endsWith("P") // Endet die Eingabe auf "P"?
        val core = if (isPause) entry.dropLast(1) else entry // "P" entfernen, wenn es eine Pause ist

        // Erste Zeichenkette ist die Dauer (z. B. "E" oder "e")
        val durationKey = core.substring(0, 1)

        // Rest sind die Noten, getrennt durch "-"
        val notePart = if (core.length > 1) core.substring(1) else null
        val pitches = notePart?.split("-") // Noten durch "-" trennen

        if (durationMap.containsKey(durationKey)) {
            val duration = durationMap[durationKey]!!
            val pitch = pitches?.first() // Nur die erste Note wird als pitch verwendet
            val isTied = pitches?.size == 2

            // Berechnung der Gesamtdauer in der aktuellen Bar
            if (currentBarDuration + duration <= 16) { // Wir gehen davon aus, dass der Takt 4/4 (16 Achtel) ist
                currentBarNotes.add(Note(duration, if (isPause) null else pitch, isTied))
                currentBarDuration += duration
            } else {
                // Eine neue Bar beginnt, wenn die aktuelle Bar voll ist
                bars.add(Bar(currentSign, currentBarNotes))
                currentBarNotes = mutableListOf()
                currentBarDuration = duration
                currentBarNotes.add(Note(duration, if (isPause) null else pitch, isTied))
            }
        } else {
            throw IllegalArgumentException("Unknown note duration: $durationKey")
        }
    }

    // Füge die letzte Bar hinzu
    if (currentBarNotes.isNotEmpty()) {
        bars.add(Bar(currentSign, currentBarNotes))
    }

    return bars
}
*/

fun parseBars(input: String): List<Bar> {
    // Map für die Dauer-Codes mit der Länge in Achteln
    val durationMap = mapOf(
        "E" to 2,  // Achtelnote
        "e" to 3,  // Punktierte Achtelnote
        "Q" to 4,  // Viertelnote
        "q" to 6,  // Punktierte Viertelnote
        "H" to 8,  // Halbe Note
        "h" to 12, // Punktierte Halbe Note
        "W" to 16  // Ganze Note
    )

    val bars = mutableListOf<Bar>()
    var currentBarNotes = mutableListOf<Note>()
    var currentBarDuration = 0  // Summe der Notenlängen in der aktuellen Bar
    var currentSign = "4/4"  // Standard-Taktart

    val entries = input.split(",") // Noten durch Kommas trennen

    for (entry in entries) {
        // Prüfen, ob es sich um eine Pause handelt
        val isPause = entry.endsWith("P") // Endet die Eingabe auf "P"?
        val core = if (isPause) entry.dropLast(1) else entry // "P" entfernen, wenn es eine Pause ist

        // Erste Zeichenkette ist die Dauer (z. B. "E" oder "e")
        val durationKey = core.substring(0, 1)

        // Rest sind die Noten, getrennt durch "-"
        val notePart = if (core.length > 1) core.substring(1) else null
        val pitches = notePart?.split("-") // Noten durch "-" trennen

        if (durationMap.containsKey(durationKey)) {
            val duration = durationMap[durationKey]!!
            val pitch = pitches?.first() // Nur die erste Note wird als pitch verwendet
            val isTied = pitches?.size == 2

            // Wenn es eine Pause ist
            if (isPause) {
                // Berechne, wie viel der Pause in die aktuelle Bar passt
                val remainingDuration = 16 - currentBarDuration  // 16 = 4/4 (maximale Bar-Dauer)

                if (duration <= remainingDuration) {
                    currentBarNotes.add(Note(duration, null, isTied))  // Füge Pause zur aktuellen Bar hinzu
                    currentBarDuration += duration
                } else {
                    // Falls Pause die aktuelle Bar überschreitet, aufteilen
                    // Teilweise in die aktuelle Bar und Rest in die nächste Bar
                    val firstPart = remainingDuration
                    val secondPart = duration - remainingDuration

                    currentBarNotes.add(Note(firstPart, null, isTied))  // Erste Hälfte der Pause in dieser Bar
                    bars.add(Bar(currentSign, currentBarNotes))  // Die vollständige Bar hinzufügen

                    // Nächste Bar mit der verbleibenden Pause
                    currentBarNotes = mutableListOf(Note(secondPart, null, isTied))
                    currentBarDuration = secondPart
                }
            } else {
                // Berechnung der Gesamtdauer in der aktuellen Bar
                if (currentBarDuration + duration <= 16) { // Wir gehen davon aus, dass der Takt 4/4 (16 Achtel) ist
                    currentBarNotes.add(Note(duration, pitch, isTied))
                    currentBarDuration += duration
                } else {
                    // Eine neue Bar beginnt, wenn die aktuelle Bar voll ist
                    bars.add(Bar(currentSign, currentBarNotes))
                    currentBarNotes = mutableListOf()
                    currentBarDuration = duration
                    currentBarNotes.add(Note(duration, pitch, isTied))
                }
            }
        } else {
            throw IllegalArgumentException("Unknown note duration: $durationKey")
        }
    }

    // Füge die letzte Bar hinzu, wenn es noch Noten gibt
    if (currentBarNotes.isNotEmpty()) {
        bars.add(Bar(currentSign, currentBarNotes))
    }

    return bars
}