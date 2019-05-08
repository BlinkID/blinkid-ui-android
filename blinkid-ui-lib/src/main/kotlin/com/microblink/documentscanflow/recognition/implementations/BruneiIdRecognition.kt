package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.BaseTwoSideRecognition
import com.microblink.documentscanflow.recognition.ResultValidator
import com.microblink.documentscanflow.recognition.resultentry.ResultKey
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.brunei.BruneiIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.brunei.BruneiIdFrontRecognizer

class BruneiIdRecognition: BaseTwoSideRecognition() {

    private val frontRecognizer by lazy { BruneiIdFrontRecognizer() }
    private val backRecognizer by lazy { BruneiIdBackRecognizer() }

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

    private fun extractFront(result: BruneiIdFrontRecognizer.Result) {
        add(FULL_NAME, result.fullName)
        add(DOCUMENT_NUMBER, result.documentNumber)
        add(ResultKey.DATE_OF_BIRTH, result.dateOfBirth)
        add(ResultKey.PLACE_OF_BIRTH, result.placeOfBirth)
        add(ResultKey.SEX, result.sex)
    }

    private fun extractBack(result: BruneiIdBackRecognizer.Result) {
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