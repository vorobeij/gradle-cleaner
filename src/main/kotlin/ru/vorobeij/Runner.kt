package ru.vorobeij

import java.io.File

class Runner(
    private val config: RunnerConfig,
    private val dependenciesRepository: DependenciesRepository
) {

    fun run() {
        config.gradleFilePath?.let(::removeDependenciesFromGradleFile)
            ?: config.gradleFilesRoot?.let(::removeFrom)
            ?: removeFrom(config.pathToGradleProject)
    }

    private fun removeFrom(root: String) {
        File(root).walkTopDown()
            .filter { it.isFile && it.name == "build.gradle.kts" }
            .map { it.absolutePath }
            .forEach(::removeDependenciesFromGradleFile)
    }

    private fun removeDependenciesFromGradleFile(
        gradleFilePath: String
    ) {
        println("removing from $gradleFilePath")
        val f = File(gradleFilePath)
        val lines = f.readLines()
        val implementations = lines
            .filter { line -> line.matches(config.dependencyPattern.toRegex()) }
            .filter { line ->
                config.dependencyPatternExclude?.let { excludePattern ->
                    !line.matches(excludePattern.toRegex())
                } ?: true
            }
        implementations.forEach { dependency ->
            val original = f.readText()
            val gradleWithoutDependency = original.lines().filter { dependency != it }.joinToString("\n")
            f.writeText(gradleWithoutDependency)
            val buildResult = dependenciesRepository.result(gradleFilePath, dependency) {
                runGradleCheck(
                    gradleTasksString = config.gradleTask,
                    pathToGradleProject = config.pathToGradleProject
                )
            }
            if (!buildResult) {
                f.writeText(original)
                println(" revert $gradleFilePath / ${dependency.trim()}")
            } else {
                println("removed $gradleFilePath / ${dependency.trim()}")
            }
        }
    }
}