package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.BaseTwoSideRecognition
import com.microblink.documentscanflow.recognition.ResultValidator
import com.microblink.documentscanflow.recognition.TwoSideRecognition
import com.microblink.entities.recognizers.Recognizer
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

    override fun extractData(frontResult: MalaysiaMyKadFrontRecognizer.Result, backResult: MalaysiaMyKadBackRecognizer.Result): String? {
        var title: String? = null
        if (frontResult.isNotEmpty()) {
            extractFrontSide()
            title = frontResult.fullName
        }
        if (backResult.isNotEmpty()) {
            extractBackSide()
            if (title == null) title = backResult.nric
        }
        return title
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
