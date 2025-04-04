# Remove unused dependencies from project's gradle files

Removes unused dependencies from gradle files:

1. removing lines with a pattern `dependencyPattern`
2. run gradle task `gradleTask`
3. save result to cache in `cacheFile`
4. Revert gradle file if build failed

## Instructions
```shell
java -jar build/libs/gradle-deps-cleaner-1.0.jar \
-gradleFilePath=/Users/sj/AndroidStudioProjects/temp/testapp/app/build.gradle.kts \
-gradleProject=/Users/sj/AndroidStudioProjects/temp/testapp/ \
-gradleTask=app:assembleDebug \
-cacheFile=~/Downloads/cache.json \
dependencyPattern=\s+implementation\(project.*
```