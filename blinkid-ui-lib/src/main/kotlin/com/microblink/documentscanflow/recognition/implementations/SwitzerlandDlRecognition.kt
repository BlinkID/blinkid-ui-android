package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.buildId1CardDetectorRecognizer
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.switzerland.SwitzerlandDlFrontRecognizer

class SwitzerlandDlRecognition : BaseRecognition() {

    private val frontRecognizer by lazy { SwitzerlandDlFrontRecognizer() }
    private val backRecognizer by lazy { buildId1CardDetectorRecognizer() }

    override fun getSingleSideRecognizers() = listOf<Recognizer<*>>(frontRecognizer, backRecognizer)

    override fun extractData(): String? {
        val result = frontRecognizer.result
        if (result.isEmpty()) {
            return null
        }

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