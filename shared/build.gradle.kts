import kotlin.collections.plus

plugins {
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    id("io.insert-koin.compiler.plugin") version "0.4.0"
}

koinCompiler {
//    userLogs = true
//    debugLogs = true
//    dslSafetyChecks = true
}

kotlin {
    val xcfName = "SharedKit"
    val namespace = "com.tezov.store.shared"

    androidLibrary {
        this.namespace = namespace
        compileSdk = 36
        minSdk = 24

        withHostTestBuilder {}
    }
    listOf(iosArm64(), iosSimulatorArm64()).forEach {
        it.binaries.framework {
            isStatic = true
            baseName = xcfName
            freeCompilerArgs += listOf(
                "-Xbinary=bundleId=$namespace",
            )
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinx.serialization.core)
                implementation(libs.compose.runtime)
                implementation(libs.compose.ui)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)

                implementation(libs.compose.navigation3.ui)
                implementation(libs.compose.navigation3.adaptive)
                implementation(libs.compose.navigation3.lifecycle)

                implementation(libs.compose.material.icons.core)

                implementation("io.insert-koin:koin-compose:4.2.0-RC2")
                implementation("io.insert-koin:koin-annotations:4.2.0-RC1")
                implementation("io.insert-koin:koin-compose-viewmodel:4.2.0-RC1")
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependencies {

            }
        }

        iosMain {
            dependencies {

            }
        }
    }
}
