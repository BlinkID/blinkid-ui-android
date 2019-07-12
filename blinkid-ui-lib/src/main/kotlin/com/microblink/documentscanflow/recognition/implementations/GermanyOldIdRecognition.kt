package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.buildId2VerticalCardDetectorRecognizer
import com.microblink.documentscanflow.buildTitle
import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.TwoSideRecognition
import com.microblink.entities.recognizers.blinkid.germany.GermanyIdOldRecognizer
import com.microblink.entities.recognizers.detector.DetectorRecognizer

class GermanyOldIdRecognition : TwoSideRecognition<GermanyIdOldRecognizer.Result, DetectorRecognizer.Result>() {

    override val frontRecognizer by lazy { GermanyIdOldRecognizer() }
    override val backRecognizer by lazy {
        buildId2VerticalCardDetectorRecognizer()
    }

    override fun extractFields() {
        if (frontResult.isNotEmpty()) extractMrzResult(frontResult.mrzResult)
    }

    override fun getResultTitle(): String? {
        return if (frontResult.isNotEmpty()) frontResult.mrzResult.buildTitle()
        else null
    }

}
