package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.SingleSideWithId1CardDetectorRecognition
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.blinkid.austria.AustriaDlFrontRecognizer

class AustriaDlRecognition : SingleSideWithId1CardDetectorRecognition<AustriaDlFrontRecognizer.Result>() {

    override val recognizer by lazy { AustriaDlFrontRecognizer() }

    override fun extractData(result: AustriaDlFrontRecognizer.Result): String? {
        result.apply {
            add(LAST_NAME, name)
            add(FIRST_NAME, firstName)
            add(DATE_OF_BIRTH, dateOfBirth)
            add(PLACE_OF_BIRTH, placeOfBirth)
            add(DATE_OF_ISSUE, dateOfIssue)
            addDateOfExpiry(dateOfExpiry.date)
            add(ISSUING_AUTHORITY, issuingAuthority)
            add(LICENCE_NUMBER, licenceNumber)
            add(VEHICLE_CATEGORIES, vehicleCategories)
        }

        return FormattingUtils.formatResultTitle(result.firstName, result.name)
    }

}