plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    id("io.insert-koin.compiler.plugin") version "0.3.0"
}

kotlin {
    androidLibrary {
        namespace = "com.tezov.store.shared"
        compileSdk = 36
        minSdk = 24

        withHostTestBuilder {}
    }
    val xcfName = "SharedKit"
    iosX64 {
        binaries.framework {
            baseName = xcfName
        }
    }
    iosArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }
    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
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
