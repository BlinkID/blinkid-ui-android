package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.BaseTwoSideRecognition
import com.microblink.documentscanflow.recognition.ResultValidator
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.austria.AustriaCombinedRecognizer
import com.microblink.entities.recognizers.blinkid.austria.AustriaIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.austria.AustriaIdFrontRecognizer

class AustriaIdRecognition
    : BaseTwoSideRecognition() {

    val frontRecognizer by lazy { AustriaIdFrontRecognizer() }
    val backRecognizer by lazy { AustriaIdBackRecognizer() }

    val frontResult by lazy { frontRecognizer.result }
    val backResult by lazy { backRecognizer.result }

    val combinedRecognizer by lazy { AustriaCombinedRecognizer() }
    val combinedResult by lazy { combinedRecognizer.result }

    override fun getSingleSideRecognizers(): List<Recognizer<*, *>> {
        return listOf(frontRecognizer, backRecognizer)
    }

    override fun getCombinedRecognizer(): Recognizer<*, *>? {
        return combinedRecognizer
    }

    override fun createValidator(): ResultValidator {
        return ResultValidator().match(combinedResult)
    }

    override fun extractFields() {
        if (combinedResult.isNotEmpty()) {
            extractCombinedResult()
        }

        if (frontResult.isNotEmpty()) {
            extractFrontSide()
        }

        if (backResult.isNotEmpty()) {
            extractMrzResult(backResult.mrzResult)
            extractBackSideNonMRZ()
        }
    }

    private fun extractCombinedResult() {
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
    }

    private fun extractFrontSide() {
        add(LAST_NAME, frontResult.surname)
        add(FIRST_NAME, frontResult.givenName)
        add(DOCUMENT_NUMBER, frontResult.documentNumber)
        add(SEX, frontResult.sex)
        add(DATE_OF_BIRTH, frontResult.dateOfBirth)
    }

    private fun extractBackSideNonMRZ() {
        add(PLACE_OF_BIRTH, backResult.placeOfBirth)
        add(ISSUING_AUTHORITY, backResult.issuingAuthority)
        add(DATE_OF_ISSUE, backResult.dateOfIssuance)
    }

    override fun getResultTitle(): String? {
        if (combinedResult.isNotEmpty()) {
            return FormattingUtils.formatResultTitle(combinedResult.givenName, combinedResult.surname)
        }

        if (frontResult.isNotEmpty()) {
            return FormattingUtils.formatResultTitle(frontResult.givenName, frontResult.surname)
        }

        if (backResult.isNotEmpty()) {
            return buildMrtdTitle(backResult.mrzResult)
        }

        return null
    }

}