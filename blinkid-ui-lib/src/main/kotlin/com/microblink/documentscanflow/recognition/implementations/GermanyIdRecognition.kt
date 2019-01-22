package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.R
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
        add(R.string.keyLastName, combinedResult.lastName)
        add(R.string.keyFirstName, combinedResult.firstName)
        add(R.string.keyDocumentNumber, combinedResult.identityCardNumber)
        addDateOfExpiry(combinedResult.dateOfExpiry)
        add(R.string.keyDateOfBirth, combinedResult.dateOfBirth)
        add(R.string.keyNationality, combinedResult.nationality)
        add(R.string.keyPlaceOfBirth, combinedResult.placeOfBirth)
        add(R.string.keyEyeColor, combinedResult.eyeColor)
        add(R.string.keyHeight, combinedResult.height)
        add(R.string.keyAddress, combinedResult.address)
        add(R.string.keyAuthority, combinedResult.issuingAuthority)
        add(R.string.keyIssueDate, combinedResult.dateOfIssue)
    }

    private fun extractFrontSide() {
        add(R.string.keyLastName, frontResult.surname)
        add(R.string.keyFirstName, frontResult.givenNames)
        add(R.string.keyNationality, frontResult.nationality)
        add(R.string.keyPlaceOfBirth, frontResult.placeOfBirth)
        add(R.string.keyDateOfBirth, frontResult.dateOfBirth)
        add(R.string.keyDocumentNumber, frontResult.documentNumber)
        addDateOfExpiry(frontResult.dateOfExpiry.date)
    }

    private fun extractBackSide() {
        extractMrzResult(backResult.mrzResult)
        add(R.string.keyEyeColor, backResult.colourOfEyes)
        add(R.string.keyAuthority, backResult.authority)
        add(R.string.keyIssueDate, backResult.dateOfIssue)
        add(R.string.keyAddress, backResult.fullAddress)
        add(R.string.keyHeight, backResult.height)
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