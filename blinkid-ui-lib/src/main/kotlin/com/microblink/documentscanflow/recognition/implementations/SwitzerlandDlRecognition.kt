package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.recognition.SingleSideWithId1CardDetectorRecognition
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.blinkid.switzerland.SwitzerlandDlFrontRecognizer

class SwitzerlandDlRecognition : SingleSideWithId1CardDetectorRecognition<SwitzerlandDlFrontRecognizer.Result>() {

    override val recognizer by lazy { SwitzerlandDlFrontRecognizer() }

    override fun extractData(result: SwitzerlandDlFrontRecognizer.Result): String? {
        add(FIRST_NAME, result.firstName)
        add(LAST_NAME, result.lastName)
        add(DATE_OF_BIRTH, result.dateOfBirth)
        add(PLACE_OF_BIRTH, result.placeOfBirth)
        add(DOCUMENT_NUMBER, result.licenseNumber)
        add(ISSUING_AUTHORITY, result.issuingAuthority)
        add(DATE_OF_ISSUE, result.dateOfIssue)
        addDateOfExpiry(result.dateOfExpiry.date)
        add(DATE_OF_EXPIRY_PERMANENT, result.isExpiryDatePermanent)
        add(LICENCE_CATEGORIES, result.vehicleCategories)

        return FormattingUtils.formatResultTitle(result.firstName, result.lastName)
    }

}