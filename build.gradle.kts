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

val fatJar = task("fatJar", type = Jar::class) {
    archiveBaseName.set("gradle-cleaner")
    manifest {
        attributes["Main-Class"] = "ru.vorobeij.main.Main"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory()) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
    with(tasks.jar.get() as CopySpec)
}

tasks {
    "assemble" {
        dependsOn(fatJar)
    }
}