package ru.vorobeij

class DepHandler {

    private val cache = FileCache()

    fun result(fPath: String, dependency: String, block: () -> Boolean): Boolean {
        val res = cache.get(fPath, dependency) ?: block()
        cache.put(fPath, dependency, res)
        return res
    }
}