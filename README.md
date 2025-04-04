# Remove unused dependencies from project's gradle files

## Instructions
```shell
java -jar build/libs/gradle-deps-cleaner-1.0.jar \
-gradleFilePath=/Users/sj/AndroidStudioProjects/temp/testapp/app/build.gradle.kts \
-gradleProject=/Users/sj/AndroidStudioProjects/temp/testapp/ \
-gradleTask=app:assembleDebug \
-cacheFile=~/Downloads/cache.json
```