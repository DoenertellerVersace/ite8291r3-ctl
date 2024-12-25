package de.ohmygit.smartie.server.modules.backlight.keyboard

import de.ohmygit.smartie.server.modules.impl.ite.IteBacklightAdapter
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


interface RgbLightStateAdapter {
    var on: Boolean
    var brightness: Int
    var rgb: Triple<Int, Int, Int>
    fun testPattern()
    fun restore()
    override fun toString(): String
}

fun Application.keyboardBacklightControl() {
    val adapter = IteBacklightAdapter()
    routing {
        get("/led/kbd/state") {
            call.respondText(if (adapter.on) "on" else "off")
        }
        get("/led/kbd/color") {
            call.respondText("${adapter.rgb.first},${adapter.rgb.second},${adapter.rgb.third}")
        }
        get("/led/kbd/brightness") {
            call.respondText("${adapter.brightness}")
        }
        post("/led/kbd/state/on") {
            adapter.on = call.parameters["on"]?.toBoolean() ?: adapter.on
            call.respondText("$adapter")
        }
        post("/led/kbd/color/{red}/{green}/{blue}") {
            adapter.rgb = Triple(
                call.parameters["red"]?.toInt() ?: adapter.rgb.first,
                call.parameters["green"]?.toInt() ?: adapter.rgb.second,
                call.parameters["blue"]?.toInt() ?: adapter.rgb.third
            )
            call.respondText("$adapter")
        }
        post("/led/kbd/brightness/{brightness}") {

            call.respondText("$adapter")
        }
    }
}

