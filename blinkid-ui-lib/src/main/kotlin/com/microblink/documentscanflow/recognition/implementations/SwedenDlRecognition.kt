package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.buildId1CardDetectorRecognizer
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.sweden.dl.SwedenDlFrontRecognizer

class SwedenDlRecognition : BaseRecognition() {

    private val frontRecognizer by lazy { SwedenDlFrontRecognizer() }
    private val backRecognizer by lazy { buildId1CardDetectorRecognizer() }

    override fun getSingleSideRecognizers(): List<Recognizer<*, *>> {
        return listOf(frontRecognizer, backRecognizer)
    }

    override fun extractData(): String? {
        val result = frontRecognizer.result
        if (result.isEmpty()) {
            return null
        }

        add(R.string.keyDocumentNumber, result.licenceNumber)
        add(R.string.keyFirstName, result.name)
        add(R.string.keyLastName, result.surname)
        add(R.string.keyDateOfBirth, result.dateOfBirth)
        add(R.string.keyIssuingAuthority, result.issuingAgency)
        add(R.string.keyIssueDate, result.dateOfIssue)
        addDateOfExpiry(result.dateOfExpiry)

        return FormattingUtils.formatResultTitle(result.name, result.surname)
    }

}