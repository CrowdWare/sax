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
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddBusiness
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import at.crowdware.sax.ui.HoverableIcon
import compose.icons.FeatherIcons
import compose.icons.FontAwesomeIcons
import compose.icons.feathericons.*
import compose.icons.fontawesomeicons.Brands
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.brands.Github
import compose.icons.fontawesomeicons.solid.FolderOpen
import compose.icons.fontawesomeicons.solid.Palette
import compose.icons.fontawesomeicons.solid.PlusCircle
import androidx.compose.ui.res.painterResource
import at.crowdware.sax.theme.LocalThemeIsDark
import org.jetbrains.compose.resources.painterResource
import sax.composeapp.generated.resources.Res
import sax.composeapp.generated.resources.ic_dark_mode
import sax.composeapp.generated.resources.ic_light_mode

@Composable
fun toolbar() {
    var isDark by LocalThemeIsDark.current
    Column(
        modifier = Modifier.width(52.dp).fillMaxHeight()/*.background(color = MaterialTheme.colors.primary)*/,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicText(
            text = "Build",
            modifier = Modifier.padding(8.dp),
            maxLines = 1,
            //style = TextStyle(color = MaterialTheme.colors.onPrimary),
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(8.dp))
        HoverableIcon(
            onClick = { },
            painter = painterResource("drawable/create_folder.xml"),
            tooltipText = "Create new Song",
            isSelected = false
        )
        Spacer(modifier = Modifier.height(8.dp))
        val icon = remember(isDark) {
            if (isDark) Res.drawable.ic_light_mode
            else Res.drawable.ic_dark_mode
        }
        HoverableIcon(
            onClick = { isDark = !isDark},
            painter = painterResource(icon),
            tooltipText = "Toogle Theme",
            isSelected = false
        )
        /*
        HoverableIcon(
            onClick = { currentProject?.isOpenProjectDialogVisible = true },
            painter = painterResource("drawable/open_file.xml"),
            tooltipText = "Open Project",
            isSelected = currentProject?.isOpenProjectDialogVisible == true
        )
        Spacer(modifier = Modifier.height(8.dp))
        HoverableIcon(
            onClick = { currentProject?.isProjectStructureVisible = true },
            painter = painterResource("drawable/tree.xml"),
            tooltipText = "Project Structure",
            isSelected = currentProject?.isProjectStructureVisible == true
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (currentProject != null) {
            if (currentProject.book != null) {
                Spacer(modifier = Modifier.height(8.dp))
                HoverableIcon(
                    onClick = { currentProject.isCreateEbookVisible = true },
                    painter = painterResource("drawable/book.xml"),
                    tooltipText = "Create Ebook",
                    isSelected = currentProject.isCreateEbookVisible == true
                )
            }
        }

        if (currentProject != null) {
            if (currentProject.app != null) {
                Spacer(modifier = Modifier.height(8.dp))
                HoverableIcon(
                    onClick = { currentProject.isCreateHTMLVisible = true },
                    painter = painterResource("drawable/html.xml"),
                    tooltipText = "Create HTML",
                    isSelected = currentProject.isCreateHTMLVisible == true
                )
            }
        }
        if (currentProject != null) {
            Spacer(modifier = Modifier.height(8.dp))
            HoverableIcon(
                onClick = { currentProject.isSettingsVisible = true },
                painter = painterResource("drawable/settings.xml"),
                tooltipText = "Settings",
                isSelected = currentProject.isSettingsVisible == true
            )
        }*/
    }
}