package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.buildId1CardDetectorRecognizer
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.italy.ItalyDlFrontRecognizer

class ItalyDlRecognition : BaseRecognition() {

    private val frontRecognizer by lazy { ItalyDlFrontRecognizer() }
    private val backRecognizer by lazy { buildId1CardDetectorRecognizer() }

    override fun getSingleSideRecognizers() = listOf<Recognizer<*, *>>(frontRecognizer, backRecognizer)

    override fun extractData(): String? {
        val result = frontRecognizer.result
        if (result.isEmpty()) {
            return null
        }

        result.apply {
            add(R.string.keyLastName, surname)
            add(R.string.keyFirstName, givenName)
            add(R.string.keyDateOfBirth, dateOfBirth)
            add(R.string.keyPlaceOfBirth, placeOfBirth)
            add(R.string.keyIssueDate, dateOfIssue)
            add(R.string.keyIssuingAuthority, issuingAuthority)
            addDateOfExpiry(dateOfExpiry.date)
            add(R.string.keyLicenceNumber, licenceNumber)
            add(R.string.keyLicenceCategories, licenceCategories)
            add(R.string.keyAddress, address)
        }

        return FormattingUtils.formatResultTitle(result.givenName, result.surname)
    }
}