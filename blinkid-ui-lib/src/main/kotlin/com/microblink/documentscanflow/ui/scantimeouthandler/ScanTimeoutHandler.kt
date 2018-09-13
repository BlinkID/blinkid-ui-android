package com.microblink.documentscanflow.ui.scantimeouthandler

interface ScanTimeoutHandler {

    fun onScanStart()

    fun onScanDone()

    fun onScanPaused()

    fun onScanResumed()

}