package com.microblink.documentscanflow.ui

import android.os.Handler
import android.view.MenuItem
import com.microblink.documentscanflow.R
import com.microblink.view.recognition.RecognizerRunnerView

internal class TorchButtonHandler {

    internal var torchItem: MenuItem? = null

    var isTorchEnabled = false
        set(isEnabled) {
            field = isEnabled
            updateIcon(isEnabled)
        }

    internal fun onTorchSupported() {
        torchItem?.isVisible = true
    }

    internal fun onTorchButtonClick(recognizerView: RecognizerRunnerView, mainThreadHandler : Handler) {
        recognizerView.setTorchState(!isTorchEnabled) { success ->
            if (success) {
                mainThreadHandler.post {isTorchEnabled = !isTorchEnabled}
            }
        }
    }

    private fun updateIcon(isEnabled: Boolean) {
        if (isEnabled) {
            torchItem?.setIcon(R.drawable.mb_ic_torch_on)
        } else {
            torchItem?.setIcon(R.drawable.mb_ic_torch_off)
        }
    }

}