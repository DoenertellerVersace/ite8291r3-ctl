import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    id("org.jetbrains.kotlin.multiplatform")
}

kotlin {
    jvm()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            // put your Multiplatform dependencies here
        }
    }
}

val venvDir = File(rootDir, "venv")
val pythonExecutable = File(venvDir, "bin/python")
val pipExecutable = File(venvDir, "bin/pip")
val pyModule = File(projectDir, "pymodules")
val requirementsFile = File(pyModule, "requirements.txt")

tasks {
    // Task to check Python 3, pip, and venv
    checkPython()

    // Task to check or create virtual environment
    checkCreateVenv()

    // Task to install driver using pip
    installDriver()

    // Task to run the GUI app with sudo privileges
    testDriver()

//    named("build") {
//        dependsOn("installDriver")
//    }
}

//tasks.getByName("build") {
//    dependsOn("buildGUI")
//}

//
//tasks.getByName("run") {
//    dependsOn("runGUI")
//}

//data class PyEnv(val path: String)
//
//abstract class PyExecutable {
//    abstract val path: String
//    fun executable(env: PyEnv): File = File(env.path, path)
//}
//
//object Python : PyExecutable() {
//    override val path = "bin/python3"
//}
//
//fun Python.module(name: String, vararg args: String) = PythonTaskDescriptor(
//    name = name,
//    description = "Run the $name module.",
//    group = "run"
//)
//
//object Pip : PyExecutable() {
//    override val path = "bin/pip3"
//}
//
//data class PythonTaskDescriptor(
//    val name: String,
//    val description: String,
//    val group: String,
//)
//
//val pythonTasks = listOf(
//
//)

fun TaskContainerScope.checkPython() = register("checkPython") {
    group = "setup"
    description = "Check if Python 3, pip, and venv are installed."

    doLast {
        val commands = listOf(
            "python3 --version",
            "pip3 --version",
            "python3 -m venv --help"
        )

        commands.forEach { cmd ->
            val result = exec {
                sh(cmd)
                isIgnoreExitValue = true
            }
            if (result.exitValue != 0) {
                throw GradleException(
                    when (cmd) {
                        "python3 --version" -> "Python 3 is not installed. Please install Python 3."
                        "pip3 --version" -> "pip is not installed. Please install pip."
                        "python3 -m venv --help" -> "venv is not available. Ensure the 'venv' module is installed."
                        else -> "Unknown error."
                    }
                )
            }
        }
    }
}

fun TaskContainerScope.checkCreateVenv() = register("createVenv") {
    group = "setup"
    description = "Check if virtual environment exists or create one."
    dependsOn("checkPython")

    doLast {
        if (!venvDir.exists()) {
            println("Creating virtual environment in ${venvDir}...")
            exec {
                commandLine("python3", "-m", "venv", venvDir.absolutePath)
            }
        } else {
            println("Virtual environment already exists at ${venvDir}.")
        }
    }
}


fun TaskContainerScope.installDriver() = register("installDriver") {
    group = "setup"
    description = "Install the userspace driver."
    dependsOn("createVenv")

    doLast {
        println("Installing driver")
        exec {
            workingDir(pyModule)
            commandLine(pipExecutable.absolutePath, "install", "--upgrade", "pip")
        }
        exec {
            workingDir(pyModule)
            commandLine(pipExecutable.absolutePath, "install", ".")
        }
    }
}

fun TaskContainerScope.testDriver() = register("testDriver") {
    group = "test"
    description = "Run the GUI application with sudo privileges."
    dependsOn("installDriver")

    doLast {
        println("Running a driver command in userspace")
        exec {
            commandLine(
                pythonExecutable.absolutePath,
                "-m",
                "ite8291r3_ctl",
                "monocolor",
                "--brightness",
                "35",
                "--rgb",
                "40,0,80"
            )
        }
    }
}

fun ExecSpec.sh(cmd: String) {
    commandLine("sh", "-c", cmd)
}