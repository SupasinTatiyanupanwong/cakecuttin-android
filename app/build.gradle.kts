plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "dev.supasintatiyanupanwong.apps.android.cakecuttin"
    compileSdk = 35

    defaultConfig {
        applicationId = "dev.supasintatiyanupanwong.apps.android.cakecuttin"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        debug {
            isDebuggable = true

            isMinifyEnabled = !isDebuggable
            isShrinkResources = isMinifyEnabled
        }
        release {
            isDebuggable = false

            isMinifyEnabled = !isDebuggable
            isShrinkResources = isMinifyEnabled

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    configurations.all {
        exclude(group = "androidx.appcompat", module = "appcompat")
        exclude(group = "androidx.activity", module = "activity")
        exclude(group = "androidx.fragment", module = "fragment")
        exclude(group = "androidx.camera", module = "camera-video")
        exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core")
        exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-android")
    }
}

dependencies {
    implementation("androidx.camera:camera-camera2:1.4.1")
    implementation("androidx.camera:camera-lifecycle:1.4.1")
    implementation("androidx.camera:camera-view:1.4.1")

    implementation("com.google.android.gms:play-services-ads-lite:23.6.0")
    implementation("com.google.android.gms:play-services-instantapps:18.1.0")
}

tasks.withType<Test> {
    enabled = false
}
