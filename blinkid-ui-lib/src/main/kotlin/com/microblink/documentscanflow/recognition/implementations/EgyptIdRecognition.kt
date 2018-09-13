package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.buildDetectorRecognizerFromPreset
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.entities.detectors.quad.document.DocumentSpecificationPreset
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.egypt.EgyptIdFrontRecognizer

class EgyptIdRecognition : BaseRecognition() {

    val recognizer by lazy { EgyptIdFrontRecognizer() }
    val backRecognizer by lazy { buildDetectorRecognizerFromPreset(DocumentSpecificationPreset.DOCUMENT_SPECIFICATION_PRESET_ID1_CARD) }

    override fun getSingleSideRecognizers(): List<Recognizer<*, *>> {
        return listOf(recognizer, backRecognizer)
    }

    override fun extractData(): String? {
        val result = recognizer.result
        if (result.isEmpty()) {
            return null
        }

        val documentNumber = result.documentNumber
        add(R.string.keyDocumentNumber, documentNumber)
        add(R.string.keyNationalNumber, result.nationalNumber)

        return null
    }

}