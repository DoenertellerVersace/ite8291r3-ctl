package de.ohmygit.smartie.server.modules

import de.ohmygit.smartie.server.modules.impl.ite.IteBacklightAdapter
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import java.lang.Thread.sleep

class KeyboardBacklightAdapterAdapterKtTest {

    @Test
    fun testKeyboardBacklight() {
        val adapter = IteBacklightAdapter()
        try {
            adapter.testPattern()
            sleep(12000)
        } catch (e: Exception) {
            fail("Failed to test keyboard backlight", e)
        } finally {
            adapter.restore()
        }
    }
}