package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.buildId2VerticalCardDetectorRecognizer
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.germany.GermanyOldIdRecognizer

class GermanyOldIdRecognition : BaseRecognition() {

    val recognizer by lazy { GermanyOldIdRecognizer() }
    val backRecognizer by lazy { buildId2VerticalCardDetectorRecognizer() }

    override fun getSingleSideRecognizers(): List<Recognizer<*, *>> {
        return listOf(recognizer, backRecognizer)
    }

    override fun extractData(): String? {
        val result = recognizer.result
        if (result.isEmpty()) {
            return null
        }

        extractMrtdResult(result)
        return buildMrtdTitle(result)
    }

}