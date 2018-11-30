package com.microblink.documentscanflow.ui.scantimeouthandler

interface ScanTimeoutHandler {

    fun registerListener(listener: Listener?)

    fun startTimer()

    fun stopTimer()

    interface Listener {
        fun onTimeout()
    }

}