package ru.vorobeij.main

import java.io.File
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.required
import ru.vorobeij.DependenciesRepository
import ru.vorobeij.FileCache
import ru.vorobeij.Runner
import ru.vorobeij.RunnerConfig
import ru.vorobeij.detectModules

object Main {

    /**
     * List all cleanup scripts with specific assembleDebug gradle task
     */
    @JvmStatic
    fun main(args: Array<String>) {

        val arguments = readArgs(args)

        val cacheFile = File(arguments.cacheFilePath)
        if (arguments.cleanCache) cacheFile.delete()
        val dependenciesRepository = DependenciesRepository(FileCache(cacheFile))

        val modules = detectModules(
            projectPath = arguments.projectPath,
            taskPrefix = arguments.taskPrefix,
            dependencyRegex = arguments.dependencyPattern,
        ).filterModulesBy(arguments.filesBelowPath)

        log(modules)

        modules.forEach { module ->
            Runner(
                config = module,
                dependenciesRepository = dependenciesRepository
            ).run()
        }
    }

    private fun readArgs(args: Array<String>): Arguments {
        val parser = ArgParser("cleaner")
        val projectPath: String by parser.option(
            ArgType.String,
            fullName = "projectPath",
            description = "project root"
        ).required()
        val taskPrefix by parser.option(
            ArgType.String,
            fullName = "taskPrefix",
            description = "task prefix, ex assembleDebug or assemble"
        ).required()
        val cleanCache by parser.option(
            ArgType.Boolean,
            fullName = "cleanCache",
            description = "Clean cache before launch"
        )
        val cacheFilePath by parser.option(
            ArgType.String,
            fullName = "cacheFile",
            description = "Path to cache file. Builds could be long to run"
        )
        val dependencyPattern by parser.option(
            ArgType.String,
            fullName = "dependencyPattern",
            description = "Regex to get dependencies"
        )
        val filesBelowPath: String? by parser.option(
            ArgType.String,
            fullName = "filesBelowPath",
            description = "Root for your team's module, submodule of the project"
        )
        parser.parse(args)
        return Arguments(
            projectPath = projectPath,
            taskPrefix = taskPrefix,
            cleanCache = cleanCache ?: false,
            cacheFilePath = cacheFilePath ?: "cache.json",
            filesBelowPath = filesBelowPath,
            dependencyPattern = dependencyPattern ?: """\s+implementation\(.*"""
        )
    }
}

private fun log(modules: List<RunnerConfig>) {
    println("Remove unused dependencies from:")
    modules.forEach {
        println("${it.gradleFilePath} ${it.gradleTask}")
    }
}

private fun List<RunnerConfig>.filterModulesBy(
    filesBelowPath: String?
): List<RunnerConfig> = if (filesBelowPath != null) {
    filter {
        it.gradleFilePath.contains(filesBelowPath)
    }
} else this
