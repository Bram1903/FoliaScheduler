plugins {
    `java-library`
    `maven-publish`
    signing
    com.gradleup.nmcp
}

publishing {
    publications {
        create<MavenPublication>("FoliaScheduler") {
            groupId = "com.deathmotion"
            artifactId = rootProject.name
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

signing {
    useGpgCmd()
    sign(publishing.publications)
}

nmcp {
    if (System.getenv("CI") == null) {
        publish("FoliaScheduler") {
            username = properties["MAVEN_USERNAME"] as String
            password = properties["MAVEN_PASSWORD"] as String
            publicationType = "USER_MANAGED"
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