// SPDX-License-Identifier: MIT
package com.xpdustry.kotlin

import arc.util.Log
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import mindustry.Vars
import mindustry.mod.Mod

@Suppress("unused")
internal class KotlinRuntimeMod : Mod() {

    @OptIn(ExperimentalSerializationApi::class)
    override fun init() {
        Json.decodeFromStream<List<KotlinModule>>(
                javaClass.classLoader.getResourceAsStream("com/xpdustry/kotlin/versions.json")!!
            )
            .forEach { module -> info("${module.name} ({}) is now available.", module.version) }
    }

    private var failed = false

    private fun info(message: String, vararg args: Any) {
        if (!failed && Vars.mods.getMod("slf4md") != null) {
            try {
                val logger =
                    Class.forName("org.slf4j.LoggerFactory")
                        .getDeclaredMethod("getLogger", Class::class.java)
                        .invoke(null, KotlinRuntimeMod::class.java)
                Class.forName("org.slf4j.Logger")
                    .getDeclaredMethod("info", String::class.java, Array<Any>::class.java)
                    .invoke(logger, message, args)
                return
            } catch (e: Exception) {
                failed = true
                Log.err("Failed to use slf4md logger", e)
            }
        }
        Log.info("&lb&fb[KotlinRuntime]&fr " + message.replace("{}", "@"), *args)
    }

    @Serializable private data class KotlinModule(val name: String, val version: String)
}
