package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.BaseTwoSideRecognition
import com.microblink.documentscanflow.recognition.ResultValidator
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.cyprus.CyprusIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.cyprus.CyprusIdFrontRecognizer
import com.microblink.entities.recognizers.blinkid.cyprus.CyprusOldIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.cyprus.CyprusOldIdFrontRecognizer

class CyprusIdRecognition : BaseTwoSideRecognition() {

    private val frontRecognizer by lazy { CyprusIdFrontRecognizer() }
    private val backRecognizer by lazy { CyprusIdBackRecognizer() }

    private val frontResult by lazy { frontRecognizer.result }
    private val backResult by lazy { backRecognizer.result }

    override fun createValidator() = ResultValidator().match("", "")

    override fun extractFields() {
        if (frontResult.isNotEmpty()) {
            extractFront(frontResult)
        }

        if (backResult.isNotEmpty()) {
            extractBack(backResult)
        }
    }

    override fun getResultTitle(): String? {
        if (backResult.isNotEmpty()) {
            return buildMrtdTitle(backResult.mrzResult)
        }
        return null
    }

    override fun getSingleSideRecognizers() = listOf<Recognizer<*, *>>(frontRecognizer, backRecognizer)

    private fun extractFront(result: CyprusIdFrontRecognizer.Result) {
        add(R.string.keyIdentityNumber, result.idNumber)
    }

    private fun extractBack(result: CyprusIdBackRecognizer.Result) {
        extractMrzResult(result.mrzResult)
    }

}