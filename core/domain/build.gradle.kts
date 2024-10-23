plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    // core
    implementation(libs.kotlinx.coroutines.core)

    // koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
}