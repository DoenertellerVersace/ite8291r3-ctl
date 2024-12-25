package de.ohmygit.smartie.server.modules.backlight

import de.ohmygit.smartie.server.modules.impl.light.LightAdapter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.lang.Thread.sleep

class ScreenKtTest {
    @Test
    fun testScreen() {
        val screen = LightAdapter()
        assertEquals(50, screen.brightness)
        sleep(300)
        screen.brightness = 5
        assertEquals(5, screen.brightness)
        sleep(300)
        screen.brightness = 100
        assertEquals(100, screen.brightness)
    }
}