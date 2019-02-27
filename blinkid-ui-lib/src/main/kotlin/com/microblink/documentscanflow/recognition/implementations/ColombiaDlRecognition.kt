package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.buildId1CardDetectorRecognizer
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.colombia.ColombiaDlFrontRecognizer
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*

class ColombiaDlRecognition : BaseRecognition() {

    private val frontRecognizer by lazy { ColombiaDlFrontRecognizer() }
    private val backRecognizer by lazy { buildId1CardDetectorRecognizer() }

    override fun getSingleSideRecognizers() = listOf<Recognizer<*, *>>(frontRecognizer, backRecognizer)

    override fun extractData(): String? {
        val result = frontRecognizer.result
        if (result.isEmpty()) {
            return null
        }

        result.apply {
            add(LICENCE_NUMBER, licenceNumber)
            add(FULL_NAME, name)
            add(DATE_OF_BIRTH, dateOfBirth)
            add(DATE_OF_ISSUE, dateOfIssue)
            add(DRIVER_RESTRICTIONS, driverRestrictions)
            add(ISSUING_AGENCY, issuingAgency)
        }

        return result.name
    }
}