package ru.vorobeij

import java.io.File
import org.junit.jupiter.api.Test

class RunnerTest {

    @Test
    fun `run local project`() {
        Runner(
            RunnerConfig(
                depHandler = DepHandler(FileCache(File("cache.json"))),
                gradleTask = "app:assembleDebug",
                pathToGradleProject = "/Users/sj/AndroidApps/suby",
                gradleFilePath = null,
                dependencyPattern = """\s+implementation\(.*""",
                dependencyPatternExclude = null,
                gradleFilesRoot = null
            )
        ).run()
    }
}