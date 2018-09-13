package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.BaseTwoSideRecognition
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.documentscanflow.recognition.ResultValidator
import com.microblink.documentscanflow.sanitizeMRZString
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.jordan.JordanCombinedRecognizer
import com.microblink.entities.recognizers.blinkid.jordan.JordanIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.jordan.JordanIdFrontRecognizer

class JordanIdRecognition : BaseTwoSideRecognition() {

    val frontRecognizer by lazy { JordanIdFrontRecognizer() }
    val backRecognizer by lazy { JordanIdBackRecognizer() }

    val frontResult by lazy { frontRecognizer.result }
    val backResult by lazy { backRecognizer.result }

    val combinedRecognizer by lazy { JordanCombinedRecognizer() }
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
        add(R.string.keyFullName, combinedResult.name)
        add(R.string.keyNationalNumber, combinedResult.nationalNumber)
        add(R.string.keySex, combinedResult.sex)
        add(R.string.keyDateOfBirth, combinedResult.dateOfBirth)
        add(R.string.keyNationality, combinedResult.nationality)
        add(R.string.keyDocumentNumber, combinedResult.documentNumber)
        add(R.string.keyIssuer, combinedResult.issuer)
        addDateOfExpiry(combinedResult.dateOfExpiry)
    }

    private fun extractFrontSide() {
        if (frontResult.isEmpty()) {
            return
        }
        add(R.string.keyFullName, frontResult.name)
        add(R.string.keyNationalNumber, frontResult.nationalNumber)
        add(R.string.keySex, frontResult.sex)
        add(R.string.keyDateOfBirth, frontResult.dateOfBirth)
    }
    
    private fun extractBackSide() {
        if (backResult.isEmpty()) {
            return
        }
        add(R.string.keyNationality, backResult.nationality)
        add(R.string.keyDocumentNumber, backResult.documentNumber?.sanitizeMRZString())
        add(R.string.keyIssuer, backResult.issuer)
        addDateOfExpiry(backResult.dateOfExpiry)
        extractMrtdResult(backResult)
    }
    
    override fun getResultTitle(): String? {
        if (combinedResult.isNotEmpty()) {
            return combinedResult.name
        }
        if (frontResult.isNotEmpty()) {
            return frontResult.name
        }
        if (backResult.isNotEmpty()) {
            return FormattingUtils.formatResultTitle(backResult.primaryId, backResult.secondaryId)
        }
        return null
    }

}