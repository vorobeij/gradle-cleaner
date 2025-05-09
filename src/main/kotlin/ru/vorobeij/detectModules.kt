package ru.vorobeij

import java.io.File

fun detectModules(
    projectPath: String,
    taskPrefix: String
): List<RunnerConfig> {

    val tempTxt = File("temp.txt")
    tempTxt.writeText("")
    runGradleCheck(
        gradleTasksString = "tasks --all",
        pathToGradleProject = projectPath,
        outputStream = tempTxt.outputStream()
    )
    val tasks = tempTxt.readLines()
        .filter { it.contains(":$taskPrefix ") }
        .map { it.split(" - ")[0] }
    tempTxt.delete()

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
                    .replace(":$taskPrefix", "")
                    .replace(":", "/")
            )
        }

        task?.let {
            RunnerConfig(
                gradleTask = it,
                pathToGradleProject = projectPath,
                gradleFilePath = file.absolutePath,
                dependencyPattern = """\s+implementation\(.*""",
                dependencyPatternExclude = null,
                gradleFilesRoot = null,
            )
        }
    }

    return commands
}