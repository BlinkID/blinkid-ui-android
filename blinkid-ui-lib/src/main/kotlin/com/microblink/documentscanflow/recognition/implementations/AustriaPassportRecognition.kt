package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.documentscanflow.recognition.util.StringCombiner
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.austria.AustriaPassportRecognizer

class AustriaPassportRecognition : BaseRecognition() {

    val recognizer by lazy { AustriaPassportRecognizer() }

    override fun getSingleSideRecognizers(): List<Recognizer<*, *>> {
        return listOf(recognizer)
    }

    override fun extractData(): String? {
        val passResult = recognizer.result
        if (passResult.isEmpty()) {
            return null
        }

        val stringCombiner = StringCombiner(StringCombiner.Country.AUSTRIA)
        val firstName = stringCombiner.combineMRZString(passResult.mrzResult.secondaryId, passResult.givenName)
        val lastName = stringCombiner.combineMRZString(passResult.mrzResult.primaryId, passResult.surname)

        add(FIRST_NAME, firstName)
        add(LAST_NAME, lastName)
        add(NATIONALITY, passResult.nationality)
        add(PLACE_OF_BIRTH, passResult.placeOfBirth)
        add(SEX, passResult.sex)
        add(AUTHORITY, passResult.issuingAuthority)
        add(DOCUMENT_NUMBER, passResult.passportNumber.removeSuffix("<"))
        add(DATE_OF_BIRTH, passResult.dateOfBirth)
        add(DATE_OF_ISSUE, passResult.dateOfIssue)
        add(ISSUER, passResult.issuingAuthority)
        addDateOfExpiry(passResult.dateOfExpiry.date)

        return FormattingUtils.formatResultTitle(firstName, lastName)
    }

}