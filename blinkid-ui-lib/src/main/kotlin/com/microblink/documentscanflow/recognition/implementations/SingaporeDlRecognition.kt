package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.recognition.SingleSideWithId1CardDetectorRecognition
import com.microblink.entities.recognizers.blinkid.singapore.SingaporeDlFrontRecognizer

class SingaporeDlRecognition : SingleSideWithId1CardDetectorRecognition<SingaporeDlFrontRecognizer.Result>() {

    override val recognizer by lazy { SingaporeDlFrontRecognizer() }

    override fun extractData(result: SingaporeDlFrontRecognizer.Result): String? {
        add(DOCUMENT_NUMBER, result.licenceNumber)
        add(FULL_NAME, result.name)
        add(DATE_OF_BIRTH, result.birthDate)
        add(DATE_OF_ISSUE, result.issueDate)
        addDateOfExpiry(result.validTill.date)

        return result.name
    }

}