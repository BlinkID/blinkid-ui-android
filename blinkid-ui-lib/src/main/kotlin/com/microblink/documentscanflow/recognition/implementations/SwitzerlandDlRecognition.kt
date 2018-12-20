package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.buildId1CardDetectorRecognizer
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.switzerland.SwitzerlandDlFrontRecognizer

class SwitzerlandDlRecognition : BaseRecognition() {

    private val frontRecognizer by lazy { SwitzerlandDlFrontRecognizer() }
    private val backRecognizer by lazy { buildId1CardDetectorRecognizer() }

    override fun getSingleSideRecognizers() = listOf<Recognizer<*, *>>(frontRecognizer, backRecognizer)

    override fun extractData(): String? {
        val result = frontRecognizer.result
        if (result.isEmpty()) {
            return null
        }

        add(R.string.keyFirstName, result.firstName)
        add(R.string.keyLastName, result.lastName)
        add(R.string.keyDateOfBirth, result.dateOfBirth)
        add(R.string.keyPlaceOfBirth, result.placeOfBirth)
        add(R.string.keyDocumentNumber, result.licenseNumber)
        add(R.string.keyIssuingAuthority, result.issuingAuthority)
        add(R.string.keyIssueDate, result.dateOfIssue)
        addDateOfExpiry(result.dateOfExpiry.date)
        add(R.string.keyDateOfExpiryPermanent, result.isExpiryDatePermanent)
        add(R.string.keyLicenceCategories, result.vehicleCategories)

        return FormattingUtils.formatResultTitle(result.firstName, result.lastName)
    }

}