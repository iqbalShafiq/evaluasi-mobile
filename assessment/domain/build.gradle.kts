plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    // other modules
    implementation(project(":core:domain"))

    // coroutines
    implementation(libs.kotlinx.coroutines.core)
}