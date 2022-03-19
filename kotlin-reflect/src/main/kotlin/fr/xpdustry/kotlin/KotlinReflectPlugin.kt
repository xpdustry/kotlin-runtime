package fr.xpdustry.kotlin

import arc.util.Log
import mindustry.mod.Plugin

@Suppress("UNUSED")
class KotlinReflectPlugin : Plugin() {
    companion object {
        const val KOTLIN_VERSION = "1.6.10"
    }

    override fun init() {
        Log.info("Loaded the kotlin reflection library v@", KOTLIN_VERSION)
    }
}