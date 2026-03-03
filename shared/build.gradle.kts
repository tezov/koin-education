import kotlin.collections.plus

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    id("io.insert-koin.compiler.plugin") version "0.3.0"
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
                // Should be replaced with declaration from lib, but I'm lazy to do it in this project
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)

                implementation("io.insert-koin:koin-compose:4.2.0-RC1")
                implementation("io.insert-koin:koin-annotations:4.2.0-RC1")
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
