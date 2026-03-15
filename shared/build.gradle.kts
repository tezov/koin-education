import kotlin.collections.plus

plugins {
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.koinCompiler)
    id("org.jetbrains.kotlin.plugin.allopen") version libs.versions.kotlin
    id("dev.mokkery") version "3.3.0-SNAPSHOT"
}

koinCompiler {
//    userLogs = true
//    debugLogs = true
//    dslSafetyChecks = true
}

kotlin {
    val xcfName = "SharedKit"
    val namespace = "com.tezov.store.shared"

    android {
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

                implementation(libs.koin.compose)
                implementation(libs.koin.compose.annotation)
                implementation(libs.koin.compose.viewmodel)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.kotlin.test.coroutine)
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

allOpen {
    annotation("${kotlin.android.namespace}.annotation.OpenForTest")
}

mokkery {
    with(stubs) {
        allowConcreteClassInstantiation = true
        allowClassInheritance = true
    }
}
