package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.BaseTwoSideRecognition
import com.microblink.documentscanflow.recognition.ResultValidator
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.germany.GermanyCombinedRecognizer
import com.microblink.entities.recognizers.blinkid.germany.GermanyIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.germany.GermanyIdFrontRecognizer

class GermanyIdRecognition : BaseTwoSideRecognition() {

    val frontRecognizer by lazy { GermanyIdFrontRecognizer() }
    val backRecognizer by lazy { GermanyIdBackRecognizer() }

    val frontResult by lazy { frontRecognizer.result }
    val backResult by lazy { backRecognizer.result }

    val combinedRecognizer by lazy { GermanyCombinedRecognizer() }
    val combinedResult by lazy { combinedRecognizer.result }

    override fun getSingleSideRecognizers(): List<Recognizer<*, *>> {
        return listOf(frontRecognizer, backRecognizer)
    }

    override fun getCombinedRecognizer(): Recognizer<*, *>? {
        return combinedRecognizer
    }

    override fun createValidator(): ResultValidator {
        return ResultValidator()
    }

    override fun extractFields() {
        if (combinedResult.isNotEmpty()) {
            extractCombinedResult()
        }
        if (frontResult.isNotEmpty()) {
            extractFrontSide()
        }
        if (backResult.isNotEmpty()) {
            extractBackSide()
        }
    }

    private fun extractCombinedResult() {
        add(LAST_NAME, combinedResult.lastName)
        add(FIRST_NAME, combinedResult.firstName)
        add(DOCUMENT_NUMBER, combinedResult.identityCardNumber)
        addDateOfExpiry(combinedResult.dateOfExpiry)
        add(DATE_OF_BIRTH, combinedResult.dateOfBirth)
        add(NATIONALITY, combinedResult.nationality)
        add(PLACE_OF_BIRTH, combinedResult.placeOfBirth)
        add(EYE_COLOR, combinedResult.eyeColor)
        add(HEIGHT, combinedResult.height)
        add(ADDRESS, combinedResult.address)
        add(AUTHORITY, combinedResult.issuingAuthority)
        add(DATE_OF_ISSUE, combinedResult.dateOfIssue)
    }

    private fun extractFrontSide() {
        add(LAST_NAME, frontResult.surname)
        add(FIRST_NAME, frontResult.givenNames)
        add(NATIONALITY, frontResult.nationality)
        add(PLACE_OF_BIRTH, frontResult.placeOfBirth)
        add(DATE_OF_BIRTH, frontResult.dateOfBirth)
        add(DOCUMENT_NUMBER, frontResult.documentNumber)
        addDateOfExpiry(frontResult.dateOfExpiry.date)
    }

    private fun extractBackSide() {
        extractMrzResult(backResult.mrzResult)
        add(AUTHORITY, backResult.authority)
        add(DATE_OF_ISSUE, backResult.dateOfIssue)
        add(ADDRESS, backResult.fullAddress)
    }

    override fun getResultTitle(): String? {
        return when {
            combinedResult.isNotEmpty() -> FormattingUtils.formatResultTitle(combinedResult.firstName, combinedResult.lastName)
            frontResult.isNotEmpty() -> FormattingUtils.formatResultTitle(frontResult.givenNames, frontResult.surname)
            backResult.isNotEmpty() -> buildMrtdTitle(backResult.mrzResult)
            else -> null
        }
    }
    
}