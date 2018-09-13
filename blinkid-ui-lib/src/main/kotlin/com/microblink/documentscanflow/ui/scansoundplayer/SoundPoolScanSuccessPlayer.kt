package com.microblink.documentscanflow.ui.scansoundplayer

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build

open class SoundPoolScanSuccessPlayer(private val context: Context, private val soundResourceId: Int) : ScanSuccessPlayer {

    private var soundPool: SoundPool? = null
    private var soundId : Int = 0
    override var isSoundEnabled : Boolean = true

    override fun prepare() {
        soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            SoundPool.Builder().setMaxStreams(1).setAudioAttributes(
                    AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
            ).build()
        } else {
            SoundPool(1, AudioManager.STREAM_MUSIC, 0)
        }
        soundId = soundPool!!.load(context, soundResourceId, 1)
    }

    override fun play() {
        if(isSoundEnabled) {
            soundPool?.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f)
        }
    }

    override fun cleanup() {
        soundPool?.release()
        soundPool = null
        soundId = -1
    }

}