plugins {
    `java-library`
    `maven-publish`
}

publishing {
    repositories {
        maven {
            name = "OSSRH"
            url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = System.getenv("MAVEN_USERNAME")
                password = System.getenv("MAVEN_PASSWORD")
            }
        }
    }

    publications {
        create<MavenPublication>("FoliaScheduler") {
            groupId = project.group as String
            artifactId = project.name
            version = rootProject.ext["versionNoHash"] as String
            from(components["java"])

            pom {
                name = "FoliaScheduler"
                description = "A simple to use scheduler, which allows scheduling task on both Bukkit and Folia."
                url = "https://github.com/Bram1903/FoliaScheduler"
                licenses {
                    license {
                        name = "MIT License"
                        url = "https://github.com/Bram1903/FoliaScheduler/blob/main/LICENSE"
                    }
                }
                developers {
                    developer {
                        id = "bram"
                        name = "Bram"
                        email = "bram@bramdekker.com"
                    }
                }
                scm {
                    connection = "scm:git:https://github.com/Bram1903/FoliaScheduler.git"
                    developerConnection = "scm:git:https://github.com/Bram1903/FoliaScheduler.git"
                    url = "https://github.com/Bram1903/FoliaScheduler/tree/main"
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