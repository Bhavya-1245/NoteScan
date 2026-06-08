plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
//    id("com.android.application")
//    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.notescan.app"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.notescan.app"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures{
        viewBinding = true
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation("com.google.firebase:firebase-auth:23.1.0")
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // To recognize Latin script
    implementation(libs.text.recognition)
// To recognize Chinese script
//     implementation(libs.text.recognition.chinese)
// // To recognize Devanagari script
//     implementation(libs.text.recognition.devanagari)
// // To recognize Japanese script
//     implementation(libs.text.recognition.japanese)
// // To recognize Korean script
//     implementation(libs.text.recognition.korean)

     implementation("com.squareup.retrofit2:retrofit:2.11.0")
     implementation(libs.converter.scalars.v290)
     implementation("com.squareup.retrofit2:converter-gson:2.11.0")
}