plugins {
    id("com.android.library")
}

android {
    namespace = "com.shilapi.xcertplay.host"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        minSdk = 17
        multiDexEnabled = true
    }

    flavorDimensions += "platform"
    productFlavors {
        create("legacy") { dimension = "platform"; minSdk = 19 }
        create("api17") { dimension = "platform"; minSdk = 17 }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }

}

dependencies {
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    api(project(":shared"))
    implementation(libs.androidx.activity)
    implementation(libs.androidx.appcompat)
    "legacyImplementation"("androidx.core:core-ktx:1.13.1")
    "api17Implementation"("androidx.core:core-ktx:1.12.0")
    testImplementation(libs.junit)
    testImplementation("org.robolectric:robolectric:4.17")
}
