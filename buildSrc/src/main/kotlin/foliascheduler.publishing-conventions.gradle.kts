plugins {
    `java-library`
    `maven-publish`
    signing
    com.gradleup.nmcp
}

publishing {
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

signing {
    val signingKeyId = System.getenv("SIGNING_KEY_ID")
    val signingKey = System.getenv("SIGNING_KEY")
    useInMemoryPgpKeys(signingKeyId, signingKey, "")
    sign(publishing.publications)
}

nmcp {
    publish("FoliaScheduler") {
        username = System.getenv("MAVEN_USERNAME")
        password = System.getenv("MAVEN_PASSWORD")
        publicationType = "USER_MANAGED"
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