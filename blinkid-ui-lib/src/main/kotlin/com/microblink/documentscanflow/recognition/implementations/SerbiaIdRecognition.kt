package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.BaseTwoSideRecognition
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.documentscanflow.recognition.ResultValidator
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.serbia.SerbiaCombinedRecognizer
import com.microblink.entities.recognizers.blinkid.serbia.SerbiaIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.serbia.SerbiaIdFrontRecognizer

class SerbiaIdRecognition: BaseTwoSideRecognition() {

    val frontRecognizer by lazy { SerbiaIdFrontRecognizer() }
    val backRecognizer by lazy { SerbiaIdBackRecognizer() }

    val frontResult by lazy { frontRecognizer.result }
    val backResult by lazy { backRecognizer.result }

    val combinedRecognizer by lazy { SerbiaCombinedRecognizer() }
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
        add(R.string.keyLastName, combinedResult.lastName)
        add(R.string.keyFirstName, combinedResult.firstName)
        add(R.string.keySex, combinedResult.sex)
        add(R.string.keyDateOfBirth, combinedResult.dateOfBirth)
        add(R.string.keyNationality, combinedResult.nationality)
        add(R.string.keyDocumentNumber, combinedResult.identityCardNumber)
        add(R.string.keyIssueDate, combinedResult.dateOfIssue)
        addDateOfExpiry(combinedResult.dateOfExpiry)
        add(R.string.keyIssuer, combinedResult.issuer)
    }

    override fun getResultTitle(): String? {
        if (combinedResult.isNotEmpty()) {
            return FormattingUtils.formatResultTitle(combinedResult.lastName, combinedResult.firstName)
        }

        if (backResult.isNotEmpty()) {
            return FormattingUtils.formatResultTitle(backResult.secondaryId, backResult.primaryId)
        }
        return null
    }
    
    private fun extractFrontSide() {
        if (frontResult.isEmpty()) {
            return
        }
        add(R.string.keyDocumentNumber, frontResult.documentNumber)
        add(R.string.keyIssueDate, frontResult.issuingDate)
        addDateOfExpiry(frontResult.validUntil)
    }

    private fun extractBackSide() {
        if (backResult.isEmpty()) {
            return
        }
        extractMrtdResult(backResult)
    }
}