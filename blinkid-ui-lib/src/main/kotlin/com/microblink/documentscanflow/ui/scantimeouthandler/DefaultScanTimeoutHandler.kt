package com.microblink.documentscanflow.ui.scantimeouthandler

import android.app.Activity
import android.os.CountDownTimer
import android.support.v7.app.AlertDialog
import com.microblink.documentscanflow.R

class DefaultScanTimeoutHandler(private val activity: Activity,
                                private var scanTimeoutMillis: Long,
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

        if (activity.isFinishing) {
            return
        }

        AlertDialog.Builder(activity).apply {
            setCancelable(true)
            setMessage(R.string.mb_timeout_message)
            setTitle(R.string.mb_timeout_title)
            setPositiveButton(R.string.mb_timeout_retry) { _, _ -> listener.onRetry() }
            setNeutralButton(R.string.mb_timeout_change_country) { _, _ -> listener.onChangeCountry() }
            setOnCancelListener { listener.onRetry() }
            show()
        }
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

    interface Listener {
        fun onTimeout()
        fun onRetry()
        fun onChangeCountry()
    }

}