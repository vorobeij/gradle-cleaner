package ru.vorobeij

data class RunnerConfig(
    val depHandler: DepHandler,
    val gradleTask: String,
    val pathToGradleProject: String,
    val gradleFilePath: String?,
    val dependencyPattern: String,
    val dependencyPatternExclude: String?,
)