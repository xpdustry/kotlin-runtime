# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/),
and this project adheres to [Semantic Versioning](http://semver.org/).

## v4.3.3+k.2.3.0 - 2026-01-16

#### Hotfixes

- Use latest version of R8 ([`cdacde5`](https://github.com/xpdustry/kotlin-runtime/commit/cdacde5f4de6847be6d7af5403cee02aab048f6f))

The last build an outdated version which did not support Kotlin `2.3` correctly.

> ### v4.3.2+k.2.3.0 release

#### :warning: Breaking changes :warning:

- The release scheme has changed. From now on, the Kotlin version is now appended as `+k.version` instead of `-k.version`.

#### Maintenance

- Updated deps and Kotlin to `2.3`.

## v4.3.2+k.2.3.0 - 2026-01-16

### :warning: Breaking changes :warning:

- The release scheme has changed. From now on, the Kotlin version is now appended as `+k.version` instead of `-k.version`.

### Maintenance

- Updated deps and Kotlin to `2.3`.

## v4.3.1-k.2.2.20 - 2025-09-26

### Changes

- Bumped dependencies version, including Kotlin to `2.2.20`.
- Added mod icon.

## v4.3.0-k.2.2.0 - 2025-07-21

### Changes

- Added kotlinx datetime (for [essentials](https://github.com/Kieaer/Essentials)).
- Added [slf4md](https://github.com/xpdustry/slf4md) compat (it will use its logger if possible).

## v4.2.0-k.2.2.0 - 2025-06-25

### Chores

- Updated kotlin to `2.2.0`.
- Bumped minimum version of Mindustry to `v149` (`v8`).

## v4.1.0-k.2.1.20 - 2025-03-24

### Chores

- Updated kotlin to `2.1.20`.

## v4.1.0-k.2.1.0 - 2025-01-09

### Bugfixes

- The mod descriptor file was named `plugin.json` instead of `mod.json`. It has been fixed.

### Chores

- Updated kotlin serialization to `1.8.0`.

## v4.0.1-k.2.1.0 - 2024-12-27

### Chores

- Updated kotlin serialization to `1.10.1`.

## v4.0.0-k.2.1.0 - 2024-12-03

### Chores

- Updated kotlin to `2.1.0` and serialization to `1.7.3`.

## v4.0.0-k.2.0.20 - 2024-09-17

### Features

- The plugin is now a dexed mod, making it usable by plugins and mods, on any platform!

### Chores

- Updated kotlin to `2.0.20`, coroutines to `1.9.0` and serialization to `1.7.2`.

## v3.2.1-k.1.9.24 - 2024-05-20

### Chores

- Updated kotlin to `1.9.24` and coroutines to `1.8.1`.

### Fix

- Fix misleading version on plugin start.

## v3.2.0-k.1.9.23 - 2024-03-30

### Added

- Added Kotlin serialization library (json).

### Chores

- Bumped version of everything, including Kotlin to `1.9.23` and Coroutines to `1.8.0`.

## v3.1.1-k.1.9.22 - 2024-01-08

### Chores

- Bump Kotlin to `1.9.22`.
- Bump several dependencies.

## v3.1.0-k.1.9.10 - 2023-10-11

### Added

- Added Kotlin coroutines `v1.7.3`.

### Chores

- Bumped version of everything, including Kotlin to `1.9.10` and Mindustry to `v146`.
- Completely removed javadoc.
- Moved plugin init message inside Plugin#init`.
- Changed plugin logging name from KotlinRuntimePlugin to KotlinRuntime.

## v3.0.0-k.1.9.0 - 2023-07-26

### Changes

- Changed repository name from `KotlinRuntimePlugin` to `kotlin-runtime`.
- Changed artifact group from `fr.xpdustry` to `com.xpdustry`.

### Chores

- Bumped version of everything, including kotlin to `1.9.0` and mindustry to `v145`.

## v2.0.0-k.1.8.20 - 2023-04-12

### Chores

- Updated license header to 2023.
- Updated Kotlin version to `1.8.20`.

## v2.0.0 - 2023-01-23

### Changes

- The plugin is now a single artifact.

### Chores

- Added license header.
- Cleanup.

## v1.0.0 - 2022-03-19

Initial release.
