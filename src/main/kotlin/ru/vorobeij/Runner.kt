package ru.vorobeij

import java.io.File

class Runner(
    private val depHandler: DepHandler,
    private val gradleTask: String,
    private val pathToGradleProject: String,
    private val gradleFilePath: String?
) {

    fun run() {
        gradleFilePath?.let(::removeDependenciesFromGradleFile)
            ?: File(pathToGradleProject).walkTopDown()
                .filter { it.isFile && it.name == "build.gradle.kts" }
                .map { it.absolutePath }
                .forEach(::removeDependenciesFromGradleFile)
    }

    private fun removeDependenciesFromGradleFile(
        gradleFilePath: String
    ) {
        val f = File(gradleFilePath)
        val lines = f.readLines()
        val implementations = lines.filter { it.matches("\\s+implementation\\(.*".toRegex()) }
        implementations.forEach { dependency ->
            val original = f.readText()
            val gradleWithoutDependency = original.lines().filter { dependency != it }.joinToString("\n")
            f.writeText(gradleWithoutDependency)
            val buildResult = depHandler.result(gradleFilePath, dependency) {
                runGradleCheck(
                    gradleTask,
                    pathToGradleProject
                )
            }
            if (!buildResult) {
                f.writeText(original)
            }
        }
    }
}