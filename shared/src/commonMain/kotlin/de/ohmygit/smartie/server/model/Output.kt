package de.ohmygit.smartie.server.model

data class Output(
    val name: String,
    val make: String,
    val model: String,
    val serial: String,
    val active: Boolean,
    val power: Boolean,
    val primary: Boolean,
    val scale: Float,
    val subpixel_hinting: String,
    val transform: String,
    val current_workspace: String?,
    val modes: List<Mode>,
    val current_mode: Mode,
    val rect: Rect
)

data class Mode(
    val width: Int,
    val height: Int,
    val refresh: Int
)