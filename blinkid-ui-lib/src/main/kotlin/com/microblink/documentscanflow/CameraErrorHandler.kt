package com.microblink.documentscanflow

import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.microblink.hardware.camera.AutoFocusRequiredButNotSupportedException
import com.microblink.hardware.camera.CameraResolutionTooSmallException
import com.microblink.recognition.FeatureNotSupportedException
import com.microblink.recognition.RecognizerError
import com.microblink.view.NotSupportedReason

internal class CameraErrorHandler(private val context: Context, private val onUnrecoverableError: () -> Unit) {

    fun handleCameraError(exc: Throwable?) {
        when (exc) {
            is CameraResolutionTooSmallException -> handleNotSupportedError(getString(R.string.mb_feature_unsupported_device))
            is RecognizerError -> {
                handleInitError(getString(R.string.mb_error_initializing))
            }
            is UnsatisfiedLinkError -> handleInitError(getString(R.string.mb_error_initializing))
            is AutoFocusRequiredButNotSupportedException -> handleNotSupportedError(getString(R.string.mb_feature_unsupported_autofocus))
            is FeatureNotSupportedException -> handleNotSupportedError(getNotSupportedReasonDescription(exc.reason))
            is SecurityException -> handleInitError(getString(R.string.mb_camera_not_allowed))
            else -> handleInitError(getString(R.string.mb_camera_error))
        }
    }

    private fun handleInitError(message: String) = AlertDialog.Builder(context, R.style.MbErrorAlertDialogTheme)
            .setTitle(getString(R.string.mb_title_error_starting))
            .setMessage(message)
            .setNeutralButton(getString(R.string.mb_ok)) { _, _ -> onUnrecoverableError() }
            .setCancelable(false)
            .create()
            .show()

    private fun handleNotSupportedError(notSupportedReason: String?) = AlertDialog.Builder(context, R.style.MbErrorAlertDialogTheme)
            .setTitle(getString(R.string.mb_title_not_supported))
            .setMessage(notSupportedReason)
            .setNeutralButton(getString(R.string.mb_ok)) { _, _ -> onUnrecoverableError() }
            .setCancelable(false)
            .create()
            .show()

    private fun getNotSupportedReasonDescription(reason: NotSupportedReason): String = when (reason) {
        NotSupportedReason.CUSTOM_UI_FORBIDDEN -> getString(com.microblink.library.R.string.mb_custom_ui_forbidden)
        NotSupportedReason.INVALID_OR_MISSING_LICENSE -> getString(com.microblink.library.R.string.mb_invalid_license)
        NotSupportedReason.UNSUPPORTED_ANDROID_VERSION -> getString(com.microblink.library.R.string.mb_feature_unsupported_android_version)
        else -> getString(com.microblink.library.R.string.mb_feature_unsupported_device)
    }

    private fun getString(@StringRes id: Int) = context.getString(id)

}