@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("java-library")
    `kotlin-dsl`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.pluginPublish)
    alias(libs.plugins.mavenPublish)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    testImplementation(gradleTestKit())
    testImplementation(kotlin("test"))
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.kotlin.poet)
}

gradlePlugin {
    plugins {
        create("buildKonfig") {
            id = "dev.tilbrook.konfig"
            implementationClass = "dev.tilbrook.konfig.KonfigPlugin"
        }
    }
}