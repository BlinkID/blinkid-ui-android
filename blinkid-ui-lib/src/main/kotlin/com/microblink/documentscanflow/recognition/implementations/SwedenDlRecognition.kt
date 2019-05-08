package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.buildId1CardDetectorRecognizer
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.sweden.SwedenDlFrontRecognizer

class SwedenDlRecognition : BaseRecognition() {

    private val frontRecognizer by lazy { SwedenDlFrontRecognizer() }
    private val backRecognizer by lazy { buildId1CardDetectorRecognizer() }

    override fun getSingleSideRecognizers(): List<Recognizer<*>> {
        return listOf(frontRecognizer, backRecognizer)
    }

    override fun extractData(): String? {
        val result = frontRecognizer.result
        if (result.isEmpty()) {
            return null
        }

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