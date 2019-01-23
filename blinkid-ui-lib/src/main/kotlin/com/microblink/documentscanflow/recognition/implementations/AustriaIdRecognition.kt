package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.BaseTwoSideRecognition
import com.microblink.documentscanflow.recognition.ResultValidator
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
        add(R.string.keyLastName, combinedResult.surname)
        add(R.string.keyFirstName, combinedResult.givenName)
        add(R.string.keyDocumentNumber, combinedResult.documentNumber)
        add(R.string.keySex, combinedResult.sex)
        add(R.string.keyDateOfBirth, combinedResult.dateOfBirth)
        add(R.string.keyHeight, combinedResult.height)
        add(R.string.keyEyeColor, combinedResult.eyeColour)
        add(R.string.keyPlaceOfBirth, combinedResult.placeOfBirth)
        add(R.string.keyNationality, combinedResult.nationality)
        add(R.string.keyIssuingAuthority, combinedResult.issuingAuthority)
        add(R.string.keyIssueDate, combinedResult.dateOfIssuance)
        add(R.string.keyPrincipalResidenceAtIssuance, combinedResult.principalResidence)
        addDateOfExpiry(combinedResult.dateOfExpiry.date)
    }

    private fun extractFrontSide() {
        add(R.string.keyLastName, frontResult.surname)
        add(R.string.keyFirstName, frontResult.givenName)
        add(R.string.keyDocumentNumber, frontResult.documentNumber)
        add(R.string.keySex, frontResult.sex)
        add(R.string.keyDateOfBirth, frontResult.dateOfBirth)
    }

    private fun extractBackSideNonMRZ() {
        add(R.string.keyPlaceOfBirth, backResult.placeOfBirth)
        add(R.string.keyIssuingAuthority, backResult.issuingAuthority)
        add(R.string.keyIssueDate, backResult.dateOfIssuance)
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