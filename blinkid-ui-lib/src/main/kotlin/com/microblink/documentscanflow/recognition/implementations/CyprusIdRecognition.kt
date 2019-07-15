package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.buildTitle
import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.TwoSideRecognition
import com.microblink.documentscanflow.recognition.extract
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.IDENTITY_NUMBER
import com.microblink.entities.recognizers.blinkid.cyprus.CyprusIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.cyprus.CyprusIdFrontRecognizer

class CyprusIdRecognition : TwoSideRecognition<CyprusIdFrontRecognizer.Result, CyprusIdBackRecognizer.Result>() {

    override val frontRecognizer by lazy { CyprusIdFrontRecognizer() }
    override val backRecognizer by lazy { CyprusIdBackRecognizer() }

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
            return backResult.mrzResult.buildTitle()
        }
        return null
    }

    private fun extractFront(result: CyprusIdFrontRecognizer.Result) {
        add(IDENTITY_NUMBER, result.idNumber)
    }

    private fun extractBack(result: CyprusIdBackRecognizer.Result) {
        extract(result.mrzResult)
    }

}