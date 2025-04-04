plugins {
    kotlin("jvm") version "2.1.20"
    kotlin("plugin.serialization") version "2.1.20"
    application
}

group = "ru.vorobeij"
version = "1.0"

application {
    mainClass = "ru.vorobeij.main.MainKt"
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.gradle.org/gradle/libs-releases") }
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = "ru.vorobeij.main.MainKt"
    }
}

tasks {
    "build" {
        dependsOn(jar)
    }
}

dependencies {
    implementation("org.gradle:gradle-tooling-api:+")
    runtimeOnly("org.slf4j:slf4j-simple:1.7.10")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.6")
//    implementation("org.jetbrains.kotlin:kotlin-reflect:+")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}