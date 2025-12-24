plugins {
    autowire(libs.plugins.android.application)
    autowire(libs.plugins.kotlin.android)
    autowire(libs.plugins.kotlin.compose)
}

android {
    namespace = property.project.samples.demo.android.packageName
    compileSdk = property.project.android.compileSdk

    defaultConfig {
        applicationId = property.project.samples.demo.android.packageName
        minSdk = property.project.android.minSdk
        targetSdk = property.project.android.targetSdk
        versionName = property.project.samples.demo.android.versionName
        versionCode = property.project.samples.demo.android.versionCode
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    lint { checkReleaseBuilds = false }
}


kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(projects.yukireflectionCore)
    implementation(platform(androidx.compose.compose.bom))
    implementation(androidx.compose.foundation.foundation)
    implementation(androidx.compose.ui.ui)
    implementation(androidx.compose.material3.material3)
    implementation(androidx.activity.activity.compose)
    implementation(androidx.core.core.ktx)
    implementation(androidx.appcompat.appcompat)
    implementation(com.google.android.material.material)
    testImplementation(junit.junit)
    androidTestImplementation(androidx.test.ext.junit)
    androidTestImplementation(androidx.test.espresso.espresso.core)
}