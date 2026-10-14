package com.shilapi.xcertplay.media

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import androidx.annotation.RequiresApi

/** The API17 renderer references only AudioTrack in this helper's signature. */
@RequiresApi(23)
internal object AudioTrackApi23 {
    fun create(rate: Int, mask: Int, encoding: Int, buffer: Int, audioType: String, payloadType: Int, advanced: Boolean): AudioTrack {
        val mode = if (advanced) AudioChannelMappingMode.AUTOMOTIVE_BUS else AudioChannelMappingMode.MOBILE_COMPATIBLE
        val selection = AudioChannelMapper.map(audioType = audioType, payloadType = payloadType, mode = mode)
        val usage = when (selection.channel) {
            AudioChannel.MEDIA -> AudioAttributes.USAGE_MEDIA
            AudioChannel.PHONE -> AudioAttributes.USAGE_VOICE_COMMUNICATION
            AudioChannel.ASSISTANT -> if (android.os.Build.VERSION.SDK_INT >= 26) AudioAttributes.USAGE_ASSISTANT else AudioAttributes.USAGE_ASSISTANCE_NAVIGATION_GUIDANCE
            AudioChannel.NAVIGATION -> AudioAttributes.USAGE_ASSISTANCE_NAVIGATION_GUIDANCE
        }
        val content = when (selection.contentType) {
            AudioContentType.MUSIC -> AudioAttributes.CONTENT_TYPE_MUSIC
            AudioContentType.SPEECH -> AudioAttributes.CONTENT_TYPE_SPEECH
        }
        return AudioTrack.Builder()
            .setAudioAttributes(AudioAttributes.Builder().setUsage(usage).setContentType(content).build())
            .setAudioFormat(AudioFormat.Builder().setSampleRate(rate).setChannelMask(mask).setEncoding(encoding).build())
            .setTransferMode(AudioTrack.MODE_STREAM).setBufferSizeInBytes(buffer).build()
    }
}
