package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.SingleSideWithId1CardDetectorRecognition
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.blinkid.sweden.SwedenDlFrontRecognizer

class SwedenDlRecognition : SingleSideWithId1CardDetectorRecognition<SwedenDlFrontRecognizer.Result>() {

    override val recognizer by lazy { SwedenDlFrontRecognizer() }

    override fun extractData(result: SwedenDlFrontRecognizer.Result): String? {
        add(DOCUMENT_NUMBER, result.licenceNumber)
        add(FIRST_NAME, result.name)
        add(LAST_NAME, result.surname)
        add(DATE_OF_BIRTH, result.dateOfBirth)
        add(ISSUING_AUTHORITY, result.issuingAgency)
        add(DATE_OF_ISSUE, result.dateOfIssue)
        addDateOfExpiry(result.dateOfExpiry.date)

        return FormattingUtils.formatResultTitle(result.name, result.surname)
    }

}