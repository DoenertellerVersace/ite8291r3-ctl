package de.ohmygit.smartie.server.modules.impl.ite

import de.ohmygit.smartie.server.modules.Python
import de.ohmygit.smartie.server.modules.backlight.keyboard.RgbLightStateAdapter

private const val DEFAULT_STATE = true
private const val DEFAULT_BRIGHTNESS = 10
private const val DEFAULT_RED = 20
private const val DEFAULT_GREEN = 0
private const val DEFAULT_BLUE = 80

class IteBacklightAdapter : RgbLightStateAdapter {
    private val python = Python()
    private var _on = DEFAULT_STATE
    private var _brightness = DEFAULT_BRIGHTNESS
    private var _rgb = Triple(DEFAULT_RED, DEFAULT_GREEN, DEFAULT_BLUE)

    init {
        applyState()
    }

    private fun applyState() {
        python.iteRun(
            "monocolor",
            "--brightness",
            if (_on) "$_brightness" else "0",
            "--rgb",
            "${_rgb.first},${_rgb.second},${_rgb.third}"
        )

    }

    override var on: Boolean
        get() = _on
        set(value) {
            if (value == _on) return
            _on = value
            applyState()
        }

    override var brightness: Int
        get() = _brightness
        set(value) {
            if (value == _brightness) return
            _brightness = value
            applyState()
        }
    override var rgb: Triple<Int, Int, Int>
        get() = _rgb
        set(value) {
            if (!_on) return
            if (value == _rgb) return
            applyState()
        }

    override fun testPattern() {
        python.module("ite8291r3_ctl") {
            exec("test-pattern")
        }
    }

    override fun restore() {
        applyState()
    }

    override fun toString(): String = "State(brightness=$brightness, rgb=$rgb, on=$on)"
}