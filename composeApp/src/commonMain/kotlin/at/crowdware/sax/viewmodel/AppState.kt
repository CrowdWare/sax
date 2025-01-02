package at.crowdware.sax.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.serialization.Serializable

@Serializable
data class State(
    val windowHeight: Int,
    val windowWidth: Int,
    val windowX: Int,
    val windowY: Int,
    var isDark: Boolean,
)

class AppState {
    var windowWidth by mutableStateOf(0)
    var windowHeight by mutableStateOf(0)
    var windowX by mutableStateOf(0)
    var windowY by mutableStateOf(0)
    var isDark by mutableStateOf(true)
}

fun createAppState(): AppState {
    return AppState()
}

object GlobalAppState {
    var appState: AppState? = null
}