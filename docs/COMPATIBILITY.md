# Compatibility

API17 (Android 4.2.2) is the minimum of this edition. Verification covers compilation, installation and main-interface startup, with a real API17 ARMv7 emulator runtime. Other devices and unexecuted application paths need their own checks.

The API17 variant does not package JNI libraries. Upstream features that require the isolated native/vendor paths are unavailable. The retained API19 Legacy variant is not an API17 release target.

Phone pairing, network/radio conditions and accessory authentication are prerequisites assessed separately. Interface startup does not establish a phone session, video presentation or audio output. See the release-specific validation record.
