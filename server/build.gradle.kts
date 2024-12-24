import org.apache.tools.ant.taskdefs.condition.Os

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

val libName = if (Os.isFamily(Os.FAMILY_WINDOWS)) "swayipc.dll" else "libswayipc.so"

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
    val includeOsDir = if (Os.isFamily(Os.FAMILY_WINDOWS)) "$includeDir/win32" else "$includeDir/linux"

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
    dependsOn("compileNativeLib")
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