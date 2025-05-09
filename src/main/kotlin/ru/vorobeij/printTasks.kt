package ru.vorobeij

import java.io.File

fun printTasks(modules: List<RunnerConfig>, cacheFile: String) {
    val text = modules.flatMapIndexed { index, config ->
        listOf(
            """echo cleaning ${index + 1}/${modules.size}""",
            """
                java -jar /Users/sj/Development/IdeaProjects/gradle-cleaner/script/gradle-cleaner-1.0.jar \
                --gradleFilePath ${config.gradleFilePath} \
                --gradleProjectRoot ${config.gradleFilesRoot} \
                --gradleTask "${config.gradleTask}" \
                --cacheFile $cacheFile \
                --dependencyPattern "${config.dependencyPattern}"
            """.trimIndent()
        )
    }.joinToString("\n\n")
    File("cleanup.sh").writeText(text)
}
