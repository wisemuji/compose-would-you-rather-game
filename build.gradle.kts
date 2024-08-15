plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.spotless)
}

subprojects {
    apply(plugin = rootProject.libs.plugins.spotless.get().pluginId)

    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        kotlin {
            target("**/*.kt")
            targetExclude("$buildDir/**/*.kt")
            licenseHeaderFile(rootProject.file("spotless/copyright.kt"))
            trimTrailingWhitespace()
            endWithNewline()
        }
    }
}
