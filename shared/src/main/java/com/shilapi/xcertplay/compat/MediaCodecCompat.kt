package com.shilapi.xcertplay.compat

import android.media.MediaCodec
import android.os.Build

object MediaCodecCompat {
    fun name(codec: MediaCodec): String =
        if (Build.VERSION.SDK_INT >= 18) MediaCodecApi18.name(codec) else "legacy-decoder"
}

@android.annotation.TargetApi(18)
private object MediaCodecApi18 {
    fun name(codec: MediaCodec): String = codec.name
}
