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

package at.crowdware.sax

import androidx.compose.runtime.Composable
import at.crowdware.sax.theme.AppTheme
import at.crowdware.sax.utils.parseBars
import at.crowdware.sax.utils.parseNotes
import at.crowdware.sax.view.desktop.desktop

@Composable
internal fun App() {
    AppTheme {
        /*val input = "EQ4,EC#5-ED5,EDb5,EC5,QC5,HP,hP,WH,EC3,qB4-QB4,HC4"
        val bars = parseBars(input)
        bars.forEach { bar ->
            println("Bar: ${bar.sign}")
            bar.notes.forEach { note ->
                println("  Note: ${note.pitch}, Duration: ${note.duration}, Tied: ${note.isTied}")
            }
        }*/
        desktop()
    }
}
