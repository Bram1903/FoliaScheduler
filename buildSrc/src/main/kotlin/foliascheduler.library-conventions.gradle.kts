plugins {
    `java-library`
    `maven-publish`
    io.github.goooler.shadow
}

group = rootProject.group
version = rootProject.version
description = project.description

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

java {
    withSourcesJar()
    withJavadocJar()
    disableAutoTargetJvm()

    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

tasks {
    withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.release = 8
    }

    jar {
        archiveClassifier = "default"
    }

    defaultTasks("build")
}

configurations.implementation.get().extendsFrom(configurations.shadow.get())