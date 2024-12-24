package de.ohmygit.smartie.server

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "ite8291GUI",
    ) {
        App()
    }
}