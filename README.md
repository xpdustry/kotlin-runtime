# TemplatePlugin

[![Build](https://github.com/Xpdustry/KotlinRuntimePlugin/actions/workflows/build.yml/badge.svg)](https://github.com/Xpdustry/KotlinRuntimePlugin/actions/workflows/build.yml)
[![Mindustry 6.0 | 7.0 ](https://img.shields.io/badge/Mindustry-6.0%20%7C%207.0-ffd37f)](https://github.com/Anuken/Mindustry/releases)
[![Xpdustry latest](https://repo.xpdustry.fr/api/badge/latest/releases/fr/xpdustry/kotlin-runtime-plugin?color=00FFFF&name=TemplatePlugin&prefix=v)](https://github.com/Xpdustry/KotlinRuntimePlugin/releases)

## Description

This plugin provides the kotlin (1.6.10) librairies for Kotlin plugins :

- `xpdustry-kotlin-stdlib`

- `xpdustry-kotlin-refeflect`

## Building

- `./gradlew jar` for a simple jar that contains only the plugin code.

- `./gradlew shadowJar` for a fatJar that contains the plugin and its dependencies (use this for your server).

## Testing

- `./gradlew runMindustryClient`: Run Mindustry in desktop with the plugin.

- `./gradlew runMindustryServer`: Run Mindustry in a server with the plugin.

## Running

This plugin is compatible with V6 and V7.

If you run on V6 or V7 up to v135, you will need [mod-loader](https://github.com/Xpdustry/ModLoaderPlugin).
