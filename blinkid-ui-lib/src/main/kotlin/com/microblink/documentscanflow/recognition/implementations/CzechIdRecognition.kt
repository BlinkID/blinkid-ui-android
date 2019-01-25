package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.BaseTwoSideRecognition
import com.microblink.documentscanflow.recognition.ResultValidator
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.czechia.CzechiaCombinedRecognizer
import com.microblink.entities.recognizers.blinkid.czechia.CzechiaIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.czechia.CzechiaIdFrontRecognizer

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
        add(R.string.keyLastName, combinedResult.lastName)
        add(R.string.keyFirstName, combinedResult.firstName)
        add(R.string.keyDocumentNumber, combinedResult.identityCardNumber)
        add(R.string.keySex, combinedResult.sex)
        add(R.string.keyDateOfBirth, combinedResult.dateOfBirth)
        add(R.string.keyPlaceOfBirth, combinedResult.placeOfBirth)
        add(R.string.keyAddress, combinedResult.address)
        add(R.string.keyPersonalNumber, combinedResult.personalIdentificationNumber)
        add(R.string.keyIssuingAuthority, combinedResult.issuingAuthority)
        add(R.string.keyNationality, combinedResult.nationality)
        addDateOfExpiry(combinedResult.dateOfExpiry)
        add(R.string.keyIssueDate, combinedResult.dateOfIssue)
        add(R.string.keyIssuingAuthority, combinedResult.issuingAuthority)
    }

    private fun extractFrontSide() {
        add(R.string.keyLastName, frontResult.surname)
        add(R.string.keyFirstName, frontResult.givenNames)
        add(R.string.keyDocumentNumber, frontResult.documentNumber)
        add(R.string.keySex, frontResult.sex)
        add(R.string.keyPlaceOfBirth, frontResult.placeOfBirth)
        add(R.string.keyDateOfBirth, frontResult.dateOfBirth)
        add(R.string.keyIssueDate, frontResult.dateOfIssue)
        addDateOfExpiry(frontResult.dateOfExpiry.date)
    }
    
    private fun extractBackSide() {
        extractMrzResult(backResult.mrzResult)
        add(R.string.keyAddress, backResult.permanentStay)
        add(R.string.keyPersonalNumber, backResult.personalNumber)
        add(R.string.keyIssuingAuthority, backResult.authority)
    }
    
}