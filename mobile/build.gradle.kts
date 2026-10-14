plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.shilapi.xcertplay"
    testBuildType = providers.gradleProperty("api17TestBuildType").getOrElse("debug")
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.shihab.diplay"
        minSdk = 19
        targetSdk = 37
        multiDexEnabled = true
        versionCode = 26
        versionName = "0.2.7"

    }

    // Generic API17 compatibility variant; no vendor platform preparation.
    flavorDimensions += "platform"
    productFlavors {
        create("legacy") {
            dimension = "platform"
            minSdk = 19
        }
        create("api17") {
            dimension = "platform"
            minSdk = 17
            applicationId = "io.github.richkv.diplay.api17"
            versionCode = 27
            versionName = "0.2.7-api17.1"
            testInstrumentationRunner = "com.shilapi.xcertplay.Api17SmokeInstrumentation"
            // No native library dependency; build and launch need no NDK.
        }
    }



    signingConfigs {
        create("release") {
            storeFile = file(
                providers.environmentVariable("ANDROID_KEYSTORE_PATH")
                    .getOrElse("missing-release-keystore.jks"),
            )
            storePassword = providers.environmentVariable("ANDROID_KEYSTORE_PASSWORD").getOrElse("")
            keyAlias = providers.environmentVariable("ANDROID_KEY_ALIAS").getOrElse("")
            keyPassword = providers.environmentVariable("ANDROID_KEY_PASSWORD").getOrElse("")
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
        release {
            optimization {
                enable = false
            }
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
}

dependencies {
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    implementation(project(":common"))
    implementation(project(":shared"))
    "legacyImplementation"(project(":native-support"))
    implementation("androidx.multidex:multidex:2.0.1")
}

// This source-only fork never bundles accessory identities or private signing material.
val credentialAssets = files(android.sourceSets.flatMap { source ->
    source.assets.directories.map { directory ->
        fileTree(directory) {
            include("**/offline-mfi/**", "**/*.pk8", "**/*.p7b", "**/*.key",
                "**/*.pem", "**/*.p12", "**/*.pfx", "**/*.jks", "**/*.keystore")
        }
    }
})
val rejectBundledCredentials by tasks.registering {
    group = "verification"
    description = "Reject unexpected credential files in APK assets."
    val filesToCheck = credentialAssets
    inputs.files(filesToCheck)
    doLast {
        check(filesToCheck.isEmpty) { "Unexpected credential files in APK assets" }
    }
}
tasks.named("preBuild") { dependsOn(rejectBundledCredentials) }
