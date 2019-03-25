package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.BaseTwoSideRecognition
import com.microblink.documentscanflow.recognition.ResultValidator
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.slovenia.SloveniaCombinedRecognizer
import com.microblink.entities.recognizers.blinkid.slovenia.SloveniaIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.slovenia.SloveniaIdFrontRecognizer

class SloveniaIdRecognition: BaseTwoSideRecognition() {

    val frontRecognizer by lazy { SloveniaIdFrontRecognizer() }
    val backRecognizer by lazy { SloveniaIdBackRecognizer() }

    val frontResult by lazy { frontRecognizer.result }
    val backResult by lazy { backRecognizer.result }

    val combinedRecognizer by lazy { SloveniaCombinedRecognizer() }
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
        add(IDENTITY_NUMBER, combinedResult.documentNumber)
        add(SEX, combinedResult.sex)
        add(SEX, combinedResult.sex)
        add(DATE_OF_BIRTH, combinedResult.dateOfBirth)
        add(ADDRESS, combinedResult.address)
        add(NATIONALITY, combinedResult.nationality)
        addDateOfExpiry(combinedResult.dateOfExpiry.date)
        add(DATE_OF_ISSUE, combinedResult.dateOfIssue)
        add(ISSUING_AUTHORITY, combinedResult.administrativeUnit)
    }

    private fun extractFrontSide() {
        add(LAST_NAME, frontResult.surname)
        add(FIRST_NAME, frontResult.givenNames)
        add(SEX, frontResult.sex)
        add(NATIONALITY, frontResult.nationality)
        add(DATE_OF_BIRTH, frontResult.dateOfBirth)
        addDateOfExpiry(frontResult.dateOfExpiry.date)
    }

    private fun extractBackSide() {
        extractMrzResult(backResult.mrzResult)
        add(ADDRESS, backResult.address)
        add(AUTHORITY, backResult.administrativeUnit)
        add(DATE_OF_ISSUE, backResult.dateOfIssue)
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