package com.shilapi.xcertplay.compat

import com.shilapi.xcertplay.shared.BuildConfig

/** Source-set capabilities. API17 excludes vendor JNI built for API19 or newer. */
object RuntimeCapabilities {
    const val api17 = BuildConfig.API17_COMPAT
    const val vehicleOutputs = !api17
    const val nativeSupport = !api17
}
