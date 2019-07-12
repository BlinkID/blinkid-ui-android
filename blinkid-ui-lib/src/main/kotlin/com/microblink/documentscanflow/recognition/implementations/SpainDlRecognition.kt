package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.recognition.SingleSideWithId1CardDetectorRecognition
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.blinkid.spain.SpainDlFrontRecognizer

class SpainDlRecognition : SingleSideWithId1CardDetectorRecognition<SpainDlFrontRecognizer.Result>() {

    override val recognizer by lazy { SpainDlFrontRecognizer() }

    override fun extractData(result: SpainDlFrontRecognizer.Result): String? {
        add(LAST_NAME, result.surname)
        add(FIRST_NAME, result.firstName)
        add(DATE_OF_BIRTH, result.dateOfBirth)
        add(PLACE_OF_BIRTH, result.placeOfBirth)
        add(DATE_OF_ISSUE, result.validFrom)
        addDateOfExpiry(result.validUntil.date)
        add(ISSUING_AUTHORITY, result.issuingAuthority)
        add(DOCUMENT_NUMBER, result.number)
        add(LICENCE_CATEGORIES, result.licenceCategories)

        return FormattingUtils.formatResultTitle(result.firstName, result.surname)
    }

}