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

//sourceSets {
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
//}

dependencies {
    implementation(projects.shared)
    implementation("ch.qos.logback:logback-classic:1.5.12")
    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    implementation("io.ktor:ktor-network:$ktorVersion")
//    testImplementation("io.ktor:ktor-server-tests-jvm:3.0.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:2.1.0")
    testImplementation("io.ktor:ktor-server-test-host-jvm:3.0.2")
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