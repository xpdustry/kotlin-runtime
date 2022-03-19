import groovy.json.JsonSlurper
import java.io.File
import fr.xpdustry.toxopid.extension.ModTarget
import net.ltgt.gradle.errorprone.CheckSeverity
import net.ltgt.gradle.errorprone.errorprone
import java.io.ByteArrayOutputStream

plugins {
    // java
    id("net.kyori.indra") version "2.1.1"
    id("net.kyori.indra.publishing") version "2.1.1"
    kotlin("jvm") version "1.6.10"
    id("com.github.ben-manes.versions") version "0.42.0"
    id("net.ltgt.errorprone") version "2.0.2"
    id("fr.xpdustry.toxopid") version "1.3.2"
}

@Suppress("UNCHECKED_CAST")
fun readJson(file: File): MutableMap<String, Any> = JsonSlurper().parse(file) as MutableMap<String, Any>

val parentMetadata = readJson(file("$rootDir/global-plugin.json"))

group = property("props.project-group").toString()
version = (parentMetadata["version"] as String) + if (indraGit.headTag() == null) "-SNAPSHOT" else ""
description = "The kotlin runtime baby !"

tasks.create("createRelease") {
    dependsOn("requireClean")

    doLast {
        // Checks if a signing key is present
        val signing = ByteArrayOutputStream().use { out ->
            exec {
                commandLine("git", "config", "--global", "user.signingkey")
                standardOutput = out
            }.run { exitValue == 0 && out.toString().isNotBlank() }
        }

        exec {
            commandLine(arrayListOf("git", "tag", "v${parentMetadata["version"]}", "-F", "./CHANGELOG.md", "-a").apply { if (signing) add("-s") })
        }

        exec {
            commandLine("git", "push", "origin", "--tags")
        }
    }
}

try {
    rootProject.tasks.named("publishMavenPublicationToXpdustryRepository").get().enabled = false
} catch (ignored: UnknownTaskException) {
}

allprojects {
    apply(plugin = "net.kyori.indra")

    indra {
        javaVersions {
            target(17)
            minimumToolchain(17)
        }
    }
}

subprojects {
    apply(plugin = "net.ltgt.errorprone")
    apply(plugin = "net.kyori.indra.publishing")
    apply(plugin = "fr.xpdustry.toxopid")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    toxopid {
        modTarget.set(ModTarget.HEADLESS)

        val compileVersion = parentMetadata["minGameVersion"] as String
        arcCompileVersion.set(compileVersion)
        mindustryCompileVersion.set(compileVersion)
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        val jetbrains = "23.0.0"
        compileOnly("org.jetbrains:annotations:$jetbrains")
        testCompileOnly("org.jetbrains:annotations:$jetbrains")

        // Static analysis
        annotationProcessor("com.uber.nullaway:nullaway:0.9.5")
        errorprone("com.google.errorprone:error_prone_core:2.11.0")
    }

    tasks.withType(JavaCompile::class.java).configureEach {
        options.errorprone {
            disableWarningsInGeneratedCode.set(true)
            disable("MissingSummary")
            if (!name.contains("test", true)) {
                check("NullAway", CheckSeverity.ERROR)
                option("NullAway:AnnotatedPackages", project.property("props.root-package").toString())
            }
        }
    }

    tasks.create("getArtifactPath") {
        doLast { println(tasks.shadowJar.get().archiveFile.get().toString()) }
    }

    tasks.named<Jar>("shadowJar") {
        val file = temporaryDir.resolve("plugin.json")
        val localMetadata = readJson(file("$projectDir/local-plugin.json"))
        file.writeText(groovy.json.JsonBuilder(localMetadata + parentMetadata).toPrettyString())
        from(file)
    }

    signing {
        val signingKey: String? by project
        val signingPassword: String? by project
        useInMemoryPgpKeys(signingKey, signingPassword)
    }

    indra {
        publishReleasesTo("xpdustry", "https://repo.xpdustry.fr/releases")
        publishSnapshotsTo("xpdustry", "https://repo.xpdustry.fr/snapshots")

        mitLicense()

        if (parentMetadata["repo"] != null) {
            val repo = (parentMetadata["repo"] as String).split("/")
            github(repo[0], repo[1]) {
                ci(true)
                issues(true)
                scm(true)
            }
        }

        configurePublications {
            pom {
                developers {
                    developer { id.set(parentMetadata["author"] as String) }
                }
            }
        }
    }
}
