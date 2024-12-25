val ktorVersion = "3.0.2"

plugins {
    id("org.jetbrains.kotlin.jvm") version "2.1.0"
    id("io.ktor.plugin") version "3.0.2"
    application
}

group = "de.ohmygit.smartie.ui.drvctl.ite8291"
version = "1.0.0"
application {
    mainClass.set("de.ohmygit.smartie.ui.drvctl.ite8291.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

sourceSets {
//    val nativeMain by getting {
//        dependencies {
//            implementation("io.ktor:ktor-server-core:$ktor_version")
//            implementation("io.ktor:ktor-server-cio:$ktor_version")
//        }
//    }
//    val nativeTest by getting {
//        dependencies {
//            implementation(kotlin("test"))
//            implementation("io.ktor:ktor-server-test-host:$ktor_version")
//        }
//    }
    main {
        java.srcDir("src/main/java")
        kotlin.srcDir("src/main/kotlin")
    }
    test {
        java.srcDir("src/test/java")
        kotlin.srcDir("src/test/kotlin")
    }
}

dependencies {
    implementation(projects.shared)
    implementation("ch.qos.logback:logback-classic:1.5.12")
    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    implementation("io.ktor:ktor-network:$ktorVersion")
//    testImplementation("io.ktor:ktor-server-tests-jvm:3.0.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:2.1.0")
    testImplementation("io.ktor:ktor-server-test-host-jvm:3.0.2")
    implementation("com.google.code.gson:gson:2.8.9")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    testImplementation("org.hamcrest:hamcrest:2.2")
}

// Configurable directories
val jniHeaderDir = layout.buildDirectory.dir("generated/sources/headers/java/main")
val cSourceDir = layout.projectDirectory.dir("native")
val outputLibDir = layout.buildDirectory.dir("libs")
val javaFilesDir = layout.projectDirectory.dir("src/main/java/")

// Detect JAVA_HOME, fallback to system properties if not set
val javaHome: String = System.getenv("JAVA_HOME") ?: System.getProperty("java.home") ?: error("JAVA_HOME is not set")
val javac: String = "$javaHome/bin/javac"
val libName: String = "libswayipc.so"

// Ensure directories exist
tasks.register("createDirs") {
    doFirst {
        jniHeaderDir.get().asFile.mkdirs()
        outputLibDir.get().asFile.mkdirs()
    }
}

// Task to generate JNI headers
tasks.register<Exec>("generateHeaders") {
    dependsOn("createDirs")
    group = "build"
    description = "Generates JNI headers for SwayIPC.java"

    workingDir = layout.projectDirectory.asFile
    commandLine(
        javac, "-h", jniHeaderDir.get().asFile,
        javaFilesDir.file("SwayIPC.java").asFile,
    )
}

// Task to generate JNI headers
tasks.register<Exec>("removeClass") {
    dependsOn("generateHeaders")
    group = "build"
    description = "rm SwayIPC.class"

    workingDir = layout.projectDirectory.asFile
    setIgnoreExitValue(true)
    commandLine(
        "rm", javaFilesDir.file("SwayIPC.class").asFile
    )
}

// Task to compile the native C code into a shared library
tasks.register<Exec>("compileNativeLib") {
    dependsOn("removeClass")
    group = "build"
    description = "Compiles the native C code into a shared library"

    workingDir = projectDir

    val includeDir = "$javaHome/include"
    val includeOsDir = "$includeDir/linux"

    commandLine(
        "/usr/bin/gcc", "-shared", "-fPIC",
        "-o", outputLibDir.get().file(libName).asFile,
        "-I", includeDir,
        "-I", includeOsDir,
        "-I", jniHeaderDir.get().asFile,
        cSourceDir.file("sway_ipc.c")
    )
}

tasks.test {
    dependsOn("compileNativeLib", "installDriver")
    sourceSets.getByName("test").java.srcDir("src/test/java")
    sourceSets.getByName("test").kotlin.srcDir("src/test/kotlin")
    useJUnitPlatform()
}

tasks.build {
    dependsOn("test")
    sourceSets.getByName("main").java.srcDir("src/main/java")
    sourceSets.getByName("main").kotlin.srcDir("src/main/kotlin")
}

//val hostOs = System.getProperty("os.name")
//val arch = System.getProperty("os.arch")
//val nativeTarget = when {
//    hostOs == "Mac OS X" && arch == "x86_64" -> macosX64("native")
//    hostOs == "Mac OS X" && arch == "aarch64" -> macosArm64("native")
//    hostOs == "Linux" -> linuxX64("native")
//    // Other supported targets are listed here: https://ktor.io/docs/native-server.html#targets
//    else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
//}
//
//nativeTarget.apply {
//    binaries {
//        executable {
//            entryPoint = "main"
//        }
//    }
//}


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