package ru.vorobeij

import java.io.File
import kotlinx.serialization.json.Json

class FileCache(
    private val cacheFile: File
) {

    private val json = Json {
        prettyPrint = true
    }

    fun put(fPath: String, dependency: String, result: Boolean) {
        val existing = readCache()
        existing[key(fPath, dependency)] = result
        cacheFile.writeText(json.encodeToString(existing))
    }

    fun get(fPath: String, dependency: String): Boolean? {
        val existing = readCache()
        println("Already removed ${existing.filter { it.value }.size} dependencies")
        return existing[key(fPath, dependency)]
    }

    private fun key(fPath: String, dependency: String): String {
        return fPath + ";" + dependency.trim()
    }

    private fun readCache(): MutableMap<String, Boolean> {
        if (!cacheFile.exists() || cacheFile.readText().isEmpty()) {
            cacheFile.writeText(json.encodeToString(mapOf<String, Boolean>()))
        }
        return json.decodeFromString<Map<String, Boolean>>(cacheFile.readText()).toMutableMap()
    }
}