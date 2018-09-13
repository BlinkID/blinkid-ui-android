package com.microblink.documentscanflow.ui.scanlineanimator

import android.support.annotation.UiThread

interface ScanLineAnimator {

    @UiThread
    fun onScanStart()

    @UiThread
    fun onScanPause()

    @UiThread
    fun onScanResume()

}