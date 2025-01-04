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
    Column(modifier = Modifier.fillMaxSize()) {
        Row (modifier = Modifier.weight(1F)){
            toolbar()
            filesView()
            playScreen()
        }
        Row (modifier = Modifier.background(color=MaterialTheme.colorScheme.primary).fillMaxWidth()){
            notesDisplay()
            keyboard()
        }
    }
}