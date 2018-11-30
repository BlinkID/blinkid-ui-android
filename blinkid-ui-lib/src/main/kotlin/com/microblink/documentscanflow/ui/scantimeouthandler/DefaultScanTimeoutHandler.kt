package com.microblink.documentscanflow.ui.scantimeouthandler

import android.os.CountDownTimer
import com.microblink.util.Log

class DefaultScanTimeoutHandler(private var scanTimeoutMillis: Long,
                                private val listener: Listener): ScanTimeoutHandler {

    private var timeoutTimer : CountDownTimer? = null
    private var currentTimerTimeoutMillis = 0L

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
        listener.onTimeout()

        // each next timeout should be longer
        scanTimeoutMillis *= 2

        // timeout duration has been updated so we want a new timer
        destroyCurrentTimeoutTimer()
    }

    private fun destroyCurrentTimeoutTimer() {
        timeoutTimer?.cancel()
        Log.w("DISI", "destroying " + timeoutTimer?.hashCode())
        timeoutTimer = null
    }

    private fun updateTimerIfTimeoutUpdated() {
        if (currentTimerTimeoutMillis != scanTimeoutMillis) {
            currentTimerTimeoutMillis = scanTimeoutMillis
            timeoutTimer = createTimeoutTimer()
            Log.w("DISI", "new timer " + timeoutTimer.hashCode())
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

    interface Listener {
        fun onTimeout()
    }

}