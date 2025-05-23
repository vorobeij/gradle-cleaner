package ru.vorobeij

data class RunnerConfig(
    val gradleTask: String,
    val pathToGradleProject: String,
    val gradleFilePath: String,
    val dependencyPattern: String,
    val dependencyPatternExclude: String?,
    val gradleFilesRoot: String?,
)