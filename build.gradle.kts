import fr.xpdustry.toxopid.dsl.mindustryDependencies
import fr.xpdustry.toxopid.spec.ModMetadata
import fr.xpdustry.toxopid.spec.ModPlatform
import java.util.Properties

plugins {
    kotlin("jvm") version libs.versions.kotlin
    kotlin("plugin.serialization") version libs.versions.kotlin
    id("com.diffplug.spotless") version libs.versions.spotless
    id("net.kyori.indra") version libs.versions.indra
    id("net.kyori.indra.publishing") version libs.versions.indra
    id("net.kyori.indra.git") version libs.versions.indra
    id("net.kyori.indra.licenser.spotless") version libs.versions.indra
    id("com.github.johnrengelman.shadow") version libs.versions.shadow
    id("fr.xpdustry.toxopid") version libs.versions.toxopid
    id("com.github.ben-manes.versions") version libs.versions.versions
}

val metadata = ModMetadata.fromJson(file("plugin.json").readText())
group = "com.xpdustry"
metadata.version += "-k." + libs.versions.kotlin.get()
if (indraGit.headTag() == null) {
    metadata.version += "-SNAPSHOT"
}
version = metadata.version
description = metadata.description

toxopid {
    compileVersion.set("v" + metadata.minGameVersion)
    platforms.set(setOf(ModPlatform.HEADLESS))
}

repositories {
    mavenCentral()
    maven("https://maven.xpdustry.com/mindustry") {
        name = "xpdustry-mindustry"
        mavenContent { releasesOnly() }
    }
}

dependencies {
    mindustryDependencies()
    api(kotlin("stdlib-jdk8"))
    api(kotlin("reflect"))
    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.jdk8)
    api(libs.kotlinx.serialization.json)
}

// Required for the GitHub actions
tasks.register("getArtifactPath") {
    doLast { println(tasks.shadowJar.get().archiveFile.get().toString()) }
}

tasks.shadowJar {
    archiveFileName.set("kotlin-runtime.jar")
    // Set the classifier to plugin for publication on a maven repository
    archiveClassifier.set("plugin")
    // Include the plugin.json file with the modified version
    doFirst {
        val temp = temporaryDir.resolve("plugin.json")
        temp.writeText(metadata.toJson(true))
        from(temp)
    }
    // Include the license of your project
    from(rootProject.file("LICENSE.md")) {
        into("META-INF")
    }
    // Generate version file
    doFirst {
        val temp = temporaryDir.resolve("kotlin-runtime-versions.properties")
        val properties = Properties()
        properties["kotlinx.coroutines"] = libs.versions.kotlinx.coroutines.get()
        properties["kotlin.base"] = libs.versions.kotlin.get()
        properties["kotlinx.serialization"] = libs.versions.kotlinx.serialization.get()
        temp.writer().use { properties.store(it, null) }
        from(temp)
    }
}

tasks.build {
    // Make sure the shadow jar is built during the build task
    dependsOn(tasks.shadowJar)
}

// Indra adds the javadoc task, we don't want that so disable it
components.named("java") {
    val component = this as AdhocComponentWithVariants
    component.withVariantsFromConfiguration(configurations.javadocElements.get()) {
        skip()
    }
}

tasks.dependencyUpdates {
    fun isNonStable(version: String): Boolean {
        val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
        val regex = "^[0-9,.v-]+(-r)?$".toRegex()
        val isStable = stableKeyword || regex.matches(version)
        return isStable.not()
    }
    rejectVersionIf {
        isNonStable(candidate.version) && !isNonStable(currentVersion)
    }
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
}

indra {
    javaVersions {
        target(17)
        minimumToolchain(17)
    }

    publishSnapshotsTo("xpdustry", "https://maven.xpdustry.com/snapshots")
    publishReleasesTo("xpdustry", "https://maven.xpdustry.com/releases")

    // The license of your project, kyori has already functions for the most common licenses
    // such as gpl3OnlyLicense() for GPLv3, apache2License() for Apache 2.0, etc.
    // You can still specify your own license using the license { } builder function.
    mitLicense()

    if (metadata.repo.isNotBlank()) {
        val repo = metadata.repo.split("/")
        github(repo[0], repo[1]) {
            ci(true)
            issues(true)
            scm(true)
        }
    }

    configurePublications {
        pom {
            organization {
                name.set("Xpdustry")
                url.set("https://www.xpdustry.com")
            }

            developers {
                developer {
                    id.set("Phinner")
                    timezone.set("Europe/Brussels")
                }
            }
        }
    }
}

spotless {
    kotlin {
        ktlint()
    }
    kotlinGradle {
        ktlint()
    }
}

indraSpotlessLicenser {
    licenseHeaderFile(rootProject.file("LICENSE_HEADER.md"))
    // Some properties to make updating the licence header easier
    property("NAME", metadata.displayName)
    property("DESCRIPTION", metadata.description)
    property("AUTHOR", "Xpdustry")
    property("YEAR", "2023")
}
