package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.recognition.SingleSideWithId1CardDetectorRecognition
import com.microblink.entities.recognizers.blinkid.mexico.MexicoVoterIdFrontRecognizer

class MexicoIdRecognition : SingleSideWithId1CardDetectorRecognition<MexicoVoterIdFrontRecognizer.Result>() {

    override val recognizer by lazy { MexicoVoterIdFrontRecognizer() }

    override fun extractData(result: MexicoVoterIdFrontRecognizer.Result): String? {
        add(FULL_NAME, result.fullName)
        add(ADDRESS, result.address)
        add(CURP, result.curp)
        add(DATE_OF_BIRTH, result.dateOfBirth)
        add(SEX, result.sex)

        return result.fullName
    }

}