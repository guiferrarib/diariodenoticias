plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    id("co.touchlab.skie") version "0.7.1"
    kotlin("plugin.serialization") version "1.9.24"
    alias(libs.plugins.sqlDelight)
}

kotlin {

    task("testClasses")
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    
    listOf(
        kotlin {
            // ...

            iosX64 {
                binaries.framework {
                    baseName = "shared"
                    isStatic = true
                    freeCompilerArgs += "-Xbinary=bundleId=com.ia.diariodenoticias.android"
                }
            }

            iosArm64 {
                binaries.framework {
                    baseName = "shared"
                    isStatic = true
                    freeCompilerArgs += "-Xbinary=bundleId=com.ia.diariodenoticias.android"
                }
            }

            iosSimulatorArm64 {
                binaries.framework {
                    baseName = "shared"
                    isStatic = true
                    freeCompilerArgs += "-Xbinary=bundleId=com.ia.diariodenoticias.android"
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
            implementation(libs.androidx.lifecycle.viewmodel.ktx)
            implementation(libs.ktor.client.android)
            implementation(libs.sql.android.driver)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.sql.native.driver)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
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
}

sqldelight {
    databases {
        create(name = "DiarioDeNoticiasDatabase") {
            packageName.set("com.ia.diariodenoticias.db")
        }
    }
}