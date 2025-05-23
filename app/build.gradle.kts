plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.drmiaji.fortyahadith"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.drmiaji.fortyahadith"
        minSdk = 24
        targetSdk = 35
        versionCode = 10
        versionName = "2.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
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
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
}

dependencies {
// Core AndroidX

    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)

    // RecyclerView
    implementation(libs.androidx.recyclerview)

    // ViewModel + LiveData
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Kotlin
    implementation(libs.kotlin.stdlib)

    // Optional: Asynchronous layout inflater (if using)
    implementation(libs.androidx.asynclayoutinflater)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.runtime.android)
    implementation(libs.androidx.navigation.compose.jvmstubs)
    implementation(libs.androidx.ui.test.android)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.firebase.crashlytics.buildtools)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.appcompat)

    // Material Design
    implementation(libs.androidx.material.icons.core)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.gson)
}