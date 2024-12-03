#  kotlin-runtime

[![Xpdustry latest](https://maven.xpdustry.com/api/badge/latest/releases/com/xpdustry/kotlin-runtime?color=00ced1&name=kotlin-runtime&prefix=v)](https://maven.xpdustry.com/#/releases/com/xpdustry/kotlin-runtime)
[![Downloads](https://img.shields.io/github/downloads/Xpdustry/KotlinRuntimePlugin/total?color=00ced1)](https://github.com/xpdustry/kotlin-runtime/releases)
[![Mindustry 7.0](https://img.shields.io/badge/Mindustry-7.0-00ced1)](https://github.com/Anuken/Mindustry/releases)

## Description

This mod allows you to write kotlin mods/plugins without having to ship the kotlin runtime.
Thus avoiding conflicts with other mods/plugins.
It currently comes with kotlin version `2.1.0` and the following libraries :

- The standard library
- The reflection library
- The coroutines library
- The serialization library (json only)

If you want to use other kotlin libraries, don't mind creating an issue.

## Usage

Besides downloading this mod alongside your mod/plugin,
you need to also make sure you do not include the kotlin stdlib when compiling your project.
To do so, just add the following in your build script:

````gradle
configurations.runtimeClasspath {
    exclude("org.jetbrains.kotlin")
    exclude("org.jetbrains.kotlinx")
}
````

Or if you need precise exclusions:

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

This mod requires Mindustry v145 or later and Java 8 or later.

## Building

- `./gradlew shadowJar` to only compile the plugin (it will be located at `/build/libs/kotlin-runtime.jar`).
- `./gradlew runMindustryServer` to run the plugin in a local Mindustry server.
- `./gradlew runMindustryClient` to run a local Mindustry client.
