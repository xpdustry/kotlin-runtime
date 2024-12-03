import com.xpdustry.toxopid.spec.ModMetadata
import com.xpdustry.toxopid.spec.ModPlatform
import java.util.Properties

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.spotless)
    alias(libs.plugins.indra.common)
    alias(libs.plugins.indra.git)
    alias(libs.plugins.indra.publishing)
    alias(libs.plugins.shadow)
    alias(libs.plugins.toxopid)
}

val metadata = ModMetadata.fromJson(file("mod.json"))
group = "com.xpdustry"
metadata.version += "-k." + libs.versions.kotlin.get()
if (indraGit.headTag() == null) {
    metadata.version += "-SNAPSHOT"
}
version = metadata.version
description = metadata.description

toxopid {
    compileVersion = "v" + metadata.minGameVersion
    platforms = setOf(ModPlatform.DESKTOP, ModPlatform.ANDROID, ModPlatform.SERVER)
}

repositories {
    mavenCentral()
    maven("https://maven.xpdustry.com/mindustry") {
        name = "xpdustry-mindustry"
        mavenContent { releasesOnly() }
    }
}

dependencies {
    compileOnly(toxopid.dependencies.mindustryCore)
    compileOnly(toxopid.dependencies.arcCore)
    api(kotlin("stdlib-jdk8"))
    api(kotlin("reflect"))
    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.jdk8)
    api(libs.kotlinx.serialization.json)
}

val generateResources =
    tasks.register("generateFiles") {
        outputs.files(fileTree(temporaryDir))

        doLast {
            val temp = temporaryDir.resolve("plugin.json")
            temp.writeText(ModMetadata.toJson(metadata, true))
        }

        doLast {
            val temp = temporaryDir.resolve("com/xpdustry/kotlin/versions.properties")
            val properties = Properties()
            properties["kotlinx.coroutines"] = libs.versions.kotlinx.coroutines.get()
            properties["kotlin.base"] = libs.versions.kotlin.get()
            properties["kotlinx.serialization"] = libs.versions.kotlinx.serialization.get()
            temp.parentFile.mkdirs()
            temp.writer().use { properties.store(it, null) }
        }
    }

tasks.shadowJar {
    archiveClassifier = "mod"
    from(generateResources)
    from(rootProject.file("LICENSE.md")) { into("META-INF") }
}

tasks.dexJar {
    r8Version = "8.8.20"
}

tasks.mergeJar {
    archiveFileName.set("kotlin-runtime.jar")
}

tasks.register("getArtifactPath") {
    doLast { println(tasks.mergeJar.get().archiveFile.get().toString()) }
}

tasks.build {
    dependsOn(tasks.mergeJar)
}

// Indra adds the javadoc task, we don't want that so disable it
components.named("java") {
    val component = this as AdhocComponentWithVariants
    component.withVariantsFromConfiguration(configurations.javadocElements.get()) {
        skip()
    }
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
}

indra {
    javaVersions {
        target(8)
        minimumToolchain(17)
    }

    publishSnapshotsTo("xpdustry", "https://maven.xpdustry.com/snapshots")
    publishReleasesTo("xpdustry", "https://maven.xpdustry.com/releases")

    mitLicense()

    val repo = metadata.repository.split("/")
    github(repo[0], repo[1]) {
        ci(true)
        issues(true)
        scm(true)
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
        licenseHeaderFile(rootProject.file("HEADER.txt"))
    }
    kotlinGradle {
        ktlint()
    }
}
