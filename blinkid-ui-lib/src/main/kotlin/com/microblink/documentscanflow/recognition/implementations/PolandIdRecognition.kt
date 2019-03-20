package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.BaseTwoSideRecognition
import com.microblink.documentscanflow.recognition.ResultValidator
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.poland.PolandCombinedRecognizer
import com.microblink.entities.recognizers.blinkid.poland.PolandIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.poland.PolandIdFrontRecognizer

class PolandIdRecognition: BaseTwoSideRecognition() {

    val frontRecognizer by lazy { PolandIdFrontRecognizer() }
    val backRecognizer by lazy { PolandIdBackRecognizer() }

    val frontResult by lazy { frontRecognizer.result }
    val backResult by lazy { backRecognizer.result }

    val combinedRecognizer by lazy { PolandCombinedRecognizer() }
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
            extractBackSide()
        }
    }

    private fun extractCombinedResult() {
        add(LAST_NAME, combinedResult.surname)
        add(FIRST_NAME, combinedResult.givenNames)
        add(FAMILY_NAME, combinedResult.familyName)
        add(PARENT_NAMES, combinedResult.parentsGivenNames)
        add(PERSONAL_NUMBER, combinedResult.personalNumber)
        add(DOCUMENT_NUMBER, combinedResult.documentNumber)
        add(SEX, combinedResult.sex)
        add(DATE_OF_BIRTH, combinedResult.dateOfBirth)
        add(NATIONALITY, combinedResult.nationality)
        add(ISSUER, combinedResult.issuedBy)
        addDateOfExpiry(combinedResult.dateOfExpiry.date)
    }

    private fun extractFrontSide() {
        add(LAST_NAME, frontResult.surname)
        add(FIRST_NAME, frontResult.givenNames)
        add(SEX, frontResult.sex)
        add(DATE_OF_BIRTH, frontResult.dateOfBirth)
    }

    private fun extractBackSide() {
        extractMrzResult(backResult.mrzResult)
    }

    override fun getResultTitle(): String? {
        var firstName: String? = ""
        var lastName: String? = ""
        when {
            combinedResult.isNotEmpty() -> {
                firstName = combinedResult.givenNames
                lastName = combinedResult.surname
            }
            frontResult.isNotEmpty() -> {
                firstName = frontResult.givenNames
                lastName = frontResult.surname
            }
            backResult.isNotEmpty() -> {
                firstName = backResult.mrzResult.secondaryId
                lastName = backResult.mrzResult.primaryId
            }
        }
        return FormattingUtils.formatResultTitle(firstName, lastName)
    }

}