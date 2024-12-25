package de.ohmygit.smartie.server.modules.backlight

import de.ohmygit.smartie.server.modules.impl.ite.IteBacklightAdapter
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

private val KeyboardStateAdapter : RgbLightStateAdapter = IteBacklightAdapter()

fun Application.keyboardBacklightControl() {
    routing {
        get("/led/kbd/state") {
            call.respondText(if (KeyboardStateAdapter.on) "on" else "off")
        }
        get("/led/kbd/color") {
            call.respondText("${KeyboardStateAdapter.rgb.first},${KeyboardStateAdapter.rgb.second},${KeyboardStateAdapter.rgb.third}")
        }
        get("/led/kbd/brightness") {
            call.respondText("${KeyboardStateAdapter.brightness}")
        }
        post("/led/kbd/state/on") {
            KeyboardStateAdapter.on = call.parameters["on"]?.toBoolean() ?: KeyboardStateAdapter.on
            call.respondText("$KeyboardStateAdapter")
        }
        post("/led/kbd/color/{red}/{green}/{blue}") {

            val red = call.parameters["red"]?.toInt() ?: KeyboardStateAdapter.rgb.first
            val green = call.parameters["green"]?.toInt() ?: KeyboardStateAdapter.rgb.second
            val blue = call.parameters["blue"]?.toInt() ?: KeyboardStateAdapter.rgb.third
            assert(red in 0..255 && green in 0..255 && blue in 0..255) {
                "RGB values must be between 0 and 255"
            }
            KeyboardStateAdapter.rgb = Triple(red, green, blue)
            call.respondText("$KeyboardStateAdapter")
        }
        post("/led/kbd/brightness/{brightness}") {
            val brightness = call.parameters["brightness"]?.toInt() ?: KeyboardStateAdapter.brightness
            assert(brightness in 0..100) { "Brightness value must be between 0 and 100" }
            KeyboardStateAdapter.brightness = brightness
            call.respondText("$KeyboardStateAdapter")
        }
    }
}
