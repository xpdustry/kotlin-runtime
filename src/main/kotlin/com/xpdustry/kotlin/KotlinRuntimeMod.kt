/*
 * This file is part of KotlinRuntime. A companion mod for Kotlin based mods and plugins.
 *
 * MIT License
 *
 * Copyright (c) 2025 Xpdustry
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
