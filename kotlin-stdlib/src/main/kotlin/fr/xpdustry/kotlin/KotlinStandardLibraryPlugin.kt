package fr.xpdustry.kotlin

import arc.util.Log
import mindustry.mod.Plugin

@Suppress("UNUSED")
class KotlinStandardLibraryPlugin : Plugin() {
    companion object {
        const val KOTLIN_VERSION = "1.6.10"
    }

    override fun init() {
        Log.info("Loaded the kotlin standard library v@", KOTLIN_VERSION)
    }
}