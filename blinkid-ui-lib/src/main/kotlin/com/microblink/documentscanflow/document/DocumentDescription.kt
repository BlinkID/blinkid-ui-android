package com.microblink.documentscanflow.document

import android.support.annotation.StringRes
import com.microblink.documentscanflow.recognition.BaseRecognition

class DocumentDescription(
        val isFullySupported: Boolean,
        @StringRes val documentNameResourceID: Int,
        val recognition: BaseRecognition)