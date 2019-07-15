package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.ResultValidator
import com.microblink.documentscanflow.recognition.TwoSideRecognition
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.entities.recognizers.blinkid.malaysia.MalaysiaMyKadBackRecognizer
import com.microblink.entities.recognizers.blinkid.malaysia.MalaysiaMyKadFrontRecognizer

class MalaysiaMyKadRecognition
    : TwoSideRecognition<MalaysiaMyKadFrontRecognizer.Result, MalaysiaMyKadBackRecognizer.Result>() {

    override val frontRecognizer by lazy { MalaysiaMyKadFrontRecognizer() }
    override val backRecognizer by lazy { MalaysiaMyKadBackRecognizer() }

    override fun createValidator(): ResultValidator {
        return ResultValidator()
                .match(frontResult.nric, backResult.nric)
                .match(frontResult.birthDate.date, backResult.dateOfBirth.date)
    }

    override fun extractFields() {
        if (frontResult.isNotEmpty()) {
            extractFrontSide()
        }
        if (backResult.isNotEmpty()) {
            extractBackSide()
        }
    }

    override fun getResultTitle(): String? {
        return when {
            frontResult.isNotEmpty() -> frontResult.fullName
            backResult.isNotEmpty() -> backResult.nric
            else -> null
        }
    }

    private fun extractFrontSide() {
        add(FULL_NAME, frontResult.fullName)
        add(NRIC_NUMBER, frontResult.nric)
        add(DATE_OF_BIRTH, frontResult.birthDate)
        add(ADDRESS, frontResult.fullAddress)
        add(SEX, frontResult.sex)
        add(RELIGION, frontResult.religion)
    }

    private fun extractBackSide() {
        add(NRIC_NUMBER, backResult.nric)
        add(DATE_OF_BIRTH, backResult.dateOfBirth)
    }

}
