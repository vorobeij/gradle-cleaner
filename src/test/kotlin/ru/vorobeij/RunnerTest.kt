package ru.vorobeij

import org.junit.jupiter.api.Test

class RunnerTest{

    @Test
    fun `run local project`() {
        Runner(
            depHandler = DepHandler(FileCache("cache.json")),
            gradleTask = "app:assembleDebug",
            pathToGradleProject = "/Users/sj/AndroidStudioProjects/temp/testapp",
            gradleFilePath = null,
        ).run()
    }
}