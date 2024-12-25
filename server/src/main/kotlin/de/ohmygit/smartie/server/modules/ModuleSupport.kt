package de.ohmygit.smartie.server.modules

import java.io.File


class VenvNotFoundException(s: String) : IllegalArgumentException(s)

class Python(venv: File = File("/home/jakob/dev/smartie/venv")) {
    private val python3 = "${venv.absolutePath}/bin/python"
    private val pip3 = "${venv.absolutePath}/bin/pip"
    private val ite = "${venv.absolutePath}/bin/ite8291r3-ctl"

    init {
        venv.exists() && venv.isDirectory() || throw VenvNotFoundException("Venv not found at ${venv.absolutePath}")
        Runtime.getRuntime().exec(arrayOf(python3, "--version"))
        println("Python 3 found at $python3")
    }

    fun install(vararg packages: String) {
        Runtime.getRuntime().exec(arrayOf(pip3, "install") + packages)
    }

    fun moduleRun(module: String, vararg args: String) {
        val strings = arrayOf(python3, "-m", module) + args
        Runtime.getRuntime().exec(strings)
        println("Running ${strings.joinToString(" ")}")
    }

    fun iteRun(vararg args: String) {
        Runtime.getRuntime().exec(arrayOf(ite) + args)
    }

    fun module(name: String, runConfig: PythonModuleScope.() -> Unit) {
        return PythonModuleScope(name).runConfig()
    }

    inner class PythonModuleScope(private val module: String) {
        fun exec(vararg args: String) {
            moduleRun(module = this.module, args = args)
        }
    }
}