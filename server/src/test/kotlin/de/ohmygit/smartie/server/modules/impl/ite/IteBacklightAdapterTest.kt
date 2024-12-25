package de.ohmygit.smartie.server.modules.impl.ite

import org.junit.jupiter.api.Test
import java.lang.Thread.sleep

class IteBacklightAdapterTest {
    @Test
    fun testKeyboardBacklight() {
        val adapter = IteBacklightAdapter()
        sleep(200)
        try {
            adapter.testPattern()
            sleep(12000)
        } catch (e: Exception) {
            org.junit.jupiter.api.fail("Failed to test keyboard backlight", e)
        } finally {
            adapter.restore()
        }
    }
}