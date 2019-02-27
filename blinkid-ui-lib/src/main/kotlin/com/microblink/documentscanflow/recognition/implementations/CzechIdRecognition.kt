package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.BaseTwoSideRecognition
import com.microblink.documentscanflow.recognition.ResultValidator
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.czechia.CzechiaCombinedRecognizer
import com.microblink.entities.recognizers.blinkid.czechia.CzechiaIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.czechia.CzechiaIdFrontRecognizer
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*

class CzechIdRecognition : BaseTwoSideRecognition() {

    val frontRecognizer by lazy { CzechiaIdFrontRecognizer() }
    val backRecognizer by lazy { CzechiaIdBackRecognizer() }

    val frontResult by lazy { frontRecognizer.result }
    val backResult by lazy { backRecognizer.result }

    val combinedRecognizer by lazy { CzechiaCombinedRecognizer() }
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
       
        if(backResult.isNotEmpty()) {
            extractBackSide()
        }
    }

    override fun getResultTitle(): String? {
        if (combinedResult.isNotEmpty()) {
            return FormattingUtils.formatResultTitle(combinedResult.firstName, combinedResult.lastName)
        }

        if (backResult.isNotEmpty()) {
            return buildMrtdTitle(backResult.mrzResult)
        }

        if (frontResult.isNotEmpty()) {
            return FormattingUtils.formatResultTitle(frontResult.givenNames, frontResult.surname)
        }

        return null
    }

    private fun extractCombinedResult() {
        add(LAST_NAME, combinedResult.lastName)
        add(FIRST_NAME, combinedResult.firstName)
        add(DOCUMENT_NUMBER, combinedResult.identityCardNumber)
        add(SEX, combinedResult.sex)
        add(DATE_OF_BIRTH, combinedResult.dateOfBirth)
        add(PLACE_OF_BIRTH, combinedResult.placeOfBirth)
        add(ADDRESS, combinedResult.address)
        add(PERSONAL_NUMBER, combinedResult.personalIdentificationNumber)
        add(ISSUING_AUTHORITY, combinedResult.issuingAuthority)
        add(NATIONALITY, combinedResult.nationality)
        addDateOfExpiry(combinedResult.dateOfExpiry)
        add(DATE_OF_ISSUE, combinedResult.dateOfIssue)
        add(ISSUING_AUTHORITY, combinedResult.issuingAuthority)
    }

    private fun extractFrontSide() {
        add(LAST_NAME, frontResult.surname)
        add(FIRST_NAME, frontResult.givenNames)
        add(DOCUMENT_NUMBER, frontResult.documentNumber)
        add(SEX, frontResult.sex)
        add(PLACE_OF_BIRTH, frontResult.placeOfBirth)
        add(DATE_OF_BIRTH, frontResult.dateOfBirth)
        add(DATE_OF_ISSUE, frontResult.dateOfIssue)
        addDateOfExpiry(frontResult.dateOfExpiry.date)
    }
    
    private fun extractBackSide() {
        extractMrzResult(backResult.mrzResult)
        add(ADDRESS, backResult.permanentStay)
        add(PERSONAL_NUMBER, backResult.personalNumber)
        add(ISSUING_AUTHORITY, backResult.authority)
    }
    
}