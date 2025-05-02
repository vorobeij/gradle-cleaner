package ru.vorobeij

import java.io.File
import java.io.OutputStream
import org.gradle.tooling.GradleConnector

fun runGradleCheck(
    gradleTasksString: String,
    pathToGradleProject: String,
    outputStream: OutputStream? = null
): Boolean {
    try {
        GradleConnector.newConnector()
            .forProjectDirectory(File(pathToGradleProject))
            .connect().use { connection ->
                val buildLauncher = connection.newBuild()
                buildLauncher.forTasks(*gradleTasksString.split(" ").toTypedArray())
                buildLauncher.setStandardOutput(outputStream)
                buildLauncher.run()
                return true
            }
    } catch (e: Exception) {
        return false
    }
}
