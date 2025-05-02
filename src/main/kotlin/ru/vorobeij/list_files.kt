package ru.vorobeij

import java.io.File
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.required

/**
 * List all cleanup scripts with specific assembleDebug gradle task
 */
fun main(args: Array<String>) {

    val parser = ArgParser("cleaner")
    val projectPath by parser.option(ArgType.String, fullName = "projectPath", description = "project root").required()
    parser.parse(args)

    val f = File("temp.txt")
    runGradleCheck(
        gradleTasksString = "tasks --all",
        pathToGradleProject = projectPath,
        outputStream = f.outputStream()
    )
    val tasks = f.readLines()
        .filter { it.contains(":assembleDebug ") }
        .map { it.split(" - ")[0] }

    File("tasks.txt").writeText(
        tasks.joinToString("\n")
    )


    val gradleFiles = File(projectPath).walkTopDown()
        .filter { it.isFile && it.name == "build.gradle.kts" }
        .toList()

    val commands = gradleFiles.mapIndexedNotNull { index, file ->

        val task = tasks.firstOrNull {
            file.absolutePath.contains(
                it
                    .replace(":assembleDebug", "")
                    .replace(":", "/")
            )
        }
        task?.let {
            listOf(
                """echo cleaning ${index + 1}/${gradleFiles.size}""",
                """
                    java -jar /Users/sj/Development/IdeaProjects/gradle-cleaner/script/gradle-cleaner-1.0.jar \
                    --gradleFilePath $file \
                    --gradleProjectRoot $projectPath \
                    --gradleTask "clean $it" \
                    --cacheFile /Users/sj/Documents/cache.json \
                    --dependencyPattern "\s+implementation\(.*"
                """.trimIndent()
            )
        }
    }.flatten()


    File("cleanup.sh").writeText(commands.joinToString("\n\n"))
}