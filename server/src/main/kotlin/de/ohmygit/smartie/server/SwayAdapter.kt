package de.ohmygit.smartie.server

import SwayIPC
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.ohmygit.smartie.server.model.*

private val resultsType = object : TypeToken<List<CommandResult>>() {}.type
private val workspacesType = object : TypeToken<List<Workspace>>() {}.type
private val outputsType = object : TypeToken<List<Output>>() {}.type
private val stringsType = object : TypeToken<List<String>>() {}.type

object Sway : AutoCloseable {

    private var connected = false
    private val ipc: SwayIPC = SwayIPC()
        get() = field.apply {
            if (!connected) {
                connect(SwayIPC.getSwaysock())
                connected = true
            }
        }

    fun sendCommand(command: String): List<CommandResult> {
        return Gson().fromJson(ipc.sendCommand(command), resultsType)
    }

    val workspaces: List<Workspace>
        get() = Gson().fromJson(ipc.getWorkspaces(), workspacesType)

    val focusedWorkspace: Workspace
        get() = workspaces.first { it.focused }

    val activeWorkspaces: List<Workspace>
        get() = workspaces.filter { it.visible }

    val outputs: List<Output>
        get() = Gson().fromJson(ipc.getOutputs(), outputsType)

    val marks: List<String>
        get() = Gson().fromJson(ipc.getMarks(), stringsType)

//    fun barConfig(barId: String): BarConfig {
//        return Gson().fromJson(ipc.getBarConfig(barId), BarConfig::class.java)
//    }

//    val barIds: List<String>
//        get() = Gson().fromJson(ipc.getBarIds(), stringsType)
//
    val version: Version
        get() = Gson().fromJson(ipc.getVersion(), Version::class.java)

    val tree: Tree
        get() = Gson().fromJson(ipc.getTree(), Tree::class.java)

    fun subscribe(events: Array<String>): Boolean = ipc.lolLol(events)

    override fun close() {
        ipc.closeConnection()
        connected = false
    }
}

//fun main() {
//    Sway.use {
//        println(Sway.workspaces)
//        println(Sway.focusedWorkspace)
//        println(Sway.activeWorkspaces)
//        println(Sway.outputs)
//        println(Sway.marks)
////        println(sway.barConfig("bar-0"))
////        println(sway.barIds)
//        println(Sway.version)
////        println(sway.tree)
//        println(Sway.subscribe(arrayOf("workspace")))
//    }
//}