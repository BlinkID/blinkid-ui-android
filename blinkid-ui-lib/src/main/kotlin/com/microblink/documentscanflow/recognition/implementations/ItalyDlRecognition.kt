package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.buildId1CardDetectorRecognizer
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.italy.ItalyDlFrontRecognizer

class ItalyDlRecognition : BaseRecognition() {

    private val frontRecognizer by lazy { ItalyDlFrontRecognizer() }
    private val backRecognizer by lazy { buildId1CardDetectorRecognizer() }

    override fun getSingleSideRecognizers() = listOf<Recognizer<*>>(frontRecognizer, backRecognizer)

    override fun extractData(): String? {
        val result = frontRecognizer.result
        if (result.isEmpty()) {
            return null
        }

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