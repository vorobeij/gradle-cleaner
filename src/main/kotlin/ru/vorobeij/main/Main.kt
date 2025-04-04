package ru.vorobeij.main

import java.io.File
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.required
import ru.vorobeij.DepHandler
import ru.vorobeij.FileCache
import ru.vorobeij.Runner
import ru.vorobeij.RunnerConfig

fun main(args: Array<String>) {
    val parser = ArgParser("dependencies-cleaner")
    val cleanCache by parser.option(
        ArgType.Boolean,
        fullName = "cleanCache",
        description = "Clean cache before launch"
    )
    val dependencyPattern by parser.option(
        ArgType.String,
        fullName = "dependencyPattern",
        description = "Regex to include lines from gradle file"
    )
    val dependencyPatternExcludes by parser.option(
        ArgType.String,
        fullName = "dependencyPatternExcludes",
        description = "Regex to exclude lines from gradle file"
    )
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
    val cacheFilePath by parser.option(
        ArgType.String,
        fullName = "cacheFile",
        description = "Path to cache file. Builds could be long to run"
    )
    parser.parse(args)

    val cacheFile = File(cacheFilePath ?: "cache.json")
    if (cleanCache == true) cacheFile.delete()
    val config = RunnerConfig(
        depHandler = DepHandler(FileCache(cacheFile)),
        gradleTask = gradleTask,
        pathToGradleProject = gradleProject,
        gradleFilePath = gradleFilePath,
        dependencyPattern = dependencyPattern ?: """\s+implementation\(.*""",
        dependencyPatternExclude = dependencyPatternExcludes,
    )
    Runner(config).run()
}
