# Testing

```sh
python3 scripts/check_source_boundary.py
./gradlew :shared:testApi17DebugUnitTest :common:testApi17DebugUnitTest :shared:testLegacyDebugUnitTest :common:testLegacyDebugUnitTest :shared:lintApi17Debug :common:lintApi17Debug :mobile:lintApi17Debug :mobile:assembleApi17Debug :mobile:assembleApi17DebugAndroidTest
```

Run `com.shilapi.xcertplay.Api17SmokeInstrumentation` on a dedicated API17 emulator. It checks old-runtime class loading and interface startup. It does not pair a phone or provide authentication. CI has no release signer or authentication assets and publishes test reports only.

With local release signing configured, use `-Papi17TestBuildType=release :mobile:assembleApi17ReleaseAndroidTest` to build checks for the signed release application. Check the test APK signer matches the application before running it.
