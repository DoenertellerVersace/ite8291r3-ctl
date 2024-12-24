package de.ohmygit.smartie.server.model

data class Tree(
    val id: Int,
    val name: String,
    val type: String,
    val border: String,
    val current_border_width: Int,
    val layout: String,
    val orientation: String,
    val percent: Float?,
    val rect: Rect,
    val window_rect: Rect,
    val deco_rect: Rect,
    val geometry: Rect,
    val urgent: Boolean,
    val sticky: Boolean,
    val marks: List<String>,
    val focused: Boolean,
    val focus: List<Int>,
    val nodes: List<Tree>,
    val floating_nodes: List<Tree>,
    val representation: String?,
    val fullscreen_mode: Int,
    val floating: String,
    val scratchpad_state: String,
    val app_id: String?,
    val pid: Int?,
    val visible: Boolean?,
    val shell: String?,
    val inhibit_idle: Boolean?,
    val idle_inhibitors: IdleInhibitors?,
    val window: Int?,
    val window_properties: WindowProperties?
)

data class IdleInhibitors(
    val application: String,
    val user: String
)

data class WindowProperties(
    val title: String,
    val `class`: String,
    val instance: String,
    val window_role: String?,
    val window_type: String?,
    val transient_for: Int?
)