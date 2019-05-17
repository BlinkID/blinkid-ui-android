package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.TwoSideRecognition
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.entities.recognizers.blinkid.brunei.BruneiTemporaryResidencePermitBackRecognizer
import com.microblink.entities.recognizers.blinkid.brunei.BruneiTemporaryResidencePermitFrontRecognizer

class BruneiTemporaryResidencePermitRecognition :
    TwoSideRecognition<BruneiTemporaryResidencePermitFrontRecognizer.Result, BruneiTemporaryResidencePermitBackRecognizer.Result>() {

    override val frontRecognizer by lazy { BruneiTemporaryResidencePermitFrontRecognizer() }
    override val backRecognizer by lazy { BruneiTemporaryResidencePermitBackRecognizer() }

    override fun extractFields() {
        if (frontResult.isNotEmpty()) {
            extractFront(frontResult)
        }
        if (backResult.isNotEmpty()) {
            extractBack(backResult)
        }
    }

    private fun extractFront(result: BruneiTemporaryResidencePermitFrontRecognizer.Result) {
        add(FULL_NAME, result.fullName)
        add(DOCUMENT_NUMBER, result.documentNumber)
        add(DATE_OF_BIRTH, result.dateOfBirth)
        add(PLACE_OF_BIRTH, result.placeOfBirth)
        add(SEX, result.sex)
        add(ADDRESS, result.address)
    }

    private fun extractBack(result: BruneiTemporaryResidencePermitBackRecognizer.Result) {
        extractMrzResult(result.mrzResult)
        add(DATE_OF_ISSUE, result.dateOfIssue)
        add(PASSPORT_NUMBER, result.passportNumber)
        add(EMPLOYER_ADDRESS, result.address)
    }

    override fun getResultTitle(): String? {
        if (backResult.isNotEmpty()) {
            return buildMrtdTitle(backResult.mrzResult)
        }
        if (frontResult.isNotEmpty()) {
            return frontResult.fullName
        }
        return null
    }

}