import java.io.ByteArrayOutputStream

val fullVersion = "1.0.0"
val snapshot = true

group = "com.deathmotion.foliascheduler"
description = rootProject.name

fun getVersionMeta(includeHash: Boolean): String {
    if (!snapshot) {
        return ""
    }
    var commitHash = ""
    if (includeHash && file(".git").isDirectory) {
        val stdout = ByteArrayOutputStream()
        exec {
            commandLine("git", "rev-parse", "--short", "HEAD")
            standardOutput = stdout
        }
        commitHash = "+${stdout.toString().trim()}"
    }
    return "$commitHash-SNAPSHOT"
}

version = "$fullVersion${getVersionMeta(true)}"
ext["versionNoHash"] = "$fullVersion${getVersionMeta(false)}"

tasks {
    wrapper {
        gradleVersion = "8.9"
        distributionType = Wrapper.DistributionType.ALL
    }

    fun subModuleTasks(taskName: String): List<Task> {
        return subprojects
            .filter { it.name != "platforms" }
            .mapNotNull { it.tasks.findByName(taskName) }
    }

    register("build") {
        val subModuleBuildTasks = subModuleTasks("build")
        dependsOn(subModuleBuildTasks)
        group = "build"

        doLast {
            val buildOut = project.layout.buildDirectory.dir("libs").get().asFile.apply {
                if (!exists()) mkdirs()
            }

            subprojects.forEach { subproject ->
                val subIn = subproject.layout.buildDirectory.dir("libs").get().asFile
                if (subIn.exists()) {
                    copy {
                        from(subIn) {
                            include("FoliaScheduler-*.jar")
                            exclude("*-javadoc.jar", "*-sources.jar")
                        }
                        into(buildOut)
                    }
                }
            }
        }
    }

    register<Delete>("clean") {
        val cleanTasks = subModuleTasks("clean")
        dependsOn(cleanTasks)
        group = "build"
        delete(rootProject.layout.buildDirectory)
    }

    defaultTasks("build")
}