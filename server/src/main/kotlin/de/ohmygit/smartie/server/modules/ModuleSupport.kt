package de.ohmygit.smartie.server.modules

import java.io.File


class VenvNotFoundException(s: String) : IllegalArgumentException(s)

class Python(venv: File = File("/home/jakob/dev/smartie/venv")) {
    private val python3 = "${venv.absolutePath}/bin/python3"
    private val pip3 = "${venv.absolutePath}/bin/pip3"

    init {
        venv.exists() && venv.isDirectory() || throw VenvNotFoundException("Venv not found at ${venv.absolutePath}")
        Runtime.getRuntime().exec(arrayOf(python3, "--version")).waitFor()
        println("Python 3 found at $python3")
    }

    fun install(vararg packages: String): Process = ProcessBuilder(pip3, "install", *packages).start()
        .apply {
            inputStream.bufferedReader().readLines().forEach {
                println(it)
            }
            errorStream.bufferedReader().readLines().forEach {
                println(it)
            }
        }


    private fun moduleRun(module: String, vararg args: String): Process {
        println("Running $python3 -m $module ${args.joinToString(" ")}")
        return ProcessBuilder(python3, "-m", module, *args).start()
    }

    fun module(name: String, runConfig: PythonModuleScope.() -> Unit) {
        return PythonModuleScope(name).runConfig()
    }

    inner class PythonModuleScope(private val module: String) {
        fun exec(vararg args: String): Process = moduleRun(module = this.module, args = args)
        fun execWait(vararg args: String): Process = moduleRun(module = this.module, args = args).apply {
            inputStream.bufferedReader().readLines().forEach {
                println(it)
            }
            errorStream.bufferedReader().readLines().forEach {
                println(it)
            }
        }
    }
}

open class PythonModule(private val name: String) {
    val python = Python().apply {
        println("Installing $name...")
        install(name)
    }

    fun module(
        runConfig: Python.PythonModuleScope.() -> Unit,
    ) = python.module(name, runConfig)
}
