package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.SingleSideRecognition
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.documentscanflow.recognition.util.StringCombiner
import com.microblink.entities.recognizers.blinkid.austria.AustriaPassportRecognizer

class AustriaPassportRecognition : SingleSideRecognition<AustriaPassportRecognizer.Result>() {

    override val recognizer by lazy { AustriaPassportRecognizer() }

    override fun extractData(result: AustriaPassportRecognizer.Result): String? {
        val stringCombiner = StringCombiner(StringCombiner.Country.AUSTRIA)
        val firstName = stringCombiner.combineMRZString(result.mrzResult.secondaryId, result.givenName)
        val lastName = stringCombiner.combineMRZString(result.mrzResult.primaryId, result.surname)

        add(FIRST_NAME, firstName)
        add(LAST_NAME, lastName)
        add(NATIONALITY, result.nationality)
        add(PLACE_OF_BIRTH, result.placeOfBirth)
        add(SEX, result.sex)
        add(DOCUMENT_NUMBER, result.passportNumber.removeSuffix("<"))
        add(DATE_OF_BIRTH, result.dateOfBirth)
        add(DATE_OF_ISSUE, result.dateOfIssue)
        add(ISSUER, result.issuingAuthority)
        addDateOfExpiry(result.dateOfExpiry.date)

        return FormattingUtils.formatResultTitle(firstName, lastName)
    }

}