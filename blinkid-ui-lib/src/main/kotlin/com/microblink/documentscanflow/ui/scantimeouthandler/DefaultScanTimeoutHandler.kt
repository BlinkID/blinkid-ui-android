package com.microblink.documentscanflow.ui.scantimeouthandler

import android.os.CountDownTimer

class DefaultScanTimeoutHandler(private var scanTimeoutMillis: Long): ScanTimeoutHandler {

    private var timeoutTimer : CountDownTimer? = null
    private var currentTimerTimeoutMillis = 0L
    private var listener: ScanTimeoutHandler.Listener? = null

    override fun registerListener(listener: ScanTimeoutHandler.Listener?) {
        this.listener = listener
    }

    override fun onScanStart() {
        updateTimerIfTimeoutUpdated()
        timeoutTimer?.start()
    }

    override fun onScanPaused() {
        timeoutTimer?.cancel()
    }

    override fun onScanResumed() {
        updateTimerIfTimeoutUpdated()
        timeoutTimer?.start()
    }

    override fun onScanDone() {
        timeoutTimer?.cancel()
    }

    private fun onTimeout() {
        listener?.onTimeout()

        // each next timeout should be longer
        scanTimeoutMillis *= 2

        // timeout duration has been updated so we want a new timer
        destroyCurrentTimeoutTimer()
    }

    private fun destroyCurrentTimeoutTimer() {
        timeoutTimer?.cancel()
        timeoutTimer = null
    }

    private fun updateTimerIfTimeoutUpdated() {
        if (currentTimerTimeoutMillis != scanTimeoutMillis) {
            currentTimerTimeoutMillis = scanTimeoutMillis
            timeoutTimer = createTimeoutTimer()
        }
    }

    private fun createTimeoutTimer() : CountDownTimer = object: CountDownTimer(scanTimeoutMillis, scanTimeoutMillis) {
        override fun onFinish() {
            onTimeout()
        }

        override fun onTick(millisUntilFinished: Long) {
            // don't care
        }
    }

}