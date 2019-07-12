package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.buildTitle
import com.microblink.documentscanflow.recognition.CombinedRecognition
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.blinkid.austria.AustriaCombinedRecognizer
import com.microblink.entities.recognizers.blinkid.austria.AustriaIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.austria.AustriaIdFrontRecognizer

class AustriaIdRecognition
    : CombinedRecognition<AustriaIdFrontRecognizer.Result, AustriaIdBackRecognizer.Result, AustriaCombinedRecognizer.Result>() {

    override val frontRecognizer by lazy { AustriaIdFrontRecognizer() }
    override val backRecognizer by lazy { AustriaIdBackRecognizer() }
    override val combinedRecognizer by lazy { AustriaCombinedRecognizer() }

    override fun extractCombinedResult(combinedResult: AustriaCombinedRecognizer.Result): String? {
        add(LAST_NAME, combinedResult.surname)
        add(FIRST_NAME, combinedResult.givenName)
        add(DOCUMENT_NUMBER, combinedResult.documentNumber)
        add(SEX, combinedResult.sex)
        add(DATE_OF_BIRTH, combinedResult.dateOfBirth)
        add(HEIGHT, combinedResult.height)
        add(EYE_COLOR, combinedResult.eyeColour)
        add(PLACE_OF_BIRTH, combinedResult.placeOfBirth)
        add(NATIONALITY, combinedResult.nationality)
        add(ISSUING_AUTHORITY, combinedResult.issuingAuthority)
        add(DATE_OF_ISSUE, combinedResult.dateOfIssuance)
        add(PRINCIPAL_RESIDENCE_AT_ISSUANCE, combinedResult.principalResidence)
        addDateOfExpiry(combinedResult.dateOfExpiry.date)
        return FormattingUtils.formatResultTitle(combinedResult.givenName, combinedResult.surname)
    }

    override fun extractFrontResult(frontResult: AustriaIdFrontRecognizer.Result): String? {
        add(LAST_NAME, frontResult.surname)
        add(FIRST_NAME, frontResult.givenName)
        add(DOCUMENT_NUMBER, frontResult.documentNumber)
        add(SEX, frontResult.sex)
        add(DATE_OF_BIRTH, frontResult.dateOfBirth)
        return FormattingUtils.formatResultTitle(frontResult.givenName, frontResult.surname)
    }

    override fun extractBackResult(backResult: AustriaIdBackRecognizer.Result): String? {
        extractMrzResult(backResult.mrzResult)
        add(PLACE_OF_BIRTH, backResult.placeOfBirth)
        add(ISSUING_AUTHORITY, backResult.issuingAuthority)
        add(DATE_OF_ISSUE, backResult.dateOfIssuance)
        return backResult.mrzResult.buildTitle()
    }

}