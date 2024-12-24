package de.ohmygit.smartie.server.modules

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import java.io.IOException

class KeyboardBacklightAdapterKtTest {

    @Test
    fun testKeyboardBacklight() {
        try {
            testPattern()
        } catch (e: IOException) {
            fail("Failed to test keyboard backlight", e)
        }
    }
}