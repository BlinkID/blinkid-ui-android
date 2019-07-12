package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.ResultValidator
import com.microblink.documentscanflow.recognition.TwoSideRecognition
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.blinkid.colombia.ColombiaIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.colombia.ColombiaIdFrontRecognizer

class ColombiaIdRecognition
    : TwoSideRecognition<ColombiaIdFrontRecognizer.Result, ColombiaIdBackRecognizer.Result>() {

    override val frontRecognizer by lazy { ColombiaIdFrontRecognizer() }
    override val backRecognizer by lazy { ColombiaIdBackRecognizer() }

    override fun createValidator(): ResultValidator {
        val frontDocNumberWithoutDots = frontResult.documentNumber.replace(".", "")
        return ResultValidator().match(frontDocNumberWithoutDots, backResult.documentNumber)
    }

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
            return FormattingUtils.formatResultTitle(frontResult.firstName, frontResult.lastName)
        } else if (backResult.isNotEmpty()) {
            return FormattingUtils.formatResultTitle(backResult.firstName, backResult.lastName)
        }

        return null
    }

    private fun extractFront(result: ColombiaIdFrontRecognizer.Result) {
        add(FIRST_NAME, result.firstName)
        add(LAST_NAME, result.lastName)
        add(DOCUMENT_NUMBER, result.documentNumber)
    }

    private fun extractBack(result: ColombiaIdBackRecognizer.Result) {
        add(FIRST_NAME, result.firstName)
        add(LAST_NAME, result.lastName)
        add(DOCUMENT_NUMBER, result.documentNumber)
        add(SEX, result.sex)
        add(DATE_OF_BIRTH, result.birthDate)
    }

}