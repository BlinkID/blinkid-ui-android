package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.buildId1CardDetectorRecognizer
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.colombia.ColombiaDlFrontRecognizer

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
            add(R.string.keyLicenceNumber, licenceNumber)
            add(R.string.keyFullName, name)
            add(R.string.keyDateOfBirth, dateOfBirth)
            add(R.string.keyIssueDate, dateOfIssue)
            add(R.string.keyDriverRestrictions, driverRestrictions)
            add(R.string.keyIssuingAgency, issuingAgency)
        }

        return result.name
    }
}