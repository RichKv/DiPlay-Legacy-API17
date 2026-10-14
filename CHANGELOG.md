# Changelog

## 0.2.7-api17.1

- Lower minimum Android level from API19 to API17 through an isolated compatibility variant.
- Isolate APIs unavailable on older Dalvik; adapt resources, notifications, audio construction, locale and diagnostic file sharing.
- Keep upstream protocol behavior and configuration choices; exclude API19 JNI/vendor initialization from the API17 build.
- Provide an independent application ID, maintainer-signed APK, corresponding source and release validation.

The selected upstream history is available at commit c8884adcc75bfda3c134db63877bd6c6f83beb74.
