package de.ohmygit.smartie.ui.drvctl.ite8291

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform