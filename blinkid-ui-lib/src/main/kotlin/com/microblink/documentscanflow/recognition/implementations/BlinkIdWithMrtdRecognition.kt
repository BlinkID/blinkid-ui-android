package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.buildTitle
import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.TwoSideRecognition
import com.microblink.documentscanflow.recognition.extract
import com.microblink.documentscanflow.recognition.util.BlinkIdRecognitionUtils
import com.microblink.entities.recognizers.blinkid.generic.BlinkIdRecognizer
import com.microblink.entities.recognizers.blinkid.mrtd.MrtdRecognizer

class BlinkIdWithMrtdRecognition : TwoSideRecognition<BlinkIdRecognizer.Result, MrtdRecognizer.Result>() {

    override val frontRecognizer by lazy { BlinkIdRecognizer() }
    override val backRecognizer by lazy { MrtdRecognizer() }
    private var title: String? = null

    override fun extractFields() {
        title = BlinkIdRecognitionUtils.extractFrontResult(frontResult, this)
        extract(backResult.mrzResult)
    }

    override fun getResultTitle(): String? {
        return when {
            title != null -> title
            backResult.isNotEmpty() -> backResult.mrzResult.buildTitle()
            else -> null
        }
    }

}