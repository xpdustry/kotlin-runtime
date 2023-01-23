import fr.xpdustry.toxopid.dsl.anukenJitpack
import fr.xpdustry.toxopid.dsl.mindustryDependencies
import fr.xpdustry.toxopid.spec.ModMetadata
import fr.xpdustry.toxopid.spec.ModPlatform

plugins {
    kotlin("jvm") version "1.8.0"
    id("com.diffplug.spotless") version "6.11.0"
    id("net.kyori.indra") version "3.0.1"
    id("net.kyori.indra.publishing") version "3.0.1"
    id("net.kyori.indra.git") version "3.0.1"
    id("net.kyori.indra.licenser.spotless") version "3.0.1"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("fr.xpdustry.toxopid") version "3.0.0"
}

val metadata = ModMetadata.fromJson(file("plugin.json").readText())
group = "fr.xpdustry"
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
    anukenJitpack()
}

dependencies {
    mindustryDependencies()
    api(kotlin("stdlib"))
    api(kotlin("reflect"))

    val junit = "5.9.0"
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junit")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junit")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junit")
}

// Required for the GitHub actions
tasks.register("getArtifactPath") {
    doLast { println(tasks.shadowJar.get().archiveFile.get().toString()) }
}

tasks.shadowJar {
    // Makes sure the name of the final jar is (plugin-display-name).jar
    archiveFileName.set(metadata.displayName + ".jar")
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
}

tasks.build {
    // Make sure the shadow jar is built during the build task
    dependsOn(tasks.shadowJar)
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

    publishSnapshotsTo("xpdustry", "https://maven.xpdustry.fr/snapshots")
    publishReleasesTo("xpdustry", "https://maven.xpdustry.fr/releases")

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
                url.set("https://www.xpdustry.fr")
            }

            developers {
                developer {
                    id.set(metadata.author)
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
    property("AUTHOR", metadata.author)
    property("YEAR", "2022")
}
