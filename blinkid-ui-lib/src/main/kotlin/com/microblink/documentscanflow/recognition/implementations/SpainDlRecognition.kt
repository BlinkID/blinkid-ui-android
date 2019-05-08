package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.buildId1CardDetectorRecognizer
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.spain.SpainDlFrontRecognizer

class SpainDlRecognition : BaseRecognition() {

    private val frontRecognizer by lazy { SpainDlFrontRecognizer() }
    private val backRecognizer by lazy { buildId1CardDetectorRecognizer() }

    override fun getSingleSideRecognizers() = listOf<Recognizer<*>>(frontRecognizer, backRecognizer)

    override fun extractData(): String? {
        val result = frontRecognizer.result

        if (result.isEmpty()) {
            return null
        }

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