package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.BaseTwoSideRecognition
import com.microblink.documentscanflow.recognition.ResultValidator
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.cyprus.CyprusOldIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.cyprus.CyprusOldIdFrontRecognizer
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*

class CyprusOldIdRecognition : BaseTwoSideRecognition() {

    private val frontRecognizer by lazy { CyprusOldIdFrontRecognizer() }
    private val backRecognizer by lazy { CyprusOldIdBackRecognizer() }

    private val frontResult by lazy { frontRecognizer.result }
    private val backResult by lazy { backRecognizer.result }

    override fun createValidator() = ResultValidator()

    override fun extractFields() {
        if (frontResult.isNotEmpty()) {
            extractFront(frontResult)
        }

        if (backResult.isNotEmpty()) {
            extractBack(backResult)
        }
    }

    override fun getResultTitle(): String? {
        if (frontResult.isNotEmpty()) {
            return FormattingUtils.formatResultTitle(frontResult.name, frontResult.surname)
        }
        return null
    }

    override fun getSingleSideRecognizers() = listOf<Recognizer<*, *>>(frontRecognizer, backRecognizer)

    private fun extractFront(result: CyprusOldIdFrontRecognizer.Result) {
        add(IDENTITY_NUMBER, result.idNumber)
        add(DOCUMENT_NUMBER, result.documentNumber)
        add(FIRST_NAME, result.name)
        add(LAST_NAME, result.surname)
    }

    private fun extractBack(result: CyprusOldIdBackRecognizer.Result) {
        add(DATE_OF_BIRTH, result.dateOfBirth)
        add(SEX, result.sex)
        addDateOfExpiry(result.expiresOn.date)
    }
}