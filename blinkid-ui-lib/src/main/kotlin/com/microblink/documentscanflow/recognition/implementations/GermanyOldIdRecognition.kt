package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.buildId2VerticalCardDetectorRecognizer
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.documentscanflow.recognition.TwoSideRecognition
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.germany.GermanyIdOldRecognizer
import com.microblink.entities.recognizers.detector.DetectorRecognizer

class GermanyOldIdRecognition : TwoSideRecognition<GermanyIdOldRecognizer.Result, DetectorRecognizer.Result>() {

    override val frontRecognizer by lazy { GermanyIdOldRecognizer() }
    override val backRecognizer by lazy {
        buildId2VerticalCardDetectorRecognizer()
    }

    override fun extractData(frontResult: GermanyIdOldRecognizer.Result, backResult: DetectorRecognizer.Result): String? {
        if (frontResult.isEmpty()) {
            return null
        }

        extractMrzResult(frontResult.mrzResult)
        return buildMrtdTitle(frontResult.mrzResult)
    }

}
