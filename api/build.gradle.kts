plugins {
    foliascheduler.`library-conventions`
    foliascheduler.`publishing-conventions`
    io.github.goooler.shadow
    `fs-version`
}

dependencies {
    compileOnly(libs.paper)
    compileOnlyApi(libs.jetbrains.annotations)
    compileOnlyApi(libs.lombok)
    annotationProcessor(libs.jetbrains.annotations)
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

        mergeServiceFiles()
    }

    assemble {
        dependsOn(shadowJar)
    }
}
