# Validation

Release-specific results accompany the APK in `VALIDATION.md`. A build, an installation and an interface startup establish separate facts. Use the exact release commit, APK hash and certificate fingerprint when interpreting results.

Required checks: source/history credential scan; API17 unit tests and lint; APK minimum SDK, package and signing verification; no JNI/credential assets; fresh API17 installation and startup; Dalvik compatibility checks; same-signer update with preserved settings.

The checks do not establish physical phone projection or every device's compatibility. Upstream reports are not evidence for this artifact. Local development signing is not the maintainer release signer. No bit-for-bit reproducibility claim is made.
