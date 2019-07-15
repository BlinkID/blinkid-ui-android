package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.recognition.SingleSideWithId1CardDetectorRecognition
import com.microblink.entities.recognizers.blinkid.malaysia.MalaysiaMyPrFrontRecognizer

class MalaysiaPrRecognition: SingleSideWithId1CardDetectorRecognition<MalaysiaMyPrFrontRecognizer.Result>() {

    override val recognizer by lazy { MalaysiaMyPrFrontRecognizer() }

    override fun extractData(result: MalaysiaMyPrFrontRecognizer.Result): String? {
        add(FULL_NAME, result.fullName)
        add(DATE_OF_BIRTH, result.birthDate)
        add(SEX, result.sex)
        add(RELIGION, result.religion)
        add(NRIC_NUMBER, result.nric)
        add(ADDRESS, result.fullAddress)
        return result.fullName
    }

}