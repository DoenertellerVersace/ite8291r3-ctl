package de.ohmygit.smartie.server.modules.backlight

import de.ohmygit.smartie.server.modules.impl.light.LightAdapter
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


private val ScreenStateAdapter : BacklightStateAdapter = LightAdapter()

fun Application.screenBacklightControl() {
    routing {
        get("/led/screen/brightness") {
            call.respondText("${ScreenStateAdapter.brightness}")
        }
        post("/led/kbd/brightness/{brightness}") {
            assert(call.parameters["brightness"]?.toInt() in 0..100) { "Brightness value must be between 0 and 100" }
            ScreenStateAdapter.brightness = call.parameters["brightness"]?.toInt() ?: ScreenStateAdapter.brightness
            call.respondText("$ScreenStateAdapter")
        }
    }
}