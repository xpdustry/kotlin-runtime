import com.xpdustry.toxopid.extension.anukeXpdustry
import com.xpdustry.toxopid.spec.ModMetadata
import com.xpdustry.toxopid.spec.ModPlatform
import com.xpdustry.toxopid.task.GithubAssetDownload
import com.xpdustry.toxopid.task.MindustryExec
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.put

plugins {
    alias(libs.plugins.spotless)
    alias(libs.plugins.indra.common)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.shadow)
    alias(libs.plugins.toxopid)
}

buildscript {
    dependencies {
        classpath(libs.kotlinx.serialization.json)
    }
}

val metadata = ModMetadata.fromJson(file("mod.json"))
metadata.version += "+k." + libs.versions.kotlin.get()
metadata.version += if (findProperty("release").toString().toBoolean()) "" else "-SNAPSHOT"
version = metadata.version
group = "com.xpdustry"
description = metadata.description

toxopid {
    compileVersion = "v" + metadata.minGameVersion
    platforms = setOf(ModPlatform.DESKTOP, ModPlatform.ANDROID, ModPlatform.SERVER)
}

repositories {
    mavenCentral()
    anukeXpdustry()
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

val generateModResources by tasks.registering {
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
                    put(
                        "version",
                        libs.versions.kotlinx.coroutines
                            .get(),
                    )
                }
                addJsonObject {
                    put("name", "Kotlinx serialization")
                    put(
                        "version",
                        libs.versions.kotlinx.serialization
                            .get(),
                    )
                }
                addJsonObject {
                    put("name", "Kotlinx datetime")
                    put(
                        "version",
                        libs.versions.kotlinx.datetime
                            .get(),
                    )
                }
            }.toString(),
        )
    }
}

tasks.shadowJar {
    from(generateModResources)
    from(rootProject.file("LICENSE.md")) { into("META-INF") }
    from("assets") { include("**") }
}

tasks.dexJar {
    r8Version = "8.10.21"
}

tasks.mergeJar {
    archiveFileName = "kotlin-runtime.jar"
    archiveClassifier = "mod"
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

indra {
    javaVersions {
        target(8)
        minimumToolchain(17)
    }
}

spotless {
    kotlin {
        ktfmt().kotlinlangStyle()
        licenseHeaderFile(rootProject.file("HEADER.txt"))
    }
    kotlinGradle {
        ktlint()
    }
}
