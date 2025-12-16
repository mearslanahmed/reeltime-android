import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

val cloudinaryProperties = Properties()
val cloudinaryPropertiesFile = project.file("cloudinary.properties")
if (cloudinaryPropertiesFile.exists()) {
    cloudinaryProperties.load(cloudinaryPropertiesFile.inputStream())
}

android {
    namespace = "com.arslan.reeltime"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.arslan.reeltime"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "CLOUDINARY_CLOUD_NAME", "\"${cloudinaryProperties.getProperty("cloudinary_cloud_name")}\"")
        buildConfigField("String", "CLOUDINARY_API_KEY", "\"${cloudinaryProperties.getProperty("cloudinary_api_key")}\"")
        buildConfigField("String", "CLOUDINARY_API_SECRET", "\"${cloudinaryProperties.getProperty("cloudinary_api_secret")}\"")
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
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:34.6.0"))

    // Add the dependency for the Firebase Authentication library
    implementation(libs.firebase.auth)

    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation(libs.material)
    implementation("androidx.activity:activity:1.12.1")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation(libs.firebase.database)
    implementation(libs.androidx.viewpager2)
    implementation(libs.androidx.recyclerview)
    implementation("androidx.cardview:cardview:1.0.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation ("com.github.bumptech.glide:glide:5.0.5")
    implementation ("com.github.ismaeldivita:chip-navigation-bar:1.4.0")
    implementation("com.github.Dimezis:BlurView:version-3.2.0")
    implementation("com.google.zxing:core:3.5.4")
    implementation("com.cloudinary:cloudinary-android:3.1.2")
}