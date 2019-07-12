package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.TwoSideRecognition
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.blinkid.cyprus.CyprusOldIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.cyprus.CyprusOldIdFrontRecognizer

class CyprusOldIdRecognition : TwoSideRecognition<CyprusOldIdFrontRecognizer.Result, CyprusOldIdBackRecognizer.Result>() {

    override val frontRecognizer by lazy { CyprusOldIdFrontRecognizer() }
    override val backRecognizer by lazy { CyprusOldIdBackRecognizer() }

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