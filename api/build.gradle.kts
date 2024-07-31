plugins {
    foliascheduler.`library-conventions`
    foliascheduler.`publishing-conventions`
    io.github.goooler.shadow
    `fs-version`
}

dependencies {
    compileOnly(libs.paper)
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
        packageName = "com.deathmotion.foliascheduler.utils"
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
