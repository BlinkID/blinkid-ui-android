package com.microblink.documentscanflow.ui.scanlineanimator

import android.support.annotation.UiThread
import com.microblink.documentscanflow.ui.view.ScanFrameLayout


class DefaultScanLineAnimator(private val scanFrameLayout: ScanFrameLayout) : ScanLineAnimator {

    @UiThread
    override fun onScanStart() {
        scanFrameLayout.startLineAnimation()
    }

    @UiThread
    override fun onScanPause() {
        scanFrameLayout.cancelLineAnimation()
    }

    @UiThread
    override fun onScanResume() {
        scanFrameLayout.startLineAnimation()
    }

}