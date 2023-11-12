import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

group = "ir.mahozad"
version = "1.0-SNAPSHOT"

tasks.withType<Test> {
    useJUnitPlatform() // JUnit 5
}

kotlin {
    // See https://kotlinlang.org/docs/gradle-configure-project.html#gradle-java-toolchains-support
    // and https://docs.gradle.org/current/userguide/toolchains.html
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of("11"))
        // vendor.set(JvmVendorSpec.ORACLE)
    }
}

tasks.wrapper {
    gradleVersion = properties["gradle.version"] as String
    networkTimeout = 60_000 // milliseconds
    distributionType = Wrapper.DistributionType.ALL
    validateDistributionUrl = false
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.materialIconsExtended)
    implementation("uk.co.caprica:vlcj:4.8.2")
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "compose-video-player"
            packageVersion = "1.0.0"
            buildTypes.release.proguard {
                configurationFiles.from("rules.pro")
            }
        }
    }
}
