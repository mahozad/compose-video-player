plugins {
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
}

group = "ir.mahozad"
version = "0.1.0"

tasks.withType<Test> {
    useJUnitPlatform() // JUnit 5
}

tasks.wrapper {
    gradleVersion = libs.versions.gradle.get()
    networkTimeout = 60_000 // milliseconds
    distributionType = Wrapper.DistributionType.ALL
    validateDistributionUrl = false
}
