#  KotlinRuntimePlugin

[![Xpdustry latest](https://maven.xpdustry.fr/api/badge/latest/releases/fr/xpdustry/kotlin-runtime?color=00ced1&name=KotlinRuntimePlugin&prefix=v)](https://maven.xpdustry.fr/#/releases/fr/xpdustry/kotlin-runtime)
[![Downloads](https://img.shields.io/github/downloads/Xpdustry/KotlinRuntimePlugin/total?color=00ced1)](https://github.com/Xpdustry/KotlinRuntimePlugin/releases)
[![Mindustry 7.0](https://img.shields.io/badge/Mindustry-7.0-00ced1)](https://github.com/Anuken/Mindustry/releases)

## Description

This plugin allows you to write kotlin plugins without having to ship the kotlin runtime with your plugin. Thus avoiding conflicts with other plugins. It currently comes with version 1.8.0 and the following libraries :

- The standard library
- The reflection library

If you want to use other kotlin core libraries, don't mind opening an issue, so I can add them in this plugin.

## Requirements

This plugin requires Mindustry v141 or later and Java 17 or later.

## Building

- `./gradlew shadowJar` to only compile the plugin (it will be located at `/build/libs/KotlinRuntimePlugin.jar`).
- `./gradlew runMindustryServer` to run the plugin in a local Mindustry server.
- `./gradlew runMindustryClient` to run a local Mindustry client.
