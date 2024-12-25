package de.ohmygit.smartie.server.modules

import java.io.File

open class Executable(file: File) {
    val executable: String = file.absolutePath
}
