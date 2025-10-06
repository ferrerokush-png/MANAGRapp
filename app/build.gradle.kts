import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

// Load secrets from local.properties (for personal use only)
val localProps = Properties().apply {
    val f = rootProject.file("local.properties")
    if (f.exists()) {
        load(f.inputStream())
    }
}
val GEMINI_API_KEY: String = (localProps.getProperty("GEMINI_API_KEY") ?: "")

android {
    namespace = "com.managr.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.managr.app"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"
        buildConfigField("String", "GEMINI_API_KEY", "\"${GEMINI_API_KEY}\"")
        
        ndk {
            abiFilters += listOf("arm64-v8a", "armeabi-v7a", "x86_64", "x86")
        }
    }
    
    packaging {
        jniLibs {
            useLegacyPackaging = false
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // Enable code obfuscation for security
            isDebuggable = false
        }
        debug {
            // Disable obfuscation for easier debugging
            isMinifyEnabled = false
            isDebuggable = true
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    // Kotlin 2.0 requires Compose Compiler 1.6.x
    composeOptions {
        kotlinCompilerExtensionVersion = "1.6.11"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}


dependencies {
    // Core modules
    implementation(project(":core:design"))
    implementation(project(":core:data"))
    implementation(project(":core:domain"))
    
    // Feature modules
    implementation(project(":feature:projects"))
    implementation(project(":feature:calendar"))
    implementation(project(":feature:analytics"))
    implementation(project(":feature:promotions"))
    implementation(project(":feature:assistant"))
    
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.1")

    // Use a modern Compose BOM to align UI libs
    implementation(platform("androidx.compose:compose-bom:2024.09.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.navigation:navigation-compose:2.7.4")
    implementation("io.coil-kt:coil-compose:2.5.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // DataStore for local settings and API keys (personal only)
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Networking: Retrofit + OkHttp
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // Gemini (Google Generative AI)
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")

    // Room database with KSP
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // ViewModel + Lifecycle for Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

    // Hilt DI (aligned with Gradle plugin)
    implementation("com.google.dagger:hilt-android:2.52")
    ksp("com.google.dagger:hilt-compiler:2.52")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // WorkManager
    implementation("androidx.work:work-runtime-ktx:2.9.1")
    
    // Security: Jetpack Security for encryption
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
    
    // Security: Biometric authentication
    implementation("androidx.biometric:biometric:1.2.0-alpha05")
    
    // Security: Certificate Pinning
    implementation("com.squareup.okhttp3:okhttp-tls:4.12.0")
    
    // Security: SafetyNet for device integrity
    implementation("com.google.android.gms:play-services-safetynet:18.0.1")

    // Performance & Memory
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.14")
    
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk:1.13.12")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.09.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    androidTestImplementation("io.mockk:mockk-android:1.13.12")

    // Removed third-party Compose Calendar to keep builds stable by default
    // implementation("com.kizitonwose.calendar:compose:2.5.0")
    // implementation("com.kizitonwose.calendar:core:2.5.0")
}