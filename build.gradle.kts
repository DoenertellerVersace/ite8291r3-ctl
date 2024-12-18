val venvDir = File("${projectDir}/venv")
val pythonExecutable = File(venvDir, "bin/python").absolutePath
val pipExecutable = File(venvDir, "bin/pip").absolutePath
val requirementsFile = File("${projectDir}/requirements.txt")

tasks {
    // Task to check Python 3, pip, and venv
    register("checkPython") {
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
                    commandLine("bash", "-c", cmd)
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

    // Task to check or create virtual environment
    register("createVenv") {
        group = "setup"
        description = "Check if virtual environment exists or create one."
        dependsOn("checkPython")

        doLast {
            if (!venvDir.exists()) {
                println("Creating virtual environment in $venvDir...")
                exec {
                    commandLine("bash", "-c", "python3 -m venv ${venvDir.absolutePath}")
                }
            } else {
                println("Virtual environment already exists at $venvDir.")
            }
        }
    }

    // Task to install driver using pip
    register("installDriver") {
        group = "setup"
        description = "Install the userspace driver."
        dependsOn("createVenv")

        doLast {
            println("Installing driver")
            exec {
                commandLine("bash", "-c", "$pipExecutable install --upgrade pip")
            }
            exec {
                commandLine("bash", "-c", "$pipExecutable install .")
            }
        }
    }

//    // Task to build the GUI app
//    register("buildGUI") {
//        group = "build"
//        description = "Build the GUI application for controlling the keyboard backlight."
//        dependsOn("installDriver")
//
//        doLast {
//            println("Building the GUI application...")
//            exec {
//                commandLine("bash", "-c", "echo hello-world")
//            }
//        }
//    }

    // Task to run the GUI app with sudo privileges
    register("testDriver") {
        group = "test"
        description = "Run the GUI application with sudo privileges."
        dependsOn("installDriver")

        doLast {
            println("Running a driver command in userspace")
            exec {
                commandLine("bash", "-c", "$pythonExecutable -m ite8291r3_ctl monocolor --brightness 35 --rgb 40,0,80")
            }
        }
    }
}

//tasks.getByName("build") {
//    dependsOn("buildGUI")
//}

//
//tasks.getByName("run") {
//    dependsOn("runGUI")
//}
