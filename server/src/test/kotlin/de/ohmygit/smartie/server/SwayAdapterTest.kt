package de.ohmygit.smartie.server

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.Test
import java.lang.Thread.sleep

class SwayAdapterTest {

    @Test
    fun sendCommand() {
        assertDoesNotThrow {
            Sway.sendCommand("floating toggle")
            Sway.sendCommand("floating toggle")
        }
    }

    @Test
    fun getWorkspaces() {
        assertTrue(Sway.workspaces.isNotEmpty())

    }

    @Test
    fun getFocusedWorkspace() {
        assertTrue(arrayOf("1", "2", "3", "4", "5").contains(Sway.focusedWorkspace.name))
    }

    @Test
    fun changeWorkspace() {
        // arrange
        assumeTrue(Sway.workspaces.size > 1, "Not enough workspaces to test")
        val workspace = Sway.workspaces.first { !it.focused }.name
        val currentWorkspace = Sway.focusedWorkspace.name

        // act
        Sway.sendCommand("workspace $workspace")
        println("workspace $workspace")
        // assert
        assertEquals(Sway.focusedWorkspace.name, workspace)
        sleep(1000)

        println("workspace $currentWorkspace")
        Sway.sendCommand("workspace $currentWorkspace")
    }

    @Test
    fun getActiveWorkspaces() {
        assertEquals(Sway.activeWorkspaces.size, 1)
    }

    @Test
    fun getOutputs() {
        assertTrue(Sway.outputs.isNotEmpty())
    }

    @Test
    fun getMarks() {
        assertTrue(Sway.marks.isEmpty())
    }

//    @Test
//    fun getBarConfig() {
//        val barId = sway.barIds
//        assertTrue(sway.barConfig(barId).mode.isNotEmpty())
//    }

//    @Test
//    fun subscribe() {
//        assertTrue(sway.subscribe(arrayOf("workspace")))
//    }

    @Test
    fun getBarIds() {
    }

    @Test
    fun getVersion() {
    }

    @Test
    fun getTree() {
    }
}