plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "src.main.java.com.notescan.app"
    compileSdk = 36

    defaultConfig {
        applicationId = "src.main.java.com.notescan.app"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // To recognize Latin script
    implementation(libs.text.recognition)
// To recognize Chinese script
    implementation(libs.text.recognition.chinese)
// To recognize Devanagari script
    implementation(libs.text.recognition.devanagari)
// To recognize Japanese script
    implementation(libs.text.recognition.japanese)
// To recognize Korean script
    implementation(libs.text.recognition.korean)

    implementation(libs.retrofit)
    implementation(libs.converter.scalars.v290)
    implementation(libs.converter.gson)
}