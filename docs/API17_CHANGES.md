# API17 compatibility changes

Baseline: DiPlay-Legacy-Android 0.2.7, `c8884adcc75bfda3c134db63877bd6c6f83beb74`.

| Change | Android compatibility reason |
| --- | --- |
| Separate API17 flavor and identity | Install on API17 while retaining the API19 source variant |
| AndroidX core 1.12 for API17 | Use a dependency with the required older minimum SDK |
| BluetoothAdapter fallback; separate API18 helper | BluetoothManager is absent on API17; avoid loading unavailable classes |
| Explicit Socket/ServerSocket close | These types do not implement Closeable on API17 |
| MediaCodec name and Canvas/AudioTrack helpers | Newer codec capability and audio builder classes must be isolated from older Dalvik class verification |
| Locale, document export and notifications | Isolate APIs absent on API17; use existing-compatible locale, file-sharing and notification behavior |
| Network-interface, reflection and TCP helpers | Avoid unavailable interface-index access, exception classes and android.system classes |
| PNG launcher resources | Use an image format supported by the target resource runtime |
| Separate API19 JNI module and upstream vendor implementations | API17 does not initialize the upstream native/vendor paths; those features remain unavailable in this variant |

The change set does not add protocol features, vehicle preparation or external application service contracts. Existing upstream options remain configurable. The source ZIP and commit allow comparison against the baseline. API17 installation/startup results do not establish phone projection or support for every device.
