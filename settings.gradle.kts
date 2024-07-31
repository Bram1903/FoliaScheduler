plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "FoliaScheduler"
include(":api")

if (System.getenv("CI") == null) {
    include(":test-plugin")
}
