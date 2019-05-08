package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.BaseTwoSideRecognition
import com.microblink.documentscanflow.recognition.ResultValidator
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.brunei.BruneiResidencePermitBackRecognizer
import com.microblink.entities.recognizers.blinkid.brunei.BruneiResidencePermitFrontRecognizer
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*


class BruneiResidencePermitRecognition: BaseTwoSideRecognition() {

    private val frontRecognizer by lazy { BruneiResidencePermitFrontRecognizer() }
    private val backRecognizer by lazy { BruneiResidencePermitBackRecognizer() }

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

    private fun extractFront(result: BruneiResidencePermitFrontRecognizer.Result) {
        add(FULL_NAME, result.fullName)
        add(DOCUMENT_NUMBER, result.documentNumber)
        add(DATE_OF_BIRTH, result.dateOfBirth)
        add(PLACE_OF_BIRTH, result.placeOfBirth)
        add(SEX, result.sex)
    }

    private fun extractBack(result: BruneiResidencePermitBackRecognizer.Result) {
        extractMrzResult(result.mrzResult)
        add(DATE_OF_ISSUE, result.dateOfIssue)
        add(ADDRESS, result.address)
        add(RACE, result.race)
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

    override fun getSingleSideRecognizers(): List<Recognizer<*>> = listOf(frontRecognizer, backRecognizer)


}