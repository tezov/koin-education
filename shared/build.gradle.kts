import kotlin.collections.plus

plugins {
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.koinCompiler)
    jacoco
    id("org.jetbrains.kotlin.plugin.allopen") version libs.versions.kotlin.get()
    id("dev.mokkery") version "3.3.0"
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
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

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
                implementation("dev.mokkery:mokkery-coroutines:3.3.0")
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

jacoco {
    toolVersion = "0.8.14"
    tasks.register<Delete>("cleanCoverageFolder") {
        group = "other"
        description = "Delete root coverage folder"
        delete(layout.buildDirectory.dir("reports/jacoco/"))
    }
    tasks.register("coveragePostProcessReport") {
        val reportDir = layout.buildDirectory.dir("reports/jacoco/html")
        doFirst {
            val filterHtml = """
                <div style="padding:10px;">
                    <label for="elementsFilter">Filter:</label>
                    <input type="text" id="elementsFilter" onkeyup="filterElements()" placeholder="enter element name...">
                </div>
                <script>
                    function filterElements() {
                        var input = document.getElementById("elementsFilter");
                        var filter = input.value.toLowerCase();
                        var rows = document.querySelectorAll("table tr");
                        rows.forEach(function(row) {
                            var nameCell = row.querySelector("td a.el_package, td a.el_class");
                            if (nameCell) {
                                row.style.display = nameCell.textContent.toLowerCase().includes(filter) ? "" : "none";
                            }
                        });
                    }
                </script>
            """.trimIndent()
            val darkThemeCss = """
                <style>
                    body {
                        background-color: #0b0c0d;
                        color: #d1d5db;
                        font-family: "Inter", sans-serif;
                        margin: 0;
                        padding: 0;
                    }
                    table {
                        width: 100%;
                        border-collapse: collapse;
                        background-color: #131416;
                        color: #d1d5db;
                    }
                    th, td {
                        border: 1px solid #2c2c2c !important;
                        padding: 6px 8px;
                    }
                    thead td {
                        background-color: #1f1f21 !important; /* dark header background */
                        color: #d1d5db;
                    }
                    tbody tr:nth-child(even) {
                        background-color: #1a1a1c; /* alternate row */
                    }
                    tbody tr:nth-child(odd) {
                        background-color: #131416;
                    }
                    td {
                        color: #d1d5db;
                    }
                    a {
                        color: #ffffff !important;
                        text-decoration: none;
                    }
                    input, select, textarea {
                        background-color: #1f1f21;
                        color: #d1d5db;
                        border: 1px solid #2c2c2c;
                    }
                    label {
                        color: #d1d5db;
                    }
                </style>
            """.trimIndent()
            val darkCodeCss = """
            <style>
                body {
                    background-color: #d1d5db !important;
                    color: #d1d5db !important;
                }
            </style>
        """.trimIndent()
            reportDir.get().asFile.walkTopDown().filter { it.isFile && it.extension == "html" }.forEach { file ->
                var html = file.readText(Charsets.UTF_8)
                val bodyTagRegex = Regex("<body.*?>")
                val bodyTag = bodyTagRegex.find(html)?.value ?: "<body>"
                val injection = when {
                    file.name.endsWith(".kt.html") -> "$bodyTag\n$darkCodeCss"
                    file.name == "index.html" -> "$bodyTag\n$darkThemeCss\n$filterHtml"
                    else -> "$bodyTag\n$darkThemeCss"
                }
                html = html.replaceFirst(bodyTagRegex, injection)
                file.writeText(html, Charsets.UTF_8)
            }
        }
    }
    tasks.register("allTestsAndCoverage", JacocoReport::class.java) {
        group = "verification"
        description = "Generate jacoco report"

        // report depends on test task
        val unitTestTask = tasks.withType<Test>().first {
            it.name.contains("testAndroidHostTest")
        }
        dependsOn(unitTestTask)
        dependsOn(tasks.named("cleanCoverageFolder"))
        finalizedBy(tasks.named("coveragePostProcessReport"))

        // jacoco report config
        executionData(unitTestTask)
        classDirectories.setFrom(
            fileTree("${layout.buildDirectory.get().asFile}/classes/kotlin/android/main")
        )
        sourceDirectories.setFrom(files("$projectDir/src/commonMain/kotlin"))
        reports {
            html.required.set(true)
            html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/html"))
            xml.required.set(true)
            xml.outputLocation.set(layout.buildDirectory.file("reports/jacoco/coverage.xml"))
        }
    }
}
