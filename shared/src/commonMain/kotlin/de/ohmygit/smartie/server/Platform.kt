package de.ohmygit.smartie.server

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform