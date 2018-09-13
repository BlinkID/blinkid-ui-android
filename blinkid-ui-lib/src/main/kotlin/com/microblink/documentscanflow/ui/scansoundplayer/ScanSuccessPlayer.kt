package com.microblink.documentscanflow.ui.scansoundplayer

interface ScanSuccessPlayer {
    var isSoundEnabled : Boolean
    fun prepare()
    fun play()
    fun cleanup()
}