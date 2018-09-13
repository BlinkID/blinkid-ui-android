package com.microblink.documentscanflow.ui.scansoundplayer

class EmptyScanSuccessPlayer : ScanSuccessPlayer {

    override var isSoundEnabled: Boolean
        get() = false
        set(value) {}

    override fun prepare() {
    }

    override fun play() {
    }

    override fun cleanup() {
    }

}