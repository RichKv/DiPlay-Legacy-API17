package com.shilapi.xcertplay

import android.media.MediaCodecList
import android.os.Build
import androidx.annotation.RequiresApi
import com.shilapi.xcertplay.airplay.AirPlayDisplayConfig

internal data class CanvasSupport(val supported: Boolean, val reason: String, val details: String)

@RequiresApi(21)
internal object CanvasCapabilitiesApi21 {
    fun query(display: AirPlayDisplayConfig, hevcEnabled: Boolean, hevcSoftwareDecoderEnabled: Boolean): CanvasSupport {
        val mime = if (hevcEnabled) "video/hevc" else "video/avc"
        return try {
        // Match MediaCodec.createDecoderByType's first suitable decoder; do not silently force
        // an enlarged stream through a software decoder on a slower head unit.
        val decoder = MediaCodecList(MediaCodecList.REGULAR_CODECS).codecInfos.firstOrNull {
            !it.isEncoder && it.supportedTypes.any { type -> type.equals(mime, ignoreCase = true) }
        }
        if (decoder == null) {
            CanvasSupport(false, "no_decoder", "Decoder capability mime=$mime result=no_decoder")
        } else {
            val hardware = if (Build.VERSION.SDK_INT >= 29) decoder.isHardwareAccelerated
                else !decoder.name.startsWith("OMX.google.") && !decoder.name.startsWith("c2.android.")
            val video = decoder.getCapabilitiesForType(mime).videoCapabilities
            val sizeSupported = video?.isSizeSupported(display.widthPixels, display.heightPixels) == true
            val rateSupported = sizeSupported && video?.areSizeAndRateSupported(
                display.widthPixels, display.heightPixels, display.fps.toDouble()) == true
            val reason = when {
                !hardware -> "software_decoder"
                hevcEnabled && hevcSoftwareDecoderEnabled -> "software_hevc_selected"
                video == null -> "no_video_capabilities"
                !sizeSupported -> "canvas_dimensions_unsupported"
                !rateSupported -> "frame_rate_unsupported"
                else -> "supported"
            }
            CanvasSupport(reason == "supported", reason,
                "Decoder capability codec=${decoder.name} mime=$mime hardware=$hardware " +
                    "sizeSupported=$sizeSupported rateSupported=$rateSupported " +
                    "widths=${video?.supportedWidths} heights=${video?.supportedHeights} " +
                    "alignment=${video?.widthAlignment}x${video?.heightAlignment} " +
                    "fpsRange=${video?.supportedFrameRates} result=$reason")
        }
        } catch (error: Exception) {
            CanvasSupport(false, "capability_query_${error.javaClass.simpleName}", "Decoder query failed: ${error.javaClass.simpleName}")
        }
    }
}
