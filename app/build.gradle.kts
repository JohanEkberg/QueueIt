plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.devtools.ksp)
    kotlin("kapt")
}

android {
    namespace = "se.johan.queueit"
    compileSdk = 35

    defaultConfig {
        applicationId = "se.johan.queueit"
        minSdk = 34
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        // For Unit test on device
        //testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // For integration test (that uses dagger/hilt)
        testInstrumentationRunner = "se.johan.queueit.HiltTestRunner"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.hilt.android)
    implementation(libs.lottie.compose)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.androidx.palette.ktx)
    ksp(libs.room.compiler)
    annotationProcessor(libs.room.compiler)
    implementation(libs.room.paging)
    implementation(libs.androidx.paging.runtime.ktx)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.paging.compose)
    kapt(libs.hilt.android.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.hilt.android.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    kaptAndroidTest(libs.hilt.android.compiler)
}