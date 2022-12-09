import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.compose.jetbrainsCompose

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

group = "ir.mahozad"
version = "1.0-SNAPSHOT"

repositories {
    google()
    jcenter()
    mavenCentral()
    jetbrainsCompose()
}

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
    distributionType = Wrapper.DistributionType.ALL
    gradleVersion = "7.5.1"
}

dependencies {
    implementation(compose.desktop.currentOs)
    // Or org.bytedeco:javacv-platform for cross-platform usage
    implementation("org.bytedeco:javacv:1.5.8")
    implementation("org.bytedeco:ffmpeg:5.1.2-1.5.8") // The main artifact
    // Brings in all FFmpeg artifacts.
    // TODO: When publishing our library for a specific platform,
    //  somehow only include the FFmpeg JAR for that platform in our publishing.
    // implementation("org.bytedeco:ffmpeg-platform:5.1.2-1.5.8")
    implementation("org.bytedeco:ffmpeg:5.1.2-1.5.8:windows-x86_64") // Platform-specific artifact

    @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
    testImplementation(compose.uiTestJUnit4)
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
    // Include the Vintage engine to be able to run JUnit 4 tests as well
    testImplementation("org.junit.vintage:junit-vintage-engine:5.9.1")
    testImplementation("org.assertj:assertj-core:3.23.1")
    testImplementation("io.mockk:mockk:1.13.2")
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "compose-video-player2"
            packageVersion = "1.0.0"
        }
    }
}
