package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.documentscanflow.recognition.util.StringCombiner
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.switzerland.SwitzerlandPassportRecognizer

class SwitzerlandPassportRecognition: BaseRecognition() {

    private val mStringCombiner by lazy { StringCombiner(StringCombiner.Country.SWITZERLAND) }
    private val recognizer by lazy { SwitzerlandPassportRecognizer() }

    override fun getSingleSideRecognizers(): List<Recognizer<*>> {
        return listOf(recognizer)
    }

    override fun extractData(): String? {
        val result = recognizer.result
        if (result.isEmpty()) {
            return null
        }

        val firstName = mStringCombiner.combineMRZString(result.mrzResult.secondaryId, result.givenName)
        val lastName = mStringCombiner.combineMRZString(result.mrzResult.primaryId, result.surname)

        add(FIRST_NAME, firstName)
        add(LAST_NAME, lastName)
        add(PASSPORT_NUMBER, result.passportNumber)
        add(NATIONALITY, result.mrzResult.nationality)
        add(DATE_OF_BIRTH, result.dateOfBirth)
        add(PLACE_OF_ORIGIN, result.placeOfOrigin)
        add(DATE_OF_ISSUE, result.dateOfIssue)
        addDateOfExpiry(result.dateOfExpiry.date)
        add(SEX, result.sex)
        add(AUTHORITY, result.authority)

        return FormattingUtils.formatResultTitle(firstName, lastName)
    }

}