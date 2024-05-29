import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.omersari.wordlyjavafinal"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.omersari.wordlyjavafinal"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Read the API key from the local.properties file
        val properties = Properties()
        val localProperties = rootProject.file("local.properties")
        if (localProperties.exists()) {
            properties.load(localProperties.inputStream())
        }

        val apiKey: String = properties.getProperty("apiKey", "")
        buildConfigField("String", "API_KEY", "\"$apiKey\"")
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
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    //animations
    implementation ("com.daimajia.androidanimations:library:2.4@aar")




    implementation ("androidx.room:room-runtime:2.6.1")
    implementation("androidx.navigation:navigation-ui:2.7.7")
    implementation("androidx.activity:activity:1.9.0")
    annotationProcessor ("androidx.room:room-compiler:2.6.1")
    implementation ("androidx.room:room-rxjava3:2.6.1")
    implementation ("io.reactivex.rxjava3:rxandroid:3.0.0")
    implementation ("com.google.android.material:material:1.11.0")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.picasso:picasso:2.8")

    implementation("com.squareup.okhttp3:okhttp:4.10.0")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}