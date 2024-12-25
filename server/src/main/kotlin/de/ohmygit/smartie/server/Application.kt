package de.ohmygit.smartie.server

import de.ohmygit.smartie.server.modules.backlight.backlightServices
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(
        Netty,
        port = SERVER_PORT,
        host = "0.0.0.0",
        module = Application::backlightServices
    ).start(wait = true)
}
