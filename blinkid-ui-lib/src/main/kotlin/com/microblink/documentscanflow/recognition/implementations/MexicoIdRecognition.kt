package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.buildId1CardDetectorRecognizer
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.mexico.MexicoVoterIdFrontRecognizer

class MexicoIdRecognition : BaseRecognition() {

    val frontRecognizer by lazy { MexicoVoterIdFrontRecognizer() }
    val backRecognizer by lazy { buildId1CardDetectorRecognizer() }

    override fun extractData(): String? {
        val result = frontRecognizer.result
        if (result.isEmpty()) {
            return null
        }

        add(FULL_NAME, result.fullName)
        add(ADDRESS, result.address)
        add(CURP, result.curp)
        add(DATE_OF_BIRTH, result.dateOfBirth)
        add(SEX, result.sex)

        return result.fullName
    }

    override fun getSingleSideRecognizers() = listOf<Recognizer<*>>(frontRecognizer, backRecognizer)

}