pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.xpdustry.com/snapshots") {
            name = "xpdustry-snapshots"
            mavenContent { snapshotsOnly() }
        }
        mavenLocal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

rootProject.name = "kotlin-runtime"
