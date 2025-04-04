package ru.vorobeij

import java.io.File
import org.gradle.tooling.GradleConnector

fun runGradleCheck(
    gradleTask: String,
    pathToGradleProject: String
): Boolean {
    try {
        GradleConnector.newConnector()
            .forProjectDirectory(File(pathToGradleProject))
            .connect().use { connection ->
                val buildLauncher = connection.newBuild()
                buildLauncher.forTasks(gradleTask)
                buildLauncher.setStandardOutput(null)
                buildLauncher.run()
                return true
            }
    } catch (e: Exception) {
        return false
    }
}
