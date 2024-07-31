plugins {
    `java-library`
    `maven-publish`
}

publishing {
    publications {
        create<MavenPublication>("FoliaScheduler") {
            groupId = project.group as String
            artifactId = project.name
            version = rootProject.ext["versionNoHash"] as String
            from(components["java"])

            pom {
                name = rootProject.name
                description = rootProject.description
                url = "https://github.com/Bram1903/FoliaScheduler"

                scm {
                    connection = "scm:git:https://github.com/Bram1903/FoliaScheduler.git"
                    developerConnection = "scm:git:https://github.com/Bram1903/FoliaScheduler.git"
                    url = "https://github.com/Bram1903/FoliaScheduler"
                }
            }
        }
    }
}

// So that SNAPSHOT is always the latest SNAPSHOT
configurations.all {
    resolutionStrategy.cacheDynamicVersionsFor(0, TimeUnit.SECONDS)
}

val taskNames = gradle.startParameter.taskNames
if (taskNames.any { it.contains("build") }
    && taskNames.any { it.contains("publish") }) {
    throw IllegalStateException("Cannot build and publish at the same time.")
}