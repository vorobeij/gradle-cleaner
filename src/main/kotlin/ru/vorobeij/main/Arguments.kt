package ru.vorobeij.main

data class Arguments(
    val projectPath: String,
    val taskPrefix: String,
    val cleanCache: Boolean,
    val cacheFilePath: String,
    val filesBelowPath: String?,
    val dependencyPattern: String,
    val gradleFile: String?,
)