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
import ru.vorobeij.printTasks

object Main {

    /**
     * List all cleanup scripts with specific assembleDebug gradle task
     */
    @JvmStatic
    fun main(args: Array<String>) {

        val parser = ArgParser("cleaner")
        val projectPath by parser.option(
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
        parser.parse(args)

        val cacheFile = File(cacheFilePath ?: "cache.json")
        if (cleanCache == true) cacheFile.delete()
        val dependenciesRepository = DependenciesRepository(FileCache(cacheFile))

        val modules: List<RunnerConfig> = detectModules(projectPath, taskPrefix)

        modules.forEach { module ->
            Runner(config = module, dependenciesRepository = dependenciesRepository).run()
        }
    }
}
