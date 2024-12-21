plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android) version "1.9.10"
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.passwordsapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.passwordsapp"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.espresso.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    // DI Hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-compiler:2.51.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0-alpha01")

    // Room DB
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    // Biometric auth
    implementation("androidx.biometric:biometric:1.1.0")

    // Material Icons
    implementation("androidx.compose.material:material-icons-extended:1.7.5")

    // Loading LazyColumn animation
    implementation("com.valentinilk.shimmer:compose-shimmer:1.0.3")

    // Navigation and Compose
    implementation("androidx.navigation:navigation-compose:2.8.5")
    implementation("androidx.compose.material3:material3:1.0.1")
    implementation("androidx.compose.material:material:1.7.6")
    implementation("com.google.accompanist:accompanist-navigation-animation:0.28.0")

    //password check
    implementation("com.nulab-inc:zxcvbn:1.3.3")
    // okhttp
    implementation("com.squareup.okhttp3:okhttp:3.2.0")
    implementation("com.squareup.okio:okio:3.0.0")
}