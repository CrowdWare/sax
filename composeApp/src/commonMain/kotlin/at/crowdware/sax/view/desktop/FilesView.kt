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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import at.crowdware.sax.model.NodeType
import at.crowdware.sax.model.TreeNode
import at.crowdware.sax.ui.TreeView

fun addChild(parent: TreeNode, child: TreeNode) {
    parent.children.add(child) // Modify the children directly
}

@Composable
fun filesView() {
    var totalHeight by remember { mutableStateOf(0f) }
    var expanded by remember { mutableStateOf(false) }
    var treeNode by remember { mutableStateOf(TreeNode(mutableStateOf(""), NodeType.OTHER)) }
    var treeNodeOffset by remember { mutableStateOf(Offset.Zero) }
    var pointerOffset by remember { mutableStateOf(Offset.Zero) }
    var treeViewHeight by remember { mutableStateOf(0.5f) }
    var treeViewSize by remember { mutableStateOf(IntSize.Zero) }
    var treeData by mutableStateOf<List<TreeNode>>(emptyList())


    val bluesChildren = SnapshotStateList<TreeNode>()
    val jazzChildren = SnapshotStateList<TreeNode>()
    val bluesNode = TreeNode(
        title = mutableStateOf( "Blues"),
        path = "",
        type = NodeType.DIRECTORY,
        children = bluesChildren
    )

    val jazzNode = TreeNode(
        title = mutableStateOf( "Jazz"),
        path = "",
        type = NodeType.DIRECTORY,
        children = jazzChildren
    )
    treeData = treeData + bluesNode + jazzNode

    val chicagoBluesNode = TreeNode(
        title = mutableStateOf("Chicago Blues"),
        path = "",
        type = NodeType.SML
    )

    val deltaBluesNode = TreeNode(
        title = mutableStateOf("Delta Blues"),
        path = "",
        type = NodeType.SML
    )

    addChild(bluesNode, chicagoBluesNode)
    addChild(bluesNode, deltaBluesNode)

    Column(
        modifier = Modifier.width(200.dp).fillMaxHeight().background(color = MaterialTheme.colors.primary).padding(4.dp)
            .onGloballyPositioned { coordinates ->
                totalHeight = coordinates.size.height.toFloat()
            }) {
        BasicText(
            text = "Project Structure",
            modifier = Modifier.padding(8.dp),
            maxLines = 1,
            style = TextStyle(color = MaterialTheme.colors.onPrimary),
            overflow = TextOverflow.Ellipsis
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(treeViewHeight)
                .background(MaterialTheme.colors.surface)
                .onGloballyPositioned { layoutCoordinates ->
                    treeViewSize = layoutCoordinates.size
                }
        ) {

            TreeView(
                tree = treeData,
                iconProvider = { node -> fileTreeIconProvider(node) },
                onNodeDoubleClick = {},
                onNodeRightClick = { node, offset, pOffset ->
                    if(node.title.value.startsWith("pages")) {
                        //currentProject.pageNode = node
                    }
                    if(node.title.value.startsWith("parts")) {
                        //currentProject.partsNode = node
                    }
                    expanded = true
                    treeNode = (node as? TreeNode)!!
                    treeNodeOffset = offset
                    pointerOffset = pOffset
                },
                onClick = {node ->
                    val pNode = node as? TreeNode
                    if (pNode != null) {
                        //if (pNode.type == NodeType.SML || pNode.type == NodeType.MD)
                            //currentProject.LoadFile(pNode.path)
                    }
                }
            )
            if (expanded) {
                val density = LocalDensity.current
                val dpOffset = with(density) {
                    DpOffset(
                        (treeNodeOffset.x + pointerOffset.x - 40).toDp(),
                        (treeNodeOffset.y - treeViewSize.height - 60).toDp()
                    )
                }
                DropdownMenu(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colors.primary,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            border = BorderStroke(1.dp, color = MaterialTheme.colors.primary),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    offset = dpOffset,
                    properties = PopupProperties(focusable = true)
                ) {
                    if (treeNode.type != NodeType.DIRECTORY) {
                        if (treeNode.title.value != "home.sml" && treeNode.title.value != "app.sml" && treeNode.title.value != "home.md") {
                            DropdownMenuItem(onClick = {
                                expanded = false
                                //currentProject.currentTreeNode = treeNode
                                //currentProject.isRenameFileDialogVisible = true
                            }) {
                                Text(text = "Rename", fontSize = 12.sp)
                            }


                            DropdownMenuItem(
                                onClick = {
                                    expanded = false
                                    //currentProject.currentTreeNode = treeNode
                                    //currentProject.deleteItem(treeNode)
                                }
                            ) {
                                Text(text = "Delete", fontSize = 12.sp)
                            }
                        }
                    } else if (treeNode.title.value.startsWith("pages")) {
                        DropdownMenuItem(onClick = {
                            expanded = false
                            //currentProject.currentTreeNode = treeNode
                            //currentProject.isPageDialogVisible = true
                        }) {
                            Text(text = "New", fontSize = 12.sp)
                        }
                    } else if (treeNode.title.value.startsWith("parts")) {
                        DropdownMenuItem(onClick = {
                            expanded = false
                            //currentProject.currentTreeNode = treeNode
                            //currentProject.isPartDialogVisible = true
                        }) {
                            Text(text = "New", fontSize = 12.sp)
                        }
                    } else if (treeNode.title.value.startsWith("images")) {
                        DropdownMenuItem(onClick = {
                            expanded = false
                            //currentProject.currentTreeNode = treeNode
                            //currentProject.isImportImageDialogVisible = true
                        }) {
                            Text(text = "Import", fontSize = 12.sp)
                        }
                    } else if (treeNode.title.value.startsWith("videos")) {
                        DropdownMenuItem(onClick = {
                            expanded = false
                            //currentProject.currentTreeNode = treeNode
                            //currentProject.isImportVideoDialogVisible = true
                        }) {
                            Text(text = "Import", fontSize = 12.sp)
                        }
                    } else if (treeNode.title.value.startsWith("sounds")) {
                        DropdownMenuItem(onClick = {
                            expanded = false
                            //currentProject.currentTreeNode = treeNode
                            //currentProject.isImportSoundDialogVisible = true
                        }) {
                            Text(text = "Import", fontSize = 12.sp)
                        }
                    } else if (treeNode.title.value.startsWith("models")) {
                        DropdownMenuItem(onClick = {
                            expanded = false
                            //currentProject.currentTreeNode = treeNode
                            //currentProject.isImportModelDialogVisible = true
                        }) {
                            Text(text = "Import", fontSize = 12.sp)
                        }
                    } else if (treeNode.title.value.startsWith("textures")) {
                        DropdownMenuItem(onClick = {
                            expanded = false
                            //currentProject.currentTreeNode = treeNode
                            //currentProject.isImportTextureDialogVisible = true
                        }) {
                            Text(text = "Import", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

