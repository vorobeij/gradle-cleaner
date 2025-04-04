package ru.vorobeij.main

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.required
import ru.vorobeij.DepHandler
import ru.vorobeij.FileCache
import ru.vorobeij.Runner

fun main(args: Array<String>) {
    val parser = ArgParser("example")
    val gradleFilePath by parser.option(
        ArgType.String,
        fullName = "gradleFilePath",
        description = "Path to gradle file, if only one. If not mentioned, all files are checked"
    )
    val gradleProject by parser.option(
        ArgType.String,
        fullName = "gradleProjectRoot",
        description = "Path to gradle project"
    ).required()
    val gradleTask by parser.option(
        ArgType.String,
        fullName = "gradleTask",
        description = "gradle task to run to check, usually build"
    ).required()
    val cacheFile by parser.option(
        ArgType.String,
        fullName = "cacheFile",
        description = "Path to cache file. Builds could be long to run"
    )
    parser.parse(args)

    Runner(
        depHandler = DepHandler(FileCache(cacheFile ?: "cache.json")),
        gradleTask = gradleTask,
        pathToGradleProject = gradleProject,
        gradleFilePath = gradleFilePath,
    ).run()
}
