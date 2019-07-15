package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.SingleSideWithId1CardDetectorRecognition
import com.microblink.entities.recognizers.blinkid.colombia.ColombiaDlFrontRecognizer
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*

class ColombiaDlRecognition : SingleSideWithId1CardDetectorRecognition<ColombiaDlFrontRecognizer.Result>() {

    override val recognizer by lazy { ColombiaDlFrontRecognizer() }

    override fun extractData(result: ColombiaDlFrontRecognizer.Result): String? {
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