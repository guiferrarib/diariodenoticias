import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import kotlinx.kover.gradle.plugin.dsl.AggregationType
import kotlinx.kover.gradle.plugin.dsl.CoverageUnit


plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    id("co.touchlab.skie") version "0.7.1"
    id("dev.mokkery") version "1.9.24-1.7.0"
    kotlin("plugin.serialization") version "1.9.24"
    alias(libs.plugins.sqlDelight)
    alias(libs.plugins.test.resources)
    alias(libs.plugins.kover)
    alias(libs.plugins.kotest)
    alias(libs.plugins.detekt)
}


val apiKey: String by project

kotlin {

    task("testClasses")

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions.freeCompilerArgs += listOf(
            "-Xmulti-platform",
            "-Xcommon-sources=build/generated/source/buildConfig/commonMain/kotlin"
        )
    }

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    listOf(
        kotlin {

            iosX64 {
                binaries.framework {
                    baseName = "shared"
                    isStatic = true
                    freeCompilerArgs += "-Xbinary=bundleId=com.ia.diariodenoticias"
                }
            }

            iosArm64 {
                binaries.framework {
                    baseName = "shared"
                    isStatic = true
                    freeCompilerArgs += "-Xbinary=bundleId=com.ia.diariodenoticias"
                }
            }

            iosSimulatorArm64 {
                binaries.framework {
                    baseName = "shared"
                    isStatic = true
                    freeCompilerArgs += "-Xbinary=bundleId=com.ia.diariodenoticias"
                }
            }

        }
    )

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.kotlinx.datetime)
            implementation(libs.koin.core)
            implementation(libs.sql.coroutines.extensions)

        }
        androidMain.dependencies {
            implementation(kotlin("test-junit"))
            implementation(libs.androidx.lifecycle.viewmodel.ktx)
            implementation(libs.ktor.client.android)
            implementation(libs.sql.android.driver)
            implementation(libs.sql.sqlite.driver)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.sql.native.driver)
        }
        commonTest.dependencies {
            implementation(kotlin("test-common"))
            implementation(kotlin("test-annotations-common"))
            implementation(libs.kotlin.test)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.server.test.host)
            implementation(libs.ktor.server.negotiation)
            implementation(libs.ktor.client.mock)
            implementation(libs.koin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.turbine.test)
            implementation(libs.kotest)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.load.resources)
        }
    }

}

android {
    namespace = "com.ia.diariodenoticias"
    compileSdk = 34
    defaultConfig {
        minSdk = 26
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    packaging {
        resources {
            excludes += "/META-INF/**"
        }
    }
    buildFeatures {
        buildConfig = true
    }
    defaultConfig {
        buildConfigField("String", "API_KEY", "\"$apiKey\"")
    }
}

kover {
    reports {
        filters {
            excludes {
                classes(
                    "comiadiariodenoticiasdb.Source",
                    "comiadiariodenoticiasdb.Article",
                    "com.ia.diariodenoticias.app.db.DatabaseDriverFactory",
                    "com.ia.diariodenoticias.BuildConfig",
                )
            }
        }
        verify {
            rule("Basic Line Coverage") {
                disabled = false
                bound {
                    minValue = 80 // Minimum coverage percentage
                    maxValue = 100 // Maximum coverage percentage (optional)
                    aggregationForGroup = AggregationType.COVERED_PERCENTAGE
                    coverageUnits.set(CoverageUnit.LINE)
                }
            }

            rule("Branch Coverage") {
                disabled = false
                bound {
                    minValue = 70 // Minimum coverage percentage for branches
                    coverageUnits.set(CoverageUnit.BRANCH)
                    aggregationForGroup = AggregationType.COVERED_PERCENTAGE
                }
            }
        }
    }
}

sqldelight {
    databases {
        create(name = "DiarioDeNoticiasDatabase") {
            packageName.set("com.ia.diariodenoticias.db")
        }
    }
}

detekt {
    toolVersion = "1.23.6"
    buildUponDefaultConfig = true // preconfigure defaults
    allRules = true // activate all available (even unstable) rules.
    config.setFrom("$projectDir/config/detekt.yml") // point to your custom config defining rules to run, overwriting default behavior
    baseline =
        file("$projectDir/config/baseline.xml") // a way of suppressing issues before introducing detekt
}

tasks.withType<Detekt>().configureEach {
    reports {
        html.required.set(true) // observe findings in your browser with structure and code snippets
        xml.required.set(true) // checkstyle like format mainly for integrations like Jenkins
        txt.required.set(true) // similar to the console output, contains issue signature to manually edit baseline files
        sarif.required.set(true) // standardized SARIF format (https://sarifweb.azurewebsites.net/) to support integrations with GitHub Code Scanning
        md.required.set(true) // simple Markdown format
    }
}

// Kotlin DSL
tasks.withType<Detekt>().configureEach {
    jvmTarget = "1.8"
}
tasks.withType<DetektCreateBaselineTask>().configureEach {
    jvmTarget = "1.8"
}

tasks.register("detektAll", Detekt::class) {

    description = "DETEKT build for all modules"
    parallel = true
    ignoreFailures = false
    autoCorrect = true
    buildUponDefaultConfig = true
    setSource(projectDir)
    baseline.set(project.file("$rootDir/shared/config/baseline.xml"))
    config.setFrom(project.file("$rootDir/shared/config/detekt.yml"))
    include("**/*.kt", "**/*.kts")
    exclude("**/resources/**", "**/build/**")

    reports {
        html.required.set(true)
        html.outputLocation.set(project.file("build/reports/detekt.html"))
    }
}

tasks.register<DetektCreateBaselineTask>("detektAllBaseline") {
    description = "Create Detekt baseline on the whole project."
    ignoreFailures.set(false)
    setSource(projectDir)
    config.setFrom(project.file("$rootDir/shared/config/detekt.yml"))
    include("**/*.kt", "**/*.kts")
    exclude("**/resources/**", "**/build/**")
    baseline.set(project.file("$rootDir/shared/config/baseline.xml"))
}