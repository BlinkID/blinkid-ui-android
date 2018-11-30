package com.microblink.documentscanflow.ui.scantimeouthandler

interface ScanTimeoutHandler {

    fun registerListener(listener: Listener?)

    fun onScanStart()

    fun onScanDone()

    fun onScanPaused()

    fun onScanResumed()

    interface Listener {
        fun onTimeout()
    }

}