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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import at.crowdware.sax.model.NodeType
import at.crowdware.sax.model.TreeNode
import at.crowdware.sax.theme.LocalThemeIsDark
import at.crowdware.sax.utils.Song

@Composable
fun fileTreeIconProvider(node: TreeNode) {
    when (node.type) { // Assuming you have a `type` field in TreeNode to determine the type
        NodeType.DIRECTORY -> Icon(Icons.Default.Folder, modifier = Modifier.size(16.dp), contentDescription = null, tint = MaterialTheme.colorScheme.onSurface)
        NodeType.IMAGE -> Icon(Icons.Default.Image, modifier = Modifier.size(16.dp), contentDescription = null, tint =  MaterialTheme.colorScheme.onSurface)
        NodeType.VIDEO -> Icon(Icons.Default.Movie, modifier = Modifier.size(16.dp), contentDescription = null, tint =  MaterialTheme.colorScheme.onSurface)
        NodeType.SOUND -> Icon(Icons.Default.MusicNote, modifier = Modifier.size(16.dp), contentDescription = null, tint =  MaterialTheme.colorScheme.onSurface)
        NodeType.XML -> Icon(Icons.Default.InsertDriveFile, modifier = Modifier.size(16.dp), contentDescription = null, tint =  MaterialTheme.colorScheme.onSurface)
        else -> Icon(Icons.Default.InsertDriveFile, modifier = Modifier.size(16.dp), contentDescription = null, tint = MaterialTheme.colorScheme.onSurface) // Default file icon
    }
}


@Composable
fun desktop() {
    val filePath = "notes.txt"
    val notes = readNotesFromFile(filePath)
    val bars = createBarsFromNotes(notes)
    val songState = remember { mutableStateOf(Song(name = "Loaded from File", bars = bars)) }
    Column(modifier = Modifier.fillMaxSize()) {
        Row (modifier = Modifier.weight(1F)){
            toolbar()
            filesView()
            playScreen()
        }
        Row (modifier = Modifier.background(color=MaterialTheme.colorScheme.primary).fillMaxWidth()){
            notesDisplay(songState.value)
            keyboard { updatedSong ->
                songState.value = updatedSong
            }
        }
    }
}