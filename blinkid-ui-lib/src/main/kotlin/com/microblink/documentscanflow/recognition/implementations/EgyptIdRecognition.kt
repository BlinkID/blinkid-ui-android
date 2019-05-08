package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.buildId1CardDetectorRecognizer
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.egypt.EgyptIdFrontRecognizer

class EgyptIdRecognition : BaseRecognition() {

    val recognizer by lazy { EgyptIdFrontRecognizer() }
    val backRecognizer by lazy { buildId1CardDetectorRecognizer() }

    override fun getSingleSideRecognizers(): List<Recognizer<*>> {
        return listOf(recognizer, backRecognizer)
    }

    override fun extractData(): String? {
        val result = recognizer.result
        if (result.isEmpty()) {
            return null
        }

        val documentNumber = result.documentNumber
        add(DOCUMENT_NUMBER, documentNumber)
        add(NATIONAL_NUMBER, result.nationalNumber)

        return null
    }

}