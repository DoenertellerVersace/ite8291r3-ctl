package de.ohmygit.smartie.server.modules

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.lang.Thread.sleep


fun Application.kbdBacklightControl() {
    testPattern()
    routing {
        get("/state") {
            call.respondText("$State")
        }
        post<Any>("/state") {
            setBrightnessState()
            setRgbState()
            Runtime.getRuntime().exec(
                State.apply()
            )
            call.respondText("$State")
        }
    }
}

private const val DEFAULT_BRIGHTNESS = 10
private const val DEFAULT_RED = 20
private const val DEFAULT_GREEN = 0
private const val DEFAULT_BLUE = 80

fun testPattern() {
    Runtime.getRuntime().exec(
        State.apply()
    )
    sleep(200)
    State.r = 100
    Runtime.getRuntime().exec(
        State.apply()
    )
    sleep(200)
    State.r = DEFAULT_RED
    Runtime.getRuntime().exec(
        State.apply()
    )
    sleep(200)
    State.g = 50
    Runtime.getRuntime().exec(
        State.apply()
    )
    sleep(200)
    State.g = DEFAULT_GREEN
    Runtime.getRuntime().exec(
        State.apply()
    )
    sleep(200)
    State.b = 255
    Runtime.getRuntime().exec(
        State.apply()
    )
    sleep(200)
    State.b = DEFAULT_BLUE
    Runtime.getRuntime().exec(
        State.apply()
    )
}

object State {
    var brightness = DEFAULT_BRIGHTNESS
    var r = DEFAULT_RED
    var g = DEFAULT_GREEN
    var b = DEFAULT_BLUE
    override fun toString(): String = "State(brightness=$brightness, r=$r, g=$g, b=$b)"
}

private fun RoutingContext.setBrightnessState() {
    val brightness = call.pathParameters["brightness"]!!
    State.brightness = brightness.toInt()
}

private fun RoutingContext.setRgbState() {
    val r = call.pathParameters["r"]!!
    val g = call.pathParameters["g"]!!
    val b = call.pathParameters["b"]!!
    State.r = r.toInt()
    State.g = g.toInt()
    State.b = b.toInt()
}

fun State.apply(): Array<String> = arrayOf(
    "/home/jakob/dev/ite8291r3-ctl/venv/bin/ite8291r3-ctl",
    "monocolor",
    "--brightness",
    "$brightness",
    "--rgb",
    "$r,$g,$b"
)
