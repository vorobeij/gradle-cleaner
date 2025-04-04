package ru.vorobeij

import java.io.File
import kotlinx.serialization.json.Json

val gradleFilePath = "/Users/sj/AndroidStudioProjects/temp/testapp/app/build.gradle.kts"

val json = Json {
    prettyPrint = true
}

fun main() {

    runFile(gradleFilePath)
}

private fun runFile(path: String) {
    val f = File(path)
    val lines = f.readLines()
    val implementations = lines.filter { it.matches("\\s+implementation\\(.*".toRegex()) }
    implementations.forEach { dependency ->
        val original = f.readText()
        val gradleWithoutDependency = original.lines().filter { dependency != it }.joinToString("\n")
        f.writeText(gradleWithoutDependency)
        val buildResult = DepHandler().result(gradleFilePath, dependency, ::runGradleCheck)
        if (!buildResult) {
            f.writeText(original)
        }
    }
}
