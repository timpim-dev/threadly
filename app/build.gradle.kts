plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

import java.util.Properties

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) file.inputStream().use { load(it) }
}

android {
    namespace = "com.threadly.felixx.dev"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.threadly.felixx.dev"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
        buildConfigField("String", "GOOGLE_SERVER_CLIENT_ID", "\"${localProperties.getProperty("google_server_client_id", "")}\"")
        manifestPlaceholders["appAuthRedirectScheme"] = "com.threadly.felixx.dev"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
    buildFeatures { compose = true; buildConfig = true }
    packaging {
        resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
        resources.excludes += "META-INF/NOTICE.md"
        resources.excludes += "META-INF/LICENSE.md"
    }
}

ksp { arg("room.schemaLocation", "$projectDir/schemas") }


dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.core)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.work.runtime)
    implementation(libs.androidx.security.crypto)
    implementation(libs.kotlinx.coroutines)
    implementation("net.openid:appauth:0.11.1")
    implementation(libs.angus.mail)
    ksp(libs.androidx.room.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
