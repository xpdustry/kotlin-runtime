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

If you want to use other kotlin libraries, don't mind opening an issue.

## Requirements

This plugin requires Mindustry v145 or later and Java 17 or later.

## Building

- `./gradlew shadowJar` to only compile the plugin (it will be located at `/build/libs/kotlin-runtime.jar`).
- `./gradlew runMindustryServer` to run the plugin in a local Mindustry server.
- `./gradlew runMindustryClient` to run a local Mindustry client.
