package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
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
        add(FULL_NAME, combinedResult.name)
        add(NATIONAL_NUMBER, combinedResult.nationalNumber)
        add(SEX, combinedResult.sex)
        add(DATE_OF_BIRTH, combinedResult.dateOfBirth)
        add(NATIONALITY, combinedResult.nationality)
        add(DOCUMENT_NUMBER, combinedResult.documentNumber)
        add(ISSUER, combinedResult.issuer)
        addDateOfExpiry(combinedResult.dateOfExpiry)
    }

    private fun extractFrontSide() {
        if (frontResult.isEmpty()) {
            return
        }
        add(FULL_NAME, frontResult.name)
        add(NATIONAL_NUMBER, frontResult.nationalNumber)
        add(SEX, frontResult.sex)
        add(DATE_OF_BIRTH, frontResult.dateOfBirth)
    }
    
    private fun extractBackSide() {
        if (backResult.isEmpty()) {
            return
        }
        add(NATIONALITY, backResult.nationality)
        add(DOCUMENT_NUMBER, backResult.documentNumber?.sanitizeMRZString())
        add(ISSUER, backResult.issuer)
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