import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import java.time.LocalDateTime

// masterpiece in version numbering ;-)
// version will increment all 10 minutes
val currentDateTime: LocalDateTime = LocalDateTime.now()
val majorVersion = (currentDateTime.year - 2014) / 10
val yearPart = (currentDateTime.year - 2014) - 10
val monthPart = String.format("%02d", currentDateTime.monthValue)
val dayPart = String.format("%02d", currentDateTime.dayOfMonth)
val hourPart = String.format("%02d", currentDateTime.hour)
val minutesPart = String.format("%02d", currentDateTime.minute)
val version = "$majorVersion.$yearPart$monthPart.$dayPart$hourPart$minutesPart".take(11)

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
    kotlin("plugin.serialization") version "1.9.0"
}

kotlin {
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.7.3")
                implementation("com.github.h0tk3y.betterParse:better-parse:0.4.4")
                implementation("io.ktor:ktor-serialization-kotlinx-json:3.0.0")
            }
            kotlin.srcDir(layout.buildDirectory.dir("generated/version"))
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
        }

    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Sax"
            packageVersion = "$version"

            linux {
                iconFile.set(project.file("desktopAppIcons/LinuxIcon.png"))
            }
            windows {
                iconFile.set(project.file("desktopAppIcons/WindowsIcon.ico"))
            }
            macOS {
                iconFile.set(project.file("desktopAppIcons/MacosIcon.icns"))
                bundleID = "at.crowdware.sax.desktopApp"
            }
        }
    }
}

tasks.named("assemble") {
    mustRunAfter("generateVersionFile")
}

tasks.register("generateVersionFile") {
    val outputDir = layout.buildDirectory.dir("generated/version").get().asFile
    val versionValue = version

    inputs.property("version", versionValue)
    outputs.dir(outputDir)

    doLast {
        // Schreibe die Versionsnummer in die Datei
        val versionFile = outputDir.resolve("Version.kt")
        versionFile.parentFile.mkdirs()
        versionFile.writeText("""
            package at.crowdware.sax

            object Version {
                const val version = "$versionValue"
            }
        """.trimIndent())
        println("Version changed to: $versionValue")
    }
}


tasks.named("build") {
    dependsOn("generateVersionFile")
}