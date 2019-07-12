package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.recognition.SingleSideWithId1CardDetectorRecognition
import com.microblink.entities.recognizers.blinkid.malaysia.MalaysiaMyTenteraFrontRecognizer

class MalaysiaTenteraRecognition : SingleSideWithId1CardDetectorRecognition<MalaysiaMyTenteraFrontRecognizer.Result>() {

    override val recognizer by lazy { MalaysiaMyTenteraFrontRecognizer() }

    override fun extractData(result: MalaysiaMyTenteraFrontRecognizer.Result): String? {
        add(FULL_NAME, result.fullName)
        add(ARMY_NUMBER, result.armyNumber)
        add(ADDRESS, result.fullAddress)
        add(DATE_OF_BIRTH, result.birthDate)
        add(SEX, result.sex)
        add(RELIGION, result.religion)
        add(NRIC_NUMBER, result.nric)

        return result.fullName
    }
    
}
