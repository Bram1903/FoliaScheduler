plugins {
    foliascheduler.`library-conventions`
    alias(libs.plugins.run.paper)
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(libs.paper)
    //implementation(libs.foliascheduler)
    implementation(project(":api")) // For local testing
}

tasks {
    processResources {
        inputs.property("version", project.version)
        filesMatching(listOf("plugin.yml")) {
            expand("version" to project.version)
        }
    }

    shadowJar {
        archiveFileName = "FoliaSchedulerTestPlugin-${rootProject.ext["versionNoHash"]}.jar"
        archiveClassifier = null

        relocate(
            "com.deathmotion.foliascheduler",
            "com.deathmotion.testfoliascheduler.shaded"
        )

        mergeServiceFiles()
    }

    assemble {
        dependsOn(shadowJar)
    }

    // 1.8.8 - 1.16.5 = Java 8
    // 1.17           = Java 16
    // 1.18 - 1.20.4  = Java 17
    // 1-20.5+        = Java 21
    val version = "1.21"
    val javaVersion = JavaLanguageVersion.of(21)

    val jvmArgsExternal = listOf(
        "-Dcom.mojang.eula.agree=true"
    )

    val sharedBukkitPlugins = runPaper.downloadPluginsSpec {}

    runServer {
        minecraftVersion(version)
        runDirectory = rootDir.resolve("run/paper/$version")

        javaLauncher = project.javaToolchains.launcherFor {
            languageVersion = javaVersion
        }

        downloadPlugins {
            from(sharedBukkitPlugins)
        }

        jvmArgs = jvmArgsExternal
    }

    runPaper.folia.registerTask {
        minecraftVersion(version)
        runDirectory = rootDir.resolve("run/folia/$version")

        javaLauncher = project.javaToolchains.launcherFor {
            languageVersion = javaVersion
        }

        downloadPlugins {
            from(sharedBukkitPlugins)
        }

        jvmArgs = jvmArgsExternal
    }
}