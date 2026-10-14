# Build

Use JDK 25, Gradle 9.5 wrapper, Android Gradle Plugin 9.3.0, Android SDK `platforms;android-37.0` and build-tools 36.0.0. API17 builds require no NDK. Set an untracked local.properties or ANDROID_SDK_ROOT for the local SDK.

```sh
python3 scripts/check_source_boundary.py
./gradlew :shared:testApi17DebugUnitTest :common:testApi17DebugUnitTest :mobile:lintApi17Debug :mobile:assembleApi17Debug
```

On Windows use `python` and `gradlew.bat`. Output: `mobile/build/outputs/apk/api17/debug/mobile-api17-debug.apk`. Compile/target SDK 37 does not change the minimum install level of API17.

## Maintainer-signed release

Supply ANDROID_KEYSTORE_PATH, ANDROID_KEYSTORE_PASSWORD, ANDROID_KEY_ALIAS and ANDROID_KEY_PASSWORD through a protected local environment. Do not place signing inputs in this repository.

```sh
./gradlew :mobile:assembleApi17Release
```

Output: `mobile/build/outputs/apk/api17/release/mobile-api17-release.apk`. Verify signing for minimum API17, APK metadata and contents. Release signing material is separate from accessory authentication. The build rejects credential containers in APK assets and has no automatic accessory-asset input. Source builds remain usable for compilation and interface startup without those materials.

A release attaches corresponding source, checksums, certificate fingerprint and its validation results. This procedure does not promise bit-identical builds across different environments or signers.
