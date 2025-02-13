plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "dev.supasintatiyanupanwong.apps.android.cakecuttin"
    compileSdk = 35

    defaultConfig {
        applicationId = "dev.supasintatiyanupanwong.apps.android.cakecuttin"
        minSdk = 21
        targetSdk = 35
        versionCode = 2
        versionName = "1.1.0"
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

    flavorDimensions += "distribution"
    productFlavors {
        create("google") {
            isDefault = true
        }
        create("amazon")
        create("huawei")
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

    "googleImplementation"("com.google.android.gms:play-services-ads-lite:23.6.0")
    "googleImplementation"("com.google.android.gms:play-services-instantapps:18.1.0")
    "googleCompileOnly"("com.huawei.hms:ads-lite:13.4.76.300")

    "huaweiImplementation"("com.huawei.hms:ads-lite:13.4.76.300")
    "huaweiCompileOnly"("com.google.android.gms:play-services-ads-lite:23.6.0")
}

afterEvaluate {
    tasks.configureEach {
        if (name.contains("UnitTest") || name.contains("AndroidTest")) {
            enabled = false
        }
    }
}
