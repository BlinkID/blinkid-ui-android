package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.buildTitle
import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.TwoSideRecognition
import com.microblink.documentscanflow.recognition.extract
import com.microblink.entities.recognizers.blinkid.brunei.BruneiResidencePermitBackRecognizer
import com.microblink.entities.recognizers.blinkid.brunei.BruneiResidencePermitFrontRecognizer
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*


class BruneiResidencePermitRecognition :
    TwoSideRecognition<BruneiResidencePermitFrontRecognizer.Result, BruneiResidencePermitBackRecognizer.Result>() {

    override val frontRecognizer by lazy { BruneiResidencePermitFrontRecognizer() }
    override val backRecognizer by lazy { BruneiResidencePermitBackRecognizer() }

    override fun extractFields() {
        if (frontResult.isNotEmpty()) {
            extractFront(frontResult)
        }
        if (backResult.isNotEmpty()) {
            extractBack(backResult)
        }
    }

    private fun extractFront(result: BruneiResidencePermitFrontRecognizer.Result) {
        add(FULL_NAME, result.fullName)
        add(DOCUMENT_NUMBER, result.documentNumber)
        add(DATE_OF_BIRTH, result.dateOfBirth)
        add(PLACE_OF_BIRTH, result.placeOfBirth)
        add(SEX, result.sex)
    }

    private fun extractBack(result: BruneiResidencePermitBackRecognizer.Result) {
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