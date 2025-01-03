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

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.awt.Dimension
import at.crowdware.sax.App
import at.crowdware.sax.Version
import at.crowdware.sax.theme.LocalThemeIsDark
import at.crowdware.sax.ui.aboutDialog
import at.crowdware.sax.viewmodel.GlobalAppState
import at.crowdware.sax.viewmodel.createAppState
import java.awt.Desktop
import java.awt.Frame
import java.awt.Window
import java.io.File
import java.io.IOException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import at.crowdware.sax.viewmodel.State

fun main() = application {
    val appName = "Playful Sax"
    val version = Version.version
    var isAboutDialogOpen by  mutableStateOf(false)
    var isAskingToClose by remember { mutableStateOf(false) }
    val appState = createAppState()
    LocalThemeIsDark.current
    GlobalAppState.appState = appState
    loadAppState()

    val windowState = rememberWindowState(
        width = (appState.windowWidth).dp,
        height = (appState.windowHeight).dp
    )
    if (Desktop.isDesktopSupported()) {
        val desktop = Desktop.getDesktop()

        // Set custom "About" handler
        if (desktop.isSupported(Desktop.Action.APP_ABOUT)) {
            desktop.setAboutHandler {
                isAboutDialogOpen = true
            }
        }

        // Set custom Quit handler if needed (optional)
        if (desktop.isSupported(Desktop.Action.APP_QUIT_HANDLER)) {
            desktop.setQuitHandler { _, quitResponse ->
                var frame: Window = Frame.getWindows()[0]
                onAppClose(frame as ComposeWindow)
                quitResponse.performQuit()
            }
        }
    }

    Window(
        onCloseRequest = { isAskingToClose = true },
        title = appName,
        resizable = true,
        state = windowState,
        icon = painterResource("desktopAppIcons/WindowsIcon.ico")
    ){
        window.minimumSize = Dimension(350, 600)
        if (isAboutDialogOpen) {
            aboutDialog(
                appName = appName,
                version = version,
                onDismissRequest = { isAboutDialogOpen = false }
            )
        }
        if (isAskingToClose) {
            onAppClose(window)
            exitApplication()
        }
        App()
    }
}

fun onAppClose(frame: ComposeWindow) {
    saveState(frame)
}

fun saveState(frame: ComposeWindow) {
    // Save the app state when the window is closed
    val appState = GlobalAppState.appState
    if (appState != null) {
        saveAppState(
            State(
                windowHeight = frame.height,
                windowWidth = frame.width,
                windowX = frame.x,
                windowY = frame.y,
                isDark = appState.isDark,
            )
        )
    }
}

fun saveAppState(state: State) {
    val userHome = System.getProperty("user.home")
    val configDirectory = if (System.getProperty("os.name").contains("Windows")) {
        File("$userHome/AppData/Local/Sax")
    } else {
        File("$userHome/Library/Application Support/Sax")
    }

    // Create the directory if it doesn't exist
    if (!configDirectory.exists()) {
        configDirectory.mkdirs()
    }

    val configFile = File(configDirectory, "app_state.json")
    try {
        val jsonState = Json.encodeToString(state)
        configFile.writeText(jsonState)
    } catch (e: IOException) {
        println("Error writing app state: ${e.message}")
        e.printStackTrace()
    }
}

fun loadAppState() {
    val appState = GlobalAppState.appState
    val userHome = System.getProperty("user.home")
    val configDirectory = if (System.getProperty("os.name").contains("Windows")) {
        File("$userHome/AppData/Local/Sax")
    } else {
        File("$userHome/Library/Application Support/Sax")
    }
    val configFile = File(configDirectory, "app_state.json")

    if(!configDirectory.exists()) {
        configDirectory.mkdirs()
    }
    try {
        val jsonState = configFile.readText()
        val state = Json.decodeFromString<State>(jsonState)
        if (appState != null) {
            appState.isDark = state.isDark
            appState.windowX = state.windowX
            appState.windowY = state.windowY
            appState.windowWidth = state.windowWidth
            appState.windowHeight = state.windowHeight
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}