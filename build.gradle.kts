import com.xpdustry.toxopid.spec.ModMetadata
import com.xpdustry.toxopid.spec.ModPlatform
import com.xpdustry.toxopid.task.GithubAssetDownload
import com.xpdustry.toxopid.task.MindustryExec
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.put

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.spotless)
    alias(libs.plugins.indra.common)
    alias(libs.plugins.indra.git)
    alias(libs.plugins.indra.publishing)
    alias(libs.plugins.shadow)
    alias(libs.plugins.toxopid)
}

buildscript {
    dependencies {
        classpath(libs.kotlinx.serialization.json)
    }
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
    api(libs.kotlinx.datetime)
}

val generateResources = tasks.register("generateFiles") {
    outputs.files(fileTree(temporaryDir))

    doLast {
        val temp = temporaryDir.resolve("mod.json")
        temp.writeText(ModMetadata.toJson(metadata, true))
    }

    doLast {
        val temp = temporaryDir.resolve("com/xpdustry/kotlin/versions.json")
        temp.parentFile.mkdirs()
        temp.writeText(
            buildJsonArray {
                addJsonObject {
                    put("name", "Kotlin stdlib+reflect")
                    put("version", libs.versions.kotlin.get())
                }
                addJsonObject {
                    put("name", "Kotlinx coroutines")
                    put("version", libs.versions.kotlinx.coroutines.get())
                }
                addJsonObject {
                    put("name", "Kotlinx serialization")
                    put("version", libs.versions.kotlinx.serialization.get())
                }
                addJsonObject {
                    put("name", "Kotlinx datetime")
                    put("version", libs.versions.kotlinx.datetime.get())
                }
            }.toString(),
        )
    }
}

tasks.shadowJar {
    from(generateResources)
    from(rootProject.file("LICENSE.md")) { into("META-INF") }
}

tasks.dexJar {
    r8Version = "8.10.21"
}

tasks.mergeJar {
    archiveFileName = "kotlin-runtime.jar"
    archiveClassifier = "mod"
}

tasks.register("getArtifactPath") {
    doLast { println(tasks.mergeJar.get().archiveFile.get().toString()) }
}

tasks.build {
    dependsOn(tasks.mergeJar)
}

val downloadSlf4md by tasks.registering(GithubAssetDownload::class) {
    owner = "xpdustry"
    repo = "slf4md"
    asset = "slf4md.jar"
    version = libs.versions.slf4md.map { "v$it" }
}

tasks.withType<MindustryExec> {
    mods.setFrom(downloadSlf4md, tasks.mergeJar)
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
        ktfmt().kotlinlangStyle()
        licenseHeaderFile(rootProject.file("HEADER.txt"))
    }
    kotlinGradle {
        ktlint().editorConfigOverride(mapOf("ktlint_code_style" to "intellij_idea", "max_line_length" to "120"))
    }
}
