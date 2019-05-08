package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.buildId1CardDetectorRecognizer
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.singapore.SingaporeDlFrontRecognizer

class SingaporeDlRecognition : BaseRecognition() {

    private val frontRecognizer by lazy { SingaporeDlFrontRecognizer() }
    private val backRecognizer by lazy { buildId1CardDetectorRecognizer() }

    override fun getSingleSideRecognizers() = listOf<Recognizer<*>>(frontRecognizer, backRecognizer)

    override fun extractData(): String? {
        val result = frontRecognizer.result
        if (result.isEmpty()) {
            return null
        }

        add(DOCUMENT_NUMBER, result.licenceNumber)
        add(FULL_NAME, result.name)
        add(DATE_OF_BIRTH, result.birthDate)
        add(DATE_OF_ISSUE, result.issueDate)
        addDateOfExpiry(result.validTill.date)

        return result.name
    }
}