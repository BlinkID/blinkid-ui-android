package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.recognition.SingleSideWithId1CardDetectorRecognition
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.blinkid.ireland.IrelandDlFrontRecognizer

class IrelandDlRecognition : SingleSideWithId1CardDetectorRecognition<IrelandDlFrontRecognizer.Result>() {

    override val recognizer by lazy { IrelandDlFrontRecognizer() }

    override fun extractData(result: IrelandDlFrontRecognizer.Result): String? {
        result.apply {
            add(LAST_NAME, surname)
            add(FIRST_NAME, firstName)
            add(DATE_OF_BIRTH, dateOfBirth)
            add(PLACE_OF_BIRTH, placeOfBirth)
            add(DATE_OF_ISSUE, dateOfIssue)
            add(ISSUER, issuedBy)
            addDateOfExpiry(dateOfExpiry.date)
            add(LICENCE_NUMBER, licenceNumber)
            add(ADDRESS, address)
            add(LICENCE_CATEGORIES, licenceCategories)
        }

        return FormattingUtils.formatResultTitle(result.firstName, result.surname)
    }

}