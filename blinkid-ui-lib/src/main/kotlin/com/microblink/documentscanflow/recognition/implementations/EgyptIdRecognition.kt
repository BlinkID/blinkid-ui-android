package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.SingleSideWithId1CardDetectorRecognition
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.DOCUMENT_NUMBER
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.NATIONAL_NUMBER
import com.microblink.entities.recognizers.blinkid.egypt.EgyptIdFrontRecognizer

class EgyptIdRecognition : SingleSideWithId1CardDetectorRecognition<EgyptIdFrontRecognizer.Result>() {

    override val recognizer by lazy { EgyptIdFrontRecognizer() }

    override fun extractData(result: EgyptIdFrontRecognizer.Result): String? {
        add(DOCUMENT_NUMBER, result.documentNumber)
        add(NATIONAL_NUMBER, result.nationalNumber)
        return null
    }

}