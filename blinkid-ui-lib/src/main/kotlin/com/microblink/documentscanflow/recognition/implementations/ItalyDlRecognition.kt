package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.recognition.SingleSideWithId1CardDetectorRecognition
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.blinkid.italy.ItalyDlFrontRecognizer

class ItalyDlRecognition : SingleSideWithId1CardDetectorRecognition<ItalyDlFrontRecognizer.Result>() {

    override val recognizer by lazy { ItalyDlFrontRecognizer() }

    override fun extractData(result: ItalyDlFrontRecognizer.Result): String? {
        result.apply {
            add(LAST_NAME, surname)
            add(FIRST_NAME, givenName)
            add(DATE_OF_BIRTH, dateOfBirth)
            add(PLACE_OF_BIRTH, placeOfBirth)
            add(DATE_OF_ISSUE, dateOfIssue)
            add(ISSUING_AUTHORITY, issuingAuthority)
            addDateOfExpiry(dateOfExpiry.date)
            add(LICENCE_NUMBER, licenceNumber)
            add(LICENCE_CATEGORIES, licenceCategories)
            add(ADDRESS, address)
        }

        return FormattingUtils.formatResultTitle(result.givenName, result.surname)
    }

}