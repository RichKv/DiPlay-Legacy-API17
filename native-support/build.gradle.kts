plugins { id("com.android.library") }

// This module is consumed only by Legacy/Automotive. API17 has no native dependency.
android {
    namespace = "com.shilapi.xcertplay.native_support"
    compileSdk = 37
    defaultConfig {
        minSdk = 19
        ndk { abiFilters += setOf("arm64-v8a", "armeabi-v7a", "x86_64", "x86") }
        externalNativeBuild { ndkBuild { arguments += "APP_PLATFORM=android-19" } }
    }
    ndkVersion = "25.2.9519653"
    externalNativeBuild {
        ndkBuild { path = file("../shared/src/main/jni/Android.mk") }
    }
}
