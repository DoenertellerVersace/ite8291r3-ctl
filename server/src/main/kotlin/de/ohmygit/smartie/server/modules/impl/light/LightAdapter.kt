package de.ohmygit.smartie.server.modules.impl.light

import de.ohmygit.smartie.server.modules.Executable
import de.ohmygit.smartie.server.modules.backlight.BacklightStateAdapter
import java.io.File


private const val DEFAULT_BRIGHTNESS = 50
private const val UP = "-A"
private const val DOWN = "-U"
private const val MULTIPLY = "-T"
private const val SET = "-S"
private const val GET = "-G"
private const val SET_MIN = "-N"
private const val GET_MIN = "-P"
private const val SAVE = "-O"
private const val RESTORE = "-I"

class LightAdapter : BacklightStateAdapter, Executable(File("/usr/bin/light")) {
    private var _brightness: Int = DEFAULT_BRIGHTNESS

    init {
        applyState()
    }

    override var brightness: Int
        get() = ProcessBuilder(executable, GET).start().inputStream.bufferedReader().use { it.readText().trim().toFloat().toInt() }
        set(value) {
            _brightness = value
            applyState()
        }

    override fun restore() {
        TODO("Not yet implemented")
    }

    private fun applyState() {
        println("Setting brightness to $_brightness")
        println("Executing: $executable $SET $_brightness")
        ProcessBuilder(executable, SET, "$_brightness").start().apply {
            errorStream.bufferedReader().use { println(it.readText()) }
            inputStream.bufferedReader().use { println(it.readText()) }
        }.waitFor()
    }

    override fun toString(): String {
        return "LightAdapter(brightness=$_brightness)"
    }
}