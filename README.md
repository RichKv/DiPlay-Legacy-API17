# DiPlay Legacy API17

An Android 4.2.2 / API17 compatibility edition of [DiPlay-Legacy-Android](https://github.com/programmerguohuajing/DiPlay-Legacy-Android), based on version 0.2.7, commit `c8884adcc75bfda3c134db63877bd6c6f83beb74`.

[简体中文](README.zh-CN.md) · [Changes](docs/API17_CHANGES.md) · [Build](docs/BUILD.md) · [Install](docs/INSTALL.md) · [Validation](docs/VALIDATION.md) · [Licenses](docs/THIRD_PARTY_NOTICES.md)

## Purpose

This edition lowers the minimum Android level from API19 (Android 4.4) to API17 (Android 4.2.2). It changes API-level access, runtime compatibility, resources and build configuration. It preserves the upstream protocol and application choices; it adds no vehicle integration or application service interfaces.

Compilation, installation and main-interface startup are this edition's verification scope. Pairing, radio/network availability and accessory authentication are separate prerequisites. An installable APK is not evidence of a working phone projection session. Builds contain no accessory identity or credentials and do not download them automatically.

## Releases

Download the maintainer-signed APK, corresponding source and SHA256 checksums from [GitHub Releases](https://github.com/RichKv/DiPlay-Legacy-API17/releases). Use the validation record attached to the particular release. No download website is deployed by this project.

Application ID: `io.github.richkv.diplay.api17`; development builds append `.debug`. Version: `0.2.7-api17.1`, versionCode 27. Updates must preserve the application ID and signing certificate, with increasing versionCode. Separately signed builds do not automatically replace one another.

## Build

JDK 25, Gradle 9.5, Android SDK 37 and build-tools 36.0.0. The API17 variant requires no NDK and packages no JNI libraries.

```sh
./gradlew :mobile:assembleApi17Debug
```

## Upstream and licenses

This is an independent derivative. The selected upstream declares API19 as its minimum; it does not establish a requirement for the Android 4.4.4 patch release. [Source provenance](docs/UPSTREAM_PROVENANCE.md) and [credited upstream README snapshots](docs/UPSTREAM-README.md) are retained. Upstream feature descriptions and validation apply to upstream, not to this APK.

Receiver code retains GPLv3; DiAuto-derived UI retains AGPL-3.0-only. BYD images and the CarPlay icon have separate notices. See the complete [third-party notices](docs/THIRD_PARTY_NOTICES.md). This project is not an official DiPlay release or an Apple-certified product.
