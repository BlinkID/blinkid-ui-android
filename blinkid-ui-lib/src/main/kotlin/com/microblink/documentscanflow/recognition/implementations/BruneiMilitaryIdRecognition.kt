package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.BaseTwoSideRecognition
import com.microblink.documentscanflow.recognition.ResultValidator
import com.microblink.documentscanflow.recognition.resultentry.ResultKey
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.brunei.BruneiMilitaryIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.brunei.BruneiMilitaryIdFrontRecognizer

class BruneiMilitaryIdRecognition: BaseTwoSideRecognition() {

    private val frontRecognizer by lazy { BruneiMilitaryIdFrontRecognizer() }
    private val backRecognizer by lazy { BruneiMilitaryIdBackRecognizer() }

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

    private fun extractFront(result: BruneiMilitaryIdFrontRecognizer.Result) {
        add(FULL_NAME, result.fullName)
        add(ResultKey.DATE_OF_BIRTH, result.dateOfBirth)
        add(ResultKey.RANK, result.rank)
    }

    private fun extractBack(result: BruneiMilitaryIdBackRecognizer.Result) {
        add(ARMY_NUMBER, result.armyNumber)
        add(DATE_OF_ISSUE, result.dateOfIssue)
        addDateOfExpiry(result.dateOfExpiry.date)
    }

    override fun getResultTitle(): String? {
        if (frontResult.isNotEmpty()) {
            return frontResult.fullName
        }
        return null
    }

    override fun getSingleSideRecognizers(): List<Recognizer<*, *>> = listOf(frontRecognizer, backRecognizer)


}