package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.BaseTwoSideRecognition
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.documentscanflow.recognition.ResultValidator
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.slovakia.SlovakiaCombinedRecognizer
import com.microblink.entities.recognizers.blinkid.slovakia.SlovakiaIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.slovakia.SlovakiaIdFrontRecognizer

class SlovakiaIdRecognition: BaseTwoSideRecognition() {

    val frontRecognizer by lazy { SlovakiaIdFrontRecognizer() }
    val backRecognizer by lazy { SlovakiaIdBackRecognizer() }

    val frontResult by lazy { frontRecognizer.result }
    val backResult by lazy { backRecognizer.result }

    val combinedRecognizer by lazy { SlovakiaCombinedRecognizer() }
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
        extractFrontSide()
        extractBackSide()
    }

    private fun extractCombinedResult() {
        add(LAST_NAME, combinedResult.lastName)
        add(FIRST_NAME, combinedResult.firstName)
        add(NATIONALITY, combinedResult.nationality)
        add(SEX, combinedResult.sex)
        add(DOCUMENT_NUMBER, combinedResult.documentNumber)
        add(SPECIAL_REMARKS, combinedResult.specialRemarks)
        add(PERSONAL_NUMBER, combinedResult.personalIdentificationNumber)
        add(DATE_OF_BIRTH, combinedResult.dateOfBirth)
        add(PLACE_OF_BIRTH, combinedResult.placeOfBirth)
        add(SURNAME_AT_BIRTH, combinedResult.surnameAtBirth)
        add(ADDRESS, combinedResult.address)
        add(ISSUING_AUTHORITY, combinedResult.issuingAuthority)
        add(DATE_OF_ISSUE, combinedResult.dateOfIssue)
        addDateOfExpiry(combinedResult.dateOfExpiry)
    }

    private fun extractFrontSide() {
        if (frontResult.isEmpty()) {
            return
        }

        add(LAST_NAME, frontResult.lastName)
        add(FIRST_NAME, frontResult.firstName)
        add(NATIONALITY, frontResult.nationality)
        add(SEX, frontResult.sex)
        add(DATE_OF_BIRTH, frontResult.dateOfBirth)
        add(PERSONAL_NUMBER, frontResult.personalNumber)
        add(DOCUMENT_NUMBER, frontResult.documentNumber)
        add(ISSUER, frontResult.issuedBy)
        addDateOfExpiry(frontResult.dateOfExpiry.date)
        add(DATE_OF_ISSUE, frontResult.dateOfIssue)
    }

    private fun extractBackSide() {
        if (backResult.isEmpty()) {
            return
        }

        add(ADDRESS, backResult.address)
        add(PLACE_OF_BIRTH, backResult.placeOfBirth)
        extractMrzResult(backResult.mrzResult)
    }
    
    override fun getResultTitle(): String? {
        if (combinedResult.isNotEmpty()) {
            return FormattingUtils.formatResultTitle(combinedResult.firstName, combinedResult.lastName)
        }
        if (frontResult.isNotEmpty()) {
            return FormattingUtils.formatResultTitle(frontResult.firstName, frontResult.lastName)
        }
        if (backResult.isNotEmpty()) {
            return buildMrtdTitle(backResult.mrzResult)
        }
        return null
    }

}