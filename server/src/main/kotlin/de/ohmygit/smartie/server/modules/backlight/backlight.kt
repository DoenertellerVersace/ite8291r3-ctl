package de.ohmygit.smartie.server.modules.backlight

import io.ktor.server.application.*


/**
 * Interface representing an RGB light state adapter.
 * Extends the LightStateAdapter interface to include RGB color control and a test pattern function.
 */
interface RgbLightStateAdapter : BacklightStateAdapter {
    /**
     * Indicates whether the light is on or off.
     */
    var on: Boolean

    /**
     * The RGB color of the light.
     * Each component (red, green, blue) should be between 0 and 255.
     */
    var rgb: Triple<Int, Int, Int>

    /**
     * Runs a test pattern on the light.
     */
    fun testPattern()
}

/**
 * Interface representing a light state adapter.
 */
interface BacklightStateAdapter {

    /**
     * The brightness level of the light.
     * Value should be between 0 and 100.
     */
    var brightness: Int

    /**
     * Restores the light to its default state.
     */
    fun restore()

    /**
     * Returns a string representation of the light state.
     * @return A string describing the current state of the light.
     */
    override fun toString(): String
}

fun Application.backlightServices() {
    screenBacklightControl()
    keyboardBacklightControl()
}