#  kotlin-runtime

[![Downloads](https://img.shields.io/github/downloads/xpdustry/kotlin-runtime/total?color=008080&label=Downloads)](https://github.com/xpdustry/kotlin-runtime/releases)
[![Mindustry 8.0](https://img.shields.io/badge/Mindustry-8.0-008080)](https://github.com/Anuken/Mindustry/releases)
[![Discord](https://img.shields.io/discord/519293558599974912?color=008080&label=Discord)](https://discord.xpdustry.com)

## Description

This mod allows you to write kotlin mods/plugins without having to ship the kotlin runtime.
Thus avoiding conflicts with other mods/plugins.
It currently comes with kotlin version `2.3.0` and the following libraries :

- The standard library (jdk8)
- The reflection library
- The coroutines library
- The serialization library (json only)
- The kotlinx datetime library (for compat)

If you want to use other kotlin libraries, don't mind creating an issue.

This mod also supports android, although you should still avoid certain java 8 features.

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
    // Add the following line if you use serialization
    exclude("org.jetbrains.kotlinx", "kotlinx-serialization-core")
    exclude("org.jetbrains.kotlinx", "kotlinx-serialization-json")
    // Add the following line if you use datetime
    exclude("org.jetbrains.kotlinx", "kotlinx-datetime")
}
```

## Requirements

This mod requires Mindustry v154 or later and Java 8 or later.

## Building

- `./gradlew :mergeJar` to only compile the mod (it will be located at `build/libs/kotlin-runtime.jar`).
- `./gradlew :runMindustryServer` to run the mod in a local Mindustry server.
- `./gradlew :runMindustryClient` to run a local Mindustry client.
