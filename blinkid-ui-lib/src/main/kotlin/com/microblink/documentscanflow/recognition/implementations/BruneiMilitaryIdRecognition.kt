package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.TwoSideRecognition
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.entities.recognizers.blinkid.brunei.BruneiMilitaryIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.brunei.BruneiMilitaryIdFrontRecognizer

class BruneiMilitaryIdRecognition: TwoSideRecognition<BruneiMilitaryIdFrontRecognizer.Result, BruneiMilitaryIdBackRecognizer.Result>() {

    override val frontRecognizer by lazy { BruneiMilitaryIdFrontRecognizer() }
    override val backRecognizer by lazy { BruneiMilitaryIdBackRecognizer() }

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
        add(DATE_OF_BIRTH, result.dateOfBirth)
        add(RANK, result.rank)
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

}