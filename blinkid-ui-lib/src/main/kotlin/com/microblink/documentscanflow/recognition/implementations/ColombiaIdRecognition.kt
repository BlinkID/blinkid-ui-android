package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.BaseTwoSideRecognition
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.documentscanflow.recognition.ResultValidator
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.colombia.ColombiaIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.colombia.ColombiaIdFrontRecognizer

class ColombiaIdRecognition
    : BaseTwoSideRecognition() {

    val frontRecognizer by lazy { ColombiaIdFrontRecognizer() }
    val backRecognizer by lazy { ColombiaIdBackRecognizer() }

    val frontResult by lazy { frontRecognizer.result }
    val backResult by lazy { backRecognizer.result  }

    override fun getSingleSideRecognizers(): List<Recognizer<*, *>> {
        return listOf(frontRecognizer, backRecognizer)
    }

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
        add(R.string.keyFirstName, result.firstName)
        add(R.string.keyLastName, result.lastName)
        add(R.string.keyDocumentNumber, result.documentNumber)
    }

    private fun extractBack(result: ColombiaIdBackRecognizer.Result) {
        add(R.string.keyFirstName, result.firstName)
        add(R.string.keyLastName, result.lastName)
        add(R.string.keyDocumentNumber, result.documentNumber)
        add(R.string.keySex, result.sex)
        add(R.string.keyDateOfBirth, result.birthDate)
    }

}