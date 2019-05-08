package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.buildId1CardDetectorRecognizer
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.ireland.IrelandDlFrontRecognizer

class IrelandDlRecognition : BaseRecognition() {

    private val frontRecognizer by lazy { IrelandDlFrontRecognizer() }
    private val backRecognizer by lazy { buildId1CardDetectorRecognizer() }

    override fun getSingleSideRecognizers() = listOf<Recognizer<*>>(frontRecognizer, backRecognizer)

    override fun extractData(): String? {
        val result = frontRecognizer.result
        if (result.isEmpty()) {
            return null
        }

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