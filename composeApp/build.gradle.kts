import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    id("org.jetbrains.kotlin.multiplatform") version "2.1.0"
    id("org.jetbrains.compose") version "1.7.0"
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.0"
}

kotlin {
    jvm("desktop")
    
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }
    
    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation("org.jetbrains.compose.runtime:runtime:1.7.0")
            implementation("org.jetbrains.compose.foundation:foundation:1.7.0")
            implementation("org.jetbrains.compose.material:material:1.7.0")
            implementation("org.jetbrains.compose.ui:ui:1.7.0")
            implementation("org.jetbrains.compose.components:components-resources:1.7.0")
            implementation("org.jetbrains.compose.components:components-ui-tooling-preview:1.7.0")
            implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel:2.8.4")
            implementation("org.jetbrains.androidx.lifecycle:lifecycle-runtime-compose:2.8.4")
            implementation(project(":shared"))
        }
        desktopMain.dependencies {
            implementation("org.jetbrains.compose.desktop:desktop:1.7.0")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.9.0")
        }
    }
}


compose.desktop {
    application {
        mainClass = "de.ohmygit.smartie.ui.drvctl.ite8291.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "de.ohmygit.smartie.ui.drvctl.ite8291"
            packageVersion = "1.0.0"
        }
    }
}
