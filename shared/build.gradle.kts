import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.nativecoroutines)
    id("maven-publish")
}

kotlin {
    val xcf = XCFramework()
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
        publishLibraryVariants("release", "debug")
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "onboarding"
            isStatic = true
            xcf.add(this)
        }
    }

    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlinx.cinterop.ExperimentalForeignApi")
                optIn("kotlin.experimental.ExperimentalObjCName")
            }
        }
        androidMain.dependencies {
            implementation(libs.kmp.koin.android)
            implementation(libs.ktor.client.okhttp)
        }
        commonMain.dependencies {
            implementation(libs.baldomero.napoli.core)
            implementation(libs.baldomero.napoli.common)
            implementation(libs.baldomero.napoli.network)

            implementation(libs.kmp.koin.core)
            implementation(libs.okio)
            implementation(libs.ktor.utils)
            implementation(libs.ktor.client.core)

            implementation(project.dependencies.platform("io.github.jan-tennert.supabase:bom:2.5.4"))
            implementation(libs.gotrue.kt)

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.baldomero.napoli.supabase.auth"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}


publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.baldomero.napoli.supabase"
            artifactId = "auth"
            version = "1.0.0"

            afterEvaluate {
                from(components["kotlin"])
            }
        }
    }
}
