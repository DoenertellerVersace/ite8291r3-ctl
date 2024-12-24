package de.ohmygit.smartie.server.model

data class Workspace(
    val num: Int,
    val name: String,
    val visible: Boolean,
    val focused: Boolean,
    val urgent: Boolean,
    val rect: Rect?,
    val output: String
)