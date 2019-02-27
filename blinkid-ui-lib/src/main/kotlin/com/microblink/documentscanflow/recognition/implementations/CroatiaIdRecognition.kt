package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.BaseTwoSideRecognition
import com.microblink.documentscanflow.recognition.ResultValidator
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.croatia.CroatiaCombinedRecognizer
import com.microblink.entities.recognizers.blinkid.croatia.CroatiaIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.croatia.CroatiaIdFrontRecognizer

class CroatiaIdRecognition
    : BaseTwoSideRecognition() {

    private val frontRecognizer by lazy { CroatiaIdFrontRecognizer() }
    private val backRecognizer by lazy { CroatiaIdBackRecognizer() }
    private val combinedRecognizer by lazy { CroatiaCombinedRecognizer() }

    private val frontResult by lazy { frontRecognizer.result }
    private val backResult by lazy { backRecognizer.result }
    private val combinedResult by lazy { combinedRecognizer.result }

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
            extractCombinedResult(combinedResult)
        }

        if (backResult.isNotEmpty()) {
            extractBackSide(backResult)
        }

        if (frontResult.isNotEmpty()) {
            extractFrontSide(frontResult)
        }
    }

    private fun extractCombinedResult(combinedResult: CroatiaCombinedRecognizer.Result) {
        add(LAST_NAME, combinedResult.lastName)
        add(FIRST_NAME, combinedResult.firstName)
        add(PERSONAL_NUMBER, combinedResult.oib)
        add(DOCUMENT_NUMBER, combinedResult.documentNumber)
        add(SEX, combinedResult.sex)
        add(DATE_OF_BIRTH, combinedResult.dateOfBirth)
        add(ADDRESS, combinedResult.residence)
        add(CITIZENSHIP, combinedResult.citizenship)
        add(ISSUING_AUTHORITY, combinedResult.issuedBy)
        add(DATE_OF_ISSUE, combinedResult.dateOfIssue)
        addDateOfExpiry(combinedResult.dateOfExpiry.date)
    }

    private fun extractFrontSide(frontResult: CroatiaIdFrontRecognizer.Result) {
        add(LAST_NAME, frontResult.lastName)
        add(FIRST_NAME, frontResult.firstName)
        add(DOCUMENT_NUMBER, frontResult.documentNumber)
        add(SEX, frontResult.sex)
        add(CITIZENSHIP, frontResult.citizenship)
        add(DATE_OF_BIRTH, frontResult.dateOfBirth)
        addDateOfExpiry(frontResult.dateOfExpiry.date)
    }

    private fun extractBackSide(backResult: CroatiaIdBackRecognizer.Result) {
        add(ADDRESS, backResult.residence)
        add(ISSUING_AUTHORITY, backResult.issuedBy)
        add(DATE_OF_ISSUE, backResult.dateOfIssue)
        extractMrzResult(backResult.mrzResult)
    }

    override fun getResultTitle(): String? {
        if (combinedResult.isNotEmpty()) {
            return FormattingUtils.formatResultTitle(combinedResult.firstName, combinedResult.lastName)
        }

        if (backResult.isNotEmpty()) {
            return buildMrtdTitle(backResult.mrzResult)
        }

        if (frontResult.isNotEmpty()) {
            return FormattingUtils.formatResultTitle(frontResult.firstName, frontResult.lastName)
        }

        return null
    }

}