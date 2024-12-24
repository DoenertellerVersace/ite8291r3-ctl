package de.ohmygit.smartie.ui.drvctl.ite8291

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}