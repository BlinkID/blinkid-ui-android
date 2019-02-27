package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.buildId1CardDetectorRecognizer
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.malaysia.MalaysiaMyTenteraFrontRecognizer

class MalaysiaTenteraRecognition : BaseRecognition() {

    private val recognizerFront by lazy { MalaysiaMyTenteraFrontRecognizer() }
    private val recognizerBack by lazy { buildId1CardDetectorRecognizer() }

    override fun getSingleSideRecognizers(): List<Recognizer<*, *>> {
        return listOf(recognizerFront, recognizerBack)
    }

    override fun extractData(): String? {
        val result = recognizerFront.result
        if (result.isEmpty()) {
            return null
        }

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
