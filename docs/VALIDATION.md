# API17 release validation — 0.2.7-api17.1

Date: 2026-10-14. Package `io.github.richkv.diplay.api17`, versionCode 27, minimum Android API17.

| Check | Result and boundary |
| --- | --- |
| API17 unit tests | 327 passed: shared 254, common 73; zero failures, errors or skips |
| Retained Legacy regression tests | 326 passed: shared 254, common 72; Legacy Kotlin compilation passed, no Legacy device acceptance |
| Release lint | Zero errors/fatal issues; existing warnings: mobile 7, common 89, shared 58 |
| Release package/signature | Minimum API17; maintainer signer; JAR/v1 signature verification for API17; no packaged JNI libraries or accessory credential assets |
| Fresh installation/startup | Actual AOSP Android 4.2.2 / API17 / ARMv7 emulator; installed APK readback hash matches; main Activity remains resumed after 12 seconds; screenshot reviewed; no captured fatal/linkage errors |
| API17 runtime checks | 21 passed on both fresh and updated release installations: class descriptors, old Bluetooth/socket/network behavior, disabled unavailable native/vendor paths, upstream preferences, diagnostic file export and main-interface startup |
| Same-signer update | Controlled versionCode 26 predecessor to release code27; selected stored configuration preserved. This is a package-manager update fixture, not a historical public release |
| Source and history | Credential/container patterns and selected source-boundary checks passed. A bounded scan is not a complete security audit |
| Phone projection/media | Not tested; installation and interface startup are not authenticated phone-session or physical audio/video evidence |

APK SHA256: `97bf5a4ea1b00d823bf7cd11a952eda6633084c1aed5deb325b685ea3f91e8c4`.

Signing-certificate SHA256: `6d7820566761ff18f62abf18af97dd7d1fda58feed2306cc9dbb9538b81f1fa2`.

The emulator is an AOSP image, not a manufacturer firmware. Other devices, installation policies and unexecuted paths remain unverified. The application displays the expected setup notice because accessory prerequisites are not supplied. No bit-for-bit reproducibility or universal hardware compatibility is claimed.

The source tag and Release checksum file identify the corresponding source archive. GitHub Actions results are available in the repository Actions tab; local signed/runtime checks remain distinct from CI.
