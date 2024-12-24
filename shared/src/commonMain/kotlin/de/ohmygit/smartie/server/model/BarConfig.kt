package de.ohmygit.smartie.server.model

data class BarConfig(
    val id: String,
    val mode: String,
    val position: String,
    val status_command: String,
    val font: String,
    val workspace_buttons: Boolean,
    val workspace_min_width: Int,
    val binding_mode_indicator: Boolean,
    val verbose: Boolean,
    val colors: Colors,
    val gaps: Gaps,
    val bar_height: Int,
    val status_padding: Int,
    val status_edge_padding: Int
)

data class Colors(
    val background: String,
    val statusline: String,
    val separator: String,
    val focused_background: String,
    val focused_statusline: String,
    val focused_separator: String,
    val focused_workspace_text: String,
    val focused_workspace_bg: String,
    val focused_workspace_border: String,
    val active_workspace_text: String,
    val active_workspace_bg: String,
    val active_workspace_border: String,
    val inactive_workspace_text: String,
    val inactive_workspace_bg: String,
    val inactive_workspace_border: String,
    val urgent_workspace_text: String,
    val urgent_workspace_bg: String,
    val urgent_workspace_border: String,
    val binding_mode_text: String,
    val binding_mode_bg: String,
    val binding_mode_border: String
)

data class Gaps(
    val top: Int,
    val right: Int,
    val bottom: Int,
    val left: Int
)

data class Version(
    val major: Int,
    val minor: Int,
    val patch: Int,
    val human_readable: String,
    val loaded_config_file_name: String
)