# Remove unused dependencies from project's gradle files

Removes unused dependencies from gradle files:

1. Launches ./gradlew tasks | grep **taskPrefix** - gets list of all modules to check for unused dependencies
2. Finds all the build.gradle.kts in project
3. For each module's build.gradle.kts
   - removes lines matched **dependencyPattern**
   - runs **gradleTask** from step 1 (for example, `:submodule:assemble` or `:submodule:assembleDebug`)
   - revert changes if build failed
4. As build tasks take a while, file cache is used, stored in `cacheFile`

## Instructions

[gradle-cleaner-1.0.jar](build%2Flibs%2Fgradle-cleaner-1.0.jar)
```shell
java -jar /Users/sj/Development/IdeaProjects/gradle-cleaner/script/gradle-cleaner-1.0.jar \
    --projectPath /Users/sj/BackendApps/s-backend \
    --taskPrefix assemble \
    --cacheFile /Users/sj/Documents/cache.json \
    --filesBelowPath /Users/sj/BackendApps/s-backend/submodule
```