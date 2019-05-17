package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.SingleSideWithId1CardDetectorRecognition
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.blinkid.newzealand.NewZealandDlFrontRecognizer

class NewZealandDlRecognition : SingleSideWithId1CardDetectorRecognition<NewZealandDlFrontRecognizer.Result>() {

    override val recognizer by lazy { NewZealandDlFrontRecognizer() }

    override fun extractData(result: NewZealandDlFrontRecognizer.Result): String? {
        val firstName = result.firstNames
        val lastName = result.surname

        add(FIRST_NAME, firstName)
        add(LAST_NAME, lastName)
        add(DATE_OF_BIRTH, result.dateOfBirth)
        add(DOCUMENT_NUMBER, result.licenseNumber)
        add(DATE_OF_ISSUE, result.dateOfIssue)
        addDateOfExpiry(result.dateOfExpiry.date)
        add(ADDRESS, result.address)

        return FormattingUtils.formatResultTitle(firstName, lastName)
    }
    
}