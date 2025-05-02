#!/bin/zsh

cd ..
./gradlew fatJar

cp ./build/libs/*.jar ./script