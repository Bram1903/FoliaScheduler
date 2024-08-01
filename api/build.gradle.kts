plugins {
    foliascheduler.`library-conventions`
    foliascheduler.`publishing-conventions`
    io.github.goooler.shadow
    `fs-version`
}

dependencies {
    api(libs.jetbrains.annotations)
    compileOnly(libs.paper)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}

tasks {
    javadoc {
        mustRunAfter(generateVersionsFile)
    }

    sourcesJar {
        mustRunAfter(generateVersionsFile)
    }

    withType<JavaCompile> {
        dependsOn(generateVersionsFile)
    }

    generateVersionsFile {
        packageName = "com.deathmotion.foliascheduler.internal"
    }

    shadowJar {
        archiveFileName = "${rootProject.name}-${rootProject.ext["versionNoHash"]}.jar"
        archiveClassifier = null

        relocate("org.jetbrains.annotations", "com.deathmotion.foliascheduler.shaded.jetbrains.annotations")
        relocate("org.intellij.lang.annotations", "com.deathmotion.foliascheduler.shaded.intellij.annotations")

        mergeServiceFiles()
    }

    assemble {
        dependsOn(shadowJar)
    }
}
