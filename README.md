#  kotlin-runtime

[![Xpdustry latest](https://maven.xpdustry.com/api/badge/latest/releases/com/xpdustry/kotlin-runtime?color=00ced1&name=kotlin-runtime&prefix=v)](https://maven.xpdustry.com/#/releases/com/xpdustry/kotlin-runtime)
[![Downloads](https://img.shields.io/github/downloads/Xpdustry/KotlinRuntimePlugin/total?color=00ced1)](https://github.com/xpdustry/kotlin-runtime/releases)
[![Mindustry 7.0](https://img.shields.io/badge/Mindustry-7.0-00ced1)](https://github.com/Anuken/Mindustry/releases)

## Description

This plugin allows you to write kotlin plugins without having to ship the kotlin runtime.
Thus avoiding conflicts with other plugins.
It currently comes with kotlin version 1.9.10 and the following libraries :

- The standard library
- The reflection library
- The coroutines library
- The serialization library (json only)

If you want to use other kotlin libraries, don't mind opening an issue.

## Usage

Besides downloading this plugin alongside yours,
you need to also make sure you do not include the kotlin stdlib when compiling your plugin.
To do so, just add the following in your build script:

```gradle
configurations.runtimeClasspath {
    exclude("org.jetbrains.kotlin", "kotlin-stdlib")
    exclude("org.jetbrains.kotlin", "kotlin-stdlib-common")
    exclude("org.jetbrains.kotlin", "kotlin-stdlib-jdk8")
    exclude("org.jetbrains.kotlin", "kotlin-reflect")
    // Add the following lines if you use coroutines
    exclude("org.jetbrains.kotlinx", "kotlinx-coroutines-core")
    exclude("org.jetbrains.kotlinx", "kotlinx-coroutines-jdk8")
    // Add the following line if you use serialization json
    exclude("org.jetbrains.kotlinx", "kotlinx-serialization-json")
}
```

## Requirements

This plugin requires Mindustry v145 or later and Java 17 or later.

## Building

- `./gradlew shadowJar` to only compile the plugin (it will be located at `/build/libs/kotlin-runtime.jar`).
- `./gradlew runMindustryServer` to run the plugin in a local Mindustry server.
- `./gradlew runMindustryClient` to run a local Mindustry client.
