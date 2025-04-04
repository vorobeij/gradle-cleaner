package ru.vorobeij

import java.io.File

class FileCache {

    private val f = File("cache.json")

    fun put(fPath: String, dependency: String, result: Boolean) {
        val existing = readCache()
        existing[key(fPath, dependency)] = result
        f.writeText(json.encodeToString(existing))
    }

    fun get(fPath: String, dependency: String): Boolean? {
        val existing = readCache()
        return existing[key(fPath, dependency)]
    }

    private fun key(fPath: String, dependency: String): String {
        return fPath + ";" + dependency.trim()
    }

    private fun readCache(): MutableMap<String, Boolean> {
        if (!f.exists()) {
            f.writeText(json.encodeToString(mapOf<String, Boolean>()))
        }
        return json.decodeFromString<Map<String, Boolean>>(f.readText()).toMutableMap()
    }
}