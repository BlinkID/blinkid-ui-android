package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.buildTitle
import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.TwoSideRecognition
import com.microblink.documentscanflow.recognition.extract
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.entities.recognizers.blinkid.brunei.BruneiIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.brunei.BruneiIdFrontRecognizer

class BruneiIdRecognition: TwoSideRecognition<BruneiIdFrontRecognizer.Result, BruneiIdBackRecognizer.Result>() {

    override val frontRecognizer by lazy { BruneiIdFrontRecognizer() }
    override val backRecognizer by lazy { BruneiIdBackRecognizer() }

    override fun extractFields() {
        if (frontResult.isNotEmpty()) {
            extractFront(frontResult)
        }
        if (backResult.isNotEmpty()) {
            extractBack(backResult)
        }
    }

    private fun extractFront(result: BruneiIdFrontRecognizer.Result) {
        add(FULL_NAME, result.fullName)
        add(DOCUMENT_NUMBER, result.documentNumber)
        add(DATE_OF_BIRTH, result.dateOfBirth)
        add(PLACE_OF_BIRTH, result.placeOfBirth)
        add(SEX, result.sex)
    }

    private fun extractBack(result: BruneiIdBackRecognizer.Result) {
        extract(result.mrzResult)
        add(DATE_OF_ISSUE, result.dateOfIssue)
        add(ADDRESS, result.address)
        add(RACE, result.race)
    }

    override fun getResultTitle(): String? {
        if (backResult.isNotEmpty()) {
            return backResult.mrzResult.buildTitle()
        }
        if (frontResult.isNotEmpty()) {
            return frontResult.fullName
        }
        return null
    }

}