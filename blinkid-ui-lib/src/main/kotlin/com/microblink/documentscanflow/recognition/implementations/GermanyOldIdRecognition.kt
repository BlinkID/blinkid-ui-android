package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.buildDetectorRecognizerFromPreset
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.entities.detectors.quad.document.DocumentSpecificationPreset
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.germany.GermanyIdOldRecognizer

class GermanyOldIdRecognition : BaseRecognition() {

    val recognizer by lazy { GermanyIdOldRecognizer() }
    val backRecognizer by lazy {
        buildDetectorRecognizerFromPreset(DocumentSpecificationPreset.DOCUMENT_SPECIFICATION_PRESET_ID2_VERTICAL_CARD)
    }

    override fun getSingleSideRecognizers(): List<Recognizer<*, *>> {
        return listOf(recognizer, backRecognizer)
    }

    override fun extractData(): String? {
        val result = recognizer.result
        if (result.isEmpty()) {
            return null
        }

        extractMrzResult(result.mrzResult)
        return buildMrtdTitle(result.mrzResult)
    }

}